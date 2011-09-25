package com.sist.server;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.sist.common.*;

//���� �Էµ� ���̵�� �н����带 ������� UIM�� �̿� �������� ��ȸ
//���� ������ �г����� �޾� LobbyLogin�� ����
public class LoginServer extends Thread implements G1Server{
	private ServerForm g1Server;
	
	// �������� ó���� ����� uiManager��ü ����
	private UserInfoManagerDAO uiManager = new UserInfoManagerDAO();
	private HashMap<String, DataOutputStream> tempUserList = new HashMap<String, DataOutputStream>();

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
			g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort());

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
//				bfReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				bfWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + e.getMessage());
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
					sendTo(name, "11 " + (int)(Math.random()*50));
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
			}else if(temp[0].equals("/regist")){
				
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
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + e.getMessage());
				}
			}
		}

		public void run() {
			String name = null;
			try {
				// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����
				name = dis.readUTF();
				tempUserList.put(name, dos);
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " �α��� ���� ����");
				while (dis != null) {
					if (!isOperatorOn) {
						break;
					}
					classfyMessage(name, dis.readUTF());
				}
			} catch (Exception e) {
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + e.getMessage());
			} finally { // ����� ó��
				tempUserList.remove(name);
				sendTo(name, " �α��� ������ ���� ����");	
//				closeStream();
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " �α��� ���� ����");
			}
		}
		
		private void closeStream(){
			try {
				dis.close();
				dos.close();
//				bfReader.close();
//				bfWriter.close();				
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + e.getMessage());
			}
		}
	}// ServerOperator
}