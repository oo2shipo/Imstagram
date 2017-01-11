package com.im.imstagram.image;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by vioooiv on 2017-01-10.
 */

public class ImageUtils
{
    public static final String TAG = "ImageUtils";

    /* inSampleSize 계산에 사용 : Set width/height of recreated image */
    public static int REQUIRED_SIZE = 320; //85

    /* 플랫폼별 상태바 사이즈 : Height */
    private static final int N_STATUS_BAR_HEIGHT_LOW_DPI = 19;
    private static final int N_STATUS_BAR_HEIGHT_MEDIUM_DPI = 25;
    private static final int N_STATUS_BAR_HEIGHT_HIGH_DPI = 38; // 갤럭시2
    private static final int N_STATUS_BAR_HEIGHT_GALLAXY_3 = 50; // 갤럭시 노트, 갤럭시3


    /**
     * memory 감소를 위해 scale 및 image 설정
     */
    public static Bitmap decodeFile(File f)
    {
        try {
            //Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            /* inJustDecodeBounds = true 일때 bitmap를 메모리에 할당하지 않고 options fields값(width, height) 이 채워진다. */
            options.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,options);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.

            int width_tmp=options.outWidth, height_tmp=options.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with current scale values
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;

        } catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * image rescale 및 memory 감소
     */
    public static Bitmap rescaleBitmap(String filepath, int targetSize)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        /* inJustDecodeBounds = true 일때 bitmap를 메모리에 할당하지 않고 options fields값(width, height) 이 채워진다. */
        options.inJustDecodeBounds = true;

        // 원하는 사이즈보다 큰 경우.
        if(options.outWidth > targetSize || options.outHeight > targetSize)
        {
            int scale_x = 1 + options.outWidth / targetSize;
            int scale_y = 1 + options.outHeight / targetSize;

            // 스케일 값을 설정해서 실제 이미지를 디코딩 한다.
            options.inSampleSize = Math.max(scale_x, scale_y);
        }

        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(filepath, options);

        return bmp;
    }

    /**
     * 배경 이미지 크기로 Resize
     */
    public static Bitmap resizeBgBitmap(Context context, int resourceId)
    {
        Bitmap bitmap = null;

        try
        {
            bitmap = ((BitmapDrawable) context.getResources().getDrawable(resourceId)).getBitmap();
            if(bitmap == null) {
                return null;
            }

			/* 윈도우 화면 크기 */
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();

            height = height - getStatusBarHeight(context); /* 상태바 크기 제외 */

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        catch(Exception e)
        {
            Log.e(TAG, "resizeBgBitmap");
        }

        return bitmap;
    }

    /**
     * Screen 상태바 높이
     */
    public static int getStatusBarHeight(Context context)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);

        int statusBarHeight = 0;

        switch(displayMetrics.densityDpi)
        {
            case 320: // 갤럭시 노트, 갤럭시3
                statusBarHeight = N_STATUS_BAR_HEIGHT_GALLAXY_3;
                break;

            case DisplayMetrics.DENSITY_HIGH: // 240
                statusBarHeight = N_STATUS_BAR_HEIGHT_HIGH_DPI;
                break;

            case DisplayMetrics.DENSITY_MEDIUM: // 160
                statusBarHeight = N_STATUS_BAR_HEIGHT_MEDIUM_DPI;
                break;

            case DisplayMetrics.DENSITY_LOW: // 120
                statusBarHeight = N_STATUS_BAR_HEIGHT_LOW_DPI;
                break;

            default:
                statusBarHeight = 0;
        }

        return statusBarHeight;
    }

    /**
     * Activity 상태바 높이
     */
    public static int getStatusBarHeightOnActivity(Activity activity)
    {
        int statusBarHeight = 0;

        Window window = activity.getWindow();

        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);

        statusBarHeight = rect.top; /* statusBar 높이 */

		/* 컨텐츠 영역 */
        //View contentView  = window.findViewById(Window.ID_ANDROID_CONTENT);
        //int contentHeight = contentView.getHeight();

        //int topBarHeight = contentView.getTop(); /* statusBar + titleBar 높이 */
        //int titleBarHeight = topBarHeight - statusBarHeight; /* titleBar 높이 */

        return statusBarHeight;
    }

    /**
     * dip -> px 변환
     */
    public static int convertDip2Px(Context context, int dipValue)
    {
        if(dipValue == ViewGroup.LayoutParams.MATCH_PARENT || dipValue == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return dipValue;
        }

        int pixelValue = 0;

        try
        {
            float scale = context.getResources().getDisplayMetrics().density;
            pixelValue = (int) (dipValue * scale + 0.5f); // 0.5f = 반올림용

            //pixelValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, resources.getDisplayMetrics());
        } catch(Exception e) {
            Log.e(TAG, "convertDip2Px()");
        }

        return pixelValue;
    }

    /**
     * px -> dip 변환
     */
    public static float convertPx2Dip(Context context, int pixelValue)
    {
        float dipValue = 0;

        try
        {
            float scale = context.getResources().getDisplayMetrics().density;
            dipValue = pixelValue / scale;
        } catch(Exception e) {
            Log.e(TAG, "convertPx2Dip()");
        }

        return dipValue;
    }

    /**
     * 최대(maxResolution) 사이즈에 맞게 이미지 해상도 변경
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int maxResolution)
    {
        if(bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        try
        {
            if(width > height) {
                newWidth = maxResolution;

                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
            } else {
                newHeight = maxResolution;

                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        } catch(Exception e) {
            Log.e(TAG, "resizeBitmap()");
        }

        return bitmap;
    }

    /**
     * 이미지에 라운드 테두리 처리
     */
    public static Bitmap getRoundedBitmap(Bitmap bitmap)
    {
        Bitmap output = null;

        try
        {
			/* 라운드 테두리 설정 */
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, 6.0f, 6.0f, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch(Exception e) {
            Log.e(TAG, "getRoundedBitmap()");
        }

        return output;
    }

    /**
     * 파일에서 이미지 가져오기 : 사이즈 조정
     */
    public static Bitmap getBitmapFile(Context context, String imagePath)
    {
        if(imagePath == null || imagePath.trim().equals("")) {
            return null;
        }

        Bitmap bitmapOriginal = null;

        try
        {
			/* dip -> px 변환 */
            //int scaleOnPixel = (int) Utils4Image.convertDip2Px(context, 600);

            /* 이미지는 절대값으로 설정 : Pixel */
            int scaleOnPixel = 1200; /* pixel */

            bitmapOriginal = rescaleAndRotateBitmap(context, imagePath, scaleOnPixel);
        } catch(Exception e) {
            Log.e(TAG, "getOriginalImageOnLocal()");
        }

        return bitmapOriginal;
    }

    /**
     * 이미지 파일(filepath)을 지정한 사이즈(targetSize)로 변환한후, 알맞게 회전해서 반환한다.
     */
    public static Bitmap rescaleAndRotateBitmap(Context context, String imagePath, int targetSize)
    {
        Bitmap bmp = null;

        bmp = rescaleBitmap(imagePath, targetSize);

        if(bmp != null)
        {
            ExifInterface exif;
            try
            {
                exif = new ExifInterface(imagePath);

                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

                int angle = 0;
                switch (rotation)
                {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        angle = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        angle = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        angle = 270;
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                    case -1:
                    default:
                        break;
                }

                if (angle > 0) {
                    Matrix mat = new Matrix();
                    // mat.postRotate(angle);
                    mat.setRotate(angle, bmp.getWidth() / 2, bmp.getHeight() / 2);
                    Bitmap convBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                    bmp.recycle();
                    bmp = convBmp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch(OutOfMemoryError e) {
                e.printStackTrace();
            }

        }
        return bmp;
    }

    /**
     * 둥근 이미지 생성
     */
    public static Bitmap getCircleBitmap(Resources resources, int resourceId)
    {
        Bitmap resImage =  BitmapFactory.decodeResource(resources, resourceId);

        return getCircleBitmap(resImage);
    }

    /**
     * 둥근 이미지 생성
     */
    public static Bitmap getCircleBitmap(Bitmap bmpImage)
    {
        if(bmpImage == null) {
            return null;
        }

        Bitmap circleBitmap = null;

        try
        {
            circleBitmap = getCircleBitmap(bmpImage, 70, false);
        } catch(Exception e) {
            return null;
        }

        return circleBitmap;
    }

    /**
     * (테두리 없는) 둥근 이미지를 반환한다.
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap, int diameter, boolean isRecycleOfOrgBmp)
    {
        Bitmap output = null;
        try
        {
            output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final Rect rect1 = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF1 = new RectF(rect1);
            final Rect rect2 = new Rect(0, 0, diameter, diameter);
            final RectF rectF2 = new RectF(rect2);

            canvas.drawARGB(0, 0, 0, 0);

            Paint paint = new Paint();
            paint.setAntiAlias(true);

            canvas.drawCircle(rectF2.centerX(), rectF2.centerY(), diameter / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect1, rect2, paint);

            if (isRecycleOfOrgBmp) {
                bitmap.recycle();
            }

        } catch (Exception ex) {
            // 예외가 발생하면, 리턴값은 null
            ex.printStackTrace();
            output = null;
        } catch (OutOfMemoryError err)  {
            // 메모리 에러가 발생하면, 리턴값은 null
            err.printStackTrace();
            output = null;
        }

        return output;
    }

    /**
     * Touch 시 이미지 효과 : ImageView
     */
    public static View.OnTouchListener getTouchEffectOnImageView()
    {
        return new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                ImageView imageView = (ImageView) view;

                switch(event.getAction() & MotionEvent.ACTION_MASK)
                {
                    case MotionEvent.ACTION_DOWN:
                        imageView.setColorFilter(0x77ffffff, PorterDuff.Mode.SRC_OVER);
                        break;

                    case MotionEvent.ACTION_UP:
                        imageView.clearColorFilter();
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        imageView.clearColorFilter();
                        break;
                }

                return false;
            }
        };
    }


    /**
     * Resource id 가져오기
     *
     * packageName : 패키지 이름 = context.getPackageName()
     * resName : 리소스 이름
     * resType : 리소스 타입 (xml, drawable, layout)
     */
    public static int getResourceId(Context context, String packageName, String resType, String resName)
    {
        int resId = context.getResources().getIdentifier(resName, resType, packageName);

        return resId;
    }

    /**
     * Resource id 가져오기 (layout, drawable, string, id, string)
     *
     * "main.xml"
     * int id = getResourceIdByName(context.getPackageName(), "layout", "main");
     * int id = getResourceIdByName(context.getPackageName(), "string", "text1");
     */
    public static int getResourceIdByName(String packageName, String className, String name)
    {
        Class r = null;
        int resId = 0;

        try
        {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; i++) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];

                    break;
                }
            }

            if (desireClass != null) {
                resId = desireClass.getField(name).getInt(desireClass);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return resId;
    }

}
