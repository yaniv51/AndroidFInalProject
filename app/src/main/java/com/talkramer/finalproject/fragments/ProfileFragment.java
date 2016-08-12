package com.talkramer.finalproject.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.talkramer.finalproject.R;

public class ProfileFragment extends Fragment {

    private View view;
    private Button addProductButton, salesHistoryButton, myProductdButton, purchaseHistoryButton;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        addProductButton = (Button) view.findViewById(R.id.addProductButton_profile_fragment);
        salesHistoryButton = (Button) view.findViewById(R.id.salesHistoryButton_profile_fragment);
        myProductdButton = (Button) view.findViewById(R.id.myProductButton_profile_fragment);
        purchaseHistoryButton = (Button) view.findViewById(R.id.purchaseHistoryButton_profile_fragment);


        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        salesHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        myProductdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        purchaseHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}