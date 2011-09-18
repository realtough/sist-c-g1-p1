package com.sist.common;
import java.util.*;

public class UserInfoManager {
	HashMap<Integer, UserInfo> userList = new HashMap<Integer, UserInfo>();
		
	public UserInfoManager(){
	
	}
	
	//회원 추가
	public void insertUser(UserInfo ui){
		userList.put(getMaxNumber(), ui);
		for(int i=0; i<userList.size(); i++){
			System.out.println(userList.get(new Integer(i)).toString());
		}
	}
	
	//회원 탈퇴
	public void deleteUser(int userNum, String password){
		
	}
	
	//회원 수정
	public void updateUser(int userNum, String password){
		
	}
	
	//회원 목록
	public HashMap<Integer, UserInfo> userAllList(){
		return null;
	}
	
	public Integer getMaxNumber(){
		int max = 0;
		if(userList.size() > max) max = userList.size();
		return new Integer(max);
	}
	
	public static void main(String[] args) {
		UserInfoManager uim = new UserInfoManager();		
		System.out.println(uim.userList.get(new Integer(1)).toString());
	}
}
