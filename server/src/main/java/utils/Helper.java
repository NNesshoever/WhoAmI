package utils;

import models.Person;

import java.util.List;
import java.util.Random;

public class Helper {

    public static Person getRandomPerson() {
        List<Person> persons = JsonService.loadJson();
        Random rnd = new Random();
        int max = persons.size();
        long seed = System.nanoTime();
        seed ^= (seed << rnd.nextInt());
        seed ^= (seed >> rnd.nextInt());
        String SeedString = String.valueOf(seed);
        int i = 0;
        int temp;
        do {
            temp = Character.digit(SeedString.charAt(i), 10);
            ++i;

        } while (temp > max - 1 && temp > 0 && i <= SeedString.length());
        return persons.get(temp);
    }
}
