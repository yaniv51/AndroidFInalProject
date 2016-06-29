package com.talkramer.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import model.Person;
import model.Product;


public class NewProductActivity extends ActionBarActivity {

    private EditText type, description, price, forWhom;
    private TextView imageProduct;
    private String id; /////ADD RANDOM ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_product);

        Button add = (Button) findViewById(R.id.main_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProduct();
            }
        });

        type = (EditText) findViewById(R.id.new_product_type);
        description = (EditText) findViewById(R.id.new_product_description);
        price = (EditText) findViewById(R.id.new_product_price);
        forWhom = (EditText) findViewById(R.id.new_product_forWhom);
        //imageProduct = (TextView) findViewById(R.id.new_p);
        setFocusing();
    }

    public void createProduct()
    {
        String prId, prType, prDescription, prPrice, prForWhom , prImageProduct;
        //Person prSeller;

        prType = type.getText().toString();
        prDescription = description.getText().toString();
        prPrice = price.getText().toString();
        prForWhom = forWhom.getText().toString();
        prImageProduct = imageProduct.getText().toString();
        //prSeller = new


        /*if(Person.instance().isIdValid(prId))
        {
            Product newProduct = new Product(prId, prType, prDescription, prPrice, prForWhom, prImageProduct, prSeller);
            Person.instance().add(newProduct);
            finish();
        }
        else
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Person Id is not valid. Try again");

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    return;
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }*/
    }

    private void setFocusing()
    {

        type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(type, InputMethodManager.SHOW_IMPLICIT);
                    Log.d("Tag", "Show type");
                }
                else
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(type.getWindowToken(),0 );
                    Log.d("Tag", "hide type");
                }
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(description, InputMethodManager.SHOW_IMPLICIT);
                }
                else
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(description.getWindowToken(), 0);
                }
            }
        });
        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(price, InputMethodManager.SHOW_IMPLICIT);
                }
                else
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(price.getWindowToken(),0 );
                }
            }
        });
        forWhom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(forWhom, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(forWhom.getWindowToken(), 0);
                }
            }
        });
        imageProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(imageProduct, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(imageProduct.getWindowToken(), 0);
                }
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search_button, menu);
//        getMenuInflater().inflate(R.menu.menu_home_button, menu);
//        getMenuInflater().inflate(R.menu.menu_profile_button, menu);
//        super.onCreateOptionsMenu(menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: {
//                NewStudentFragment newStudentFragment = new NewStudentFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_frag_container, newStudentFragment, "newStudentFragment");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                break;
            }

            case R.id.home: {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                Fragment page = getSupportFragmentManager().findFragmentByTag("studentDetailsFragment");
//                page.onOptionsItemSelected(item);
                break;
            }

            case R.id.profile: {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                Fragment page = getSupportFragmentManager().findFragmentByTag("studentDetailsFragment");
//                page.onOptionsItemSelected(item);
                break;
            }

            default:
                return false;
        }
        return  false;
    }
}