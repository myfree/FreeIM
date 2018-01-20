package com.ykhuo.im.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 存储对象工具类
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
	
	//SharedPreferences对象可以被同一应用程序下的其他组件共享
	public static SharedPreferences getSharePerference(Context context){
		//设置 文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
		return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}
	
	public static boolean isFirst(SharedPreferences sp){
		return sp.getBoolean("isFirst", false);
	}
	
	public static void setStringSharedPerference(SharedPreferences sp,String key,String value){
		Editor editor=sp.edit();
		editor.putString(key, value);
		editor.commit();	//关闭editor
	}
	
	public static void setBooleanSharedPerference(SharedPreferences sp,String key,boolean value){
		Editor editor=sp.edit();
		editor.putBoolean(key, value);
		editor.commit();	//关闭editor
	}
}
