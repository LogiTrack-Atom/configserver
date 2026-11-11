package com.logitrack_atom.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Comprueba el contexto sin depender de disponibilidad de Git remoto.
 * @author MaxDz
 * @version 0.2.0
 */
@SpringBootTest(properties = {
    "spring.cloud.config.server.native.search-locations=classpath:/config-fixture/"
})
@ActiveProfiles("native")
class DemoApplicationTests {
    /** Comprueba el ensamblado del servidor con un backend local de prueba. */
    @Test
    void contextLoads() { }
}
