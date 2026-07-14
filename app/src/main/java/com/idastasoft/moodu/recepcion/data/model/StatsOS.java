package com.idastasoft.moodu.recepcion.data.model;

import com.google.gson.annotations.SerializedName;

public class StatsOS {
    @SerializedName("ordenesHoy") private Integer ordenesHoy;
    @SerializedName("ordenesMes") private Integer ordenesMes;
    @SerializedName("enEspera") private Integer enEspera;
    @SerializedName("enCurso") private Integer enCurso;
    @SerializedName("finalizadas") private Integer finalizadas;

    public Integer getOrdenesHoy() { return ordenesHoy; }
    public void setOrdenesHoy(Integer v) { this.ordenesHoy = v; }
    public Integer getOrdenesMes() { return ordenesMes; }
    public void setOrdenesMes(Integer v) { this.ordenesMes = v; }
    public Integer getEnEspera() { return enEspera; }
    public void setEnEspera(Integer v) { this.enEspera = v; }
    public Integer getEnCurso() { return enCurso; }
    public void setEnCurso(Integer v) { this.enCurso = v; }
    public Integer getFinalizadas() { return finalizadas; }
    public void setFinalizadas(Integer v) { this.finalizadas = v; }
}
