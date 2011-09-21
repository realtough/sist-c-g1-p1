package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;

import com.sist.common.Tools;
import com.sist.common.UserInfoManager;

//���� �Էµ� ���̵�� �н����带 ������� UIM�� �̿� �������� ��ȸ
//���� ������ �г����� �޾� LobbyLogin�� ����
public class LoginServer extends Thread {
	ServerForm g1Server;
	String userID;
	String userPW;
	String dbID;
	String dbPW;
	String tempName;	

	// �������� ó���� ����� uiManager��ü ����
	private UserInfoManager uiManager = new UserInfoManager();
	private HashMap<String, DataOutputStream> tempUserList = new HashMap<String, DataOutputStream>();

	public LoginServer(ServerForm g1Server) {
		this.g1Server = g1Server;
	}

	// ���� ����, �α��ε��� ���������� �ٷ� ���� ���� ����
	private void loginServerStart() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(Tools.portLoginServer);
			InetAddress inet = InetAddress.getLocalHost();
			g1Server.appendServerLog("[�α��μ���] " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort());

			while (true) { // ���ѹݺ��ϸ� ������ ���� ��� ���ù� �����带 ������ ����
				socket = serverSocket.accept();
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

	class ServerOperator extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;
		boolean isOperatorOn = true;
		
		public ServerOperator(Socket socket) {
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
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
					sendTo(name, "11 " + result[1]);
					g1Server.appendServerLog(name + " �α��� ����");
					isOperatorOn = false;
					break;
				case 12:
					// ���̵� ��ġ, �н����� Ʋ���� (���â ����� ��õ�)
					sendTo(name, "12");
					g1Server.appendServerLog(name + " �α��� ���� (��й�ȣ Ʋ��)");
					break;
				case 22:
					// ���̵� ������ (���â ����� ��õ�)
					sendTo(name, "22");
					g1Server.appendServerLog(name + " �α��� ���� (���̵� Ʋ��)");
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
						dos.writeUTF("[" + "�α��μ���" + "] " + msg);
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
				// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����
				name = dis.readUTF();
				tempUserList.put(name, dos);
				g1Server.appendServerLog(name + " �α��� ���� ����");
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
				g1Server.appendServerLog(name + " �α��� ���� ����");
			}
		}
	}// ServerOperator
}
