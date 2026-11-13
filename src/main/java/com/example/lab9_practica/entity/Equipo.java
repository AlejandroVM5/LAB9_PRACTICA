package com.example.lab9_practica.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_equipo", nullable = false, unique = true, length = 100)
    private String nombreEquipo;

    @Column(name = "tag", nullable = false, unique = true, length = 5)
    private String tag;

    @Column(name = "nombre_capitan", nullable = false, length = 100)
    private String nombreCapitan;

    @Column(name = "cantidad_jugadores", nullable = false)
    private Integer cantidadJugadores;

    @Column(name = "juego", nullable = false, length = 100)
    private String juego;

    @Column(name = "pais", nullable = false, length = 100)
    private String pais;

    @Column(name = "correo_contacto", nullable = false, length = 150)
    private String correoContacto;

    @Column(name = "telefono_contacto", nullable = false, length = 9)
    private String telefonoContacto;

    // El estado se registra automaticamente como "Activo" y al desactivar cambia a "Inactivo"
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    // Getters y Setters (Sin Lombok)
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

    public String getNombreCapitan() {
        return nombreCapitan;
    }

    public void setNombreCapitan(String nombreCapitan) {
        this.nombreCapitan = nombreCapitan;
    }

    public Integer getCantidadJugadores() {
        return cantidadJugadores;
    }

    public void setCantidadJugadores(Integer cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
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

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
