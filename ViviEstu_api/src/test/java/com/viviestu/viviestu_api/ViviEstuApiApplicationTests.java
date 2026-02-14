package com.viviestu.viviestu_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        // 1. Usamos base de datos en memoria (H2) para no necesitar PostgreSQL real
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",

        // 2. Evitamos que Hibernate intente validar cosas raras
        "spring.jpa.hibernate.ddl-auto=create-drop",

        // 3. Definimos una clave secreta falsa para que JWT no falle
        "jwt.secret=clave_secreta_super_larga_para_tests_1234567890"
})

class ViviEstuApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
