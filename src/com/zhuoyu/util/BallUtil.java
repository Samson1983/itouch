package com.zhuoyu.util;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.DisplayMetrics;

public class BallUtil {

	public static int[] getDisplayXY(Activity sa){
		// 获得屏幕尺寸
		   DisplayMetrics dm = new DisplayMetrics();
		   dm = sa.getApplicationContext().getResources().getDisplayMetrics();
//	       DisplayMetrics dm = new DisplayMetrics();
//	       sa.getWindowManager().getDefaultDisplay().getMetrics(dm);
	       int[] xyPoint = new int[2];
	       xyPoint[0] = dm.widthPixels;
	       xyPoint[1] = dm.heightPixels;
	       return xyPoint;
	}
	
	
	/**
	 * 检测2球是否碰撞
	 * (两个图心的坐标差 <=两个图的半径之和,两图碰撞,反之未碰撞)
	 * @param x1 坐标,球1
	 * @param y1 坐标,球1
	 * @param x2 坐标,球2
	 * @param y2 坐标,球2
	 * @param ballSize 球大小
	 * @return true 碰撞, false 未碰
	 */
	public static boolean isBall2ballBump(float x1,float y1,float x2,float y2,int ballSize){
		float part =  ballSize /2 ;
		float tmpX1 = x1- part;
		float tmpY1 = y1 - part;
		float tmpX2 = x2 - part;
		float tmpY2 = y2 -part;
		//(x1-x2)平方+(y1-y2)平方)的平方根
//				double result = Math.sqrt((x1-x2)+(y1-y2));
//				if (result <= circle2radius)
		 if(Math.sqrt((tmpX1-tmpX2)*(tmpX1-tmpX2) +              //不等于,并没有产生碰撞
				    (tmpY1-tmpY2)*(tmpY1-tmpY2)) <= 2*part ) //为去掉差的像表
		{
			 return true;
		} 
		
	return false;	
	}
	
	
	
	/**
	 * 球是否碰墙
	 * @param screenWidth
	 * @param screenHeight
	 * @param x 坐标
	 * @param y 坐标
	 * @param ballSize 球大小
	 * @return true 为碰墙,false 未碰墙
	 */
	public static boolean isBall2WallBump(int screenWidth,int screenHeight,int x,int y,int ballSize){
//			int HuaBan_x = screenWidth   - ballSize;
//			int HuaBan_y = screenHeight  - ballSize;
 		if (BallUtil.eqSize(screenWidth, x, ballSize) 
				|| BallUtil.eqSize(screenHeight, y, ballSize) ){
 			return true;
 		}
 		
		return false;	
	}
	
	/**
	 * 判断是否超出最大范围(边界检测，使球不会飞出边界)
	 * @param max 最大范围
	 * @param reduce 需要扣减的值
	 * @param currSize 当前大小
	 * @return true 超出最大范围  false未超 
	 */
	public static boolean eqSize(int max, int currSize,int reduce){
		int realitySize = max   - reduce;
		if (currSize <= 0  || currSize >= realitySize){
			return true;
		}
		return false;	
	}
	
	
	/**
	 * 简单振动
	 * @param activity 调用该方法的Activity实例
	 * @param milliseconds 振动的时长，单位是毫秒
	 */
	public static void Vibrate(final Context context, long milliseconds) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	/**
	 * 自定义振动
	 * @param activity 调用该方法的Activity实例
	 * @param pattern  自定义振动模式 。数组中数字的含义依次是[静止时长，振动时长，静止时长，振动时长。。。]时长的单位是毫秒
	 * @param isRepeat  是否反复振动，如果是true，反复振动，如果是false，只振动一次
	 */
	public static void Vibrate(final Activity activity, long[] pattern,
			boolean isRepeat) {
		Vibrator vib = (Vibrator) activity
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}
	
	
}
