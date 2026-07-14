package com.idastasoft.moodu.recepcion.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.idastasoft.moodu.recepcion.RecepcionApp;
import com.idastasoft.moodu.recepcion.ui.connect.ConnectActivity;

public class SessionUtil {

    public static void cerrarSesion(Context context) {
        RecepcionApp app = (RecepcionApp) context.getApplicationContext();
        app.getPrefs().logout();
        Intent intent = new Intent(context, ConnectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
