import java.util.*;

public class UserInfoManager {
	HashMap<Integer, UserInfo> userList = new HashMap<Integer, UserInfo>();
		
	public UserInfoManager(){
	
		//Test Data
		userList.put(getMaxNumber(), new UserInfo("hoon", "sistc", "����", "������", "�������սô�", "", ""));
		userList.put(getMaxNumber(), new UserInfo("hoon", "sistc", "����", "������", "�������սô�", "", ""));
		userList.put(getMaxNumber(), new UserInfo("hoon", "sistc", "����", "������", "�������սô�", "", ""));
		userList.put(getMaxNumber(), new UserInfo("hoon", "sistc", "����", "������", "�������սô�", "", ""));
	}
	
	//ȸ�� �߰�
	public void userInsert(){
		
	}
	
	//ȸ�� Ż��
	public void userDelete(int userNum, String password){
		
	}
	
	//ȸ�� ����
	public void userModify(int userNum, String password){
		
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
