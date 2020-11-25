package com.example.lib;

public class Movie {
    private String name;
    private String picture;
    private String description;
    private int rating;
    private int length;
    private int ageRestriction;
    private boolean isActive;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getAgeRestriction() {
        return this.ageRestriction;
    }

    public void setAgeRestriction(int ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public boolean isIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }


    public Movie(String name, String picture, String description, int rating, int length, int ageRestriction,
            boolean isActive) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.rating = rating;
        this.length = length;
        this.ageRestriction = ageRestriction;
        this.isActive = isActive;
    }
}
