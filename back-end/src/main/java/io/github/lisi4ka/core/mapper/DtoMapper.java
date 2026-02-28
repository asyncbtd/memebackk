package io.github.lisi4ka.core.mapper;


import io.github.lisi4ka.service.model.Image;
import io.github.lisi4ka.web.dto.ImageResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    ImageResponse toDto(Image source);
    Image toModel(ImageResponse source);
}
