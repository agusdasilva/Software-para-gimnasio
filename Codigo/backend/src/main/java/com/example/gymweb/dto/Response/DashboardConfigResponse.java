package com.example.gymweb.dto.Response;

import java.util.List;

public class DashboardConfigResponse {
    private List<String> noticias;
    private List<String> recordatorios;

    public List<String> getNoticias() {
        return noticias;
    }

    public void setNoticias(List<String> noticias) {
        this.noticias = noticias;
    }

    public List<String> getRecordatorios() {
        return recordatorios;
    }

    public void setRecordatorios(List<String> recordatorios) {
        this.recordatorios = recordatorios;
    }
}
