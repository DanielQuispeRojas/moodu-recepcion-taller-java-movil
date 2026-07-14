package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class ClienteOSDetail {
    private String tipo;
    @SerializedName("nombre") private String nombre;
    private String apellidos;
    @SerializedName("razonSocial") private String razonSocial;
    private String dni;
    private String ruc;
    private String correo;
    private String direccion;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String nombreCompleto() {
        if ("EMPRESA".equals(tipo)) return razonSocial == null ? "" : razonSocial;
        return ((nombre == null ? "" : nombre) + " " + (apellidos == null ? "" : apellidos)).trim();
    }
}
