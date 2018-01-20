package com.ykhuo.im.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;

public class AudioManager {
	private MediaRecorder mMediaRecorder;
	private String mDir;  //�ļ�������
	private String mCurrentFilePath; 
	
	private static AudioManager mInstance;
	
	private boolean isPrepared;
	
	private AudioManager(String dir){
		mDir=dir;
	}
	
	/**
	 * �ص�׼�����
	 * @author zhouwen
	 *
	 */
	public interface AudioStateListener{
		void wellPrepared();
	}
	
	public AudioStateListener mListener;
	
	public void setOnAudioStateListener(AudioStateListener listener){
		mListener=listener;
	}
	
	
	public static AudioManager getInstance(String dir){
		if(mInstance==null){
			synchronized(AudioManager.class){
				if(mInstance==null){
					mInstance=new AudioManager(dir);
				}
			}
			
		}
		return mInstance;
	}
	
	/**
	 * ׼��
	 */
	public void prepareAudio(){
		try {
			isPrepared=false;
			
			File dir=new File(mDir);
			if(!dir.exists())
				dir.mkdirs();
			
			String fileName=ToolsUtils.generateFileName();
			File file=new File(dir,fileName);
			
			mCurrentFilePath=file.getAbsolutePath();
			
			mMediaRecorder=new MediaRecorder();
			//��������ļ�
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			//����MediaRecorder����ƵԴΪ��˷�
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//������Ƶ��ʽ
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			//������Ƶ�ı���Ϊamr
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			//׼������
			isPrepared=true;
			if(mListener!=null){
				mListener.wellPrepared();
			}  
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ��������ļ�������
	 * @return
	 */
//	private String generateFileName() {
//		return UUID.randomUUID().toString()+".amr";
//	}

 
	/**
	 * �����ȼ�
	 * @return
	 */
	public int getVoiceLevel(int maxLevel){
		if(isPrepared){
			//mMediaRecorder.getMaxAmplitude() 1-32767֮��
			try {
				return maxLevel*mMediaRecorder.getMaxAmplitude()/32768+1;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 1;
	}
	
	/**
	 * �ͷ�
	 */
	public void release(){  
		mMediaRecorder.stop();
		mMediaRecorder.release();
		mMediaRecorder=null;
	}
	
	/**
	 * ȡ��
	 */
	public void cancel(){
		release();
		if(mCurrentFilePath!=null){
			File file=new File(mCurrentFilePath);
			file.delete();
			mCurrentFilePath=null;
		}
	}


	public String getCurrentFilePath() {
		// TODO Auto-generated method stub
		return mCurrentFilePath;
	}
	
}
