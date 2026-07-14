package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginPayload {
    private String token;
    @SerializedName("userId") private long userId;
    private String username;
    private String rol;
    @SerializedName("nombreCompleto") private String nombreCompleto;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
}
