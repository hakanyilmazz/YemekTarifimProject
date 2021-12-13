package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class RequestUser {
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
    public byte[] profilePhoto;

    public RequestUser(String name, String surname, int age, Country country, boolean isOnline, byte[] binaryImage) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.country = country;
        this.isOnline = isOnline;
        this.profilePhoto = binaryImage;
    }

    public RequestUser(String name, String surname, int age, Country country, boolean isOnline) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.country = country;
        this.isOnline = isOnline;
    }

    public RequestUser(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public RequestUser(String id) {
        this.id = id;
    }

    public RequestUser() {
    }
}

