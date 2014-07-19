package com.tackle.data.service;

import com.tackle.data.model.Category;

import se.emilsjolander.sprinkles.Query;

/**
 * Created by andersonblough on 7/6/14.
 */
public class CategoryService {

    public Category findCategoryByID(long id) {
        return Query.one(Category.class,
                "select * from " + Category.TABLE_NAME + " where _id=?", "" + id).get();
    }
}
