package com.maziad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.maziad.OrderStatus.ORDER_BEING_BUILT;
import static com.maziad.OrderStatus.ORDER_RECEIVED;
import static com.maziad.OrderStatus.ORDER_SUCCESSFUL;

@Controller
public class OrderController {

    @Autowired
    public SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("orders")
    public void buildOrder(Order order, Principal principal) {
        String user = principal.getName();
        sendMessage(new OrderState(user, ORDER_RECEIVED));
        synchronized (this) {
            sleep(2000);
            sendMessage(new OrderState(user, ORDER_BEING_BUILT));
            sleep(2000);
            sendMessage(new OrderState(user, ORDER_SUCCESSFUL));
        }
    }

    public void sendMessage(OrderState state) {
        messagingTemplate.convertAndSend(String.format("/user/%s/queue/private", state.getUser()), state);
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
