package com.zhuoyu.view.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhuoyu.util.Constant;
import com.zhuoyu.util.ZhuoyuUIUtil;
import com.zhuoyu.view.R;
import com.zhuoyu.view.SportActivity;
import com.zhuoyu.view.R.string;

/**
 * 2个 ToggleButton
 * @author Administrator
 *
 */
public class ToggleButton2Preference extends Preference  {
	private Boolean oldValue = true; // 默认情况是true

	public ToggleButton2Preference(Context context) {
		super(context);
	}

	public ToggleButton2Preference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ToggleButton2Preference(Context context, AttributeSet attrs,
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
		params2.leftMargin = 50;
		final ToggleButton oneBbtn = new ToggleButton(getContext()); // 定义		
		// ToggleButton
		// 用于设置ON/OFF
		oneBbtn.setLayoutParams(params2);
		oneBbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updatePreference(oneBbtn.isChecked());
				notifyChanged();
			}
		});
		oneBbtn.setChecked(oldValue);
		oneBbtn.setText(oneBbtn.isChecked()? "NO":"OFF"); //统一用英文
		Constant.autoSvrFlag = oneBbtn.isChecked(); 
//		tbtn.setBackgroundColor(Color.RED);
		


		final ToggleButton twoBbtn = new ToggleButton(getContext()); // 定义		
		// ToggleButton
		// 用于设置ON/OFF
		twoBbtn.setLayoutParams(params2);
		twoBbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				twoBbtn.setChecked(true);
				notifyChanged();
				Intent intent = new Intent(getContext(),SportActivity.class);
				intent.setAction(Constant.MAIN_ACTION);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().startActivity(intent);
//				Toast.makeText(getContext(), "You Press click", Toast.LENGTH_SHORT).show();
			}
		});
		twoBbtn.setChecked(true); //体验按钮永远点亮
		twoBbtn.setText(R.string.experience);
		twoBbtn.setWidth(58);	
		
		LinearLayout tbnLayout = new LinearLayout(getContext()); // 定义layout
		tbnLayout.setLayoutParams(params1);	
		tbnLayout.setPadding(10, 0, 5, 0);
		tbnLayout.setOrientation(LinearLayout.HORIZONTAL);
		tbnLayout.addView(oneBbtn);
		tbnLayout.addView(twoBbtn);				
		
		LinearLayout layout = new LinearLayout(getContext()); // 定义layout
//		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.setPadding(5, 10, 10, 5);
		layout.setOrientation(LinearLayout.VERTICAL);	
		layout.addView(subLayout); // 把view加入到layout
		layout.addView(tbnLayout); // 把view加入到layout
		layout.setId(android.R.id.widget_frame);
		return layout;
	}


	/**
	 @Override
	    protected View onCreateView(ViewGroup parent) {


	        LinearLayout layout = new LinearLayout(getContext());  //定义layout


	        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
	                LinearLayout.LayoutParams.WRAP_CONTENT,
	                LinearLayout.LayoutParams.WRAP_CONTENT);
	        params1.weight = 1.0f;


	        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
	                LinearLayout.LayoutParams.WRAP_CONTENT,
	                LinearLayout.LayoutParams.WRAP_CONTENT);
	        params2.gravity = Gravity.RIGHT;


	        layout.setPadding(5, 5, 5, 5);
	        layout.setOrientation(LinearLayout.HORIZONTAL);


	        TextView view = new TextView(getContext());     //定义 Textview用于显示标题
	        view.setText(getTitle());
	        view.setLayoutParams(params1);
	        view.setHeight(40);
	        view.setGravity(Gravity.CENTER_VERTICAL);
	        view.setBackgroundColor(Color.BLACK);


	        final ToggleButton tbtn = new ToggleButton(getContext());  //定义 ToggleButton 用于设置ON/OFF
	        tbtn.setLayoutParams(params2);
	        tbtn.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                updatePreference(tbtn.isChecked());
	                notifyChanged();
	            }
	        });
	        tbtn.setChecked(oldValue);
	        tbtn.setBackgroundColor(Color.RED);

	        layout.addView(view);    //把view加入到layout
	        layout.addView(tbtn);     //把tbtn加入到layout
	        layout.setId(android.R.id.widget_frame);


	        return layout;
	    }
*/
	 
	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		Boolean temp = restoreValue ? getPersistedBoolean(false)
				: (Boolean) defaultValue; // 设置初始值，如果没有设置成false，如果有就设置defaultValue
		if (!restoreValue)
			persistBoolean(temp);
		this.oldValue = temp; // 把值赋值给默认值
	}

	private void updatePreference(Boolean newValue) {
		SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(getKey(), newValue);
		editor.commit(); // 更新 SharedPreferences 配置文件中的值
		this.oldValue = newValue; // 把更新值赋值给默认值
	}
}
