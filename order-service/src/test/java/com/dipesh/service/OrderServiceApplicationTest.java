package com.dipesh.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderServiceApplicationTest
{
    @Test
    void hasSpringBootApplicationAnnotation()
    {
        SpringBootApplication annotation = OrderServiceApplication.class.getAnnotation(SpringBootApplication.class);

        assertNotNull(annotation);
    }
}
