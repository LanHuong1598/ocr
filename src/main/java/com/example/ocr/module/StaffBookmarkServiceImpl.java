package com.example.ocr.module;

import com.example.ocr.schema.ocr;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.com.itechcorp.base.exception.APIException;

import javax.transaction.Transactional;
import java.io.*;
import java.util.*;
@Transactional
@Service("staffBookmarkServiceImpl")
public class StaffBookmarkServiceImpl implements StaffBookmarkService {

    public ocr getImage(MultipartFile image) throws APIException, IOException, InterruptedException {

        String uuid = UUID.randomUUID().toString();
        String path = "/home/lanhuong/Documents/ORC/MTA_OCR_2020/server/" + uuid;
        File attachment = new File(path + ".jpg");

        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(attachment));
        FileCopyUtils.copy(image.getInputStream(), stream);
        stream.close();


        ProcessBuilder processBuilder =
                new ProcessBuilder("sh", "-c", "cd /home/lanhuong/Documents/ORC/MTA_OCR_2020/ && python test.py " + path + ".jpg");
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        assert exitCode == 0;

        Thread.sleep(1000);
        File f = new File("/home/lanhuong/Documents/ORC/MTA_OCR_2020/result_image/" + uuid + ".jpg");
        String encodedString = Base64
                .getEncoder()
                .encodeToString(StreamUtils.copyToByteArray(new FileInputStream(f)));

        int i = 0;
        String idNo = "";
        String name = "";
        FileInputStream fis = new FileInputStream("/home/lanhuong/Documents/ORC/MTA_OCR_2020/result_image/" + uuid + ".txt");
        Scanner scanner = new Scanner(fis);

        while (scanner.hasNextLine()) {
            if (i == 0) idNo = scanner.nextLine();
            if (i == 1) name = scanner.nextLine();
            i++;
            if (i > 1) break;
        }

        scanner.close();

        ocr ocr = new ocr(encodedString, idNo, name);

        return ocr;
    }
}