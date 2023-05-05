package com.dp.hloworld.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class FileUploadHelper {

    //		public final String UPLOAD_DIR = "C:\\Users\\debam\\Documents\\SpringRestDataMySQL\\SpringRestDataMySQL\\src\\main\\resources\\template\\static\\";
    public final String UPLOAD_DIR_IMG = new ClassPathResource("static\\image").getFile().getAbsolutePath();
    public final String UPLOAD_DIR_VDO = new ClassPathResource("static\\videos").getFile().getAbsolutePath();

    public FileUploadHelper() throws IOException {

    }

    public static String getRandomPath() {
        Date date = new Date();
        Random rand = new Random();
        String rand_int = String.valueOf(rand.nextInt(999999999));
        String strDateFormat = "ddMMyyyyhhmmssa_" + rand_int;
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public String uploadFile(MultipartFile multipartFile) {
        boolean f = false;

        try {
            String random_path = "_" + getRandomPath()+".";
            String final_file_name = multipartFile.getOriginalFilename().split("\\.")[0]+random_path+multipartFile.getOriginalFilename().split("\\.")[1];
            Files.copy(multipartFile.getInputStream(), Paths.get(UPLOAD_DIR_IMG + "\\" + final_file_name), StandardCopyOption.REPLACE_EXISTING);
            f = true;
            System.out.println(UPLOAD_DIR_IMG + "\\" + final_file_name);
            return final_file_name;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String uploadMovie(MultipartFile multipartFile) {
        boolean f = false;

        try {
            String random_path = "_" + getRandomPath()+".";
            String final_file_name = multipartFile.getOriginalFilename().split("\\.")[0]+random_path+multipartFile.getOriginalFilename().split("\\.")[1];
            Files.copy(multipartFile.getInputStream(), Paths.get(UPLOAD_DIR_IMG + "\\" + final_file_name), StandardCopyOption.REPLACE_EXISTING);
            f = true;
            System.out.println(UPLOAD_DIR_IMG + "\\" + final_file_name);

            return final_file_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
