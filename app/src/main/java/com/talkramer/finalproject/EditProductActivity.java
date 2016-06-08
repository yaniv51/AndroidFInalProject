package com.talkramer.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import model.Person;
import model.Product;

public class EditProductActivity extends ActionBarActivity {

    private EditText type, description, price, forWhom;
    private TextView imageProduct;
    private Product currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        final String prId = (String) getIntent().getExtras().get("PRODUCT_ID");
        Log.d("TAG", "product id = " + prId);
        currentProduct = Person.instance().getProduct(prId);

        Button save = (Button) findViewById(R.id.edit_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updateProduct())
                {
                    return;
                }
                updateSharedPref("ACTION", "EDIT");
                updateSharedPref("PRODUCT_ID",id.getText().toString());
                finish();
            }
        });

        type = (EditText) findViewById(R.id.edit_product_type);
        description = (EditText) findViewById(R.id.edit_product_description);
        price = (EditText) findViewById(R.id.edit_product_price);
        forWhom = (EditText) findViewById(R.id.edit_product_forWhom);
        imageProduct = (TextView) findViewById(R.id.edit_product_image);
        updateProductDetailsOnUi();
        setFocusing();
    }

    private void updateSharedPref(String tag, String action)
    {
        // Create object of SharedPreferences.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();
        //put your value
        editor.putString(tag, action);

        //commits your edits
        editor.commit();
    }

    //update UI text views
    private void updateProductDetailsOnUi()
    {
        type.setText(currentProduct.getType());
        description.setText(currentProduct.getDescription());
        price.setText(currentProduct.getPrice());
        forWhom.setText(currentProduct.getForWhom());
        imageProduct.setText(currentProduct.getImageProduct());
    }

    public boolean updateProduct()
    {
        String prType, prDescription, prPrice, prForWhom, prImageProduct;
        boolean isLegal;

        prType = type.getText().toString();
        prDescription = description.getText().toString();
        prPrice = price.getText().toString();
        prForWhom = forWhom.getText().toString();
        prImageProduct = imageProduct.getText().toString();

        isLegal = Person.instance().isIdValid(currentProduct.getId());
        //check if new ID is not in used
        if(!isLegal && (currentProduct.getId().compareTo(currentProduct.getId())!=0) || currentProduct.getId().isEmpty()) {
            //if already in use and not by current product - show error message
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Product Id is not valid. Try again");

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    return;
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return false;
        }

        Product newProduct = new Product(currentProduct.getId(), prType, prDescription, prPrice, prForWhom ,prImageProduct,Person.instance());
        Person.instance().updateProductInformation(currentProduct.getId(), newProduct);
        return true;
    }

    private void setFocusing()
    {
        type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(type, InputMethodManager.SHOW_IMPLICIT);
                    Log.d("Tag", "Show type");
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(type.getWindowToken(), 0);
                    Log.d("Tag", "hide type");
                }
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(description, InputMethodManager.SHOW_IMPLICIT);
                } else {
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
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(price.getWindowToken(), 0);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
