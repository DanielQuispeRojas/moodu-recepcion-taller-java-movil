package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("ok") private boolean ok;
    @SerializedName("mensaje") private String mensaje;
    @SerializedName("payload") private T payload;

    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public T getPayload() { return payload; }
    public void setPayload(T payload) { this.payload = payload; }
}
