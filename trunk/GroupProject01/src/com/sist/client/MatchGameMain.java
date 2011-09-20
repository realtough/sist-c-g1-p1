package com.sist.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

public class MatchGameMain extends JFrame implements ActionListener {
	JPanel panmain = new JPanel();
	JPanel panuser1 = new JPanel();
	JPanel panuser2 = new JPanel();
	JPanel panuser3 = new JPanel();

	JButton btn[] = new JButton[64];

	JButton fhint = new JButton("��ü����");
	JButton hint = new JButton("��Ʈ");
	JButton main = new JButton("����");
	JButton ready = new JButton("�غ�");
	JButton start = new JButton("����");

	JTextField tfstate = new JTextField(25);
	JTextField tfscore = new JTextField(40);
	JTextArea chat = new JTextArea();

	Random rNum = new Random();
	String[] frame = new String[60];//�̹��� ����
	ImageIcon[] buttonPicture = new ImageIcon[60];
	int[] num = new int[64];
	int select = 0; // ���õ� ��ư�� ��

	int[] selIdx = { 0, 0 }; // ���õ� ��ư���� �ε���
	
	int score = 0;	// ����
	
	int count = 0;
	int success = 0;
	// 50���� �̹��� ������ �迭��ư�� �������� ��� ���� �Ѵ�.
	// 64ĭ�̴� 32���� ¦�� �̷��� ��.
	
	public MatchGameMain() {
		super("���� �׸� ã�� ����");
		panmain.setBackground(Color.blue);
		panuser1.setBackground(Color.cyan);
		panuser2.setBackground(Color.green);
		panuser3.setBackground(Color.orange);

		JScrollPane js = new JScrollPane(chat);

		int[] num = new int[64];
		// String[] frame = { ".\\image\\MatchGame\\1.gif"};
		
		for (int i = 0; i < 60; i++) {
			frame[i] = ".\\image\\MatchGame\\" + (i + 1) + ".gif";
			buttonPicture[i] = new ImageIcon(frame[i]);
			System.out.println(frame[i]);
		}
		// ���� �ǳ�
		setLayout(null);

		panmain.setBounds(10, 10, 580, 400);
		panmain.setLayout(new GridLayout(8, 8, 5, 5));
		for (int i = 0; i < btn.length; i++) {
			// ��ư ����
			btn[i] = new JButton();
			btn[i].setEnabled(false);
			btn[i].setBackground(Color.orange);

			// ��ư �гο� �߰�
			panmain.add(btn[i]);
		}

		// ��ư
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout(3, 3));
		p3.add("Center", fhint);
		p3.add("East", hint);

		setLayout(null);
		JPanel p4 = new JPanel();
		p4.setLayout(new GridLayout(4, 1, 3, 3));
		p4.setBounds(600, 420, 180, 140);
		p4.add(p3);
		p4.add(ready);
		p4.add(start);
		p4.add(main);

		// ����
		setLayout(null);
		JPanel p5 = new JPanel();
		p5.setLayout(new GridLayout(3, 1, 10, 10));
		p5.setBounds(600, 10, 180, 400);
		p5.add(panuser1);
		p5.add(panuser2);
		p5.add(panuser3);

		// ä��
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout(3, 3));
		p1.add("Center", tfscore);
		p1.add("East", tfstate);

		setLayout(null);
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout(5, 5));
		p2.setBounds(10, 420, 580, 140);
		p2.add("Center", chat);
		p2.add("South", p1);

		add(panmain);
		add(p2);
		add(p4);
		add(p5);

		setSize(800, 600);
		setVisible(true);
		setResizable(false);
		newGame();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MatchGameMain();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	// ��ư�� �̹����� �������� �־��ش�.
	public void newGame() {
		tfstate.setText("New Game");
		shuffle();
		answer();
		for (int i = 0; i < btn.length; i++) {
			btn[i].setBackground(Color.orange);
			btn[i].setEnabled(false);
		}

		tfscore.setText("0");
		score = 0;
		count = 0;
		success = 0;

	}
	private void answer() {//����,���� �ִ� �κ�

		tfstate.setText("Answer");

		for (int i = 0; i < btn.length; i++) {
//			btn[i].setLabel(frame[num[i] % 32]);
			int temp = i;
			if(temp>=60)temp = 59;
			btn[i].setIcon(buttonPicture[temp]);
			
		}
	}

	private void shuffle() { // ����

		// ���� ����
		int number = rNum.nextInt(num.length);
		num[0] = number;

		for (int i = 1; i < num.length; i++) {
			number = rNum.nextInt(num.length);

			for (int j = 0; j < i; j++) {
				if (num[j] == number) {
					i--;
					break;
				} else if (j + 1 == i) {
					num[i] = number;
				}
			}
		}
	}

}
