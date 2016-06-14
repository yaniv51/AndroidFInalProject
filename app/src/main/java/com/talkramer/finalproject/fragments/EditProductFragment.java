package com.talkramer.finalproject.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        final String productId = (String) getActivity().getIntent().getExtras().get(Helper.ProductId);
        Log.d("TAG", "student id = " + productId);

        view = inflater.inflate(R.layout.fragment_edit_product, container, false);
        currentProduct = Model.getInstance().getProduct(productId);

        Button save = (Button) view.findViewById(R.id.edit_product_save);
        Button cancel = (Button) view.findViewById(R.id.edit_product_cancel);
        Button delete = (Button) view.findViewById(R.id.edit_product_delete);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updateProduct())
                {
                    //if already in use and not by current student - show error message
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("Values are not valid. Try again");

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            return;
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
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

        setSpinnerAdapter();
        UpdateProductOnUI();

        return view;
    }

    private void UpdateProductOnUI()
    {
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
        Log.d("TAG", "item is"+productType);
        typeSpinner.setSelection(productType.ordinal());
    }

    private  void setSpinnerAdapter()
    {
        ArrayAdapter<CharSequence> spinnerAdapter;

        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.product_types_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerAdapter);
    }


    private void ShowSaveDialog()
    {
        SaveOperationDialog dialog = new SaveOperationDialog();
        dialog.setDelegate(new SaveOperationDialog.Delegate() {
            @Override
            public void ok() {
                Log.d("TAG", "activity receive OK");
                //TODO: set save operation for update
                getFragmentManager().popBackStack();
            }
        });
        dialog.show(getFragmentManager(), "GGG");
    }


    public boolean updateProduct()
    {
        String description, stringPrice, sellerId;
        Helper.Customers customer = Helper.Customers.MEN;
        Helper.ProductType type;
        Product newProduct;
        int spinnerSelection, price;

        description = this.description.getText().toString();
        stringPrice = this.price.getText().toString();
        sellerId = this.seller.getText().toString();

        customer = menRadio.isChecked()? Helper.Customers.MEN : customer;
        customer = womenRadio.isChecked()? Helper.Customers.WOMEN : customer;
        customer = unisexRadio.isChecked()? Helper.Customers.UNISEX : customer;

        spinnerSelection = typeSpinner.getSelectedItemPosition();

        switch (spinnerSelection)
        {
            case 0:
                type = Helper.ProductType.SHIRT;
                break;
            case 1:
                type = Helper.ProductType.PANTS;
                break;
            case 2:
                type = Helper.ProductType.DRESS;
                break;
            default:
                type = Helper.ProductType.OTHER;
                break;
        }

        try {
            price = Integer.parseInt(stringPrice);
        }
        catch (Exception ex)
        {
            return false;
        }

        newProduct = new Product(currentProduct.getId(), type, description, price , customer, currentProduct.getImageProduct(),sellerId);

        Model.getInstance().updateProductInformation(currentProduct.getId(), newProduct);

        //TODO: return false and show error in case of fail to convert price to int
        return  true;
    }
}
