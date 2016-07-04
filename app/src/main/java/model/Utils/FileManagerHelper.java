package model.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yaniv on 04/07/2016.
 */
public class FileManagerHelper {
    private Context context;

    public FileManagerHelper(Context context)
    {
        this.context = context;
    }


    public Bitmap loadImageFromFile(String imageFileName){
        String str = null;
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);

            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            //Log.d("TAG", "Load image "+imageFileName+" from cache");
        } catch (FileNotFoundException e) {
            Log.d("TAG", "Could not find image "+imageFileName);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TAG", "General exception on load image "+imageFileName);
            e.printStackTrace();
        }
        return bitmap;
    }

    public void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        FileOutputStream fos;
        OutputStream out = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            if(imageFile.exists())
            {
                //TODO: update image that already exist
                Log.d("TAG", "image already exist, update image");
                return;
            }
            imageFile.createNewFile();
            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //add the picture to the gallery so we dont need to manage the cache size
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
            Log.d("tag","add image to cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            Log.d("TAG", "Could not save image "+imageFileName);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("TAG", "General exception on save image "+imageFileName);
            e.printStackTrace();
        }
    }
}
