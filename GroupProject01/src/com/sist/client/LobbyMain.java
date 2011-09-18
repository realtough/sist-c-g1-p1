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
import com.sist.server.ChattingServer;

//�κ�ȭ���� ���� ���� ��� ����
public class LobbyMain extends JFrame implements ActionListener {

	Dimension frameSize = new Dimension(810, 645);
	Dimension framePosition = new Dimension(
			Tools.centerX - frameSize.width / 2, Tools.centerY
					- frameSize.height / 2);

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

	JPanel jpChat = new JPanel();
	JTextArea jtaChatList = new JTextArea();
	JTextField jtfChatInput = new JTextField();
	
	JScrollPane jsChatList = new JScrollPane(jtaChatList);
	JScrollBar jbChatListBar;

	boolean sendSuspend = true;

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
		jpGameMain.setBackground(Color.BLUE);
		jtaChatList.setLineWrap(true);
		// Test//

		// ä��â ����
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
		jpGameMain.setBounds(5, 5, 600, 430);
		jpMain.add(jpGameMain);
		jpChat.setBounds(5, 440, 602, 153);
		jpMain.add(jpChat);
		jpRightTab.setBounds(610, 5, 190, 585);
		jpMain.add(jpRightTab);
		jpMain.setBorder(bdMainEdge);
		add(jpMain);

		// ������ �⺻���� (��ġ�� �׻� �߾�, ������¡ �Ұ�)
		setBounds(framePosition.width, framePosition.height, frameSize.width,
				frameSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		// ���� ����� �α���â ���
		new LobbyLogin(this);

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
			ClientReceiver crThread = new ClientReceiver(socket);
			ClientSender csThread = new ClientSender(userName, socket);
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

	public void appendChatLog(String msg) {
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
			new LobbyLogin(this);
		} else if (ob == jmExit) {
			System.exit(0);
		}

		if (ob == jtfChatInput) {
			sendSuspend = false;
		}
	}

	class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream dis;

		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (dis != null) {
					// appendChatLog(dis.readUTF());
					classfyMessage(dis.readUTF());
				}
			} catch (IOException ioe) {
				// TODO: handle exception
				ioe.printStackTrace();
			}
		}

		private void classfyMessage(String msg) {
			String msgtemp[] = msg.split(" ", 3);
			if (msgtemp[0].equals("[��������]")) {				
				// �������� ����� "|"�� �ĺ��ڷ� �ϳ��� ���ڿ��� ������ �����Ƿ� �̸� �и��Ѵ�
				String userList[] = msgtemp[1].split("\\|");
				// ���̺��� �κ� ������ �Ұ����ϹǷ� ���̺� ������ �ٽ� ��ü ��������� �����Ѵ�
				for (int i = 0; i < dtModel.getRowCount(); i++) {
					dtModel.removeRow(i);
				}				
				for (int i = 0; i < userList.length; i++) {
					String temp[] = {userList[i], "�ź�" };
					dtModel.addRow(temp);
				}
			} else {
				appendChatLog(msg);
			}
		}
	}

	class ClientSender extends Thread {
		Socket socket;
		DataOutputStream dos;
		String name;

		public ClientSender(String userName, Socket socket) {
			// socket�� output ��Ʈ���� write�Ѵ�
			this.socket = socket;
			this.name = userName;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				if (dos != null) {
					dos.writeUTF(name); // ���� ���ӽ� �̸��� ���� �����Ѵ�
				}
				while (dos != null) {
					if (!sendSuspend) {
						dos.writeUTF(jtfChatInput.getText());
						jtfChatInput.setText("");
						sendSuspend = true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
