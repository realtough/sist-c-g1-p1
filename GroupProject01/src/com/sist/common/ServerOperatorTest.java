package com.sist.common;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

import com.sist.server.ServerForm;

abstract class ServerOperator extends Thread {

	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	ServerForm g1Server;
	
	public ServerOperator(ServerForm g1Server) {
		this.g1Server = g1Server; 
		// TODO Auto-generated constructor stub
	}
	
	public ServerOperator(Socket socket) {
		this.socket = socket;
		g1Server.appendServerLog(socket.getInetAddress() + ":" + socket.getPort()
		 + " ����");
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	abstract protected void sendUserStatus();
	
	// 1:1�޽��� - ��������� �޴»�� �θ��Ը� �����Ѵ�
	/*
	private void sendTo(String from, String to, String msg) {
		// Iterator�� 1ȸ��?
//		Iterator<String> clientsName = clients.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				String name = clientsName.next();
				if (name.equals(from)) {
//					DataOutputStream dos = (DataOutputStream) clients.get(name);
					dos.writeUTF("[" + to + "] �Կ��� �ӼӸ� : " + msg);
				}
				if (name.equals(to)) {
//					DataOutputStream dos = (DataOutputStream) clients.get(name);
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
	*/
	abstract public void classfyMessage(String name, String msg);
	/*
	public void run() {
		String name = null;
		try {
			// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����
			name = dis.readUTF();
//			clients.put(name, dos);
			dos.writeUTF("�����ϽŰ��� ȯ���մϴ�");
			sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
			sendUserStatus();
			// �Է� ��Ʈ�� ������ �ݺ��Ͽ� Ŭ���̾�Ʈ ��ü�� �����Ѵ�
			while (dis != null) {
				classfyMessage(name, dis.readUTF());
			}
		} catch (Exception e) {
			// TODO: handle exception				
		} finally { // ����� ó��
//			clients.remove(name);
			sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
			g1Server.appendServerLog(socket.getInetAddress() + ":"
			 + socket.getPort() + " ���� ����");	
			sendUserStatus();
		}
	}*/
}//ServerOperator