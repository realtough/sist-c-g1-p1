package com.sist.common;
import java.util.*;

public class UserInfoManager {
	HashMap<Integer, UserInfo> userList = new HashMap<Integer, UserInfo>();
		
	public UserInfoManager(){
	
	}
	
	//ȸ�� �߰�
	public void insertUser(UserInfo ui){
		userList.put(getMaxNumber(), ui);
		for(int i=0; i<userList.size(); i++){
			System.out.println(userList.get(new Integer(i)).toString());
		}
	}
	
	//ȸ�� Ż��
	public void deleteUser(int userNum, String password){
		
	}
	
	//ȸ�� ����
	public void updateUser(int userNum, String password){
		
	}
	
	//ȸ�� ���
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
