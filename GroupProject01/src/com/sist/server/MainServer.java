package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sist.common.G1Server;

//�α��� ���� ��� ��� ó��. ��ü, 1:1, 1:M ���� ��� �����Ұ�
//Ŭ���̾�Ʈ�κ��� ���� �г�����  Ű��, ���޹��� �޽����� ������ �޴� �ؽ��ʿ� ����
public class MainServer extends Thread implements G1Server {

	ServerForm g1Server;
	boolean isServerOn = false;
	boolean isOperatorOn = false;
	private HashMap<String, DataOutputStream> clients; // ������������

	public MainServer(ServerForm g1Server) {
		// Ŭ���̾�Ʈ�� ������ ������ �ؽ��� clients���� - key�� id, value�� �޽���
		// Thread Safe ���·� �����
		this.g1Server = g1Server;
		clients = new HashMap<String, DataOutputStream>();
		Collections.synchronizedMap(clients);
	}

	private void chatServerStart() {
		isServerOn = true;
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(10000);
			InetAddress inet = InetAddress.getLocalHost();
			// �������� �˸�. �����ǿ� ��Ʈ ǥ��
			g1Server.appendServerLog("[ä�ü���] " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort() + " ����");
			while (true) { // ���ѹݺ��ϸ� ������ ���� ��� ���ù� �����带 ������ ����
				if(!isServerOn){
					isOperatorOn = false;
					serverSocket.close();
					break;
				}
				socket = serverSocket.accept();
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
			g1Server.appendServerLog("[ä�ü���] " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort() + " ����");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		chatServerStart();
	}

	class ServerOperator extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		public ServerOperator(Socket socket) {
			this.socket = socket;
			g1Server.appendServerLog(socket.getInetAddress() + ":"
					+ socket.getPort() + " ����");
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void sendUserStatus() {
			g1Server.appendServerLog("���� ������ ���� : " + clients.size() + "�� �Դϴ�");
			// ������ ���� ��Ȳ�� ������ ������� (����, �����)
			// �� ���� ���������� ��ü�� �����Ѵ�
			Iterator<String> clientsName = clients.keySet().iterator();
			String userList[] = clients.keySet().toArray(new String[0]);

			String connectedUser = "";
			for (int i = 0; i < userList.length; i++) {
				connectedUser += userList[i] + "|";
			}
			while (clientsName.hasNext()) {
				try {
					DataOutputStream dos = (DataOutputStream) clients
							.get(clientsName.next());
					dos.writeUTF("[��������]@" + connectedUser);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// 1:1�޽��� - ��������� �޴»�� �θ��Ը� �����Ѵ�
		private void sendTo(String from, String to, String msg) {
			// Iterator�� 1ȸ��?
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

		private void classfyMessage(String name, String msg) {
			String temp[] = msg.split(" ", 3);
			if (temp[0].equals("/w")) {
				sendTo(name, temp[1], temp[2]);
			} else if (temp[0].equals("/g")){

			} else {
				sendToAll(name, msg);
			}
		}

		public void run() {
			String name = null;
			try {
				// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����
				name = dis.readUTF();
				clients.put(name, dos);
				dos.writeUTF("�����ϽŰ��� ȯ���մϴ�");
				sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
				sendUserStatus();
				// �Է� ��Ʈ�� ������ �ݺ��Ͽ� Ŭ���̾�Ʈ ��ü�� �����Ѵ�
				while (dis != null) {
					if(!isOperatorOn){
						break;
					}
					classfyMessage(name, dis.readUTF());
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally { // ����� ó��
				clients.remove(name);
				sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
				g1Server.appendServerLog(socket.getInetAddress() + ":"
						+ socket.getPort() + " ���� ����");
				sendUserStatus();
			}
		}
	}// ServerOperator
}// class
