package com.maziad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String wsUrl = "ws://localhost:8090/orders";
        Order order = new Order("Apple Macbook", 1);
        int totalClients = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(totalClients);

        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < totalClients; i++) {
            clients.add(new Client(wsUrl));
        }

        clients.forEach(client -> {
            executorService.submit(() -> {
                client.sendOrder(order);
                for (int j = 0; j < OrderStatus.values().length; j++) {
                    System.out.println(client.poll());
                }
            });
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}
