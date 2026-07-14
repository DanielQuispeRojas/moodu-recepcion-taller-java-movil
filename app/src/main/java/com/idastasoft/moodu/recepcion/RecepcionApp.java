package com.idastasoft.moodu.recepcion;

import android.app.Application;

import com.idastasoft.moodu.recepcion.data.api.ApiClient;
import com.idastasoft.moodu.recepcion.data.local.PreferencesManager;

public class RecepcionApp extends Application {

    private PreferencesManager prefs;

    public PreferencesManager getPrefs() {
        return prefs;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new PreferencesManager(this);
        ApiClient.init(prefs);
    }
}
