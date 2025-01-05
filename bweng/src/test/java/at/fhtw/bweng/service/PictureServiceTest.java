package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PictureDto;
import at.fhtw.bweng.mapper.PictureMapper;
import at.fhtw.bweng.model.Picture;
import at.fhtw.bweng.property.MinioProperties;
import at.fhtw.bweng.repository.PictureRepository;
import at.fhtw.bweng.storage.FileStorage;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PictureServiceTest {

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private PictureMapper pictureMapper;

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioProperties minioProperties;

    @InjectMocks
    private PictureService pictureService;

    private static final UUID PICTURE_ID = UUID.randomUUID();
    private static final String EXTERNAL_ID = "external-id";
    private static final String NAME = "picture.jpg";
    private static final String CONTENT_TYPE = "image/jpeg";
    private static final Picture PICTURE = new Picture(PICTURE_ID, EXTERNAL_ID, NAME, CONTENT_TYPE);

    @Test
    void save_savesPicture() {
        // arrange
        when(pictureRepository.save(any(Picture.class))).thenReturn(PICTURE);

        // act
        Picture result = pictureService.save(PICTURE);

        // assert
        assertThat(result).isEqualTo(PICTURE);
        verify(pictureRepository, times(1)).save(PICTURE);
    }

    @Test
    void upload_savesAndReturnsPictureDto() {
        // arrange
        MultipartFile file = mock(MultipartFile.class);
        when(fileStorage.upload(file)).thenReturn(EXTERNAL_ID);
        when(file.getContentType()).thenReturn(CONTENT_TYPE);
        when(file.getOriginalFilename()).thenReturn(NAME);
        PictureDto pictureDto = new PictureDto(PICTURE_ID);
        when(pictureMapper.toDto(any(Picture.class))).thenReturn(pictureDto);
        when(pictureRepository.save(any(Picture.class))).thenReturn(PICTURE);

        // act
        PictureDto result = pictureService.upload(file);

        // assert
        assertThat(result).isEqualTo(pictureDto);
        verify(pictureRepository, times(1)).save(any(Picture.class));
        verify(fileStorage, times(1)).upload(file);
    }

    @Test
    void findById_returnsPicture() {
        // arrange
        when(pictureRepository.findById(PICTURE_ID)).thenReturn(Optional.of(PICTURE));

        // act
        Picture result = pictureService.findById(PICTURE_ID);

        // assert
        assertThat(result).isEqualTo(PICTURE);
    }

    @Test
    void findById_throwsEntityNotFoundExceptionWhenNotFound() {
        // arrange
        when(pictureRepository.findById(PICTURE_ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatThrownBy(() -> pictureService.findById(PICTURE_ID))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void asResource_returnsResource() {
        // arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        when(fileStorage.download(EXTERNAL_ID)).thenReturn(inputStream);

        // act
        Resource result = pictureService.asResource(PICTURE);

        // assert
        assertThat(result).isInstanceOf(InputStreamResource.class);
    }

    @Test
    void generatePresignedUrl_returnsUrl() throws Exception {
        // arrange
        String bucketName = "test-bucket";
        String url = "http://example.com/presigned-url";
        when(minioProperties.getBucket()).thenReturn(bucketName);
        when(minioClient.getPresignedObjectUrl(any())).thenReturn(url);

        // act
        String result = pictureService.generatePresignedUrl(EXTERNAL_ID);

        // assert
        assertThat(result).isEqualTo(url);
    }

    @Test
    void generatePresignedUrl_throwsRuntimeExceptionOnError() throws Exception {
        // arrange
        lenient().when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenThrow(new RuntimeException("Error generating presigned URL"));

        // act & assert
        assertThatThrownBy(() -> pictureService.generatePresignedUrl(EXTERNAL_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error generating presigned URL");
    }

}