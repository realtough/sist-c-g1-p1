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

//�κ�ȭ���� ���� ���� ��� ����
public class LobbyMain extends JFrame implements ActionListener, G1Client {
	private Dimension gameMainSize = new Dimension(800, 600);
	private Dimension frameSize = new Dimension(1024, 768);
	private Dimension framePosition = new Dimension(Tools.centerX
			- frameSize.width / 2, Tools.centerY - frameSize.height / 2);

	// �α��ΰ� ���� �� ���̾�α� ����
	private LobbyLogin lLogin;

	// ����ȿ���� ī�巹�̾ƿ� ����
	private Border bdMainEdge = BorderFactory
			.createEtchedBorder(EtchedBorder.RAISED);
	private CardLayout card = new CardLayout();

	// �޴� ���� �� ����
	private JMenuBar jmb = new JMenuBar();
	private JMenu jmGameMenu = new JMenu("����");
	private JMenuItem jmLogin = new JMenuItem("�α���");
	private JMenuItem jmExit = new JMenuItem("����");

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

	private JButton jbGame1 = new JButton("����1");
	private JButton jbGame2 = new JButton("����2");
	private JButton jbGame3 = new JButton("����3");
	private JButton jbGame4 = new JButton("����4");

	// ä��â Ȯ�� ��� ����
	private JPanel jpChatButtonPanel = new JPanel();
	private JTextArea jtaChatScreen = new JTextArea();
	private JScrollPane jsChatScreenPane = new JScrollPane(jtaChatScreen);
	private JButton jbChatMaximize = new JButton("��");
	private JButton jbChatMinimize = new JButton("��");
	private JScrollBar jsBar;

	private boolean isChatMaximized = false;
	private UserInfoVO myVO;
	private ClientSender csThread;

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
		jsBar = jsChatScreenPane.getVerticalScrollBar();
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
		Tools.insert(jpChat, jsChatList, 0, 0, 1, 1, 1.0, 0.9);
		Tools.insert(jpChat, jtfChatInput, 0, 1, 1, 1, 1.0, 0.1);
		jpChatButtonPanel.add(jbChatMaximize);
		jpChatButtonPanel.add(jbChatMinimize);
		Tools.insert(jpChat, jpChatButtonPanel, 1, 0, 1, 2, 0.0, 0.5);

		// ���� ����â ��ġ
		jpRightTab.setLayout(new GridBagLayout());
		Tools.insert(jpRightTab, jpUserInfo, 0, 0, 1, 1, 0.5, 0.5);
		Tools.insert(jpRightTab, new JScrollPane(jtUserList), 0, 1, 1, 1, 0.5,
				0.5);

		// �г� ��ġ (����ȭ��, ���� ����â, ä��â)
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

		// ������ �⺻���� (��ġ�� �׻� �߾�, ������¡ �Ұ�)
		setBounds(framePosition.width, framePosition.height, frameSize.width,
				frameSize.height);
		lLogin = new LobbyLogin(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		// �׼��̺�Ʈ ����
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
					// �������� ����� "|"�� �ĺ��ڷ� �ϳ��� ���ڿ��� ������ �����Ƿ� �̸� �и��Ѵ�
					String userList[] = msgtemp[2].split("@");
					// ���̺��� �κ� ������ �Ұ����ϹǷ� ���̺� ������ �ٽ� ��ü ��������� �����Ѵ�
					for (int i = dtModel.getRowCount() - 1; i >= 0; i--) {
						dtModel.removeRow(i);
					}
					for (int i = 0; i < userList.length; i++) {
						String temp[] = { userList[i], "�ź�" };
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
			// socket�� output ��Ʈ���� write�Ѵ�
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