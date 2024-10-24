package com.example.snackorderingapp.model;

import java.io.Serializable;

public class Order implements Serializable {
    private Long orderId;
    private String orderNumber;
    private double totalMoney;
    private String status;
    private Long accountId;
    private Long branchId;
    private String createdBy;
    private String createdDate;
    private String updatedBy;
    private String updatedDate;

    public Order(Long orderId, String orderNumber, double totalMoney, String status,
                 Long accountId, Long branchId, String createdBy, String createdDate,
                 String updatedBy, String updatedDate) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.totalMoney = totalMoney;
        this.status = status;
        this.accountId = accountId;
        this.branchId = branchId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    // Getters and setters for all fields
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public double getTotalMoney() { return totalMoney; }
    public void setTotalMoney(double totalMoney) { this.totalMoney = totalMoney; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public String getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(String updatedDate) { this.updatedDate = updatedDate; }
}
