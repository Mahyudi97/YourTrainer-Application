package com.example.yourtrainer;

public class PersonalTrainer {
    private String trainerFullName;
    private String trainerUsername;
    private String trainerPhone;
    private String trainerAddress;
    private String trainerGender;
    private String trainerEmail;
    private String imageId;

    public PersonalTrainer(){

    }
    public PersonalTrainer(String trainerFullName, String trainerUsername, String trainerPhone, String trainerAddress, String trainerGender, String trainerEmail, String imageId) {

        this.trainerFullName = trainerFullName;
        this.trainerUsername = trainerUsername;
        this.trainerPhone = trainerPhone;
        this.trainerAddress = trainerAddress;
        this.trainerGender = trainerGender;
        this.trainerEmail = trainerEmail;
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTrainerFullName() {
        return trainerFullName;
    }

    public void setTrainerFullName(String trainerFullName) {
        this.trainerFullName = trainerFullName;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getTrainerPhone() {
        return trainerPhone;
    }

    public void setTrainerPhone(String trainerPhone) {
        this.trainerPhone = trainerPhone;
    }

    public String getTrainerAddress() {
        return trainerAddress;
    }

    public void setTrainerAddress(String trainerAddress) {
        this.trainerAddress = trainerAddress;
    }

    public String getTrainerGender() {
        return trainerGender;
    }

    public void setTrainerGender(String trainerGender) {
        this.trainerGender = trainerGender;
    }

    public String getTrainerEmail() {
        return trainerEmail;
    }

    public void setTrainerEmail(String trainerEmail) {
        this.trainerEmail = trainerEmail;
    }

}
