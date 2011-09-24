package com.sist.common;

import java.util.Date;

//유저정보 클래스
public class UserInfoVO {
	private String id;
	private String pw;
	private String c_name;
	private Date birth;
	private char sex;
	private Date joinus;
	private String nname;	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public char getSex() {
		return sex;
	}
	public void setSex(char sex) {
		this.sex = sex;
	}
	public Date getJoinus() {
		return joinus;
	}
	public void setJoinus(Date joinus) {
		this.joinus = joinus;
	}
	public String getNname() {
		return nname;
	}
	public void setNname(String nname) {
		this.nname = nname;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id+"|"+pw+"|"+c_name+"|"+birth+"|"+sex+"|"+joinus+"|"+nname;
	}	
}//class
