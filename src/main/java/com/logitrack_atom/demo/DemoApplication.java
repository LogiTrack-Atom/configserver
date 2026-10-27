package com.logitrack_atom.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Inicia el servidor de configuración centralizada de LogiTrack Atom.
 *
 * <p>Publica las propiedades versionadas en Git para los servicios de negocio.
 * No necesita base de datos ni consume configuración de sí mismo.</p>
 *
 * @author MaxDz
 * @version 0.2.0
 */
@SpringBootApplication
@EnableConfigServer
public class DemoApplication {
    /** Construye el punto de entrada administrado por Spring Boot. */
    public DemoApplication() { }

    /**
     * Inicia la aplicación y clona el repositorio configurado antes de servir solicitudes.
     * @param args opciones de ejecución de Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
