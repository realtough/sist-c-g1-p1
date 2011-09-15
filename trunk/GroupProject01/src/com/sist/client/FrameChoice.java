package com.sist.client;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class FrameChoice extends Frame implements ActionListener {

	Panel p1 = new Panel();
	Panel p2 = new Panel();
	Panel p3 = new Panel();
	
	
	
	TextField tfState = new TextField("New Game");
	TextField tfScore = new TextField("0");

	Button[] btn = new Button[16];

	Button btStart = new Button("Start");
	Button btExit = new Button("Exit");

	int[] num = new int[16];
	String[] fruits = { "���", "����", "����", "����", "������", "�ڵ�", "����", "Ű��" };

	Random rNum = new Random();

	int select = 0; // ���õ� ��ư�� ��

	int[] selIdx = { 0, 0 }; // ���õ� ��ư���� �ε���
	
	int score = 0;	// ����
	
	int count = 0;
	int success = 0;

	public FrameChoice() {
		super("FrameChoice");

		p1.setLayout(new GridLayout(4, 4, 10, 10));
		for (int i = 0; i < btn.length; i++) {
			// ��ư ����
			btn[i] = new Button();
			btn[i].setEnabled(false);
			btn[i].setBackground(Color.orange);

			// ��ư �гο� �߰�
			p1.add(btn[i]);
		}

		p2.setLayout(new GridLayout(1, 2, 10, 10));
		p2.add(btStart);
		p2.add(btExit);

		p3.setLayout(new GridLayout(1, 2, 10, 10));
		tfState.setEditable(false);
		tfScore.setEditable(false);
		p3.add(tfState);
		p3.add(tfScore);

		this.setLayout(new BorderLayout(10, 10));
		this.add("North", p3);
		this.add("Center", p1);
		this.add("South", p2);

		this.setBounds(300, 300, 300, 300);
		this.setVisible(true);

		btStart.addActionListener(this);
		btExit.addActionListener(this);

		for (int i = 0; i < btn.length; i++) {
			btn[i].addActionListener(this);
		}

		shuffle();
		answer();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new FrameChoice();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();

		if (source == btStart) {
			if (btStart.getLabel().equals("Start")) {
				start();
				btStart.setLabel("New Game");
			} else if (btStart.getLabel().equals("New Game")) {
				newGame();
				btStart.setLabel("Start");
			}

			tfState.setText("Start");
		} else if (source == btExit) {
			System.exit(0);
		} else {
			for (int i = 0; i < btn.length; i++) {
				if (source == btn[i]) {

					if (select == 2) { // ���� ��ư�� 2���̰� 3��° ��ư�� Ŭ������ ��
						// if(selIdx[0])
						if (selIdx[0] != -1) {
							btn[selIdx[0]].setLabel("");
							btn[selIdx[0]].setBackground(Color.ORANGE);
							btn[selIdx[0]].setEnabled(true);

							btn[selIdx[1]].setLabel("");
							btn[selIdx[1]].setBackground(Color.ORANGE);
							btn[selIdx[1]].setEnabled(true);
						}
						btn[i].setLabel(fruits[num[i] % 8]);
						btn[i].setBackground(Color.red);

						select = 0;
						
						score -= 10;	// Ʋ������ 10�� ����
						count++;

						tfState.setText(String.valueOf(count)+"�� �õ�, "+ String.valueOf(success)+"�� ����");
						tfScore.setText(String.valueOf(score));

					}

					btn[i].setLabel(fruits[num[i] % 8]);
					btn[i].setBackground(Color.red);
					btn[i].setEnabled(false);

					selIdx[select] = i; // 2

					if (select == 1) { // 2�� ��ư���õ��� ��
						
						
						// �ΰ��� ��ư�� ���� �����϶�
						if (num[selIdx[0]] % 8 == num[selIdx[1]] % 8) {	// �ΰ��� ��ư�� ���� �����϶�
							btn[selIdx[0]].setBackground(Color.DARK_GRAY);
							btn[selIdx[0]].setEnabled(false);

							btn[selIdx[1]].setBackground(Color.DARK_GRAY);
							btn[selIdx[1]].setEnabled(false);

							selIdx[0] = -1;
							selIdx[1] = -1;
							
							score += 100;	// ����� 100�� �߰�
							count++;
							success++;
							tfState.setText(String.valueOf(count)+"�� �õ�, "+ String.valueOf(success)+"�� ����");
							
							tfScore.setText(String.valueOf(score));
							
							if(success == 8){
								tfState.setText("Clear~!! (" + String.valueOf(count) + "�� �õ�)");
								
							}
						}
						select++;
					}

					if (select == 0) { // ��ư 1�� ����
						select++;
					}

				}
			}

		}
	}

	private void start() {
		// TODO Auto-generated method stub
		tfState.setText("Start");
		
		for (int i = 0; i < btn.length; i++) {
			btn[i].setLabel("");
			btn[i].setEnabled(true);
		}
	}

	private void newGame() { // �� ����
		// TODO Auto-generated method stub
		tfState.setText("New Game");
		shuffle();
		answer();
		for (int i = 0; i < btn.length; i++) {
			btn[i].setBackground(Color.orange);
			btn[i].setEnabled(false);
		}

		tfScore.setText("0");
		score = 0;
		count = 0;
		success = 0;


	}

	private void answer() {

		tfState.setText("Answer");

		for (int i = 0; i < btn.length; i++) {
			btn[i].setLabel(fruits[num[i] % 8]);
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