package com.itouch.view;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import cn.waps.AdView;
import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import com.itouch.common.AdHelper;
import com.itouch.util.Constant;
import com.itouch.util.LogcatHelper;
import com.itouch.util.ZhuoyuUIUtil;

public class TabHostSample extends TabActivity implements OnCheckedChangeListener,UpdatePointsNotifier {
 
	private RadioGroup mainTab;
	private TabHost tabhost;
	private Intent iHome;
	private Intent iNews;
	private Intent iInfo;
	private Intent iSearch;
	private Intent iMore;	
	public static TabHostSample tbSA;
	public static TextView summary;
	
	String displayPointsText;
	boolean showDialog = false;
    LogcatHelper logger; 
	
	final Handler mHandler = new Handler();


	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ZhuoyuUIUtil.hasSdcard()){
        	Handler handler = new Handler();
        	handler.post(new Runnable() {
        		public void run() {
        			//日志
        			LogcatHelper.init(TabHostSample.this);
        			logger = LogcatHelper.getInstance(TabHostSample.this);
        			logger.start();
        		};
        	});
        }
		
        Constant.share = this.getSharedPreferences("perference", MODE_PRIVATE);
        Constant.unLock = Constant.share.getBoolean("unLock", true);
//		System.out.println("++++Constant.unLock="+Constant.unLock);
        //从TabActivity上面获取放置Tab的TabHost
        tabhost = this.getTabHost();

        
        setContentView(R.layout.main);
        mainTab=(RadioGroup)findViewById(R.id.main_tab);
        mainTab.setOnCheckedChangeListener(this);
        tabhost = getTabHost();
        
        iHome = new Intent(this, SettingActivity.class);
        tabhost.addTab(tabhost.newTabSpec("iHome")
        		.setIndicator(getResources().getString(R.string.tab1))
        		.setContent(iHome));

        
		iNews = new Intent(this, SettingActivity.class);
		tabhost.addTab(tabhost.newTabSpec("iNews")
	        	.setIndicator(getResources().getString(R.string.tab2))
	        	.setContent(iNews));
		
		iInfo = new Intent(this, SettingActivity.class);
		tabhost.addTab(tabhost.newTabSpec("iInfo")
	        	.setIndicator(getResources().getString(R.string.tab3))
	        	.setContent(iInfo));
		
		iSearch = new Intent(this,SettingActivity.class);
		tabhost.addTab(tabhost.newTabSpec("iSearch")
	        	.setIndicator(getResources().getString(R.string.menu_search))
	        	.setContent(iSearch));
		
		iMore = new Intent(this, SettingActivity.class);
		 tabhost.addTab(tabhost.newTabSpec("iMore")
	        		.setIndicator(getResources().getString(R.string.more))
	        		.setContent(iMore));        
        
		 this.tbSA = this;
		 

		 if ("true".equals(AdHelper.getOnLineADVaule(this))){
			 // 互动广告调用方式
			 LinearLayout layout = (LinearLayout) findViewById(R.id.AdLinearLayout);
			 new AdView(this, layout).DisplayAd();
		 }
		 
		 		 
    }
	
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radio_button0:
			this.tabhost.setCurrentTabByTag("iHome");
			break;
		case R.id.radio_button1:
			//显示推荐列表（综合）
			AppConnect.getInstance(this).showOffers(this);
			this.tabhost.setCurrentTabByTag("iHome");
			break;
		case R.id.radio_button2:
			this.tabhost.setCurrentTabByTag("iHome");
			//用户反馈
			AppConnect.getInstance(this).showFeedback();

			//未订购
//			Constant.unLock = false;
//			QQUIUtil.saveConfig("unLock", Constant.unLock);			
//			  selecterDialog();
			  
			break;
		case R.id.radio_button3:
			//手动检查新版本
			AppConnect.getInstance(this).checkUpdate(this);
			
			//奖励虚拟货币
//			AppConnect.getInstance(this).awardPoints(60, this);

			
			break;
		case R.id.radio_button4:
			//退出
			this.finish();
//			System.exit(0); //释放内存
			
			break;
		}
	}


	public  void selecterDialog() {
		new AlertDialog.Builder(this).
			       setTitle("解锁将扣除"+Constant.SALE_PRICE+"积分？")
					  .setPositiveButton("同意", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int which) {
							  //消费虚拟货币.
							  AppConnect.getInstance(TabHostSample.this).spendPoints(Constant.SALE_PRICE, TabHostSample.this);
							  Constant.pointTotal = Constant.pointTotal -Constant.SALE_PRICE;
							  showDialog = true; //显示提示框
						  }
					  })			       
					.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.dismiss();
							//TODO: 不需要
						}
					})	.show();
	}
	
	
	@Override
	protected void onDestroy() {
		AppConnect.getInstance(this).finalize();
		super.onDestroy();
		logger.stop();
	}
	
	@Override
	protected void onResume() {
		Constant.pointTotal = -1; //初始化,积分总额
		
		// 从服务器端获取当前用户的虚拟货币.
		// 返回结果在回调函数getUpdatePoints(...)中处理
//		System.out.println("onResume++++++++++++");
//		try {
//			AppConnect.getInstance(this).getPoints(this);
//			
//		} catch (Exception e) {
//			System.out.println("获取积分异常!");
//			e.printStackTrace();
//		}
		super.onResume();
	}
	
	/**
	 * 【不能在这里做耗时程序处理,需要使用mHandler】
	 * AppConnect.getPoints()方法的实现，必须实现
	 * 
	 * @param currencyName
	 *            虚拟货币名称.
	 * @param pointTotal
	 *            虚拟货币余额.
	 */
	public void getUpdatePoints(String currencyName, int pointTotal) {
		if (!Constant.unLock){
			displayPointsText = currencyName + ": " + pointTotal;
//			System.out.println(Constant.pointTotal+","+pointTotal);
			if (Constant.pointTotal == pointTotal){
				//已订购
				Constant.unLock = true;
//				Constant.pointTotal = -1;
				/**不能在这里做程序处理**/
			}else{
				Constant.pointTotal = pointTotal;			
			}
//			System.out.println(displayPointsText);
			mHandler.post(mUpdateResults);
		}
	}
	
	/**
	 * 【不能在这里做耗时程序处理,需要使用mHandler】
	 * AppConnect.getPoints() 方法的实现，必须实现
	 * 
	 * @param error
	 *            请求失败的错误信息
	 */
	public void getUpdatePointsFailed(String error) {
		if (!Constant.unLock){
			displayPointsText = error;
//			System.out.println(displayPointsText);
			mHandler.post(mUpdateResults);
		}
	}	
	
	// 创建一个线程
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
	
			if (showDialog) {
				String oraderMsg = "";
				if (Constant.unLock) {
					oraderMsg = "订购成功,剩余" + displayPointsText;
					summary.setText(""); //去掉需要积分
					ZhuoyuUIUtil.saveConfig("unLock", Constant.unLock); //保存标记
				} else {
					oraderMsg = displayPointsText;
				}

				new AlertDialog.Builder(TabHostSample.this)
						.setTitle(oraderMsg)
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();
				
				showDialog = false; //不显示,提示框
			}
		}
	};
	
 
	 
}