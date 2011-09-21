package com.sist.common;

import java.util.*;
import java.util.Date;
import java.sql.*;

//DB�� ����ʿ�
public class UserInfoManagerDAO {
	private final String ORACLE_URL = "";
	HashMap<Integer, UserInfoVO> alluserList = new HashMap<Integer, UserInfoVO>();

	public UserInfoManagerDAO() {
		alluserList.put(getMaxNumber(), new UserInfoVO("hoon", "sist", "������",
				new Date(), 1, new Date(), "����"));
		alluserList.put(getMaxNumber(), new UserInfoVO("ho", "sist", "����ȣ",
				new Date(), 1, new Date(), "ȣ��"));
		alluserList.put(getMaxNumber(), new UserInfoVO("jun", "sist", "������",
				new Date(), 1, new Date(), "����"));
		alluserList.put(getMaxNumber(), new UserInfoVO("hyun", "sist", "������",
				new Date(), 1, new Date(), "����"));
		alluserList.put(getMaxNumber(), new UserInfoVO("wook", "sist", "������",
				new Date(), 1, new Date(), "����"));
	}

	// ȸ�� �߰�
	public void insertUser(UserInfoVO ui) {
		alluserList.put(getMaxNumber(), ui);
//		for (int i = 0; i < alluserList.size(); i++) {
//			System.out.println(alluserList.get(new Integer(i)).toString());
//		}
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
			UserInfoVO sui = alluserList.get(itUserNo.next());
			if (sui.getUserID().equals(id)) {
				result = sui.getUserNickname();
				break;
			} 
		}				
		return result; 
	}
	// ȸ�� ���
	public HashMap<Integer, UserInfoVO> userAllList() {
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
			UserInfoVO sui = alluserList.get(itUserNo.next());
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
