package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

class ServletInitializerTest {

    @Test
    void configure_ReturnsSpringApplicationBuilder() {
        // Create an instance of ServletInitializer
        ServletInitializer servletInitializer = new ServletInitializer();

        // Call the configure method and get the returned SpringApplicationBuilder
        SpringApplicationBuilder builder = servletInitializer.configure(new SpringApplicationBuilder());

        // Verify that the returned SpringApplicationBuilder is not null
        assertNotNull(builder);

        // You can add more assertions here if needed
    }
}