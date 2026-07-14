package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class RegistrarDispositivoRequest {
    private String codigo;
    private String nombre;
    private String tipo;
    private String ip;
    @SerializedName("infoAdicional") private String infoAdicional;

    public RegistrarDispositivoRequest(String codigo, String nombre, String tipo, String ip, String infoAdicional) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ip = ip;
        this.infoAdicional = infoAdicional;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String v) { this.codigo = v; }
    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }
    public String getTipo() { return tipo; }
    public void setTipo(String v) { this.tipo = v; }
    public String getIp() { return ip; }
    public void setIp(String v) { this.ip = v; }
    public String getInfoAdicional() { return infoAdicional; }
    public void setInfoAdicional(String v) { this.infoAdicional = v; }
}
