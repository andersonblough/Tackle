package com.tackle.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tackle.data.model.BaseModel;
import com.tackle.data.model.Category;
import com.tackle.data.model.TackleEvent;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * @author andersonblough (bill.a@akta.com)
 */
public class TackleData {

    public static final String TAG = "TACKLE";
    public static final String DATABASE_NAME = "tackle.db";
    public static final int DATABASE_VERSION = 0;

    public static void initDB(Context context) {
        Sprinkles sprinkles = Sprinkles.init(context, DATABASE_NAME, DATABASE_VERSION);
        sprinkles.addMigration(getMigration());
    }

    private static Migration getMigration() {
        return new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE " + TackleEvent.TABLE_NAME
                        + "(" + BaseModel.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TackleEvent.COLUMN_TITLE + " TEXT, "
                        + TackleEvent.COLUMN_TYPE + " INTEGER, "
                        + TackleEvent.COLUMN_CATEGORY_ID + " INTEGER, "
                        + TackleEvent.COLUMN_NOTES + " TEXT, "
                        + TackleEvent.COLUMN_STATUS + " TEXT, "
                        + TackleEvent.COLUMN_START_DATE + " INTEGER, "
                        + TackleEvent.COLUMN_END_DATE + " INTEGER, "
                        + TackleEvent.COLUMN_FREQUENCY + " INTEGER, "
                        + TackleEvent.COLUMN_ALL_DAY + " INTEGER, "
                        + TackleEvent.COLUMN_BY_DAY + " TEXT, "
                        + TackleEvent.COLUMN_UNTIL + " INTEGER, "
                        + TackleEvent.COLUMN_COUNT + " INTEGER);");

                db.execSQL("CREATE TABLE " + Category.TABLE_NAME
                        + "(" + Category.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + Category.COLUMN_TITLE + " TEXT, "
                        + Category.COLUMN_COLOR + " TEXT);");

                db.execSQL(Category.RAW_STATEMENT);
            }
        };
    }


}
