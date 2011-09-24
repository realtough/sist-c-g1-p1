package com.sist.common;

import java.util.*;
import java.util.Date;
import java.sql.*;

//DB�� ����ʿ�
public class UserInfoManagerDAO {

	private final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:xe";	
	private final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String ORACLE_ID = "hoon";
	private final String ORACLE_PW = "73048442";
	private Connection dbConnection;
	private PreparedStatement pStatement;

	HashMap<Integer, UserInfoVO> alluserList = new HashMap<Integer, UserInfoVO>();

	// ����̹� ���
	public UserInfoManagerDAO() {
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
	public void insertUser(UserInfoVO ui) {
		alluserList.put(getMaxNumber(), ui);
		// for (int i = 0; i < alluserList.size(); i++) {
		// System.out.println(alluserList.get(new Integer(i)).toString());
		// }
	}

	// ȸ�� Ż��
	public void deleteUser(int userNum, String password) {

	}

	// ȸ�� ����
	public void updateUser(int userNum, String password) {

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
	
	//�α����� ������ ���� ������ ��´�
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
			uiVO.setC_name(rs.getString(3));
			uiVO.setBirth(rs.getDate(4));
			uiVO.setSex(rs.getString(5).charAt(0));
			uiVO.setJoinus(rs.getDate(6));
			uiVO.setNname(rs.getString(7));
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			disconnectDB();
		}
		return uiVO;
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
