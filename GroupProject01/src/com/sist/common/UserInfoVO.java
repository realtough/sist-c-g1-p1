package com.sist.common;

import java.text.SimpleDateFormat;
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
	//VO객체의 모든값을 식별자로 묶어 하나의 문자열로 출력
	//네트워크로 전송할시 문자열 전송 한가지로만 처리하기 위함
	public String toString() {
		String regex = "@";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return id+regex+pw+regex+c_name+regex+sdf.format(birth)+regex+sex+regex+sdf.format(joinus)+regex+nname;
	}	
}//class
