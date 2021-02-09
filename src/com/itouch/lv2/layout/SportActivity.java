package com.itouch.lv2.layout;


import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.itouch.common.AppVersionAdapter;
import com.itouch.common.InitHelper;
import com.itouch.common.MyPhoneStateListener;
import com.itouch.lv2.bean.Ball_v2;
import com.itouch.util.BallUtil;
import com.itouch.util.Constant;
import com.itouch.util.ZhuoyuUIUtil;
import com.itouch.view.R;
import com.itouch.view.lock.LockService;

public class SportActivity extends Activity {
	private static BallSurfaceView bsv;
	private Ball_v2 touchBall;
    private int xtype2,ytype2;
    private boolean bumpfishFlag = false;
    
    private long exitTime = 0;	
    private boolean firstLongClick = false;
	
    TelephonyManager manager ;
    
    /**读配置文件*/
 	private void init() {
 		
		//初始化共享属性
		InitHelper.instance(this, this);
 		
 		if (Constant.prefs.getBoolean("tranp", false)){ //背景设置|透明|桌面避纸
 			//透明,代码不支持从配置文件实现
 			Resources res = getResources();
 	        Drawable drawable = res.getDrawable(R.drawable.bkcolor);
 	        drawable.setAlpha(100);
 	        this.getWindow().setBackgroundDrawable(drawable); 	       
// 	       this.setTheme(Color.BLACK);

// 			  this.setBackgroundColor(Color.BLACK);
// 			  this.getBackground().setAlpha(100);
// 			this.setTheme(android.R.style.Theme_Translucent);
 		}else{
 			//不透明
 			this.setTheme(R.style.Theme);
 		}
 		
 	}


	
	
	
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(Constant.TAG, "----------------onCreate----------------");
			init();
			bsv = new BallSurfaceView(this);
			super.onCreate(savedInstanceState);
			
		     /**
		      * 因为activity设置了WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG类型(最前能获得home键响应),
		      * 接通电话时会跳出来,所以有控制
		      */
			//获取电话服务
		     manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		     // 手动注册对PhoneStateListener中的listen_call_state状态进行监听
		     manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);	 
		     
			/**第一次时触发*/
			bsv.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					System.out.println("--------------BallSurfaceView onTouch--------------"+v.getId());
					
					for (Ball_v2 ball : bsv.getBallList()) {
						ball.setPause(true);
						
						int x= (int)event.getX();
						int y=(int)event.getY(); //为鼠标不在图标中心所以-50
						float ballx = ball.getRunX()+ball.getBallSize();
						float bally = ball.getRunY()+ball.getBallSize();
						
						if (x >ball.getRunX() &&  x < ballx
								&& y >ball.getRunY() &&  y< bally){
							Log.e("System.out", "拖动-----------"+ball.getBallid());
							touchBall = ball;
							if (Constant.vibrate){
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
	  
		/**屏蔽home使用线程是实现，
		 * 不使用onAttachedToWindow方法,因为收到短信时屏蔽不了home,并且屏幕无响应,需要按“退出键”一次后才正常.
		 */
		mHandler.postDelayed(mDisableHomeKeyRunnable,200);
	}
	
	
	/**每移动一点都触发*/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		System.out.println("----------SportActivity onTouchEvent--------------------");
			//2球碰撞
			if (touchBall!= null){
				int x= (int)event.getX() - 60;
				int y =(int)event.getY() - 60; //为鼠标不在图标中心所以-50
				for (Ball_v2 ball2 : bsv.getBallList()) {
					if (ball2.getBallid() == touchBall.getBallid()){
						continue; //因为ballList包含自己,所以排除自己
					}
					if(BallUtil.isBall2ballBump(x, y, ball2.getRunX(), ball2.getRunY(), touchBall.getBallSize())){
						
						if (!bumpfishFlag){
							Log.e("System.out", "---2球碰撞结束-------");
//							Toast.makeText(this, "解除锁屏!", Toast.LENGTH_SHORT).show();
							bumpfishFlag =true;
								float part =  touchBall.getBallSize() /2 ;
								float tmpX1 = x- part;  //1圆心x
								float tmpY1 = y - part;//1圆心y
								float tmpX2 = ball2.getRunX() - part;
								float tmpY2 = ball2.getRunY() -part;	

								xtype2 =(int) (((Math.abs(tmpX2)+ Math.abs(tmpX1) ) /2) +100);						             
								ytype2 =(int) ((Math.abs(tmpY2)+ Math.abs(tmpY1) ) /2)  ;
								 
//								System.out.println("tmpX1:"+tmpX1+",tmpX2:"+tmpX2+",tmpY1:"+tmpY1+",tmpY2:"+tmpY2+",part:"+part);
//								System.out.println("xtype2:"+xtype2+",ytype2:"+ytype2);
								
//								bsv.setThreadRun(false); //停止球的线程
//								touchBall.setPause(true);
//								ball2.setPause(true);
								if(Constant.music){
									Constant.froth2bumpMusic.start(); //播放
								}
								if (Constant.vibrate){
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
				for (Ball_v2 ball : bsv.getBallList()) {
					
					int x= (int)event.getX();
					int y=(int)event.getY(); //为鼠标不在图标中心所以-50
					float ballx = ball.getRunX()+ball.getBallSize();
					float bally = ball.getRunY()+ball.getBallSize();
					if (x >ball.getRunX() &&  x < ballx
							&& y >ball.getRunY() &&  y< bally){
						if (Constant.vibrate){
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
//		System.out.println("-----onStart-------");
		super.onStart();
	}





	@Override
	protected void onRestart() {
//		System.out.println("-----onRestart-------");
		super.onRestart();
	}





	@Override
	protected void onResume() {
//		System.out.println("-----onResume-------");
		super.onResume();
	}



//
////TODO: 与全屏显示冲突
//	@Override
//    public void onAttachedToWindow() {
//		System.out.println("-------onAttachedToWindow---------");
////android底层系统把HOME键屏蔽了，但如果发现它是TYPE_KEYGUARD类的窗体，则不会过滤。所以把Activity修改成TYPE_KEYGUARD
////类就好了。 注意android 1.6 不支持屏蔽Home键
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG); 
//        super.onAttachedToWindow();
//		//发广播
////        Intent intent = new Intent(Constant.MAIN_ACTION);
////       this.sendBroadcast(intent); 
//        
//    }
 
	
	
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
            		AppVersionAdapter.stopMainActivity(this);//关闭            		
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
            	return true;  //屏蔽按键
			case KeyEvent.KEYCODE_ENDCALL:
				Log.e(Constant.TAG, "挂电话键");
				return true;  //屏蔽按键
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
	
		/**
		 * 屏幕home键
		 */
		public void disableHomeKey() {
//			this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		}

	
	
	
	
	
	
    
    public static BallSurfaceView getBsv() {
		return bsv;
	}

	public static void setBsv(BallSurfaceView bsv) {
		SportActivity.bsv = bsv;
	}

	public Ball_v2 getTouchBall() {
		return touchBall;
	}

	public void setTouchBall(Ball_v2 touchBall) {
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

	
}
