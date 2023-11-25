package com.example.b2b.controller;

import com.example.b2b.entity.mensagem.Chat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Chat enviarMensagem(@Payload Chat chat){
        chat.setData(new Date());
        return chat;
    }

}
