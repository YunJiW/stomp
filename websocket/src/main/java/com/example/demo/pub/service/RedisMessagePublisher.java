package com.example.demo.pub.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.example.demo.chat.ChatMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisMessagePublisher {
	
	private final RedisTemplate<String, Object> redisTemplate;
	
	
	public void publish(ChannelTopic topic,ChatMessage message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}

}
