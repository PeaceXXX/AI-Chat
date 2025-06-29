package com.talk.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Version {
    @Id
    private Long id = 1L;
    private String version;

    public Version() {}

    public Version(String version) {
        this.id = 1L;
        this.version = version;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
} 