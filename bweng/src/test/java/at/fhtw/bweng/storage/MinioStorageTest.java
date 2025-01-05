package at.fhtw.bweng.storage;

import at.fhtw.bweng.exception.FileException;
import at.fhtw.bweng.property.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioStorageTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioProperties minioProperties;

    @InjectMocks
    private MinioStorage minioStorage;

    @Test
    void upload_withValidFile_returnsUuid() throws Exception {
        // Arrange
        String bucketName = "test-bucket";
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "dummy content".getBytes());
        when(minioProperties.getBucket()).thenReturn(bucketName);

        // Act
        String result = minioStorage.upload(file);

        // Assert
        assertThat(result).isNotNull();
        verify(minioClient).putObject(any());
    }

    @Test
    void upload_withInvalidFileFormat_throwsFileException() {
        // Arrange
        MockMultipartFile invalidFile = new MockMultipartFile("file", "test.txt", "text/plain", "dummy content".getBytes());

        // Act & Assert
        assertThatThrownBy(() -> minioStorage.upload(invalidFile))
                .isInstanceOf(FileException.class)
                .hasMessageContaining("File format not allowed");
    }

    @Test
    void upload_whenMinioThrowsException_throwsFileException() throws Exception {
        // Arrange
        String bucketName = "test-bucket";
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "dummy content".getBytes());
        when(minioProperties.getBucket()).thenReturn(bucketName);

        // Use a generic Exception or RuntimeException
        doThrow(new RuntimeException("Minio error")).when(minioClient).putObject(any());

        // Act & Assert
        assertThatThrownBy(() -> minioStorage.upload(file))
                .isInstanceOf(FileException.class)
                .hasMessageContaining("Upload failed");
    }

    @Test
    void download_withValidId_returnsInputStream() throws Exception {
        // Arrange
        String bucketName = "test-bucket";
        String fileId = "file-id";
        when(minioProperties.getBucket()).thenReturn(bucketName);

        // Mock the GetObjectResponse
        GetObjectResponse mockResponse = mock(GetObjectResponse.class);

        // Make the MinioClient return that mock
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(mockResponse);

        // Stub readAllBytes() to return an actual byte array
        when(mockResponse.readAllBytes()).thenReturn("dummy content".getBytes());

        // Act
        InputStream result = minioStorage.download(fileId);

        // Assert
        assertThat(result).isNotNull();

        // Verify the content
        String content = new String(result.readAllBytes());
        assertThat(content).isEqualTo("dummy content");

        verify(minioClient).getObject(any(GetObjectArgs.class));
    }




    @Test
    void download_whenMinioThrowsException_throwsFileException() throws Exception {
        // Arrange
        String bucketName = "test-bucket";
        String fileId = "file-id";

        when(minioProperties.getBucket()).thenReturn(bucketName);

        // Instead of MinioException (abstract), just throw a RuntimeException to simulate a failure
        doThrow(new RuntimeException("Minio error")).when(minioClient).getObject(any(GetObjectArgs.class));

        // Act & Assert
        assertThatThrownBy(() -> minioStorage.download(fileId))
                .isInstanceOf(FileException.class)
                .hasMessageContaining("Download failed");
    }



}