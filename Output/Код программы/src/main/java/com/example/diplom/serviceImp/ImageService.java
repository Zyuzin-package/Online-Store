package com.example.diplom.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
@Service
public class ImageService {

    public boolean saveImage(MultipartFile file, String name, String path, String category) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/" + path)));
                stream.write(bytes);
                stream.close();

                System.out.println("Successful load file");
                return true;
            } catch (Exception e) {
                System.out.println("Error load file" + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("File is empty");
            return false;
        }
    }


    public boolean removeImage(String imageUrl) {
        String path = "src/main/resources/static" + imageUrl;
        File file = new File(path);
        if (!file.delete()) {
            return false;
        }
        return true;
    }
}
