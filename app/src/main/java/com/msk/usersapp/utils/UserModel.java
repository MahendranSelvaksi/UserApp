package com.msk.usersapp.utils;

/**
 * Created by Value Stream Technologies on 21-06-2018.
 */
public class UserModel {

    private int id,totalPage;
    private String firstName, lastName, imageURL, adapterType;

    public UserModel(String adapterType) {
        this.adapterType = adapterType;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getAdapterType() {
        return adapterType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
