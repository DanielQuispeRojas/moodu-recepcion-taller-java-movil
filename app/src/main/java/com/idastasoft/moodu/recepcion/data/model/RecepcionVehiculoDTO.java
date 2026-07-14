package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class RecepcionVehiculoDTO {
    private Integer kilometraje;
    @SerializedName("espejoIzquierdo") private boolean espejoIzquierdo;
    @SerializedName("espejoDerecho") private boolean espejoDerecho;
    private boolean lunas;
    private boolean radio;
    private boolean pantalla;
    private boolean encendedor;
    private boolean antena;
    @SerializedName("cargadorCel") private boolean cargadorCel;
    private boolean triangulos;
    private boolean cubresol;
    private boolean herramientas;
    private boolean gato;
    @SerializedName("llantaRepuesto") private boolean llantaRepuesto;
    private boolean faros;
    @SerializedName("tapaGasolina") private boolean tapaGasolina;
    private boolean placas;
    private boolean tapetes;
    private boolean extintor;
    @SerializedName("llaveTuercas") private boolean llaveTuercas;
    private String otros = "";

    public RecepcionVehiculoDTO() {}

    public RecepcionVehiculoDTO(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

    public Integer getKilometraje() { return kilometraje; }
    public void setKilometraje(Integer kilometraje) { this.kilometraje = kilometraje; }
    public boolean isEspejoIzquierdo() { return espejoIzquierdo; }
    public void setEspejoIzquierdo(boolean v) { this.espejoIzquierdo = v; }
    public boolean isEspejoDerecho() { return espejoDerecho; }
    public void setEspejoDerecho(boolean v) { this.espejoDerecho = v; }
    public boolean isLunas() { return lunas; }
    public void setLunas(boolean v) { this.lunas = v; }
    public boolean isRadio() { return radio; }
    public void setRadio(boolean v) { this.radio = v; }
    public boolean isPantalla() { return pantalla; }
    public void setPantalla(boolean v) { this.pantalla = v; }
    public boolean isEncendedor() { return encendedor; }
    public void setEncendedor(boolean v) { this.encendedor = v; }
    public boolean isAntena() { return antena; }
    public void setAntena(boolean v) { this.antena = v; }
    public boolean isCargadorCel() { return cargadorCel; }
    public void setCargadorCel(boolean v) { this.cargadorCel = v; }
    public boolean isTriangulos() { return triangulos; }
    public void setTriangulos(boolean v) { this.triangulos = v; }
    public boolean isCubresol() { return cubresol; }
    public void setCubresol(boolean v) { this.cubresol = v; }
    public boolean isHerramientas() { return herramientas; }
    public void setHerramientas(boolean v) { this.herramientas = v; }
    public boolean isGato() { return gato; }
    public void setGato(boolean v) { this.gato = v; }
    public boolean isLlantaRepuesto() { return llantaRepuesto; }
    public void setLlantaRepuesto(boolean v) { this.llantaRepuesto = v; }
    public boolean isFaros() { return faros; }
    public void setFaros(boolean v) { this.faros = v; }
    public boolean isTapaGasolina() { return tapaGasolina; }
    public void setTapaGasolina(boolean v) { this.tapaGasolina = v; }
    public boolean isPlacas() { return placas; }
    public void setPlacas(boolean v) { this.placas = v; }
    public boolean isTapetes() { return tapetes; }
    public void setTapetes(boolean v) { this.tapetes = v; }
    public boolean isExtintor() { return extintor; }
    public void setExtintor(boolean v) { this.extintor = v; }
    public boolean isLlaveTuercas() { return llaveTuercas; }
    public void setLlaveTuercas(boolean v) { this.llaveTuercas = v; }
    public String getOtros() { return otros; }
    public void setOtros(String otros) { this.otros = otros; }
}
