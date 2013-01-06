package com.pinalka.dao.impl;

import com.pinalka.dao.ItemDAO;
import com.pinalka.domain.Item;
import org.springframework.stereotype.Repository;

/**
 * DAO implementation for the Item class
 *
 * @author gman
 */
@Repository
public class ItemDAOImpl extends GenericDAOImpl<Item> implements ItemDAO {

    /**
     * Constructor for the DAO class
     */
    public ItemDAOImpl() {
        super("select i from com.pinalka.domain.Item i");
    }
}
