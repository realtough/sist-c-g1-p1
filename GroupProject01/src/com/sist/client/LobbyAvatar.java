package com.sist.client;

import javax.swing.*;

//아바타 선택창
public class LobbyAvatar extends JFrame{
	//아이콘 사이즈 -> 110x185
	ImageIcon male01 = new ImageIcon(".\\image\\avatar\\male01.gif");
	ImageIcon male02 = new ImageIcon(".\\image\\avatar\\male02.gif");
	ImageIcon male03 = new ImageIcon(".\\image\\avatar\\male03.gif");
	ImageIcon male04 = new ImageIcon(".\\image\\avatar\\male04.gif");
	ImageIcon female01 = new ImageIcon(".\\image\\avatar\\female01.gif");
	ImageIcon female02 = new ImageIcon(".\\image\\avatar\\female02.gif");
	ImageIcon female03 = new ImageIcon(".\\image\\avatar\\female03.gif");
	ImageIcon female04 = new ImageIcon(".\\image\\avatar\\female04.gif");		
	JButton jbTest = new JButton(male01);
	 				
	public LobbyAvatar(){		
		jbTest.setIcon(male02);
		add(jbTest);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(110, 185);
		setVisible(true);
	}

	public static void main(String[] args) {
		new LobbyAvatar();
	}
}
