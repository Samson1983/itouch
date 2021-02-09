package com.zhuoyu.view;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.zhuoyu.hv4.lock.TopFloatService;
import com.zhuoyu.util.Constant;
import com.zhuoyu.util.ZhuoyuUIUtil;

public class MainActivity extends Activity {
	
	public static  int ballSpeed;
    public static  boolean vibrate ;
    public static boolean fillScreen;
    public static boolean isTranp;
    public static Drawable bmpdwe;
	public static MainActivity ma;
    
    
    /**读配置文件*/
	private void init() {
//		if (Constant.prefs == null){
  		 	/**不会为空*/
			Constant.prefs = PreferenceManager.getDefaultSharedPreferences(this) ;

//			System.out.println("----------Constant.prefs:"+Constant.prefs);
			// 获得屏幕尺寸
			DisplayMetrics dm = new DisplayMetrics();
			dm = this.getApplicationContext().getResources().getDisplayMetrics();
			Constant.screenWidth = dm.widthPixels;
			Constant.screenHeight = dm.heightPixels;		
			
			Constant.froth2bumpMusic = MediaPlayer.create(this, R.raw.froth2bump);
			if (Constant.musicValue == 0.0f){
				Constant.musicValue = (float) (Constant.prefs.getInt("musicValue", 3) * 0.1); //初始化读取,标配0.3f
			}
			System.out.println("0000--------"+Constant.musicValue);
			Constant.froth2bumpMusic.setVolume(Constant.musicValue, Constant.musicValue); //根据进度条设置音量
			
			Constant.imgList = ZhuoyuUIUtil.initImageList(this); //读取图片	

 	       //获取图片
    	   Constant.ballBitmap1 = ZhuoyuUIUtil.setImageResource(this,null,1, R.drawable.ball_man); 
    	   Constant.ballBitmap2 = ZhuoyuUIUtil.setImageResource(this,null,2, R.drawable.ball_wife_son);    	   
//		}
		
		if (Constant.prefs.getBoolean("startFroth", false)){ //是否使用本程序
			//TODO: 不需要
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE); //去title
		
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


		fillScreen = Constant.prefs.getBoolean("fillScreen", false);
		if (fillScreen){ //是否全屏
			this.setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
			// 下两句为设置全屏
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
		}
		
		if (Constant.prefs.getBoolean("tranp", false)){ //背景设置|透明|桌面避纸
			//代码不支持从配置文件实现
//			this.setTheme(android.R.style.Theme_Translucent);
			isTranp=true;
		}else{
			// 获取壁纸管理器
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
			// 获取当前壁纸
			Drawable wallpaperDrawable = wallpaperManager.getDrawable();
			// 将Drawable,转成Bitmap
			Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();

			// 需要详细说明一下，mScreenCount、getCurrentWorkspaceScreen()、mScreenWidth、mScreenHeight分别
			//对应于Launcher中的桌面屏幕总数、当前屏幕下标、屏幕宽度、屏幕高度.等下拿Demo的哥们稍微要注意一下
			float step = 0;
			// 计算出屏幕的偏移量
			step = (bm.getWidth() - 480) / (7 - 1);
			// 截取相应屏幕的Bitmap
			Bitmap pbm = Bitmap.createBitmap(bm, (int) (5 * step), 0,Constant.screenWidth,Constant.screenHeight);
			// 设置 背景
			bmpdwe = new BitmapDrawable(pbm);			
			
			isTranp=false;
		}
		
		
		ballSpeed = Constant.prefs.getInt("frothSpeed", 3);//泡泡的速度
		Log.e(Constant.TAG, "init-------------ballSpeed:"+ballSpeed);
		vibrate = Constant.prefs.getBoolean("vibrate", false); //振动
		
		
		
	
	}	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		Handler handler = new Handler();
//		handler.post(new Runnable() {
//			public void run() {
//				//初始化
//				init();
//			};
//		});
		
//		Intent intent1= new Intent(Intent.ACTION_MAIN); 
//		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识   
//		intent1.addCategory(Intent.CATEGORY_HOME);
//		this.startActivity(intent1);   
//		Intent intent2= new Intent(Intent.ACTION_MAIN); 
//		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识   
//		intent2.addCategory(Intent.CATEGORY_HOME);
//		this.startActivity(intent2); 
		
		init();
		startService(new Intent(this,TopFloatService.class));
		ma = this;
		
//		this.finish(); //关闭自己,放到TopFrame.remove关闭
	}


	
	
}