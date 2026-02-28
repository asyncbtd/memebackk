package io.github.lisi4ka.web;

import io.github.lisi4ka.service.model.Image;
import io.github.lisi4ka.web.dto.ImageResponse;
import io.github.lisi4ka.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Проверяем тип файла
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            Image savedImage = imageService.saveImage(file, title, description);
            ImageResponse response = convertToResponse(savedImage);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ImageResponse>> getAllImages() {
        try {
            List<Image> images = imageService.getAllImages();
            List<ImageResponse> responses = images.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            // В случае ошибки возвращаем пустой список
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ImageResponse>> searchImages(@RequestParam(required = false) String q) {
        try {
            List<Image> images;

            if (q == null || q.trim().isEmpty()) {
                // Если поисковый запрос пустой, возвращаем все изображения
                images = imageService.getAllImages();
            } else {
                // Выполняем поиск по запросу
                images = imageService.searchImages(q.trim());
            }

            List<ImageResponse> responses = images.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            // В случае ошибки возвращаем пустой список
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            byte[] imageBytes = imageService.getImageBytes(fileName);
            Image image = imageService.findByFileName(fileName);

            if (image == null) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(image.getFileType()));
            headers.setContentDispositionFormData("inline", image.getOriginalFileName());
            headers.setCacheControl("max-age=3600"); // Кэшируем на 1 час

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<?> deleteImage(@PathVariable String fileName) {
        try {
            boolean deleted = imageService.deleteImage(fileName);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Image not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting image file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    // Дополнительные эндпоинты для удобства

    @GetMapping("/count")
    public ResponseEntity<Long> getImageCount() {
        try {
            long count = imageService.getAllImages().size();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.ok(0L);
        }
    }

    @GetMapping("/{fileName}/info")
    public ResponseEntity<ImageResponse> getImageInfo(@PathVariable String fileName) {
        try {
            Image image = imageService.findByFileName(fileName);
            if (image == null) {
                return ResponseEntity.notFound().build();
            }
            ImageResponse response = convertToResponse(image);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Вспомогательный метод для преобразования Image в ImageResponse
    private ImageResponse convertToResponse(Image image) {
        return new ImageResponse(
                image.getId(),
                image.getTitle(),
                image.getDescription(),
                image.getFileName(),
                image.getOriginalFileName(),
                image.getFileType(),
                image.getFileSize(),
                image.getUploadDate()
        );
    }
}