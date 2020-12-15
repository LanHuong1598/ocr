package com.example.ocr.api;

import com.example.ocr.module.StaffBookmarkService;
import com.example.ocr.schema.ocr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/secured/ws/rest/v1/")
public class testAPI {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StaffBookmarkService staffBookmarkService;

    @PreAuthorize("permitAll()")
    @PostMapping("/test")
    public ResponseEntity<ocr> test(@Valid @RequestBody MultipartFile multipartFile) {
        try {
            return ResponseEntity.ok().body(staffBookmarkService.getImage(multipartFile));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
