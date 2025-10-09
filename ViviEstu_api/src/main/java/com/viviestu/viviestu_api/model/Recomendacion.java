package com.viviestu.viviestu_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recomendaciones",
        uniqueConstraints = @UniqueConstraint(name = "uk_usuario_zona", columnNames = {"id_usuario", "id_zona"}))
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_zona", nullable = false)
    private Integer idZona;

    public Recomendacion() {}

    public Recomendacion(Integer idUsuario, Integer idZona) {
        this.idUsuario = idUsuario;
        this.idZona = idZona;
    }

    public Integer getId() { return id; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Integer getIdZona() { return idZona; }
    public void setIdZona(Integer idZona) { this.idZona = idZona; }
}
