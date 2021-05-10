package utils;

import models.Person;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class JsonService {

    private static final String FILE_NAME = "peoples.json";

    public static List<Person> loadJson() {
        ObjectMapper json = new ObjectMapper();
        ClassLoader classLoader = JsonService.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(FILE_NAME);

        try {
            List<Person> dto = Arrays.asList(json.readValue(is, Person[].class));
            fillBaseString(dto);
            return dto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void fillBaseString(List<Person> persons){
        for (Person person: persons){
            person.setBase64Image(ImagePathConverter.ImagePathToBaseString(person.getImagePath()));
        }
    }
}
