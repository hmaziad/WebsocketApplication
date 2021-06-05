package com.maziad;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Client {
    private final String wsUrl;
    private StompSession stompSession;
    private BlockingQueue<OrderState> blockingQueue;

    public Client(String wsUrl) {
        this.wsUrl = wsUrl;
        blockingQueue = new ArrayBlockingQueue<>(3);
        setUpWebsocket();
    }

    private void setUpWebsocket() {
        OrderSessionHandler handler = new OrderSessionHandler(this);
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            stompSession = stompClient.connect(wsUrl, handler).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void sendOrder(Order order) {
        stompSession.send("/makeorder/orders", order);
    }

    public void addOrderState(OrderState state) {
        blockingQueue.add(state);
    }

    public OrderState poll() {
        try {
           return blockingQueue.poll(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("Request Timed out");
    }
}
