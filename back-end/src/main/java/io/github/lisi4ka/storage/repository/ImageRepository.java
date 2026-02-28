package io.github.lisi4ka.storage.repository;

import io.github.lisi4ka.storage.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    List<ImageEntity> findByTitleContainingIgnoreCase(String title);

    ImageEntity findByFileName(String fileName);

    @Query("SELECT i FROM ImageEntity i WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "ORDER BY i.uploadDate DESC")
    List<ImageEntity> searchByTitleOrDescription(@Param("query") String query);

    List<ImageEntity> findAllByOrderByUploadDateDesc();

    boolean existsByFileName(String fileName);

    long count();

    void deleteByFileName(String fileName);
}
