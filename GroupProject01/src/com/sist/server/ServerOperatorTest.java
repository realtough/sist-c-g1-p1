package com.sist.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.sist.common.Tools;
import com.sist.server.*;

//�̿ϼ� ���Ұ�
class ServerOperatorTest extends Thread {
	
	private Socket sockett;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private String inputString;
	private String outputString;
	private String serverName;
	
	private ServerForm serverForm;
	private boolean isOperatorOn;
	private HashMap<String, DataOutputStream> connectedUserList;
	
	public ServerOperatorTest(Socket socket) {				
		this.sockett = socket;
//		g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + socket.getInetAddress() + ":" + socket.getPort() + " ����");
		try {
			dis = new DataInputStream(sockett.getInputStream());
			dos = new DataOutputStream(sockett.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
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

	public void run() {
		String name = null;
		try {
			// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����				
			name = dis.readUTF();
			g1Server.appendServerLog("ä���̸�:"+name);
			connectedUserList.put(name, dos);				
//			sendUserStatus();
			dos.writeUTF("�����ϽŰ��� ȯ���մϴ�");
			sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");				
			// �Է� ��Ʈ�� ������ �ݺ��Ͽ� Ŭ���̾�Ʈ ��ü�� �����Ѵ�
			while (dis != null) {					
				classfyMessage(name, dis.readUTF());
			}
		} catch (Exception e) {
			// TODO: handle exception				
		} finally { // ����� ó��
			connectedUserList.remove(name);
			sendUserStatus();
			sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + socket.getInetAddress() + ":"
			 + socket.getPort() + " ���� ����");	
			
		}
	}
	
	//���μ����κ��� �� SO����

	public void sendUserStatus() {
//		g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + "���� ������ ���� : " + connectedUserList.size() + "�� �Դϴ�");
//		g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER+"���� ���� ��� ����");
		// ������ ���� ��Ȳ�� ������ ������� (����, �����)
		// �� ���� ���������� ��ü�� �����Ѵ�
		Iterator<String> clientsName = connectedUserList.keySet().iterator();
		String userList[] = connectedUserList.keySet().toArray(new String[0]);

		String connectedUser = "";
		for (int i = 0; i < userList.length; i++) {
			connectedUser += userList[i] + "|";
		}
		while (clientsName.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream) connectedUserList
						.get(clientsName.next());
				dos.writeUTF("[��������]@" + connectedUser);
//				g1Server.appendServerLog("[��������]@" + connectedUser);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	// 1:1�޽��� - ��������� �޴»�� �θ��Ը� �����Ѵ�
	private void sendTo(String from, String to, String msg) {
		// Iterator�� 1ȸ��?
		Iterator<String> clientsName = connectedUserList.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				String name = clientsName.next();
				if (name.equals(from)) {
					DataOutputStream dos = (DataOutputStream) connectedUserList.get(name);
					dos.writeUTF("[" + to + "] �Կ��� �ӼӸ� : " + msg);
				}
				if (name.equals(to)) {
					DataOutputStream dos = (DataOutputStream) connectedUserList.get(name);
					dos.writeUTF("[" + from + "] ���� �ӼӸ� : " + msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	
	
	// �Ϲ� ä�� - �޽����� ������ ������ �������� �����Ѵ�
	void sendToAll(String from, String msg) {
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
//		g1Server.appendServerLog(name+":"+msg);
		String temp[] = msg.split(" ", 3);
		if (temp[0].equals("/w")) {
			sendTo(name, temp[1], temp[2]);
		} else if (temp[0].equals("/first")) {
			sendUserStatus();
		} 
		else {
			sendToAll(name, msg);
		}
	}
	*/	
}//ServerOperator