package com.sist.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import com.sist.common.*;

//로비화면의 실제 폼과 기능 구현
public class LobbyMain extends JFrame implements ActionListener, G1Client {
	Dimension gameMainSize = new Dimension(800, 600);
	Dimension frameSize = new Dimension(1024, 768);
	Dimension framePosition = new Dimension(
			Tools.centerX - frameSize.width / 2, Tools.centerY
					- frameSize.height / 2);

	// 로그인과 가입 폼 다이얼로그 선언
	public LobbyLogin lLogin;
	public LobbyRegister lRegister;

	// 보더효과와 카드레이아웃 설정
	Border bdMainEdge = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	CardLayout card = new CardLayout();

	// 메뉴 선언 및 생성
	JMenuBar jmb = new JMenuBar();
	JMenu jmGameMenu = new JMenu("게임");
	JMenuItem jmLogin = new JMenuItem("로그인");
	JMenuItem jmExit = new JMenuItem("종료");

	JPanel jpMain = new JPanel();
	JPanel jpRightTab = new JPanel();
	JPanel jpUserInfo = new JPanel();
	JTable jtUserList;
	DefaultTableModel dtModel;

	JPanel jpGameMain = new JPanel();
	JPanel jpGameSelect = new JPanel();

	JPanel jpChat = new JPanel();
	JTextArea jtaChatList = new JTextArea();
	JTextField jtfChatInput = new JTextField();

	JScrollPane jsChatList = new JScrollPane(jtaChatList);
	JScrollBar jbChatListBar;

	JButton jbGame1 = new JButton("게임1");
	JButton jbGame2 = new JButton("게임2");
	JButton jbGame3 = new JButton("게임3");
	JButton jbGame4 = new JButton("게임4");

	ClientOperator chatThread;
//	ClientOperator loginThread;
//	public String userName;
//	public Socket socket;

	public LobbyMain() {
		super("Mini Game");

		// 메뉴바 설정
		jmGameMenu.add(jmLogin);
		jmGameMenu.add(jmExit);
		jmb.add(jmGameMenu);
		setJMenuBar(jmb);

		// 접속 유저 정보 테이블 생성
		String col[] = { "닉네임", "계급" };
		String row[][] = new String[0][2];
		dtModel = new DefaultTableModel(row, col) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};

		jtUserList = new JTable(dtModel);

		// Test//
		jpRightTab.setBackground(Color.RED);
		// jpChat.setBackground(Color.GREEN);
		// jpGameMain.setBackground(Color.BLUE);
		// Test//

		// GameSelect패널 설정
		jpGameSelect.setLayout(new GridLayout(2, 2, 5, 5));
		jpGameSelect.add(jbGame1);
		jpGameSelect.add(jbGame2);
		jpGameSelect.add(jbGame3);
		jpGameSelect.add(jbGame4);

		// 채팅창 설정
		jtaChatList.setLineWrap(true);
		jbChatListBar = jsChatList.getVerticalScrollBar();
		jtaChatList.setEditable(false);
		jpChat.setLayout(new GridBagLayout());
		Tools.insert(jpChat, jsChatList, 0, 0, 1, 1, 0.5, 0.9);
		Tools.insert(jpChat, jtfChatInput, 0, 1, 1, 1, 0.5, 0.1);

		// 유저 정보창 배치
		jpRightTab.setLayout(new GridBagLayout());
		Tools.insert(jpRightTab, jpUserInfo, 0, 0, 1, 1, 0.5, 0.5);
		Tools.insert(jpRightTab, new JScrollPane(jtUserList), 0, 1, 1, 1, 0.5,
				0.5);

		// 패널 배치 (메인화면, 우측 정보창, 채팅창)
		jpMain.setLayout(null);
		jpGameMain.setLayout(card);
		jpGameMain.add("GAMESELECT", jpGameSelect);
		jpGameMain.setBounds(5, 5, gameMainSize.width, gameMainSize.height);
		jpMain.add(jpGameMain);
		jpChat.setBounds(5, 605, 800, 100);
		jpMain.add(jpChat);
		jpRightTab.setBounds(810, 5, 200, 700);
		jpMain.add(jpRightTab);
		jpMain.setBorder(bdMainEdge);
		add(jpMain);

		// 프레임 기본설정 (위치는 항상 중앙, 리사이징 불가)
		setBounds(framePosition.width, framePosition.height, frameSize.width,
				frameSize.height);
		lLogin = new LobbyLogin(this);
		lRegister = new LobbyRegister(lLogin);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		// 액션이벤트 설정
		jtfChatInput.addActionListener(this);
		jmLogin.addActionListener(this);
		jmExit.addActionListener(this);
	}

	// 네트워크 관련 기능 초기화와 시작
	// 서버아이피와 포트는 Tools클래스에서 정의
	public void startChat(String userName) {
		try {
			Socket socket = new Socket(Tools.serverIp, Tools.MAIN_SERVER_PORT);
			chatThread = new ClientOperator(this, userName, socket);
			chatThread.start();
			chatThread.sendMessage("/first");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void appendChatLog(String msg) {
		jtaChatList.append(msg + "\n");
		jbChatListBar.setValue(jbChatListBar.getMaximum());
	}

	public static void main(String[] args) {
		new LobbyMain();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object ob = e.getSource();

		if (ob == jmLogin) {
			lLogin.lobbyLoginStart();
			lLogin.setVisible(true);
		} else if (ob == jmExit) {
			System.exit(0);
		}
		if (ob == jtfChatInput) {
			chatThread.sendMessage(jtfChatInput.getText().trim());
			jtfChatInput.setText("");
		}
	}

	public void classfyMessage(String msg) {
		String msgtemp[] = msg.split("@");
//		System.out.println("LM "+msg);
		if (msgtemp[0].equals("[접속유저]")) {
			// 접속유저 목록은 "|"를 식별자로 하나의 문자열로 합쳐져 있으므로 이를 분리한다
			String userList[] = msgtemp[1].split("\\|");
			// 테이블은 부분 수정이 불가능하므로 테이블 삭제후 다시 전체 유저목록을 삽입한다
//			System.out.println(msgtemp[1]);
			if (dtModel.getRowCount() > 0) {
				for (int i = dtModel.getRowCount() - 1; i >= 0; i--) {
					dtModel.removeRow(i);				
				}
			}
			for (int i = 0; i < userList.length; i++) {				
//				System.out.println(userList[i] + "테이블 입력");
				String temp[] = { userList[i], "신병" };				
				dtModel.addRow(temp);
			}
		} else {
			appendChatLog(msg);
		}
	}
}