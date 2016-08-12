package com.talkramer.finalproject.model.Domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.talkramer.finalproject.model.Utils.Helper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yaniv on 10/08/2016.
 */
public class ProductSql {

    final static String PRODUCT_TABLE = "products";
    final static String PRODUCT_TABLE_ID = "_id";
    final static String PRODUCT_TABLE_TYPE = "type";
    final static String PRODUCT_TABLE_DESCRIPTION = "description";
    final static String PRODUCT_TABLE_PRICE = "price";
    final static String PRODUCT_TABLE_FOR_WHOM ="for_whom";
    final static String PRODUCT_TABEL_SELLER_ID = "seller_id";
    final static String PRODUCT_TABLE_DELETED = "deleted";
    final static String PRODUCT_LAST_UPDATED = "last_updated";
    final static String PRODUCT_BUYER = "buyer";
    final static String PRODUCT_SELLER_EMAIL = "seller_email";

    final static String TEXT_TYPE = " TEXT";
    final static String INTEGER_TYPE = " INTEGER";
    final static  String SEPERATOR = ",";


    static public void create(SQLiteDatabase db) {
        String query = "create table " + PRODUCT_TABLE + " (" +
                PRODUCT_TABLE_ID + TEXT_TYPE +" PRIMARY KEY," +
                PRODUCT_TABLE_TYPE + TEXT_TYPE + SEPERATOR +
                PRODUCT_TABLE_DESCRIPTION + TEXT_TYPE + SEPERATOR +
                PRODUCT_TABLE_PRICE + INTEGER_TYPE + SEPERATOR +
                PRODUCT_TABLE_FOR_WHOM + TEXT_TYPE + SEPERATOR +
                PRODUCT_TABEL_SELLER_ID + TEXT_TYPE + SEPERATOR +
                PRODUCT_TABLE_DELETED + INTEGER_TYPE +SEPERATOR +
                PRODUCT_LAST_UPDATED + TEXT_TYPE +SEPERATOR +
                PRODUCT_BUYER +TEXT_TYPE + SEPERATOR +
                PRODUCT_SELLER_EMAIL + TEXT_TYPE

                +");";

        db.execSQL(query);
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + PRODUCT_TABLE + ";");
    }

    public static List<Product> getAllProducts(SQLiteDatabase db) {
        Cursor cursor = db.query(PRODUCT_TABLE, null, null , null, null, null, null);
        List<Product> products = new LinkedList<Product>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(PRODUCT_TABLE_ID);
            int typeIndex = cursor.getColumnIndex(PRODUCT_TABLE_TYPE);
            int descriptionIndex = cursor.getColumnIndex(PRODUCT_TABLE_DESCRIPTION);
            int priceIndex = cursor.getColumnIndex(PRODUCT_TABLE_PRICE);
            int forWhomIndex = cursor.getColumnIndex(PRODUCT_TABLE_FOR_WHOM);
            int sellerIdIndex = cursor.getColumnIndex(PRODUCT_TABEL_SELLER_ID);
            int deletedIndex = cursor.getColumnIndex(PRODUCT_TABLE_DELETED);
            int lastUpdateIndex = cursor.getColumnIndex(PRODUCT_LAST_UPDATED);
            int buyerIndex = cursor.getColumnIndex(PRODUCT_BUYER);
            int sellerEmailIndex = cursor.getColumnIndex(PRODUCT_SELLER_EMAIL);

            do {
                String id = cursor.getString(idIndex);
                String type = cursor.getString(typeIndex);
                String description = cursor.getString(descriptionIndex);
                int price = cursor.getInt(priceIndex);
                String forWhom = cursor.getString(forWhomIndex);
                String sellerId = cursor.getString(sellerIdIndex);
                int deleted = cursor.getInt(deletedIndex);
                String lastUpdated = cursor.getString(lastUpdateIndex);
                String buyer = cursor.getString(buyerIndex);
                String sellerEmail = cursor.getString(sellerEmailIndex);

                Helper.ProductType newType = Helper.ProductType.valueOf(type);
                Helper.Customers newForWhom = Helper.Customers.valueOf(forWhom);
                boolean deletedBool = deleted == 1;

                Product pr = new Product(id, newType, description, price, newForWhom, sellerId, sellerEmail, null);
                pr.setDeleted(deletedBool);
                pr.setLastUpdated(lastUpdated);
                pr.setBuyerEmail(buyer);

                products.add(pr);
            } while (cursor.moveToNext());
        }
        return products;
    }

    public static Product getProductById(SQLiteDatabase db, String id) {
        String where = PRODUCT_TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(PRODUCT_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(PRODUCT_TABLE_ID);
            int typeIndex = cursor.getColumnIndex(PRODUCT_TABLE_TYPE);
            int descriptionIndex = cursor.getColumnIndex(PRODUCT_TABLE_DESCRIPTION);
            int priceIndex = cursor.getColumnIndex(PRODUCT_TABLE_PRICE);
            int forWhomIndex = cursor.getColumnIndex(PRODUCT_TABLE_FOR_WHOM);
            int sellerIdIndex = cursor.getColumnIndex(PRODUCT_TABEL_SELLER_ID);
            int deletedIndex = cursor.getColumnIndex(PRODUCT_TABLE_DELETED);
            int lastUpdateIndex = cursor.getColumnIndex(PRODUCT_LAST_UPDATED);
            int buyerIndex = cursor.getColumnIndex(PRODUCT_BUYER);
            int sellerEmailIndex = cursor.getColumnIndex(PRODUCT_SELLER_EMAIL);


            String productId = cursor.getString(idIndex);
            String type = cursor.getString(typeIndex);
            String description = cursor.getString(descriptionIndex);
            int price = cursor.getInt(priceIndex);
            String forWhom = cursor.getString(forWhomIndex);
            String sellerId = cursor.getString(sellerIdIndex);
            int deleted = cursor.getInt(deletedIndex);
            String lastUpdated = cursor.getString(lastUpdateIndex);
            String buyer = cursor.getString(buyerIndex);
            String sellerEmail = cursor.getString(sellerEmailIndex);


            Helper.ProductType newType = Helper.ProductType.valueOf(type);
            Helper.Customers newForWhom = Helper.Customers.valueOf(forWhom);
            boolean deletedBool = deleted == 1;

            Product pr = new Product(productId, newType, description, price, newForWhom, sellerId, sellerEmail, null);
            pr.setDeleted(deletedBool);
            pr.setLastUpdated(lastUpdated);
            pr.setBuyerEmail(buyer);
            return pr;
        }
        return null;
    }

    public static void add(SQLiteDatabase db, Product product) {
        ContentValues values = new ContentValues();
        try {
            values.put(PRODUCT_TABLE_ID, product.getId());
            values.put(PRODUCT_TABLE_TYPE, product.getType().toString());
            values.put(PRODUCT_TABLE_DESCRIPTION, product.getDescription());
            values.put(PRODUCT_TABLE_PRICE, product.getPrice());
            values.put(PRODUCT_TABLE_FOR_WHOM, product.getForWhom().toString());
            values.put(PRODUCT_TABEL_SELLER_ID, product.getSellerId());
            values.put(PRODUCT_TABLE_DELETED, product.getDeleted()? 1:0);
            values.put(PRODUCT_LAST_UPDATED, product.getLastUpdated());
            values.put(PRODUCT_BUYER, product.getBuyerEmail());
            values.put(PRODUCT_SELLER_EMAIL, product.getSellerEmail());
             db.insertWithOnConflict(PRODUCT_TABLE, PRODUCT_TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        }catch (Exception ex)
        {
            Log.d("TAG", "add SQL exception: " + ex.getMessage());
        }
    }

    public static boolean deleteById(SQLiteDatabase db, String productId)
    {
        //db.delete(PRODUCT_TABLE, PRODUCT_TABLE_ID+"=?", new String[] {productId});
        return db.delete(PRODUCT_TABLE, PRODUCT_TABLE_ID + "=" + productId, null) > 0;
    }

    public static String getLastUpdateDate(SQLiteDatabase db){
        return LastUpdateSql.getLastUpdate(db,PRODUCT_TABLE);
    }
    public static void setLastUpdateDate(SQLiteDatabase db, String date){
        LastUpdateSql.setLastUpdate(db,PRODUCT_TABLE, date);
    }
}
