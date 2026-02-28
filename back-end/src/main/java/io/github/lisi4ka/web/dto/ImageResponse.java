package io.github.lisi4ka.web.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ImageResponse(
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
