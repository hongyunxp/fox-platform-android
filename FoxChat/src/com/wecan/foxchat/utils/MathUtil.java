package com.wecan.foxchat.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 关于数学运算的辅助类
 * @author jiladeyouxiang@qq.com
 * @version 1.0.0
 * @create 2012-12-11
 */
public class MathUtil {
	
	/**
	 * 将dip为单位的值转化为对应的px值
	 * @param context
	 * @param dip		dip的值
	 * @return			返回对应的px值
	 */
	public static float dipToPx(Context context, int dip){
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());
		return px;
	}
	
	/**
	 * 将dip为单位的值转化为对应的px值
	 * @param context
	 * @param dip		dip的值
	 * @return			返回对应的px值
	 */
	public static int dipToPxInt(Context context, int dip){
		return (int)dipToPx(context, dip);
	}
	
	/**
	 * 将sp为单位的值转化为对应的px值
	 * @param context
	 * @param sp		sp的值
	 * @return			返回对应的px值
	 */
	public static float spToPx(Context context, int sp){
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
				r.getDisplayMetrics());
		return px;
	}

}
