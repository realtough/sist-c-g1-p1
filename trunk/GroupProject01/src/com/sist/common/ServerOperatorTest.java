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
		 + " 연결");
		try {
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	abstract protected void sendUserStatus();
	
	// 1:1메시지 - 보낸사람과 받는사람 두명에게만 전송한다
	/*
	private void sendTo(String from, String to, String msg) {
		// Iterator는 1회용?
//		Iterator<String> clientsName = clients.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				String name = clientsName.next();
				if (name.equals(from)) {
//					DataOutputStream dos = (DataOutputStream) clients.get(name);
					dos.writeUTF("[" + to + "] 님에게 귓속말 : " + msg);
				}
				if (name.equals(to)) {
//					DataOutputStream dos = (DataOutputStream) clients.get(name);
					dos.writeUTF("[" + from + "] 님의 귓속말 : " + msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 일반 채팅 - 메시지가 들어오면 접속한 전원에게 전송한다
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
			// 환영메세지 출력후, 접속자 정보를 해쉬맵에 저장
			name = dis.readUTF();
//			clients.put(name, dos);
			dos.writeUTF("접속하신것을 환영합니다");
			sendToAll("서버", name + " 님이 입장 하셨습니다");
			sendUserStatus();
			// 입력 스트림 내용을 반복하여 클라이언트 전체에 전송한다
			while (dis != null) {
				classfyMessage(name, dis.readUTF());
			}
		} catch (Exception e) {
			// TODO: handle exception				
		} finally { // 퇴장시 처리
//			clients.remove(name);
			sendToAll("서버", name + " 님이 퇴장 하셨습니다");
			g1Server.appendServerLog(socket.getInetAddress() + ":"
			 + socket.getPort() + " 연결 끊김");	
			sendUserStatus();
		}
	}*/
}//ServerOperator