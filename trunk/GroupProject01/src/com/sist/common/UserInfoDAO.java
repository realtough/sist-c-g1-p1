package com.sist.common;

import java.util.*;
import java.util.Date;
import java.sql.*;

//DB���� ó�� Ŭ����
public class UserInfoDAO {

//	private final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:xe";	
	private final String ORACLE_URL = "jdbc:oracle:thin:@211.238.142.116:1521:xe";
	private final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String ORACLE_ID = "hoon";	
	private final String ORACLE_PW = "sistc";
	private Connection dbConnection;
	private PreparedStatement pStatement;

	// ����̹� ���
	public UserInfoDAO() {
		try {
			Class.forName(ORACLE_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connectDB() {
		try {
			dbConnection = DriverManager.getConnection(ORACLE_URL, ORACLE_ID,
					ORACLE_PW);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnectDB() {
		try {
			if (dbConnection != null) {
				dbConnection.close();
			}
			if (pStatement != null) {
				pStatement.close();
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}

	// ȸ�� �߰�
	public void insertUser(UserInfoVO uiVO) {
		try {
			connectDB();
			String sql = "insert into customer values(?, ?, ?, ?, ?, sysdate, ?)";
			pStatement = dbConnection.prepareStatement(sql);
			pStatement.setString(1, uiVO.getId());
			pStatement.setString(2, uiVO.getPw());
			pStatement.setString(3, uiVO.getName());
			long time = uiVO.getBirth().getTime();
			pStatement.setDate(4, new java.sql.Date(time));			
			pStatement.setString(5, String.valueOf(uiVO.getSex()));
			pStatement.setString(6, uiVO.getNickname());
			pStatement.executeQuery();			
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			disconnectDB();
		}
	}

	// ȸ�� Ż��
	public void deleteUser(int userNum, String password) {

	}

	// ȸ�� ����
	public void updateUser(int userNum, String password) {

	}

	// ��ü ȸ�� ���
	// ArrayList�� ������ ����
	public ArrayList<UserInfoVO> userAllList() {
		ArrayList<UserInfoVO> allUser = new ArrayList<UserInfoVO>();
		try {
			connectDB();
			String sql = "select id, pw, name, birth, sex, joinus, nickname from customer";
			pStatement = dbConnection.prepareStatement(sql);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()){
				UserInfoVO uiVO =new UserInfoVO();
				uiVO.setId(rs.getString(1));
				uiVO.setPw(rs.getString(2));
				uiVO.setName(rs.getString(3));
				uiVO.setBirth(rs.getDate(4));
				uiVO.setSex(rs.getString(5).charAt(0));
				uiVO.setJoinus(rs.getDate(6));
				uiVO.setNickname(rs.getString(7));
				allUser.add(uiVO);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			disconnectDB();
		}
		return allUser;
	}
	
	//ID�� ������ ���� ������ ��´�
	public UserInfoVO getUserInfo(String id){
		UserInfoVO uiVO = new UserInfoVO();
		try {
			connectDB();
			String sql = "select * from customer where id like ?";
			pStatement = dbConnection.prepareStatement(sql);
			pStatement.setString(1, id);
			ResultSet rs = pStatement.executeQuery();
			rs.next();
			uiVO.setId(rs.getString(1));
			uiVO.setPw(rs.getString(2));
			uiVO.setName(rs.getString(3));
			uiVO.setBirth(rs.getDate(4));
			uiVO.setSex(rs.getString(5).charAt(0));
			uiVO.setJoinus(rs.getDate(6));
			uiVO.setNickname(rs.getString(7));			
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			disconnectDB();
		}
		return uiVO;
	}
	
	// �ߺ� ��� �˻�
	public boolean isExist(String ident, String msg){
		boolean bCheck = false;
		try {			
			String sql;
			connectDB();			
			if(ident.equals("id")){
				sql = "select count(*) from customer where id like ?";	
			}else{
				sql = "select count(*) from customer where nickname like ?";
			}			
			pStatement = dbConnection.prepareStatement(sql);
			pStatement.setString(1, msg);			
			ResultSet rs = pStatement.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			if(count == 0){
				//�ߺ��Ǵ�ID�� �г����� ���� ���
				bCheck = true;
			}else {
				//�ߺ��� �߰ߵ� ���
				bCheck = false;
			}
		} catch (SQLException e) {
			// TODO: handle exception
		}		
		return bCheck;
	}

	// �α��μ����� ���� ���� ���� ����
	// ID�� ���� ���, ID�� ������ �н����尡 Ʋ�����, ��� �������
	public String verifyUser(String userID, String userPW) {
		String result = "";
		try {
			connectDB();
			String sql = "select count(*) from customer where id like ?";
			pStatement = dbConnection.prepareStatement(sql);
			pStatement.setString(1, userID);
			ResultSet rs = pStatement.executeQuery();
			rs.next();
			int correct = rs.getInt(1);
			if (correct == 0) {
				// ���̵� ����
				result = "22";
			} else if (correct == 1) {
				// ���̵�� ��й�ȣ ��� ��ġ
				sql = "select pw from customer where id like ?";
				pStatement = dbConnection.prepareStatement(sql);
				pStatement.setString(1, userID);
				rs = pStatement.executeQuery();
				rs.next();
				String pw = rs.getString(1);
				rs.close();
				if (pw.equals(userPW)) {
					result = "11";
				} else {
					// ���̵�� ������ ��й�ȣ�� Ʋ����
					result = "12";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			disconnectDB();
		}
		return result;
	}
}
