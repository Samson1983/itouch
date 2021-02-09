package com.zhuoyu.hv4.layout;


import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Toast;

import com.zhuoyu.hv4.bean.Ball;
import com.zhuoyu.util.BallUtil;
import com.zhuoyu.util.Constant;
import com.zhuoyu.util.ZhuoyuUIUtil;
import com.zhuoyu.view.MainActivity;
import com.zhuoyu.view.lock.LockService;

public class SportLayout extends View {
	
	private Ball touchBall;
    private int xtype2,ytype2;
    public  int bumpfishFlag = -1;
    public  int imgIndex = 0;
	public boolean heartFishFlag = false;
    private  int ballSpeed ;
    private  boolean music = true;
    private boolean fillScreen;
    private boolean initBmpFinishFlag = false;
    
    private long exitTime = 0;	
    private boolean firstLongClick = false;
	
    BallFactory bsv;
    Context cnt;
    TopFrame ft;
    SportLayout sa;
    Canvas cs;
    Bitmap b;
    
	public SportLayout(Context context) {
		super(context);
		cnt = context;
		sa = this;
		Log.d(Constant.TAG, "----------------onCreate----------------");
		getStatusBarHeight();	
		OnTouchListener onTLster = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("--------------BallSurfaceView onTouch--------------"
								+ v.getId());
				if (event.getAction() == MotionEvent.ACTION_DOWN) { // 按下
					for (Ball ball : bsv.getBallList()) {
						ball.setPause(true);

						int x = (int) event.getX();
						int y = (int) event.getY(); // 为鼠标不在图标中心所以-50
						float ballx = ball.getRunX() + ball.getBallSize();
						float bally = ball.getRunY() + ball.getBallSize();

						if (x > ball.getRunX() && x < ballx
								&& y > ball.getRunY() && y < bally) {
							Log.e("System.out",
									"拖动-----------" + ball.getBallid());
							touchBall = ball;
							if (MainActivity.vibrate) {
								BallUtil.Vibrate(cnt, 20L); // 需要权限
							}

						}
					}
				}

				return false;
			}
		};	
			
			
	 this.setOnTouchListener(onTLster);
	 this.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("click");
			
		}
	});
 	
		
	 
	  if (!MainActivity.isTranp){ //不透明
		  this.setBackgroundDrawable(MainActivity.bmpdwe);
	  }else{
//		  this.setBackgroundColor(android.R.style.Theme_Translucent); //透明
		  //半透明
		  this.setBackgroundColor(Color.BLACK);
		  this.getBackground().setAlpha(100);
	  }
	  
		Handler handler = new Handler();
		handler.post(new Runnable() {
			public void run() {
				ZhuoyuUIUtil.startServiceAll(cnt,true);//启动所有服务
				
				bsv = new BallFactory(sa);  
			};
		});

	  
	  ballHandler.post(ballMoveRunnable); //球移动线程
	}
    
    

	// 绘制拖动时的图片
	/**
	 * 系统调的onDraw方法
	 * 需要把程序逻辑与UI分离,否则会触发android 4.0主线程响应太慢,而放弃绘制
	 *  (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
		//Log.(TAG, "onDraw ######" );
		invalidateDragImg(canvas);
		if (initBmpFinishFlag){
			canvas.drawBitmap(b,xtype2, ytype2,null);
		}
	}

	// 图片更随手势移动
	private void invalidateDragImg(Canvas canvas) {
		//Log.e(TAG, "handleActionUpEvenet : invalidateDragImg" );
		//以合适的坐标值绘制该图片
//		Random random = new Random();
//		int x = random.nextInt(200);
//		canvas.drawBitmap(Constant.ballBitmap1, x, 100, null);
		
		Ball b1 = BallFactory.ball;
		Ball b2 = BallFactory.ball2;
//		System.out.println("invalidateDragImg:"+b1.getRunX()+ b1.getRunY());
		canvas.drawBitmap(b1.getBallBitmapBk(), b1.getRunX(), b1.getRunY(), null);
		canvas.drawBitmap(b2.getBallBitmapBk(), b2.getRunX(), b2.getRunY(), null);
		
	}	
	
	
	
	
	/**球的移动动画*/
	Handler ballHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			ballHandler.postDelayed(ballMoveRunnable, 40);
		};
	};	
	
	Runnable ballMoveRunnable = new Runnable() {
		@Override
		public void run() {
			Message msg = ballHandler.obtainMessage();
			msg.arg1 = bumpfishFlag;
			
			if (bumpfishFlag == 0){
				System.out.println("bumpfishFlag == 0");
				ballHandler.removeCallbacks(ballMoveRunnable);	
				//防止相交后球还乱跑
				for (Ball ball2 : bsv.getBallList()) {
					ball2.setPause(true);	
				}
			}else{
				
				bsv.onDraw2();//动画移动计算
				Handler handler = new Handler();
				handler.post(new Runnable() {
					public void run() {
						invalidate();        //重绘动画
					};
				});
				
				// 把msg放入消息队列中：
				ballHandler.sendMessage(msg);	
			}
			
		}
 
	};
		
		
	/**结束动画*/	
	Handler heartHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			heartHandler.postDelayed(heartRunnable, 100);
			if (msg.arg1 == Constant.imgList.size()){
//				System.out.println("=end======10===========");
//				ft.remove(); //关闭
			}
		};
	};	
		
	Runnable heartRunnable = new Runnable() {
		
		boolean clearFlag = false;
		@Override
		public void run() {
			Message msg = heartHandler.obtainMessage();

			System.out.println("+++++++"+ imgIndex);
			
			if (imgIndex < Constant.imgList.size()) {
				// 执行心形动画
				b = Constant.imgList.get(imgIndex);
				initBmpFinishFlag = true; //完成标记
				invalidate();        //重绘
				msg.arg1 = ++imgIndex;
				// 把msg放入消息队列中：
				heartHandler.sendMessage(msg);					
			}else{
				heartHandler.removeCallbacks(heartRunnable);
				System.out.println("-----ft:"+ft);
				System.out.println("--------finish--------");
				if (!clearFlag){
					System.out.println("---------finish clearFlag vielw----------");
					ft.remove(); //关闭
					clearFlag = true;
				}
			}
		}
	};
		

	public void  threadFinish(){
		bumpfishFlag = 0;
		imgIndex = Constant.imgList.size();
	}

	
	/**每移动一点都触发*/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		System.out.println("----------SportActivity onTouchEvent--------------------");
			//2球碰撞
			if (touchBall!= null){
				int x= (int)event.getX() - 50;
				int y =(int)event.getY() - 50; //为鼠标不在图标中心所以-50
				for (Ball ball2 : bsv.getBallList()) {
					if (ball2.getBallid() == touchBall.getBallid()){
						continue; //因为ballList包含自己,所以排除自己
					}
					
					if(BallUtil.isBall2ballBump(x, y, ball2.getRunX(), ball2.getRunY(), touchBall.getBallSize())){
							Log.e("System.out", "---2球碰撞结束-------");
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
								
								if(music){
									Constant.froth2bumpMusic.start(); //播放
								}
								if (MainActivity.vibrate){
									BallUtil.Vibrate(cnt, 100L);	
								}
								
								bumpfishFlag = 0; //停止球的运动																
								heartHandler.post(heartRunnable);//启动心型动画
								
					}
				}


				//判断是否碰壁
	 		if (BallUtil.isBall2WallBump(Constant.screenWidth, Constant.screenHeight, x, y, touchBall.getBallSize())){
	 			Log.e("System.out", "---碰壁-------");
 				
	 		}else{
	 			//未碰到
	 			touchBall.setRunX(x);
				touchBall.setRunY(y);
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
						if (MainActivity.vibrate){
							BallUtil.Vibrate(cnt, 10L); //需要权限						
						}
						
					}					
					
					ball.setPause(false);
				}
				
				touchBall = null; //使用完释放
			}

		
	  return super.onTouchEvent(event);
	}

 
 
	protected void getStatusBarHeight() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
   	 System.out.println("surfaceChanged fill："+
			 Constant.screenHeight+","+  Constant.statusBarHeight +","    
			 +MainActivity.fillScreen);
		
		if(!MainActivity.fillScreen){ //非全屏去掉状态栏的高度
			if (Constant.statusBarHeight == 0){
				Rect rect= new Rect();  
				MainActivity.ma.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);  
				Constant.statusBarHeight = rect.top; //状态栏高度
				Constant.screenHeight = Constant.screenHeight - Constant.statusBarHeight;
			    System.out.println("000000000000000---statusBarHeight:"+Constant.statusBarHeight);    
			}else{
				Constant.screenHeight = Constant.screenHeight - Constant.statusBarHeight;
			}
			
		}
		
	} 
	
	

////TODO: 与全屏显示冲突
//	@SuppressLint("NewApi")
//	@Override
//    public void onAttachedToWindow() {
//		System.out.println("-------onAttachedToWindow---------");
////android底层系统把HOME键屏蔽了，但如果发现它是TYPE_KEYGUARD类的窗体，则不会过滤。所以把Activity修改成TYPE_KEYGUARD
////类就好了。 注意android 1.6 不支持屏蔽Home键
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD); 
//        super.onAttachedToWindow();
//		//发广播
////        Intent intent = new Intent(Constant.MAIN_ACTION);
////       this.sendBroadcast(intent); 
//        
//    }
// 
	
	
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
            		Toast.makeText(cnt, "紧急解锁！",Toast.LENGTH_SHORT).show();
            		LockService.setFirstLockFlag(false);// 释放锁屏标记
                		ft.remove();//关闭
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

 
 /**禁止通知栏下拉:可以只针对4.0以上版本,其他版本不需要,并且状态栏自带日期*/   
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);
        try {

            Object service = cnt.getSystemService("statusbar");            
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
            Method test = statusbarManager.getMethod("collapse");
            test.invoke(service);
        	
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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


	public boolean isFillScreen() {
		return fillScreen;
	}


	public void setFillScreen(boolean fillScreen) {
		this.fillScreen = fillScreen;
	}


//	@Override 简单显示
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		 View v = getChildAt(0);
//	       v.layout(l, t, r, b);
//	}

//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		final int count = getChildCount();
//		for (int i = 0; i < count; i++) {
//			final View child = getChildAt(i);
//			if (child.getVisibility() != View.GONE) {
//				
//				final int childWidth = child.getMeasuredWidth();
//				child.setVisibility(View.VISIBLE);
//				child.measure(r - l, b - t); // 这个调用必须有，网上很多文章没有这个调用导致子控件始终无法显示。
////				child.layout(childLeft, 0, childLeft + childWidth, child.getMeasuredHeight());
////				childLeft += childWidth;
//				
//                AbsoluteLayout.LayoutParams lp =
//                        (AbsoluteLayout.LayoutParams) child.getLayoutParams();
//
//                int childLeft = lp.x;
//                int childTop =  lp.y;
//                child.layout(childLeft, childTop,
//                        childLeft + child.getMeasuredWidth(),
//                        childTop + child.getMeasuredHeight());				
//			}
//		}
//	}	

	public TopFrame getFt() {
		return ft;
	}


	public void setFt(TopFrame ft) {
		this.ft = ft;
	}
    
	
}
