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

import com.sist.common.ClientOperator;
import com.sist.common.Tools;

//�κ�ȭ���� ���� ���� ��� ����
public class LobbyMain extends JFrame implements ActionListener {
	Dimension gameMainSize = new Dimension(800, 600);
	Dimension frameSize = new Dimension(1024, 768);
	Dimension framePosition = new Dimension(
			Tools.centerX - frameSize.width / 2, Tools.centerY
					- frameSize.height / 2);

	// �α��ΰ� ���� �� ���̾�α� ����
	LobbyLogin lLogin;
	LobbyRegister lRegister;
	ChatOperator chOperator;

	// ����ȿ���� ī�巹�̾ƿ� ����
	Border bdMainEdge = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	CardLayout card = new CardLayout();

	// �޴� ���� �� ����
	JMenuBar jmb = new JMenuBar();
	JMenu jmGameMenu = new JMenu("����");
	JMenuItem jmLogin = new JMenuItem("�α���");
	JMenuItem jmExit = new JMenuItem("����");

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

	JButton jbGame1 = new JButton("����1");
	JButton jbGame2 = new JButton("����2");
	JButton jbGame3 = new JButton("����3");
	JButton jbGame4 = new JButton("����4");

	boolean sendMessage = false;

	public LobbyMain() {
		super("Mini Game");

		// �޴��� ����
		jmGameMenu.add(jmLogin);
		jmGameMenu.add(jmExit);
		jmb.add(jmGameMenu);
		setJMenuBar(jmb);

		// ���� ���� ���� ���̺� ����
		String col[] = { "�г���", "���" };
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

		// GameSelect�г� ����
		jpGameSelect.setLayout(new GridLayout(2, 2, 5, 5));
		jpGameSelect.add(jbGame1);
		jpGameSelect.add(jbGame2);
		jpGameSelect.add(jbGame3);
		jpGameSelect.add(jbGame4);

		// ä��â ����
		jtaChatList.setLineWrap(true);
		jbChatListBar = jsChatList.getVerticalScrollBar();
		jtaChatList.setEditable(false);
		jpChat.setLayout(new GridBagLayout());
		Tools.insert(jpChat, jsChatList, 0, 0, 1, 1, 0.5, 0.9);
		Tools.insert(jpChat, jtfChatInput, 0, 1, 1, 1, 0.5, 0.1);

		// ���� ����â ��ġ
		jpRightTab.setLayout(new GridBagLayout());
		Tools.insert(jpRightTab, jpUserInfo, 0, 0, 1, 1, 0.5, 0.5);
		Tools.insert(jpRightTab, new JScrollPane(jtUserList), 0, 1, 1, 1, 0.5,
				0.5);

		// �г� ��ġ (����ȭ��, ���� ����â, ä��â)
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

		// ������ �⺻���� (��ġ�� �׻� �߾�, ������¡ �Ұ�)
		setBounds(framePosition.width, framePosition.height, frameSize.width,
				frameSize.height);
		lLogin = new LobbyLogin(this);
		lRegister = new LobbyRegister(lLogin);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		// �׼��̺�Ʈ ����
		jtfChatInput.addActionListener(this);
		jmLogin.addActionListener(this);
		jmExit.addActionListener(this);
	}

	public void clientStart() {
		String serverIp = "localhost";
		String userName = "ȫ�浿";
		int serverPort = 10000;
		try {
			Socket socket = new Socket(serverIp, serverPort);
			chOperator = new ChatOperator(userName, socket);
			chOperator.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void appendChatLog(String msg) {
		jtaChatList.append(msg + "\n");
		jbChatListBar.setValue(jbChatListBar.getMaximum());
	}

	public static void main(String[] args) {
		new LobbyMain().clientStart();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object ob = e.getSource();

		if (ob == jmLogin) {
			lLogin.setVisible(true);
		} else if (ob == jmExit) {
			System.exit(0);
		}

		if (ob == jtfChatInput) {
			System.out.println("Chat input");
			chOperator.sendSuspend = false;
		}
	}

	class ChatOperator extends ClientOperator {

		public ChatOperator(String userName, Socket socket) {
			super(userName, socket);
		}

		public void run() {
			while (true) {

				if(!sendSuspend){					
					System.out.println("send called in thread");
					outputString = jtfChatInput.getText().trim();
					jtfChatInput.setText("");
					send();														
				}
				
				if (doStream != null) {		
					
				}
				
				if (diStream != null) {
					if (!receiveSuspend)
						receive();
					classfyMessage(inputString);
				}				
			}
		}

		public void classfyMessage(String msg) {
			String msgtemp[] = msg.split(" ", 3);
			if (msgtemp[0].equals("/sys")) {
				// �������� ����� "|"�� �ĺ��ڷ� �ϳ��� ���ڿ��� ������ �����Ƿ� �̸� �и��Ѵ�
				String userList[] = msgtemp[1].split("\\|");
				// ���̺��� �κ� ������ �Ұ����ϹǷ� ���̺� ������ �ٽ� ��ü ��������� �����Ѵ�
				for (int i = dtModel.getRowCount() - 1; i >= 0; i--) {
					dtModel.removeRow(i);
				}
				for (int i = 0; i < userList.length; i++) {
					String temp[] = { userList[i], "�ź�" };
					dtModel.addRow(temp);
				}
			} else {
				appendChatLog(msg);
			}
		}
	}
}