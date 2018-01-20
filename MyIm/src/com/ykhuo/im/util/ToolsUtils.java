package com.ykhuo.im.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

public class ToolsUtils {
	//选取文件夹返回
	public static final int INTENT_REQUEST_FILE = 20;
	
	
	public static void selectFile(Activity activity){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");//无类型限制
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		activity.startActivityForResult(intent, ToolsUtils.INTENT_REQUEST_FILE);
	}
	
	
	//模拟用户点击返回键
	public static void onBack(){  
	  new Thread(){  
	    public void run() {  
	     try{  
	       Instrumentation inst = new Instrumentation();  
	       inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);  
	     }  
	     catch (Exception e) {  
	                 Log.e("Exception when onBack", e.toString());  
	             }  
	    }  
	  }.start();  
	}
	
	//模拟用户点击Home键
	public static void onHome(){  
	  new Thread(){  
	    public void run() {  
	     try{  
	       Instrumentation inst = new Instrumentation();  
	       inst.sendKeyDownUpSync(KeyEvent.KEYCODE_HOME);  
	     }  
	     catch (Exception e) {  
	                 Log.e("Exception when onHome", e.toString());  
	             }  
	    }  
	  }.start();  
	}
	
	//将字符串转换成Date日期型
	public static Date getDate(String str){
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd"); 
		Date my=null;
		try {
			my = date.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return my;
	}
	
	//将Date日期型转换成字符串型
	public static String dateToString(Date time){ 
	    SimpleDateFormat formatter; 
	    formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
	    String ctime = formatter.format(time); 

	    return ctime; 
	} 
	
	/** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }
    
    /** 
     * 根据byte数组，生成文件 
     */  
    public static void getFile(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath,fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    }
    
    
    /**
	 * 随机生成文件的名称
	 * @return
	 */
	public static String generateFileName() {
		return UUID.randomUUID().toString()+".amr";
	}
	
	/**
	 * 随机生成文件的名称
	 * @return
	 */
	public static String generateFileName(String file) {
		return UUID.randomUUID().toString()+"."+file;
	}
	
	/**
	 * 语音文件的位置
	 */
	public static String getFileUrl(){
		return Environment.getExternalStorageDirectory()+"/freeIM/audios";
	}
	
	/**
	 * 文件的位置
	 */
	public static String getFileUrl(String DirName){
		return Environment.getExternalStorageDirectory()+"/freeIM/"+DirName;
	}

}
