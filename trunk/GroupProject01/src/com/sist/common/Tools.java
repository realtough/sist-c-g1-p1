package com.sist.common;

import java.awt.*;
import java.sql.*;
import java.text.*;

public class Tools {
	private static Toolkit myTK = Toolkit.getDefaultToolkit();
	public static final int centerX = myTK.getScreenSize().width / 2;
	public static final int centerY = myTK.getScreenSize().height / 2;
	public static final String serverIp = "localhost";
	public static final int MAIN_SERVER_PORT = 10000;
	public static final int LOGIN_SERVER_PORT = 10001;
	public static final String LOGIN_SERVER_HEADER = "[�α��μ���] ";
	public static final String MAIN_SERVER_HEADER = "[���μ���] ";
	
	//�׸���� ���̾ƿ� ��ġ�� �޼ҵ�
	public static void insert(Container cnt, Component cmp, int x, int y,
			int w, int h) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.insets = new Insets(2, 2, 2, 2);
		cnt.add(cmp, gbc);
	}

	public static void insert(Container cnt, Component cmp, int x, int y,
			int w, int h, double wx, double wy) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.insets = new Insets(2, 2, 2, 2);
		cnt.add(cmp, gbc);
	}

	public static void insert(Container cnt, Component cmp, int x, int y,
			int w, int h, double wx, double wy, int insetSize) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.insets = new Insets(insetSize, insetSize, insetSize, insetSize);
		cnt.add(cmp, gbc);
	}	
	
	//UserInfoVO�� toString()�޼ҵ�� ������ Ư�� ���ڿ��� �ٽ� VO��ü�� ��ȯ�ϱ� ���� �޼ҵ�
	//��Ʈ��ũ�� ���۹��� ���ڿ��� VO��ü�� ȯ���� ����ϱ� ����
	public static UserInfoVO stringToUserInfo(String uimsg){
		
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		UserInfoVO uiVO = new UserInfoVO();		
		String temp[] = uimsg.split("@", 7);		

		try {
			uiVO.setId(temp[0]);
			uiVO.setPw(temp[1]);
			uiVO.setName(temp[2]);
			uiVO.setBirth(sdFormat.parse(temp[3]));	//ParseException �߻�����
			uiVO.setSex(temp[4].charAt(0));
			uiVO.setJoinus(sdFormat.parse(temp[5])); //ParseException �߻�����
			uiVO.setNickname(temp[6]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return uiVO;
	}
}
