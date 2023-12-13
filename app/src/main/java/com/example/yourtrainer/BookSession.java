package com.example.yourtrainer;

public class BookSession {
    private String bookSessionType;
    private String bookSessionStatus;
    private String trainerId;
    private  String clientId;
    private String dateSession;

    public BookSession(){

    }
    public BookSession(String bookSessionType, String bookSessionStatus, String trainerId, String clientId,String dateSession) {
        this.bookSessionType = bookSessionType;
        this.bookSessionStatus = bookSessionStatus;
        this.trainerId = trainerId;
        this.clientId = clientId;
        this.dateSession =  dateSession;
    }

    public String getDateSession() {
        return dateSession;
    }

    public void setDateSession(String dateSession) {
        this.dateSession = dateSession;
    }

    public String getBookSessionType() {
        return bookSessionType;
    }

    public void setBookSessionType(String bookSessionType) {
        this.bookSessionType = bookSessionType;
    }

    public String getBookSessionStatus() {
        return bookSessionStatus;
    }

    public void setBookSessionStatus(String bookSessionStatus) {
        this.bookSessionStatus = bookSessionStatus;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
