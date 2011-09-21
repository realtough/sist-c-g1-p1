package com.sist.common;

import java.util.*;

//DB랑 통신필요
public class UserInfoManager {
	HashMap<Integer, UserInfo> alluserList = new HashMap<Integer, UserInfo>();

	public UserInfoManager() {
		alluserList.put(getMaxNumber(), new UserInfo("hoon", "sist", "조병훈",
				new Date(), 1, new Date(), "훈이"));
		alluserList.put(getMaxNumber(), new UserInfo("ho", "sist", "김재호",
				new Date(), 1, new Date(), "호이"));
		alluserList.put(getMaxNumber(), new UserInfo("jun", "sist", "주형준",
				new Date(), 1, new Date(), "준이"));
		alluserList.put(getMaxNumber(), new UserInfo("hyun", "sist", "김지현",
				new Date(), 1, new Date(), "현이"));
		alluserList.put(getMaxNumber(), new UserInfo("wook", "sist", "조성욱",
				new Date(), 1, new Date(), "욱이"));
	}

	// 회원 추가
	public void insertUser(UserInfo ui) {
		alluserList.put(getMaxNumber(), ui);
		for (int i = 0; i < alluserList.size(); i++) {
			System.out.println(alluserList.get(new Integer(i)).toString());
		}
	}

	// 회원 탈퇴
	public void deleteUser(int userNum, String password) {

	}

	// 회원 수정
	public void updateUser(int userNum, String password) {

	}

	// 회원 찾기
	public String getNickByID(String id){
		Iterator<Integer> itUserNo = alluserList.keySet().iterator();
		String result = "";
		while (itUserNo.hasNext()) {
			UserInfo sui = alluserList.get(itUserNo.next());
			if (sui.getUserID().equals(id)) {
				result = sui.getUserNickname();
				break;
			} 
		}				
		return result; 
	}
	// 회원 목록
	public HashMap<Integer, UserInfo> userAllList() {
		return null;
	}

	public Integer getMaxNumber() {
		int max = 0;
		if (alluserList.size() > max)
			max = alluserList.size();
		return new Integer(max);
	}

	// 로그인서버로 부터 받은 정보 검증
	// ID가 없을 경우, ID는 맞지만 패스워드가 틀릴경우, 모두 맞을경우
	public String verifyUser(String userID, String userPW) {
		Iterator<Integer> itUserNo = alluserList.keySet().iterator();
		String result = "";
		while (itUserNo.hasNext()) {
			UserInfo sui = alluserList.get(itUserNo.next());
			if (sui.getUserID().equals(userID)
					&& sui.getUserPW().equals(userPW)) {
				result = "11 "+sui.getUserNickname();
				break;
			} else if (sui.getUserID().equals(userID)
					&& !sui.getUserPW().equals(userPW)) {
				result = "12";
				break;
			} else if (!sui.getUserID().equals(userID)) {
				result = "22";
			}
		}
		return result;
	}
}
