package com.ykhuo.im.util;

import com.ykhuo.im.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogManager {
	private Dialog mDialog;
	
	private ImageView mIcon;
	private ImageView mVoice;
	
	private TextView mLable;
	
	private Context mContext;
	
	public DialogManager(Context context){
		this.mContext=context;
	}
	
	/**
	 * 显示录音对话框
	 */
	public void showRecordingDialog(){
		mDialog=new Dialog(mContext,R.style.Theme_AudioDialog);
		LayoutInflater inflater=LayoutInflater.from(mContext);
		View view=inflater.inflate(R.layout.dialog_recorder, null);
		mDialog.setContentView(view);
		
		mIcon=(ImageView) mDialog.findViewById(R.id.id_recorder_dialog_icon);
		mVoice=(ImageView) mDialog.findViewById(R.id.id_recorder_dialog_voice);
		mLable=(TextView) mDialog.findViewById(R.id.id_recorder_dialog_label);
		
		mDialog.show();
	}
	
	/**
	 * 正在录音时的显示
	 */
	public void recording(){
		if(mDialog!=null&&mDialog.isShowing()){
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setImageResource(R.drawable.recorder);
			mLable.setText("手指上滑，取消发送");
		}
	}
	
	
	/**
	 * 显示Cancel对话框
	 */
	public void wantToCancel(){
		if(mDialog!=null&&mDialog.isShowing()){
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setImageResource(R.drawable.cancel);
			mLable.setText("松开手指，取消发送");
		}
	}
	
	/**
	 * 显示tooShort对话框
	 */
	public void tooShort(){
		if(mDialog!=null&&mDialog.isShowing()){
			mIcon.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setImageResource(R.drawable.voice_to_short);
			mLable.setText("录音时间过短");
		}
	}
	
	/**
	 * 隐藏对话框
	 */
	public void dimissDialog(){
		if(mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
			mDialog=null;
		}
	}
	
	/**
	 * 更新对话框的音量
	 * 通过level去更新voice上的图片
	 * @param level
	 */
	public void updateVoiceLevel(int level){
		if(mDialog!=null&&mDialog.isShowing()){
			//mIcon.setVisibility(View.VISIBLE);
			//mVoice.setVisibility(View.VISIBLE);
			//mLable.setVisibility(View.VISIBLE);
			
			int resId=mContext.getResources().getIdentifier("v"+level,"drawable" , mContext.getPackageName());
			mVoice.setImageResource(resId);
		}
	}
	
}
