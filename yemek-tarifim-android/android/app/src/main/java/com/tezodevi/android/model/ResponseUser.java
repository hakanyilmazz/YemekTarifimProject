package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class ResponseUser {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("surname")
    public String surname;

    @SerializedName("age")
    public Integer age;

    @SerializedName("country")
    public Country country;

    @SerializedName("isOnline")
    public Boolean isOnline;

    @SerializedName("profilePhoto")
    public String profilePhoto;

    @SerializedName("friends")
    public Set<ResponseUser> friends;

    public ResponseUser(String name, String surname, Integer age, Country country, Boolean isOnline, String profilePhoto) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.country = country;
        this.isOnline = isOnline;
        this.profilePhoto = profilePhoto;
    }

}
