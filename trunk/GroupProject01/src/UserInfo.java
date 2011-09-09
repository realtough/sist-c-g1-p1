//유저정보 클래스
public class UserInfo {
	private String userID;
	private String userPW;
	private String userName;
	private String userNickname;
	private String userMemo;
	private String userIp;
	private String userPort;	
	
	public UserInfo(){
		
	}

	public UserInfo(String userID, String userPW, String userName,
			String userNickname, String userMemo, String userIp, String userPort) {
		super();
		this.userID = userID;
		this.userPW = userPW;
		this.userName = userName;
		this.userNickname = userNickname;
		this.userMemo = userMemo;
		this.userIp = userIp;
		this.userPort = userPort;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserPW() {
		return userPW;
	}

	public void setUserPW(String userPW) {
		this.userPW = userPW;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserMemo() {
		return userMemo;
	}

	public void setUserMemo(String userMemo) {
		this.userMemo = userMemo;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserPort() {
		return userPort;
	}

	public void setUserPort(String userPort) {
		this.userPort = userPort;
	}
	

}
