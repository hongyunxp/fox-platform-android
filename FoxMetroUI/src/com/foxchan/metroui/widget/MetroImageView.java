package com.foxchan.metroui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

/**
 * Metro风格的ImageView控件
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年7月31日
 */
public class MetroImageView extends ImageView {
	
	public static final String TAG = "MetroImageView";
	
	// This is the base transformation which is used to show the image
	// initially. The current computation for this shows the image in
	// it's entirety, letterboxing as needed. One could choose to
	// show the image as cropped instead.
	//
	// This matrix is recomputed when we go from the thumbnail image to
	// the full size image.
	protected Matrix mBaseMatrix = new Matrix();
	// This is the supplementary transformation which reflects what
	// the user has done in terms of zooming and panning.
	//
	// This matrix remains the same when we go from the thumbnail image
	// to the full size image.
	protected Matrix mSuppMatrix = new Matrix();
	// This is the final matrix which is computed as the concatentation
	// of the base matrix and the supplementary matrix.
	protected final Matrix mDisplayMatrix = new Matrix();
	// Temporary buffer used for getting the values out of a matrix.
	protected final float[] mMatrixValues = new float[9];
	// The current bitmap being displayed.
	// protected final RotateBitmap mBitmapDisplayed = new RotateBitmap(null);
	protected Bitmap bitmap = null;
	
	private int mThisWidth = -1;
	private int mThisHeight = -1;
	/** 最大缩放比例 */
	private float mMaxZoom = 2.0f;
	/** 最小缩放比例 */
	private float mMinZoom = 1.0f;
	/** 图片的原始宽度 */
	private int bmpOriginalWidth;
	/** 图片的原始高度 */
	private int bmpOriginalHeight;
	/** 图片适应屏幕的缩放比例 */
	private float scaleRate;
	
	/**
	 * 构造一个MetroImageView
	 * @param context
	 * @param bmpOriginalWidth	图片原来的宽度
	 * @param bmpOriginalHeight	图片原来的高度
	 */
	public MetroImageView(Context context, int bmpOriginalWidth, int bmpOriginalHeight){
		super(context);
		this.bmpOriginalWidth = bmpOriginalWidth;
		this.bmpOriginalHeight = bmpOriginalHeight;
		init();
	}

	public MetroImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	/**
	 * 初始化控件
	 */
	private void init(){
		setScaleType(ImageView.ScaleType.MATRIX);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		bitmap = bm;
		//缩放到屏幕大小
//		zoomTo();
		//居中
//		layoutToCenter();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}

}
