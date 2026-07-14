package com.idastasoft.moodu.recepcion.data.api;

import com.idastasoft.moodu.recepcion.data.local.PreferencesManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static PreferencesManager prefs;
    private static final Map<String, Object> cache = new ConcurrentHashMap<>();

    public static void init(PreferencesManager preferencesManager) {
        prefs = preferencesManager;
    }

    private static OkHttpClient buildClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            String token = prefs != null ? prefs.getToken() : null;
            String deviceCode = prefs != null ? prefs.getDispositivoCodigo() : null;
            Request.Builder builder = original.newBuilder();
            if (token != null && !token.isBlank()) {
                builder.addHeader("Authorization", "Bearer " + token);
            }
            if (deviceCode != null && !deviceCode.isBlank()) {
                builder.addHeader("X-Device-Code", deviceCode);
            }
            return chain.proceed(builder.build());
        };

        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logging)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private static <T> T createService(String baseUrl, Class<T> clazz) {
        String key = baseUrl + ":" + clazz.getSimpleName();
        Object cached = cache.get(key);
        if (cached != null) {
            return clazz.cast(cached);
        }
        String adjusted = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        Retrofit retro = new Retrofit.Builder()
                .baseUrl(adjusted)
                .client(buildClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        T service = retro.create(clazz);
        cache.put(key, service);
        return service;
    }

    private static String getCurrentBaseUrl() {
        if (prefs != null && prefs.getApiBaseUrl() != null) {
            return prefs.getApiBaseUrl();
        }
        return PreferencesManager.DEFAULT_URL;
    }

    public static AuthApi getAuthApi() { return createService(getCurrentBaseUrl(), AuthApi.class); }
    public static AuthApi getAuthApi(String url) { return createService(url, AuthApi.class); }
    public static SistemaApi getSistemaApi() { return createService(getCurrentBaseUrl(), SistemaApi.class); }
    public static SistemaApi getSistemaApi(String url) { return createService(url, SistemaApi.class); }
    public static CoreApi getCoreApi() { return createService(getCurrentBaseUrl(), CoreApi.class); }
    public static RecepcionApi getRecepcionApi() { return createService(getCurrentBaseUrl(), RecepcionApi.class); }
    public static ServicioApi getServicioApi() { return createService(getCurrentBaseUrl(), ServicioApi.class); }
}
