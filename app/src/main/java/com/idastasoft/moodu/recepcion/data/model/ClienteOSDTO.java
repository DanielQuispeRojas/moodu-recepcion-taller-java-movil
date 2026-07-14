package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class ClienteOSDTO {
    private String tipo;
    private String nombres = "";
    private String apellidos = "";
    @SerializedName("razonSocial") private String razonSocial = "";
    @SerializedName("representanteLegal") private String representanteLegal = "";
    private String dni = "";
    private String ruc = "";
    private String correo = "";
    private String direccion = "";
    private String pais = "";
    private String ciudad = "";
    @SerializedName("codigoPostal") private String codigoPostal = "";

    public ClienteOSDTO(String tipo, String nombres, String apellidos, String razonSocial,
                        String representanteLegal, String dni, String ruc, String correo,
                        String direccion, String pais, String ciudad, String codigoPostal) {
        this.tipo = tipo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.razonSocial = razonSocial;
        this.representanteLegal = representanteLegal;
        this.dni = dni;
        this.ruc = ruc;
        this.correo = correo;
        this.direccion = direccion;
        this.pais = pais;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getRepresentanteLegal() { return representanteLegal; }
    public void setRepresentanteLegal(String representanteLegal) { this.representanteLegal = representanteLegal; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
}
