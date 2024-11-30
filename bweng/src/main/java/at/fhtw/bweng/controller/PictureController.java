package at.fhtw.bweng.controller;

import at.fhtw.bweng.dto.PictureDto;
import at.fhtw.bweng.model.Picture;
import at.fhtw.bweng.service.PictureService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/pictures")
public class PictureController {
    private final PictureService pictureService;
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public PictureDto upload(@RequestParam("file") MultipartFile file) {
        return pictureService.upload(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> retrieve(@PathVariable("id") UUID id) {
        Picture picture = pictureService.findById(id);
        Resource resource = pictureService.asResource(picture);
        MediaType mediaType = MediaType.parseMediaType(picture.getContentType());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}
