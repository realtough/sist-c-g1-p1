package com.sist.server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import com.sist.common.Tools;

public class ChattingServer extends JFrame implements ActionListener {
	Dimension dSize = new Dimension(640, 600);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);
	// boolean isServerOn = false;
	private HashMap<String, DataOutputStream> clients;

	private JTextArea jtaServerLog = new JTextArea();
	private JScrollPane jsPane = new JScrollPane(jtaServerLog);
	private JTextField jtfServerInput = new JTextField();

	private JMenuBar jmb = new JMenuBar();
	private JMenu jmServer = new JMenu("����");
	private JMenuItem jmStart = new JMenuItem("����");
	private JMenuItem jmClose = new JMenuItem("����");

	public ChattingServer() {
		// Ŭ���̾�Ʈ�� ������ ������ �ؽ��� clients���� - key�� id, value�� �޽���
		// Thread Safe ���·� �����
		clients = new HashMap<String, DataOutputStream>();
		Collections.synchronizedMap(clients);

		jtaServerLog.setEditable(false);

		add("Center", jsPane);
		add("South", jtfServerInput);

		// �޴��� ����
		jmb.add(jmServer);
		jmServer.add(jmStart);
		jmServer.add(jmClose);

		// ������ ����
		// setJMenuBar(jmb);
		setTitle("G1 Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(dPosition.width, dPosition.height, dSize.width, dSize.height);			
		setResizable(false);
		setVisible(true);

		// �̺�Ʈ ���
		jmStart.addActionListener(this);
		jmClose.addActionListener(this);
		jtfServerInput.addActionListener(this);
	}

	private void serverStart() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(10000);
			InetAddress inet = InetAddress.getLocalHost();
			// �������� �˸�. �����ǿ� ��Ʈ ǥ��
			appendServerLog("������ ���۵Ǿ����ϴ� - " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort());

			while (true) { // ���ѹݺ��ϸ� ������ ���� ��� ���ù� �����带 ������ ����
				socket = serverSocket.accept();
				ServerReceiver srThread = new ServerReceiver(socket);
				srThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void appendServerLog(String msg) {
		jtaServerLog.append(msg + "\n");
	}

	private void showUserNumber() {
		appendServerLog("���� ������ ���� : " + clients.size() + "�� �Դϴ�");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChattingServer().serverStart();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jmStart) {
			// serverStart();
		} else if (ob == jmClose) {
			// setVisible(false);
			// dispose();
			// isServerOn = false;
		}
		if (ob == jtfServerInput) {
			sendToAll("����", jtfServerInput.getText());
			jtfServerInput.setText("");
		}
	}

	private void sendConnectedUser(){	
		//������ ���� ��Ȳ�� ������ ������� (����, �����) 
		//�� ���� ���������� ��ü�� �����Ѵ�
		Iterator<String> clientsName = clients.keySet().iterator();
		String userList[] = clients.keySet().toArray(new String[0]);
		
		String connectedUser = "";
		for(int i=0; i<userList.length; i++){
			connectedUser += userList[i] + "|";
		}
		while (clientsName.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream) clients
						.get(clientsName.next());
				dos.writeUTF("[" + "��������" + "] " + connectedUser);				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	// 1:1�޽��� - ��������� �޴»�� �θ����Ը� �����Ѵ�
	private void sendTo(String from, String to, String msg) {
		//Iterator�� 1ȸ��?
		Iterator<String> clientsName = clients.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				String name = clientsName.next();
				if (name.equals(from)) {
					DataOutputStream dos = (DataOutputStream) clients
							.get(name);
					dos.writeUTF("[" + to + "] �Կ��� �ӼӸ� : " + msg);
				}
				if (name.equals(to)) {
					DataOutputStream dos = (DataOutputStream) clients
							.get(name);
					dos.writeUTF("[" + from + "] ���� �ӼӸ� : " + msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// �Ϲ� ä�� - �޽����� ������ ������ �������� �����Ѵ�
	private void sendToAll(String from, String msg) {
		Iterator<String> clientsName = clients.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream) clients
						.get(clientsName.next());
				dos.writeUTF("[" + from + "] " + msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ServerReceiver extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		public ServerReceiver(Socket socket) {
			this.socket = socket;
			appendServerLog(socket.getInetAddress() + ":" + socket.getPort()
					+ " ����");
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			String name = null;
			try {
				// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����
				name = dis.readUTF()
						+ (Long.toString(System.currentTimeMillis()).substring(
								9, 12));
				clients.put(name, dos);
				dos.writeUTF("�����ϽŰ��� ȯ���մϴ�");
				sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");				
				showUserNumber();
				sendConnectedUser();
				// �Է� ��Ʈ�� ������ �ݺ��Ͽ� Ŭ���̾�Ʈ ��ü�� �����Ѵ�
				while (dis != null) {
					classfyMessage(name, dis.readUTF());
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally { // ����� ó��
				sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
				appendServerLog(socket.getInetAddress() + ":"
						+ socket.getPort() + " ���� ����");
				clients.remove(name);
				showUserNumber();
				sendConnectedUser();
			}
		}

		// �޽��� �з�
		// /w �г��� �Ҹ� => �ӼӸ�
		private void classfyMessage(String name, String msg) {
			String temp[] = msg.split(" ", 3);
			if (temp[0].equals("/w")) {
				sendTo(name, temp[1], temp[2]);
			} else {
				sendToAll(name, msg);
			}
		}
	}// ServerReceiver

}// class