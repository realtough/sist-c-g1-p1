package com.sist.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.GridLayout;

public class RandomCount extends JFrame implements ActionListener {

	JPanel mainP = new JPanel(); // 랜덤 숫자 세기 메인 판넬
	JPanel leftP = new JPanel(); // 좌측 플레이할 판넬
	JPanel rightP = new JPanel(); // 우측 보여지는 판넬

	JPanel enemyP = new JPanel(); // 우측 상단 상대방 판넬
	JPanel enemyP_In = new JPanel(); // enemyPanel 안의 판넬
	JPanel loginP = new JPanel(); // 우측 중단 로그인 목록 판넬
	JPanel actionP = new JPanel(); // 우측 하단 엑션 판넬
	JPanel emptyEast = new JPanel(); // 좌우측 빈공간
	JPanel emptyWest = new JPanel(); // 좌우측 빈공간
	
	JList loginL = new JList();
	
	JButton btnNew = new JButton("시작");

	JButton btnnum[][] = new JButton[5][5];
	JButton btnE[][] = new JButton[5][5];

	Font f = new Font("굴림체", Font.BOLD, 10); // Font size 및 설정

	public RandomCount() {
		super("차례로선택하기");
		Layout();
	}

	public void Layout() {
		mainP.setLayout(new BorderLayout());

		this.add(mainP, "Center");

		mainP.setLayout(new GridLayout(0, 2)); // 1/2 좌우측 구분
		mainP.add(leftP); // 좌측 GameP
		mainP.add(rightP); // 우측 subP
		leftP.setBackground(new Color(0, 0, 150));
		rightP.setBackground(new Color(255, 0, 0));

		leftP.setLayout(new GridLayout(5, 5));

		rightP.add(enemyP);
		rightP.add(loginP);
		// rightP.add(actionP);

		rightP.setLayout(new GridLayout(3, 0));

		enemyP.setLayout(new BorderLayout());
		enemyP.add("East",emptyEast);
		enemyP.add("West",emptyWest);
		enemyP.add("Center",enemyP_In);
		enemyP_In.setLayout(new GridLayout(5,5));
		enemyP.setBackground(new Color(100, 10, 0));
		
		loginP.setLayout(new BorderLayout());
		loginP.add(loginL, "Center");
		
		
		

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				btnE[i][j] = new JButton();
				
				enemyP_In.add(btnE[i][j]);
			}
		}

		int c = 0;	//나중에 삭제
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				c++;
				btnnum[i][j] = new JButton();

				leftP.add(btnnum[i][j]);
				btnnum[i][j].setText(i + "" + j); // setText
				btnnum[i][j].setFont(f); // SetFont

				btnnum[i][j].addActionListener(this);
			}
		}

		mainP.setBackground(new Color(0, 0, 0));
		this.setBounds(300, 300, 600, 430);
		this.setVisible(true);
	}

	public static void main(String[] args) {

		new RandomCount();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
