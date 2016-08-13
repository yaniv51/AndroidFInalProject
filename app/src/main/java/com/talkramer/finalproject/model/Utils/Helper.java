package com.talkramer.finalproject.model.Utils;

/**
 * Created by Yaniv on 08/06/16.
 */
public class Helper {

    public enum ProductType
    {
        SHIRT, PANTS, DRESS, OTHER
    }

    public enum Customers
    {
        MEN, WOMEN, UNISEX
    }


    public enum ActionResult
    {
        CANCEL, SAVE, DELETE
    }

    public enum GridProductFilter
    {
        ALL_PRODUCTS, ITEMS_FOR_SALE, ALL_SELLER_ITEMS, PURCH_HISTORY, SEARCH
    }

    public static final String ProductId = "PRODUCT_ID";
    public static final String OPERATION = "OPERATION";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String productChildren = "product";
    public static final String counterChildren = "counter";
    public static final String PRICE = "PRICE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String GENDER = "GENDER";
    public static final String TYPE = "TYPE";

    public static final String GRID_VIEW_NO_FILTER_TAG = "GridViewFragment-All";
}
