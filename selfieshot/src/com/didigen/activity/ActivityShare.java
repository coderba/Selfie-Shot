package com.didigen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.didigen.R;
import com.didigen.models.Addon;
import com.didigen.models.Picture;
import com.didigen.utils.EffectUtil;
import com.didigen.utils.MyImageViewDrawableOverlay;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.imagezoom.ImageViewTouch;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;


/**
 * Created by Cem on 12/29/2015.
 */
public class ActivityShare extends FragmentActivity {

    public ImageView button_share;
    public ImageView buttonSticker;
    public GPUImageView imageView;
    public ImageView saveButton;
    public Uri uri;
    public MediaPlayer clickSound;
    public HorizontalScrollView stickerTool;
    private MyImageViewDrawableOverlay mImageViewDrawbleOverlay;
    private ViewGroup drawArea;
    public RelativeLayout shareLayout;

    public RelativeLayout sticker1, sticker2, sticker3, sticker4, sticker5, sticker6, sticker7, sticker8, smile1, smile2, smile3, smile4, smile5,
                          specialSticker1, specialSticker2, specialSticker3, specialSticker4, specialSticker5,specialSticker6, specialSticker7, specialSticker8,
                          specialSticker9, specialSticker10, specialSticker11, specialSticker12,specialSticker13,specialSticker14,specialSticker15,specialSticker16,
                          specialSticker17,specialSticker18,specialSticker19,specialSticker20,specialSticker21,specialSticker22,specialSticker23,specialSticker24,specialSticker25,
                          specialSticker26,specialSticker27,specialSticker28,specialSticker29,specialSticker30,specialSticker31,specialSticker32,specialSticker33,specialSticker34,
                          specialSticker35,specialSticker36,specialSticker37,specialSticker38;
    public View overlay;
    public Bitmap currentBitmap;
    public Intent share;
    public AVLoadingIndicatorView avLoadingIndicatorViewShare;
    public AdView mAdView;
    public AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    public Bitmap bitmap;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Runtime.getRuntime().maxMemory();


        mAdView = (AdView) findViewById(R.id.ad_view);
        adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mInterstitialAd= new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.banner_id_gecis));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        clickSound= MediaPlayer.create(getApplicationContext(), R.raw.button_click_default);
        buttonSticker= (ImageView) findViewById(R.id.button_sticker);
        imageView= (GPUImageView) findViewById(R.id.photo_taken_view);
        stickerInstall();
        stickerTool = (HorizontalScrollView) findViewById(R.id.sticker_tool);
        drawArea= (ViewGroup) findViewById(R.id.drawing_view_container);
        shareLayout= (RelativeLayout) findViewById(R.id.share_layout);
        saveButton= (ImageView) findViewById(R.id.save_button);
        avLoadingIndicatorViewShare= (AVLoadingIndicatorView) findViewById(R.id.share_process);
        button_share= (ImageView) findViewById(R.id.button_share);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        overlay = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_drawable_overlay, null);
        mImageViewDrawbleOverlay = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,height);
        mImageViewDrawbleOverlay.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);
        RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(width, height);
        imageView.setLayoutParams(rparams);

        uri= Picture.getmUriPicture();
        //bitmap= Picture.getmBitmapPicture();

        Log.i("getPicture_2", "" + Picture.getmBitmapPicture());
        Log.i("getUri_fromShare ", "" + Picture.getmUriPicture());



        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Bitmap mBitmap;
        mBitmap= getOrientation(uri.toString(), bitmap);

        imageView.setImage(mBitmap);

        share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    startActivity(Intent.createChooser(share, "Share Image"));
                }
                //clickSound.start();
            }
        });

        mImageViewDrawbleOverlay.setDoubleTapListener(new ImageViewTouch.OnImageViewTouchDoubleTapListener() {
            @Override
            public void onDoubleTap() {
                onBackPressed();
            }
        });

        mImageViewDrawbleOverlay.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                stickerTool.setVisibility(View.GONE);
            }
        });

        buttonSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerTool.setVisibility(View.VISIBLE);
                stickerListener();

            }
        });

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerTool.setVisibility(View.GONE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePicture();
                stickerTool.setVisibility(View.GONE);
                imageView.requestRender();

            }
        });

    }

    private void savePicture(){
        final Bitmap newBitmap = Bitmap.createBitmap(mImageViewDrawbleOverlay.getWidth(), mImageViewDrawbleOverlay.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageViewDrawbleOverlay.getWidth(), mImageViewDrawbleOverlay.getHeight());
        try {
            cv.drawBitmap(imageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cv.drawBitmap(currentBitmap, null, dst, null);
        }
        EffectUtil.applyOnSave(cv, mImageViewDrawbleOverlay);

        GPUImage gpuImage= new GPUImage(getApplicationContext());
        gpuImage.saveToPictures(newBitmap, "HD Camera",
                System.currentTimeMillis() + ".jpg",
                new GPUImage.OnPictureSavedListener() {
                    @Override
                    public void onPictureSaved(final Uri mUri) {
                        share.putExtra(Intent.EXTRA_STREAM, mUri);
                        imageView.setImage(mUri);
                        //toast();
                        Toast.makeText(getApplicationContext(), "Picture Saved!", Toast.LENGTH_SHORT).show();
                    }
                }, getApplicationContext(), avLoadingIndicatorViewShare);

        mImageViewDrawbleOverlay.clear();
        mImageViewDrawbleOverlay.clearOverlays();
    }

    private void stickerInstall(){

        smile1= (RelativeLayout) findViewById(R.id.smile_one);
        smile2= (RelativeLayout) findViewById(R.id.smile_two);
        smile3= (RelativeLayout) findViewById(R.id.smile_three);
        smile4= (RelativeLayout) findViewById(R.id.smile_four);
        smile5= (RelativeLayout) findViewById(R.id.smile_five);

        sticker1= (RelativeLayout) findViewById(R.id.sticker_one);
        sticker2= (RelativeLayout) findViewById(R.id.sticker_two);
        sticker3= (RelativeLayout) findViewById(R.id.sticker_three);
        sticker4= (RelativeLayout) findViewById(R.id.sticker_four);
        sticker5= (RelativeLayout) findViewById(R.id.sticker_five);
        sticker6= (RelativeLayout) findViewById(R.id.sticker_six);
        sticker7= (RelativeLayout) findViewById(R.id.sticker_seven);
        sticker8= (RelativeLayout) findViewById(R.id.sticker_eight);

        specialSticker1= (RelativeLayout) findViewById(R.id.ss1);
        specialSticker2= (RelativeLayout) findViewById(R.id.ss2);
        specialSticker3= (RelativeLayout) findViewById(R.id.ss3);
        specialSticker4= (RelativeLayout) findViewById(R.id.ss4);
        specialSticker5= (RelativeLayout) findViewById(R.id.ss5);
        specialSticker6= (RelativeLayout) findViewById(R.id.ss6);
        specialSticker7= (RelativeLayout) findViewById(R.id.ss7);
        specialSticker8= (RelativeLayout) findViewById(R.id.ss8);
        specialSticker9= (RelativeLayout) findViewById(R.id.ss9);
        specialSticker10= (RelativeLayout) findViewById(R.id.ss10);
        specialSticker11= (RelativeLayout) findViewById(R.id.ss11);
        specialSticker12= (RelativeLayout) findViewById(R.id.ss12);
        specialSticker13= (RelativeLayout) findViewById(R.id.ss13);
        specialSticker14= (RelativeLayout) findViewById(R.id.ss14);
        specialSticker15= (RelativeLayout) findViewById(R.id.ss15);
        specialSticker16= (RelativeLayout) findViewById(R.id.ss16);
        specialSticker17= (RelativeLayout) findViewById(R.id.ss17);
        specialSticker18= (RelativeLayout) findViewById(R.id.ss18);
        specialSticker19= (RelativeLayout) findViewById(R.id.ss19);
        specialSticker20= (RelativeLayout) findViewById(R.id.ss20);
        specialSticker21= (RelativeLayout) findViewById(R.id.ss21);
        specialSticker22= (RelativeLayout) findViewById(R.id.ss22);
        specialSticker23= (RelativeLayout) findViewById(R.id.ss23);
        specialSticker24= (RelativeLayout) findViewById(R.id.ss24);
        specialSticker25= (RelativeLayout) findViewById(R.id.ss25);
        specialSticker26= (RelativeLayout) findViewById(R.id.ss26);
        specialSticker27= (RelativeLayout) findViewById(R.id.ss27);
        specialSticker28= (RelativeLayout) findViewById(R.id.ss28);
        specialSticker29= (RelativeLayout) findViewById(R.id.ss29);
        specialSticker30= (RelativeLayout) findViewById(R.id.ss30);
        specialSticker31= (RelativeLayout) findViewById(R.id.ss31);
        specialSticker32= (RelativeLayout) findViewById(R.id.ss32);
        specialSticker33= (RelativeLayout) findViewById(R.id.ss33);
        specialSticker34= (RelativeLayout) findViewById(R.id.ss34);
        specialSticker35= (RelativeLayout) findViewById(R.id.ss35);
        specialSticker36= (RelativeLayout) findViewById(R.id.ss36);
        specialSticker37= (RelativeLayout) findViewById(R.id.ss37);
        specialSticker38= (RelativeLayout) findViewById(R.id.ss38);
    }

    private void stickerListener(){

        smile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "1");
                Addon sticker = EffectUtil.addonList.get(0);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        smile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "1");
                Addon sticker = EffectUtil.addonList.get(1);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        smile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "1");
                Addon sticker = EffectUtil.addonList.get(2);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        smile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "1");
                Addon sticker = EffectUtil.addonList.get(3);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        smile5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addon sticker = EffectUtil.addonList.get(4);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });
        sticker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "1");
                Addon sticker = EffectUtil.addonList.get(5);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        sticker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "2");
                Addon sticker = EffectUtil.addonList.get(6);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        sticker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "3");
                Addon sticker = EffectUtil.addonList.get(7);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        sticker4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "4");
                Addon sticker = EffectUtil.addonList.get(8);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        sticker5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "5");
                Addon sticker = EffectUtil.addonList.get(9);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        sticker6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "6");
                Addon sticker = EffectUtil.addonList.get(10);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });


        sticker7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "7");
                Addon sticker = EffectUtil.addonList.get(11);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        sticker8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(12);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(13);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(14);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(15);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(16);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(17);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(18);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(19);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(20);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(21);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(22);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(23);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(24);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(25);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(26);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(27);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(28);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(29);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(30);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(31);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(32);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(33);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(34);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });
        specialSticker23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(35);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(36);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(37);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(38);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(39);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(40);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(41);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(42);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(43);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(44);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(45);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(46);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(47);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(48);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker37.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(49);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });

        specialSticker38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sticker", "8");
                Addon sticker = EffectUtil.addonList.get(50);
                EffectUtil.addStickerImage(mImageViewDrawbleOverlay, ActivityShare.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {
                            }
                        });
            }
        });


    }

    public static Bitmap getOrientation(String path, Bitmap source){
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

    private void toast(){
        LinearLayout  layout=new LinearLayout(this);
        layout.setBackgroundColor(Color.rgb(249, 55, 7));

        TextView  tv=new TextView(this);
        // set the TextView properties like color, size etc
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);

        tv.setGravity(Gravity.CENTER_VERTICAL);

        // set the text you want to show in  Toast
        tv.setText("Picture Saved!");

        ImageView   img=new ImageView(this);

        // give the drawble resource for the ImageView

        // add both the Views TextView and ImageView in layout
        layout.addView(tv);

        Toast toast=new Toast(this); //context is object of Context write "this" if you are an Activity
        // Set The layout as Toast View
        toast.setView(layout);

        // Position you toast here toast position is 200 dp from bottom you can give any integral value
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.show();
    }

    public void onBackPressed(){
        imageView.requestRender();
        super.onBackPressed();
    }

}
