package esameTAAS.productMicroservice.Controllers;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class DB_ExampleReciever {

    public void receiveMessage(String message) {
        System.out.println("Received by Object Receiver <" + message + ">");
    }
}