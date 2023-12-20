package com.devstack.pos.dao.custom;

import com.devstack.pos.dao.CrudDao;
import com.devstack.pos.entity.LoyaltyCard;
import com.devstack.pos.enums.CardType;

import java.sql.SQLException;


public interface LoyaltyCardDao extends CrudDao<LoyaltyCard, Integer > {
    public LoyaltyCard findCard(String email) throws SQLException, ClassNotFoundException;
}
