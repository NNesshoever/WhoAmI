package server;

import Dtos.PersonDto;
import Dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class JsonService {

    private static final String FILE_NAME = "peoples.json";

    public static List<PersonDto> loadPersonJson() {
        ObjectMapper json = new ObjectMapper();
        ClassLoader classLoader = JsonService.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(FILE_NAME);

        try {
            List<PersonDto> dto = Arrays.asList(json.readValue(is, PersonDto[].class));
            return dto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
