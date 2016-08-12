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

    public static final String ProductId = "PRODUCT_ID";
    public static final String OPERATION = "OPERATION";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String RememberMe = "REMEMBER_ME";
    public static final String Email = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
}
