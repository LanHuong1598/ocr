package com.example.ocr.module;

import com.example.ocr.schema.input;
import com.example.ocr.schema.ocr;
import vn.com.itechcorp.base.exception.APIException;

import java.io.IOException;

public interface StaffBookmarkService {

    ocr getImage(input image) throws APIException, IOException, InterruptedException;

}
