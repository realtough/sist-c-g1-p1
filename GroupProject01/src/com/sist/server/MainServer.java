package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sist.common.Tools;

//�α��� ���� ��� ��� ó��. ��ü, 1:1, 1:M ���� ��� �����Ұ�
//Ŭ���̾�Ʈ�κ��� ���� �г�����  Ű��, ���޹��� �޽����� ������ �޴� �ؽ��ʿ�
//�������� ����
public class MainServer extends Thread implements G1Server {
	ServerForm g1Server;
	boolean isServerOn = false;
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
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
					+ inet.getHostAddress() + ":" + serverSocket.getLocalPort());

			while (true) { // ���ѹݺ��ϸ� ������ ���� ��� ���ù� �����带 ������ ����
				socket = serverSocket.accept();
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
		} catch (IOException e) {
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + e.getMessage());
		}
	}

	public void run() {
		chatServerStart();
	}

	class ServerOperator extends Thread {

		private Socket socket;
		private DataInputStream dis;
		private DataOutputStream dos;
		private BufferedReader bfReader;
		private BufferedWriter bfWriter;

		public ServerOperator(Socket socket) {
			this.socket = socket;
			String userip = (this.socket.getInetAddress() + ":" + this.socket
					.getPort()).substring(1);
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + userip + " ����");
			try {
				dis = new DataInputStream(this.socket.getInputStream());
				dos = new DataOutputStream(this.socket.getOutputStream());
//				bfReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
//				bfWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}

		public void sendUserStatus() {
			g1Server.appendServerLog("���� ������ �� : " + clients.size());
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
					dos.writeUTF("/sys " + connectedUser);
				} catch (IOException e) {
					g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
							+ e.getMessage());
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
					g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
							+ e.getMessage());
				}
			}
		}

		// �Ϲ� ä�� - �޽����� ������ ������ �������� �����Ѵ�
		void sendToAll(String from, String msg) {
			Iterator<String> clientsName = clients.keySet().iterator();
			while (clientsName.hasNext()) {
				try {
					DataOutputStream dos = (DataOutputStream) clients
							.get(clientsName.next());
					dos.writeUTF("[" + from + "] " + msg);
				} catch (IOException e) {
					g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
							+ e.getMessage());
				}
			}
		}

		private void classfyMessage(String name, String msg) {
			String temp[] = msg.split(" ", 3);
			if (temp[0].equals("/w")) {
				sendTo(name, temp[1], temp[2]);
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
					classfyMessage(name, dis.readUTF());
				}
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			} finally { // ����� ó��
				clients.remove(name);
				sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ socket.getInetAddress() + ":" + socket.getPort()
						+ " ���� ����");
				closeStream();
				sendUserStatus();
			}
		}
		
		private void closeStream(){
			try {
//				bfReader.close();
//				bfWriter.close();
				dis.close();
				dos.close();
				socket.close();
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}
	}// ServerOperator
}// class