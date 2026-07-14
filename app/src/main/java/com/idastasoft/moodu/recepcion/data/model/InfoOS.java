package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InfoOS {
    @SerializedName("id") private Long id;
    @SerializedName("noOrdenServicio") private String noOrdenServicio;
    private String estado;
    private ClienteOSDetail cliente;
    @SerializedName("VehiculoCli") private VehiculoOSDetail vehiculoCli;
    private List<ContactoOSDetail> contactos;
    @SerializedName("detalleOS") private List<DetalleOSDetail> detalleOS;
    private RecepcionOSDetail recepcionVehiculo;
    @SerializedName("fechaEmision") private String fechaEmision;
    private Double totalGeneral;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNoOrdenServicio() { return noOrdenServicio; }
    public void setNoOrdenServicio(String v) { this.noOrdenServicio = v; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public ClienteOSDetail getCliente() { return cliente; }
    public void setCliente(ClienteOSDetail cliente) { this.cliente = cliente; }
    public VehiculoOSDetail getVehiculoCli() { return vehiculoCli; }
    public void setVehiculoCli(VehiculoOSDetail v) { this.vehiculoCli = v; }
    public List<ContactoOSDetail> getContactos() { return contactos; }
    public void setContactos(List<ContactoOSDetail> v) { this.contactos = v; }
    public List<DetalleOSDetail> getDetalleOS() { return detalleOS; }
    public void setDetalleOS(List<DetalleOSDetail> v) { this.detalleOS = v; }
    public RecepcionOSDetail getRecepcionVehiculo() { return recepcionVehiculo; }
    public void setRecepcionVehiculo(RecepcionOSDetail v) { this.recepcionVehiculo = v; }
    public String getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(String v) { this.fechaEmision = v; }
    public Double getTotalGeneral() { return totalGeneral; }
    public void setTotalGeneral(Double v) { this.totalGeneral = v; }
}
