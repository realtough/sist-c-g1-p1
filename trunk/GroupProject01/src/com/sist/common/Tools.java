package com.sist.common;

import java.awt.*;

public class Tools {
	private static Toolkit myTK = Toolkit.getDefaultToolkit();
	public static int centerX = myTK.getScreenSize().width / 2;
	public static int centerY = myTK.getScreenSize().height / 2;
	
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
