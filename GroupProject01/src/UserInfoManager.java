import java.util.*;

public class UserInfoManager {
	HashMap<Integer, UserInfo> userList = new HashMap<Integer, UserInfo>();
		
	public UserInfoManager(){
	
		//Test Data
		userList.put(getMaxNumber(), new UserInfo("hoon", "sistc", "병훈", "오리발", "열심히합시다", "", ""));
		userList.put(getMaxNumber(), new UserInfo("hoon", "sistc", "병훈", "오리발", "열심히합시다", "", ""));
		userList.put(getMaxNumber(), new UserInfo("hoon", "sistc", "병훈", "오리발", "열심히합시다", "", ""));
		userList.put(getMaxNumber(), new UserInfo("hoon", "sistc", "병훈", "오리발", "열심히합시다", "", ""));
	}
	
	//회원 추가
	public void userInsert(){
		
	}
	
	//회원 탈퇴
	public void userDelete(int userNum, String password){
		
	}
	
	//회원 수정
	public void userModify(int userNum, String password){
		
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
