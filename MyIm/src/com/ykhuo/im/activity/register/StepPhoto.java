package com.ykhuo.im.activity.register;

import java.util.Date;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ykhuo.im.R;
import com.ykhuo.im.action.UserAction;
import com.ykhuo.im.bean.TranObject;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.global.Result;
import com.ykhuo.im.util.PhotoUtils;
import com.ykhuo.im.view.HandyTextView;

public class StepPhoto extends RegisterStep implements OnClickListener {

	private ImageView mIvUserPhoto;
	private LinearLayout mLayoutSelectPhoto;
	private LinearLayout mLayoutTakePicture;
	private LinearLayout mLayoutAvatars;

	private View[] mMemberBlocks;
	private String mTakePicturePath;
	private Bitmap mUserPhoto;
	private String mAccount;
	private String mPassword;
	private Date mBirthday;
	private String mName;
	private int mGender;
	private static TranObject mReceivedInfo = null;
	private static boolean mIsReceived = false;

	public StepPhoto(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	

	public void setUserPhoto(Bitmap bitmap) {
		if (bitmap != null) {
			mUserPhoto = bitmap;
			mIvUserPhoto.setImageBitmap(mUserPhoto);
			return;
		}
		showCustomToast("δ��ȡ��ͼƬ");
		mUserPhoto = null;
		mIvUserPhoto.setImageResource(R.drawable.ic_common_def_header);
	}

	public String getTakePicturePath() {
		return mTakePicturePath;
	}

	@Override
	public void initViews() {
		mIvUserPhoto = (ImageView) findViewById(R.id.reg_photo_iv_userphoto);
		mLayoutSelectPhoto = (LinearLayout) findViewById(R.id.reg_photo_layout_selectphoto);
		mLayoutTakePicture = (LinearLayout) findViewById(R.id.reg_photo_layout_takepicture);
	}

	@Override
	public void initEvents() {
		mLayoutSelectPhoto.setOnClickListener(this);
		mLayoutTakePicture.setOnClickListener(this);
	}

	@Override
	public boolean validate() {
		if (mUserPhoto == null) {
			showCustomToast("�����ͷ��");
			return false;
		}
		return true;
	}

	@Override
	public void doNext() {
		putAsyncTask(new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("���Ժ�,�����ύ...");
			}

			@Override
			protected Integer doInBackground(Void... params) {
				try {
					mIsReceived = false;
					mNetService.setupConnection();
					if (!mNetService.isConnected()) {
						return 0;
					} else {
						byte[] photoByte = PhotoUtils.getBytes(mUserPhoto);
						User user = new User(mAccount, mName, mPassword,
								mBirthday, mGender, photoByte);
						UserAction.register(user);
						while (!mIsReceived) {
						}// ���û�յ��Ļ��ͻ�һֱ����;
						mNetService.closeConnection();
						if (mReceivedInfo.getResult() == Result.REGISTER_SUCCESS)
							return 1;
						else
							return 2;
					}
				} catch (Exception e) {
					Log.d("regester", "ע���쳣");

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
						showCustomToast("ע��ɹ�");
						mActivity.finish();
					} else if (result == 2) {
						showCustomToast("ע��ʧ��");
					}
				}
			}

		});
	}

	@Override
	public boolean isChange() {
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_photo_layout_selectphoto:
			PhotoUtils.selectPhoto(mActivity);
			break;

		case R.id.reg_photo_layout_takepicture:
			mTakePicturePath = PhotoUtils.takePicture(mActivity);
			break;
		}
	}

	public Bitmap getPhoto() {
		return mUserPhoto;
	}

	public void setAccount(String account) {
		this.mAccount = account;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public void setGender(int gender) {
		this.mGender = gender;
	}

	public void setBirthday(Date birthday) {
		this.mBirthday = birthday;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	public static void setRegisterInfo(TranObject object, boolean isReceived) {

		mReceivedInfo = object;
		mIsReceived = true;
	}

}
