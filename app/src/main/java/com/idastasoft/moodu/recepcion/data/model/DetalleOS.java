package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class DetalleOS {
    @SerializedName("id") private Long id;
    private String sku;
    @SerializedName("servicio") private String servicio;
    private Integer cantidad;
    @SerializedName("precioUnitario") private Double precioUnitario;
    private Double total;
    private String estado;
    @SerializedName("fechaInicio") private String fechaInicio;
    @SerializedName("fechaFin") private String fechaFin;

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
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String v) { this.fechaInicio = v; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String v) { this.fechaFin = v; }
}
