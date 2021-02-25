package server;

import Dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class JsonService {

    private static final String FILE_NAME = "peoples.json";

    public static List<UserDto> loadJson() {
        ObjectMapper json = new ObjectMapper();
        ClassLoader classLoader = JsonService.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream(FILE_NAME);

        try {
            List<UserDto> dto = Arrays.asList(json.readValue(is, UserDto[].class));
            return dto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
