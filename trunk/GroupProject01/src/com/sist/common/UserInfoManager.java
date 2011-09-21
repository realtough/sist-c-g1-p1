package com.sist.common;

import java.util.*;

//DB�� ����ʿ�
public class UserInfoManager {
	HashMap<Integer, UserInfo> alluserList = new HashMap<Integer, UserInfo>();

	public UserInfoManager() {
		alluserList.put(getMaxNumber(), new UserInfo("hoon", "sist", "������",
				new Date(), 1, new Date(), "����"));
		alluserList.put(getMaxNumber(), new UserInfo("ho", "sist", "����ȣ",
				new Date(), 1, new Date(), "ȣ��"));
		alluserList.put(getMaxNumber(), new UserInfo("jun", "sist", "������",
				new Date(), 1, new Date(), "����"));
		alluserList.put(getMaxNumber(), new UserInfo("hyun", "sist", "������",
				new Date(), 1, new Date(), "����"));
		alluserList.put(getMaxNumber(), new UserInfo("wook", "sist", "������",
				new Date(), 1, new Date(), "����"));
	}

	// ȸ�� �߰�
	public void insertUser(UserInfo ui) {
		alluserList.put(getMaxNumber(), ui);
		for (int i = 0; i < alluserList.size(); i++) {
			System.out.println(alluserList.get(new Integer(i)).toString());
		}
	}

	// ȸ�� Ż��
	public void deleteUser(int userNum, String password) {

	}

	// ȸ�� ����
	public void updateUser(int userNum, String password) {

	}

	// ȸ�� ã��
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
	// ȸ�� ���
	public HashMap<Integer, UserInfo> userAllList() {
		return null;
	}

	public Integer getMaxNumber() {
		int max = 0;
		if (alluserList.size() > max)
			max = alluserList.size();
		return new Integer(max);
	}

	// �α��μ����� ���� ���� ���� ����
	// ID�� ���� ���, ID�� ������ �н����尡 Ʋ�����, ��� �������
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
