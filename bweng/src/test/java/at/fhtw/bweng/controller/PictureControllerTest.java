package at.fhtw.bweng.controller;


import at.fhtw.bweng.dto.PictureDto;
import at.fhtw.bweng.model.Picture;
import at.fhtw.bweng.service.PictureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PictureControllerTest {

    @Mock
    private PictureService pictureService;

    @InjectMocks
    private PictureController pictureController;

    @Test
    void upload_returnsPictureDto() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "mock image content".getBytes());
        UUID pictureId = UUID.randomUUID();
        PictureDto expectedDto = new PictureDto(pictureId);
        when(pictureService.upload(file)).thenReturn(expectedDto);

        // Act
        PictureDto actualDto = pictureController.upload(file);

        // Assert
        assertThat(actualDto).isNotNull();
        assertThat(actualDto.id()).isEqualTo(expectedDto.id());
        verify(pictureService, times(1)).upload(file);
    }

    @Test
    void retrieve_returnsResource() {
        // Arrange
        UUID pictureId = UUID.randomUUID();
        Picture picture = new Picture(pictureId, "external-id", "image.jpg", "image/jpeg");
        Resource mockResource = mock(Resource.class);

        when(pictureService.findById(pictureId)).thenReturn(picture);
        when(pictureService.asResource(picture)).thenReturn(mockResource);

        // Act
        ResponseEntity<Resource> response = pictureController.retrieve(pictureId);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_JPEG);
        assertThat(response.getBody()).isEqualTo(mockResource);
        verify(pictureService, times(1)).findById(pictureId);
        verify(pictureService, times(1)).asResource(picture);
    }
}