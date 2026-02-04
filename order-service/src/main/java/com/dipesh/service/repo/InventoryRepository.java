package com.dipesh.service.repo;

import com.dipesh.service.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, String>
{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryEntity i where i.productId = :productId")
    Optional<InventoryEntity> findByProductIdForUpdate(@Param("productId") String productId);
}
