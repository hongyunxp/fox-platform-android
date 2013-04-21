package com.foxchan.foxdiary.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

/**
 * 处理图片的工具类
 * @author chengqingmin@www.lezhixing.com.cn
 * @create 2013-3-6
 */
public class BitmapUtils {
	
	/** 通过相册获得图片 */
	public static final int REQUEST_IMAGE_BYSDCARD = 0x01;
	/** 通过相机获得图片 */
	public static final int REQUEST_IMAGE_BYCAMARA = 0x02;
	
	/**
	 * 将图片保存到SD卡上，文件将被保存到“/data/data/PACKAGE_NAME/files”目录下
	 * @param context
	 * @param fileName	文件保存的路径
	 * @param bitmap	保存的图片对象
	 */
	public static void persistImage(final Context context, final String fileName,
			final Bitmap bitmap) {
		persistImage(context, fileName, bitmap, 100);
	}
	
	/**
	 * 将图片保存到SD卡上，文件将被保存到“/data/data/PACKAGE_NAME/files”目录下
	 * @param context
	 * @param fileName	文件保存的路径
	 * @param bitmap	保存的图片对象
	 * @param quality	图片的质量
	 */
	public static void persistImage(final Context context, final String fileName,
			final Bitmap bitmap, final int quality) {
		if(bitmap == null) return;
		FileOutputStream fos = null;
		ByteArrayOutputStream bos = null;
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, quality, bos);
			byte[] bytes = bos.toByteArray();
			fos.write(bytes);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Closer.close(bos);
			Closer.close(fos);
		}
	}
	
	/**
	 * 从SD卡上读取图片文件
	 * @param context
	 * @param filePath	图片的路径
	 * @return			返回图片对象
	 */
	public static Bitmap loadBitmap(Context context, String filePath){
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = context.openFileInput(filePath);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			Closer.close(fis);
		}
		return bitmap;
	}
	
	/**
	 * 获得照相机使用的目录
	 * @return	返回照相机使用的目录
	 */
	public static String getCamaraPath(){
		return Environment.getExternalStorageDirectory() + File.separator +
				"FounderNews" + File.separator;
	}
	
	/**
     * 将Drawable转化为Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 获得圆角图片的方法
     * @param bitmap
     * @param roundPx 一般设成14
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获得带倒影的图片方法
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }
    
    /**
     * 将图片转化为灰度图
     * @param bmpOriginal	原来的图片
     * @return				返回处理后的黑白图片对象
     */
	public static Bitmap getBWBitmap(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}
    
    /**
     * 将bitmap转化为drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
    	Drawable drawable = new BitmapDrawable(bitmap);
    	return drawable;
    }
    
    /**
     * 获取图片类型
     * @param file
     * @return
     */
    public static String getImageType(File file){
        if(file == null||!file.exists()){
            return null;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            String type = getImageType(in);
            return type;
        } catch (IOException e) {
            return null;
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch(IOException e){
            }
        }
    }
    
    /**
     * detect bytes's image type by inputstream
     * @param in
     * @return
     * @see #getImageType(byte[])
     */
    public static String getImageType(InputStream in) {
        if(in == null){
            return null;
        }
        try{
            byte[] bytes = new byte[8];
            in.read(bytes);
            return getImageType(bytes);
        }catch(IOException e){
            return null;
        }
    }

    /**
     * detect bytes's image type
     * @param bytes 2~8 byte at beginning of the image file  
     * @return image mimetype or null if the file is not image
     */
    public static String getImageType(byte[] bytes) {
        if (isJPEG(bytes)) {
            return "image/jpeg";
        }
        if (isGIF(bytes)) {
            return "image/gif";
        }
        if (isPNG(bytes)) {
            return "image/png";
        }
        if (isBMP(bytes)) {
            return "application/x-bmp";
        }
        return null;
    }

    private static boolean isJPEG(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == (byte)0xFF) && (b[1] == (byte)0xD8);
    }

    private static boolean isGIF(byte[] b) {
        if (b.length < 6) {
            return false;
        }
        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == 0x42) && (b[1] == 0x4d);
    }
    
}
