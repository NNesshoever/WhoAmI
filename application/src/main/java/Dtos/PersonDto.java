package Dtos;

import org.example.App;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class PersonDto implements Serializable {
    private int id;
    private String imagePath;
    private String base64Image;
    private String surname;
    private String lastname;
    private int age;
    private String birthdate;
    private String height;
    private String hairColor;
    private String eyeColor;
    private String job;
    private ArrayList<String> accessories;
    private ArrayList<String> personality = new ArrayList<>();
    private String relationship;
    private String nationality;
    private ArrayList<String> production = new ArrayList<>();
    private String gender;

    public PersonDto() {

    }

    public PersonDto(int id, String imagePath, String surname, String lastname, String birthdate, String height, String hairColor, String eyeColor, String job, ArrayList<String> accessories, ArrayList<String> personality, String relationship, String nationality, ArrayList<String> production, String gender) {
        this.id = id;
        this.imagePath = imagePath;
        this.surname = surname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.height = height;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.job = job;
        this.accessories = accessories;
        this.personality = personality;
        this.relationship = relationship;
        this.nationality = nationality;
        this.production = production;
        this.gender = gender;

        setCalculatedAge();
        setBase64Image();
    }

    private void setBase64Image() {
        if (!imagePath.isEmpty()) {
            try {
                base64Image = Base64.getEncoder().encodeToString(App.class.getResourceAsStream(imagePath).readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCalculatedAge() {
        String[] splitBirthDate = birthdate.split("\\.");
        LocalDate birthdate = LocalDate.of(Integer.parseInt(splitBirthDate[2]), Integer.parseInt(splitBirthDate[1]), Integer.parseInt(splitBirthDate[0]));
        LocalDate now = LocalDate.now();
        setAge(Period.between(birthdate, now).getYears());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public ArrayList<String> getAccessories() {
        return accessories;
    }

    public void setAccessories(ArrayList<String> accessories) {
        this.accessories = accessories;
    }

    public ArrayList<String> getPersonality() {
        return personality;
    }

    public void setPersonality(ArrayList<String> personality) {
        this.personality = personality;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public ArrayList<String> getProduction() {
        return production;
    }

    public void setProduction(ArrayList<String> production) {
        this.production = production;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String generateReadableString(String[] arr){
        String result = "";
        for (String s : arr) {
            result = s + ", ";
        }
        if(arr.length > 0){
            result = result.substring(0, result.length()-2);
        }
        return  result;
    }

    @Override
    public String toString() {
        setCalculatedAge();
        setBase64Image();
        return
                surname + " " + lastname +
                        " (" + gender + ", " + age + " Jahre, " + height + ", " + hairColor + ")\n" +
                        "Augenfarbe: " + eyeColor +
                        "\nBeziehungsstatus: " + relationship +
                        "\nNationalität: " + nationality +
                        "\nPersönlichkeit: " + generateReadableString(personality.toArray(new String[]{})) +
                        "\nAccessories: " + generateReadableString(accessories.toArray(new String[]{})) +
                        "\nBeruf: " + job +
                        "\nWerke: " + generateReadableString(production.toArray(new String[]{}));
    }
}

