package com.sist.common;
import java.util.*;

//DB�� ����ʿ�
public class UserInfoManager {
	HashMap<Integer, UserInfo> userList = new HashMap<Integer, UserInfo>();
		
	public UserInfoManager(){
		userList.put(getMaxNumber(), new UserInfo("abcd", "1234", "ȫ�浿", new Date(), 1, new Date(), "�浿��"));
		userList.put(getMaxNumber(), new UserInfo("dcba", "4321", "�̼���", new Date(), 1, new Date(), "������"));
		userList.put(getMaxNumber(), new UserInfo("test", "1111", "��û��", new Date(), 2, new Date(), "û��"));
		userList.put(getMaxNumber(), new UserInfo("qwert", "0987", "Ȳ����", new Date(), 2, new Date(), "����"));
		userList.put(getMaxNumber(), new UserInfo("zxcv", "5678", "�̸���", new Date(), 1, new Date(), "õ�����"));
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
	
	//�α��μ����� ���� ���� ���� ����
	//ID�� ���� ���, ID�� ������ �н����尡 Ʋ�����, ��� �������
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
