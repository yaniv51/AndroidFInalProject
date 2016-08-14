package com.talkramer.finalproject.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.talkramer.finalproject.R;
import com.talkramer.finalproject.model.Utils.Helper;

import java.util.HashMap;

/**
 * Created by Yaniv on 13/08/2016.
 */
public class SearchDialog extends DialogFragment {

    private EditText descriptionEditText, startPriceEditText, endPriceEditText;
    private Spinner typeSpinner;
    private RadioButton menRadio, womenRadio, unisexRadio;
    private SearchActionDelegate delegate;
    private CheckBox priceCheckbox, typeCheckbox, genderCheckbox, descriptionCheckbox;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_search, null);

        descriptionEditText = (EditText) view.findViewById(R.id.search_dialog_free_text);
        startPriceEditText = (EditText) view.findViewById(R.id.search_dialog_start_price);
        endPriceEditText = (EditText) view.findViewById(R.id.search_dialog_end_price);

        typeSpinner = (Spinner) view.findViewById(R.id.search_dialog_type);
        menRadio = (RadioButton) view.findViewById(R.id.search_dialog_radio_men);
        womenRadio = (RadioButton) view.findViewById(R.id.search_dialog_radio_women);
        unisexRadio = (RadioButton) view.findViewById(R.id.search_dialog_radio_unisex);

        priceCheckbox = (CheckBox) view.findViewById(R.id.search_dialog_price_checkbox);
        typeCheckbox = (CheckBox) view.findViewById(R.id.search_dialog_type_checkbox);
        genderCheckbox = (CheckBox) view.findViewById(R.id.search_dialog_gender_checkbox);
        descriptionCheckbox = (CheckBox) view.findViewById(R.id.search_dialog_description_checkbox);

        priceCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(priceCheckbox.isChecked())
                    setPriceEnable(true);
                else
                    setPriceEnable(false);
            }
        });
        typeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeCheckbox.isChecked())
                    typeSpinner.setEnabled(true);
                else
                    typeSpinner.setEnabled(false);
            }
        });
        genderCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(genderCheckbox.isChecked())
                    setGenderEnable(true);
                else
                    setGenderEnable(false);
            }
        });

        descriptionCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descriptionCheckbox.isChecked())
                    descriptionEditText.setEnabled(true);
                else
                    descriptionEditText.setEnabled(false);
            }
        });

        setSpinnerAdapter();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        HashMap<String, String> filter;

                        filter = getSearchFilter();
                        hideInput();
                        if(filter == null || filter.size() == 0)
                            delegate.cancle();

                        Log.d("TAG", "search with filters: "+ filter.toString());
                        delegate.ok(filter);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("TAG", "Cancel search operation");
                        hideInput();
                        delegate.cancle();
                    }
                });
        return builder.create();
    }

    private void hideInput()
    {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setPriceEnable(boolean enable)
    {
        startPriceEditText.setEnabled(enable);
        endPriceEditText.setEnabled(enable);
    }

    private void setGenderEnable(boolean enable)
    {
        menRadio.setEnabled(enable);
        womenRadio.setEnabled(enable);
        unisexRadio.setEnabled(enable);
    }

    private HashMap<String, String> getSearchFilter()
    {
        HashMap<String, String> filter;
        int startPrice, endPrice, spinnerSelection;
        String type, gender, description;
        Helper.Customers customer = Helper.Customers.MEN;

        filter = new HashMap<String, String>();
        if(priceCheckbox.isChecked())
        {
            startPrice = Integer.parseInt(startPriceEditText.getText().toString());
            endPrice = Integer.parseInt(endPriceEditText.getText().toString());
            filter.put(Helper.PRICE, startPrice+"-"+endPrice);
        }
        if(typeCheckbox.isChecked())
        {
            spinnerSelection = typeSpinner.getSelectedItemPosition();

            switch (spinnerSelection)
            {
                case 0:
                    type = Helper.ProductType.SHIRT.toString();
                    break;
                case 1:
                    type = Helper.ProductType.PANTS.toString();
                    break;
                case 2:
                    type = Helper.ProductType.DRESS.toString();
                    break;
                default:
                    type = Helper.ProductType.OTHER.toString();
                    break;
            }
            filter.put(Helper.TYPE, type);
        }

        if(genderCheckbox.isChecked())
        {
            customer = menRadio.isChecked()? Helper.Customers.MEN : customer;
            customer = womenRadio.isChecked()? Helper.Customers.WOMEN : customer;
            customer = unisexRadio.isChecked()? Helper.Customers.UNISEX : customer;
            filter.put(Helper.GENDER, customer.toString());
        }

        if(descriptionCheckbox.isChecked())
        {
            description = descriptionEditText.getText().toString();
            filter.put(Helper.DESCRIPTION, description);
        }

        return filter;
    }

    private  void setSpinnerAdapter()
    {
        ArrayAdapter<CharSequence> spinnerAdapter;

        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.product_types_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerAdapter);
    }

    public void setDelegate(SearchActionDelegate delegate) {
        this.delegate = delegate;
    }

    public interface SearchActionDelegate{
        public void ok(HashMap<String, String> filter);
        public void cancle();
    }
}
