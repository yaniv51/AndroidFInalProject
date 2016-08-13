package com.talkramer.finalproject.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.talkramer.finalproject.ApplicationStartup;
import com.talkramer.finalproject.model.Domain.Product;
import com.talkramer.finalproject.model.Domain.ProductSql;
import com.talkramer.finalproject.model.Utils.FileManagerHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
public class Model {

    private static Model instance;
    private ModelFirebase firebaseModel;
    private ModelCloudinary cloudinary;
    private FileManagerHelper fileManager;
    private ModelSql sqlModel;
    private UpdateProductsListener firebaseListener, uiListener;

    List<Product> data;

    private Model()
    {
        data = new LinkedList<Product>();
        firebaseModel = new ModelFirebase(ApplicationStartup.getAppContext());
        cloudinary = new ModelCloudinary(ApplicationStartup.getAppContext());
        fileManager = new FileManagerHelper(ApplicationStartup.getAppContext());
        sqlModel = new ModelSql(ApplicationStartup.getAppContext());

        initializeListeners();
    }

    public static Model getInstance()
    {
        if(instance == null)
            instance = new Model();

        return instance;
    }

    private void initializeListeners()
    {
        firebaseListener = new UpdateProductsListener() {
            @Override
            public void notify(List<Product> products) {
                List<Product> updatedProducts;
                String lastUpdateDate;
                if(products == null)
                    return;
                lastUpdateDate = ProductSql.getLastUpdateDate(sqlModel.getReadbleDB());
                updatedProducts = updateLocalProducts(products, lastUpdateDate);
                data = updatedProducts;
                if(uiListener != null)
                    uiListener.notify(updatedProducts);
            }
        };
        firebaseModel.setNotifyUpdate(firebaseListener);
    }

    public void setUpdateUIListener(UpdateProductsListener listener)
    {
        uiListener = listener;
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
        final String lastUpdateDate = ProductSql.getLastUpdateDate(sqlModel.getReadbleDB());
        firebaseModel.getAllProductsAsync(new GetProductsListenerInterface() {
            @Override
            public void done(List<Product> products) {
                List<Product> res = null;

                res = updateLocalProducts(products, lastUpdateDate);

                if(res == null)
                    return;

                data = res;
                listener.done(res);
            }
        });
    }

    private List<Product> updateLocalProducts(List<Product> products, String lastUpdateDate)
    {
        List<Product> res = null;
        if(products != null && products.size() > 0) {
            //update the local DB
            String reacentUpdate = lastUpdateDate;
            for (Product p : products) {
                if (!p.getDeleted()) {
                    //update DB and image cach
                    cachUpdate(p);
                    if (reacentUpdate == null || (p.getLastUpdated() != null && p.getLastUpdated().compareTo(reacentUpdate) > 0)) {
                        reacentUpdate = p.getLastUpdated();
                    }
                    Log.d("TAG", "updating: " + p.toString());
                }
            }
            ProductSql.setLastUpdateDate(sqlModel.getWritableDB(), reacentUpdate);
            res = ProductSql.getAllProducts(sqlModel.getReadbleDB());
        }
        return res;
    }


    public void add(final Product newProduct, final OperationListener listener)
    {
        newProduct.setLastUpdated(getCurrentDate());
        //get cloud counter for set new product ID
        firebaseModel.getMaxItem(new GetMaxProductIdListener() {
            @Override
            public void success(int counter) {
                newProduct.setId((counter+1) +"");
                cloudinaryUpdate(newProduct, new OperationListener() {
                    @Override
                    public void success() {
                        firebaseModel.addNewProduct(newProduct, new OperationListener() {
                            @Override
                            public void success() {
                                listener.success();
                            }

                            @Override
                            public void fail(String msg) {
                                listener.fail(msg);
                            }
                        });
                    }

                    @Override
                    public void fail(String msg) {
                        listener.fail(msg);
                    }
                });
            }

            @Override
            public void fail(String msg) {
                listener.fail(msg);
            }
        });
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

    private void cloudinaryUpdate(Product newProduct, OperationListener listener)
    {
        cloudinary.uploadImage(newProduct.getId(), newProduct.getImageProduct(), listener);
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

    public void updateProductInformation(String productId, Product newProduct, OperationListener listener)
    {
        Product updatedProduct = null;

        for(int i=0; i<data.size(); i++) {
            updatedProduct = data.get(i);

            if (updatedProduct.getId().compareTo(productId) == 0) {
                updatedProduct.setType(newProduct.getType());
                updatedProduct.setDescription(newProduct.getDescription());
                updatedProduct.setPrice(newProduct.getPrice());
                updatedProduct.setForWhom(newProduct.getForWhom());
                updatedProduct.setImageProduct(newProduct.getImageProduct());
                updatedProduct.setLastUpdated(getCurrentDate());
                break;
            }
        }
        if(updatedProduct != null)
        {
            asyncUpdateProduct(updatedProduct, listener);
        }
    }

    public void asyncUpdateProduct(final Product product, final OperationListener listener)
    {
        cloudinaryUpdate(product, new OperationListener() {
            @Override
            public void success() {
                firebaseModel.updateProduct(product, listener);
                cachUpdate(product);
            }

            @Override
            public void fail(String msg) {
                listener.fail(msg);
            }
        });
    }

    public List<Product> getProducts(){
        return data;
    }

    public void delete(Product product, Model.OperationListener listener){
        product.setDeleted(true);
        data.remove(product);
        fileManager.removeImage(product.getId());
        boolean removed = ProductSql.deleteById(sqlModel.getWritableDB(), product.getId());

        firebaseModel.remove(product, listener);
        //TODO: check if cannot use remove on cloudinary because only the admin can remove an object
        //cloudinary.removeImage(product.getId());
    }

    public void buyProduct(Product product, OperationListener listener)
    {
        String buyerEmail = getUserEmail();
        product.setBuyerEmail(buyerEmail);
        cachUpdate(product);
        firebaseModel.updateProduct(product, listener);
    }

    public String getUserId() {return firebaseModel.getUserId();}

    public String getUserEmail() {return firebaseModel.getUserEmail();}

    public FirebaseUser getFirebaseUser() {return firebaseModel.getFirebaseUser();}

    public void signUp(String email, String password, OperationListener listener)
    {
        firebaseModel.signUp(email, password, listener);
    }

    public void login(String email, String password, AuthListener listener)
    {
        firebaseModel.login(email, password, listener);
    }

    public void logout() {firebaseModel.logout();}

    public void resetPassword(String email, OperationListener listener)
    {
        firebaseModel.resetPassword(email, listener);
    }

    public interface GetProductsListenerInterface {
        void done(List<Product> prList);
    }

    public interface LoadImageListener{
        void onResult(String id, Bitmap imageBmp);
    }

    public interface AuthListener{
        void onDone(String userId, Exception e);
    }

    public interface OperationListener {
        void success();
        void fail(String msg);
    }

    public interface GetMaxProductIdListener
    {
        void success(int counter);
        void fail(String msg);
    }

    public interface UpdateProductsListener
    {
        void notify(List<Product> products);
    }
}
