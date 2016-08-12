package com.talkramer.finalproject.fragments;


import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.talkramer.finalproject.R;

import java.util.List;

import com.talkramer.finalproject.model.Utils.Helper;
import com.talkramer.finalproject.model.Model;
import com.talkramer.finalproject.model.Domain.Product;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridViewFragment extends Fragment {
    private View view;
    GridView grid;
    List<Product> data;
    ImageAdapter imageAadapter;
    private static boolean initialize = true;

    ProgressBar progressBar;

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

        //TODO: add progressbar
        progressBar = (ProgressBar)view.findViewById(R.id.mainProgressBar);

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

        Button addProductButton = (Button) view.findViewById(R.id.grid_view_add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "GridViewFragment - clicked on add product");

                Fragment newFragment = new NewProductFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.main_frag_container, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });

        loadProductsData();

        return view;
    }

    private void loadProductsData()
    {
        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getAllProductsAsync(new Model.GetProductsListenerInterface() {
            @Override
            public void done(List<Product> products) {
                progressBar.setVisibility(View.GONE);
                data = products;
                imageAadapter.notifyDataSetChanged();
            }
        });
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
            TextView text;

            if (convertView == null) {
                //if it's not recycled, initialize
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.grid_item_layout, null);
            } else {
            }

            //set params of each item
            final Product localProduct = data.get(position);
            if(localProduct == null)
                return  convertView;

            text = (TextView) convertView.findViewById(R.id.grid_item_text);
            final ImageView image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            text.setText(""+localProduct.getId());

            //if image is null - load online image
            if(localProduct.getImageProduct() == null)
            {
                //Log.d("TAG","list gets image " + localProduct.getId());
                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.grid_item_progressbar);
                progress.setVisibility(View.VISIBLE);
                Model.getInstance().loadImage(localProduct,new Model.LoadImageListener() {
                    @Override
                    public void onResult(String id, Bitmap imageBmp) {
                        localProduct.setImageProduct(imageBmp);
                        setImage(image, progress, imageBmp);
                    }
                });
            }
            else
            {
                image.setImageBitmap(localProduct.getImageProduct());
            }
            return convertView;
        }

        private void setImage(final ImageView imageView, final ProgressBar imageProgress, final Bitmap image)
        {
            imageProgress.setVisibility(View.GONE);
            imageView.setImageBitmap(image);
        }
    }
}
