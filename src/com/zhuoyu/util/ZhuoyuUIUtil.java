package com.zhuoyu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoyu.view.R;
import com.zhuoyu.view.lock.LockKeyguardService;
import com.zhuoyu.view.lock.LockService;

public class ZhuoyuUIUtil {

	
	public static void startServiceAll(Context context,boolean isLock){
        /**
         * 启动开机重启服务service
         * http://www.cnblogs.com/xiaoQLu/archive/2012/07/17/2595294.html
         * 包含FLAG_ACTIVITY_NEW_TASK的Intent启动Activity的Task正在运行, 则不会为该Activity创建新的Task,
         */
        Intent i = new Intent(context,LockKeyguardService.class);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(i);
      
      /**启动锁屏service*/
      Intent intent=new Intent(context,LockService.class);
      context.startService(intent);		
      
      LockService.setFirstLockFlag(isLock); //设置锁屏标记
		
	}
	
	
	public static void saveConfig(String key,boolean value) {
		/**
		 * 得到配置参数的类 参数1 配置参数文件的名字,没有后缀名 参数2 文件访问模式 只能是生成这个文件的应用访问
		 */
		Editor editor = Constant.share.edit();// 取得编辑器
		editor.putBoolean(key, value);// 存储配置 参数1 是key 参数2 是值
		editor.commit();// 提交刷新数据
	}	
	
	
	/** 
	 *  处理图片  
	 * @param bm 所要转换的bitmap 
	 * @param newWidth新的宽 
	 * @param newHeight新的高   
	 * @return 指定宽高的bitmap 
	 */ 
	 public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){   
	    // 获得图片的宽高   
	    int width = bm.getWidth();   
	    int height = bm.getHeight();   
	    // 计算缩放比例   
	    float scaleWidth = ((float) newWidth) / width;   
	    float scaleHeight = ((float) newHeight) / height;   
	    // 取得想要缩放的matrix参数   
	    Matrix matrix = new Matrix();   
	    matrix.postScale(scaleWidth, scaleHeight);   
	    // 得到新的图片   www.2cto.com
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);   
	    return newbm;   
	}  
	 
	
	/**
	 * 初始化图片列表、读取大小
	 */
	public static ArrayList<Bitmap> initImageList(Context context) {
		 
		ArrayList<Bitmap> imgList = new ArrayList<Bitmap>(); 
			// 用反射机制来获取资源中的图片ID和尺寸
			Field[] fields = R.drawable.class.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().indexOf("type") !=-1  
						)// 只包含了type的图片
				{
					int index = 0;
					try {
						index = field.getInt(R.drawable.class);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 保存图片ID
					Bitmap b = BitmapFactory.decodeResource(context.getResources(),index);
					b = ZhuoyuUIUtil.zoomImg(b, 100, 100);
					imgList.add(b);
				}
			}
			
			return imgList;
			
	}		
	
	public static void setTitleSize(TextView title){
		title.setTextSize(18);
		title.setHeight(25);
		title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
//		view.getPaint().setFakeBoldText(true);//加粗
		title.setTextColor(Color.BLACK);
		
	}
	public static void setSummarySize(TextView summary){
		summary.setTextSize(12);
		summary.setHeight(20);
	}
	
	/**
	 * 获取Bitmap图片
	 * @param context
	 * @param oneBbtn
	 * @param index
	 * @param defaultImg
	 * @return
	 */
	public static Bitmap setImageResource(Context context,ImageView oneBbtn,int index,int defaultImg ) {
//		String path = ZhuoyuUIUtil.getPath(context.getPackageName());
//		String fileName = ZhuoyuUIUtil.getFileName(index);		
//		path = path +File.separator+fileName;
//		File file = new File(path);
//		Bitmap b = null;
//		if (file.exists()) {
//			b = BitmapFactory.decodeFile(path); //获取用户图片
//		}else{	
//			b = BitmapFactory.decodeResource(context.getResources(), defaultImg); //获取默认图片
//		}
//		
//		if (oneBbtn != null){
//			oneBbtn.setImageBitmap(b);
//		}		
//		return b;
		
		String fileName = ZhuoyuUIUtil.getFileName(index);			
		Bitmap b = null;
		b = readFileBitmap(context,fileName);	
		System.out.println("-==============b:"+b);
		if (b == null){
			b = BitmapFactory.decodeResource(context.getResources(), defaultImg); //获取默认图片
		}
		if (oneBbtn != null){
			oneBbtn.setImageBitmap(b);
		}
		return b;
	}
	
	
	/**
	 * 存在SDCard没挂载读不出来的情况
	 * @param context
	 * @param oneBbtn
	 * @param index
	 * @param defaultImg
	 * @return
	 */
	public static Bitmap setImageResource_(Context context,ImageView oneBbtn,int index,int defaultImg ) {
		String path = ZhuoyuUIUtil.getPath(context.getPackageName());
		String fileName = ZhuoyuUIUtil.getFileName(index);		
		path = path +File.separator+fileName;
		File file = new File(path);
		Bitmap b = null;
		if (file.exists()) {
			b = BitmapFactory.decodeFile(path); //获取用户图片
		}else{	
			b = BitmapFactory.decodeResource(context.getResources(), defaultImg); //获取默认图片
		}
		
		if (oneBbtn != null){
			oneBbtn.setImageBitmap(b);
		}		
		return b;
	}
	public static String getPath(String pkgName){
		String path= Environment.getExternalStorageDirectory().toString();
		path = path +File.separator+ pkgName;
//		File file= Environment.getExternalStoragePublicDirectory(activity.getPackageName());
		return path;
	}
	
	public static String getFileName(int id){
		return id == 1? "ball_first.png":"ball_second.png";
	}
	
	
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param content  文件内容
	 * @throws IOException 
	 */
	public static Bitmap readFileBitmap(Context context,String filename){
		//利用javaIO实现文件的保存
		Bitmap bitmap = null;
		try {
			FileInputStream fis = context.openFileInput(filename);
			bitmap =  BitmapFactory.decodeStream(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return bitmap;
	}
	
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param content  文件内容
	 * @throws IOException 
	 */
	public static void saveFileBitmap(Context context,String filename, Bitmap bitmap) {
		//利用javaIO实现文件的保存
		FileOutputStream outStream;
		try {
			outStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			bitmap.compress(CompressFormat.PNG, 100, outStream);		
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{

		}
	}
	
	/**
	 * 保存到sdCard
	 * @param bitmap
	 */
	public  static void storeInSD(String path,String fileName,Bitmap bitmap) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		File imageFile = new File(file,fileName);
		try {
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	/**
	 * 两张图片合并为一张图片(在src的右下角画入水印)
	 * @param src
	 * @param watermark
	 * @return
	 */
	private static Bitmap createBitmap(Bitmap src, Bitmap watermark) {
		String tag = "createBitmap";
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);
		// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);
		// 在src的右下角画入水印 //save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);
		// 保存 //store
		cv.restore();
		// 存储
		return newb;
	}
	

	/**
	 *  android中将两张图片合并为一张图片
	 * @param firstBitmap
	 * @param secondBitmap
	 * @return
	 */
	public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
	        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),
	                firstBitmap.getConfig());
	        Canvas canvas = new Canvas(bitmap);
	        canvas.drawBitmap(firstBitmap, new Matrix(), null);
//	        canvas.drawBitmap(secondBitmap, 0, 0, null);
	        int x  = ( firstBitmap.getWidth() - secondBitmap.getWidth() ) /2;
	        int y =  ( firstBitmap.getHeight() - secondBitmap.getHeight() ) /2;
	        canvas.drawBitmap(secondBitmap, x, y, null);
	        return bitmap;
	    }
	
    /** 
     * 转换图片成圆形 
     * @param bitmap 传入Bitmap对象 
     * @return 
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) { 
            int width = bitmap.getWidth(); 
            int height = bitmap.getHeight(); 
            float roundPx; 
            float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom; 
            if (width <= height) { 
                    roundPx = width / 2; 
                    top = 0; 
                    bottom = width; 
                    left = 0; 
                    right = width; 
                    height = width; 
                    dst_left = 0; 
                    dst_top = 0; 
                    dst_right = width; 
                    dst_bottom = width; 
            } else { 
                    roundPx = height / 2; 
                    float clip = (width - height) / 2; 
                    left = clip; 
                    right = width - clip; 
                    top = 0; 
                    bottom = height; 
                    width = height; 
                    dst_left = 0; 
                    dst_top = 0; 
                    dst_right = height; 
                    dst_bottom = height; 
            } 
              
            Bitmap output = Bitmap.createBitmap(width, 
                            height, Config.ARGB_8888);            
            Canvas canvas = new Canvas(output); 
              
            final int color = 0xff424242; 
            final Paint paint = new Paint(); 
            final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom); 
            final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom); 
            final RectF rectF = new RectF(dst); 

            paint.setAntiAlias(true); 
              
            canvas.drawARGB(0, 0, 0, 0); 
            paint.setColor(color); 
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
            canvas.drawBitmap(bitmap, src, dst, paint); 
            return output; 
    }	
		
}
