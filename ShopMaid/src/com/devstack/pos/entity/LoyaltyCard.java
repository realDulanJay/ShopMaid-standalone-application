package com.devstack.pos.entity;

import com.devstack.pos.enums.CardType;

public class LoyaltyCard implements SuperEntity {
    private int Code;
    private CardType cardType;
    private String barcode;
    private String email;

    public LoyaltyCard() {
    }

    public LoyaltyCard(int code, CardType cardType, String barcode, String email) {
        Code = code;
        this.cardType = cardType;
        this.barcode = barcode;
        this.email = email;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoyaltyCard{" +
                "Code=" + Code +
                ", cardType=" + cardType +
                ", barcode='" + barcode + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
