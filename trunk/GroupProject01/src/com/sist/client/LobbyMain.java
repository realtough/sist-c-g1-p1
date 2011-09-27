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

import com.sist.common.Tools;
import com.sist.common.UserInfoVO;

//로비화면의 실제 폼과 기능 구현
public class LobbyMain extends JFrame implements ActionListener, G1Client {
	private Dimension gameMainSize = new Dimension(800, 600);
	private Dimension frameSize = new Dimension(1024, 768);
	private Dimension framePosition = new Dimension(Tools.centerX
			- frameSize.width / 2, Tools.centerY - frameSize.height / 2);

	// 로그인과 가입 폼 다이얼로그 선언
	private LobbyLogin lLogin;

	// 보더효과와 카드레이아웃 설정
	private Border bdMainEdge = BorderFactory
			.createEtchedBorder(EtchedBorder.RAISED);
	private CardLayout card = new CardLayout();

	// 메뉴 선언 및 생성
	private JMenuBar jmb = new JMenuBar();
	private JMenu jmGameMenu = new JMenu("게임");
	private JMenuItem jmLogin = new JMenuItem("로그인");
	private JMenuItem jmExit = new JMenuItem("종료");

	private JPanel jpMain = new JPanel();
	private JPanel jpRightTab = new JPanel();
	private JPanel jpUserInfo = new JPanel();
	private JTable jtUserList;
	private DefaultTableModel dtModel;

	private JPanel jpGameMain = new JPanel();
	private JPanel jpGameSelect = new JPanel();

	private JPanel jpChat = new JPanel();
	private JTextArea jtaChatList = new JTextArea();
	private JTextField jtfChatInput = new JTextField();

	private JScrollPane jsChatList = new JScrollPane(jtaChatList);
	private JScrollBar jbChatListBar;

	private JButton jbGame1 = new JButton("게임1");
	private JButton jbGame2 = new JButton("게임2");
	private JButton jbGame3 = new JButton("게임3");
	private JButton jbGame4 = new JButton("게임4");

	// 채팅창 확대 축소 관련
	private JPanel jpChatButtonPanel = new JPanel();
	private JTextArea jtaChatScreen = new JTextArea();
	private JScrollPane jsChatScreenPane = new JScrollPane(jtaChatScreen);
	private JButton jbChatMaximize = new JButton("△");
	private JButton jbChatMinimize = new JButton("▽");
	private JScrollBar jsBar;

	private boolean isChatMaximized = false;
	private UserInfoVO myVO;
	private ClientSender csThread;

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
		jsBar = jsChatScreenPane.getVerticalScrollBar();
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
		Tools.insert(jpChat, jsChatList, 0, 0, 1, 1, 1.0, 0.9);
		Tools.insert(jpChat, jtfChatInput, 0, 1, 1, 1, 1.0, 0.1);
		jpChatButtonPanel.add(jbChatMaximize);
		jpChatButtonPanel.add(jbChatMinimize);
		Tools.insert(jpChat, jpChatButtonPanel, 1, 0, 1, 2, 0.0, 0.5);

		// 유저 정보창 배치
		jpRightTab.setLayout(new GridBagLayout());
		Tools.insert(jpRightTab, jpUserInfo, 0, 0, 1, 1, 0.5, 0.5);
		Tools.insert(jpRightTab, new JScrollPane(jtUserList), 0, 1, 1, 1, 0.5,
				0.5);

		// 패널 배치 (메인화면, 우측 정보창, 채팅창)
		jpMain.setLayout(null);
		jpGameMain.setLayout(card);
		jpGameMain.add("GAMESELECT", jpGameSelect);
		jpGameMain.add("CHATSCREEN", jsChatScreenPane);
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		// 액션이벤트 설정
		jtfChatInput.addActionListener(this);
		jmLogin.addActionListener(this);
		jmExit.addActionListener(this);
		jbChatMaximize.addActionListener(this);
		jbChatMinimize.addActionListener(this);
	}

	public void clientStart(String userName) {
		try {
			myVO = Tools.stringToUserInfo(userName);
			Socket socket = new Socket(Tools.serverIp, Tools.MAIN_SERVER_PORT);
			ClientReceiver crThread = new ClientReceiver(socket);
			csThread = new ClientSender(myVO.getNickname(), socket);
			crThread.start();
			csThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg) {
		csThread.resumeSend(msg);
		jtfChatInput.setText("");
	}

	private void appendChatLog(String msg) {
		jtaChatList.append(msg + "\n");
		jtaChatScreen.append(msg + "\n");
		jbChatListBar.setValue(jbChatListBar.getMaximum());
		jsBar.setValue(jsBar.getMaximum());
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
			lLogin.clearForm();
		} else if (ob == jmExit) {
			System.exit(0);
		}
		if (ob == jtfChatInput) {
			sendMessage(jtfChatInput.getText().trim());
		}
		if (ob == jbChatMaximize) {
			card.show(jpGameMain, "CHATSCREEN");
		} else if (ob == jbChatMinimize) {
			card.show(jpGameMain, "GAMESELECT");
		}
	}

	class ClientReceiver extends Thread {
		private Socket socket;
		private BufferedReader bfReader;

		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				bfReader = new BufferedReader(new InputStreamReader(
						this.socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (bfReader != null) {
					classfyMessage(bfReader.readLine());
				}
			} catch (IOException ioe) {
				// TODO: handle exception
				ioe.printStackTrace();
			} finally {
				closeStream();
			}
		}

		private void closeStream() {
			try {
				bfReader.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void classfyMessage(String msg) {
			String msgtemp[] = msg.split("#", 3);
			if (msgtemp[0].equals("[server]")) {
				if (msgtemp[1].equals("userlist")) {
					// 접속유저 목록은 "|"를 식별자로 하나의 문자열로 합쳐져 있으므로 이를 분리한다
					String userList[] = msgtemp[2].split("@");
					// 테이블은 부분 수정이 불가능하므로 테이블 삭제후 다시 전체 유저목록을 삽입한다
					for (int i = dtModel.getRowCount() - 1; i >= 0; i--) {
						dtModel.removeRow(i);
					}
					for (int i = 0; i < userList.length; i++) {
						String temp[] = { userList[i], "신병" };
						dtModel.addRow(temp);
					}
				} else if (msgtemp[1].equals("userinfo")) {

				}
			} else {
				appendChatLog(msg);
			}
		}
	}

	class ClientSender extends Thread {
		private Socket socket;
		private BufferedWriter bfWriter;
		private String name;
		private boolean isSenderSuspend;
		private String outputMessage = "";

		public ClientSender(String userName, Socket socket) {
			// socket의 output 스트림에 write한다
			this.socket = socket;
			this.name = userName;
			try {
				bfWriter = new BufferedWriter(new OutputStreamWriter(
						this.socket.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				if (bfWriter != null) {
					bfWriter.write(name + "\n");
					if (bfWriter != null)
						bfWriter.flush();
					suspendSend();
				}
				while (bfWriter != null) {
					if (!isSenderSuspend) {
						bfWriter.write(outputMessage + "\n");
						if (bfWriter != null)
							bfWriter.flush();
						suspendSend();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				closeStream();
			}
		}

		public void suspendSend() {
			outputMessage = "";
			isSenderSuspend = true;
		}

		public void resumeSend(String msg) {
			outputMessage = msg;
			isSenderSuspend = false;
		}

		private void closeStream() {
			try {
				bfWriter.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}// ClientSender
}// class