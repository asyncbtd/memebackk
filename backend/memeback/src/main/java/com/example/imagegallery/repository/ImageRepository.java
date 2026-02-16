package com.example.imagegallery.repository;

import com.example.imagegallery.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByTitleContainingIgnoreCase(String title);

    Image findByFileName(String fileName);

    @Query("SELECT i FROM Image i WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY i.uploadDate DESC")
    List<Image> searchByTitleOrDescription(@Param("query") String query);

    List<Image> findAllByOrderByUploadDateDesc();

    boolean existsByFileName(String fileName);

    long count();
}