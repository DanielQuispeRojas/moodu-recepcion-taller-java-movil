package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class VehiculoOSDTO {
    private String tipo = "";
    private String marca = "";
    private String modelo = "";
    private String color = "";
    @SerializedName("anioFab") private Integer anioFab;
    @SerializedName("noPlaca") private String noPlaca = "";
    private Integer cilindrada;

    public VehiculoOSDTO(String tipo, String marca, String modelo, String color, Integer anioFab, String noPlaca) {
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.anioFab = anioFab;
        this.noPlaca = noPlaca;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Integer getAnioFab() { return anioFab; }
    public void setAnioFab(Integer anioFab) { this.anioFab = anioFab; }
    public String getNoPlaca() { return noPlaca; }
    public void setNoPlaca(String noPlaca) { this.noPlaca = noPlaca; }
    public Integer getCilindrada() { return cilindrada; }
    public void setCilindrada(Integer cilindrada) { this.cilindrada = cilindrada; }
}
