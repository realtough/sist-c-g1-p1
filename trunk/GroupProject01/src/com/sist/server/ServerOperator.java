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
		 + " 연결");
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
		//클라이언트쪽으로 메시지를 보낸다
		//어느 서버로부터의 메시지 인지 (헤더를 붙여 구분), 
		//1:1인지, 1:M인지 전원대상 인지 구별해 메시지를 보낼것
		
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
		serverForm.appendServerLog("현재 접속자 수는 : " + connectedUserList.size() + "명 입니다");
		// 접속한 유저 상황에 변동이 있을경우 (입장, 퇴장시)
		// 새 접속 유저정보를 전체에 전송한다
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
				dos.writeUTF("[접속유저]@" + connectedUser);				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	// 1:1메시지 - 보낸사람과 받는사람 두명에게만 전송한다
	private void sendTo(String from, String to, String msg) {
		// Iterator는 1회용?
		Iterator<String> connectedUserListName = connectedUserList.keySet().iterator();
		while (connectedUserListName.hasNext()) {
			try {
				String name = connectedUserListName.next();
				if (name.equals(from)) {
					DataOutputStream dos = (DataOutputStream) connectedUserList.get(name);
					dos.writeUTF("[" + to + "] 님에게 귓속말 : " + msg);
				}
				if (name.equals(to)) {
					DataOutputStream dos = (DataOutputStream) connectedUserList.get(name);
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
		//로그인 세션인지 채팅 세션인지 구분해서 헤더 붙여서 보냄
//		isOperatorOn = true
		String name = null;
		try {
			// 환영메세지 출력후, 접속자 정보를 해쉬맵에 저장
			name = dis.readUTF();
			connectedUserList.put(name, dos);
			dos.writeUTF("접속하신것을 환영합니다");
//			g1Server.appendServerLog("[로그인서버] " + name + " 로그인 세션 열림");
			sendUserStatus();
			sendToAll("서버", name + " 님이 입장 하셨습니다");				
			// 입력 스트림 내용을 반복하여 클라이언트 전체에 전송한다
			while (dis != null) {
				classfyMessage(name, dis.readUTF());
			}
		} catch (Exception e) {
			// TODO: handle exception				
		} finally { // 퇴장시 처리
			connectedUserList.remove(name);
			sendToAll("서버", name + " 님이 퇴장 하셨습니다");
			serverForm.appendServerLog(socket.getInetAddress() + ":"
			 + socket.getPort() + " 연결 끊김");	
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
//		} finally { // 퇴장시 처리
////			tempUserList.remove(name);
////			sendTo(name, " 로그인 서버와 연결 종료");
////			g1Server.appendServerLog("[로그인서버] " + name + " 로그인 세션 종료");
//		}
//	}
	
	
}//ServerOperator