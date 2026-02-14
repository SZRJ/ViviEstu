package com.viviestu.viviestu_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/// Entidad Comentario: representa la experiencia de un usuario sobre una zona
@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_experiencia")
    private Integer idExperiencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona", nullable = false)
    private Zona zona;

    @Column(nullable = false, length = 2000)
    private String comentario;

    @Column
    private LocalDateTime fecha;

    public Comentario() {}

    /// Getters y setters
    public Integer getIdExperiencia() { return idExperiencia; }
    public void setIdExperiencia(Integer idExperiencia) { this.idExperiencia = idExperiencia; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
