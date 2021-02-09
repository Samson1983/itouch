package com.zhuoyu.view;


import java.io.IOException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhuoyu.bean.Ball;
import com.zhuoyu.util.BallUtil;
import com.zhuoyu.util.Constant;
import com.zhuoyu.util.ZhuoyuUIUtil;
import com.zhuoyu.view.lock.LockService;

public class SportActivity extends Activity {
	private static BallSurfaceView bsv;
	private Ball touchBall;
    private int xtype2,ytype2;
    private boolean bumpfishFlag = false;
    private  int ballSpeed ;
    private  boolean music = true;
    private  boolean vibrate ;
    private boolean fillScreen;
    
    private long exitTime = 0;	
    private boolean firstLongClick = false;
	
	
    /**读配置文件*/
	private void init() {
//		if (Constant.prefs == null){
			Constant.prefs =PreferenceManager.getDefaultSharedPreferences(this) ;

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
//			
//			try {
//				Constant.froth2bumpMusic.prepare();  //准备
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			Constant.imgList = ZhuoyuUIUtil.initImageList(this); //读取图片	

 	       //获取图片
    	   Constant.ballBitmap1 = ZhuoyuUIUtil.setImageResource(this,null,1, R.drawable.ball_man); 
    	   Constant.ballBitmap2 = ZhuoyuUIUtil.setImageResource(this,null,2, R.drawable.ball_wife_son);    	   
//		}
		
		if (Constant.prefs.getBoolean("startFroth", false)){ //是否使用本程序
			//TODO: 不需要
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE); //去title
		fillScreen = Constant.prefs.getBoolean("fillScreen", false);
		if (fillScreen){ //是否全屏
			this.setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
			// 下两句为设置全屏
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
		}
		
		if (Constant.prefs.getBoolean("Tranp", false)){ //背景设置|透明|桌面避纸
			//代码不支持从配置文件实现
//			this.setTheme(android.R.style.Theme_Translucent);
		}else{
			this.setTheme(R.style.Theme);
		}
		
		
		ballSpeed = Constant.prefs.getInt("frothSpeed", 5);//泡泡的速度
//		music = Constant.prefs.getBoolean("musice", false); //音效
		vibrate = Constant.prefs.getBoolean("vibrate", false); //振动
		
		
		
	
	}

	
	
	
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(Constant.TAG, "----------------onCreate----------------");
			init();
			bsv = new BallSurfaceView(this);
			super.onCreate(savedInstanceState);
			
			/**第一次时触发*/
			bsv.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					System.out.println("--------------BallSurfaceView onTouch--------------"+v.getId());
					
					for (Ball ball : bsv.getBallList()) {
						ball.setPause(true);
						
						int x= (int)event.getX();
						int y=(int)event.getY(); //为鼠标不在图标中心所以-50
						float ballx = ball.getRunX()+ball.getBallSize();
						float bally = ball.getRunY()+ball.getBallSize();
						
						if (x >ball.getRunX() &&  x < ballx
								&& y >ball.getRunY() &&  y< bally){
							Log.e("System.out", "拖动-----------"+ball.getBallid());
							touchBall = ball;
							if (vibrate){
								BallUtil.Vibrate(SportActivity.this, 20L); //需要权限						
							}
							
						}
					}
					
					return false;
				}
			});
				
			
		
	  ZhuoyuUIUtil.startServiceAll(this,true);//启动所有服务
      
	  setContentView(bsv); //启动BallSurfaceView
	  
//		//禁止Home键
	  SportActivity.this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	  
		//在onCreate中调用：
//		mHandler.postDelayed(mDisableHomeKeyRunnable,200);
	}
	
	
	/**每移动一点都触发*/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		System.out.println("----------SportActivity onTouchEvent--------------------");
			//2球碰撞
			if (touchBall!= null){
				int x= (int)event.getX() - 60;
				int y =(int)event.getY() - 60; //为鼠标不在图标中心所以-50
				for (Ball ball2 : bsv.getBallList()) {
					if (ball2.getBallid() == touchBall.getBallid()){
						continue; //因为ballList包含自己,所以排除自己
					}
					if(BallUtil.isBall2ballBump(x, y, ball2.getRunX(), ball2.getRunY(), touchBall.getBallSize())){
						
						if (!bumpfishFlag){
							Log.e("System.out", "---2球碰撞结束-------");
//							Toast.makeText(this, "解除锁屏!", Toast.LENGTH_SHORT).show();
							bumpfishFlag =true;
							LockService.setFirstLockFlag(false);// 释放锁屏标记
								float part =  touchBall.getBallSize() /2 ;
								float tmpX1 = x- part;  //1圆心x
								float tmpY1 = y - part;//1圆心y
								float tmpX2 = ball2.getRunX() - part;
								float tmpY2 = ball2.getRunY() -part;	

								xtype2 =(int) (((Math.abs(tmpX2)+ Math.abs(tmpX1) ) /2) +100);						             
								ytype2 =(int) ((Math.abs(tmpY2)+ Math.abs(tmpY1) ) /2)  ;
								 
								System.out.println("tmpX1:"+tmpX1+",tmpX2:"+tmpX2+",tmpY1:"+tmpY1+",tmpY2:"+tmpY2+",part:"+part);
								System.out.println("xtype2:"+xtype2+",ytype2:"+ytype2);
								
//								bsv.setThreadRun(false); //停止球的线程
//								touchBall.setPause(true);
//								ball2.setPause(true);
								if(music){
									Constant.froth2bumpMusic.start(); //播放
								}
								if (vibrate){
									BallUtil.Vibrate(SportActivity.this, 100L);	
								}
								
						}	
					}
				}


				//判断是否碰壁
	 		if (BallUtil.isBall2WallBump(Constant.screenWidth, Constant.screenHeight, x, y, touchBall.getBallSize())){
	 			Log.e("System.out", "---碰壁-------");
 				
	 		}else{
	 			//未碰到
	 			touchBall.setRunX(x);
				touchBall.setRunY(y);
//				bsv.onDraw2(null);
	 		}
	 		
	 		
			}
			
			if(event.getAction()==MotionEvent.ACTION_UP){ //松手恢复
				for (Ball ball : bsv.getBallList()) {
					
					int x= (int)event.getX();
					int y=(int)event.getY(); //为鼠标不在图标中心所以-50
					float ballx = ball.getRunX()+ball.getBallSize();
					float bally = ball.getRunY()+ball.getBallSize();
					if (x >ball.getRunX() &&  x < ballx
							&& y >ball.getRunY() &&  y< bally){
						if (vibrate){
							BallUtil.Vibrate(SportActivity.this, 10L); //需要权限						
						}
						
					}					
					
					ball.setPause(false);
				}
				
				touchBall = null; //使用完释放
			}

		
	  return super.onTouchEvent(event);
	}

 
	@Override
	protected void onStart() {
		System.out.println("-----onStart-------");
		super.onStart();
	}





	@Override
	protected void onRestart() {
		System.out.println("-----onRestart-------");
		super.onRestart();
	}





	@Override
	protected void onResume() {
		System.out.println("-----onResume-------");
		super.onResume();
	}




//TODO: 与全屏显示冲突
	@Override
    public void onAttachedToWindow() {
		System.out.println("-------onAttachedToWindow---------");
//android底层系统把HOME键屏蔽了，但如果发现它是TYPE_KEYGUARD类的窗体，则不会过滤。所以把Activity修改成TYPE_KEYGUARD
//类就好了。 注意android 1.6 不支持屏蔽Home键
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG); 
        super.onAttachedToWindow();
		//发广播
//        Intent intent = new Intent(Constant.MAIN_ACTION);
//       this.sendBroadcast(intent); 
        
    }
 
	
	
    @Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
  	  Log.e(Constant.TAG, "onKeyUp:"+keyCode+"");
      switch(keyCode) {            
      case KeyEvent.KEYCODE_BACK:
			Log.e(Constant.TAG, "松开菜单键");
			firstLongClick = false;
			break;
      }
		return false;
	}





	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
    	  Log.e(Constant.TAG, "onKeyDown:"+keyCode+"");
            switch(keyCode) {            
            case KeyEvent.KEYCODE_BACK:
            	Log.e(Constant.TAG, "菜单键,丫的，看你往哪里跑!");
            	if (!firstLongClick){
            		firstLongClick = true;
            		exitTime = System.currentTimeMillis();
            	}
            	System.out.println("long:"+(System.currentTimeMillis() - exitTime));
            	
            	if ((System.currentTimeMillis() - exitTime) > 5000) {
            		Toast.makeText(getApplicationContext(), "紧急解锁！",Toast.LENGTH_SHORT).show();
            		LockService.setFirstLockFlag(false);// 释放锁屏标记
                		finish();
            		}

            	return true;  //屏蔽按键           
            case KeyEvent.KEYCODE_HOME:
                    Log.e(Constant.TAG, "丫的，看你往哪里跑!");
                    return true;  //屏蔽按键           
            case KeyEvent.KEYCODE_CALL:
            	Log.e(Constant.TAG, "拔电话键");            	
            	return true;  //屏蔽按键
            case KeyEvent.KEYCODE_POWER:
            	Log.e(Constant.TAG, "电源键按下");
            		break;
			case KeyEvent.KEYCODE_ENDCALL:
				Log.e(Constant.TAG, "挂电话键");
				break;
            }
            return false;
            
            
    } 

 
 /**禁止通知栏下拉*/   
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);
        try {

            Object service = getSystemService("statusbar");            
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
            Method test = statusbarManager.getMethod("collapse");
            test.invoke(service);
        	
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
    
    
	
	
	Handler mHandler = new Handler();
		Runnable mDisableHomeKeyRunnable = new Runnable() {
	
			@Override
			public void run() {
				disableHomeKey();
			}
		};
	
		public void disableHomeKey() {
			this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		}

	
	
	
	
	
	
    
    public static BallSurfaceView getBsv() {
		return bsv;
	}

	public static void setBsv(BallSurfaceView bsv) {
		SportActivity.bsv = bsv;
	}

	public Ball getTouchBall() {
		return touchBall;
	}

	public void setTouchBall(Ball touchBall) {
		this.touchBall = touchBall;
	}


	public int getXtype2() {
		return xtype2;
	}

	public void setXtype2(int xtype2) {
		this.xtype2 = xtype2;
	}

	public int getYtype2() {
		return ytype2;
	}

	public void setYtype2(int ytype2) {
		this.ytype2 = ytype2;
	}

	public boolean isBumpfishFlag() {
		return bumpfishFlag;
	}

	public void setBumpfishFlag(boolean bumpfishFlag) {
		this.bumpfishFlag = bumpfishFlag;
	}

	public int getBallSpeed() {
		return ballSpeed;
	}

	public void setBallSpeed(int ballSpeed) {
		this.ballSpeed = ballSpeed;
	}

	public boolean isMusic() {
		return music;
	}

	public void setMusic(boolean music) {
		this.music = music;
	}

	public boolean isVibrate() {
		return vibrate;
	}

	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

	public boolean isFillScreen() {
		return fillScreen;
	}


	public void setFillScreen(boolean fillScreen) {
		this.fillScreen = fillScreen;
	}
    
	
	
}
