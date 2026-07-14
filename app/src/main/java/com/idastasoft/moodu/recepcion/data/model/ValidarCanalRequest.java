package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class ValidarCanalRequest {
    @SerializedName("codigoCanal") private String codigoCanal;
    private String clave;

    public ValidarCanalRequest(String codigoCanal, String clave) {
        this.codigoCanal = codigoCanal;
        this.clave = clave;
    }

    public String getCodigoCanal() { return codigoCanal; }
    public void setCodigoCanal(String v) { this.codigoCanal = v; }
    public String getClave() { return clave; }
    public void setClave(String v) { this.clave = v; }
}
