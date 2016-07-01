package model;

import android.content.Context;

import com.cloudinary.Cloudinary;

/**
 * Created by Yaniv on 01/07/16.
 */
public class ModelCloudinary {
    Cloudinary cloudinary;

    public ModelCloudinary(Context context)
    {
        cloudinary = new Cloudinary("CLOUDINARY_URL=cloudinary://796956498877215:54yr5DOPhb2DUFJigNxgXf9PGYE@dugje2rt7");
    }
}
