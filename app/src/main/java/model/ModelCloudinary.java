package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.util.BufferRecycler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by Yaniv on 01/07/16.
 */
public class ModelCloudinary {
    Cloudinary cloudinary;

    public ModelCloudinary(Context context)
    {
        cloudinary = new Cloudinary("cloudinary://796956498877215:54yr5DOPhb2DUFJigNxgXf9PGYE@dugje2rt7");
    }

    public void uploadImage(final String imageName,final Bitmap image)
    {
        if(image == null)
            return;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                    //String name = imageName.substring(0,imageName.lastIndexOf("."));
                    Map res = null;
                    try {
                        res = cloudinary.uploader().upload(bs, ObjectUtils.asMap("public_id", imageName));
                        Log.d("TAG", "save image to url" + res.get("url"));
                    } catch (IOException e) {
                        Log.d("TAG", e.getMessage());
                        e.printStackTrace();
                    }
                }
                catch (Exception ex)
                {
                    Log.d("TAG", ex.getMessage());
                }
            }
        });
        t.start();
    }

    public Bitmap loadImage(String imageName) {
        URL url = null;
        try {
            url = new URL(cloudinary.url().generate(imageName));
            Log.d("TAG", "load image from url" + url);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "url" + url);

        return null;
    }
}