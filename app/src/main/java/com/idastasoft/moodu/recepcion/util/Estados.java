package com.idastasoft.moodu.recepcion.util;

import com.idastasoft.moodu.recepcion.R;

public class Estados {

    public static String estadoTexto(String estado) {
        if (estado == null) return "Desconocido";
        switch (estado) {
            case "EN_ESPERA": return "En espera";
            case "EN_CURSO": return "En curso";
            case "PAUSADO": return "Pausado";
            case "FINALIZADO": return "Finalizado";
            case "CERRADO": return "Cerrado";
            case "ANULADO": return "Anulado";
            default: return estado;
        }
    }

    public static int estadoColor(String estado) {
        if (estado == null) estado = "";
        switch (estado) {
            case "EN_ESPERA": return R.color.estado_espera;
            case "EN_CURSO": return R.color.estado_curso;
            case "PAUSADO": return R.color.estado_pausado;
            case "FINALIZADO": return R.color.estado_finalizado;
            case "CERRADO": return R.color.estado_cerrado;
            case "ANULADO": return R.color.estado_anulado;
            default: return R.color.estado_espera;
        }
    }
}
