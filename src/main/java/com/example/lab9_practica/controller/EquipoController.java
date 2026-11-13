package com.example.lab9_practica.controller;

import com.example.lab9_practica.dto.EquipoListadoDto;
import com.example.lab9_practica.entity.Equipo;
import com.example.lab9_practica.exception.EquipoException;
import com.example.lab9_practica.repository.EquipoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/equipos")
public class EquipoController {

    private final EquipoRepository equipoRepository;

    public EquipoController(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    // =========================================================================
    // PREGUNTA 1 (6 puntos): Registrar equipos participantes en el sistema
    // =========================================================================
    @PostMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> registrarEquipo(@RequestBody Equipo equipo) {

        // Validar campos obligatorios de registro
        if (equipo.getNombreEquipo() == null || equipo.getNombreEquipo().isBlank()) {
            throw new EquipoException("El nombre del equipo es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (equipo.getTag() == null || equipo.getTag().isBlank()) {
            throw new EquipoException("El tag del equipo es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (equipo.getNombreCapitan() == null || equipo.getNombreCapitan().isBlank()) {
            throw new EquipoException("El nombre del capitán es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (equipo.getCantidadJugadores() == null) {
            throw new EquipoException("La cantidad de jugadores es obligatoria", HttpStatus.BAD_REQUEST);
        }
        if (equipo.getJuego() == null || equipo.getJuego().isBlank()) {
            throw new EquipoException("El juego es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (equipo.getPais() == null || equipo.getPais().isBlank()) {
            throw new EquipoException("El país es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (equipo.getCorreoContacto() == null || equipo.getCorreoContacto().isBlank()) {
            throw new EquipoException("El correo de contacto es obligatorio", HttpStatus.BAD_REQUEST);
        }
        if (equipo.getTelefonoContacto() == null || equipo.getTelefonoContacto().isBlank()) {
            throw new EquipoException("El teléfono de contacto es obligatorio", HttpStatus.BAD_REQUEST);
        }

        // Aplicar todas las validaciones de formato, rangos y unicidad (null en idActual porque es nuevo)
        validarReglasEquipo(equipo, null);

        // Limpiar cadenas y registrar automaticamente el estado como "Activo"
        equipo.setId(null);
        equipo.setNombreEquipo(equipo.getNombreEquipo().trim());
        equipo.setTag(equipo.getTag().trim().toUpperCase());
        equipo.setNombreCapitan(equipo.getNombreCapitan().trim());
        equipo.setJuego(equipo.getJuego().trim());
        equipo.setPais(equipo.getPais().trim());
        equipo.setCorreoContacto(equipo.getCorreoContacto().trim());
        equipo.setTelefonoContacto(equipo.getTelefonoContacto().trim());
        equipo.setEstado("Activo");

        Equipo guardado = equipoRepository.save(equipo);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", "success");
        response.put("mensaje", "Equipo registrado exitosamente");
        response.put("equipo", guardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201 Created
    }

    // =========================================================================
    // PREGUNTA 2 (5 puntos): Obtener todos los equipos y consultar por Tag
    // =========================================================================

    // 2.1 Obtener todos los equipos mostrando: Nombre del Equipo, Tag, Capitan, Juego, Pais, Estado
    @GetMapping({"", "/"})
    public ResponseEntity<List<EquipoListadoDto>> listarEquipos() {
        List<Equipo> lista = equipoRepository.findAll();
        List<EquipoListadoDto> respuesta = new ArrayList<>();

        for (Equipo e : lista) {
            String estadoFormateado = "Activo".equalsIgnoreCase(e.getEstado()) ? "Activo" : "Inactivo";
            respuesta.add(new EquipoListadoDto(
                    e.getId(),
                    e.getNombreEquipo(),
                    e.getTag(),
                    e.getNombreCapitan(),
                    e.getJuego(),
                    e.getPais(),
                    estadoFormateado
            ));
        }
        return ResponseEntity.ok(respuesta); // HTTP 200 OK
    }

    // 2.2 Consultar un equipo utilizando su Tag (soportando /api/equipos/tag/{tag} y /api/equipos/{tag})
    @GetMapping({"/tag/{tag}", "/{tag}"})
    public ResponseEntity<Map<String, Object>> obtenerEquipoPorTag(@PathVariable("tag") String tag) {
        Optional<Equipo> opt = equipoRepository.findByTagIgnoreCase(tag.trim());
        if (opt.isEmpty()) {
            throw new EquipoException("Equipo no encontrado con el tag: " + tag, HttpStatus.NOT_FOUND); // HTTP 404
        }

        Equipo e = opt.get();
        e.setEstado("Activo".equalsIgnoreCase(e.getEstado()) ? "Activo" : "Inactivo");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", "success");
        response.put("equipo", e);
        return ResponseEntity.ok(response); // HTTP 200 OK
    }

    // =========================================================================
    // PREGUNTA 3 (4 puntos): Actualización parcial de equipos
    // Solo se actualizan los campos enviados por el cliente y se aplican las mismas validaciones
    // =========================================================================
    @PutMapping({"", "/", "/{id}"})
    public ResponseEntity<Map<String, Object>> actualizarEquipo(
            @PathVariable(value = "id", required = false) Integer idPath,
            @RequestBody Equipo datosEnviados) {

        Integer id = (idPath != null) ? idPath : datosEnviados.getId();
        if (id == null || id <= 0) {
            throw new EquipoException("Debe proporcionar un ID válido para actualizar el equipo", HttpStatus.BAD_REQUEST);
        }

        Optional<Equipo> opt = equipoRepository.findById(id);
        if (opt.isEmpty()) {
            throw new EquipoException("Equipo no encontrado con el ID: " + id, HttpStatus.NOT_FOUND); // HTTP 404
        }

        Equipo equipoDb = opt.get();

        // Validar solo los campos que el cliente haya enviado en el JSON
        validarReglasEquipo(datosEnviados, id);

        // Actualizacion parcial (como en Clase 9.2 Slide 51): solo reemplazamos si el campo != null
        if (datosEnviados.getNombreEquipo() != null) {
            if (datosEnviados.getNombreEquipo().isBlank()) {
                throw new EquipoException("El nombre del equipo no puede estar vacío", HttpStatus.BAD_REQUEST);
            }
            equipoDb.setNombreEquipo(datosEnviados.getNombreEquipo().trim());
        }
        if (datosEnviados.getTag() != null) {
            equipoDb.setTag(datosEnviados.getTag().trim().toUpperCase());
        }
        if (datosEnviados.getNombreCapitan() != null) {
            if (datosEnviados.getNombreCapitan().isBlank()) {
                throw new EquipoException("El nombre del capitán no puede estar vacío", HttpStatus.BAD_REQUEST);
            }
            equipoDb.setNombreCapitan(datosEnviados.getNombreCapitan().trim());
        }
        if (datosEnviados.getCantidadJugadores() != null) {
            equipoDb.setCantidadJugadores(datosEnviados.getCantidadJugadores());
        }
        if (datosEnviados.getJuego() != null) {
            if (datosEnviados.getJuego().isBlank()) {
                throw new EquipoException("El juego no puede estar vacío", HttpStatus.BAD_REQUEST);
            }
            equipoDb.setJuego(datosEnviados.getJuego().trim());
        }
        if (datosEnviados.getPais() != null) {
            if (datosEnviados.getPais().isBlank()) {
                throw new EquipoException("El país no puede estar vacío", HttpStatus.BAD_REQUEST);
            }
            equipoDb.setPais(datosEnviados.getPais().trim());
        }
        if (datosEnviados.getCorreoContacto() != null) {
            equipoDb.setCorreoContacto(datosEnviados.getCorreoContacto().trim());
        }
        if (datosEnviados.getTelefonoContacto() != null) {
            equipoDb.setTelefonoContacto(datosEnviados.getTelefonoContacto().trim());
        }
        if (datosEnviados.getEstado() != null && !datosEnviados.getEstado().isBlank()) {
            equipoDb.setEstado(datosEnviados.getEstado().trim());
        }

        Equipo actualizado = equipoRepository.save(equipoDb);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", "success");
        response.put("mensaje", "Equipo actualizado correctamente");
        response.put("equipo", actualizado);
        return ResponseEntity.ok(response); // HTTP 200 OK
    }

    // =========================================================================
    // PREGUNTA 4 (2 puntos): Desactivar un equipo (eliminación lógica)
    // El estado del equipo cambia a "Inactivo"
    // =========================================================================
    @DeleteMapping({"/{id}", "/desactivar/{id}"})
    public ResponseEntity<Map<String, Object>> desactivarEquipo(@PathVariable("id") String idOrTag) {
        Optional<Equipo> opt;

        // Soportamos desactivar tanto por ID numerico como por Tag
        try {
            int id = Integer.parseInt(idOrTag);
            opt = equipoRepository.findById(id);
        } catch (NumberFormatException ex) {
            opt = equipoRepository.findByTagIgnoreCase(idOrTag.trim());
        }

        if (opt.isEmpty()) {
            throw new EquipoException("Equipo no encontrado", HttpStatus.NOT_FOUND); // HTTP 404
        }

        Equipo equipoDb = opt.get();
        equipoDb.setEstado("Inactivo");
        equipoRepository.save(equipoDb);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", "success");
        response.put("mensaje", "El equipo '" + equipoDb.getNombreEquipo() + "' ha sido desactivado (Estado: Inactivo)");
        response.put("equipo", equipoDb);
        return ResponseEntity.ok(response); // HTTP 200 OK
    }

    // =========================================================================
    // METODO AUXILIAR DE VALIDACIONES (PREGUNTAS 1, 3 y 5)
    // =========================================================================
    private void validarReglasEquipo(Equipo datos, Integer idActual) {

        // 1. Nombre de equipo duplicado
        if (datos.getNombreEquipo() != null && !datos.getNombreEquipo().isBlank()) {
            Optional<Equipo> existenteNombre = equipoRepository.findByNombreEquipoIgnoreCase(datos.getNombreEquipo().trim());
            if (existenteNombre.isPresent() && (idActual == null || !existenteNombre.get().getId().equals(idActual))) {
                throw new EquipoException("Nombre de equipo duplicado: ya existe un equipo con el nombre '"
                        + datos.getNombreEquipo().trim() + "'", HttpStatus.CONFLICT); // HTTP 409 Conflict
            }
        }

        // 2. Tag entre 2 y 5 caracteres y no duplicado
        if (datos.getTag() != null) {
            String tagLimpio = datos.getTag().trim();
            if (tagLimpio.length() < 2 || tagLimpio.length() > 5) {
                throw new EquipoException("El tag del equipo debe tener entre 2 y 5 caracteres", HttpStatus.BAD_REQUEST); // HTTP 400
            }
            Optional<Equipo> existenteTag = equipoRepository.findByTagIgnoreCase(tagLimpio);
            if (existenteTag.isPresent() && (idActual == null || !existenteTag.get().getId().equals(idActual))) {
                throw new EquipoException("Tag duplicado: ya existe un equipo registrado con el tag '"
                        + tagLimpio + "'", HttpStatus.CONFLICT); // HTTP 409 Conflict
            }
        }

        // 3. Cantidad de jugadores entre 5 y 10
        if (datos.getCantidadJugadores() != null) {
            int cant = datos.getCantidadJugadores();
            if (cant < 5 || cant > 10) {
                throw new EquipoException("Cantidad de jugadores fuera del rango permitido: debe estar entre 5 y 10 jugadores",
                        HttpStatus.BAD_REQUEST); // HTTP 400
            }
        }

        // 4. Correo con formato valido
        if (datos.getCorreoContacto() != null) {
            String correo = datos.getCorreoContacto().trim();
            String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!correo.matches(regexEmail)) {
                throw new EquipoException("Correo inválido: debe ingresar un correo electrónico con formato válido",
                        HttpStatus.BAD_REQUEST); // HTTP 400
            }
        }

        // 5. Telefono de 9 digitos numericos
        if (datos.getTelefonoContacto() != null) {
            String telefono = datos.getTelefonoContacto().trim();
            if (!telefono.matches("\\d{9}")) {
                throw new EquipoException("Teléfono inválido: el teléfono de contacto debe contener exactamente 9 dígitos numéricos",
                        HttpStatus.BAD_REQUEST); // HTTP 400
            }
        }
    }
}
