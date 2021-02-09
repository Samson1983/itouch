package com.zhuoyu.util;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

public class Constant {

	public static String TAG = "Zhuoyu";
	public static String MAIN_ACTION = "LockMain";
	
	/**静态变量不会因activity finsh与释放*/
	public static SharedPreferences prefs ;
	public static MediaPlayer froth2bumpMusic; //音效
	public static float musicValue = 0.0f; //音效大小
	public static int screenWidth = 0;
	public static int screenHeight = 0;	
	public static int statusBarHeight = 0; //顶部状态栏高度
	public static ArrayList<Bitmap> imgList;
	public static Bitmap ballBitmap1;// 球的图片
	public static Bitmap ballBitmap2;// 球的图片
	/**
	 * 启动程序服务
	 */
	public static boolean autoSvrFlag = false; 
	
	
	public static boolean unLock = false; //积分墙解锁
	public static int pointTotal ; //积分总额
	public final static int SALE_PRICE = 40; //消费价格
	
	public static SharedPreferences  share; //保存订购标记
}
