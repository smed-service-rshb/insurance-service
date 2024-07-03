package ru.softlab.efr.services.insurance.services.types;

/**
 * ДТО договора для эл. писем
 */
public class InsuranceEmailedDto {
    private String number;
    private String programName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
}
