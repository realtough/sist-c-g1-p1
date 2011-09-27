package com.sist.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import com.sist.common.Tools;

//�α��� ������ ���. �Է¹��� ���̵�� �н����带 �ϳ��� ���ڿ��� ���� ����
//���� �޾����� �����κ��� �г����� ���޹޾� LobbyMain�� ����
public class LobbyLogin extends JDialog implements ActionListener {
	private Dimension dSize = new Dimension(250, 150);
	private Dimension dPosition = new Dimension(getParent().getX()
			+ getParent().getSize().width / 2 - dSize.width / 2, getParent()
			.getY() + getParent().getSize().height / 2 - dSize.height / 2);
	private JPanel jpButtonPanel = new JPanel();
	private JLabel jlID = new JLabel("���̵�   ", JLabel.RIGHT);
	private JLabel jlPW = new JLabel("��й�ȣ   ", JLabel.RIGHT);
	private JTextField jtfID = new JTextField(10);
	private JPasswordField jpfPW = new JPasswordField(8);
	private JButton jbAccept = new JButton("�α���");
	private JButton jbCancel = new JButton("������");
	private JButton jbRegister = new JButton("����");
	private LobbyMain lobby;
	private String id;
	private String pw;
//	private String idpwMessage;
	private String userName;
	boolean isSuspend = true;
	boolean isStop = false;
	private ClientReceiver crThread;
	private ClientSender csThread;
	private LobbyRegister lbRegister;

	public LobbyLogin(LobbyMain parent) {
		super(parent, "�α��� �ϼ���", ModalityType.APPLICATION_MODAL);
		lobby = parent;
		setLayout(new GridBagLayout());
		jpButtonPanel.setLayout(new GridLayout(1, 3, 2, 2));

		jpButtonPanel.add(jbAccept);
		jpButtonPanel.add(jbRegister);
		jpButtonPanel.add(jbCancel);
		
		Tools.insert(this, jlID, 0, 0, 1, 1, 0.0, 0.5, 5);
		Tools.insert(this, jlPW, 0, 1, 1, 1, 0.0, 0.5, 5);
		Tools.insert(this, jtfID, 1, 0, 2, 1, 0.9, 0.5, 5);
		Tools.insert(this, jpfPW, 1, 1, 2, 1, 0.9, 0.5, 5);
		Tools.insert(this, jpButtonPanel, 0, 3, 4, 1, 0.5, 0.5, 5);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(dPosition.width, dPosition.height, dSize.width, dSize.height);

		// �̺�Ʈ ���
		jtfID.addActionListener(this);
		jpfPW.addActionListener(this);
		jbAccept.addActionListener(this);
		jbRegister.addActionListener(this);
		jbCancel.addActionListener(this);

		setResizable(false);
	}

	public void lobbyLoginStart() {
		try {
			Socket socket = new Socket(Tools.serverIp, Tools.LOGIN_SERVER_PORT);
			userName = (socket.getLocalAddress() + ":" + socket.getLocalPort())
					.substring(1);
			crThread = new ClientReceiver(socket);
			csThread = new ClientSender(userName, socket);
			crThread.start();
			csThread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "�α��� ������ ������ �� �����ϴ�. ���� ���¸� Ȯ���ϼ���");
		}
	}

	private void userLogin() {
		// ���̵� ������
		// ���̵�� ������ ��й�ȣ�� Ʋ����
		// ������ ��� �ʿ�
		if (checkInfo()) {
			String idpwmsg = "/login " + id + " " + pw;
			// System.out.println("idpw : " + idpwMessage);
//			isSuspend = false;
			sendMessage(idpwmsg);
		}
	}

	private boolean checkInfo() {
		boolean isCorrect = false;
		id = jtfID.getText().trim();
		pw = String.valueOf(jpfPW.getPassword());

		if (id.length() == 0) {
			JOptionPane.showMessageDialog(this, "���̵� �Է��ϼ���");
		} else if (pw.length() == 0) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �Է��ϼ���");
		} else {
			isCorrect = true;
		}
		return isCorrect;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jbAccept) {
			userLogin();
		} else if (ob == jbCancel) {			
			isStop = true;
			setVisible(false);
			dispose();
		} else if (ob == jbRegister) {
			setVisible(false);
//			new LobbyRegister(this).setVisible(true);
			lbRegister = new LobbyRegister(this);
			lbRegister.setVisible(true);
		} else if (ob == jtfID) {
			jpfPW.requestFocus();
		} else if (ob == jpfPW) {
			userLogin();
		}
	}

	public void clearForm(){
		jtfID.setText("");
		jpfPW.setText("");
	}

	public void sendMessage(String msg){
		csThread.resumeSend(msg);
	}
	
	private void classfyMessage(String msg) {
//		System.out.println("Receive : " + msg);
		String msgtemp[] = msg.split("#", 3);
		if (msgtemp[0].equals("[login]")) {
			switch (Integer.parseInt(msgtemp[1])) {
			case 11: // ���̵�� �н����� ��ġ �α��� ����
				lobby.clientStart(msgtemp[2]);
				isStop = true;
				setVisible(false);
				dispose();
				break;
			case 12:
				JOptionPane.showMessageDialog(this, "��й�ȣ�� Ʋ���ϴ�");
				break;
			case 22:
				JOptionPane.showMessageDialog(this, "���̵� Ʋ���ϴ�");
				break;
			case 33:
				JOptionPane.showMessageDialog(this, "������ ���� �߻� - â�� �ݰ� �ٽ� �õ��ϼ���");
				break;
			}
		} else if(msgtemp[0].equals("[regist]")){			
			if(msgtemp[1].equals("check")){			
				switch(Integer.parseInt(msgtemp[2])){
				case 11:
					//Ȯ�ε� ���					
					JOptionPane.showMessageDialog(lbRegister, "Ȯ�� �Ǿ����ϴ�");
					lbRegister.unlockInput();
					break;
				case 22:
					//�ߺ��� ���� ���			
					JOptionPane.showMessageDialog(lbRegister, "�ߺ� �˴ϴ�. �ٽ� �Է��ϼ���");
					//lbRegister.clearNickForm();
					break;
				}
			}		
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
					classfyMessage(dis.readUTF());
				}
			} catch (IOException ioe) {
				// TODO: handle exception
				ioe.printStackTrace();
			}
		}
	}

	class ClientSender extends Thread {
		private Socket socket;
		private DataOutputStream dos;
		private String name;
		private String message;
		private boolean isSenderSuspend;
		
		public void suspendSend(){
			message = "";
			isSenderSuspend = true;			
		}
		
		public void resumeSend(String msg){
			message = msg;
			isSenderSuspend = false;
		}
		
		public ClientSender(String userName, Socket socket) {
			// socket�� output ��Ʈ���� write�Ѵ�
			suspendSend();
			this.socket = socket;
			this.name = userName;
			try {
				dos = new DataOutputStream(this.socket.getOutputStream());
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
					if (!isSenderSuspend) {
//						System.out.println("Send : " + message);
						dos.writeUTF(message);						
						suspendSend();
					} else {
						repaint();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
