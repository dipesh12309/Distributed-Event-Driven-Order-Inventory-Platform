package com.dipesh.service.repo;

import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryRepositoryTest
{
    @Test
    void declaresLockingQuery() throws NoSuchMethodException
    {
        Method method = InventoryRepository.class.getMethod("findByProductIdForUpdate", String.class);

        Lock lock = method.getAnnotation(Lock.class);
        Query query = method.getAnnotation(Query.class);
        Param param = method.getParameters()[0].getAnnotation(Param.class);

        assertNotNull(lock);
        assertEquals(LockModeType.PESSIMISTIC_WRITE, lock.value());
        assertNotNull(query);
        assertTrue(query.value().contains("InventoryEntity"));
        assertNotNull(param);
        assertEquals("productId", param.value());
    }
}
