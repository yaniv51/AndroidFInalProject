package com.talkramer.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import model.Model;
import model.Person;
import model.Product;

public class ProductListActivity extends Activity {
    ListView list;
    List<Product> data;
    MyAddapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        list = (ListView) findViewById(R.id.product_listView);
        data = Model.getInstance().getProducts();

        adapter = new MyAddapter();
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(),
                        ProductDetailsActivity.class);
                // Create object of SharedPreferences.
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                //now get Editor
                SharedPreferences.Editor editor = sharedPref.edit();
                //put your value
                editor.putString("ACTION", "DEFAULT");
                //commits your edits
                editor.commit();

                intent.putExtra("PRODUCT_ID", data.get(position).getId());

                startActivityForResult(intent, 1);
            }
        });

        ImageButton addBtn = (ImageButton) findViewById(R.id.product_list_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        NewProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                if(adapter!=null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    class MyAddapter extends BaseAdapter {
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
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.activity_product_list_row, null);
                Log.d("TAG", "create view:" + position);
            }else{
                Log.d("TAG", "use convert view:" + position);
            }

            TextView type = (TextView) convertView.findViewById(R.id.product_list_row_type);
            TextView price = (TextView) convertView.findViewById(R.id.product_list_row_price);
            TextView forWhom = (TextView) convertView.findViewById(R.id.product_list_row_forWhom);
            TextView imageProduct = (TextView) convertView.findViewById(R.id.product_list_row_image);

            Product pr = data.get(position);
            if(pr==null)
                return convertView;
            type.setText(pr.getType().toString());
            price.setText(pr.getPrice());
            forWhom.setText(pr.getForWhom().toString());
            imageProduct.setText(pr.getImageProduct());

            return convertView;
        }
    }
}
