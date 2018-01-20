package com.ykhuo.im.activity;

import com.ykhuo.im.activity.LoginActivity;
import com.ykhuo.im.activity.MainActivity;
import com.ykhuo.im.fragment.FriendListFragment;
import com.ykhuo.im.fragment.MessageFragment;
import com.ykhuo.im.fragment.UserInfoFragment;
import com.ykhuo.im.network.NetService;
import com.ykhuo.im.util.FaceConversionUtil;
import com.ykhuo.im.util.ToolsUtils;
import com.ykhuo.im.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends FragmentActivity{
	protected static final String TAG = "MainActivity";
	private Context mContext;
	private ImageButton mNews,mConstact,mSetting;
	private View mPopView;
	private View currentButton;
	
	private TextView app_cancle;
	private TextView app_exit;
	private TextView app_change;
	
	private PopupWindow mPopupWindow;
	private LinearLayout buttomBarGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext=this;
		System.out.println("初始化Main");
		findView();
		init();
		initEmoji();//初始化读取表情
	}
	
	private void findView(){
		mPopView=LayoutInflater.from(mContext).inflate(R.layout.app_exit, null);
		buttomBarGroup=(LinearLayout) findViewById(R.id.buttom_bar_group);
		mNews=(ImageButton) findViewById(R.id.buttom_news);
		mConstact=(ImageButton) findViewById(R.id.buttom_constact);
		mSetting=(ImageButton) findViewById(R.id.buttom_setting);
		
		app_cancle=(TextView) mPopView.findViewById(R.id.app_cancle);
		app_change=(TextView) mPopView.findViewById(R.id.app_change_user);
		app_exit=(TextView) mPopView.findViewById(R.id.app_exit);
	}
	
	private void init(){
		mNews.setOnClickListener(newsOnClickListener);
		mConstact.setOnClickListener(constactOnClickListener);
		mSetting.setOnClickListener(settingOnClickListener);
		
		//模拟点击
		mConstact.performClick();
		
		mPopupWindow=new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		
		app_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		
		app_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				((Activity)mContext).overridePendingTransition(R.anim.activity_up, R.anim.fade_out);
				NetService.getInstance().closeConnection();
				finish();
			}
		});
		
		app_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NetService.getInstance().closeConnection();
				finish();
			}
		});
	}
	
	private OnClickListener newsOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			MessageFragment messageFragment=new MessageFragment();
			ft.replace(R.id.fl_content, messageFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
		}
	};
	
	private OnClickListener constactOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			FriendListFragment constactFatherFragment=new FriendListFragment();
			ft.replace(R.id.fl_content, constactFatherFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener settingOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			UserInfoFragment settingFragment=new UserInfoFragment();
			ft.replace(R.id.fl_content, settingFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private void setButton(View v){
		if(currentButton!=null&&currentButton.getId()!=v.getId()){
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton=v;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_MENU){
			mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));
			mPopupWindow.showAtLocation(buttomBarGroup, Gravity.BOTTOM, 0, 0);
			mPopupWindow.setAnimationStyle(R.style.app_pop);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			mPopupWindow.update();
		}
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {    
			
			//模拟HOME效果
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;  
       }
		
		return super.onKeyDown(keyCode, event);
		
	}
	
	private void initEmoji(){
		new Thread(new Runnable() {
            @Override
            public void run() {
                FaceConversionUtil.getInstace().getFileText(getApplication());
            }
        }).start();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
