package com.talkramer.finalproject.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.talkramer.finalproject.R;
import com.talkramer.finalproject.model.Domain.Product;
import com.talkramer.finalproject.model.Model;
import com.talkramer.finalproject.model.Utils.Helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridViewFragment extends Fragment {
    private View view;
    private GridView grid;
    private List<Product> data;
    private ImageAdapter imageAadapter;
    private Model.UpdateProductsListener updateProductsListener;
    ProgressBar progressBar, smallProgressbar;

    private boolean firstExec;
    private Helper.GridProductFilter filter;
    private HashMap<String, String> searchFilter;

    public void setFilter(Helper.GridProductFilter newFilter)
    {
        filter = newFilter;
    }

    public Helper.GridProductFilter getFilter() {return filter;}

    public void setSearchFilter(HashMap<String, String> newFilter) { searchFilter = newFilter; }

    public GridViewFragment() {
        updateProductsListener = new Model.UpdateProductsListener() {
            @Override
            public void notify(List<Product> products) {
                if(products != null)
                    loadProductData(products);
            }
        };
        Model.getInstance().setUpdateUIListener(updateProductsListener);
        firstExec = true;
        searchFilter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "Create grid view");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        grid = (GridView) view.findViewById(R.id.yani_gridview_gridview);

        if(firstExec)
            data = new LinkedList<Product>();

        imageAadapter = new ImageAdapter();

        progressBar = (ProgressBar)view.findViewById(R.id.mainProgressBar);
        smallProgressbar = (ProgressBar) view.findViewById(R.id.small_progress_bar);

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
                transaction.add(R.id.main_frag_container, newFragment,"ProductDetailsFragment");
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });

        //createView call on resume, need to load all products just in first start of current fragment
        if(firstExec)
        {
            firstExec = false;
            loadProductsData();
        }

        return view;
    }

    private void loadProductsData()
    {
        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getAllProductsAsync(new Model.GetProductsListenerInterface() {
            @Override
            public void done(List<Product> products) {
                data = Model.getInstance().getFilterProducts(filter, searchFilter);
                progressBar.setVisibility(View.GONE);
                imageAadapter.notifyDataSetChanged();
            }
        });
    }


    private void loadProductData(List<Product> products)
    {
        smallProgressbar.setVisibility(View.VISIBLE);
        data = Model.getInstance().getFilterProducts(filter, searchFilter);
        imageAadapter.notifyDataSetChanged();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        smallProgressbar.setVisibility(View.GONE);
                    }
                });
            }
        };
        thread.start();
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
            loadProductsData();
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

            final ImageView image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            final TextView text = (TextView) convertView.findViewById(R.id.grid_item_text);
            text.setText("Price: "+localProduct.getPrice());
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
