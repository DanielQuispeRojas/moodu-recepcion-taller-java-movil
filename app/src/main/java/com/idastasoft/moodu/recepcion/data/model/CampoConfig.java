package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class CampoConfig {
    @SerializedName("placeHolder") private String placeHolder;
    private String tipo;
    private boolean visibilidad;
    private boolean requerido;

    public CampoConfig(String placeHolder, String tipo, boolean visibilidad, boolean requerido) {
        this.placeHolder = placeHolder;
        this.tipo = tipo;
        this.visibilidad = visibilidad;
        this.requerido = requerido;
    }

    public String getPlaceHolder() { return placeHolder; }
    public void setPlaceHolder(String placeHolder) { this.placeHolder = placeHolder; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public boolean isVisibilidad() { return visibilidad; }
    public void setVisibilidad(boolean visibilidad) { this.visibilidad = visibilidad; }
    public boolean isRequerido() { return requerido; }
    public void setRequerido(boolean requerido) { this.requerido = requerido; }
}
