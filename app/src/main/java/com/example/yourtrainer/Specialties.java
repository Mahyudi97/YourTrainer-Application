package com.example.yourtrainer;

public class Specialties {
    private String specialtiesName;
    private String trainerId;

    public Specialties() {
    }

    public Specialties(String specialtiesName, String trainerId) {
        this.specialtiesName = specialtiesName;
        this.trainerId = trainerId;
    }

    public String getSpecialtiesName() {
        return specialtiesName;
    }

    public void setSpecialtiesName(String specialtiesName) {
        this.specialtiesName = specialtiesName;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }
}
