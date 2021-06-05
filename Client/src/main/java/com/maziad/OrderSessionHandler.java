package com.maziad;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class OrderSessionHandler implements StompSessionHandler {
    private Client client;

    public OrderSessionHandler(Client client) {
        this.client = client;
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        stompSession.subscribe("/topic/orders", this);
        stompSession.subscribe("/user/queue/private", this);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        throw new RuntimeException(throwable);
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        throw new RuntimeException(throwable);
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return OrderState.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        client.addOrderState((OrderState) payload);
    }

}
