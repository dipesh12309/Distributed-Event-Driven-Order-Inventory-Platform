package com.dipesh.service.payment;

import org.springframework.stereotype.Component;

import java.util.Random;

import static java.lang.Thread.sleep;

@Component
public class PaymentGateway
{

    private final Random random = new Random();

    public boolean charge(String userId, double amount)
    {

        // simulate network latency
        try
        {
            sleep(300);
        }
        catch (InterruptedException e)
        {
            //ignored for now
        }

        return random.nextInt(10) < 8;
    }
}
