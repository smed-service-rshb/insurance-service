package ru.softlab.efr.services.insurance.services.types;

import java.time.LocalDate;

public class PersonalEmailedDto {

    private String name;
    private String surName;
    private String thirdName;
    private LocalDate birthDate;

    public String getName() {
        return this.name;
    }
    public String getSurName() {
        return this.surName;
    }
    public String getThirdName() {
        return this.thirdName;
    }
    public LocalDate getBirthDate() {
        return this.birthDate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurName(String surName) {
        this.surName = surName;
    }
    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
