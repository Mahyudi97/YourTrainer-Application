package com.example.yourtrainer;

public class Client {
    private String clientFullName;
    private String clientUsername;
    private String clientPhone;
    private String clientAddress;
    private String clientGender;
    private String clientEmail;
    private String imageId;

    public Client(){

    }
    public Client( String clientFullName, String clientUsername, String clientPhone, String clientAddress, String clientGender, String clientEmail,String imageId) {
        this.clientFullName = clientFullName;
        this.clientUsername = clientUsername;
        this.clientPhone = clientPhone;
        this.clientAddress = clientAddress;
        this.clientGender = clientGender;
        this.clientEmail = clientEmail;
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientGender() {
        return clientGender;
    }

    public void setClientGender(String clientGender) {
        this.clientGender = clientGender;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
