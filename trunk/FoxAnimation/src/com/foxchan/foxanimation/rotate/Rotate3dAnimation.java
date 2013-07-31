package com.foxchan.foxanimation.rotate;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.graphics.Camera;
import android.graphics.Matrix;

/**
 * 翻转的动画
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013年7月31日
 */
public class Rotate3dAnimation extends Animation {

	/** 旋转的轴：X轴 */
	public static final String X = "X";
	/** 旋转的轴：Y轴 */
	public static final String Y = "Y";

	/** 开始旋转的角度 **/
	private final float mFromDegrees;
	/** 旋转结束的角度 **/
	private final float mToDegrees;
	/** 图片中心X坐标 **/
	private final float mCenterX;
	/** 图片中心Y坐标 **/
	private final float mCenterY;
	/** Z轴上的距离 **/
	private final float mDepthZ;
	/** 缩放效果 **/
	private final boolean mReverse;
	/** 视角 **/
	private Camera mCamera;
	/** 围绕那个柱旋转 **/
	private String rotate;

	public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX,
			float centerY, float depthZ, boolean reverse, String rotate) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depthZ;
		mReverse = reverse;
		this.rotate = rotate;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float fromDegrees = mFromDegrees;
		// 生成中间角度
		float degrees = fromDegrees
				+ ((mToDegrees - fromDegrees) * interpolatedTime);

		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;

		final Matrix matrix = t.getMatrix();

		camera.save();
		// x轴 正值向右
		// y轴 正值向上
		// z轴 正值缩小
		if (mReverse) {
			// 由大变小
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			// 由小变大
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}

		// 围绕Y轴进行旋转
		if (X.equals(rotate)) {
			camera.rotateX(degrees);
		} else if (Y.equals(rotate)) {
			camera.rotateY(degrees);
		}

		camera.getMatrix(matrix);
		camera.restore();

		// 旋转矩阵运算会以图像的原点为变换中心点，如果想要以图片中心点为旋转的中心点
		// 就需要首先把整个View的中心移动到原点，矩阵运算完成后再把View移回原来的位置
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}
