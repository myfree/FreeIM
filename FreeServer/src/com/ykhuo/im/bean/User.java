package com.ykhuo.im.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {
	private int account;
	private String username;
	private String password;
	private String nickname;
	private byte[] photo;
	private int sex;
	private Date birthday;
	private boolean online;
	
	private static final long serialVersionUID = 1L;
	//serialVersionUID������Java�����л����ơ�
	//����˵��Java�����л�������ͨ���ж����serialVersionUID����֤�汾һ���Եġ�
	//�ڽ��з����л�ʱ��JVM��Ѵ������ֽ����е�serialVersionUID�뱾����Ӧʵ�����serialVersionUID���бȽϣ�
	//�����ͬ����Ϊ��һ�µģ����Խ��з����л�������ͻ�������л��汾��һ�µ��쳣������InvalidCastException��
	
	public int getAccount() {
		return account;
	}
	public void setAccount(int account) {
		this.account = account;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public boolean Online() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}	
	
	//�����б�
	private ArrayList<User> friendList;

	public ArrayList<User> getFriendList() {
		return friendList;
	}

	public void setFriendList(ArrayList<User> friendList) {
		this.friendList = friendList;
	}
	
	
}
