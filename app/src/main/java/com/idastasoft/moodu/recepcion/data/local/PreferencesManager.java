package com.idastasoft.moodu.recepcion.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class PreferencesManager {
    private final SharedPreferences prefs;

    public static final String DEFAULT_URL = "http://10.0.2.2:8080";

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_NOMBRE = "user_nombre";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_BASE_URL = "base_url";
    private static final String KEY_CENTRAL_URL = "central_url";
    private static final String KEY_CODIGO_CANAL = "codigo_canal";
    private static final String KEY_NOMBRE_CENTRAL = "nombre_central";
    private static final String KEY_DISPOSITIVO_CODIGO = "dispositivo_codigo";

    public PreferencesManager(Context context) {
        prefs = context.getSharedPreferences("moodu_recepcion", Context.MODE_PRIVATE);
    }

    public String getToken() { return prefs.getString(KEY_TOKEN, null); }
    public void setToken(String v) { prefs.edit().putString(KEY_TOKEN, v).apply(); }

    public String getUsername() { return prefs.getString(KEY_USERNAME, null); }
    public void setUsername(String v) { prefs.edit().putString(KEY_USERNAME, v).apply(); }

    public String getUserRole() { return prefs.getString(KEY_USER_ROLE, null); }
    public void setUserRole(String v) { prefs.edit().putString(KEY_USER_ROLE, v).apply(); }

    public String getUserNombreCompleto() { return prefs.getString(KEY_USER_NOMBRE, null); }
    public void setUserNombreCompleto(String v) { prefs.edit().putString(KEY_USER_NOMBRE, v).apply(); }

    public long getUserId() { return prefs.getLong(KEY_USER_ID, -1); }
    public void setUserId(long v) { prefs.edit().putLong(KEY_USER_ID, v).apply(); }

    public String getBaseUrl() { return prefs.getString(KEY_BASE_URL, DEFAULT_URL); }
    public void setBaseUrl(String v) { prefs.edit().putString(KEY_BASE_URL, v).apply(); }

    public String getCentralUrl() { return prefs.getString(KEY_CENTRAL_URL, null); }
    public void setCentralUrl(String v) { prefs.edit().putString(KEY_CENTRAL_URL, v).apply(); }

    public String getCodigoCanal() { return prefs.getString(KEY_CODIGO_CANAL, null); }
    public void setCodigoCanal(String v) { prefs.edit().putString(KEY_CODIGO_CANAL, v).apply(); }

    public String getNombreCentral() { return prefs.getString(KEY_NOMBRE_CENTRAL, null); }
    public void setNombreCentral(String v) { prefs.edit().putString(KEY_NOMBRE_CENTRAL, v).apply(); }

    public String getDispositivoCodigo() {
        String existing = prefs.getString(KEY_DISPOSITIVO_CODIGO, null);
        if (existing != null) return existing;
        String nuevo = UUID.randomUUID().toString();
        prefs.edit().putString(KEY_DISPOSITIVO_CODIGO, nuevo).apply();
        return nuevo;
    }

    public boolean isLoggedIn() { return getToken() != null; }
    public boolean isConectado() { return getCentralUrl() != null && getCodigoCanal() != null; }
    public String getApiBaseUrl() { return getCentralUrl() != null ? getCentralUrl() : getBaseUrl(); }

    public void logout() {
        prefs.edit()
                .remove(KEY_TOKEN)
                .remove(KEY_USERNAME)
                .remove(KEY_USER_ROLE)
                .remove(KEY_USER_NOMBRE)
                .remove(KEY_USER_ID)
                .apply();
    }

    public void saveLogin(String token, long userId, String username, String rol, String nombreCompleto) {
        setToken(token);
        setUserId(userId);
        setUsername(username);
        setUserRole(rol);
        setUserNombreCompleto(nombreCompleto);
    }

    public void saveConexion(String centralUrl, String codigoCanal, String nombreCentral) {
        setCentralUrl(centralUrl);
        setCodigoCanal(codigoCanal);
        setNombreCentral(nombreCentral);
    }

    public void clearConexion() {
        setCentralUrl(null);
        setCodigoCanal(null);
        setNombreCentral(null);
    }
}
