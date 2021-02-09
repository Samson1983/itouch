package com.itouch.hv4.layout;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.itouch.common.InitHelper;
import com.itouch.util.Constant;
import com.itouch.util.ZhuoyuUIUtil;
import com.itouch.view.R;
import com.itouch.view.R.drawable;
import com.itouch.view.R.raw;

public class MainActivity extends Activity {
	
    public static boolean isTranp;
    public static Drawable bmpdwe;
	public static MainActivity ma;
    
    
    /**读配置文件*/
	private void init() {
 
		//初始化共享属性
		InitHelper.instance(this, this);
		
		if (Constant.prefs.getBoolean("tranp", false)){ //背景设置|透明|桌面避纸
			//透明,代码不支持从配置文件实现
//			this.setTheme(android.R.style.Theme_Translucent);
			isTranp=true;
//			bmpdwe = new BitmapDrawable(shot());	
			
		}else{
			//不透明
			Drawable wallpaperDrawable = null;
			try{
				// 获取壁纸管理器
				WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
				// 获取当前壁纸
				wallpaperDrawable = wallpaperManager.getDrawable();
				// 将Drawable,转成Bitmap
				Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
				
				// 需要详细说明一下，mScreenCount、getCurrentWorkspaceScreen()、mScreenWidth、mScreenHeight分别
				//对应于Launcher中的桌面屏幕总数、当前屏幕下标、屏幕宽度、屏幕高度.等下拿Demo的哥们稍微要注意一下
				float step = 0;
				// 计算出屏幕的偏移量
				step = (bm.getWidth() - 480) / (7 - 1); // 解决背景图片,小于480时的异常;//0除以任何数都出出异常
				// 截取相应屏幕的Bitmap
				Bitmap pbm = Bitmap.createBitmap(bm, (int) (5 * step), 0,Constant.screenWidth,Constant.screenHeight);
				// 设置 背景
				bmpdwe = new BitmapDrawable(pbm);			
				
				isTranp=false;
			}catch (Exception e) {
				bmpdwe = wallpaperDrawable;	
			}
		}
		
	}	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("Constant.Tag", "--------onReceive,"+Constant.autoSvrFlag);
		if (Constant.autoSvrFlag){ //是否启动锁屏应用
			//初始化
			init();
			
			int sysVersion = Integer.parseInt(VERSION.SDK);
			
			Handler handler = new Handler();
			handler.post(new Runnable() {
				/**
				 * 1. 因为创建这个比较耗时
				 * 2. getStatusBarHeight();获取高度需要在Activity的onCreate之后才可以获取到
				 */
				public void run() {
					//单例 
//					startService(new Intent(this,TopFloatService.class));
					 new  TopFloatService(MainActivity.this);					
				};
			});			

			 
			
			ma = this;
//		this.finish(); //关闭自己,放到TopFrame.remove关闭
		}
		
	}



	/**
	 *  截屏方法
	 *  return 
	 */
	private Bitmap shot() {       
			View view = getWindow().getDecorView();  
            Display display = this.getWindowManager().getDefaultDisplay();       
            view.layout(0, 0, display.getWidth(), display.getHeight()); 
            view.setDrawingCacheEnabled(true);//允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap  
            Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());  
     return bmp;  
    }	
	
}