package com.ykhuo.im.activity.register;

import java.io.IOException;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ykhuo.im.R;
import com.ykhuo.im.action.UserAction;
import com.ykhuo.im.bean.TranObject;
import com.ykhuo.im.global.Result;
import com.ykhuo.im.util.VerifyUtils;
import com.ykhuo.im.view.HandyTextView;

public class StepAccount extends RegisterStep implements TextWatcher {

	private EditText mEtAccount;
	private HandyTextView mHtvNotice;

	private static String mAccount;
	private static boolean mIsChange = true;

	private static TranObject mReceivedInfo = null;
	private static boolean mIsReceived = false;

	public StepAccount(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	public String getAccount() {
		return mAccount;
	}

	@Override
	public void initViews() {
		mEtAccount = (EditText) findViewById(R.id.reg_account_et_account);
		mHtvNotice = (HandyTextView) findViewById(R.id.reg_account_htv_notice);
	}

	@Override
	public void initEvents() {
		System.out.println(mEtAccount);
		mEtAccount.addTextChangedListener(this);
	}

	@Override
	public void doNext() {
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("������֤�˺�,���Ժ�...");
			}

			@Override
			protected Integer doInBackground(Void... params) {
				try {
					mNetService.closeConnection();
					System.out.println("setupConnection");
					mNetService.setupConnection();
					if (!mNetService.isConnected()) {
						return 0;
					}
					UserAction.accountVerify(mAccount);
					while (!mIsReceived) {
						System.out.println("������");
					}// ���û�յ��Ļ��ͻ�һֱ����;
					mNetService.closeConnection();
					System.out.println(mReceivedInfo.getResult());
					if (mReceivedInfo.getResult() == Result.ACCOUNT_EXISTED)		
						return 1;// �����û����Ѵ���
					else if(mReceivedInfo.getResult() == Result.ACCOUNT_CAN_USE)
						return 2;// �����û�������
				} catch (IOException e) {
					Log.d("register", "ע���˺��쳣");
				}
				return 0;
				
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				if (result == 0) {
					showCustomToast("�������쳣");
				} else {
					if (result == 1) {
						showCustomToast("���˺��ѱ�ע��");
					} else if (result == 2) {
						mIsChange = false;
						showCustomToast("���˺ſ���");
						mOnNextActionListener.next();
					}
				}
				mReceivedInfo = null;
				mIsReceived = false;
			}
		}.execute();
	}

	@Override
	public boolean validate() {
		mAccount = null;
		if (VerifyUtils.isNull(mEtAccount)) {
			showCustomToast("����д�˺�");
			mEtAccount.requestFocus();
			return false;
		}
		String account = mEtAccount.getText().toString().trim();
		if (VerifyUtils.matchAccount(account)) {
			mAccount = account;
			return true;
		}
		showCustomToast("�˺Ÿ�ʽ����ȷ");
		mEtAccount.requestFocus();
		return false;
	}

	@Override
	public boolean isChange() {
		return mIsChange;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mIsChange = true;
		if (s.toString().length() > 0) {
			mHtvNotice.setVisibility(View.VISIBLE);
			char[] chars = s.toString().toCharArray();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < chars.length; i++) {
				buffer.append(chars[i] + "");
			}
			mHtvNotice.setText(buffer.toString());
		} else {
			mHtvNotice.setVisibility(View.GONE);
		}
	}
	public static void setRegisterInfo(TranObject object, boolean isReceived) {

		mReceivedInfo = object;
		mIsReceived = isReceived;
	}


}
