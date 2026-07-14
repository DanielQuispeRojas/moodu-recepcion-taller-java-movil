package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CrearOSRequest {
    @SerializedName("cliente") private ClienteOSDTO cliente;
    @SerializedName("vehiculo") private VehiculoOSDTO vehiculo;
    @SerializedName("contacto") private List<String> contacto = new java.util.ArrayList<>();
    @SerializedName("servicios") private List<ServicioOSDTO> servicios = new java.util.ArrayList<>();
    @SerializedName("recepcion") private RecepcionVehiculoDTO recepcion;

    public CrearOSRequest(ClienteOSDTO cliente, VehiculoOSDTO vehiculo, List<String> contacto,
                          List<ServicioOSDTO> servicios, RecepcionVehiculoDTO recepcion) {
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.contacto = contacto;
        this.servicios = servicios;
        this.recepcion = recepcion;
    }

    public ClienteOSDTO getCliente() { return cliente; }
    public void setCliente(ClienteOSDTO v) { this.cliente = v; }
    public VehiculoOSDTO getVehiculo() { return vehiculo; }
    public void setVehiculo(VehiculoOSDTO v) { this.vehiculo = v; }
    public List<String> getContacto() { return contacto; }
    public void setContacto(List<String> v) { this.contacto = v; }
    public List<ServicioOSDTO> getServicios() { return servicios; }
    public void setServicios(List<ServicioOSDTO> v) { this.servicios = v; }
    public RecepcionVehiculoDTO getRecepcion() { return recepcion; }
    public void setRecepcion(RecepcionVehiculoDTO v) { this.recepcion = v; }
}
