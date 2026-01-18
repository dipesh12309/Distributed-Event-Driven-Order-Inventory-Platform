package com.dipesh.service.paymentservice;

import com.dipesh.model.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPaymentService implements PaymentService {

    private final Map<String, Payment> store = new ConcurrentHashMap<>();

    @Override
    public Payment initiatePayment(Order order) {
        Payment payment = new Payment(order.getOrderId());
        store.put(payment.getPaymentId(), payment);
        return payment;
    }

    @Override
    public Payment confirmPayment(String paymentId) {
        Payment payment = store.get(paymentId);

        if (payment == null) {
            throw new IllegalStateException("Payment not found");
        }

        // here is where gateway response would be handled
        payment.markSuccess(); // or markFailed()

        return payment;
    }
}
