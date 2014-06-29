package com.tackle.data.model;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrement;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class BaseModel extends Model {

    public static final String ID = "_id";

    public BaseModel() {
        super();
    }

    @AutoIncrement
    @Key
    @Column(ID)
    private long id;

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof TackleEvent) && (((TackleEvent) o).getId() == getId());
    }
}
