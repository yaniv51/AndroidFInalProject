package com.talkramer.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import model.Person;
import model.Product;

public class ProductDetailsActivity extends Activity {

    Product currentProduct;
    private boolean edited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        final String prId = (String) getIntent().getExtras().get("PRODUCT_ID");
        Log.d("TAG", "product id = " + prId);
        updateProductById(prId);

        Button editButton = (Button) findViewById(R.id.product_details_editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        EditProductActivity.class);
                intent.putExtra("PRODUCT_ID", currentProduct.getId());
                startActivity(intent);
            }
        });
    }

    private void updateProductById(String productId)
    {
        currentProduct = Person.instance().getProduct(productId);
        updateProductDetails();
    }

    private String readPreference(String tag)
    {
        String action;

        //use shared preferences for transfer flags from activities
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        action = sharedPref.getString(tag, null);

        return action;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String action;

        action = readPreference("ACTION");
        if(action==null) {
            Log.d("TAG", "resume details - no action");
            return;
        }
        if(action.compareTo("EDIT") == 0) {
            Log.d("TAG", "resume details - edit");
            final String prId = readPreference("PRODUCT_ID");
            updateProductById(prId);
            edited = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.d("TAG", "onBackPressed");
        Intent returnIntent = new Intent();
        if(edited)
            setResult(Activity.RESULT_OK, returnIntent);
        else
            setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    //update UI text views
    private void updateProductDetails()
    {
        if(currentProduct == null)
            return;
        TextView typeTextView = (TextView) findViewById(R.id.product_details_type);
        typeTextView.setText(currentProduct.getType());

        TextView descriptionTextView = (TextView) findViewById(R.id.product_details_description);
        descriptionTextView.setText(currentProduct.getDescription());

        TextView priceTextView = (TextView) findViewById(R.id.product_details_price);
        priceTextView.setText(currentProduct.getPrice());

        TextView forWhomTextView = (TextView) findViewById(R.id.product_details_forWhom);
        forWhomTextView.setText(currentProduct.getForWhom());

        TextView imageProduct = (TextView) findViewById(R.id.product_details_image);
        imageProduct.setText(currentProduct.getImageProduct());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_details, menu);
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
