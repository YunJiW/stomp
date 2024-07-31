package com.example.demo.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.example.demo.chat.ChatMessage;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.pub.service.RedisMessagePublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	
	private final RedisMessagePublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;
	
	@MessageMapping("/chat/message")
    public void message(ChatMessage chatMessage) {
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatRoomRepository.enterChatRoom(chatMessage.getRoomId());
            chatMessage.setMessage(chatMessage.getSender() + "님 등장!");
        }

        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        // publisher에 topic과 chatMessage를 전달
        redisPublisher.publish(chatRoomRepository.getTopic(chatMessage.getRoomId()), chatMessage);
    }
}
