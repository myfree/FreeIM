package com.ykhuo.im.view;

import com.ykhuo.im.R;
import com.ykhuo.im.util.AudioManager;
import com.ykhuo.im.util.AudioManager.AudioStateListener;
import com.ykhuo.im.util.DialogManager;
import com.ykhuo.im.util.ToolsUtils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class AudioRecorderButton extends Button implements AudioStateListener {
	private static final int DISTANCE_Y_CANCEL=50;//Ĭ��Y�ķ�Χ
	private static final int STATE_NORMAL=1;//Ĭ��״̬
	private static final int STATE_RECORDING=2;//¼��״̬
	private static final int STATE_WANT_TO_CANCEL=3;//ȡ��״̬
	
	private int mCurState=STATE_NORMAL;//��ǰ��¼״̬
	//�Ѿ���ʼ¼��
	private boolean isRecording=false;
	
	private DialogManager mDialogManager;
	
	private AudioManager mAudioManager;
	
	//��ʱ����
	private float mTime;
	//�Ƿ񴥷�longclick
	private boolean mReady;
	
	public AudioRecorderButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public AudioRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mDialogManager=new DialogManager(getContext());
		/**
		 * ���ﻹӦ���ж�SD���Ƿ���ڣ��Ƿ�ɶ�
		 */
		String dir=ToolsUtils.getFileUrl();

		mAudioManager=AudioManager.getInstance(dir);
		mAudioManager.setOnAudioStateListener(this);
		
		setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				mReady=true;
				mAudioManager.prepareAudio();
				return false;
			}
		});
	}
	
	/**
	 * ¼����ɺ�Ļص�
	 * @author zhouwen
	 *
	 */
	public interface AudioFinishRecorderListener{
		void onFinish(float seconds,String filePath);
	}
	
	private AudioFinishRecorderListener mListener;
	
	public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener){
		mListener=listener;
	}
	
	
	
	/**
	 * ��ȡ������С��Runnable
	 */
	private Runnable mGetVoiceLevelRunnable=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isRecording){
				try {
					Thread.sleep(100);
					mTime+=0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	};
	
	private static final int MSG_AUDIO_PREPARED=0x110;
	private static final int MSG_VOICE_CHANGED=0x111;
	private static final int MSG_DIALOG_DIMISS=0x112;
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
				case MSG_AUDIO_PREPARED:
					//TODO ������ʾӦ����audio end prepared�Ժ�
					mDialogManager.showRecordingDialog();
					isRecording=true;
					
					new Thread(mGetVoiceLevelRunnable).start();
					
					break;
				case MSG_VOICE_CHANGED:
					mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
					break;
				case MSG_DIALOG_DIMISS:
					mDialogManager.dimissDialog();
					break;
				
			}
		};
	};
	
	
	/**
	 * ��ȫ׼�����һ���ص�
	 */
	@Override
	public void wellPrepared() {
		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action=event.getAction();
		int x=(int)event.getX();
		int y=(int)event.getY();
		switch(action){
			case MotionEvent.ACTION_DOWN:
				changeState(STATE_RECORDING);
				break;
			case MotionEvent.ACTION_MOVE:
				//�ж��Ƿ��Ѿ���ʼ¼��
				if(isRecording){
					
				}
				
				//����X��Y�����꣬�ж��Ƿ���Ҫȡ��
				if(wantToCancel(x,y)){
					changeState(STATE_WANT_TO_CANCEL);
				}else{
					changeState(STATE_RECORDING);
				}
				break;
			case MotionEvent.ACTION_UP:
				if(!mReady){
					reset();
					return super.onTouchEvent(event);
				}
				if(!isRecording||mTime<0.75f){
					mDialogManager.tooShort();
					mAudioManager.cancel();
					mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);//�ӳ�
				}else if(mCurState==STATE_RECORDING){//����¼�ƽ���
					mDialogManager.dimissDialog();
					mAudioManager.release();
					
					if(mListener!=null){
						mListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
					}
					//release
					//callbackToAct
				}else if(mCurState==STATE_WANT_TO_CANCEL){
					mDialogManager.dimissDialog();
					mAudioManager.cancel();
					//cancel
				}
				
				reset();
				break;
		}
		
		
		
		
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	
	/**
	 * �ظ�״̬����־λ
	 */
	private void reset() {
		// TODO Auto-generated method stub
		isRecording=false;
		mReady=false;
		mTime=0;
		changeState(STATE_NORMAL);
	}

	private boolean wantToCancel(int x, int y) {
		if(x<0||x>getWidth()){
			return true;
		}
		if(y<-DISTANCE_Y_CANCEL||y>getHeight()+DISTANCE_Y_CANCEL){
			return true;
		}
		return false;
	}

	/**
	 * �ı�button���ı�
	 * @param stateRecording
	 */
	private void changeState(int state) {
		// TODO Auto-generated method stub
		if(mCurState!=state){
			mCurState=state;
			switch(state){
				case STATE_NORMAL:
					setBackgroundResource(R.drawable.btn_recorder_normal);
					setText(R.string.str_recorder_normal);
				break;
				case STATE_RECORDING:
					setBackgroundResource(R.drawable.btn_recording);
					setText(R.string.str_recorder_recording);
					if(isRecording){
						mDialogManager.recording();
					}
				break;
				case STATE_WANT_TO_CANCEL:
					setBackgroundResource(R.drawable.btn_recording);
					setText(R.string.str_recorder_cancel);
					mDialogManager.wantToCancel();
				break;
			}
		}
	}

}
