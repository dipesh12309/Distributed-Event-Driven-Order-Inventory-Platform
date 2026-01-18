package com.dipesh.service.paymentservice;

import com.dipesh.model.Order;

public interface PaymentService {

    Payment initiatePayment(Order order);

    Payment confirmPayment(String paymentId);
}
