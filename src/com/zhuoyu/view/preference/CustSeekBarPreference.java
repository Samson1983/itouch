package com.zhuoyu.view.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zhuoyu.util.Constant;
import com.zhuoyu.util.ZhuoyuUIUtil;
import com.zhuoyu.view.TabHostSample;

/**
 * 带限制、红字
 * 带备注summary
 * @author Administrator
 *
 */
public class CustSeekBarPreference extends Preference implements 
OnSeekBarChangeListener {

	public static int maximum = 8; // 由于目前是针对字体的所有最大值设成 8
	public static int interval = 1; // 按照 1增长

	private int oldValue = 3;
	private TextView monitorBox;

	public CustSeekBarPreference(Context context) {

		super(context);
	}

	public CustSeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CustSeekBarPreference(Context context, AttributeSet attrs,
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
		TabHostSample.summary = summary;  //赋值动态更新
		summary.setLayoutParams(params1);
		summary.setSingleLine(); //单行显示
		summary.setText( Constant.unLock ? "" : getSummary() );
		summary.setTextSize(12); //设置字体大小
		summary.setTextColor(Color.RED);
		summary.setGravity(Gravity.CENTER_VERTICAL);
		
		LinearLayout subLayout = new LinearLayout(getContext()); // 定义layout
		subLayout.setLayoutParams(params1);	
		subLayout.setPadding(10, 0, 5, 0);
		subLayout.setOrientation(LinearLayout.VERTICAL);
		subLayout.addView(title);
		subLayout.addView(summary);
				
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				100,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params2.gravity = Gravity.RIGHT;
		
		SeekBar bar = new SeekBar(getContext());
		bar.setMax(maximum);
		bar.setProgress(this.oldValue);
		bar.setLayoutParams(params2);
		bar.setOnSeekBarChangeListener(this); //处理
		bar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				System.out.println("8888888888"+Constant.unLock);
				
				boolean unOrder = true; //true不成功,false成功
				
				//为了放过ACTION_DOWN\ACTION_MOVIE,否则进度条拖不动
				if (Constant.unLock){
					 //已订购
					unOrder = false;
				}
//				System.out.println("8888888888---"+Constant.unLock);
			
				
				if(event.getAction() == MotionEvent.ACTION_UP){					
					if (!Constant.unLock){
						TabHostSample.tbSA.selecterDialog();
					}else{
					  //已订购
						unOrder = false;
						TabHostSample.summary.setText(""); //去掉需要积分
					}
					
					return unOrder; 	
					
				}
				
					

				return unOrder; 
				
			}
		});
		
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params3.gravity = Gravity.RIGHT;			
		
		this.monitorBox = new TextView(getContext());
		this.monitorBox.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
		this.monitorBox.setLayoutParams(params3);
		this.monitorBox.setPadding(5, 5, 5, 0);
		this.monitorBox.setText(bar.getProgress() + "");

		
		LinearLayout layout = new LinearLayout(getContext()); // 定义layout
		layout.setPadding(5, 10, 10, 5);
		layout.setOrientation(LinearLayout.HORIZONTAL);	
		layout.addView(subLayout); // 把view加入到layout
		layout.addView(bar); // 把bar加入到layout
		layout.addView(this.monitorBox); //显示进度条的值
		layout.setId(android.R.id.widget_frame);
		return layout;
		
/**		// 定义布局
		LinearLayout layout = new LinearLayout(getContext());
		layout.setPadding(15, 5, 2, 5);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		
		// 定义属性1
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.WRAP_CONTENT,
		LinearLayout.LayoutParams.WRAP_CONTENT);
		params1.gravity = Gravity.CENTER_VERTICAL; // 属性1的对齐方式 这里 , Gravity.CENTER_VERTICA, 竖直方向居中主要是处理SeekBar的值改变时能够很好的跟着做居中的变化
		params1.weight = 1.0f;

		// 定义属性2
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				100,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params2.gravity = Gravity.RIGHT;
		// 定义属性2

		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params3.gravity = Gravity.RIGHT;



		TextView view = new TextView(getContext());
		view.setText(getTitle());
		QQUIUtil.setTitleSize(view);
//		view.setTextSize(18);
//		view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		view.setGravity(Gravity.LEFT);
		view.setLayoutParams(params1);

		SeekBar bar = new SeekBar(getContext());
		bar.setMax(maximum);
		bar.setProgress(this.oldValue);
		bar.setLayoutParams(params2);
		bar.setOnSeekBarChangeListener(this); //处理
		this.monitorBox = new TextView(getContext());

		this.monitorBox.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
		this.monitorBox.setLayoutParams(params3);
		this.monitorBox.setPadding(5, 5, 5, 0);
		this.monitorBox.setText(bar.getProgress() + "");
		
//		this.monitorBox.setTextSize(bar.getProgress()); // 设置 monitorBox 的字体大小为
														// SeekBar的值

		layout.addView(view);
		layout.addView(bar);
		layout.addView(this.monitorBox);
		layout.setId(android.R.id.widget_frame);

		return layout;*/
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

//		progress = Math.round(((float) progress) / interval) * interval;
//
//		if (progress < 10) // 当值小于 10 时 就把SeekBar的值设置成10 主要是因为字体小于10的话 基本是看不到了
//		{
//
//			progress = 10;
//		} else if (progress > 72) // 当值大于 72 时 就把SeekBar的值设置成72
//									// 主要是因为SeekBar如果设成最大值中间的阀只剩一半的比较难看(个人认为)
//
//		{
//			progress = 72;
//		}
//
//		if (!callChangeListener(progress)) {
//
//			seekBar.setProgress((int) this.oldValue);
//			return;
//
//		}

//		seekBar.setProgress(progress);

		System.out.println("----------onProgressChanged");
		
		if (progress ==0){ //最小是1
			progress = 1;
		}
		this.oldValue = progress;
		this.monitorBox.setText(progress + "");
//		this.monitorBox.setTextSize(progress*2); // 设置 monitorBox 的字体大小跟着SeekBar变化
		updatePreference(progress);

//		notifyChanged();  //自动会刷新不需要调用
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	protected Object onGetDefaultValue(TypedArray ta, int index) {

		int dValue = (int) ta.getInt(index, oldValue);

		return dValue;				
//		return validateValue(dValue);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

		int temp = restoreValue ? getPersistedInt(oldValue) : (Integer) defaultValue;

		if (!restoreValue)
			persistInt(temp);

		this.oldValue = temp;
	}

//	private int validateValue(int value) {
//
//		if (value > 72)
//			value = 72;
//
//		else if (value < 10)
//			value = 10;
//
//		else if (value % interval != 0)
//			value = Math.round(((float) value) / interval) * interval;
//
//		return value;
//	}

	private void updatePreference(int newValue) {

		SharedPreferences.Editor editor = getEditor();

		editor.putInt(getKey(), newValue);
		editor.commit();
	}

}