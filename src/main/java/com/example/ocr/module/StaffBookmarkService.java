package com.example.ocr.module;

import com.example.ocr.schema.ocr;
import org.springframework.web.multipart.MultipartFile;
import vn.com.itechcorp.base.exception.APIException;

import java.io.IOException;

public interface StaffBookmarkService {

    ocr getImage(MultipartFile image) throws APIException, IOException, InterruptedException;

}
