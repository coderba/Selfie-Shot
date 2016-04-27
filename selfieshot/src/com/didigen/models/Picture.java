package com.didigen.models;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Cem on 2/6/2016.
 */
public class Picture {

    public static Bitmap mBitmapPicture;
    public static Uri mUriPicture;

    public static void setmBitmapPicture(Bitmap bitmap){
        mBitmapPicture= bitmap;
    }

    public static Bitmap getmBitmapPicture(){
        return mBitmapPicture;
    }

    public static void setmUriPicture(Uri uri){
        mUriPicture= uri;
    }

    public static Uri getmUriPicture(){
        return mUriPicture;
    }

}
