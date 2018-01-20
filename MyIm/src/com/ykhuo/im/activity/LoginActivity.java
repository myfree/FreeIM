package com.ykhuo.im.activity;

import java.io.IOException;
import java.util.List;

import com.ykhuo.im.network.NetService;
import com.ykhuo.im.action.UserAction;
import com.ykhuo.im.activity.LoginActivity;
import com.ykhuo.im.activity.MainActivity;
import com.ykhuo.im.activity.register.RegisterActivity;
import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.databse.ImDB;
import com.ykhuo.im.global.Result;
import com.ykhuo.im.util.VerifyUtils;
import com.ykhuo.im.BaseActivity;
import com.ykhuo.im.R;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class LoginActivity extends BaseActivity {
	
	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLoginButton;
	private Button mRegisterButton;
	private EditText mAccount;
	private EditText mPassword;;
	
	private NetService mNetService = NetService.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mContext = this;
		initViews();
		initEvents();
		
	}
	
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		mLoginButton = (Button) findViewById(R.id.login);
		mRegisterButton = (Button) findViewById(R.id.register);
		mAccount = (EditText) findViewById(R.id.account);
		mPassword = (EditText) findViewById(R.id.password);
		
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		mLoginButton.setOnClickListener(loginOnClickListener);
		mRegisterButton.setOnClickListener(registerOnClickListener);

	}

	private OnClickListener loginOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String account = mAccount.getText().toString().trim();
			String password = mPassword.getText().toString().trim();
			if (account.equals("")) {
				showCustomToast("ÇëÌîÐ´ÕËºÅ");
				mAccount.requestFocus();
			} else if (password.equals("")) {
				showCustomToast("ÇëÌîÐ´ÃÜÂë");
			} else if (!VerifyUtils.matchAccount(account)) {
				showCustomToast("ÕËºÅ¸ñÊ½´íÎó");
				mAccount.requestFocus();
			} else if (mPassword.length() < 6) {
				showCustomToast("ÃÜÂë¸ñÊ½´íÎó");
			} else {
				tryLogin(account, password);
			}
		}
	};

	private void tryLogin(final String username, final String password) {
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("ÕýÔÚµÇÂ¼,ÇëÉÔºó...");
			}

			@Override
			protected Integer doInBackground(Void... params) {
				try {
					
					mNetService.closeConnection();
					mNetService.onInit(LoginActivity.this);
					mNetService.setupConnection();
					Log.d("network", "set up connection");
					if (!mNetService.isConnected()) {
						return 0;
					}
					
					User user = new User();
					user.setUsername(username);
					user.setPassword(password);
					UserAction.loginVerify(user);
					ApplicationData data = ApplicationData.getInstance();
					data.initData(LoginActivity.this);
					data.start();
					System.out.println(data.getReceivedMessage().getResult());
					if (data.getReceivedMessage().getResult() == Result.LOGIN_SUCCESS){
						
						return 1;// µÇÂ¼³É¹¦
					}else if(data.getReceivedMessage().getResult() == Result.LOGIN_FAILED)
						return 2;// µÇÂ¼Ê§°Ü
				} catch (IOException e) {
					Log.d("network", "IOÒì³£");
				}
				return 0;

			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				if (result == 0) {
					showCustomToast("·þÎñÆ÷Òì³£");
				} else {
					if (result == 2) {
						showCustomToast("µÇÂ¼Ê§°Ü");
					} else if (result == 1) {
						Intent intent = new Intent(mContext, MainActivity.class);
						startActivity(intent);
						finish();
					}
				}
			}
		}.execute();

	}
	
	private OnClickListener registerOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, RegisterActivity.class);
			startActivity(intent);
		}
	};
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
