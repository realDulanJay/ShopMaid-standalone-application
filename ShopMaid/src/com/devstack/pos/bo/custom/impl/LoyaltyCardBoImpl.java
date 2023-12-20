package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.LoyaltyCardBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.LoyaltyCardDao;
import com.devstack.pos.dto.LoyaltyCardDto;
import com.devstack.pos.entity.LoyaltyCard;
import com.devstack.pos.enums.DaoType;

import java.sql.SQLException;

public class LoyaltyCardBoImpl implements LoyaltyCardBo {

    private final LoyaltyCardDao loyaltyCardDao = DaoFactory.getInstance().getDao(DaoType.LOYALTY_CARD);

    @Override
    public boolean saveLoyaltyData(LoyaltyCardDto loyaltyCardDto) throws SQLException, ClassNotFoundException {
        return loyaltyCardDao.save(
                new LoyaltyCard(
                        loyaltyCardDto.getCode(),
                        loyaltyCardDto.getCardType(),
                        loyaltyCardDto.getBarcode(),
                        loyaltyCardDto.getEmail()
                )
        );
    }

    @Override
    public LoyaltyCard findCard(String email) throws SQLException, ClassNotFoundException {
        return loyaltyCardDao.findCard(email);
    }
}
