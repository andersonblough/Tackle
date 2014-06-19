package com.tackle.data.model;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * @author andersonblough (bill.a@akta.com)
 */
@Table(Category.TABLE_NAME)
public class Category extends Model implements CategoryColumns {

    public static final String CATEGORY_INBOX = "Inbox";
    public static final String RAW_STATEMENT = "INSERT INTO " + Category.TABLE_NAME +
            "(" + COLUMN_TITLE + ", " + COLUMN_COLOR + ") VALUES('" + CATEGORY_INBOX + "', '#B3B3B3')";

    public static final String TABLE_NAME = "categories";

    @Key
    @Column(COLUMN_TITLE)
    private String title;
    @Column(COLUMN_COLOR)
    private String color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
