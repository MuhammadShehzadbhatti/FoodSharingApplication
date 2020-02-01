package com.example.foodsharingapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class User implements Parcelable {

    private @ServerTimestamp Date timestamp;
    private UserUploadFoodModel uploadModel;
    private String userId;
    private String userName;
    private String userPhoneNumber;
    private String userEmail;
    private String userProfilePicName;
    private String userProfilePicUrl;
    private String sex;
    private String userPassword;
    private String userDateOfBirth;
    private String userCountry;
    private String userCountryCurrency;
    private String userCountryISO;
    private String userCountryDialCode;
    private Boolean userWithFb;

    public User(){

    }

    protected User(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        userEmail = in.readString();
        userPhoneNumber = in.readString();
        userProfilePicName = in.readString();
        userProfilePicUrl = in.readString();
        sex = in.readString();
        userDateOfBirth = in.readString();
        userCountry = in.readString();
        userCountryCurrency = in.readString();
        userCountryISO = in.readString();
        userCountryDialCode = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserUploadFoodModel getUploadModel() {
        return uploadModel;
    }

    public void setUploadModel(UserUploadFoodModel uploadModel) {
        this.uploadModel = uploadModel;
    }

    public String getUserCountryCurrency() {
        return userCountryCurrency;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserCountryCurrency(String userCountryCurrency) {
        this.userCountryCurrency = userCountryCurrency;
    }

    public String getUserCountryISO() {
        return userCountryISO;
    }

    public void setUserCountryISO(String userCountryISO) {
        this.userCountryISO = userCountryISO;
    }

    public String getUserCountryDialCode() {
        return userCountryDialCode;
    }

    public void setUserCountryDialCode(String userCountryDialCode) {
        this.userCountryDialCode = userCountryDialCode;
    }

    public Boolean getUserWithFb() {
        return userWithFb;
    }

    public void setUserWithFb(Boolean userWithFb) {
        this.userWithFb = userWithFb;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public void setUserDateOfBirth(String userDateOfBirth) {
        this.userDateOfBirth = userDateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserProfilePicName() {
        return userProfilePicName;
    }

    public void setUserProfilePicName(String userProfilePicName) {
        this.userProfilePicName = userProfilePicName;
    }

    public String getUserProfilePicUrl() {
        return userProfilePicUrl;
    }

    public void setUserProfilePicUrl(String userProfilePicUrl) {
        System.out.println("user profile pic    "+userProfilePicUrl);
        this.userProfilePicUrl = userProfilePicUrl;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }



    public String getUserName() {

        return userName;
    }

    public void setUserName(String username) {

        this.userName = username;
    }

    public String getUserEmail() {

        return userEmail;
    }

    public void setUserEmail(String userEmail) {

        this.userEmail = userEmail;
    }

    public String getUserPassword() {

        return userPassword;
    }

    public void setUserPassword(String userPassword) {

        this.userPassword = userPassword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userPhoneNumber);
        dest.writeString(userProfilePicName);
        dest.writeString(userProfilePicUrl);
        dest.writeString(sex);
        dest.writeString(userDateOfBirth);
        dest.writeString(userCountry);
        dest.writeString(userCountryCurrency);
        dest.writeString(userCountryISO);
        dest.writeString(userCountryDialCode);
    }
}
