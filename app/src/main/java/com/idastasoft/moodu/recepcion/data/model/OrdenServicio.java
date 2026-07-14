package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdenServicio {
    @SerializedName("id") private Long id;
    @SerializedName(value = "noOrdenServicio", alternate = {"noOS"}) private String noOrdenServicio;
    @SerializedName("fechaEmision") private String fechaEmision;
    @SerializedName("fechaFin") private String fechaFin;
    @SerializedName(value = "estadoOS", alternate = {"estado"}) private String estadoOS;
    @SerializedName("clienteNombres") private String clienteNombres;
    @SerializedName("clienteApellidos") private String clienteApellidos;
    @SerializedName("clienteTipo") private String clienteTipo;
    @SerializedName(value = "vehiculoMarca", alternate = {"marca"}) private String vehiculoMarca;
    @SerializedName("vehiculoModelo") private String vehiculoModelo;
    @SerializedName(value = "vehiculoPlaca", alternate = {"placa"}) private String vehiculoPlaca;
    @SerializedName("vehiculoColor") private String vehiculoColor;
    @SerializedName("vehiculoAnio") private Integer vehiculoAnio;
    @SerializedName("totalGeneral") private Double totalGeneral;
    @SerializedName("cantidadServicios") private Integer cantidadServicios;
    @SerializedName("detalles") private List<DetalleOS> detalles;
    @SerializedName("contactos") private List<String> contactos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNoOrdenServicio() { return noOrdenServicio; }
    public void setNoOrdenServicio(String v) { this.noOrdenServicio = v; }
    public String getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(String v) { this.fechaEmision = v; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String v) { this.fechaFin = v; }
    public String getEstadoOS() { return estadoOS; }
    public void setEstadoOS(String v) { this.estadoOS = v; }
    public String getClienteNombres() { return clienteNombres; }
    public void setClienteNombres(String v) { this.clienteNombres = v; }
    public String getClienteApellidos() { return clienteApellidos; }
    public void setClienteApellidos(String v) { this.clienteApellidos = v; }
    public String getClienteTipo() { return clienteTipo; }
    public void setClienteTipo(String v) { this.clienteTipo = v; }
    public String getVehiculoMarca() { return vehiculoMarca; }
    public void setVehiculoMarca(String v) { this.vehiculoMarca = v; }
    public String getVehiculoModelo() { return vehiculoModelo; }
    public void setVehiculoModelo(String v) { this.vehiculoModelo = v; }
    public String getVehiculoPlaca() { return vehiculoPlaca; }
    public void setVehiculoPlaca(String v) { this.vehiculoPlaca = v; }
    public String getVehiculoColor() { return vehiculoColor; }
    public void setVehiculoColor(String v) { this.vehiculoColor = v; }
    public Integer getVehiculoAnio() { return vehiculoAnio; }
    public void setVehiculoAnio(Integer v) { this.vehiculoAnio = v; }
    public Double getTotalGeneral() { return totalGeneral; }
    public void setTotalGeneral(Double v) { this.totalGeneral = v; }
    public Integer getCantidadServicios() { return cantidadServicios; }
    public void setCantidadServicios(Integer v) { this.cantidadServicios = v; }
    public List<DetalleOS> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOS> v) { this.detalles = v; }
    public List<String> getContactos() { return contactos; }
    public void setContactos(List<String> v) { this.contactos = v; }
}
