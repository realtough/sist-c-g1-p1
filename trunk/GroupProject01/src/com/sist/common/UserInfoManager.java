package com.sist.common;
import java.util.*;

//DB랑 통신필요
public class UserInfoManager {
	HashMap<Integer, UserInfo> userList = new HashMap<Integer, UserInfo>();
		
	public UserInfoManager(){
		userList.put(getMaxNumber(), new UserInfo("abcd", "1234", "홍길동", new Date(), 1, new Date(), "길동이"));
		userList.put(getMaxNumber(), new UserInfo("dcba", "4321", "이순신", new Date(), 1, new Date(), "순신이"));
		userList.put(getMaxNumber(), new UserInfo("test", "1111", "심청이", new Date(), 2, new Date(), "청이"));
		userList.put(getMaxNumber(), new UserInfo("qwert", "0987", "황진이", new Date(), 2, new Date(), "진이"));
		userList.put(getMaxNumber(), new UserInfo("zxcv", "5678", "이만기", new Date(), 1, new Date(), "천하장사"));
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
	
	//로그인서버로 부터 받은 정보 검증
	//ID가 없을 경우, ID는 맞지만 패스워드가 틀릴경우, 모두 맞을경우
	public int verifyUser(UserInfo ui){
		Iterator<Integer> itUserNo = userList.keySet().iterator();
		while(itUserNo.hasNext()){
			UserInfo sui = userList.get(itUserNo.next());
//			if(ui.getUserID())
		}
		int result = 0;
		
		return result;
	}
}
