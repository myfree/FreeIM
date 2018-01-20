package com.ykhuo.im.bean;

import java.io.Serializable;

public class ChatEntity implements Serializable{
	public static final int  RECEIVE = 0;
	public static final int SEND = 1;
	public static final int SEND_VOICE = 2;// ��������
	public static final int RECEIVE_VOICE = 3;// ��������
	public static final int SEND_PHOTO = 4;// ������Ƭ
	public static final int RECEIVE_PHOTO = 5;// ������Ƭ
	public static final int SEND_FILE = 6;// �����ļ�
	public static final int RECEIVE_FILE = 7;// �����ļ�
	
	private int senderId;	   //����id
	private int receiverId;    //����id
	private String sendDate;   //��������
	private int messageType;   //��Ϣ����
	private String content;    //����
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
	
	//������Ϣ ���ԡ�����
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
