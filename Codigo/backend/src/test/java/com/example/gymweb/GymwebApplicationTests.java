package com.example.gymweb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GymwebApplicationTests {

    @Test
    void contextLoads() {
        // Arranque mínimo para validar wiring del contexto con H2 en memoria
    }
}
