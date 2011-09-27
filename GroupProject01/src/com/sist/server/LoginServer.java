package com.sist.server;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.sist.common.*;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

//���� �Էµ� ���̵�� �н����带 ������� UIM�� �̿� �������� ��ȸ
//���� ������ �г����� �޾� LobbyLogin�� ����
public class LoginServer extends Thread implements G1Server {
	private ServerForm g1Server;

	// �������� ó���� ����� uiManager��ü ����
	private UserInfoDAO uiManager = new UserInfoDAO();
	private HashMap<String, DataOutputStream> tempUserList = new HashMap<String, DataOutputStream>();
//	private HashMap<String, BufferedWriter> tempUserList = new HashMap<String, BufferedWriter>();

	public LoginServer(ServerForm g1Server) {
		this.g1Server = g1Server;
	}

	// ���� ����, �α��ε��� ���������� �ٷ� ���� ���� ����
	private void loginServerStart() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(Tools.LOGIN_SERVER_PORT);
			InetAddress inet = InetAddress.getLocalHost();
			g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER
					+ inet.getHostAddress() + ":" + serverSocket.getLocalPort());

			while (true) { // ���ѹݺ��ϸ� ������ ���� ��� ���ù� �����带 ������ ����
				socket = serverSocket.accept();
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
		} catch (IOException e) {
			g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + e.getMessage());
		}
	}

	public void run() {
		loginServerStart();
	}

	class ServerOperator extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;
		BufferedReader bfReader;
		BufferedWriter bfWriter;
		boolean isOperatorOn = true;

		public ServerOperator(Socket socket) {
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				// bfReader = new BufferedReader(new
				// InputStreamReader(socket.getInputStream()));
				// bfWriter = new BufferedWriter(new
				// OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER
						+ e.getMessage());
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
					sendTo("login", name, "11#" + uiManager.getUserInfo(temp[1]));
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
							+ " �α��� ����");
					isOperatorOn = false;
					break;
				case 12:
					// ���̵� ��ġ, �н����� Ʋ���� (���â ����� ��õ�)
					sendTo("login", name, "12");
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
							+ " �α��� ���� (��й�ȣ Ʋ��)");
					break;
				case 22:
					// ���̵� ������ (���â ����� ��õ�)
					sendTo("login", name, "22");
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
							+ " �α��� ���� (���̵� Ʋ��)");
					break;
				default:
					sendTo("login", name, "33");
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
							+ " �α��� ���� (������ ����)");
					break;
				}
			} else if (temp[0].equals("/regist")) {
				// DAO�� �ҷ��� �������� ó��
				UserInfoVO uiVO = Tools.stringToUserInfo(temp[1]);
				uiManager.insertUser(uiVO);
			} else if (temp[0].equals("/check")) {
				// DAO�� �ҷ��� �������� ó��								
				if(uiManager.isExist(temp[1], temp[2])){
					sendTo("regist", name, "check#11");
				}else{
					sendTo("regist", name, "check#22");
				}
			}
		}

		private void sendTo(String from, String to, String msg) {
			try {
				DataOutputStream dos2 = tempUserList.get(to);
				dos2.writeUTF("["+from+"]#" + msg);				
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}

		public void run() {
			String name = null;
			try {
				// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����
				name = dis.readUTF();
				tempUserList.put(name, dos);
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
						+ " �α��� ���� ����");
				while (dis != null) {
					if (!isOperatorOn) {
						break;
					}
					classfyMessage(name, dis.readUTF());
				}
			} catch (Exception e) {
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER
						+ e.getMessage());
			} finally { // ����� ó��
				tempUserList.remove(name);
				// sendTo(name, " �α��� ������ ���� ����");
				closeStream();
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
						+ " �α��� ���� ����");
			}
		}

		private void closeStream() {
			try {
				dis.close();
				dos.close();
				// bfReader.close();
				// bfWriter.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER
						+ e.getMessage());
			}
		}
	}// ServerOperator
}