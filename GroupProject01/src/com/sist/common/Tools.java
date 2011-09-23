package com.sist.common;

import java.awt.*;
import java.io.*;
import java.net.*;

public class Tools {
	private static Toolkit myTK = Toolkit.getDefaultToolkit();
	public static int centerX = myTK.getScreenSize().width / 2;
	public static int centerY = myTK.getScreenSize().height / 2;
	public static String serverIp = "211.238.142.116";
	public static final int MAIN_SERVER_PORT = 10000;
	public static final int LOGIN_SERVER_PORT = 10001;
	public static final String LOGIN_SERVER_HEADER = "[�α��μ���]";
	public static final String MAIN_SERVER_HEADER = "[���μ���]";
	
	//�׸���� ���̾ƿ� �� �޼ҵ�
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
}
