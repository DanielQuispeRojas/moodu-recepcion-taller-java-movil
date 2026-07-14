package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class VehiculoOSDetail {
    private String tipo;
    private String marca;
    private String modelo;
    private String color;
    @SerializedName("anioFab") private Integer anioFab;
    @SerializedName("noPlaca") private String noPlaca;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Integer getAnioFab() { return anioFab; }
    public void setAnioFab(Integer v) { this.anioFab = v; }
    public String getNoPlaca() { return noPlaca; }
    public void setNoPlaca(String v) { this.noPlaca = v; }
}
