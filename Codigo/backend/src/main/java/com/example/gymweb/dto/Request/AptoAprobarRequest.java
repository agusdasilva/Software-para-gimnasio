package com.example.gymweb.dto.Request;

import java.time.LocalDate;

public class AptoAprobarRequest {
    private LocalDate fechaVencimiento;

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
