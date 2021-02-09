package com.zhuoyu.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.zhuoyu.bean.Ball;
import com.zhuoyu.util.Constant;
 
public class BallSurfaceView extends SurfaceView
implements SurfaceHolder.Callback{
		 private SportActivity sportActivity ;//调用该SurfaceView的上下文引用
         private Ball ball,ball2 ;//小球
         private SurfaceHolder holder ;
         public SportActivity getSportActivity() {
			return sportActivity;
		}


		public void setSportActivity(SportActivity sportActivity) {
			this.sportActivity = sportActivity;
		}

		private List<Ball> ballList;
         private RefreshThread thread;
         private boolean threadRun =true; //结束线程开关
         private int imgIndex = 0;
         
         
         public boolean isThreadRun() {
			return threadRun;
		}


		public void setThreadRun(boolean threadRun) {
			this.threadRun = threadRun;
		}


		public RefreshThread getThread() {
			return thread;
		}


		public void setThread(RefreshThread thread) {
			this.thread = thread;
		}


		public List<Ball> getBallList() {
			return ballList;
		}


		public void setBallList(List<Ball> ballList) {
			this.ballList = ballList;
		}


		public BallSurfaceView(Context context) {
                   super(context);
                   this.sportActivity = (SportActivity)context ;
                   
                   ball = new Ball(1,0,320,this); 
                   ball2 = new Ball(2,180,0,this);
                   ballList = new ArrayList<Ball>();
                   ballList.add(ball);
                   ballList.add(ball2);
                   holder = this.getHolder();
//                   this.setZOrderOnTop(true);  
//                   this.setsetEGLConfigChooser(8, 8, 8, 8, 16, 0);  
                   holder.setFormat(PixelFormat.TRANSLUCENT);
                   
                   holder.addCallback(this);
                   
         }
 

//		@Override
         /**
          *  只能有一个holder.lockCanvas操作否则绘动画时会出现(闪屏、闪烁、卡帧）
          * 	Handler：系统控制刷新时机
          * 	SurfaceView的独立线程,自已控制刷新时机
          *    
         * @param canvas
         */
        protected void onDraw2(Canvas canvas) {
//                   super.onDraw(canvas);
        	try{
        		
	                   if(canvas == null){ 
	                	   canvas = holder.lockCanvas();//锁定画布     
	                	   if(canvas != null){   
	                		   canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);   //清除画布
	                		   /**判断是否已经相交:防止小球刷新乱动 */
	                		   if (this.sportActivity.isBumpfishFlag()){
	                			   ball.setPause(true);
								   ball2.setPause(true);  
	                		   }
	                		   /**调用刷新球的位置*/
	                		   ball.onDrawList(canvas,ballList);
	                           ball2.onDrawList(canvas,ballList);
	                           
//	                           System.out.println(Thread.currentThread().getId()+"-----------"+Thread.currentThread().getName());
	                           
	                           /**判断是否已经相交 */
	                           if (this.sportActivity.isBumpfishFlag()){
	                        		//执行心形动画
	                        	   if (this.imgIndex < Constant.imgList.size()){
		                        	   Bitmap b = Constant.imgList.get(this.imgIndex);
		                        	   canvas.drawBitmap(b, this.sportActivity.getXtype2(), this.sportActivity.getYtype2(),null);
		                        	   this.imgIndex++;
	                        	   } else{
//											this.imgIndex = 0;
											this.surfaceDestroyed(holder); //停止绘动主线程 
											this.sportActivity.finish(); //关闭
											System.out.println("--------finish--------");
										}
										
	                           }
	                           
	                           holder.unlockCanvasAndPost(canvas);//释放锁
	                	   }
	                   }
        	}catch (Exception e) {
				e.printStackTrace();
			}	                   
                   /**
                   Paint p = new Paint();
                   int c = p.getColor();
                   p.setAlpha(50);//飞碟 幻影效果
                   p.setColor(Color.BLUE);//设置背景白色
                   if(canvas != null){
                	   canvas.drawRect(0, 0, sportActivity.screenWidth, sportActivity.screenHeight, p);
                   }
                   p.setColor(c);
                   */

         }
 
		@Override
         public void surfaceChanged(SurfaceHolder holder, int format, int width,
                            int height) {
        	 System.out.println("surfaceChanged fill："+
        			 Constant.screenHeight+","+  Constant.statusBarHeight +","    
        			 +this.sportActivity.isFillScreen());
        		
        		if(!this.sportActivity.isFillScreen()){ //非全屏去掉状态栏的高度
        			if (Constant.statusBarHeight == 0){
        				Rect rect= new Rect();  
        				this.sportActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);  
        				Constant.statusBarHeight = rect.top; //状态栏高度
        				Constant.screenHeight = Constant.screenHeight - Constant.statusBarHeight;
        			    System.out.println("000000000000000---statusBarHeight:"+Constant.statusBarHeight);    
        			}else{
        				Constant.screenHeight = Constant.screenHeight - Constant.statusBarHeight;
        			}
        			
        		}
         }
 
         @Override
         public void surfaceCreated(SurfaceHolder holder) {
        	 System.out.println("surfaceCreated");
        		 thread =  new RefreshThread();
        		 thread.start();
         }
         
         
 /**
         @Override
		protected void onAttachedToWindow() {
        	 this.sportActivity.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
			super.onAttachedToWindow();
		}
*/

		@Override
         public void surfaceDestroyed(SurfaceHolder holder) {
        	 System.out.println("surfaceDestroyed");
         }
        
         public class RefreshThread extends Thread{
 
                   @Override
                   public void run() {
 
                            while(threadRun){
                                     Canvas canvas = null;
                                     try{
                                               onDraw2(canvas);
                                     }catch(Exception e){
                                               e.printStackTrace();
                                     }
                                    
                                     try {
                                               Thread.sleep(40);
                                     } catch (InterruptedException e) {
                                               e.printStackTrace();
                                     }
                            }
                   }
                  
         }
         

 
}