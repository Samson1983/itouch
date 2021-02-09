package com.itouch.view.preference;

import com.itouch.util.ZhuoyuUIUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 2个Radio
 * @author Administrator
 *
 */
public class RadioPreference extends Preference  {
	private Boolean isTranspValue = false; // 默认情况是false
	private String iTitle;
	private String radio1name;
	private String radio2name;
	
	public RadioPreference(Context context) {
		super(context);
	}

	public RadioPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RadioPreference(Context context, AttributeSet attrs,
			int defStyle) {
        super(context, attrs, defStyle);
        
	}

	
	@Override
	protected View onCreateView(ViewGroup parent) {
		 String tmp = (String) getTitle();
		 String[] arr  = tmp.split("\\|");
		 if (arr != null && arr.length >= 2){
			 iTitle = arr[0];
			 radio1name = arr[1];
			 radio2name = arr[2];
		 }
		 
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);		
		params1.weight = 1.0f;
		TextView title = new TextView(getContext()); // 定义 Textview用于显示标题
		title.setText(iTitle);
		ZhuoyuUIUtil.setTitleSize(title); //设置字体大小、颜色等
		title.setLayoutParams(params1);
		title.setGravity(Gravity.CENTER_VERTICAL);
		
		
		TextView summary = new TextView(getContext()); // 定义 summary用于显示标题
		summary.setLayoutParams(params1);
		summary.setText(getSummary());
		ZhuoyuUIUtil.setSummarySize(summary); //设置字体大小
		summary.setGravity(Gravity.CENTER_VERTICAL);
		
		LinearLayout subLayout = new LinearLayout(getContext()); // 定义layout
		subLayout.setLayoutParams(params1);	
		subLayout.setPadding(10, 0, 5, 0);
		subLayout.setOrientation(LinearLayout.VERTICAL);
		subLayout.addView(title);
		subLayout.addView(summary);
				
       /**btn*/		
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params2.gravity = Gravity.CENTER_HORIZONTAL;
		params2.leftMargin = 30;
		final RadioButton oneBbtn = new RadioButton(getContext()); // 定义		
		// ToggleButton
		// 用于设置ON/OFF
		oneBbtn.setLayoutParams(params2);
		oneBbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updatePreference(oneBbtn.isChecked());
				notifyChanged();
				//oldValue is true
			}
		});		
//		oneBbtn.setBackgroundColor(Color.RED);
//		oneBbtn.setText("透明");		
//		SharedPreferences sp  = getSharedPreferences();
//		String radio1Name = sp.getString("radio1", "N/A");		
		

		oneBbtn.setText(radio1name);
		
		
		final RadioButton twoBbtn = new RadioButton(getContext()); // 定义		
		twoBbtn.setLayoutParams(params2);
		twoBbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updatePreference(!twoBbtn.isChecked());
				notifyChanged();
				//oldValue is false
			}
		});
		
		twoBbtn.setText(radio2name);	
//		twoBbtn.setText("墙纸");	
//		twoBbtn.setPadding(55, 0, 55, 0);
		
		if (isTranspValue){
			oneBbtn.setChecked(true);
			twoBbtn.setChecked(false);
		}else{
			oneBbtn.setChecked(false);
			twoBbtn.setChecked(true);
		}
		
		
		LinearLayout tbnLayout = new LinearLayout(getContext()); // 定义layout
		tbnLayout.setLayoutParams(params1);	
		tbnLayout.setPadding(10, 0, 5, 0);
		tbnLayout.setOrientation(LinearLayout.HORIZONTAL);
		tbnLayout.addView(oneBbtn);
		tbnLayout.addView(twoBbtn);		

		
		LinearLayout layout = new LinearLayout(getContext()); // 定义layout
		layout.setPadding(5, 10, 10, 5);
		layout.setOrientation(LinearLayout.VERTICAL);	
		layout.addView(subLayout); // 把tbtn加入到layout
		layout.addView(tbnLayout); // 把view加入到layout
		layout.setId(android.R.id.widget_frame);
		return layout;
	}


	 
	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		Boolean temp = restoreValue ? getPersistedBoolean(false)
				: (Boolean) defaultValue; // 设置初始值，如果没有设置成false，如果有就设置defaultValue
		if (!restoreValue)
			persistBoolean(temp);
		this.isTranspValue = temp; // 把值赋值给默认值
	}

	private void updatePreference(Boolean newValue) {
		SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(getKey(), newValue);
		editor.commit(); // 更新 SharedPreferences 配置文件中的值
		this.isTranspValue = newValue; // 把更新值赋值给默认值
	}
}
