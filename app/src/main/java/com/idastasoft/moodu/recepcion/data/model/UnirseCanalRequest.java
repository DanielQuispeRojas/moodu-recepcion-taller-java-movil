package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class UnirseCanalRequest {
    @SerializedName("codigoCanal") private String codigoCanal;
    private String clave;
    private String username;
    private String password;

    public UnirseCanalRequest(String codigoCanal, String clave, String username, String password) {
        this.codigoCanal = codigoCanal;
        this.clave = clave;
        this.username = username;
        this.password = password;
    }

    public String getCodigoCanal() { return codigoCanal; }
    public void setCodigoCanal(String v) { this.codigoCanal = v; }
    public String getClave() { return clave; }
    public void setClave(String v) { this.clave = v; }
    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }
    public String getPassword() { return password; }
    public void setPassword(String v) { this.password = v; }
}
