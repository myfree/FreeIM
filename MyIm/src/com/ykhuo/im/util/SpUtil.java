package com.ykhuo.im.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * �洢���󹤾���
 * @author zhouwen
 *
 */
public class SpUtil {
	private static final String NAME="MyIm";
	private static SpUtil instance;
	static{
		instance=new SpUtil();
	}
	
	public static SpUtil getInstance(){
		if(instance==null){
			instance=new SpUtil();
		}
		return instance;
	}
	
	//SharedPreferences������Ա�ͬһӦ�ó����µ������������
	public static SharedPreferences getSharePerference(Context context){
		//���� �ļ���˽������,ֻ�ܱ�Ӧ�ñ������,�ڸ�ģʽ��,д������ݻḲ��ԭ�ļ�������
		return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}
	
	public static boolean isFirst(SharedPreferences sp){
		return sp.getBoolean("isFirst", false);
	}
	
	public static void setStringSharedPerference(SharedPreferences sp,String key,String value){
		Editor editor=sp.edit();
		editor.putString(key, value);
		editor.commit();	//�ر�editor
	}
	
	public static void setBooleanSharedPerference(SharedPreferences sp,String key,boolean value){
		Editor editor=sp.edit();
		editor.putBoolean(key, value);
		editor.commit();	//�ر�editor
	}
}
