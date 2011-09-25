package com.sist.client;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

//아바타 선택창
//800x600크기의 패널로 카드레이아웃으로 메인 클라이언트에 배치 예정
class LobbyAvatar extends JFrame implements ActionListener{
	//아이콘 사이즈 -> 110x185
	ImageIcon male01 = new ImageIcon(".\\image\\avatar\\male01.gif");
	ImageIcon male02 = new ImageIcon(".\\image\\avatar\\male02.gif");
	ImageIcon male03 = new ImageIcon(".\\image\\avatar\\male03.gif");
	ImageIcon male04 = new ImageIcon(".\\image\\avatar\\male04.gif");
	ImageIcon female01 = new ImageIcon(".\\image\\avatar\\female01.gif");
	ImageIcon female02 = new ImageIcon(".\\image\\avatar\\female02.gif");
	ImageIcon female03 = new ImageIcon(".\\image\\avatar\\female03.gif");
	ImageIcon female04 = new ImageIcon(".\\image\\avatar\\female04.gif");			
	JButton jbAvatar01 = new JButton(male01);
	JButton jbAvatar02 = new JButton(male02);
	JButton jbAvatar03 = new JButton(male03);
	JButton jbAvatar04 = new JButton(male04);
	JButton jbAvatar05 = new JButton(female01);
	JButton jbAvatar06 = new JButton(female02);
	JButton jbAvatar07 = new JButton(female03);
	JButton jbAvatar08 = new JButton(female04);
	 				
	public LobbyAvatar(){		
		setLayout(new GridLayout(2, 4, 5, 5));
		add(jbAvatar01);
		add(jbAvatar02);
		add(jbAvatar03);
		add(jbAvatar04);
		add(jbAvatar05);
		add(jbAvatar06);
		add(jbAvatar07);
		add(jbAvatar08);
				
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setVisible(true);
	}

	public static void main(String[] args) {
		new LobbyAvatar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub		
		JButton jb = (JButton)e.getSource();
		if(jb == jbAvatar01){
			
		} else if(jb == jbAvatar02){
			
		} else if(jb == jbAvatar03){
			
		} else if(jb == jbAvatar04){
			
		} else if(jb == jbAvatar05){
			
		} else if(jb == jbAvatar06){
			
		} else if(jb == jbAvatar07){
			
		} else if(jb == jbAvatar08){
			
		}
	}
}
