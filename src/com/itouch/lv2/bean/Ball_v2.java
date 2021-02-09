package com.itouch.lv2.bean;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.itouch.lv2.layout.BallSurfaceView;
import com.itouch.lv2.layout.SportActivity;
import com.itouch.util.BallUtil;
import com.itouch.util.Constant;
 
/**
 * 球类
 * @author zcl
 *
 */
/**
 * @author Administrator
 *
 */
public class Ball_v2 {
          
	// int ball_size = (int) (Math.random() * 30);
	private int ballid = 0; //唯 一标记
	private int ballSize; //图片大小
	private float runX;// 球的x坐标位置
	private float runY;// 球的y坐标位置
	private int x_increase;
	private int y_increase;
	private Paint paint;
	private Bitmap ballBitmapBk;// 球的图片
	private SportActivity sa;
	private boolean isPause; //拖动时暂停绘画
	
	
	
	
	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	private int mass;
	
	public int getMass() {
		return mass;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}

	public Bitmap getBallBitmapBk() {
		return ballBitmapBk;
	}

	public void setBallBitmapBk(Bitmap ballBitmapBk) {
		this.ballBitmapBk = ballBitmapBk;
	}

	public int getX_increase() {
		return x_increase;
	}

	public void setX_increase(int x_increase) {
		this.x_increase = x_increase;
	}

	public int getY_increase() {
		return y_increase;
	}

	public void setY_increase(int y_increase) {
		this.y_increase = y_increase;
	}
	public int getBallid() {
		return ballid;
	}

	public void setBallid(int ballid) {
		this.ballid = ballid;
	}

	public float getRunX() {
		return runX;
	}

	public void setRunX(float runX) {
		this.runX = runX;
	}

	public float getRunY() {
		return runY;
	}

	public void setRunY(float runY) {
		this.runY = runY;
	}
	public int getBallSize() {
		return ballSize;
	}

	public void setBallSize(int ballSize) {
		this.ballSize = ballSize;
	}
         
         
         
         
         public Ball_v2(int ballId,int x, int y, BallSurfaceView bsv){
                   paint = new Paint();
              	   paint.setAntiAlias(true); //直接在paint上加上抗锯齿
              	   
                	 //每次创建对象计数
        	       this.ballid = ballId;
        	       this.mass = ballId;
        	       //起始坐标随机
        	       this.runX  = x;
        	       this.runY =y;  
//        	       System.out.println(this.ballid);
//        	       System.out.println(this.runX+","+this.runY);
        	       

        	       sa = bsv.getSportActivity();
        	       //获取图片
        	       if (1 == ballId){
        	    	   ballBitmapBk = Constant.ballBitmap1; 
        	       }else{
        	    	   ballBitmapBk = Constant.ballBitmap2;
        	       }
        	       
        	       this.ballSize = ballBitmapBk.getWidth() ; //5为图片外框与圆的距离
        	       System.out.println(ballBitmapBk.getWidth()+",+++++++++,"+this.ballSize);
        	       
        	    	 x_increase = - Constant.ballSpeed; 
        	    	 y_increase = - Constant.ballSpeed /2;
//                   x_increase = -8;
//                   y_increase = -4;
//                   System.out.println("x:"+x_increase+"y:"+y_increase);
         }
         
         public void onDrawList(Canvas canvas,List<Ball_v2> ballList){
        	 
        	if (!isPause){
	        	 for (Ball_v2 ball2 : ballList) {
					if (this.ballid == ball2.getBallid()){
	//					Log.e("System.out", "所以排除自己");
						continue; //因为ballList包含自己,所以排除自己
					}
				
//					System.out.println("this.runX:"+this.runX+", this.runY:"+this.runY);
//					System.out.println("ball2.runX:"+ball2.getRunX()+", ball2.runY:"+ball2.getRunY());
					
					/**
					 * 两个图心的坐标差 <=两个图的半径之和,两图碰撞,反之未碰撞
					 */
					//(x1-x2)平方+(y1-y2)平方)的平方根
				if(BallUtil.isBall2ballBump(this.runX, this.runY, ball2.getRunX(), ball2.getRunY(), ballSize)){
//						Log.e("System.out", "碰.........需要转向");
//						System.out.println("..begin.......x:"+x_increase+","+y_increase);

						/**算法有问题：有时候重叠不转向;*/
						/**样式1
						x_increase = -x_increase ;
						ball2.setX_increase(-ball2.getX_increase());
						y_increase = -y_increase ;
						ball2.setY_increase(-ball2.getY_increase());
					    */	
						
						/**样式2	
						 int realitySizeX = Constant.screenWidth   - ballSize;
						int realitySizeY = Constant.screenHeight   - ballSize;
						if (runX > ballSize || runX < realitySizeX){
							x_increase = -x_increase ;
							ball2.setX_increase(-ball2.getX_increase());
						}
						if (runY > ballSize || runY < realitySizeY) {
							y_increase = -y_increase ;
							ball2.setY_increase(-ball2.getY_increase()); 
							
						}*/
						

					
						//碰撞后速度公式x                                     
						int vx0Final = ((this.mass - ball2.getMass()) * 
											      
								this.x_increase + 2 * ball2.getMass() *                        
											      
								ball2.getX_increase()) / (this.mass + ball2.getMass());             
											      
						int vx1Final = ((ball2.getMass() - this.mass) *  
											      
									 	ball2.getX_increase() + 2 * this.mass *                        
											      
									 	this.x_increase) / (this.mass + ball2.getMass());              
											
						
						this.x_increase = vx0Final;                                
											      
						ball2.setX_increase(vx1Final);                               
											      
						this.runX += this.x_increase;                                 
								
						ball2.setRunX(ball2.getRunX()+ball2.getX_increase());
						
						//碰撞后速度公式y			
						int vy0Final = ((this.mass - ball2.getMass()) * 
							      
								this.y_increase + 2 * ball2.getMass() *                        
											      
								ball2.getY_increase()) / (this.mass + ball2.getMass());             
											      
						int vy1Final = ((ball2.getMass() - this.mass) *  
											      
									 	ball2.getY_increase() + 2 * this.mass *                        
											      
									 	this.y_increase) / (this.mass + ball2.getMass());              
											      
						this.y_increase = vy0Final;                                
											      
						ball2.setY_increase(vy1Final);                               
											      
						this.runY += this.y_increase;                                 
								
						ball2.setRunY(ball2.getRunY()+ball2.getY_increase());			
						
						
						/**防止:时间长了球会静止不动问题
						 * x_increase与y_increase的值不能同时为0,否则会因为没有编移量,以不动。
						 * x_increase与y_increase的值不能相同,否则会反复闪烁
						 * ball2.setX_increase与ball1.setX_increase的值不能相同，因为通过动量公式计算出结果都一样,即球不会运动。
						 * ball2.setX_increase与ball1.setX_increase的值正负数必须相反，因为会与球1同方向运动
						 * */
					if (x_increase == 0 && y_increase == 0){
		        	       	 x_increase = - Constant.ballSpeed -1; 
		        	    	 y_increase = - Constant.ballSpeed /2 -1;					
							ball2.setX_increase(- y_increase); 
							ball2.setY_increase(- x_increase);				
//							System.out.println("x_increase:"+x_increase+","+y_increase);
//							System.out.println("ball2.x_increase:"+ball2.getX_increase()+","+ball2.getY_increase());
						}  
					 
						
//						if ((x_increase <=0 && ball2.getX_increase() <=0)
//								&& (y_increase <=0 && ball2.getY_increase() <=0)
//								){
//		        	       	 x_increase = - 20; 
//		        	    	 y_increase = - 20;
//							ball2.setX_increase(- y_increase);
//							ball2.setY_increase(- x_increase);
//							System.out.println("x_increase:"+x_increase+","+y_increase);
//							System.out.println("ball2.x_increase:"+ball2.getX_increase()+","+ball2.getY_increase());
//
//						} 						
					
						
//						System.out.println("...af......"+x_increase+","+y_increase);
					}else{
						
//						if (x_increase >=10 || y_increase >=10 ){
//		        	    	 x_increase = - sa.getBallSpeed(); 
//		        	    	 y_increase = - sa.getBallSpeed() /2;
//							ball2.setX_increase(- y_increase);
//							ball2.setY_increase(- x_increase);
//						}						
						
					}
					
			
				} //for end
	        	 
	        	 //绘图自己
	        	 this.onDraw(canvas);
        	}else{
        		 this.iDraw(canvas);
        		 
        		
        	}
       	 
        	
        	
         }
        
         
         public void iDraw(Canvas canvas) {
        	 int c = paint.getColor();//保存颜色，之后还原为之前颜色                   
//                   paint.setStyle( Paint.Style.STROKE );   //空心   
//                   paint.setAlpha( 75 );   // Bitmap透明度(0 ~ 100)
        	 paint.setColor(Color.WHITE);

        	 boundaryTest(); //检查是否碰墙
        	 if(canvas != null) { 
        		 canvas.drawBitmap(ballBitmapBk,runX,runY,paint);                	   
        	 }
         }
         
         public void onDraw(Canvas canvas) {
//                   int c = paint.getColor();//保存颜色，之后还原为之前颜色                   
//                   paint.setStyle( Paint.Style.STROKE );   //空心   
//                   paint.setAlpha( 75 );   // Bitmap透明度(0 ~ 100)
//                   paint.setColor(Color.WHITE);
                   boundaryTest(); //检查是否碰墙
                   move(); //移动                          
                   
                   if(canvas != null) { 
                	   canvas.drawBitmap(ballBitmapBk,runX,runY,paint);                	   
                	   
//                	   System.out.println("runX:"+runX+",runY:"+runY);
//                	   canvas.drawCircle(runX,runY, ball_size, paint);//因为canvas.drawCircle方法的cx,cy,是从中心画图
                   }
//                   paint.setColor(Color.RED);
                   
                   
         }
         
         
         /**
          * 运动
          */
         private void move() {
             runX = runX + x_increase;
			 runY = runY + y_increase;
         }
 
        
         
         /**
          * 边界检测，使球不会飞出边界
         * 内部判断是否碰壁使用
          * 判断球是否到达了边界,若到达了则转向(利用正负数)
          * flag: 是外面传入控制转向使用
          */
         private void boundaryTest(){
// 			// 计算画球时X轴,Y轴的最大坐标
//          	 System.out.println(sa.screenWidth+"-"+ballSize+","+sa.screenHeight); //不能放在构造函数,因为没有layout,得不到屏幕大小
//  	 			System.out.println("runX:"+runX+",runY:"+runY);

               	//判断球是否到达了边界,若到达了则转向(利用正负数)
  	 			if (BallUtil.eqSize(Constant.screenWidth, (int)runX, ballSize))
//   				if (runX <= ball_size || runX >= HuaBan_x) //因为canvas.drawCircle方法的cx,cy,是从中心画所有要减去半径
   					x_increase = -x_increase;
   				if (BallUtil.eqSize(Constant.screenHeight, (int)runY, ballSize))
//   				if (runY <= ball_size || runY >= HuaBan_y) //因为canvas.drawCircle方法的cx,cy,是从中心画所有要减去半径
   					y_increase = -y_increase;
   				
         }
         
}