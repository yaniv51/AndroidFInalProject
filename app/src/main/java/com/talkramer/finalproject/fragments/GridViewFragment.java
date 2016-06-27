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
                String productId = data.get(position).getId();

                Log.d("TAG", "GridViewFragment - clicked on product: ProductId = " + productId);
                getActivity().getIntent().putExtra(Helper.ProductId, productId);
                getActivity().getIntent().putExtra(Helper.OPERATION, Helper.ActionResult.CANCEL.ordinal());

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
        int result;
        super.onResume();

        result = getActivity().getIntent().getIntExtra(Helper.OPERATION, 0);

        //update UI only in case of return from edit product with successfully edit
        if(result == Helper.ActionResult.CANCEL.ordinal()) {
            Log.d("TAG", "ProductDetailsFragment - resume with Cancel operation");
        }
        else if(result == Helper.ActionResult.SAVE.ordinal() || result == Helper.ActionResult.DELETE.ordinal())
        {
            //notify adapter list while fragment is resume - check if possible to do only when item removed/edited
            String action = result==Helper.ActionResult.SAVE.ordinal()? "Save":"Delete";
            Log.d("TAG", "GridViewFragment - resume with "+ action +" operation");
            if(imageAadapter!=null)
                imageAadapter.notifyDataSetChanged();
        }
        else
            Log.d("TAG", "GridViewFragment - resume with unknown operation: "+result);
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
                //Log.d("TAG", "create view:" + position);
                image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            } else {
                //Log.d("TAG", "use convert view:" + position);
            }

            //set params of each item
            image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            int imagerResource = R.drawable.image;
            Drawable res = getResources().getDrawable(imagerResource);
            image.setImageDrawable(res);

            Product localProduct = data.get(position);
            if(localProduct == null)
                return  convertView;

            text = (TextView) convertView.findViewById(R.id.grid_item_text);
            text.setText(""+localProduct.getId());
            return convertView;
        }
    }
}
