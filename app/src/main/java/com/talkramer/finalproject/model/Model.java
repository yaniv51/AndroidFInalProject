package com.talkramer.finalproject.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.talkramer.finalproject.ApplicationStartup;
import com.talkramer.finalproject.model.Domain.Product;
import com.talkramer.finalproject.model.Domain.ProductSql;
import com.talkramer.finalproject.model.Utils.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import com.talkramer.finalproject.model.Utils.FileManagerHelper;
public class Model {

    private static Model instance;
    private ModelFirebase firebaseModel;
    private ModelCloudinary cloudinary;
    private FileManagerHelper fileManager;
    private ModelSql sqlModel;

    private boolean firstInit = false;

    List<Product> data;

    private Model()
    {
        data = new LinkedList<Product>();
        firebaseModel = new ModelFirebase(ApplicationStartup.getAppContext());
        cloudinary = new ModelCloudinary(ApplicationStartup.getAppContext());
        fileManager = new FileManagerHelper(ApplicationStartup.getAppContext());
        sqlModel = new ModelSql(ApplicationStartup.getAppContext());

        //ProductSql.drop(sqlModel.getWritableDB());
    }

    public static Model getInstance()
    {
        if(instance == null)
            instance = new Model();

        return instance;
    }

    public void loadImage(final Product product, final LoadImageListener listener) {
        final String  imageName = product.getId();
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                //first try to find the image on the device - use last updated for verify image is updated
                Bitmap bmp = fileManager.loadImageFromFile(imageName, product.getLastUpdated());

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
            final String lastUpdateDate = ProductSql.getLastUpdateDate(sqlModel.getReadbleDB());
            firebaseModel.getAllProductsAsync(new GetProductsListenerInterface() {
                @Override
                public void done(List<Product> products) {
                    List<Product> res = null;
                    if(products != null && products.size() > 0) {
                        //update the local DB
                        String reacentUpdate = lastUpdateDate;
                        for (Product p : products) {
                            //update DB and image cach
                            cachUpdate(p);
                            if (reacentUpdate == null || (p.getLastUpdated() != null && p.getLastUpdated().compareTo(reacentUpdate) > 0)) {
                                reacentUpdate = p.getLastUpdated();
                            }
                            Log.d("TAG","updating: " + p.toString());
                        }
                        ProductSql.setLastUpdateDate(sqlModel.getWritableDB(), reacentUpdate);
                        res = ProductSql.getAllProducts(sqlModel.getReadbleDB());
                    }
                    //return the complete product list to the caller
                    if(res == null)
                        res = new LinkedList<Product>();
                    data = res;
                    listener.done(res);
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
        newProduct.setLastUpdated(getCurrentDate());

        data.add(newProduct);
        cloudUpdate(newProduct);
        cachUpdate(newProduct);
    }

    public String getCurrentDate()
    {
        return formatDateToString(new Date());
    }

    public String formatDateToString(Date date)
    {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  dateFormatGmt.format(date).toString();
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
        ProductSql.add(sqlModel.getWritableDB(), newProduct);
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
                product.setLastUpdated(getCurrentDate());
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
        fileManager.removeImage(product.getId());
        boolean removed = ProductSql.deleteById(sqlModel.getWritableDB(), product.getId());
        if(removed)
        {

        }
        //TODO: check if cannot use remove on cloudinary because only the admin can remove an object
        //cloudinary.removeImage(product.getId());
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

    public void login(String email, String password, AuthListener listener)
    {
        firebaseModel.login(email, password, listener);
    }

    public void logout() {firebaseModel.logout();}

    public String getUserId() {return firebaseModel.getUserId();}

    public FirebaseUser getFirebaseUser() {return firebaseModel.getFirebaseUser();}

    public void signUp(String email, String password, SignupListener listener)
    {
        firebaseModel.signUp(email, password, listener);
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

    public interface SignupListener {
        public void success();
        public void fail(String msg);
    }
}
