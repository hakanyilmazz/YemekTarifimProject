package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("id")
    public String id;

    @SerializedName("countryName")
    public String countryName;

    public Country(String selectedCountryId) {
        this.id = selectedCountryId;
    }
}
