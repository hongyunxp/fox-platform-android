package com.foxchan.metroui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.foxchan.foxanimation.rotate.Rotate3dAnimation;

/**
 * 磁贴控件
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年8月2日
 */
public class CubeView extends RelativeLayout {
	
	/** 正面的图片控件 */
	private RotateImageView ivFront;
	/** 反面的图片控件 */
	private RotateImageView ivBack;
	/** 控件的左间距 */
	private int marginLeft;
	/** 控件的上间距 */
	private int marginTop;
	/** 是否是第一次加载控件的标志 */
	private boolean isFirst = true;
	/** 磁贴被选中执行的监听器 */
	private OnCubeStartListener onCubeStartListener;
	
	private Handler handler = new Handler();
	private Runnable frontRunnable = new Runnable() {
		
		@Override
		public void run() {
			showFront();
		}
	};
	private Runnable backRunnable = new Runnable() {
		
		@Override
		public void run() {
			showBack();
		}
	};
	private Runnable startCubeRunnable = new Runnable() {
		
		@Override
		public void run() {
			onCubeStartListener.onCubeStart();
			handler.post(frontRunnable);
		}
	};
	
	public CubeView(Context context) {
		super(context);
	}

	public CubeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CubeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * 初始化控件
	 */
	private void init(){
		ivFront = (RotateImageView)getChildAt(0);
		ivBack = (RotateImageView)getChildAt(1);
		//因为要旋转，所以需要缓存视图的
		setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		ivFront.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.post(backRunnable);
			}
		});
		ivBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handler.post(frontRunnable);
			}
		});
	}
	
	/**
	 * 显示正面
	 */
	private void showFront(){
		applyRotation(-1, 0, -90, ivFront, ivBack, this, Rotate3dAnimation.Y);
	}
	
	/**
	 * 显示反面
	 */
	private void showBack(){
		applyRotation(1, 0, 90, ivFront, ivBack, this, Rotate3dAnimation.Y);
	}
	
	/**
	 * 应用旋转动画
	 * @param position	
	 * @param start
	 * @param end
	 * @param ivFront
	 * @param ivBack
	 * @param container
	 * @param axis
	 */
	private void applyRotation(int position, int start, int end,
			ImageView ivFront, ImageView ivBack, ViewGroup container,
			String axis) {
		final float centerX = container.getWidth() / 2.0f;
		final float centerY = container.getHeight() / 2.0f;
		
		final Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(
				start, end, centerX, centerY, 310.0f, true, Rotate3dAnimation.Y);
		rotate3dAnimation.setDuration(100);
		rotate3dAnimation.setFillAfter(true);
		//动画插入器，加速
		rotate3dAnimation.setInterpolator(new AccelerateInterpolator());
		rotate3dAnimation.setAnimationListener(new DisplayNextView(position,
				ivFront, ivBack, container, axis));
		
		container.startAnimation(rotate3dAnimation);
	}
	
	/**
	 * 
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2013年7月31日
	 */
	private final class DisplayNextView implements Animation.AnimationListener{
		
		private int mPosition;
		private View vFront;
		private View vBack;
		private ViewGroup vgContainer;
		private String axis;

		public DisplayNextView(int mPosition, View vFront, View vBack,
				ViewGroup vgContainer, String axis) {
			this.mPosition = mPosition;
			this.vFront = vFront;
			this.vBack = vBack;
			this.vgContainer = vgContainer;
			this.axis = axis;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			vgContainer.post(new ViewSwaper(mPosition, vFront, vBack, vgContainer, axis));
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}
		
	}
	
	/**
	 * 交换两个界面的线程
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2013年7月31日
	 */
	private final class ViewSwaper implements Runnable{
		
		private int mPosition;
		private View vFront;
		private View vBack;
		private ViewGroup vgContainer;
		private String axis;
		private ScaleAnimation largerAnimation;
		private ScaleAnimation smallerAnimation;

		public ViewSwaper(int mPosition, View vFront, View vBack,
				ViewGroup vgContainer, String axis) {
			this.mPosition = mPosition;
			this.vFront = vFront;
			this.vBack = vBack;
			this.vgContainer = vgContainer;
			this.axis = axis;
			largerAnimation = new ScaleAnimation(0.4f, 1.0f, 0.4f, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.2f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			smallerAnimation = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f,
					Animation.RELATIVE_TO_SELF, 0.2f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}

		@Override
		public void run() {
			final float centerX = vgContainer.getWidth() / 2.0f;
			final float centerY = vgContainer.getHeight() / 2.0f;
			Rotate3dAnimation rotation = null;
			AnimationSet animationSet = null;
			if(mPosition > -1){
				vFront.setVisibility(View.GONE);
				vBack.setVisibility(View.VISIBLE);
				vBack.requestFocus();
				rotation = new Rotate3dAnimation(-90, 0, centerX, centerY,
						310.0f, false, axis);
				animationSet = new AnimationSet(true);
				animationSet.addAnimation(largerAnimation);
				animationSet.addAnimation(rotation);
			} else {
				vFront.setVisibility(View.VISIBLE);
				vBack.setVisibility(View.GONE);
				vFront.requestFocus();
				rotation = new Rotate3dAnimation(90, 0, centerX, centerY,
						310.0f, false, axis);
				animationSet = new AnimationSet(true);
				animationSet.addAnimation(rotation);
			}
			rotation.setFillAfter(true);
			// 动画插入器 减速
			rotation.setInterpolator(new DecelerateInterpolator());
			rotation.setAnimationListener(new Animation.AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							getLayoutParams());
					if(mPosition > -1){//显示反面
						params.setMargins(0, 0, params.rightMargin, params.bottomMargin);
					} else {//显示正面
						params.setMargins(marginLeft, marginTop, params.rightMargin, params.bottomMargin);
					}
					setLayoutParams(params);
					bringToFront();
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					if(onCubeStartListener != null && mPosition > -1){
						handler.postDelayed(startCubeRunnable, 200);
					}
				}
			});
			animationSet.setDuration(300);
			vgContainer.startAnimation(animationSet);
		}
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(isFirst){
			init();
			marginLeft = l;
			marginTop = t;
			isFirst = false;
		}
		super.onLayout(changed, l, t, r, b);
	}

	/**
	 * 绑定磁贴被选中执行的监听器
	 * @param onCubeStartListener	磁贴被选中执行的监听器
	 */
	public void setOnCubeStartListener(OnCubeStartListener onCubeStartListener) {
		this.onCubeStartListener = onCubeStartListener;
	}

	/**
	 * 磁贴被选中执行的监听器
	 * @author foxchan@live.cn
	 * @version 1.0.0
	 * @create 2013年8月2日
	 */
	public interface OnCubeStartListener {
		
		/**
		 * 执行被选中执行后需要执行的方法
		 */
		void onCubeStart();
		
	}

}
