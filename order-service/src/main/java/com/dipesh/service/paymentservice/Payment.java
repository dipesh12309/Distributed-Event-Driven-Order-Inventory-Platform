package com.dipesh.service.paymentservice;

import java.time.Instant;
import java.util.UUID;

public class Payment {

    private final String paymentId;
    private final String orderId;
    private PaymentStatus status;
    private final Instant createdAt;

    public Payment(String orderId) {
        this.paymentId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.status = PaymentStatus.INITIATED;
        this.createdAt = Instant.now();
    }

    public void markSuccess() {
        this.status = PaymentStatus.SUCCESS;
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
        return orderId;
    }
}
