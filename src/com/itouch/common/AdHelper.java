package com.itouch.common;

import android.content.Context;
import cn.waps.AppConnect;

public class AdHelper {

	public static String getOnLineADVaule(Context context){
//		 String value = AppConnect.getInstance(context).getConfig("ad", "false"); //安卓、智慧云审核不通过
		 String value = "true";
		 return value;
	}
}
