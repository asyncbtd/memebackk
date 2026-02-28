package io.github.lisi4ka.service;

import io.github.lisi4ka.core.mapper.EntityMapper;
import io.github.lisi4ka.service.model.Image;
import io.github.lisi4ka.storage.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final EntityMapper entityMapper;
    private final ImageRepository imageRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public Image saveImage(MultipartFile file, String title, String description) throws IOException {
        // Валидация
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Создаем директорию если не существует
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Генерируем уникальное имя файла
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // Сохраняем файл
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        var image = Image.builder()
                .title(title)
                .description(description)
                .fileName(fileName)
                .originalFileName(originalFileName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
        var imageEntity = entityMapper.toEntity(image);
        imageRepository.save(imageEntity);
        return image;
    }

    public List<Image> getAllImages() {
        return imageRepository.findAllByOrderByUploadDateDesc().stream()
                .map(entityMapper::toModel)
                .toList();
    }

    public List<Image> searchImages(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllImages();
        }
        String searchQuery = query.trim().toLowerCase();
        return imageRepository.searchByTitleOrDescription(searchQuery).stream()
                .map(entityMapper::toModel)
                .toList();
    }

    public byte[] getImageBytes(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }
        return Files.readAllBytes(filePath);
    }

    public boolean deleteImage(String fileName) throws IOException {
        var filePath = Paths.get(uploadDir).resolve(fileName);
        var fileDeleted = Files.deleteIfExists(filePath);

        if (existsByFileName(fileName)) {
            imageRepository.deleteByFileName(fileName);
            return true;
        }

        return fileDeleted;
    }

    public Image findByFileName(String fileName) {
        var entity = imageRepository.findByFileName(fileName);
        return entityMapper.toModel(entity);
    }

    public boolean existsByFileName(String fileName) {
        return imageRepository.existsByFileName(fileName);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ".jpg";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

}