package com.foxchan.metroui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.foxchan.foxanimation.rotate.Rotate3dAnimation;
import com.foxchan.metroui.widget.RotateImageView;

public class AppStart extends Activity {
	
	private RotateImageView ivFront;
	private RotateImageView ivBack;
	private RelativeLayout rlContainer;
	private Button btnShowFront;
	private Button btnShowBack;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_start);
		rlContainer = (RelativeLayout)findViewById(R.id.metro_imageview_container);
		ivFront = (RotateImageView)findViewById(R.id.metro_imageview_front);
		ivBack = (RotateImageView)findViewById(R.id.metro_imageview_back);
		btnShowFront = (Button)findViewById(R.id.show_front);
		btnShowBack = (Button)findViewById(R.id.show_back);
		
		//因为要旋转，所以需要缓存视图的
		rlContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		btnShowFront.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handler.post(frontRunnable);
			}
		});
		btnShowBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handler.post(backRunnable);
			}
		});
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
		applyRotation(-1, 0, -90, ivFront, ivBack, rlContainer, Rotate3dAnimation.Y);
	}
	
	/**
	 * 显示反面
	 */
	private void showBack(){
		applyRotation(1, 0, 90, ivFront, ivBack, rlContainer, Rotate3dAnimation.Y);
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
			largerAnimation.setDuration(500);
			smallerAnimation = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f,
					Animation.RELATIVE_TO_SELF, 0.2f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			smallerAnimation.setDuration(500);
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
			rotation.setDuration(500);
			rotation.setFillAfter(true);
			// 动画插入器 减速
			rotation.setInterpolator(new DecelerateInterpolator());

			vgContainer.startAnimation(animationSet);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_start, menu);
		return true;
	}

}
