package com.talk.backend.object;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

public class VersionRequest {
    @NotBlank(message = "version不能为空")
    @Pattern(regexp = "^v\\d+\\.\\d+\\.\\d+$", message = "version格式必须为v1.0.0")
    @Schema(example = "v1.0.0", description = "版本号，格式如v1.0.0")
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
} 