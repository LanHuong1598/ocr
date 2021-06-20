package com.example.ocr.schema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class input {

    MultipartFile front;
    MultipartFile back;

}
