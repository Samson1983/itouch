package com.zhuoyu.hv4.layout;


import com.zhuoyu.view.MainActivity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;


public class TopFrame {

	RelativeLayout mainLayout;
	WindowManager wm = null;
	WindowManager.LayoutParams wmParams = null;
	private final static int FLAG_APKTOOL_VALUE = 1280;
	private Context context;

	SportLayout layout ;

	public TopFrame(Context context) {
		super();
		this.context = context;
	}



	public void createView() {
		// 获取WindowManager
		wm = (WindowManager) context.getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		// 设置LayoutParams(全局变量）相关参数
		wmParams = new WindowManager.LayoutParams();
		wmParams.type |= WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
				| WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		//全屏设置,只支持4.0以上版本【改为利用MainActivity实现】
//		wmParams.flags |= LayoutParams.FLAG_FULLSCREEN|LayoutParams.FLAG_NOT_TOUCH_MODAL;
//		wmParams.flags |= FLAG_APKTOOL_VALUE; 
		
		// * |WindowManager. LayoutParams
		// * .FLAG_NOT_FOCUSABLE
		// */// 不接受任何按键事件
		// 以屏幕左上角为原点，设置x、y初始值

		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.FILL_PARENT;
		wmParams.height = WindowManager.LayoutParams.FILL_PARENT;
		wmParams.format = PixelFormat.RGBA_8888;

		//非Activity中，用LayoutInflater 获取XML布局文件
		LayoutInflater inflater = LayoutInflater.from(context);
//		layout  = inflater.inflate(R.layout.activity_main, null);
		layout = new SportLayout(context);
		layout.setFt(this);
		
		
	}

	
	public  void addView() {
		wm.addView(layout, wmParams);		
	}
	
	public  void  remove() {
		if (layout != null){
			MainActivity.ma.finish(); //关闭MainActivity,现在才关是为了取状态栏的高度getStatusBarHeight();、实现全屏init()真机可以（模拟器：4.1生效 ; 2.2不生成）
			layout.threadFinish();
			wm.removeView(layout);
		}
	}
}
