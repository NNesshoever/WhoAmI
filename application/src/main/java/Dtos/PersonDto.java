package Dtos;

import java.io.Serializable;
import java.util.ArrayList;

public class PersonDto implements Serializable {
    private int id;
    private String surname;
    private String lastname;
    private int age;
    private String height;
    private String hairColor;
    private String eyeColor;
    private String job;
    private String accessoires;
    private ArrayList<String> personality = new ArrayList<>();
    private String relationship;
    private String nationality;
    private ArrayList<String> production = new ArrayList<>();
    private String gender;

    public PersonDto() {
    }

    public PersonDto(int id, String surname, String lastname, int age, String height, String hairColor, String eyeColor, String job, String accessoires, ArrayList<String> personality, String relationship, String nationality, ArrayList<String> production, String gender) {
        this.id = id;
        this.surname = surname;
        this.lastname = lastname;
        this.age = age;
        this.height = height;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.job = job;
        this.accessoires = accessoires;
        this.personality = personality;
        this.relationship = relationship;
        this.nationality = nationality;
        this.production = production;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAccessoires() {
        return accessoires;
    }

    public void setAccessoires(String accessoires) {
        this.accessoires = accessoires;
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

    @Override
    public String toString() {
        return
                "Surname='" + surname +
                ", Lastname='" + lastname +
                ", Age=" + age +
                ", Height='" + height +
                ", HairColor='" + hairColor +
                ", Eye Color='" + eyeColor +
                ", Job='" + job +
                ", Accessories='" + accessoires +
                ", Personality=" + personality +
                ", Relationship='" + relationship +
                ", Nationality='" + nationality +
                ", Production=" + production +
                ", Gender='" + gender;
    }
}

