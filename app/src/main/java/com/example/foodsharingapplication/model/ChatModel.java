package com.example.foodsharingapplication.model;

public class ChatModel {

    private User sender;
    private User reciever;
    private String message;
    private String conversationID;
    private String foodName;

    public ChatModel(User sender, User reciever, String message,  String foodName) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.foodName = foodName;
    }

    public ChatModel(){

    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
