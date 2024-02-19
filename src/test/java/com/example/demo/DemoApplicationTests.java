package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class DemoApplicationTests {

	private final ApplicationContext context;

    @Autowired
    DemoApplicationTests(ApplicationContext context) {
        this.context = context;
    }

    @Test
    void contextLoads() {
        // Verify that the application context loads successfully
        assertNotNull(context);
    }

}
