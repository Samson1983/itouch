package com.zhuoyu.view.preference;

import java.io.File;

import com.zhuoyu.util.ZhuoyuUIUtil;
import com.zhuoyu.view.R;
import com.zhuoyu.view.SettingActivity;
import com.zhuoyu.view.R.drawable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Environment;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 2个 ImageButton
 * @author Administrator
 *
 */
public class ImageButton2Preference extends Preference  {
	private Boolean oldValue = false; // 默认情况是false

	public ImageButton2Preference(Context context) {
		super(context);
	}

	public ImageButton2Preference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageButton2Preference(Context context, AttributeSet attrs,
			int defStyle) {
        super(context, attrs, defStyle);
        
	}
	
	/**
	 * 图片背景效果
	 * @param v
	 * @param event
	 */
	public void setBkg(View v) {
		BitmapDrawable bd = (BitmapDrawable)((ImageView) v).getDrawable();
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(), bd.getIntrinsicHeight(), Config.ARGB_8888);
//		System.out.println(bd.getIntrinsicWidth()+","+bd.getIntrinsicHeight());
		Paint p = new Paint();
		p.setColor(Color.CYAN);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b.extractAlpha(), 0, 0, p);	        		
		StateListDrawable sld = new StateListDrawable();
		sld.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(bitmap));
		
		v.setBackgroundDrawable(sld);	
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
		params2.leftMargin = 10;
		final ImageView oneBbtn = new ImageView(getContext()); // 定义	
		oneBbtn.setId(1);
		oneBbtn.setLayoutParams(params2);
		ZhuoyuUIUtil.setImageResource(getContext(), oneBbtn,oneBbtn.getId(), R.drawable.ball_man); //设置资源文件
		oneBbtn.setBackgroundColor(Color.TRANSPARENT);
		/**光晕一定要setPadding  setClickable*/
		oneBbtn.setPadding(3, 3, 3, 3); 
		oneBbtn.setClickable(true);
		oneBbtn.setScaleType(ScaleType.FIT_CENTER);		
		
//		oneBbtn.setOnLongClickListener(l);
		oneBbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setBkg(v); //按下效果
				SettingActivity.ShowPickDialog(getContext(),v); //相册
//				Toast.makeText(getContext(), "You Press click", Toast.LENGTH_SHORT).show();	
				
			}
		});
		
		/**
		oneBbtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) { //按下 
//		        	v.setBackgroundColor(Color.YELLOW);
					setBkg(v, event); //按下效果
					SettingActivity.ShowPickDialog(getContext(),v); //相册
					Toast.makeText(getContext(), "You Press click", Toast.LENGTH_SHORT).show();
		        	
		        } else if (event.getAction() == MotionEvent.ACTION_UP) {//放开 
		        	v.setBackgroundColor(Color.TRANSPARENT);
		        }
				
	            
				return false;
			}

		}); */
		
		
		


		final ImageButton twoBbtn = new ImageButton(getContext()); // 定义	
		twoBbtn.setId(2);
		twoBbtn.setLayoutParams(params2);
		ZhuoyuUIUtil.setImageResource(getContext(), twoBbtn,twoBbtn.getId(),R.drawable.ball_wife_son); //设置资源文件
		twoBbtn.setBackgroundColor(Color.TRANSPARENT);
		/**光晕一定要setPadding  setClickable*/
		twoBbtn.setPadding(3, 3, 3, 3);  
		twoBbtn.setClickable(true);
		twoBbtn.setScaleType(ScaleType.FIT_CENTER);
		twoBbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setBkg(v); //按下效果
				SettingActivity.ShowPickDialog(getContext(),v); //相册
//				Toast.makeText(getContext(), "You Press click 2", Toast.LENGTH_SHORT).show();	
				
			}
		});
		
		/**
		twoBbtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) { //按下 
//		        	v.setBackgroundColor(Color.YELLOW);
					setBkg(v, event); //按下效果
					SettingActivity.ShowPickDialog(getContext(),v);//相册
					Toast.makeText(getContext(), "You Press click 2", Toast.LENGTH_SHORT).show();
		        	
		        } else if (event.getAction() == MotionEvent.ACTION_UP) {//放开 
		        	v.setBackgroundColor(Color.TRANSPARENT);
		        }
				return false;
			}

		 
		}); */
		
		LinearLayout.LayoutParams tbnParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);	
		LinearLayout twoLayout = new LinearLayout(getContext()); // 定义layout
		twoLayout.setLayoutParams(tbnParams);
		twoLayout.setOrientation(LinearLayout.HORIZONTAL);
		twoLayout.addView(oneBbtn);
		twoLayout.addView(twoBbtn);				
		twoLayout.setBackgroundColor(Color.LTGRAY);

		
		TextView oneTxt = new TextView(getContext());
//		oneTxt.setText("泡泡1");
		oneTxt.setPadding(60, 0, 0, 0);
		TextView twoTxt = new TextView(getContext());
//		twoTxt.setText("泡泡2");
		twoTxt.setPadding(110, 0, 0, 0);
		
		LinearLayout.LayoutParams threeParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);	
		LinearLayout threeLayout = new LinearLayout(getContext()); // 定义layout
		threeLayout.setLayoutParams(threeParams);
		threeLayout.setOrientation(LinearLayout.HORIZONTAL);
		threeLayout.addView(oneTxt);
		threeLayout.addView(twoTxt);				
		threeLayout.setBackgroundColor(Color.LTGRAY);
		
		
		
		
		LinearLayout layout = new LinearLayout(getContext()); // 定义layout
//		layout.setPadding(5, 10, 10, 5);
		layout.setOrientation(LinearLayout.VERTICAL);	
		layout.addView(subLayout); // 把view加入到layout
		layout.addView(twoLayout); // 把view加入到layout
		layout.addView(threeLayout); // 把view加入到layout
//		layout.setBackgroundColor(Color.BLACK);
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

	private void updatePreference(Boolean newValue) {
		SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(getKey(), newValue);
		editor.commit(); // 更新 SharedPreferences 配置文件中的值
		this.oldValue = newValue; // 把更新值赋值给默认值
	}
}
