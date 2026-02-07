package com.dipesh.service.repo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRepositoryTest
{
    @Test
    void isInterface()
    {
        assertTrue(OrderRepository.class.isInterface());
    }
}
