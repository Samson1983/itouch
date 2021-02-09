package com.zhuoyu.view.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.waps.AppConnect;

import com.zhuoyu.util.ZhuoyuUIUtil;
import com.zhuoyu.view.R;
import com.zhuoyu.view.TabHostSample;

/**
 * 1个button
 * @author Administrator
 *
 */
public class ButtonPreference extends Preference  {
	private Boolean oldValue = false; // 默认情况是false

	public ButtonPreference(Context context) {
		super(context);
	}

	public ButtonPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ButtonPreference(Context context, AttributeSet attrs,
			int defStyle) {
        super(context, attrs, defStyle);
        
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);		
		params1.weight = 1.0f;
		TextView title = new TextView(getContext()); // 定义 Textview用于显示标题
		title.setText(getTitle());
		ZhuoyuUIUtil.setTitleSize(title); //设置字体大小、颜色等
		title.setLayoutParams(params1);
		title.setGravity(Gravity.CENTER_VERTICAL);
		

		TextView summary = new TextView(getContext()); // 定义 summary用于显示标题
		summary.setLayoutParams(params1);
		summary.setText(getSummary());
		ZhuoyuUIUtil.setSummarySize(summary); //设置字体大小
		summary.setGravity(Gravity.CENTER_VERTICAL);
//		summary.setBackgroundColor(Color.BLUE);
		
		LinearLayout subLayout = new LinearLayout(getContext()); // 定义layout
		subLayout.setLayoutParams(params1);	
		subLayout.setPadding(10, 0, 5, 0);
		subLayout.setOrientation(LinearLayout.VERTICAL);
		subLayout.addView(title);
		subLayout.addView(summary);
				
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params2.gravity = Gravity.RIGHT;
		final Button tbtn = new Button(getContext()); // 定义		
		// ToggleButton
		// 用于设置ON/OFF
		tbtn.setLayoutParams(params2);
		tbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				updatePreference(tbtn.isChecked());
//				notifyChanged();
				Toast.makeText(getContext(), "You Press click", Toast.LENGTH_SHORT).show();
			}
		});
//		tbtn.setChecked(oldValue);
		tbtn.setText(R.string.experience);
		tbtn.setWidth(58);
//		tbtn.setBackgroundColor(Color.RED);
		
		LinearLayout layout = new LinearLayout(getContext()); // 定义layout
		
//		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.setPadding(5, 10, 10, 5);
		layout.setOrientation(LinearLayout.HORIZONTAL);	
		layout.addView(subLayout); // 把view加入到layout
		layout.addView(tbtn); // 把tbtn加入到layout
		layout.setId(android.R.id.widget_frame);
		return layout;
	}

 
	 
	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		Boolean temp = restoreValue ? getPersistedBoolean(false)
				: (Boolean) defaultValue; // 设置初始值，如果没有设置成false，如果有就设置defaultValue
		if (!restoreValue)
			persistBoolean(temp);
		this.oldValue = temp; // 把值赋值给默认值
	}

//	private void updatePreference(Boolean newValue) {
//		SharedPreferences.Editor editor = getEditor();
//		editor.putBoolean(getKey(), newValue);
//		editor.commit(); // 更新 SharedPreferences 配置文件中的值
//		this.oldValue = newValue; // 把更新值赋值给默认值
//	}
}
