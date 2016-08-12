package com.talkramer.finalproject.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.talkramer.finalproject.model.Domain.LastUpdateSql;
import com.talkramer.finalproject.model.Domain.ProductSql;


/**
 * Created by Yaniv on 10/08/2016.
 */
public class ModelSql {
    final static int VERSION = 4;
    SqlHelper sqlDb;

    public ModelSql(Context context)
    {
        sqlDb = new SqlHelper(context);
    }

    public SQLiteDatabase getWritableDB(){
        return sqlDb.getWritableDatabase();
    }

    public SQLiteDatabase getReadbleDB() {
        return sqlDb.getReadableDatabase();
    }


    class SqlHelper extends SQLiteOpenHelper {
        public SqlHelper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            ProductSql.create(db);
            LastUpdateSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("TAG", "Upgrading DB to version: " +VERSION);
            ProductSql.drop(db);
            LastUpdateSql.drop(db);
            onCreate(db);
        }
    }
}
