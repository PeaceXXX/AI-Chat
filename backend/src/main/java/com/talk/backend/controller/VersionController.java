package com.talk.backend.controller;

import com.talk.backend.service.VersionService;
import com.talk.backend.object.VersionRequest;
import com.talk.backend.object.VersionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class VersionController {

    @Autowired
    private VersionService versionService;

    @GetMapping("/version")
    public VersionResponse getVersion() {
        String version = versionService.getVersion();
        String status = version != null ? "ok" : "not set";
        long timestamp = System.currentTimeMillis();
        return new VersionResponse(version, status, timestamp);
    }

    @PostMapping("/version")
    public VersionResponse saveOrUpdateVersion(@Valid @RequestBody VersionRequest body) {
        String version = body.getVersion();
        versionService.saveOrUpdateVersion(version);
        return new VersionResponse(version, "saved", System.currentTimeMillis());
    }
} 