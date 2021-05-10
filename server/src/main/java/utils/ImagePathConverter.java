package utils;

import Dtos.PersonDto;
import server.Application;

import java.io.IOException;
import java.util.Base64;

public class ImagePathConverter {

    public static String ImagePathToBaseString(String imagePath){
        String base64Image = null;
        try {
            base64Image = Base64.getEncoder().encodeToString(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagePath).readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Image;
    }
}
