package com.viviestu.viviestu_api.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

/// Entidad que representa las preferencias del usuario (universidad, presupuesto, etc.)
@Entity
@Table(name = "preferencias")
public class Preferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_preferencia")
    private Long idPreferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @Column(name = "universidad", nullable = false)
    private String universidad;

    @Column(name = "presupuesto", nullable = false)
    private Float presupuesto;

    @Column(name = "transporte", nullable = false)
    private String transporte;

    @Column(name = "tiempo_max", nullable = false)
    private Float tiempoMax;

    @Column(name = "seguridad", nullable = false)
    private String seguridad;

    public Long getIdPreferencia() { return idPreferencia; }
    public void setIdPreferencia(Long idPreferencia) { this.idPreferencia = idPreferencia; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getUniversidad() { return universidad; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }

    public Float getPresupuesto() { return presupuesto; }
    public void setPresupuesto(Float presupuesto) { this.presupuesto = presupuesto; }

    public String getTransporte() { return transporte; }
    public void setTransporte(String transporte) { this.transporte = transporte; }

    public Float getTiempoMax() { return tiempoMax; }
    public void setTiempoMax(Float tiempoMax) { this.tiempoMax = tiempoMax; }

    public String getSeguridad() { return seguridad; }
    public void setSeguridad(String seguridad) { this.seguridad = seguridad; }
}
