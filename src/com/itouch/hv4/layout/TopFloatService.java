package com.itouch.hv4.layout;

import android.content.Context;
import android.os.Handler;




public class TopFloatService  {

	public static TopFrame tf = null ;

 	
 
	public  TopFloatService(Context c) {
		
		if (tf == null){
			tf = new TopFrame(c);
			Handler handler = new Handler();
			handler.post(new Runnable() {
				public void run() {
					tf.createView();
					
					tf.addView();
				};
			});
		}
		

	}

 
	
	public static void destory() {
//		System.out.println("--------onDestroy----------");
		if (tf != null) {
			tf.remove();
		}
			
	}


}