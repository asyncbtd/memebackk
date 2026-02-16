package com.example.imagegallery.service;

import com.example.imagegallery.model.Image;
import com.example.imagegallery.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ImageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Autowired
    private ImageRepository imageRepository;

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

        // Сохраняем информацию в БД
        Image image = new Image(
                title,
                description,
                fileName,
                originalFileName,
                file.getContentType(),
                file.getSize()
        );

        return imageRepository.save(image);
    }

    public List<Image> getAllImages() {
        return imageRepository.findAllByOrderByUploadDateDesc();
    }

    public List<Image> searchImages(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllImages();
        }
        String searchQuery = query.trim().toLowerCase();
        return imageRepository.searchByTitleOrDescription(searchQuery);
    }

    public byte[] getImageBytes(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + fileName);
        }
        return Files.readAllBytes(filePath);
    }

    public boolean deleteImage(String fileName) throws IOException {
        // Удаляем файл
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        boolean fileDeleted = Files.deleteIfExists(filePath);

        // Удаляем запись из БД
        Image image = imageRepository.findByFileName(fileName);
        if (image != null) {
            imageRepository.delete(image);
            return true;
        }
        return fileDeleted;
    }

    public Image findByFileName(String fileName) {
        return imageRepository.findByFileName(fileName);
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