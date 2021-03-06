package com.example.foodsharingapplication.extras;



import android.net.Uri;

import com.example.foodsharingapplication.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class MyData {

    private String foodTitle,foodDescription,foodPickUpDetail,foodPrice;
    private String foodType;
    private String foodTypeCuisine;
    private String Payment;
    private String AvailabilityDays;

    User user = new User();

    private HashMap<String,String> hashMap;

    private ArrayList<Uri> mArrayUri;
    private String mImageUri;


    public MyData(){

    }

    public MyData(String foodTitle, String foodDescription, String foodPickUpDetail, String foodPrice, String foodType, String foodTypeCuisine, String payment, String availabilityDays, User user) {

        this.foodTitle = foodTitle;
        this.foodDescription = foodDescription;
        this.foodPickUpDetail = foodPickUpDetail;
        this.foodPrice = foodPrice;
        this.foodType = foodType;
        this.foodTypeCuisine = foodTypeCuisine;
        Payment = payment;
        AvailabilityDays = availabilityDays;

        this.user = user;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        if(foodTitle.trim().equals("")){
            this.foodTitle = "No title";
        }else {
            this.foodTitle = foodTitle;
        }
    }

    public String getAvailabilityDays() {
        return AvailabilityDays;
    }

    public void setAvailabilityDays(String availabilityDays) {
        AvailabilityDays = availabilityDays;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodPickUpDetail() {
        return foodPickUpDetail;
    }

    public void setFoodPickUpDetail(String foodPickUpDetail) {
        this.foodPickUpDetail = foodPickUpDetail;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodTypeCuisine() {
        return foodTypeCuisine;
    }

    public void setFoodTypeCuisine(String foodTypeCuisine) {
        this.foodTypeCuisine = foodTypeCuisine;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public ArrayList<Uri> getmArrayUri() {
        return mArrayUri;
    }

    public void setmArrayUri(ArrayList<Uri>  mArrayUri) {
        this.mArrayUri = mArrayUri;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
