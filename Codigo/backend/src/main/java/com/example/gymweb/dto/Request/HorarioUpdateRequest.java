package com.example.gymweb.dto.Request;

import java.util.List;

public class HorarioUpdateRequest {
    private List<DiaHorarioRequest> dias;

    public List<DiaHorarioRequest> getDias() {
        return dias;
    }

    public void setDias(List<DiaHorarioRequest> dias) {
        this.dias = dias;
    }

    public static class DiaHorarioRequest {
        private Integer id;
        private String dia;
        private String horaApertura;
        private String horaCierre;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDia() {
            return dia;
        }

        public void setDia(String dia) {
            this.dia = dia;
        }

        public String getHoraApertura() {
            return horaApertura;
        }

        public void setHoraApertura(String horaApertura) {
            this.horaApertura = horaApertura;
        }

        public String getHoraCierre() {
            return horaCierre;
        }

        public void setHoraCierre(String horaCierre) {
            this.horaCierre = horaCierre;
        }
    }
}
