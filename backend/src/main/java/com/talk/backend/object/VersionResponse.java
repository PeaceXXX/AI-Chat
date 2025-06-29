package com.talk.backend.object;

public class VersionResponse {
    private String version;
    private String status;
    private long timestamp;

    public VersionResponse() {}

    public VersionResponse(String version, String status, long timestamp) {
        this.version = version;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
} 