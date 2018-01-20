package com.ykhuo.im.activity;

import com.ykhuo.im.R;
import com.ykhuo.im.activity.LoginActivity;
import com.ykhuo.im.util.SpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	protected static final String TAG = "WelcomeActivity";
	private Context mContext;
	private ImageView mImageView;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		mContext = this;
		findView();
		init();
	}
	
	private void findView() {
		mImageView = (ImageView) findViewById(R.id.iv_welcome);
	}

	private void init() {
		
		//延迟ＵＩ操作，为了多显示会welcome页面，不然一下子就跳走了
		mImageView.postDelayed(new Runnable() {
			@Override
			public void run() {
				SpUtil.getInstance();
				sp = SpUtil.getSharePerference(mContext);
				SpUtil.getInstance();
				boolean isFirst = SpUtil.isFirst(sp);
				if (!isFirst) {
					SpUtil.getInstance();
					SpUtil.setBooleanSharedPerference(sp,
							"isFirst", true);
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}
		},2000);
		
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
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
