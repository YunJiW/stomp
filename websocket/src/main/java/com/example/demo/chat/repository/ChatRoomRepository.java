package com.example.demo.chat.repository;

import com.example.demo.chat.ChatRoom;
import com.example.demo.sub.service.RedisMessageSubscriber;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    private final RedisMessageListenerContainer redisMessageListener;   // 채팅방(topic)에 발행되는 메시지를 처리할 Listner *
    private final RedisMessageSubscriber redisSubscriber;  // 구독 처리 서비스 *
    private static final String CHAT_ROOMS = "CHAT_ROOM";

    private final RedisTemplate<String, Object> redisTemplate;  // redis
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;   // redis 저장

    private Map<String, ChannelTopic> topics;   // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다. *

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);    // 채팅방 redis에 저장
        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);    // roomId로 topic을 잦음
        if (topic == null)  // topic이 없으면
            topic = new ChannelTopic(roomId);   // 토픽을 만들고 Map에 토픽정보 저장
        redisMessageListener.addMessageListener(redisSubscriber, topic);    // 토픽에 대한 리스너 등록
        topics.put(roomId, topic);  // topic 추가
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
