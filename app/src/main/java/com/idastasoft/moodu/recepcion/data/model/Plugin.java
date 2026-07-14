package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Plugin {
    private String nombre;
    @SerializedName("paqueteBase") private String paqueteBase;
    @SerializedName("subNombre") private String descripcion;
    private Boolean activo;
    private String version;
    private Map<String, Object> config;

    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }
    public String getPaqueteBase() { return paqueteBase; }
    public void setPaqueteBase(String v) { this.paqueteBase = v; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String v) { this.descripcion = v; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean v) { this.activo = v; }
    public String getVersion() { return version; }
    public void setVersion(String v) { this.version = v; }
    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> v) { this.config = v; }

    public String getCarpeta() {
        if (paqueteBase == null) return null;
        String[] parts = paqueteBase.split("\\.");
        return parts[parts.length - 1];
    }

    public String getNombreMostrar() {
        return nombre == null ? null : nombre.replace("-", " ").replace("_", " ");
    }
}
