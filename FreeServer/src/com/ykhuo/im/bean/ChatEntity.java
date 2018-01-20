package com.ykhuo.im.bean;

import java.io.Serializable;

public class ChatEntity implements Serializable{
	public static final int  RECEIVE = 0;
	public static final int SEND = 1;
	public static final int SEND_VOICE = 2;// 发送语音
	public static final int RECEIVE_VOICE = 3;// 接收语音
	public static final int SEND_PHOTO = 4;// 发送照片
	public static final int RECEIVE_PHOTO = 5;// 接收照片
	public static final int SEND_FILE = 6;// 发送文件
	public static final int RECEIVE_FILE = 7;// 接收文件
	
	private int senderId;	   //发送id
	private int receiverId;    //接收id
	private String sendDate;   //发送日期
	private int messageType;   //消息类型
	private String content;    //内容
	private Object object;     //object
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public String getSendTime() {
		return sendDate;
	}
	public void setSendTime(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	//语音消息 属性、方法
	private float time;
	private String filePath;
	
	public void Recorder(float time, String filePath) {
		this.time = time;
		this.filePath = filePath;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
