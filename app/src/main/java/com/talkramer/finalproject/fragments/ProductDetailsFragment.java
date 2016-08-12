package com.talkramer.finalproject.fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.talkramer.finalproject.R;

import com.talkramer.finalproject.model.Utils.Helper;
import com.talkramer.finalproject.model.Model;
import com.talkramer.finalproject.model.Domain.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsFragment extends Fragment {
    private TextView description, price, seller, type;
    private RadioButton menRadio, womenRadio, unisexRadio;
    private Product currentProduct;
    private ImageView imageView;
    private View view;
    private Button buyButton, editButton;
    private ProgressBar progressBar;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String productId = (String) getActivity().getIntent().getExtras().get(Helper.ProductId);
        Log.d("TAG", "ProductDetailsFragment - product id = " + productId);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_details, container, false);

        currentProduct = Model.getInstance().getProduct(productId);

        buyButton = (Button) view.findViewById(R.id.product_details_buy);
        editButton = (Button) view.findViewById(R.id.product_details_edit);

        progressBar = (ProgressBar) view.findViewById(R.id.product_details_progressbar);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnable(false);
                Model.getInstance().buyProduct(currentProduct, new Model.OperationListener() {
                    @Override
                    public void success() {
                        setEnable(true);
                        buyButton.setEnabled(false);
                        showMessage("Product marked as bought. Please contact seller on: \n" + currentProduct.getSellerEmail());
                    }

                    @Override
                    public void fail(String msg) {
                        setEnable(true);
                        showMessage("Could not buy product. Try again later. " + msg);
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "ProductDetailsFragment - click to edit product: " + currentProduct.getId());
                getActivity().getIntent().putExtra(Helper.ProductId, productId);
                getActivity().getIntent().putExtra(Helper.OPERATION, Helper.ActionResult.CANCEL.ordinal());

                Fragment newFragment = new EditProductFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.main_frag_container, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });

        //get all UI components
        description = (TextView) view.findViewById(R.id.product_details_description);
        price = (TextView) view.findViewById(R.id.product_details_price);
        seller = (TextView) view.findViewById(R.id.product_details_seller);
        type = (TextView) view.findViewById(R.id.product_details_type);
        menRadio = (RadioButton) view.findViewById(R.id.product_details_radio_men);
        womenRadio = (RadioButton) view.findViewById(R.id.product_details_radio_women);
        unisexRadio = (RadioButton) view.findViewById(R.id.product_details_radio_unisex);
        imageView = (ImageView) view.findViewById(R.id.product_details_imageView);

        UpdateProductOnUI();
        return  view;
    }

    @Override
    public void onResume() {
        int result;
        super.onResume();

        result = getActivity().getIntent().getIntExtra(Helper.OPERATION, 0);

        //update UI only in case of return from edit product with successfully edit
        if(result == Helper.ActionResult.CANCEL.ordinal()) {
            Log.d("TAG", "ProductDetailsFragment - resume with Cancel operation");
        }
        else if(result == Helper.ActionResult.SAVE.ordinal())
        {
            Log.d("TAG", "ProductDetailsFragment - resume with Save operation");
            UpdateProductOnUI();
        }
        else if(result == Helper.ActionResult.DELETE.ordinal())
        {
            Log.d("TAG", "ProductDetailsFragment - resume with Delete operation");
            Model.getInstance().delete(currentProduct, new Model.OperationListener() {
                @Override
                public void success() { }

                @Override
                public void fail(String msg) { } });
            //close current fragment and open the last one
            getFragmentManager().popBackStack();
        }
        else
            Log.d("TAG", "ProductDetailsFragment - resume with unknown operation: "+result);


        //https://developer.android.com/training/basics/fragments/communicating.html
    }

    //Update UI components by current product information
    private void UpdateProductOnUI()
    {
        String[] typeArray;

        Helper.ProductType productType;
        Helper.Customers customer;
        description.setText(currentProduct.getDescription());
        price.setText("" + currentProduct.getPrice());
        seller.setText(currentProduct.getSellerEmail());

        customer = currentProduct.getForWhom();
        menRadio.setChecked(customer == Helper.Customers.MEN);
        womenRadio.setChecked(customer == Helper.Customers.WOMEN);
        unisexRadio.setChecked(customer == Helper.Customers.UNISEX);

        productType = currentProduct.getType();
        //Log.d("TAG", "ProductDetailsFragment -  item use" + productType);
        typeArray = getResources().getStringArray(R.array.product_types_array);
        type.setText(typeArray[productType.ordinal()]);
        imageView.setImageBitmap(currentProduct.getImageProduct());

        if(currentProduct.getDeleted())
        {
            buyButton.setEnabled(false);
            editButton.setEnabled(false);
        }

        if(currentProduct.getBuyerEmail() != null && currentProduct.getBuyerEmail().compareTo("") !=0)
        {
            buyButton.setEnabled(false);
        }
    }

    private void setEnable(boolean enable)
    {
        if(enable)
            progressBar.setVisibility(view.GONE);
        else
            progressBar.setVisibility(view.VISIBLE);

        description.setEnabled(enable);
        price.setEnabled(enable);
        buyButton.setEnabled(enable);
        editButton.setEnabled(enable);
        seller.setEnabled(enable);
        type.setEnabled(enable);
    }

    private void showMessage(String message)
    {
        //if already in use and not by current student - show error message
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                return;
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
