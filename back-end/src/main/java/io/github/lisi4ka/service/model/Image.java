package io.github.lisi4ka.service.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Image(
        Long id,
        String title,
        String description,
        String fileName,
        String originalFileName,
        String fileType,
        Long fileSize,
        LocalDateTime uploadDate
) {
}
