package com.talkramer.finalproject.fragments;


import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.talkramer.finalproject.R;

import java.util.List;

import model.Helper;
import model.Model;
import model.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridViewFragment extends Fragment {
    private View view;
    GridView grid;
    List<Product> data;
    ImageAdapter imageAadapter;

    public GridViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        grid = (GridView) view.findViewById(R.id.yani_gridview_gridview);

        data = Model.getInstance().getProducts();
        imageAadapter = new ImageAdapter();

        grid.setAdapter(imageAadapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.d("TAG", "clicked on " + +position);

                getActivity().getIntent().putExtra(Helper.ProductId, data.get(position).getId());

                Fragment newFragment = new ProductDetailsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.main_frag_container, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //notify adapter list while fragment is resume - check if possible to do only when item removed/edited
        if(imageAadapter!=null)
            imageAadapter.notifyDataSetChanged();
    }

    public class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image;
            TextView text;


            if (convertView == null) {
                //if it's not recycled, initialize
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.grid_item_layout, null);
                Log.d("TAG", "create view:" + position);
                image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            } else {
                Log.d("TAG", "use convert view:" + position);
            }

            //set params of each item
            image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            int imagerResource = R.drawable.image;
            Drawable res = getResources().getDrawable(imagerResource);
            image.setImageDrawable(res);

            text = (TextView) convertView.findViewById(R.id.grid_item_text);
            text.setText(""+position);
            return convertView;
        }
    }
}
