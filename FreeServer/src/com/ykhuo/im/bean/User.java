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
	//serialVersionUID适用于Java的序列化机制。
	//简单来说，Java的序列化机制是通过判断类的serialVersionUID来验证版本一致性的。
	//在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体类的serialVersionUID进行比较，
	//如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常，即是InvalidCastException。
	
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
	
	//好友列表
	private ArrayList<User> friendList;

	public ArrayList<User> getFriendList() {
		return friendList;
	}

	public void setFriendList(ArrayList<User> friendList) {
		this.friendList = friendList;
	}
	
	
}
