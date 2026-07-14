package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class UnirseCanalPayload {
    private boolean ok;
    @SerializedName("pendienteAprobacion") private Boolean pendienteAprobacion;
    @SerializedName("nombreCentral") private String nombreCentral;
    @SerializedName("codigoCanal") private String codigoCanal;
    private Integer puerto;

    public boolean isOk() { return ok; }
    public void setOk(boolean v) { this.ok = v; }
    public Boolean getPendienteAprobacion() { return pendienteAprobacion; }
    public void setPendienteAprobacion(Boolean v) { this.pendienteAprobacion = v; }
    public String getNombreCentral() { return nombreCentral; }
    public void setNombreCentral(String v) { this.nombreCentral = v; }
    public String getCodigoCanal() { return codigoCanal; }
    public void setCodigoCanal(String v) { this.codigoCanal = v; }
    public Integer getPuerto() { return puerto; }
    public void setPuerto(Integer v) { this.puerto = v; }
}
