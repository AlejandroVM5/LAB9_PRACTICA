package com.example.lab9_practica.repository;

import com.example.lab9_practica.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {

    // Buscar por nombre de equipo (para validar que sea unico)
    Optional<Equipo> findByNombreEquipoIgnoreCase(String nombreEquipo);

    // Buscar por Tag (para validar unicidad y para el endpoint de consulta por Tag)
    Optional<Equipo> findByTagIgnoreCase(String tag);
}
