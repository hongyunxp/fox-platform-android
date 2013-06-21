package com.foxchan.foxutils.tool;

/**
 * 该类提供与数学计算相关的工具类
 * @author foxchan@live.cn
 * @version 1.0.0
 * @create 2013-6-21
 */
public class MathUtils {
	/**
	 * 计算两点间的距离
	 * @param x1	起始点的横坐标值
	 * @param y1	起始点的纵坐标值
	 * @param x2	目标点的横坐标值
	 * @param y2	目标点的纵坐标值
	 * @return		返回两点间的距离
	 */
	public static double distanceBetweenTowPoints(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.abs(x1-x2)*Math.abs(x1-x2)+Math.abs(y1-y2)*Math.abs(y1-y2));
	}

	/**
	 * 计算点a(x,y)的角度
	 * @param x	目标点的横坐标值
	 * @param y	目标点的纵坐标值
	 * @return	返回点a(x,y)的角度
	 */
	public static double pointTotoDegrees(double x,double y) {
		return Math.toDegrees(Math.atan2(x,y));
	}
	
	/**
	 * 检测点是否在指定的圆内
	 * @param sx	被检测点的横坐标值
	 * @param sy	被检测点的纵坐标值
	 * @param r		圆的半径
	 * @param x		圆的圆心横坐标值
	 * @param y		圆的圆心纵坐标值
	 * @return		如果被检测点在园中则返回true，否则返回false
	 */
	public static boolean isPointInCircle(float sx, float sy, float r, float x, float y) {
		return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
	}
}
