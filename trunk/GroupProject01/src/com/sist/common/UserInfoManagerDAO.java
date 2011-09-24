package com.sist.common;

import java.util.*;
import java.util.Date;
import java.sql.*;

//DB�� ����ʿ�
public class UserInfoManagerDAO {

	private final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1521:xe";	
	private final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String ORACLE_ID = "hoon";
//	private final String ORACLE_PW = "73048442";
	private final String ORACLE_PW = "sistc";
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
	public void insertUser(UserInfoVO uiVO) {
		try {
			connectDB();
			String sql = "insert into customer values(?, ?, ?, ?, ?, sysdate, ?)";
			pStatement = dbConnection.prepareStatement(sql);
			pStatement.setString(1, uiVO.getId());
			pStatement.setString(2, uiVO.getPw());
			pStatement.setString(3, uiVO.getC_name());
			pStatement.setDate(4, (java.sql.Date)uiVO.getBirth());
			pStatement.setString(5, String.valueOf(uiVO.getSex()));
			pStatement.setString(6, uiVO.getNname());
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
	public ArrayList<UserInfoVO> userAllList() {
		ArrayList<UserInfoVO> allUser = new ArrayList<UserInfoVO>();
		try {
			connectDB();
			String sql = "select id, pw, c_name, birth, sex, joinus, nname from customer";
			pStatement = dbConnection.prepareStatement(sql);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()){
				UserInfoVO uiVO =new UserInfoVO();
				uiVO.setId(rs.getString(1));
				uiVO.setPw(rs.getString(2));
				uiVO.setC_name(rs.getString(3));
				uiVO.setBirth(rs.getDate(4));
				uiVO.setSex(rs.getString(5).charAt(0));
				uiVO.setJoinus(rs.getDate(6));
				uiVO.setNname(rs.getString(7));
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
		} catch (SQLException e) {
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
