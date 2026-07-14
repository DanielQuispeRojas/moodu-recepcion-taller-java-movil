package com.idastasoft.moodu.recepcion.data.model;

public class RegistrarDispositivoPayload {
    private long id;
    private String codigo;
    private String estado;
    private String mensaje;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String v) { this.codigo = v; }
    public String getEstado() { return estado; }
    public void setEstado(String v) { this.estado = v; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String v) { this.mensaje = v; }
}
