package com.talkramer.finalproject.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.talkramer.finalproject.R;
import com.talkramer.finalproject.dialogs.SaveOperationDialog;

import com.talkramer.finalproject.model.Utils.Helper;
import com.talkramer.finalproject.model.Model;
import com.talkramer.finalproject.model.Domain.Product;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProductFragment extends Fragment {

    private EditText description, price, seller;
    private Spinner typeSpinner;
    private RadioButton menRadio, womenRadio, unisexRadio;
    private ImageButton imageButton;
    private Product currentProduct;
    private View view;
    private Bitmap newImage;


    public EditProductFragment() {
        // Required empty public constructor
        newImage = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String productId = (String) getActivity().getIntent().getExtras().get(Helper.ProductId);
        Log.d("TAG", "EditProductFragment - product Id = " + productId);

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
                getActivity().getIntent().putExtra(Helper.OPERATION, Helper.ActionResult.SAVE.ordinal());
                Log.d("TAG", "EditProductFragment - Product has been edited");
                ShowSaveDialog();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra(Helper.OPERATION, Helper.ActionResult.CANCEL.ordinal());
                Log.d("TAG", "EditProductFragment - Edit has been canceled");
                getFragmentManager().popBackStack();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra(Helper.OPERATION, Helper.ActionResult.DELETE.ordinal());
                Log.d("TAG", "EditProductFragment - Product has been deleted");
                getFragmentManager().popBackStack();
            }
        });

        description = (EditText) view.findViewById(R.id.edit_product_description);
        price = (EditText) view.findViewById(R.id.edit_product_price);
        seller = (EditText) view.findViewById(R.id.edit_product_seller);
        typeSpinner = (Spinner) view.findViewById(R.id.edit_product_planets_spinner_type);
        menRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_men);
        womenRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_women);
        unisexRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_unisex);
        imageButton = (ImageButton) view.findViewById(R.id.edit_product_details_imageView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
                {
                    startActivityForResult(takePictureIntent, Helper.REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        setSpinnerAdapter();
        UpdateProductOnUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Helper.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            Bundle extras = data.getExtras();
            newImage = (Bitmap)extras.get("data");
            imageButton.setImageBitmap(newImage);
        }
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
        typeSpinner.setSelection(productType.ordinal());
        imageButton.setImageBitmap(currentProduct.getImageProduct());
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
                getFragmentManager().popBackStack();
            }
        });
        dialog.show(getFragmentManager(), "GGG");
    }

    public boolean updateProduct()
    {
        Bitmap localImage;
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

        if(newImage == null)
            localImage = currentProduct.getImageProduct();
        else
            localImage = newImage;

        newProduct = new Product(currentProduct.getId(), type, description, price , customer,sellerId, localImage);

        Model.getInstance().updateProductInformation(currentProduct.getId(), newProduct);
        return  true;
    }
}
