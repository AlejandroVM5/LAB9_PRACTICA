CREATE DATABASE IF NOT EXISTS esports_db;
USE esports_db;

DROP TABLE IF EXISTS equipos;

CREATE TABLE equipos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_equipo VARCHAR(100) NOT NULL UNIQUE,
    tag VARCHAR(5) NOT NULL UNIQUE,
    nombre_capitan VARCHAR(100) NOT NULL,
    cantidad_jugadores INT NOT NULL,
    juego VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    correo_contacto VARCHAR(150) NOT NULL,
    telefono_contacto VARCHAR(9) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activo'
);

INSERT INTO equipos (nombre_equipo, tag, nombre_capitan, cantidad_jugadores, juego, pais, correo_contacto, telefono_contacto, estado)
VALUES
('Thunder Awaken', 'TA', 'Matthew', 5, 'Dota 2', 'Perú', 'contacto@thunderawaken.pe', '987654321', 'Activo'),
('KRÜ Esports', 'KRU', 'Klaus', 6, 'Valorant', 'Argentina', 'info@kruesports.com', '912345678', 'Activo');
