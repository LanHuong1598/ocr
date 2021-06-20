package com.example.ocr.module;

import com.example.ocr.schema.input;
import com.example.ocr.schema.ocr;
import org.apache.tomcat.jni.Proc;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import vn.com.itechcorp.base.exception.APIException;

import javax.transaction.Transactional;
import java.io.*;
import java.util.*;
@Transactional
@Service("staffBookmarkServiceImpl")
public class StaffBookmarkServiceImpl implements StaffBookmarkService {

    public ocr getImage(input image) throws APIException, IOException, InterruptedException {

        String ocr_front_path ="/home/lanhuong/Documents/OCR/front_model/";
        String ocr_back_path ="/home/lanhuong/Documents/OCR/back_model/";

        String uuid = UUID.randomUUID().toString();
        String path = ocr_front_path + "server/" + uuid;
        File attachment = new File(path + ".jpg");

        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(attachment));
        FileCopyUtils.copy(image.getFront().getInputStream(), stream);
        stream.close();

        ProcessBuilder processBuilder =
                new ProcessBuilder("sh", "-c", "cd " + ocr_front_path + " && python test.py " + path + ".jpg");
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        assert exitCode == 0;


        path = ocr_back_path + "server/" + uuid;
        File attachmentback = new File(path + ".jpg");
        BufferedOutputStream streamback = new BufferedOutputStream(
                new FileOutputStream(attachmentback));
        FileCopyUtils.copy(image.getBack().getInputStream(), streamback);
        streamback.close();

        ProcessBuilder processBuilderback =
                new ProcessBuilder("sh", "-c", "cd " + ocr_back_path + " && python test.py " + path + ".jpg");
        Process process_back = processBuilderback.start();
        exitCode = process_back.waitFor();
        assert exitCode == 0;

        Thread.sleep(1200);

        File f = new File( ocr_front_path + "result_image/" + uuid + ".jpg");


        int i = 0;
        String idNo = "";
        String name = "";
        String birthday = "";
        String tmp = "";
        String hometown = "";
        String address = "";
        String expirydate = "";

        String dantoc = "";
        String tongiao = "";
        String dauhieu = "";

        try {
            FileInputStream fis = new FileInputStream(ocr_front_path + "result_image/" + uuid + ".txt");
            Scanner scanner = new Scanner(fis);


            if (fis != null) {
                while (scanner.hasNextLine()) {
                    if (i == 0) idNo = scanner.nextLine();
                    if (i == 1) name = scanner.nextLine();
                    if (i == 2) birthday = scanner.nextLine();
                    if (i == 3) tmp = scanner.nextLine();
                    if (i == 4) tmp = scanner.nextLine();
                    if (i == 5) hometown = scanner.nextLine();
                    if (i == 6) hometown = hometown + " " + scanner.nextLine();
                    if (i == 7) address = scanner.nextLine();
                    if (i == 8) address = address + " " + scanner.nextLine();
                    i++;
                    if (i > 8) break;
                }
            }

            scanner.close();
        }
        catch (Exception e) {
        }
        try {
            FileInputStream fis_back = new FileInputStream(ocr_back_path + "result_image_back/" + uuid + ".txt");
            Scanner scanner_back = new Scanner(fis_back);

            i = 0;

            File f_back = new File(ocr_back_path + "result_image/" + uuid + ".jpg");
            if (f_back != null) {
                while (scanner_back.hasNextLine()) {
                    if (i == 0) dantoc = scanner_back.nextLine();
                    if (i == 1) tongiao = scanner_back.nextLine();
                    if (i == 2) dauhieu = scanner_back.nextLine();
                    if (i == 3) dauhieu = dauhieu + " " + scanner_back.nextLine();
                    i++;
                    if (i > 4) break;
                }
            }
            scanner_back.close();

        }
        catch (Exception e){

        }
        ocr ocr = new ocr(idNo, name, birthday, hometown, address, expirydate, dantoc, tongiao, dauhieu);

        return ocr;
    }
}