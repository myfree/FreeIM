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
	//ѡȡ�ļ��з���
	public static final int INTENT_REQUEST_FILE = 20;
	
	
	public static void selectFile(Activity activity){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");//����������
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		activity.startActivityForResult(intent, ToolsUtils.INTENT_REQUEST_FILE);
	}
	
	
	//ģ���û�������ؼ�
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
	
	//ģ���û����Home��
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
	
	//���ַ���ת����Date������
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
	
	//��Date������ת�����ַ�����
	public static String dateToString(Date time){ 
	    SimpleDateFormat formatter; 
	    formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
	    String ctime = formatter.format(time); 

	    return ctime; 
	} 
	
	/** 
     * ���ָ���ļ���byte���� 
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
     * ����byte���飬�����ļ� 
     */  
    public static void getFile(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            File dir = new File(filePath);  
            if(!dir.exists()&&dir.isDirectory()){//�ж��ļ�Ŀ¼�Ƿ����  
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
	 * ��������ļ�������
	 * @return
	 */
	public static String generateFileName() {
		return UUID.randomUUID().toString()+".amr";
	}
	
	/**
	 * ��������ļ�������
	 * @return
	 */
	public static String generateFileName(String file) {
		return UUID.randomUUID().toString()+"."+file;
	}
	
	/**
	 * �����ļ���λ��
	 */
	public static String getFileUrl(){
		return Environment.getExternalStorageDirectory()+"/freeIM/audios";
	}
	
	/**
	 * �ļ���λ��
	 */
	public static String getFileUrl(String DirName){
		return Environment.getExternalStorageDirectory()+"/freeIM/"+DirName;
	}

}
