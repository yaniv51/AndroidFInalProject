package com.talkramer.finalproject.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.talkramer.finalproject.R;
import com.talkramer.finalproject.dialogs.SaveOperationDialog;

import model.Helper;
import model.Model;
import model.Product;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProductFragment extends Fragment {

    private EditText description, price, seller;
    private Spinner typeSpinner;
    private RadioButton menRadio, womenRadio, unisexRadio;
    private Product currentProduct;
    private Helper.ActionResult result;

    private View view;


    public EditProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_product, container, false);

        final String productId = (String) getActivity().getIntent().getExtras().get(Helper.ProductId);
        Log.d("TAG", "student id = " + productId);
        currentProduct = Model.getInstance().getProduct(productId);

        Button save = (Button) view.findViewById(R.id.edit_product_save);
        Button cancel = (Button) view.findViewById(R.id.edit_product_cancel);
        Button delete = (Button) view.findViewById(R.id.edit_product_delete);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updateProduct())
                {
                    return;
                }
                result = Helper.ActionResult.SAVE;
                ShowSaveDialog();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = Helper.ActionResult.CANCEL;
                getActivity().finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().delete(currentProduct);
                result = Helper.ActionResult.DELETE;
                getActivity().finish();
            }
        });

        description = (EditText) view.findViewById(R.id.edit_product_description);
        price = (EditText) view.findViewById(R.id.edit_product_price);
        seller = (EditText) view.findViewById(R.id.edit_product_seller);
        typeSpinner = (Spinner) view.findViewById(R.id.edit_product_planets_spinner_type);
        menRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_men);
        womenRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_women);
        unisexRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_unisex);

        return view;
    }

    private void UpdateProductOnUI()
    {
        Helper.ProductType productType;
        Helper.Customers customer;
        description.setText(currentProduct.getDescription());
        price.setText(currentProduct.getPrice());
        seller.setText(currentProduct.getSellerId());

        customer = currentProduct.getForWhom();
        menRadio.setSelected(customer == Helper.Customers.MEN);
        womenRadio.setSelected(customer == Helper.Customers.WOMEN);
        unisexRadio.setSelected(customer == Helper.Customers.UNISEX);
    }


    private void ShowSaveDialog()
    {
        SaveOperationDialog dialog = new SaveOperationDialog();
        dialog.setDelegate(new SaveOperationDialog.Delegate() {
            @Override
            public void ok() {
                Log.d("TAG", "activity receive OK");
                getActivity().finish();
            }
        });
        dialog.show(getFragmentManager(), "GGG");
    }


    public boolean updateProduct()
    {
        String description, price, sellerId;
        Helper.Customers customer = Helper.Customers.MEN;
        Product newProduct;

        description = this.description.getText().toString();
        price = this.description.getText().toString();
        sellerId = this.seller.getText().toString();

        customer = menRadio.isSelected()? Helper.Customers.MEN : customer;
        customer = womenRadio.isSelected()? Helper.Customers.WOMEN : customer;
        customer = unisexRadio.isSelected()? Helper.Customers.UNISEX : customer;

        newProduct = new Product(currentProduct.getId(), Helper.ProductType.DRESS, description, Integer.parseInt(price), customer, currentProduct.getImageProduct(),sellerId);

        Model.getInstance().updateProductInformation(currentProduct.getId(), newProduct);

        //TODO: return false and show error in case of fail to convert price to int
        return  true;
    }
}
