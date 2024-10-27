package com.example.snackorderingapp.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Snack snack;
    private int amount;

    public CartItem(Snack snack, int amount) {
        this.snack = snack;
        this.amount = amount;
    }

    public Snack getSnack() {
        return snack;
    }

    public void setSnack(Snack snack) {
        this.snack = snack;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}