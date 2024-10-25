package com.example.snackorderingapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Snack implements Serializable {

    private Integer foodId;

    private String foodName;

    private String imageURL;

    private String description;

    private String ingredients;

    private double price;

    private boolean isAvailable;

    private String category;

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date updatedDate;

    // Constructors, getters, and setters


    public Snack() {
    }

    public Snack(Integer foodId, String foodName, String imageURL, String description, String ingredients, double price, boolean isAvailable, String category, String createdBy, Date createdDate, String updatedBy, Date updatedDate) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.imageURL = imageURL;
        this.description = description;
        this.ingredients = ingredients;
        this.price = price;
        this.isAvailable = isAvailable;
        this.category = category;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "Snack{" +
                "foodId=" + foodId +
                ", foodName='" + foodName + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", category='" + category + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedDate=" + updatedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Snack snack = (Snack) o;
        return Double.compare(price, snack.price) == 0 && isAvailable == snack.isAvailable && Objects.equals(foodId, snack.foodId) && Objects.equals(foodName, snack.foodName) && Objects.equals(imageURL, snack.imageURL) && Objects.equals(description, snack.description) && Objects.equals(ingredients, snack.ingredients) && Objects.equals(category, snack.category) && Objects.equals(createdBy, snack.createdBy) && Objects.equals(createdDate, snack.createdDate) && Objects.equals(updatedBy, snack.updatedBy) && Objects.equals(updatedDate, snack.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, foodName, imageURL, description, ingredients, price, isAvailable, category, createdBy, createdDate, updatedBy, updatedDate);
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
