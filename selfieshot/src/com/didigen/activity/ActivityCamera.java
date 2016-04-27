//WRITTEN BY: CEM BASAR BASKAN

package com.didigen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.didigen.GPUImageFilterTools;
import com.didigen.R;
import com.didigen.models.Picture;
import com.didigen.tools.ImageTool;
import com.didigen.utils.CameraHelper;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.lantouzi.wheelview.WheelView;
import com.wang.avi.AVLoadingIndicatorView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import io.fabric.sdk.android.Fabric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImage.OnPictureSavedListener;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorDodgeBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFalseColorFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLaplacianFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNonMaximumSuppressionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWeakPixelInclusionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;


public class ActivityCamera extends Activity {
    private static final String TAG= ActivityCamera.class.getSimpleName();

    public GPUImage mGPUImage;
    public CameraHelper mCameraHelper;
    public CameraLoader mCamera;
    public GPUImageFilter mFilter;
    public GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
    public Bitmap bitmap;
    public ImageButton buttonFilter, buttonCapture, buttonTimer;
    public Context mainContext;
    public RelativeLayout barLayout;
    public WheelView mWheelView;
    public int timerValue;
    public int wheelValue;
    public boolean isTimerClick;
    public boolean isCountDownOn;
    public ImageButton buttonTimerSelect, buttonShuffle;
    public ImageView buttonFlash, buttonCamSwitch;
    public ToggleButton buttonVignette;
    public TextView textViewCount;
    public Typeface typeface;
    public CountDownTimer counterWorker;
    public int defaultSelectWhell = 0;
    public GLSurfaceView glSurfaceView;
    public int mode;
    public float pointX, pointY;
    public static final int FOCUS = 1;
    public Handler handler = new Handler();
    public View focusIndex, cameraSwitchView;
    public Camera.Parameters parameters;
    public DisplayMetrics displayMetrics;
    public int countView = 0;
    public String countViewString = null;
    public DiscreteSeekBar seekBar;
    public MediaPlayer clickDefaultSound, countdownSound, wheelChangeSound, takePhotoSound;
    public Intent intent;
    public AVLoadingIndicatorView avLoadingIndicatorView;
    public HorizontalScrollView horizontalScrollView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        Runtime.getRuntime().maxMemory();

        setContentView(R.layout.activity_camera);
        mainContext = getApplicationContext();

        isTimerClick = false;
        isCountDownOn = false;

        mCameraHelper = new CameraHelper(this);
        mCamera = new CameraLoader();

        clickDefaultSound = MediaPlayer.create(mainContext, R.raw.button_click_default);
        countdownSound = MediaPlayer.create(mainContext, R.raw.countdown);
        wheelChangeSound = MediaPlayer.create(mainContext, R.raw.wheel_change);
        takePhotoSound = MediaPlayer.create(mainContext, R.raw.take_photo);

        cameraSwitchView = findViewById(R.id.img_switch_camera);
        seekBar = ((DiscreteSeekBar) findViewById(R.id.seekBar_camera));
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.progress_custom);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.scroll_view);
        barLayout = (RelativeLayout) findViewById(R.id.bar);
        mGPUImage = new GPUImage(this);
        mGPUImage.setGLSurfaceView((GLSurfaceView) findViewById(R.id.surfaceView));
        textViewCount = (TextView) findViewById(R.id.count_text_view);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/trs_million.ttf");
        textViewCount.setTypeface(typeface);
        buttonVignette = (ToggleButton) findViewById(R.id.button_viginette);
        buttonFilter = (ImageButton) findViewById(R.id.button_filter);
        buttonFlash = (ImageView) findViewById(R.id.button_flash);
        buttonFlash.setBackgroundResource(R.drawable.flash_off_red);
        buttonShuffle = (ImageButton) findViewById(R.id.button_shuffle);
        buttonCamSwitch = (ImageView) findViewById(R.id.img_switch_camera);
        buttonCapture = (ImageButton) findViewById(R.id.button_capture);
        buttonTimer = (ImageButton) findViewById(R.id.button_timer);

        ///setting touchable effect to surface
        glSurfaceView = (GLSurfaceView) findViewById(R.id.surfaceView);
        focusIndex = (View) findViewById(R.id.focus_index);

        if (!mCameraHelper.hasFrontCamera() || !mCameraHelper.hasBackCamera()) {
            cameraSwitchView.setVisibility(View.GONE);
        }

        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, final int value, boolean fromUser) {
                if (mFilterAdjuster != null) {
                    mFilterAdjuster.adjust(value);
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });

        buttonVignette.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //clickDefaultSound.start();
                    switchFilterTo(new GPUImageVignetteFilter());
                    buttonVignette.setBackgroundResource(R.drawable.small_lens_filled_red);
                } else {
                    //clickDefaultSound.start();
                    switchFilterTo(new GPUImageFilter());
                    buttonVignette.setBackgroundResource(R.drawable.small_lens_red);
                }
            }
        });

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clickDefaultSound.start();
                horizontalScrollView.setVisibility(View.VISIBLE);
                barLayout.setVisibility(View.INVISIBLE);
                filterListener();
            }
        });

        buttonFlash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                turnLight(mCamera.mCameraInstance);
            }
        });

        buttonShuffle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //clickDefaultSound.start();
                switchFilterTo(filterShuffle());
            }
        });

        buttonCapture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(), "DEVELOPED BY C.B.B", Toast.LENGTH_LONG).show();
                final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.blink);

                if (isTimerClick == false) {
                    takePhotoSound.start();
                    takePicture();
                }

                if (isTimerClick == true && isCountDownOn == false) {

                    int tValueMilis;
                    if (timerValue == 0) {
                        tValueMilis = 0;
                    } else {
                        tValueMilis = (timerValue + 1) * 1000;
                    }

                    countView = wheelValue;
                    counterWorker = new CountDownTimer(tValueMilis, 1000) {
                        @Override
                        public void onTick(long l) {

                            isCountDownOn = true;

                            countViewString = Integer.toString(countView);
                            textViewCount.setVisibility(View.VISIBLE);
                            textViewCount.setText(countViewString);
                            countdownSound.start();
                            buttonCapture.startAnimation(animation);
                            textViewCount.startAnimation(animation);

                            countView--;
                        }

                        @Override
                        public void onFinish() {
                            takePhotoSound.start();
                            takePicture();
                            buttonCapture.clearAnimation();
                            textViewCount.clearAnimation();
                            textViewCount.setVisibility(View.GONE);

                            isCountDownOn = false;
                        }
                    }.start();
                }

                if (isTimerClick == true && isCountDownOn == true) {

                    clickDefaultSound.start();
                    counterWorker.cancel();
                    buttonCapture.clearAnimation();
                    textViewCount.clearAnimation();
                    textViewCount.setVisibility(View.GONE);
                    isCountDownOn = false;
                }
            }
        });

        buttonCamSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //clickDefaultSound.start();
                mCamera.switchCamera();
            }
        });

        buttonTimer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

                //clickDefaultSound.start();
                barLayout.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);

                final List<String> seconds_arr = new ArrayList<>();
                for (int i = 0; i <= 10; i++) {
                    seconds_arr.add(String.valueOf(i));
                }

                final PopupWindow popupWindow = new PopupWindow(view.getContext());

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.wheel_layout);
                LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = layoutInflater.inflate(R.layout.whell_view, linearLayout);

                mWheelView = (WheelView) layout.findViewById(R.id.wheel_view_item);
                mWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                    @Override
                    public void onWheelItemSelected(WheelView wheelView, int position) {

                        wheelChangeSound.start();

                        if (Integer.parseInt(wheelView.getItems().get(position)) == 0) {
                            isTimerClick = false;
                        }
                        //returns selected second.
                        wheelValue = Integer.parseInt(wheelView.getItems().get(position));
                        defaultSelectWhell = wheelValue;
                    }

                    @Override
                    public void onWheelItemChanged(WheelView wheelView, int position) {
                        wheelChangeSound.start();
                    }
                });

                mWheelView.setItems(seconds_arr);
                mWheelView.selectIndex(defaultSelectWhell);
                mWheelView.setAdditionCenterMark("sec");

                buttonTimerSelect = (ImageButton) layout.findViewById(R.id.button_timer_select);
                buttonTimerSelect.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //clickDefaultSound.start();
                        timerValue = wheelValue;
                        isTimerClick = true;
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setContentView(layout);
                popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(layout, Gravity.BOTTOM, 15, 15);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        barLayout.setVisibility(View.VISIBLE);
                        seekBar.setVisibility(View.VISIBLE);
                    }
                });

            }
        });


        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (((event.getAction()) & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    pointX = event.getX();
                    pointY = event.getY();
                    mode = FOCUS;
                }

                if (((event.getAction()) & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
                    if (spacing(event) > 10f) {
                    }
                }

                if (((event.getAction()) & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                }

                if (((event.getAction()) & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
                    mode = FOCUS;
                }

                if (((event.getAction()) & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
                    if (mode == FOCUS) {
                    }
                }
                return false;
            }
        });

        glSurfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalScrollView.setVisibility(View.GONE);
                seekBar.setVisibility(View.VISIBLE);
                barLayout.setVisibility(View.VISIBLE);
                try {
                    pointFocus((int) pointX, (int) pointY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countdownSound.start();
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(focusIndex.getLayoutParams());
                layout.setMargins((int) pointX - 60, (int) pointY - 60, 0, 0);
                focusIndex.setLayoutParams(layout);
                focusIndex.setVisibility(View.VISIBLE);
                ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(800);
                focusIndex.startAnimation(sa);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        focusIndex.setVisibility(View.INVISIBLE);
                    }
                }, 800);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.onResume();
    }

    @Override
    protected void onPause() {
        mCamera.onPause();
        super.onPause();
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    private void takePicture() {
        // TODO get a size that is about the size of the screen
        Camera.Parameters params = mCamera.mCameraInstance.getParameters();

        setDispaly(params, mCamera.mCameraInstance);

        //params.setRotation(90);

        List<Camera.Size> sizeList = params.getSupportedPictureSizes();

        final int lastItem = sizeList.size();
//        final int width = sizeList.get(Math.round(lastItem / 2) + 2).width;
//        final int height = sizeList.get(Math.round(lastItem / 2) + 2).height;
        final int width = sizeList.get(Math.round(lastItem / 2) ).width;
        final int height = sizeList.get(Math.round(lastItem / 2) ).height;
        for (Camera.Size size : params.getSupportedPictureSizes()) {
            Log.i("Screen Size ", "Supported: " + size.width + "x" + size.height);
            if (size.width == 1920 && size.height == 1080) {
                params.setPictureSize(1920, 1080);
                Log.i("HD", "IMAGE");
                break;
            } else {
                params.setPictureSize(width, height);
                Log.i("MEDIUM QUALITY", "IMAGE");
            }
        }

        mCamera.mCameraInstance.setParameters(params);

        mCamera.mCameraInstance.takePicture(null, null,
                new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, final Camera camera) {



                        final File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                        //buraya müdahale edilecek, 90 Portrait yerine landscape dosya yaratýyor.



                        if (pictureFile == null) {
                            Log.d("mCAMERAdistance",
                                    "Error creating media file, check storage permissions");
                            return;
                        }
                        pictureFile.setReadable(true, false);
                        pictureFile.setWritable(true, false);
                        try {
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            bitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
                            if(bitmap.getWidth()> bitmap.getHeight()){
                                bitmap = ImageTool.rotateImage(bitmap, 90);
                                Log.i(TAG, "ROTATE IT 90 DEGREE");
                            }
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Log.d("mCAMERAdistance", "File not found: " + e.getMessage());
                        } catch (IOException e) {
                            Log.d("mCAMERAdistance", "Error accessing file: " + e.getMessage());
                        }

                        // mGPUImage.setImage(bitmap);
                        final GLSurfaceView view = (GLSurfaceView) findViewById(R.id.surfaceView);
                        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                        mGPUImage.saveToPictures(bitmap, "Selfie Shot",
                                System.currentTimeMillis() + ".jpg",
                                new OnPictureSavedListener() {
                                    @Override
                                    public void onPictureSaved(final Uri uri) {

                                        pictureFile.delete();
                                        //camera.startPreview();
                                        //view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                                        Picture.setmBitmapPicture(bitmap);
                                        Picture.setmUriPicture(uri);
                                        mGPUImage.requestRender();
                                        intent = new Intent(getApplicationContext(), ActivityShare.class);
                                        startActivity(intent);
                                    }
                                }, getApplicationContext(), avLoadingIndicatorView);

                    }
                });
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static File getOutputMediaFile(final int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Selfie Shot");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Selfie Shot", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    public void switchFilterTo(final GPUImageFilter filter) {
        mFilter = filter;
        mGPUImage.setFilter(mFilter);
        mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
    }

    public class CameraLoader {
        public int mCurrentCameraId = 0;
        public Camera mCameraInstance;

        public void onResume() {
            setUpCamera(mCurrentCameraId);
        }

        public void onPause() {
            releaseCamera();
        }

        public void switchCamera() {
            releaseCamera();
            mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
            setUpCamera(mCurrentCameraId);
        }

        public void setUpCamera(final int id) {
            mCameraInstance = getCameraInstance(id);
            Parameters parameters = mCameraInstance.getParameters();

            // TODO adjust by getting supportedPreviewSizes and then choosing
            // the best one for screen size (best fill screen)
            if (parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            mCameraInstance.setParameters(parameters);
            int orientation = mCameraHelper.getCameraDisplayOrientation(ActivityCamera.this, mCurrentCameraId);
            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
            mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT;
            mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);
        }

        public Camera getCameraInstance(final int id) {
            Camera c = null;
            try {
                c = mCameraHelper.openCamera(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return c;
        }

        public void releaseCamera() {
            mCameraInstance.setPreviewCallback(null);
            mCameraInstance.release();
            mCameraInstance = null;
        }
    }

    //CLICK FOCUS METHODS
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    int curZoomValue = 0;

    private void addZoomIn(int delta) {
        try {
            Camera.Parameters params = mCamera.mCameraInstance.getParameters();
            Log.d("Camera", "Is support Zoom " + params.isZoomSupported());
            if (!params.isZoomSupported()) {
                return;
            }
            curZoomValue += delta;
            if (curZoomValue < 0) {
                curZoomValue = 0;
            } else if (curZoomValue > params.getMaxZoom()) {
                curZoomValue = params.getMaxZoom();
            }

            if (!params.isSmoothZoomSupported()) {
                params.setZoom(curZoomValue);
                mCamera.mCameraInstance.setParameters(params);
                return;
            } else {
                mCamera.mCameraInstance.startSmoothZoom(curZoomValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pointFocus(int x, int y) {
        mCamera.mCameraInstance.cancelAutoFocus();
        parameters = mCamera.mCameraInstance.getParameters();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            showPoint(x, y);
        }
        mCamera.mCameraInstance.setParameters(parameters);
        autoFocus();
    }

    private void showPoint(int x, int y) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            //xy???
            int rectY = -x * 2000 / getScreenWidth() + 1000;
            int rectX = y * 2000 / getScreenHeight() - 1000;

            int left = rectX < -900 ? -1000 : rectX - 100;
            int top = rectY < -900 ? -1000 : rectY - 100;
            int right = rectX > 900 ? 1000 : rectX + 100;
            int bottom = rectY > 900 ? 1000 : rectY + 100;
            Rect area1 = new Rect(left, top, right, bottom);
            areas.add(new Camera.Area(area1, 800));
            parameters.setMeteringAreas(areas);
        }
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    public int getScreenHeight() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.widthPixels;
    }

    public void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
        this.displayMetrics = DisplayMetrics;
    }

    public void autoFocus() {

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mCamera.mCameraInstance == null) {
//                    return;
//                }
//                mCamera.mCameraInstance.autoFocus(new Camera.AutoFocusCallback() {
//                    @Override
//                    public void onAutoFocus(boolean success, Camera camera) {
//                        Log.i("onAutoFocus ",""+success);
//                        if (success) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//                            } else {
//                                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//                            }
//                            setDispaly(parameters, mCamera.mCameraInstance);
//                            try {
//                                mCamera.mCameraInstance.setParameters(parameters);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            mCamera.mCameraInstance.startPreview();
//                        }
//                    }
//                });
//
//            }
//        });

        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    Log.i("metallica", "cbb");
                    e.printStackTrace();
                }
                if (mCamera.mCameraInstance == null) {
                    return;
                }
                mCamera.mCameraInstance.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        Log.i("onAutoFocus ", "" + success);
                        if (success) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                            } else {
                                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                            }
                            setDispaly(parameters, mCamera.mCameraInstance);
                            try {
                                mCamera.mCameraInstance.setParameters(parameters);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mCamera.mCameraInstance.startPreview();
                        }
                    }
                });
            }
        }.start();
    }

    public void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Build.VERSION.SDK_INT >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }
    }

    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation",
                    new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("setDisplayOrientation()", "error");
        }
    }

    public void filterListener() {

        RelativeLayout relativeLayout_zero = (RelativeLayout) findViewById(R.id.zero);
        relativeLayout_zero.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFilterTo(new GPUImageFilter());
                barLayout.setVisibility(View.VISIBLE);
                horizontalScrollView.setVisibility(View.GONE);

            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.one);
        relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageContrastFilter());
                barLayout.setVisibility(View.VISIBLE);
            }
        });

        RelativeLayout relativeLayout_2 = (RelativeLayout) findViewById(R.id.two);
        relativeLayout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageGrayscaleFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_3 = (RelativeLayout) findViewById(R.id.three);
        relativeLayout_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageSharpenFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayou_4 = (RelativeLayout) findViewById(R.id.four);
        relativeLayou_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageSobelEdgeDetection());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_5 = (RelativeLayout) findViewById(R.id.five);
        relativeLayout_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageDirectionalSobelEdgeDetectionFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_6 = (RelativeLayout) findViewById(R.id.six);
        relativeLayout_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImage3x3ConvolutionFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_7 = (RelativeLayout) findViewById(R.id.seven);
        relativeLayout_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageFilterGroup());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_8 = (RelativeLayout) findViewById(R.id.eight);
        relativeLayout_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageEmbossFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_9 = (RelativeLayout) findViewById(R.id.nine);
        relativeLayout_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImagePosterizeFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_10 = (RelativeLayout) findViewById(R.id.ten);
        relativeLayout_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageGammaFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_11 = (RelativeLayout) findViewById(R.id.eleven);
        relativeLayout_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageColorInvertFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_12 = (RelativeLayout) findViewById(R.id.twelve);
        relativeLayout_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageHueFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_13 = (RelativeLayout) findViewById(R.id.thirteen);
        relativeLayout_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImagePixelationFilter());
                //popupWindow.dismiss();
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_14 = (RelativeLayout) findViewById(R.id.fourteen);
        relativeLayout_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageSaturationFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_15 = (RelativeLayout) findViewById(R.id.fifteen);
        relativeLayout_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageExposureFilter());
                //popupWindow.dismiss();
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_16 = (RelativeLayout) findViewById(R.id.sixteen);
        relativeLayout_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageHighlightShadowFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_17 = (RelativeLayout) findViewById(R.id.seventeen);
        relativeLayout_17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageMonochromeFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_18 = (RelativeLayout) findViewById(R.id.eightteen);
        relativeLayout_18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageOpacityFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_19 = (RelativeLayout) findViewById(R.id.nineteen);
        relativeLayout_19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageRGBFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_20 = (RelativeLayout) findViewById(R.id.twenty);
        relativeLayout_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageWhiteBalanceFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_21 = (RelativeLayout) findViewById(R.id.twentyone);
        relativeLayout_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageVignetteFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_22 = (RelativeLayout) findViewById(R.id.twentytwo);
        relativeLayout_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageToneCurveFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_23 = (RelativeLayout) findViewById(R.id.twentythree);
        relativeLayout_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
//                switchFilterTo(new GPUImageColorBurnBlendFilter());
                switchFilterTo(new GPUImageHazeFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_24 = (RelativeLayout) findViewById(R.id.twentyfour);
        relativeLayout_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageColorDodgeBlendFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });


        RelativeLayout relativeLayout_feight = (RelativeLayout) findViewById(R.id.feight);
        relativeLayout_feight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageLookupFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_seight = (RelativeLayout) findViewById(R.id.seight);
        relativeLayout_seight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageColorBalanceFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_25 = (RelativeLayout) findViewById(R.id.twentyfive);
        relativeLayout_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "25", Toast.LENGTH_SHORT).show();
                switchFilterTo(new GPUImageSepiaFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });


        RelativeLayout relativeLayout_26 = (RelativeLayout) findViewById(R.id.fnine);
        relativeLayout_26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageGaussianBlurFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });


        ///
        RelativeLayout relativeLayout_27 = (RelativeLayout) findViewById(R.id.fifty);
        relativeLayout_27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageCrosshatchFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });
        ///

        RelativeLayout relativeLayout_28 = (RelativeLayout) findViewById(R.id.ffone);
        relativeLayout_28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageBoxBlurFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_29 = (RelativeLayout) findViewById(R.id.fftwo);
        relativeLayout_29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageCGAColorspaceFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_30 = (RelativeLayout) findViewById(R.id.ffthree);
        relativeLayout_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageDilationFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_31 = (RelativeLayout) findViewById(R.id.fffour);
        relativeLayout_31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageKuwaharaFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_32 = (RelativeLayout) findViewById(R.id.fffive);
        relativeLayout_32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageRGBDilationFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_33 = (RelativeLayout) findViewById(R.id.ffsix);
        relativeLayout_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageSketchFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_34 = (RelativeLayout) findViewById(R.id.ffseven);
        relativeLayout_34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageToonFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_35 = (RelativeLayout) findViewById(R.id.ffeight);
        relativeLayout_35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageToneCurveFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_36 = (RelativeLayout) findViewById(R.id.ffnine);
        relativeLayout_36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageBulgeDistortionFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_37 = (RelativeLayout) findViewById(R.id.sixty);
        relativeLayout_37.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageSphereRefractionFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_38 = (RelativeLayout) findViewById(R.id.sone);
        relativeLayout_38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageHazeFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_39 = (RelativeLayout) findViewById(R.id.stwo);
        relativeLayout_39.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageLaplacianFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_40 = (RelativeLayout) findViewById(R.id.sthree);
        relativeLayout_40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageNonMaximumSuppressionFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_41 = (RelativeLayout) findViewById(R.id.sfour);
        relativeLayout_41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);
                switchFilterTo(new GPUImageGlassSphereFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

        RelativeLayout relativeLayout_42 = (RelativeLayout) findViewById(R.id.sfive);
        relativeLayout_42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageSwirlFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });
        RelativeLayout relativeLayout_43 = (RelativeLayout) findViewById(R.id.ssix);
        relativeLayout_43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageWeakPixelInclusionFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });
        RelativeLayout relativeLayout_44 = (RelativeLayout) findViewById(R.id.sseven);
        relativeLayout_44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageFalseColorFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });
        RelativeLayout relativeLayout_45 = (RelativeLayout) findViewById(R.id.snine);
        relativeLayout_45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horizontalScrollView.setVisibility(View.GONE);

                switchFilterTo(new GPUImageLevelsFilter());
                barLayout.setVisibility(View.VISIBLE);

            }
        });

    }

    public void turnLight(Camera mCamera) {
        if (mCamera == null || mCamera.getParameters() == null
                || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = mCamera.getParameters().getFlashMode();
        List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {//????
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
            buttonFlash.setBackgroundResource(R.drawable.flash_on_red);

        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {//????
            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                buttonFlash.setBackgroundResource(R.drawable.flash_auto_red);
                mCamera.setParameters(parameters);

            } else if (supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                buttonFlash.setBackgroundResource(R.drawable.flash_off_red);
                mCamera.setParameters(parameters);
            }

        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            buttonFlash.setBackgroundResource(R.drawable.flash_off_red);
        }
    }

    public GPUImageFilter filterShuffle() {
        Random randomNumbers = new Random();
        int number = randomNumbers.nextInt(20);

        switch (number) {
            case 0:
                return new GPUImageSepiaFilter();
            case 1:
                return new GPUImageGrayscaleFilter();
            case 2:
                return new GPUImageSharpenFilter();
            case 3:
                return new GPUImageEmbossFilter();
            case 4:
                return new GPUImageSobelEdgeDetection();
            case 5:
                return new GPUImageWhiteBalanceFilter();
            case 6:
                return new GPUImageWeakPixelInclusionFilter();
            case 7:
                return new GPUImageGammaFilter();
            case 8:
                return new GPUImagePosterizeFilter();
            case 9:
                return new GPUImageCrosshatchFilter();
            case 10:
                return new GPUImageColorInvertFilter();
            case 11:
                return new GPUImageHueFilter();
            case 12:
                return new GPUImagePixelationFilter();
            case 13:
                return new GPUImageSaturationFilter();
            case 14:
                return new GPUImageMonochromeFilter();
            case 15:
                return new GPUImageExposureFilter();
            case 16:
                return new GPUImageHighlightShadowFilter();
            case 17:
                return new GPUImageMonochromeFilter();
            case 18:
                return new GPUImageOpacityFilter();
            case 19:
                return new GPUImageRGBFilter();
            default:
                return new GPUImageGaussianBlurFilter();
        }

    }
    public static int getImageOrientation(File file_){
        int rotate = 0;
        try {
            //File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    file_.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }
}



