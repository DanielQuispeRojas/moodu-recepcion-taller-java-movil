package com.idastasoft.moodu.recepcion.data.model;

import java.util.Map;

public class CamposRequeridosResponse {
    private Map<String, CampoConfig> natural;
    private Map<String, CampoConfig> empresa;

    public Map<String, CampoConfig> getNatural() { return natural; }
    public void setNatural(Map<String, CampoConfig> natural) { this.natural = natural; }
    public Map<String, CampoConfig> getEmpresa() { return empresa; }
    public void setEmpresa(Map<String, CampoConfig> empresa) { this.empresa = empresa; }
}
