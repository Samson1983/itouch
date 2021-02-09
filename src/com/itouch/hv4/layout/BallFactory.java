package com.itouch.hv4.layout;

import java.util.ArrayList;
import java.util.List;

import android.view.SurfaceHolder;

import com.itouch.hv4.bean.Ball;
import com.itouch.util.Constant;
 
public class BallFactory {
		 private SportLayout sportActivity ;//调用该SurfaceView的上下文引用
         public static Ball ball,ball2 ;//小球
         
         public SportLayout getSportActivity() {
			return sportActivity;
		}


		public void setSportActivity(SportLayout sportActivity) {
			this.sportActivity = sportActivity;
		}

		private List<Ball> ballList;
        private boolean threadRun =true; //结束线程开关
        private int imgIndex = 0;
         
         
         public boolean isThreadRun() {
			return threadRun;
		}


		public void setThreadRun(boolean threadRun) {
			this.threadRun = threadRun;
		}


 

		public List<Ball> getBallList() {
			return ballList;
		}


		public void setBallList(List<Ball> ballList) {
			this.ballList = ballList;
		}


		public BallFactory(SportLayout context) {
                   this.sportActivity = (SportLayout)context ;
                   ball = new Ball(1,0,320);
                   ball2 = new Ball(2,150,0);
                   ballList = new ArrayList<Ball>();
                   ballList.add(ball);
                   ballList.add(ball2);
                   
         }
 

//		@Override
         /**
          *  只能有一个holder.lockCanvas操作否则绘动画时会出现(闪屏、闪烁、卡帧）
          * 	Handler：系统控制刷新时机
          * 	SurfaceView的独立线程,自已控制刷新时机
          *    
         * @param canvas
         */
        public void onDraw2() {
        	try{
	                		   /**调用刷新球的位置*/
	                		   ball.onDrawList(ballList);
	                           ball2.onDrawList(ballList);

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
        
        
//	protected void onDraw3() {
//		try {
//
////			// 执行心形动画
////			Bitmap b = Constant.imgList.get(this.imgIndex);
////			ImageView v = this.sportActivity.heartImgV;
////			v.setImageBitmap(b);
////			this.imgIndex++;
////			if (this.imgIndex == Constant.imgList.size()) {
////				this.imgIndex = 0;
////				this.sportActivity.finish(); // 关闭
////				System.out.println("--------finish--------");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
 
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
                            int height) {
        	 System.out.println("surfaceChanged fill："+
        			 Constant.screenHeight+","+  Constant.statusBarHeight +","    
        			 +Constant.fillScreen);
        		
//        		if(!this.sportActivity.isFillScreen()){ //非全屏去掉状态栏的高度
//        			if (Constant.statusBarHeight == 0){
//        				Rect rect= new Rect();  
//        				this.sportActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);  
//        				Constant.statusBarHeight = rect.top; //状态栏高度
//        				Constant.screenHeight = Constant.screenHeight - Constant.statusBarHeight;
//        			    System.out.println("000000000000000---statusBarHeight:"+Constant.statusBarHeight);    
//        			}else{
//        				Constant.screenHeight = Constant.screenHeight - Constant.statusBarHeight;
//        			}
//        			
//        		}
         }
 
 
 
 
         

 
}