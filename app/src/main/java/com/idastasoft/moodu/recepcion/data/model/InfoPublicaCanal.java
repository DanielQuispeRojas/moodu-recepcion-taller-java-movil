package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class InfoPublicaCanal {
    @SerializedName("canalActivo") private Boolean canalActivo;
    @SerializedName("codigoCanal") private String codigoCanal;
    @SerializedName("nombreCentral") private String nombreCentral;
    @SerializedName("requiereClave") private Boolean requiereClave;

    public Boolean getCanalActivo() { return canalActivo; }
    public void setCanalActivo(Boolean v) { this.canalActivo = v; }
    public String getCodigoCanal() { return codigoCanal; }
    public void setCodigoCanal(String v) { this.codigoCanal = v; }
    public String getNombreCentral() { return nombreCentral; }
    public void setNombreCentral(String v) { this.nombreCentral = v; }
    public Boolean getRequiereClave() { return requiereClave; }
    public void setRequiereClave(Boolean v) { this.requiereClave = v; }
}
