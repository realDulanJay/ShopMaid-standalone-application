package com.devstack.pos.bo.custom;

import com.devstack.pos.bo.SuperBo;
import com.devstack.pos.dto.LoyaltyCardDto;
import com.devstack.pos.entity.LoyaltyCard;

import java.sql.SQLException;

public interface LoyaltyCardBo extends SuperBo {
    public boolean saveLoyaltyData(LoyaltyCardDto loyaltyCardDto) throws SQLException, ClassNotFoundException;

    public LoyaltyCard findCard(String email) throws SQLException, ClassNotFoundException;
}
