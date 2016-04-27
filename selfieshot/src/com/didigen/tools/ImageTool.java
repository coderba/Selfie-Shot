package com.didigen.tools;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by Cem on 3/6/2016.
 */
public class ImageTool {

    public static int getOrientation(ExifInterface exifInterface){
        ExifInterface ei = exifInterface;
        int rotation;

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotation= 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotation= 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotation= 270;
                break;
            default:
                rotation= 0;
                break;

        }
        return rotation;
    }

    public static Bitmap setOrientation(String path, Bitmap source){
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                source=  rotateImage(source, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                source= rotateImage(source, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                source= rotateImage(source, 270);
                break;
            default:

                break;
        }
        return source;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

}
