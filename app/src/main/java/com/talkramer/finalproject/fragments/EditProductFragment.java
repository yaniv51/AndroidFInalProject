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
import android.widget.ProgressBar;
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

    private EditText description, price;
    private Spinner typeSpinner;
    private RadioButton menRadio, womenRadio, unisexRadio;
    private ImageButton imageButton;
    private Product currentProduct;
    private View view;
    private Bitmap newImage;
    private ProgressBar progressBar;
    Button saveButton, cancelButton, deleteButton;


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

        saveButton = (Button) view.findViewById(R.id.edit_product_save);
        cancelButton = (Button) view.findViewById(R.id.edit_product_cancel);
        deleteButton = (Button) view.findViewById(R.id.edit_product_delete);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updateProduct())
                {
                    showErrorMessage("Values are not valid. Try again");
                    return;
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra(Helper.OPERATION, Helper.ActionResult.CANCEL.ordinal());
                Log.d("TAG", "EditProductFragment - Edit has been canceled");
                getFragmentManager().popBackStack();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getIntent().putExtra(Helper.OPERATION, Helper.ActionResult.DELETE.ordinal());
                Log.d("TAG", "EditProductFragment - Product has been deleted");
                getFragmentManager().popBackStack();
            }
        });

        description = (EditText) view.findViewById(R.id.edit_product_description);
        price = (EditText) view.findViewById(R.id.edit_product_price);
        typeSpinner = (Spinner) view.findViewById(R.id.edit_product_planets_spinner_type);
        menRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_men);
        womenRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_women);
        unisexRadio = (RadioButton) view.findViewById(R.id.edit_product_radio_unisex);
        imageButton = (ImageButton) view.findViewById(R.id.edit_product_details_imageView);

        progressBar = (ProgressBar) view.findViewById(R.id.edit_product_progressbar);

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

    private void showErrorMessage(String message)
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

        customer = currentProduct.getForWhom();
        menRadio.setChecked(customer == Helper.Customers.MEN);
        womenRadio.setChecked(customer == Helper.Customers.WOMEN);
        unisexRadio.setChecked(customer == Helper.Customers.UNISEX);

        productType = currentProduct.getType();
        typeSpinner.setSelection(productType.ordinal());
        if(currentProduct.getImageProduct() == null)
        {
            Model.getInstance().loadImage(currentProduct, new Model.LoadImageListener() {
                @Override
                public void onResult(String id, Bitmap imageBmp) {
                    currentProduct.setImageProduct(imageBmp);
                    imageButton.setImageBitmap(imageBmp);
                }
            });
        }
        else
        {
            imageButton.setImageBitmap(currentProduct.getImageProduct());
        }
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
        String description, stringPrice, sellerId, sellerEmail, buyerEmail;
        Helper.Customers customer = Helper.Customers.MEN;
        Helper.ProductType type;
        Product newProduct;
        boolean deleted;
        int spinnerSelection, price;

        description = this.description.getText().toString();
        stringPrice = this.price.getText().toString();
        sellerId = currentProduct.getSellerId();
        sellerEmail = currentProduct.getSellerEmail();
        buyerEmail = currentProduct.getBuyerEmail();
        deleted = currentProduct.getDeleted();

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

        newProduct = new Product(currentProduct.getId(), type, description, price , customer,sellerId, sellerEmail, localImage);
        newProduct.setBuyerEmail(buyerEmail);
        newProduct.setDeleted(deleted);

        setEnable(false);
        Model.getInstance().updateProductInformation(currentProduct.getId(), newProduct, new Model.OperationListener() {
            @Override
            public void success() {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        setEnable(true);
                        getActivity().getIntent().putExtra(Helper.OPERATION, Helper.ActionResult.SAVE.ordinal());
                        Log.d("TAG", "EditProductFragment - Product has been edited");
                        ShowSaveDialog();
                    }
                };
                getActivity().runOnUiThread(run);
            }

            @Override
            public void fail(final String msg) {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        setEnable(true);
                        showErrorMessage(msg);
                    }
                };
                getActivity().runOnUiThread(run);
            }
        });
        return  true;
    }

    private void setEnable(boolean enable)
    {
        if(enable)
            progressBar.setVisibility(view.GONE);
        else
            progressBar.setVisibility(view.VISIBLE);

        description.setEnabled(enable);
        price.setEnabled(enable);
        typeSpinner.setEnabled(enable);
        saveButton.setEnabled(enable);
        cancelButton.setEnabled(enable);
        imageButton.setEnabled(enable);
        deleteButton.setEnabled(enable);
    }
}
