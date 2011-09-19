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

	JPanel mainP = new JPanel(); // ���� ���� ���� ���� �ǳ�
	JPanel leftP = new JPanel(); // ���� �÷����� �ǳ�
	JPanel rightP = new JPanel(); // ���� �������� �ǳ�

	JPanel enemyP = new JPanel(); // ���� ��� ���� �ǳ�
	JPanel enemyP_In = new JPanel(); // enemyPanel ���� �ǳ�
	JPanel loginP = new JPanel(); // ���� �ߴ� �α��� ��� �ǳ�
	JPanel actionP = new JPanel(); // ���� �ϴ� ���� �ǳ�
	JPanel emptyEast = new JPanel(); // �¿��� �����
	JPanel emptyWest = new JPanel(); // �¿��� �����
	
	JList loginL = new JList();
	
	JButton btnNew = new JButton("����");

	JButton btnnum[][] = new JButton[5][5];
	JButton btnE[][] = new JButton[5][5];

	Font f = new Font("����ü", Font.BOLD, 10); // Font size �� ����

	public RandomCount() {
		super("���ʷμ����ϱ�");
		Layout();
	}

	public void Layout() {
		mainP.setLayout(new BorderLayout());

		this.add(mainP, "Center");

		mainP.setLayout(new GridLayout(0, 2)); // 1/2 �¿��� ����
		mainP.add(leftP); // ���� GameP
		mainP.add(rightP); // ���� subP
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

		int c = 0;	//���߿� ����
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
