package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class DetalleOSDetail {
    @SerializedName("id") private Long id;
    private String sku;
    private String servicio;
    private Integer cantidad;
    @SerializedName("precioUnitario") private Double precioUnitario;
    private Double total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getServicio() { return servicio; }
    public void setServicio(String servicio) { this.servicio = servicio; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double v) { this.precioUnitario = v; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
