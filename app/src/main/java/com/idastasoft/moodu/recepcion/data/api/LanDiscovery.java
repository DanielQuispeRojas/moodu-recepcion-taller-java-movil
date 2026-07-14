package com.idastasoft.moodu.recepcion.data.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.os.Build;

import com.google.gson.Gson;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LanDiscovery {

    public static class CanalEncontrado {
        public String ip;
        public String puerto;
        public String codigoCanal;
        public String nombreCentral;
        public boolean requiereClave;

        public CanalEncontrado(String ip, String puerto, String codigoCanal, String nombreCentral, boolean requiereClave) {
            this.ip = ip;
            this.puerto = puerto;
            this.codigoCanal = codigoCanal;
            this.nombreCentral = nombreCentral;
            this.requiereClave = requiereClave;
        }
    }

    private static class InfoPayload {
        Boolean canalActivo;
        String codigoCanal;
        String nombreCentral;
        Boolean requiereClave;
    }

    private static class InfoPublicaRaw {
        boolean ok;
        String mensaje;
        InfoPayload payload;
    }

    private static final Gson gson = new Gson();

    private static final OkHttpClient scanClient = new OkHttpClient.Builder()
            .connectTimeout(250, TimeUnit.MILLISECONDS)
            .readTimeout(250, TimeUnit.MILLISECONDS)
            .build();

    public static List<String> obtenerSubredes(Context context) {
        Set<String> subredes = new LinkedHashSet<>();
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.net.Network network = cm.getActiveNetwork();
                LinkProperties link = cm.getLinkProperties(network);
                if (link != null) {
                    for (android.net.LinkAddress la : link.getLinkAddresses()) {
                        java.net.InetAddress addr = la.getAddress();
                        if (addr instanceof Inet4Address && !addr.isLoopbackAddress() && !addr.isLinkLocalAddress()) {
                            String host = addr.getHostAddress();
                            if (host != null) {
                                String[] parts = host.split(Pattern.quote("."));
                                if (parts.length == 4) {
                                    subredes.add(parts[0] + "." + parts[1] + "." + parts[2]);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignore) {
        }
        if (subredes.isEmpty()) {
            subredes.add("192.168.1");
            subredes.add("192.168.0");
        }
        return new ArrayList<>(subredes);
    }

    public static List<CanalEncontrado> escanear(Context context, String puerto) throws Exception {
        final String port = (puerto == null || puerto.isBlank()) ? "8080" : puerto;
        List<String> subredes = obtenerSubredes(context);

        List<Callable<CanalEncontrado>> tareas = new ArrayList<>();
        for (String sub : subredes) {
            for (int i = 1; i <= 254; i++) {
                final String ip = sub + "." + i;
                final String url = "http://" + ip + ":" + port + "/sistema/canal/info-publica";
                tareas.add(() -> {
                    try {
                        Request req = new Request.Builder().url(url).get().build();
                        Response resp = scanClient.newCall(req).execute();
                        if (resp.isSuccessful()) {
                            String body = resp.body() != null ? resp.body().string() : null;
                            InfoPublicaRaw raw = gson.fromJson(body, InfoPublicaRaw.class);
                            InfoPayload p = raw != null ? raw.payload : null;
                            if (raw != null && raw.ok && p != null
                                    && Boolean.TRUE.equals(p.canalActivo)
                                    && p.codigoCanal != null && !p.codigoCanal.isBlank()) {
                                return new CanalEncontrado(ip, port, p.codigoCanal,
                                        p.nombreCentral != null ? p.nombreCentral : p.codigoCanal,
                                        Boolean.TRUE.equals(p.requiereClave));
                            }
                        }
                    } catch (Exception ignore) {
                    }
                    return null;
                });
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(Math.min(50, Math.max(1, tareas.size())));
        List<CanalEncontrado> resultado = new ArrayList<>();
        try {
            List<Future<CanalEncontrado>> futures = executor.invokeAll(tareas);
            for (Future<CanalEncontrado> f : futures) {
                CanalEncontrado c = f.get();
                if (c != null) resultado.add(c);
            }
        } finally {
            executor.shutdownNow();
        }

        Set<String> seen = new LinkedHashSet<>();
        List<CanalEncontrado> unicos = new ArrayList<>();
        for (CanalEncontrado c : resultado) {
            if (seen.add(c.ip)) unicos.add(c);
        }
        return unicos;
    }
}
