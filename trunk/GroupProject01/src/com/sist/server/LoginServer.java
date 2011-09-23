package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sist.common.*;

//���� �Էµ� ���̵�� �н����带 ������� UIM�� �̿� �������� ��ȸ
//���� ������ �г����� �޾� LobbyLogin�� ����
public class LoginServer extends Thread implements G1Server{
	ServerForm g1Server;
	String userID;
	String userPW;
	String dbID;
	String dbPW;
	String tempName;	

	// �������� ó���� ����� uiManager��ü ����
	private UserInfoManagerDAO uiManager = new UserInfoManagerDAO();
	private HashMap<String, DataOutputStream> tempUserList;

	public LoginServer(ServerForm g1Server) {
		tempUserList = new HashMap<String, DataOutputStream>();
		Collections.synchronizedMap(tempUserList);
		this.g1Server = g1Server;
	}

	// ���� ����, �α��ε��� ���������� �ٷ� ���� ���� ����
	private void loginServerStart() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(Tools.LOGIN_SERVER_PORT);
			InetAddress inet = InetAddress.getLocalHost();
			g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort());

			while (true) { // ���ѹݺ��ϸ� ������ ���� ��� ���ù� �����带 ������ ����
				socket = serverSocket.accept();
//				System.out.println("Login Connected");
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		loginServerStart();
	}

	private class ServerOperator extends Thread {

		Socket sockett;
		DataInputStream dis;
		DataOutputStream dos;
		boolean isOperatorOn = true;
		
		public ServerOperator(Socket socket) {
			this.sockett = socket;
			try {
				dis = new DataInputStream(sockett.getInputStream());
				dos = new DataOutputStream(sockett.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void classfyMessage(String name, String msg) {
			String temp[] = msg.split(" ", 3);
			if (temp[0].equals("/login")) {
				String result[] = uiManager.verifyUser(temp[1], temp[2]).split(
						" ", 2);
				switch (Integer.parseInt(result[0])) {
				case 11:
					// ���̵�� �н����� ��ġ (�α���)
					// sendTo(����, �޽���)
					sendTo(name, "11@" + result[1]);
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " �α��� ����");
					isOperatorOn = false;
					break;
				case 12:
					// ���̵� ��ġ, �н����� Ʋ���� (���â ����� ��õ�)
					sendTo(name, "12");
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " �α��� ���� (��й�ȣ Ʋ��)");
					break;
				case 22:
					// ���̵� ������ (���â ����� ��õ�)
					sendTo(name, "22");
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " �α��� ���� (���̵� Ʋ��)");
					break;
				}
			}
		}

		private void sendTo(String to, String msg) {
			Iterator<String> clientsName = tempUserList.keySet().iterator();
			while (clientsName.hasNext()) {
				try {
					String name = clientsName.next();
					if (name.equals(to)) {
						DataOutputStream dos = (DataOutputStream) tempUserList
								.get(name);
						dos.writeUTF("[" + "�α��μ���" + "]@" + msg);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void run() {
			String name = null;
			try { 
				name = dis.readUTF();
				g1Server.appendServerLog("�α���:"+name);
				tempUserList.put(name, dos);
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " �α��� ���� ����");
				while (dis != null) {
					if (!isOperatorOn) {
						break;
					}
					classfyMessage(name, dis.readUTF());
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally { // ����� ó��
				tempUserList.remove(name);
				sendTo(name, " �α��� ������ ���� ����");
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " �α��� ���� ����");
			}
		}
	}// ServerOperator
}