package com.example.foodsharingapplication.model;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class UserUploadFoodModel implements Parcelable {

    //public static final String PAYPAL_CLIENT_ID="AVULyNmcckcTupOFuhbixw6Y9-eLfvfFeIWWA-oWTDoDLmYBywtiJTJLsfEcfhklndSEfIVgMv0DXASr";
    public static final String PAYPAL_CLIENT_ID = "Aft7p51eX-yqZs35DdKeo7zTAHzREns2FbsNtXP9vFSqHRAl0Liyi6lf1IO85DE8hMvWRnHbF1Ri9MsE";
    public static final Creator<UserUploadFoodModel> CREATOR = new Creator<UserUploadFoodModel>() {
        @Override
        public UserUploadFoodModel createFromParcel(Parcel in) {
            return new UserUploadFoodModel(in);
        }

        @Override
        public UserUploadFoodModel[] newArray(int size) {
            return new UserUploadFoodModel[size];
        }
    };
    User user = new User();
    private String adId;
    private String foodTitle, foodDescription, foodPickUpDetail, foodPrice;
    private String foodType;
    private String foodTypeCuisine;
    private String Payment;
    private String AvailabilityDays;
    private String paymentDetails;
    private float rating;
    private User foodPostedBy;
    private String foodUploadDateAndTime;
    private double latitude, longitude;
    private HashMap<String, String> hashMap;
    private ArrayList<String> mArrayString;
    private ArrayList<Uri> mArrayUri;
    private String mImageUri;

    public UserUploadFoodModel() {

    }

    public UserUploadFoodModel(Parcel parcel) {

        adId = parcel.readString();
        foodTitle = parcel.readString();
        foodDescription = parcel.readString();
        foodPickUpDetail = parcel.readString();
        foodPrice = parcel.readString();
        foodType = parcel.readString();
        foodTypeCuisine = parcel.readString();
        Payment = parcel.readString();
        AvailabilityDays = parcel.readString();
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<String> getmArrayString() {
        return mArrayString;
    }

    public void setmArrayString(ArrayList<String> mArrayString) {
        this.mArrayString = mArrayString;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        if (foodTitle.trim().equals("")) {
            this.foodTitle = "No title";
        } else {
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

    public void setmArrayUri(ArrayList<Uri> mArrayUri) {
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

    public User getFoodPostedBy() {
        return foodPostedBy;
    }

    public void setFoodPostedBy(User foodPostedBy) {
        this.foodPostedBy = foodPostedBy;
    }

    public String getFoodUploadDateAndTime() {
        return foodUploadDateAndTime;
    }

    public void setFoodUploadDateAndTime(String foodUploadDateAndTime) {
        this.foodUploadDateAndTime = foodUploadDateAndTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(foodTitle);
        dest.writeString(foodDescription);
        dest.writeString(foodPickUpDetail);
        dest.writeString(foodPrice);
        dest.writeString(foodType);
        dest.writeString(foodTypeCuisine);
        dest.writeString(Payment);
        dest.writeString(AvailabilityDays);

    }

}
