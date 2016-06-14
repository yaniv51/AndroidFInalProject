package com.talkramer.finalproject.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.talkramer.finalproject.R;

import model.Helper;
import model.Model;
import model.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsFragment extends Fragment {
    private TextView description, price, seller, type;
    private RadioButton menRadio, womenRadio, unisexRadio;
    private Product currentProduct;
    private Helper.ActionResult result;
    private View view;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String productId = (String) getActivity().getIntent().getExtras().get(Helper.ProductId);
        Log.d("TAG", "product id = " + productId);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_details, container, false);

        currentProduct = Model.getInstance().getProduct(productId);

        Button buyButton = (Button) view.findViewById(R.id.product_details_buy);
        Button editButton = (Button) view.findViewById(R.id.product_details_edit);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "click to buy product: "+currentProduct.getId());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "click to edit product: " + currentProduct.getId());
                getActivity().getIntent().putExtra(Helper.ProductId, productId);

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

        UpdateProductOnUI();

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: update UI only in case of return from edit product with successfully edit
        //https://developer.android.com/training/basics/fragments/communicating.html
        Log.d("TAG", "PRODUCT DETAILS - RESUME");
        UpdateProductOnUI();
    }

    //Update UI components by current product information
    private void UpdateProductOnUI()
    {
        String[] typeArray;

        Helper.ProductType productType;
        Helper.Customers customer;
        description.setText(currentProduct.getDescription());
        price.setText("" + currentProduct.getPrice());
        seller.setText(currentProduct.getSellerId());

        customer = currentProduct.getForWhom();
        menRadio.setChecked(customer == Helper.Customers.MEN);
        womenRadio.setChecked(customer == Helper.Customers.WOMEN);
        unisexRadio.setChecked(customer == Helper.Customers.UNISEX);

        productType = currentProduct.getType();
        Log.d("TAG", "item use" + productType);
        typeArray = getResources().getStringArray(R.array.product_types_array);
        type.setText(typeArray[productType.ordinal()]);
    }
}
