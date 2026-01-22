package com.dipesh.service.repo;

import com.dipesh.service.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, String>
{
}
