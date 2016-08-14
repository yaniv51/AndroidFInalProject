package com.talkramer.finalproject.model.Domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.talkramer.finalproject.model.Utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
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
        Cursor cursor;
        List<Product> products;
        String where;

        where = PRODUCT_TABLE_DELETED + " = ? and " + PRODUCT_BUYER + " == ?";
        String[] args = {"0", ""};

        cursor = db.query(PRODUCT_TABLE, null, where , args, null, null, null);
        products = getProductsByCursor(cursor);
        return products;
    }

    public static List<Product> getProductsBySeller(SQLiteDatabase db, String sellerId) {
        String where;
        Cursor cursor;
        List<Product> products;

        String[] args = {sellerId};
        where = PRODUCT_TABEL_SELLER_ID + " = ?";
        cursor = db.query(PRODUCT_TABLE, null, where, args, null, null, null);

        products = getProductsByCursor(cursor);
        return products;
    }

    public static List<Product> getSaleHistoryForUser(SQLiteDatabase db, String sellerId) {
        String where;
        Cursor cursor;
        List<Product> products;

        String[] args = {sellerId, ""};
        where = PRODUCT_TABEL_SELLER_ID + " = ? and " + PRODUCT_BUYER + " != ?" ;
        cursor = db.query(PRODUCT_TABLE, null, where, args, null, null, null);

        products = getProductsByCursor(cursor);
        return products;
    }

    public static List<Product> getProductsByBuyer(SQLiteDatabase db, String buyerId) {
        String where;
        Cursor cursor;
        List<Product> products;

        String[] args = {buyerId};
        where = PRODUCT_BUYER + " = ?";
        cursor = db.query(PRODUCT_TABLE, null, where, args, null, null, null);

        products = getProductsByCursor(cursor);
        return products;
    }

    public static List<Product> getProductsByCustomSearch(SQLiteDatabase db, HashMap<String, String> filter)
    {
        String where;
        ArrayList<String> args;
        List<Product> products;
        StringBuilder queryBuilder;
        Cursor cursor;
        int counter;

        args = new ArrayList<String>();
        queryBuilder = new StringBuilder();
        counter = 0;
        for(String key :filter.keySet())
        {
            String query;
            if(counter > 0)
                queryBuilder.append(" and ");

            String value = filter.get(key);
            if(key.compareTo(Helper.PRICE) == 0)
            {
                String[] prices = value.split("-");
                query = PRODUCT_TABLE_PRICE + " > ? and " +PRODUCT_TABLE_PRICE+" < ?";
                args.add(prices[0]);
                args.add(prices[1]);
                queryBuilder.append(query);
            }
            else if(key.compareTo(Helper.GENDER) == 0)
            {
                query = PRODUCT_TABLE_FOR_WHOM + " = ?";
                args.add(value);
                queryBuilder.append(query);
            }
            else if(key.compareTo(Helper.TYPE) == 0)
            {
                query = PRODUCT_TABLE_TYPE + " = ?";
                args.add(value);
                queryBuilder.append(query);
            }
            else if(key.compareTo(Helper.DESCRIPTION) == 0)
            {
                query = PRODUCT_TABLE_DESCRIPTION + " LIKE ?";
                value = "%"+value+"%";
                args.add(value);
                queryBuilder.append(query);
            }
            counter++;
        }

        queryBuilder.append(" and "+PRODUCT_TABLE_DELETED + " = ? and " + PRODUCT_BUYER + " = ?");
        args.add("0");
        args.add("");

        where = queryBuilder.toString();
        String[] newArgs = new String[args.size()];
        newArgs = args.toArray(newArgs);
        cursor = db.query(PRODUCT_TABLE, null, where, newArgs, null, null, null);
        products = getProductsByCursor(cursor);
        return  products;
    }

    private static List<Product> getProductsByCursor(Cursor cursor)
    {
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

    public static String getProductLastUpdated(SQLiteDatabase db, String id)
    {
        String where = PRODUCT_TABLE_ID + " = ?";
        String[] args = {id};
        String[] columns = {PRODUCT_LAST_UPDATED.toString()};
        Cursor cursor = db.query(PRODUCT_TABLE, columns, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int lastUpdateIndex = cursor.getColumnIndex(PRODUCT_LAST_UPDATED);

            String lastUpdated = cursor.getString(lastUpdateIndex);

            return lastUpdated;
        }
        return null;
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
