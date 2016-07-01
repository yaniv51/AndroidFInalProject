package model;

import android.graphics.Bitmap;

import com.talkramer.finalproject.ApplicationStartup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yaniv on 08/06/16.
 */
public class Model {

    private static Model instance;
    private Bitmap image;
    private ModelFirebase firebase;
    private ModelCloudinary cloudinary;

    List<Product> data;

    private Model()
    {
        data = new LinkedList<Product>();
        firebase = new ModelFirebase(ApplicationStartup.getAppContext());
        cloudinary = new ModelCloudinary(ApplicationStartup.getAppContext());
        init();
    }

    public void SetLocalBitmap(Bitmap image)
    {
        this.image = image;
        for(int i=0; i<data.size(); i++)
            data.get(i).setImageProduct(image);
    }

    public static Model getInstance()
    {
        if(instance == null)
            instance = new Model();

        return instance;
    }


    private void init() {
        for (int i = 0; i < 12; i++) {
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

            product = new Product("" + i, type, "Description: " + i, 12 + i, customer, "link " + i, "sellerId: "+i, image);

            add(product);

        }
    }

    public void add(Product newProduct)
    {
        data.add(newProduct);
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
                product.setImageProductLink(newProduct.getImageProductLink());
                product.setImageProduct(newProduct.getImageProduct());
            }
        }
    }

    public List<Product> getProducts(){
        return data;
    }

    public void delete(Product product){
        data.remove(product);
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
}
