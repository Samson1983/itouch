package com.itouch.util;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

public class Constant {

	/**静态变量不会因activity finsh与释放*/
	public static String TAG = "Zhuoyu";
	public static String MAIN_ACTION = "LockMain";
	public static SharedPreferences prefs ;
	public static MediaPlayer froth2bumpMusic; //音效
	public static float musicValue = 0.0f; //音效大小
	public static int ballSpeed = 0; //速度大小
	public static  boolean music =true ; //音效
	public static  boolean vibrate ; //振动
	public static boolean fillScreen; //全屏
	public static int screenWidth = 0;
	public static int screenHeight = 0;	
	public static int statusBarHeight = 0; //顶部状态栏高度
	public static ArrayList<Bitmap> imgList;
	public static Bitmap ballBitmap1;// 球的图片
	public static Bitmap ballBitmap2;// 球的图片
	public static boolean autoSvrFlag = false;  //启动程序服务
	public static boolean unLock = false; //积分墙解锁
	public static int pointTotal ; //积分总额
	public static SharedPreferences  share; //保存订购标记
	/**静态常量*/
	public final static int SALE_PRICE = 40; //消费价格
	public final static int SDK_VERSION_4 = 14;  
	public final static int SDK_VERSION_2 = 13;  	
	
}
