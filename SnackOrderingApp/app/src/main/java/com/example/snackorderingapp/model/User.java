package com.example.snackorderingapp.model;

import java.io.Serializable;

public class User implements Serializable {
    private Long accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private double longitude;
    private double latitude;
    private Long branchId;
    private String birthday;
    private String createdBy;
    private String createdDate;
    private String updatedBy;
    private String updatedDate;

    public User(Long accountId, String firstName, String lastName, String email, String phone,
                String address, Long branchId, String birthday, String createdBy,
                String createdDate, String updatedBy, String updatedDate) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.branchId = branchId;
        this.birthday = birthday;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;

    }

    public User() {

    }

    // Getters
    public Long getAccountId() { return accountId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public Long getBranchId() { return branchId; }
    public String getBirthday() { return birthday; }
    public String getCreatedBy() { return createdBy; }
    public String getCreatedDate() { return createdDate; }
    public String getUpdatedBy() { return updatedBy; }
    public String getUpdatedDate() { return updatedDate; }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Setters
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }
}
