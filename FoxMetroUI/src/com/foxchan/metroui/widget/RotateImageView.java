package com.foxchan.metroui.widget;

import com.foxchan.foxutils.tool.PhoneUtils;
import com.foxchan.metroui.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * 支持点击效果的图片控件
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年8月1日
 */
public class RotateImageView extends ImageView {
	
	private boolean onAnimation = true;
	private int rotateSize = 30;
	private int rotateDegree = 10;
	
	/** 图标在横向上旋转的角度 */
	private int rotateDegreeX = 0;
	/** 图标在纵向上旋转的角度 */
	private int rotateDegreeY = 0;
	
	private boolean isFirst = true;
	/** 缩放的比例 */
	private float zoomRate = 0.90f;
	/** 控件的宽度 */
	private int vWidth;
	/** 控件的高度 */
	private int vHeight;
	/** 图片的宽度 */
	private int imageWidth;
	/** 图片的高度 */
	private int imageHeight;
	/** 图片的宽度是否需要充满屏幕 */
	private boolean isWidthFillScreen;
	/** 图片的高度是否需要充满屏幕 */
	private boolean isHeightFillScreen;
	/** 窗口的宽度 */
	private int windowWidth;
	/** 窗口的高度 */
	private int windowHeight;
	private boolean isFinish = true;
	private boolean isActionMove = false;
	private boolean isScale = false;
	private Camera camera;
	
	private boolean isXBiggerThanY = false;
	private float rotateX = 0;
	private float rotateY = 0;
	private boolean isClicked = false;
	
	private View.OnClickListener onClickListener;
	
	private Handler scaleHandler = new Handler(){
		
		private Matrix matrix = new Matrix();
		private float s;
		private int count = 0;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			matrix.set(getImageMatrix());
			switch(msg.what){
			case 1:
				if(isFinish) {
					isFinish = false;
					count = 0;
					s = (float)Math.sqrt(Math.sqrt(zoomRate));
					beginScale(matrix, s);
					scaleHandler.sendEmptyMessage(2);
				}
				break;
			case 2:
				beginScale(matrix, s);
				if(count < 4){
					scaleHandler.sendEmptyMessage(2);
				} else {
					isFinish = true;
					if(!isActionMove && onClickListener != null && !isClicked){
						onClickListener.onClick(getRootView());
						isClicked = true;
					}
				}
				int zoomSize = (int)(zoomRate * vWidth);
				count += zoomSize;
				break;
			case 6:
				if(!isFinish){
					scaleHandler.sendEmptyMessage(6);
				} else {
					isFinish = false;
					count = 0;
					s = (float)Math.sqrt(Math.sqrt(1.0f / zoomRate));
					beginScale(matrix, s);
					scaleHandler.sendEmptyMessage(2);
				}
				break;
			}
		}
		
	};
	private Handler rotateHandler = new Handler(){
		
		private Matrix matrix = new Matrix();
		private float count = 0;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			matrix.set(getImageMatrix());
			switch(msg.what){
			case 1:
				count = 0;
				beginRotate(matrix, (isXBiggerThanY ? count : 0),
						(isXBiggerThanY ? 0 : count));
				rotateHandler.sendEmptyMessage(2);
				break;
			case 2:
				beginRotate(matrix, (isXBiggerThanY ? count : 0),
						(isXBiggerThanY ? 0 : count));
				if(count < rotateDegree){
					rotateHandler.sendEmptyMessage(2);
				} else {
					isFinish = true;
				}
				count += rotateDegree;
				break;
			case 3:
				beginRotate(matrix, (isXBiggerThanY ? count : 0),
						(isXBiggerThanY ? 0 : count));
				if(count > 0){
					rotateHandler.sendEmptyMessage(3);
				} else {
					isFinish = true;
					if(!isActionMove && onClickListener != null){
						onClickListener.onClick(getRootView());
					}
				}
				count -= rotateDegree;
				break;
			case 6:
				count = rotateDegree;
				beginRotate(matrix, (isXBiggerThanY ? count : 0),
						(isXBiggerThanY ? 0 : count));
				rotateHandler.sendEmptyMessage(3);
				break;
			}
		}
		
	};
	
	public RotateImageView(Context context) {
		super(context);
	}
	
	public RotateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	public RotateImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs);
	}
	
	/**
	 * 初始化控件的界面
	 * @param context
	 * @param attrs
	 */
	private void initView(Context context, AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotateImageView);
		isWidthFillScreen = a.getBoolean(
				R.styleable.RotateImageView_is_width_fill_screen, false);
		isHeightFillScreen = a.getBoolean(
				R.styleable.RotateImageView_is_height_fill_screen, false);
		if(isWidthFillScreen){
			windowWidth = PhoneUtils.getWindowWidth((Activity)context);
			vWidth = windowWidth;
		}
		if(isHeightFillScreen){
			windowHeight = PhoneUtils.getWindowHeight((Activity)context);
			vHeight = windowHeight;
		}
		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isFirst){
			init();
			isFirst = false;
		}
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
	}
	
	/**
	 * 初始化控件
	 * @create 2013年8月1日
	 * @modify 2013年8月1日
	 * @author foxchan@live.cn
	 */
	private void init(){
		camera = new Camera();
		if(!isWidthFillScreen){
			vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		}
		if(!isHeightFillScreen){
			vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
		}
		setScaleType(ScaleType.MATRIX);
		Drawable drawable = getDrawable();
		BitmapDrawable bd = (BitmapDrawable)drawable;
		bd.setAntiAlias(true);
		//计算缩小的角度
		rotateDegreeX = (int)Math.toDegrees(Math.atan2(rotateSize, vWidth));
		rotateDegreeY = (int)Math.toDegrees(Math.atan2(rotateSize, vHeight));
		imageWidth = bd.getIntrinsicWidth();
		imageHeight = bd.getIntrinsicHeight();
		//重置图片的大小以适应控件当前的大小
		if(isWidthFillScreen || isHeightFillScreen){
			Matrix matrix = new Matrix();
			resetImageView(matrix);
			setImageMatrix(matrix);
		}
	}
	
	public void resetImageView(Matrix matrix){
		if(imageWidth != vWidth || imageHeight != vHeight){
			float sx = (float)vWidth / imageWidth;
			float sy = (float)vHeight / imageHeight;
			matrix.postScale(sx, sy);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		if(!onAnimation) return true;
		float x = 0.0f;
		float y = 0.0f;
		switch(ev.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			x = ev.getX();
			y = ev.getY();
			rotateX = vWidth / 2 - x;
			rotateY = vHeight / 2 - y;
			isXBiggerThanY = Math.abs(rotateX) > Math.abs(rotateY) ? true : false;
			isScale = (x > vWidth / 3 && x < vWidth * 2 / 3 && y > vHeight / 3 && y < vHeight * 2 / 3);
			isActionMove = false;
			
			if(isScale){
				scaleHandler.sendEmptyMessage(1);
			} else {
				if(this.rotateX > 0){//左边
					rotateDegree = rotateDegreeX;
				} else if(this.rotateY > 0){//上边
					rotateDegree = rotateDegreeY;
				} else if(this.rotateX < 0){//右边
					rotateDegree = rotateDegreeX;
				} else if(this.rotateY < 0){//下边
					rotateDegree = rotateDegreeY;
				}
				rotateHandler.sendEmptyMessage(1);
			}
			isClicked = true;
			break;
		case MotionEvent.ACTION_MOVE:
			x = ev.getX();
			y = ev.getY();
			if(x > vWidth || y > vHeight || x < 0 || y < 0){
				isActionMove = true;
			} else {
				isActionMove = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(isScale){
				scaleHandler.sendEmptyMessage(6);
			} else {
				rotateHandler.sendEmptyMessage(6);
			}
			isClicked = false;
			break;
		}
		return true;
	}
	
	/**
	 * 进行缩放操作
	 * @create 2013年8月1日
	 * @modify 2013年8月1日
	 * @author foxchan@live.cn
	 */
	private synchronized void beginScale(Matrix matrix, float scale){
		int scaleX = (int)(vWidth * 0.5f);
		int scaleY = (int)(vHeight * 0.5f);
		matrix.postScale(scale, scale, scaleX, scaleY);
//		resetImageView(matrix);
		setImageMatrix(matrix);
	}
	
	/**
	 * 进行翻转操作
	 * @create 2013年8月1日
	 * @modify 2013年8月1日
	 * @author foxchan@live.cn
	 */
	private synchronized void beginRotate(Matrix matrix, float rotateX,
			float rotateY) {
		int scaleX = (int)(vWidth * 0.5f);
		int scaleY = (int)(vHeight * 0.5f);
		camera.save();
		camera.rotateX(this.rotateY > 0 ? rotateY : -rotateY);
		camera.rotateY(this.rotateX < 0 ? rotateX : -rotateX);
		camera.getMatrix(matrix);
		camera.restore();
		//控制中心点
		if(this.rotateX > 0 && rotateX != 0){//左边
			matrix.preTranslate(-vWidth, -scaleY);
			matrix.postTranslate(vWidth, scaleY);
		} else if(this.rotateY > 0 && rotateY != 0){//上边
			matrix.preTranslate(-scaleX, -vHeight);
			matrix.postTranslate(scaleX, vHeight);
		} else if(this.rotateX < 0 && rotateX != 0){//右边
			matrix.preTranslate(-0, -scaleY);
			matrix.postTranslate(0, scaleY);
		} else if(this.rotateY < 0 && rotateY != 0){//下边
			matrix.preTranslate(-scaleX, -0);
			matrix.postTranslate(scaleX, 0);
		}
		resetImageView(matrix);
		setImageMatrix(matrix);
	}

	/**
	 * 绑定点击事件的监听器
	 */
	public void setOnClickListener(View.OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public int getvWidth() {
		return vWidth;
	}

	public void setvWidth(int vWidth) {
		this.vWidth = vWidth;
	}

	public int getvHeight() {
		return vHeight;
	}

	public void setvHeight(int vHeight) {
		this.vHeight = vHeight;
	}

}