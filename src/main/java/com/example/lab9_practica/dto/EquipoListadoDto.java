package com.example.lab9_practica.dto;

// DTO para la Pregunta 2: Mostrar solo Nombre del Equipo, Tag, Capitan, Juego, Pais y Estado (Activo / Inactivo)
public class EquipoListadoDto {

    private Integer id;
    private String nombreEquipo;
    private String tag;
    private String capitan;
    private String juego;
    private String pais;
    private String estado;

    public EquipoListadoDto() {
    }

    public EquipoListadoDto(Integer id, String nombreEquipo, String tag, String capitan, String juego, String pais, String estado) {
        this.id = id;
        this.nombreEquipo = nombreEquipo;
        this.tag = tag;
        this.capitan = capitan;
        this.juego = juego;
        this.pais = pais;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCapitan() {
        return capitan;
    }

    public void setCapitan(String capitan) {
        this.capitan = capitan;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
