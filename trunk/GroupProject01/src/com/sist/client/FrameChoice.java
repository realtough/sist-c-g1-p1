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
	String[] fruits = { "사과", "딸기", "포도", "수박", "복숭아", "자두", "참외", "키위" };

	Random rNum = new Random();

	int select = 0; // 선택된 버튼의 수

	int[] selIdx = { 0, 0 }; // 선택된 버튼들의 인덱스
	
	int score = 0;	// 점수
	
	int count = 0;
	int success = 0;

	public FrameChoice() {
		super("FrameChoice");

		p1.setLayout(new GridLayout(4, 4, 10, 10));
		for (int i = 0; i < btn.length; i++) {
			// 버튼 생성
			btn[i] = new Button();
			btn[i].setEnabled(false);
			btn[i].setBackground(Color.orange);

			// 버튼 패널에 추가
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

					if (select == 2) { // 열린 버튼이 2개이고 3번째 버튼을 클릭했을 때
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
						
						score -= 10;	// 틀렸을시 10점 감점
						count++;

						tfState.setText(String.valueOf(count)+"번 시도, "+ String.valueOf(success)+"번 성공");
						tfScore.setText(String.valueOf(score));

					}

					btn[i].setLabel(fruits[num[i] % 8]);
					btn[i].setBackground(Color.red);
					btn[i].setEnabled(false);

					selIdx[select] = i; // 2

					if (select == 1) { // 2개 버튼선택됐을 때
						
						
						// 두개의 버튼이 같은 과일일때
						if (num[selIdx[0]] % 8 == num[selIdx[1]] % 8) {	// 두개의 버튼이 같은 과일일때
							btn[selIdx[0]].setBackground(Color.DARK_GRAY);
							btn[selIdx[0]].setEnabled(false);

							btn[selIdx[1]].setBackground(Color.DARK_GRAY);
							btn[selIdx[1]].setEnabled(false);

							selIdx[0] = -1;
							selIdx[1] = -1;
							
							score += 100;	// 정답시 100점 추가
							count++;
							success++;
							tfState.setText(String.valueOf(count)+"번 시도, "+ String.valueOf(success)+"번 성공");
							
							tfScore.setText(String.valueOf(score));
							
							if(success == 8){
								tfState.setText("Clear~!! (" + String.valueOf(count) + "번 시도)");
								
							}
						}
						select++;
					}

					if (select == 0) { // 버튼 1개 선택
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

	private void newGame() { // 새 게임
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

	private void shuffle() { // 섞기

		// 난수 생성
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