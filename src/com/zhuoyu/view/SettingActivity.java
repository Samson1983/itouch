package com.zhuoyu.view;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuoyu.util.Constant;
import com.zhuoyu.util.ZhuoyuUIUtil;

public class SettingActivity extends PreferenceActivity{

	private static SettingActivity activity;
	private static ImageView imgView ;
	private static Bitmap bm ;
	private long exitTime = 0;	
	
	protected void onCreate(Bundle savedInstanceState) {
        
		super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.setting_preference);
        
        ZhuoyuUIUtil.startServiceAll(this,false);//启动所有服务
    }

	
	/**
	 * 选择提示对话框
	 */
	public static void ShowPickDialog(Context context,View v) {
		activity = ((SettingActivity) context);
		imgView = (ImageView) v;
		bm = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
		System.out.println("--------------ShowPickDialog");
//		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
		 new AlertDialog.Builder(activity).
		       setTitle("第"+imgView.getId()+"个泡泡,设置头像...")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						/**
						 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
						 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
						 */
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						
						/**
						 * 下面这句话，与其它方式写是一样的效果，如果：
						 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						 * intent.setType(""image/*");设置数据类型
						 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
						 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
						 */
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						
						activity.startActivityForResult(intent, 1);

					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						/**
						 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
						 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
						 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
						 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
						 */
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						//下面这句指定调用相机拍照后的照片存储的路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										"xiaoma.jpg")));
						activity.startActivityForResult(intent, 2);
						
						
					}
				}).show();
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_OK:
				System.out.println("RESULT_OK");
				break;
			case RESULT_FIRST_USER:
				System.out.println("RESULT_FIRST_USER");
				break;
			case RESULT_CANCELED: //用户取消
				System.out.println("RESULT_CANCELED");
				return;
//				break;
	
			default:
				break;
		}
		
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			startPhotoZoom(data.getData());
			break;
		// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/xiaoma.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话，
			 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只
			 * 在这个地方加下，大家可以根据不同情况在合适的
			 * 地方做判断处理类似情况
			 * 
			 */
			if(data != null){
				setPicToView(data);
			}
			break;
		default:
			break;

		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	

	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	private static void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
		 * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
		 * 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		int tmpWd = (int) (bm.getWidth() - (bm.getWidth() * 0.3)); //减30%
		int tmpHg = (int) (bm.getHeight() - (bm.getHeight() * 0.3)); //减30%
		intent.putExtra("outputX", tmpWd);
		intent.putExtra("outputY", tmpHg);
		intent.putExtra("return-data", true);


		activity.startActivityForResult(intent, 3);
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private static void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			
//			int w = 50;//getWidth(); 
//	        int h = 50;//getHeight(); 
//	        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888); 
//	        Canvas canvas = new Canvas(bitmap); 
//	        Paint paint = new Paint(1); 
//	        paint.setColor(0xff000000); 
//	        RectF rectf = new RectF(0F, 0F, w, h); 
//	        canvas.drawOval(rectf, paint); 
			
			/**1.在调用getDrawingCache()方法从ImageView对象获取图像之前，一定要调用imageview.setDrawingCacheEnabled(true)方法,
			 * 否则，无法从ImageView对象iv_photo中获取图像；
			 * */
//			imgView.setDrawingCacheEnabled(true);
//			imgView.getDrawingCache();
			Bitmap tmpBitmap=  ZhuoyuUIUtil.toRoundBitmap(photo);

			System.out.println(bm.getWidth()+","+bm.getHeight());
			Bitmap ok = ZhuoyuUIUtil.mergeBitmap(bm,tmpBitmap);
//	        Drawable drawable = new BitmapDrawable(bitmap);		
//			Bitmap ok = createBitmap(oneBbtn.getDrawingCache(),tmpBitmap);
			
			/**
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
			 * 传到服务器，QQ头像上传采用的方法跟这个类似
			 */
			
			/*
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			// 将图片流以字符串形式存储下来
			
			tp = new String(Base64Coder.encodeLines(b));
			这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，
			服务器处理的方法是服务器那边的事了，吼吼
			
			如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
			为我们可以用的图片类型就OK啦...吼吼
			Bitmap dBitmap = BitmapFactory.decodeFile(tp);
			Drawable drawable = new BitmapDrawable(dBitmap);
			*/
//			alertDialog.dismiss(); //关闭
			imgView.setImageBitmap(ok);
//			oneBbtn.setBackgroundDrawable(drawable);
			String path= ZhuoyuUIUtil.getPath(activity.getPackageName());
			String fileName = ZhuoyuUIUtil.getFileName(imgView.getId());
			System.out.println(path);
//			
//			FileOutputStream fos = activity.openFileOutput(file,Context_MODE_WORLD_WRITEABLE);
//			fos.write(bytes);
//			fos.close();
//			FileInputStream fis = activity.openFileInput("fileName");

//			ZhuoyuUIUtil.storeInSD(path,fileName,ok);

			ZhuoyuUIUtil.saveFileBitmap(activity,fileName,ok);
			
		}
	}	
	
	

	   @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	            // TODO Auto-generated method stub
	    	  Log.e(Constant.TAG, "onKeyDown:"+keyCode+"");
	            switch(keyCode) {
	            case KeyEvent.KEYCODE_MENU:
	            	Log.e(Constant.TAG, "菜单键,丫的，看你往哪里跑!");
	            	return true;  //屏蔽按键           
	            case KeyEvent.ACTION_DOWN:
	            case KeyEvent.KEYCODE_BACK:
	            	Log.e(Constant.TAG, "退出键,丫的，看你往哪里跑!"+System.currentTimeMillis()+","+exitTime);
	            	if ((System.currentTimeMillis() - exitTime) > 2000) { 
	            		Toast.makeText(getApplicationContext(), "再按一次退出程序",
	            		Toast.LENGTH_SHORT).show();
	            		exitTime = System.currentTimeMillis();
	            		System.out.println("exitTime:"+exitTime);
	            		} else {
	            		finish();
//	            		System.exit(0);
	            		}

	            	return true;  //屏蔽按键           
	            case KeyEvent.KEYCODE_HOME:
	                    Log.e(Constant.TAG, "HOME键丫的，看你往哪里跑!");
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

	
}
