package io.github.lisi4ka.core.mapper;

import io.github.lisi4ka.service.model.Image;
import io.github.lisi4ka.storage.entity.ImageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    ImageEntity toEntity(Image source);
    Image toModel(ImageEntity source);
}
