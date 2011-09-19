package com.sist.server;

import java.io.*;
import java.net.*;

public class LoginServer extends Thread{
	
	public LoginServer(G1GameServer g1Server) {
		// TODO Auto-generated constructor stub
	}
	
	//���� ����, �α��ε��� ���������� �ٷ� ���� ���� ����
	private void userServerStart(){
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(10001);
			InetAddress inet = InetAddress.getLocalHost();
//			appendServerLog("[�α��μ���] " + inet.getHostAddress() + ":"
//					+ serverSocket.getLocalPort());

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
	
	public void run(){
		
	}
	
	class ServerReceiver extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		public ServerReceiver(Socket socket) {
			this.socket = socket;
//			appendServerLog(socket.getInetAddress() + ":" + socket.getPort()
//					+ " ����");
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			/*
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
				System.out.println(e.getMessage());
			} finally { // ����� ó��
				clients.remove(name);
				sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
				appendServerLog(socket.getInetAddress() + ":"
						+ socket.getPort() + " ���� ����");
				showUserNumber();
				sendConnectedUser();
			}
			*/
		}
	}//ServerReceiver
}
