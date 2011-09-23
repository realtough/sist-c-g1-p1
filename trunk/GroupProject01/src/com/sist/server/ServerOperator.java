package com.sist.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.sist.server.*;

class ServerOperator extends Thread {

	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private String inputString;
	private String outputString;
	private String serverName;
	
	private ServerForm serverForm;
	private boolean isOperatorOn;
	private HashMap<String, DataOutputStream> connectedUserList;
	
	public ServerOperator(ServerForm serverForm, Socket socket) {
		isOperatorOn = true;
		connectedUserList = new HashMap<String, DataOutputStream>();
		Collections.synchronizedMap(connectedUserList);
		this.serverForm = serverForm;
		this.socket = socket;
		serverForm.appendServerLog(socket.getInetAddress() + ":" + socket.getPort()
		 + " ����");
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopOperator(){
		isOperatorOn = false;
	}
	
	public void sendMessage(){
		//Ŭ���̾�Ʈ������ �޽����� ������
		//��� �����κ����� �޽��� ���� (����� �ٿ� ����), 
		//1:1����, 1:M���� ������� ���� ������ �޽����� ������
		
	}
	
	public void receiveMessage(){		
		String msgtemp[] = inputString.split(" ");
		if (msgtemp[0].equals("/login")) {			
//			serverForm.liServer.classfyMessage(inputString);
		} else if(msgtemp[0].equals("/w")) {			
//			serverForm.mnServer.classfyMessage(inputString);
		} else if(msgtemp[0].equals("/regist")){
			
		} else {
			
		}
		inputString = "";
	}
	
	public void sendUserStatus() {
		serverForm.appendServerLog("���� ������ ���� : " + connectedUserList.size() + "�� �Դϴ�");
		// ������ ���� ��Ȳ�� ������ ������� (����, �����)
		// �� ���� ���������� ��ü�� �����Ѵ�
		Iterator<String> connectedUserListName = connectedUserList.keySet().iterator();
		String userList[] = connectedUserList.keySet().toArray(new String[0]);

		String connectedUser = "";
		for (int i = 0; i < userList.length; i++) {
			connectedUser += userList[i] + "|";
		}
		while (connectedUserListName.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream) connectedUserList
						.get(connectedUserListName.next());
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
		Iterator<String> connectedUserListName = connectedUserList.keySet().iterator();
		while (connectedUserListName.hasNext()) {
			try {
				String name = connectedUserListName.next();
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

	// �Ϲ� ä�� - �޽����� ������ ������ �������� �����Ѵ�
	void sendToAll(String from, String msg) {
		Iterator<String> connectedUserListName = connectedUserList.keySet().iterator();
		while (connectedUserListName.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream) connectedUserList
						.get(connectedUserListName.next());
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
		} else {
			sendToAll(name, msg);
		}
	}

	public void run() {
		//�α��� �������� ä�� �������� �����ؼ� ��� �ٿ��� ����
//		isOperatorOn = true
		String name = null;
		try {
			// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����
			name = dis.readUTF();
			connectedUserList.put(name, dos);
			dos.writeUTF("�����ϽŰ��� ȯ���մϴ�");
//			g1Server.appendServerLog("[�α��μ���] " + name + " �α��� ���� ����");
			sendUserStatus();
			sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");				
			// �Է� ��Ʈ�� ������ �ݺ��Ͽ� Ŭ���̾�Ʈ ��ü�� �����Ѵ�
			while (dis != null) {
				classfyMessage(name, dis.readUTF());
			}
		} catch (Exception e) {
			// TODO: handle exception				
		} finally { // ����� ó��
			connectedUserList.remove(name);
			sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
			serverForm.appendServerLog(socket.getInetAddress() + ":"
			 + socket.getPort() + " ���� ����");	
			sendUserStatus();
		}
	}
	
	
////	public void run() {
//
//		try {
//			while (dis != null) {
//				if (!isOperatorOn) {
//					break;
//				}
////				classfyMessage(name, dis.readUTF());					
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		} finally { // ����� ó��
////			tempUserList.remove(name);
////			sendTo(name, " �α��� ������ ���� ����");
////			g1Server.appendServerLog("[�α��μ���] " + name + " �α��� ���� ����");
//		}
//	}
	
	
}//ServerOperator