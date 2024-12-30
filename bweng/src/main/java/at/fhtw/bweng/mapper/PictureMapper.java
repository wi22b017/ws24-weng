package at.fhtw.bweng.mapper;

import at.fhtw.bweng.dto.PictureDto;
import at.fhtw.bweng.model.Picture;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PictureMapper {
    PictureDto toDto(Picture picture);
    Picture toEntity(PictureDto pictureDto);
}
