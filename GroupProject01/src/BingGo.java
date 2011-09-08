

//내꺼

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;

public class BingGo extends JFrame implements ActionListener{
	/**
	 * @param args
	 */
	int num[][] = new int[5][5];
	JButton btnMy[][] = new JButton[5][5];
	JButton btnCom[][] = new JButton[5][5];
	boolean myBool[][] = new boolean[5][5];
	boolean comBool[][] = new boolean[5][5];
	boolean bool = false;
	int myCount = 0;
	int comCount = 0;
	int win = 0;
	int lose = 0;
	JButton btnNew = new JButton("새게임");
	JButton btnExit = new JButton("종료");
	JButton btnSet = new JButton("기권하기");
	JLabel lblWin = new JLabel("Win :",JLabel.CENTER);
	JLabel lblLose = new JLabel("Lose :",JLabel.CENTER);
	JLabel lblWinS = new JLabel("",JLabel.CENTER);
	JLabel lblLoseS = new JLabel("",JLabel.CENTER);
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	JPanel p4 = new JPanel();
	JPanel p5 = new JPanel();
	JPanel p6 = new JPanel();
	JPanel p7 = new JPanel();
	JPanel p8 = new JPanel();
//	JTextField tf = new JTextField();
	//JButton bt1 = new JButton("입력");
	//JTextArea ta = new JTextArea();
	
	public BingGo(){
		super("BingGo Game");
		SLayout();
	}

	
	private void SLayout(){

		p1.setLayout(new GridLayout(5,5));
		p2.setLayout(new GridLayout(5,5));

		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				btnMy[i][j] = new JButton();
				btnCom[i][j]= new JButton();
				p1.add(btnMy[i][j]);
				p2.add(btnCom[i][j]);
				btnMy[i][j].addActionListener(this);
				btnCom[i][j].addActionListener(this);
			}
		}
		
		p3.setLayout(new GridLayout(1,2,10,0));
		p3.add(p1);
		p3.add(p2);
		
		p4.setLayout(new GridLayout(2,2));
		p4.add(lblWin);
		p4.add(lblWinS);
		p4.add(lblLose);
		p4.add(lblLoseS);
		
		p5.setLayout(new GridLayout(1,4));
		p5.add(btnNew);
		p5.add(btnSet);
		p5.add(btnExit);
		p5.add(p4);
		
		this.setLayout(new BorderLayout(0,10));
		this.add("Center",p3);
		this.add("South",p5);
		
		
	
		this.setBounds(300, 300, 600, 430);
		this.setVisible(true);
		
		btnNew.addActionListener(this);
		btnSet.addActionListener(this);
		btnExit.addActionListener(this);
		
		
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BingGo();
	}

	
	//액션
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object ob = e.getSource();
		if(ob == btnNew){
			newGame();			
		}else if(ob == btnSet){
			clear();
			this.setTitle("BingGo Game - 기권패했습니다.");
			lose++;
		}else if(ob == btnExit){
			System.exit(0);
		}else{
			if(!bool){
				for(int i=0; i<5; i++){
					for(int j =0; j<5; j++){
						if(ob==btnMy[i][j] && btnMy[i][j].getText()!=""){
							rule(btnMy[i][j].getText());
							btnMy[i][j].setEnabled(false);
							myBool[i][j] = true;
							result();
							
							if (!bool){

								computer();
							}
						}
					}
				}
			}
			
		}
	}

	//컴퓨터가 선택
	private void computer() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {}
		Random ran = new Random();
		int ran1=0;
		int ran2=0;
		int ran3=0;
		do{
			ran1 = ran.nextInt(50)+1;
			ran2 = ran.nextInt(5);
			ran3 = ran.nextInt(5);
			if(ran1 == num[ran2][ran3] && !comBool[ran2][ran3])
				break;
		}while(true);
		
		for(int i=0; i<5; i++){
			for(int j=0; j<5; j++){
				String s = btnMy[i][j].getText();
				int t = Integer.parseInt(s);
				if(t == num[ran2][ran3]){
					btnMy[i][j].setEnabled(false);
					myBool[i][j] = true;
					btnCom[ran2][ran3].setEnabled(false);
					comBool[ran2][ran3] = true;
				}
			}
		}
		result();
		
	}

	//초기 상태로 돌아감
	private void clear() {
		// TODO Auto-generated method stub
		win = 0;
		lose = 0;
		lblWinS.setText("");
		lblLoseS.setText("");
		
		for(int i=0; i<5; i++){
			for(int j=0; j<5; j++){
				btnMy[i][j].setEnabled(true);
				btnCom[i][j].setEnabled(true);
				btnMy[i][j].setText("");
				myBool[i][j] = false;
				comBool[i][j] = false;
			}
		}
		this.setTitle("BingGo Game");
	}
	
	//게임 결과 확인/출력
	private void result() {
		myCount = 0;
		comCount = 0;
		for(int i=0; i<5; i++){
			if(myBool[i][0]&&myBool[i][1]&&myBool[i][2]&&myBool[i][3]&myBool[i][4]){
				myCount++;
			}
			if(comBool[i][0]&&comBool[i][1]&&comBool[i][2]&&comBool[i][3]&comBool[i][4]){
				comCount++;		
			}
			if(myBool[0][i]&&myBool[1][i]&&myBool[2][i]&&myBool[3][i]&myBool[4][i]){
				myCount++;
			}
			if(comBool[0][i]&&comBool[1][i]&&comBool[2][i]&&comBool[3][i]&comBool[4][i]){
				comCount++;		
			}
		}
		if(myBool[0][0]&&myBool[1][1]&&myBool[2][2]&&myBool[3][3]&myBool[4][4]){
			myCount++;
		}		
		if(comBool[0][0]&&comBool[1][1]&&comBool[2][2]&&comBool[3][3]&comBool[4][4]){
			comCount++;
		}
		if(myBool[0][4]&&myBool[1][3]&&myBool[2][2]&&myBool[3][1]&myBool[4][0]){
			myCount++;
		}		
		if(comBool[0][4]&&comBool[1][3]&&comBool[2][2]&&comBool[3][1]&comBool[4][0]){
			comCount++;  
		}
		
		if(myCount>=5 && comCount>=5){
			this.setTitle("BingGo Game - 비겼습니다.");
			bool = true;
			return;
		}
		if(myCount>=5){
			this.setTitle("BingGo Game - 이겼습니다.");
			win++;
			lblWinS.setText(String.valueOf(win));
			bool = true;
		}else if(comCount>=5){
			this.setTitle("BingGo Game - 졌습니다.");
			lose++;
			lblLoseS.setText(String.valueOf(lose));
			bool = true;
		}
		
	}

	//버튼을 눌렸을때 버튼 변경
	private void rule(String text) {
		// TODO Auto-generated method stub
		int su = Integer.parseInt(text);
		for(int i=0; i<5; i++){
			for(int j=0; j<5; j++){
				if(num[i][j] == su){
					btnCom[i][j].setEnabled(false);
					comBool[i][j] = true;
				}
			}
		}
	}

	//새로운 게임 시작
	private void newGame() {
		// TODO Auto-generated method stub
		bool = false;

		randomNum();
		for(int i=0; i<5; i++){
			for(int j=0; j<5; j++){
				btnMy[i][j].setEnabled(true);
				btnCom[i][j].setEnabled(true);
				btnMy[i][j].setText(String.valueOf(num[i][j]));
				myBool[i][j] = false;
				comBool[i][j] = false;
			}
		}
		randomNum();
		this.setTitle("BingGo Game");
				
	}

	//num[][]을 썩음
	private void randomNum() {
		// TODO Auto-generated method stub
		Random ran = new Random();
		int x = 1;
		int storage;
		int ran1;
		int ran2;
		for(int i=0; i<5; i++){
			for(int j=0; j<5; j++){
					num[i][j] = x;
					x++;
			}
		}
		for(int i=0; i<25; i++){
			for(int j=0; j<25; j++){
				ran1 = ran.nextInt(5);
				ran2 = ran.nextInt(5);
				storage = num[0][0];
				num[0][0] = num[ran1][ran2];
				num[ran1][ran2] = storage;
				x++;
			}
		}
	}
}
