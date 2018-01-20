package com.ykhuo.im.bean;

import java.sql.Date;

/**
 * ������MessageEntity ˵����messageFragment��ʾ����Ϣ
 */
public class MessageTabEntity {
	public static final int MAKE_FRIEND_REQUEST = 0;// �յ����ǽ��ѵ�����
	public static final int MAKE_FRIEND_RESPONSE_ACCEPT = 1;//�յ��ظ����Է�����
	public static final int MAKE_FRIEND_RESPONSE_REJECT = 2;//�յ��ظ����Է��ܾ�
	public static final int FRIEND_MESSAGE = 3;// �յ��������ѵ���Ϣ
	public static final int VOICE_MESSAGE = 4;// �յ�����������Ϣ
	
	private int unReadCount; //��ȡ��Ϣ����
	private int senderId;// ���ͷ���Id
	private String content;
	private String filepath;
	private int messageType;
	private String sendTime;
	private String name;

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	private byte[] photo;

	public int getUnReadCount() {
		return unReadCount;
	}

	public void setUnReadCount(int count) {
		this.unReadCount = count;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

}
