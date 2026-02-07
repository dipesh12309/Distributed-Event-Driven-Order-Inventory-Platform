package com.dipesh.service.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessedEventStoreTest
{
    @Test
    void isInterface()
    {
        assertTrue(ProcessedEventStore.class.isInterface());
    }
}
