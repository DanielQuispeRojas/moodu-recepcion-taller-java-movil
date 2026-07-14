package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class ContactoOSDetail {
    @SerializedName("numeroTelefono") private String numeroTelefono;

    public String getNumeroTelefono() { return numeroTelefono; }
    public void setNumeroTelefono(String v) { this.numeroTelefono = v; }
}
