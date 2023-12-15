package ru.softlab.efr.services.insurance.controllers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.annotations.HasRight;
import ru.softlab.efr.services.insurance.model.db.DbImage;
import ru.softlab.efr.services.insurance.model.rest.InlineResponse200;
import ru.softlab.efr.services.insurance.services.DbImageService;
import ru.softlab.efr.services.insurance.utils.StringUtils;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController
public class DbImageController implements DbImageApi {

    private final DbImageService dbImageService;

    private static final List<String> allowedExtension = Arrays.asList("jpg", "jpeg", "png", "gif");


    public DbImageController(DbImageService dbImageService) {
        this.dbImageService = dbImageService;
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<InlineResponse200> addImage(@Valid @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {
        if (content == null || content.getOriginalFilename() == null ||
                !allowedExtension.contains(FilenameUtils.getExtension(content.getOriginalFilename().toLowerCase()))) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(
                new InlineResponse200(dbImageService.save(
                        new DbImage(null,
                                new String(content.getOriginalFilename().toLowerCase()
                                        .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)),
                        content.getBytes())
                        .getId()));
    }

    @Override
    @HasRight(Right.EDIT_PRODUCT_SETTINGS)
    public ResponseEntity<Void> deleteImage(@PathVariable("id") Long id) throws Exception {
        if (!dbImageService.delete(id)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) throws Exception {
        DbImage dbImage = dbImageService.findDbImageById(id);
       if(dbImage == null) {
           return ResponseEntity.notFound().build();
       }
        String extension = "";
        if (!StringUtils.isEmpty(dbImage.getName())){
            extension = FilenameUtils.getExtension(dbImage.getName());
            extension = "jpg".equals(extension) ? "jpeg" : extension;
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("image/" + extension))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbImage.getName() + "\"")
                .body(dbImage.getImage());
    }
}
