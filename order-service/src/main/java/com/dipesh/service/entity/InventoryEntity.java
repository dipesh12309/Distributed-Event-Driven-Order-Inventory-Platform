package com.dipesh.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class InventoryEntity
{
    @Id
    @Column(name = "product_id", length = 64)
    private String productId;

    @Column(name = "available_qty", nullable = false)
    private int availableQty;

    @Version
    private int version;

    protected InventoryEntity()
    {
    }

    public InventoryEntity(String productId, int availableQty)
    {
        this.productId = productId;
        this.availableQty = availableQty;
    }
}
