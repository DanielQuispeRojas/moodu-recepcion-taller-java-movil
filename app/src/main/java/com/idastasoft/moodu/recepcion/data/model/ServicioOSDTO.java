package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class ServicioOSDTO {
    private String sku;
    private String servicio;
    private int cantidad;
    @SerializedName("precioUnitario") private double precioUnitario;

    public ServicioOSDTO(String sku, String servicio, int cantidad, double precioUnitario) {
        this.sku = sku;
        this.servicio = servicio;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}
