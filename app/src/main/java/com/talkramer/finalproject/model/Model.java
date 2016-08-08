package com.talkramer.finalproject.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.talkramer.finalproject.ApplicationStartup;
import com.talkramer.finalproject.model.Domain.Product;
import com.talkramer.finalproject.model.Utils.Helper;

import java.util.LinkedList;
import java.util.List;

import com.talkramer.finalproject.model.Utils.FileManagerHelper;
public class Model {

    private static Model instance;
    private ModelFirebase firebaseModel;
    private ModelCloudinary cloudinary;
    private FileManagerHelper fileManager;

    private boolean firstInit = false;

    List<Product> data;

    private Model()
    {
        data = new LinkedList<Product>();
        firebaseModel = new ModelFirebase(ApplicationStartup.getAppContext());
        cloudinary = new ModelCloudinary(ApplicationStartup.getAppContext());
        fileManager = new FileManagerHelper(ApplicationStartup.getAppContext());

        //init();
    }

    public static Model getInstance()
    {
        if(instance == null)
            instance = new Model();

        return instance;
    }

    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                //first try to fin the image on the device
                Bitmap bmp = fileManager.loadImageFromFile(imageName);

                if (bmp == null) {
                    bmp = cloudinary.loadImage(imageName);
                    //save the image locally for next time
                    if (bmp != null)
                        fileManager.saveImageToFile(bmp,imageName);
                }
                return bmp;
            }
            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(imageName, result);
            }
        };
        task.execute();
    }

    public void getAllProductsAsync(final GetProductsListenerInterface listener)
    {
        if(!firstInit)
        {
            final String lastUpdateDate = "";//StudentSql.getLastUpdateDate(modelSql.getReadbleDB());
            firebaseModel.getAllProductsAsync(new GetProductsListenerInterface() {
                @Override
                public void done(List<Product> products) {
                    if(products != null && products.size() > 0) {
                        data = products;
                        //TODO: update the local DB
                        /*String reacentUpdate = lastUpdateDate;
                        for (Student s : students) {
                            StudentSql.add(modelSql.getWritableDB(), s);
                            if (reacentUpdate == null || s.getLastUpdated().compareTo(reacentUpdate) > 0) {
                                reacentUpdate = s.getLastUpdated();
                            }
                            Log.d("TAG","updating: " + s.toString());
                        }
                        StudentSql.setLastUpdateDate(modelSql.getWritableDB(), reacentUpdate);*/
                    }
                    //return the complete student list to the caller
                    //List<Product> res = StudentSql.getAllStudents(modelSql.getReadbleDB());
                    listener.done(products);
                }
            });
        }
        else
            init();
    }

    private void init() {
        for (int i = 0; i < 2; i++) {
            Helper.ProductType type;
            Helper.Customers customer;
            Product product;

            if (i % 3 == 0)
                customer = Helper.Customers.MEN;
            else if (i % 3 == 1)
                customer = Helper.Customers.WOMEN;
            else
                customer = Helper.Customers.UNISEX;

            if (i % 4 == 0)
                type = Helper.ProductType.PANTS;
            else if (i % 4 == 1)
                type = Helper.ProductType.DRESS;
            else if (i % 4 == 2)
                type = Helper.ProductType.SHIRT;
            else
                type = Helper.ProductType.OTHER;

            product = new Product("" + i, type, "Description: " + i, 12 + i, customer, "sellerId: "+i, null);

            add(product);

        }
    }

    public void add(Product newProduct)
    {
        data.add(newProduct);
        cloudUpdate(newProduct);
        cachUpdate(newProduct);
    }

    private void cloudUpdate(Product newProduct)
    {
        firebaseModel.add(newProduct, new AddProductListener() {
            @Override
            public void done(Product pr) {
                Log.d("TAG", "Finish add product " + pr.getId());
            }
        });
        cloudinary.uploadImage(newProduct.getId(), newProduct.getImageProduct());
    }

    private void cachUpdate(Product newProduct)
    {
        fileManager.saveImageToFile(newProduct.getImageProduct(), newProduct.getId());
    }

    public Product getProduct(String id){
        for (int i=0; i<data.size(); i++) {
            Product product = data.get(i);

            if(product.getId().equals(id)){
                return product;
            }
        }
        return null;
    }

    public void updateProductInformation(String productId, Product newProduct)
    {
        for(int i=0; i<data.size(); i++) {
            Product product = data.get(i);

            if (product.getId().compareTo(productId) == 0)
            {
                product.setType(newProduct.getType());
                product.setDescription(newProduct.getDescription());
                product.setPrice(newProduct.getPrice());
                product.setForWhom(newProduct.getForWhom());
                product.setImageProduct(newProduct.getImageProduct());
                //TODO: check if update works
                cloudUpdate(product);
                cachUpdate(product);
            }
        }
    }

    public List<Product> getProducts(){
        return data;
    }

    public void delete(Product product){
        data.remove(product);
        firebaseModel.remove(product);
        //TODO: check if cannot use remove on cloudinary because only the admin can remove an object
        //cloudinary.removeImage(product.getId());
        fileManager.removeImage(product.getId());
    }

    public String getNewProductId()
    {
        Product lastProduct;
        int newId;

        if(!data.isEmpty())
        {
            lastProduct = data.get(data.size()-1);
            newId = Integer.parseInt(lastProduct.getId()) + 1;
        }
        else
        {
            newId = 1;
        }
        return  ""+newId;
    }

    public void add(Product pr, AddProductListener listener) {
        firebaseModel.add(pr, listener);
    }

    /*public void getProduct(String id, GetProductListener listener) {
        firebaseModel.getProduct(id, listener);
    }*/

    public void getProducts(GetProductsListenerInterface listener) {
        firebaseModel.getAllProductsAsync(listener);
    }

    public void login(String email, String password, AuthListener listener)
    {
        firebaseModel.login(email, password, listener);
    }

    public interface GetProductsListenerInterface {
        void done(List<Product> prList);
    }

    public interface AddProductListener {
        void done(Product pr);
    }

    public interface LoadImageListener{
        void onResult(String id, Bitmap imageBmp);
    }

    public interface AuthListener{
        void onDone(String userId, Exception e);
    }
}
