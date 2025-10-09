package com.viviestu.viviestu_api.dto;

import com.viviestu.viviestu_api.model.MedioTransporte;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SimulacionGastoRequest {

    @NotNull(message = "El monto de alquiler es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal alquilerMensual;

    @NotNull(message = "El medio de transporte es obligatorio")
    private MedioTransporte medioTransporte;

    private BigDecimal costoTransporteOverride;

    // Getters y setters
    public BigDecimal getAlquilerMensual() {
        return alquilerMensual;
    }

    public void setAlquilerMensual(BigDecimal alquilerMensual) {
        this.alquilerMensual = alquilerMensual;
    }

    public MedioTransporte getMedioTransporte() {
        return medioTransporte;
    }

    public void setMedioTransporte(MedioTransporte medioTransporte) {
        this.medioTransporte = medioTransporte;
    }

    public BigDecimal getCostoTransporteOverride() {
        return costoTransporteOverride;
    }

    public void setCostoTransporteOverride(BigDecimal costoTransporteOverride) {
        this.costoTransporteOverride = costoTransporteOverride;
    }
}
