package com.example.demo.chat.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.chat.ChatRoom;
import com.example.demo.chat.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/room")
    public String rooms(Model model) {
    	log.info("chat/room 이동");
        return "room";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam("name") String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable("roomId") String roomId) {
        model.addAttribute("roomId", roomId);
        return "roomdetail";
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable("roomId") String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }

}
