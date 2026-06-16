package com.example.umc.mock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MockController {

    @GetMapping("/api/health")
    public String getHealth() {
        return "OK";
    }

    @GetMapping("/api/mock/result")
    public Map<String, Object> getMockResult() {
        return Map.of(
                "roomName", "공학관 301호",
                "hotCount", 7,
                "coldCount", 3,
                "resultType", "HOT",
                "missionName", "에어컨 온도 낮추기",
                "selectedNickname", "고슴이"
        );
    }

    @GetMapping("/api/mock/rooms")
    public List<Map<String, Object>> getMockRoomList() {
        return List.of(
                Map.of(
                        "roomId", 1,
                        "roomName", "공학관 301호",
                        "roomCode", "ENG301"
                ),
                Map.of(
                        "roomId", 2,
                        "roomName", "공학관 302호",
                        "roomCode", "ENG302"
                )
        );
    }
}