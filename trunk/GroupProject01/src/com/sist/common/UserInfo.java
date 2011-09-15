package com.sist.common;

import java.util.Date;

//유저정보 클래스
public class UserInfo {
	private String userID;
	private String userPW;
	private String userName;
	private Date birthDate;
	private int sex;
	private Date registerDate;
	private String userNickname;
	
	public UserInfo(){
		
	}

	public UserInfo(String userID, String userPW, String userName,
			Date birthDate, int sex, Date registerDate, String userNickname) {
		super();
		this.userID = userID;
		this.userPW = userPW;
		this.userName = userName;
		this.birthDate = birthDate;
		this.sex = sex;
		this.registerDate = registerDate;
		this.userNickname = userNickname;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserPW() {
		return userPW;
	}

	public void setUserPW(String userPW) {
		this.userPW = userPW;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}


}//class
