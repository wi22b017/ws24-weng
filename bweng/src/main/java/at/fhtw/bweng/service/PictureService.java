package at.fhtw.bweng.service;

import at.fhtw.bweng.dto.PictureDto;
import at.fhtw.bweng.mapper.PictureMapper;
import at.fhtw.bweng.model.Picture;
import at.fhtw.bweng.property.MinioProperties;
import at.fhtw.bweng.repository.PictureRepository;
import at.fhtw.bweng.storage.FileStorage;
import io.minio.MinioClient;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;

import java.io.InputStream;
import java.util.UUID;

@Service
public class PictureService {
    private final PictureRepository pictureRepository;
    private final FileStorage fileStorage;
    private final PictureMapper pictureMapper;
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public PictureService(PictureRepository pictureRepository, FileStorage fileStorage, PictureMapper pictureMapper, MinioClient minioClient, MinioProperties minioProperties) {
        this.pictureRepository = pictureRepository;
        this.fileStorage = fileStorage;
        this.pictureMapper = pictureMapper;
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    public Picture save(Picture picture) {
        return pictureRepository.save(picture);
    }

    public PictureDto upload(MultipartFile file) {
        String externalId = fileStorage.upload(file);

        Picture picture = new Picture();
        picture.setExternalId(externalId);
        picture.setContentType(file.getContentType());
        picture.setName(file.getOriginalFilename());

        return pictureMapper.toDto(pictureRepository.save(picture));
    }

    public Picture findById(UUID id) {
        return pictureRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Resource asResource(Picture picture) {
        InputStream stream = fileStorage.download(picture.getExternalId());
        return new InputStreamResource(stream);
    }

    public String generatePresignedUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(60 * 60) // URL valid for 1 hour
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }

}
