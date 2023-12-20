package com.devstack.pos.enums;

public enum CardType {
    PLATINUM("platinum"),GOLD("gold"),SILVER("silver");

    private String cardType;

    CardType(String cardType) {
        this.cardType = cardType;
    }
}
