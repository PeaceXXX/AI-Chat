package com.talk.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VersionController {

    @GetMapping("/version")
    public Map<String, Object> getVersion() {
        Map<String, Object> response = new HashMap<>();
        response.put("version", "1.0.0");
        response.put("name", "AI Chat Backend");
        response.put("status", "running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
} 