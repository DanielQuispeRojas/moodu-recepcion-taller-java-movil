package com.idastasoft.moodu.recepcion.ui.recepcion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.idastasoft.moodu.recepcion.R;
import com.idastasoft.moodu.recepcion.RecepcionApp;
import com.idastasoft.moodu.recepcion.data.api.ApiClient;
import com.idastasoft.moodu.recepcion.data.model.OrdenServicio;
import com.idastasoft.moodu.recepcion.data.model.StatsOS;
import com.idastasoft.moodu.recepcion.databinding.FragmentRecepcionInicioBinding;
import com.idastasoft.moodu.recepcion.ui.main.MainActivity;
import com.idastasoft.moodu.recepcion.util.SessionUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;

public class RecepcionInicioFragment extends Fragment {

    private FragmentRecepcionInicioBinding binding;
    private OSAdapter adapter;
    private WebSocket eventoWs;
    private final OkHttpClient wsClient = new OkHttpClient();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable pollRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecepcionInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setToolbarTitle("Recepcion Taller");

        adapter = new OSAdapter(os -> {
            VerOSFragment fragment = VerOSFragment.newInstance(os.getId() != null ? os.getId() : 0L);
            requireParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) requireActivity()).setToolbarTitle("OS #" + (os.getNoOrdenServicio() != null ? os.getNoOrdenServicio() : os.getId()));
        });

        binding.rvOrdenes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvOrdenes.setAdapter(adapter);

        binding.btnNuevaOs.setOnClickListener(v -> {
            CrearOSFragment fragment = new CrearOSFragment();
            requireParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            ((MainActivity) requireActivity()).setToolbarTitle("Nueva Orden de Servicio");
        });

        binding.swipeRefresh.setOnRefreshListener(this::cargarDatos);

        cargarDatos();
        iniciarPolling();
        conectarEventosWS();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void iniciarPolling() {
        pollRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAdded() || !isResumed()) return;
                cargarDatos();
                mainHandler.postDelayed(this, 10000);
            }
        };
        mainHandler.postDelayed(pollRunnable, 10000);
    }

    private void cargarDatos() {
        binding.swipeRefresh.setRefreshing(true);
        binding.tvEmpty.setVisibility(View.GONE);

        new Thread(() -> {
            try {
                retrofit2.Response<com.idastasoft.moodu.recepcion.data.model.ApiResponse<StatsOS>> statsRes =
                        ApiClient.getRecepcionApi().obtenerStats().execute();
                if (statsRes.isSuccessful() && statsRes.body() != null && statsRes.body().isOk()) {
                    StatsOS stats = statsRes.body().getPayload();
                    requireActivity().runOnUiThread(() -> {
                        binding.tvStatsHoy.setText(String.valueOf(stats != null && stats.getOrdenesHoy() != null ? stats.getOrdenesHoy() : 0));
                        binding.tvStatsEspera.setText(String.valueOf(stats != null && stats.getEnEspera() != null ? stats.getEnEspera() : 0));
                        binding.tvStatsCurso.setText(String.valueOf(stats != null && stats.getEnCurso() != null ? stats.getEnCurso() : 0));
                        binding.tvStatsFinalizadas.setText(String.valueOf(stats != null && stats.getFinalizadas() != null ? stats.getFinalizadas() : 0));
                    });
                }
            } catch (Exception ignore) {
            }

            try {
                retrofit2.Response<List<OrdenServicio>> osRes = ApiClient.getRecepcionApi().listarOSEnCurso().execute();
                if (osRes.isSuccessful()) {
                    List<OrdenServicio> ordenes = osRes.body() != null ? osRes.body() : new ArrayList<>();
                    requireActivity().runOnUiThread(() -> {
                        adapter.submitList(ordenes);
                        binding.tvEmpty.setVisibility(ordenes.isEmpty() ? View.VISIBLE : View.GONE);
                    });
                } else if (osRes.code() == 401) {
                    requireActivity().runOnUiThread(() -> SessionUtil.cerrarSesion(requireContext()));
                } else {
                    requireActivity().runOnUiThread(() -> {
                        binding.tvEmpty.setText("Error: " + osRes.message());
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    binding.tvEmpty.setText("Error de conexion: " + e.getLocalizedMessage());
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                });
            }

            requireActivity().runOnUiThread(() -> binding.swipeRefresh.setRefreshing(false));
        }).start();
    }

    @Override
    public void onDestroyView() {
        if (eventoWs != null) {
            eventoWs.close(1000, null);
            eventoWs = null;
        }
        if (pollRunnable != null) mainHandler.removeCallbacks(pollRunnable);
        super.onDestroyView();
        binding = null;
    }

    private void conectarEventosWS() {
        String base = ((RecepcionApp) requireActivity().getApplication()).getPrefs().getApiBaseUrl();
        if (base == null) return;
        String wsBase = base.replace("http", "ws");
        if (wsBase.endsWith("/")) wsBase = wsBase.substring(0, wsBase.length() - 1);
        String url = wsBase + "/ws/eventos";
        Request request = new Request.Builder().url(url).build();
        eventoWs = wsClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    String tipo = new JSONObject(text).optString("tipo");
                    if ("ORDEN_CREADA".equals(tipo) || "ORDEN_ACTUALIZADA".equals(tipo)) {
                        requireActivity().runOnUiThread(() -> cargarDatos());
                    }
                } catch (Exception ignore) {
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                eventoWs = null;
                mainHandler.postDelayed(() -> {
                    if (isResumed()) conectarEventosWS();
                }, 5000);
            }
        });
    }

    private androidx.fragment.app.FragmentManager requireParentFragmentManager() {
        return requireActivity().getSupportFragmentManager();
    }
}
