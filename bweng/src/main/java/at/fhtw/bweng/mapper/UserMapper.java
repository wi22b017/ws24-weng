package at.fhtw.bweng.mapper;

import at.fhtw.bweng.dto.UserResponseDto;
import at.fhtw.bweng.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Basic field mapping between User and UserResponseDto
    @Mapping(target = "profilePictureUrl", ignore = true) // Handle profile picture URL manually
    UserResponseDto toDto(User user);
    User toEntity(UserResponseDto userResponseDto);
}
