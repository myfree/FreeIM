/**
 * 文件名：User.java
 * 时间：2015年5月9日上午10:23:19
 * 作者：修维康
 */
package com.ykhuo.im.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 类名：User 说明：用户对象
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int account;
	private String username;
	private String nickname;
	private String password;
	private Date birthday;
	private int sex; // 0代表女生 1代表男生
	private boolean Online;
	//private String location;
	private byte[] photo;
	//private int age;
	private String userBriefIntro;

	public String getUserBriefIntro() {
		return userBriefIntro;
	}

	public void setUserBriefIntro(String userBriefIntro) {
		this.userBriefIntro = userBriefIntro;
	}

	private ArrayList<User> friendList;

	public ArrayList<User> getFriendList() {
		return friendList;
	}

	public void setFriendList(ArrayList<User> friendList) {
		this.friendList = friendList;
	}

	public User(String username, String nickname, String password,
			Date birthday, int sex, byte[] photo) {
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.birthday = birthday;
		this.sex = sex;
		this.photo = photo;
	}

	public User() {

	}
	
	

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public boolean isOnline() {
		return Online;
	}

	public void setOnline(boolean online) {
		Online = online;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public boolean equals(Object o) {
		User user = (User) o;
		if (this.account == user.getAccount())
			return true;
		return false;
	}
}
