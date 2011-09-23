package com.sist.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.sist.common.Tools;
import com.sist.server.*;

//미완성 사용불가
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
//		g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + socket.getInetAddress() + ":" + socket.getPort() + " 연결");
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
				// 아이디와 패스워드 일치 (로그인)
				// sendTo(유저, 메시지)
				sendTo(name, "11@" + result[1]);
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " 로그인 성공");
				isOperatorOn = false;
				break;
			case 12:
				// 아이디 일치, 패스워드 틀릴때 (경고창 띄운후 재시도)
				sendTo(name, "12");
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " 로그인 실패 (비밀번호 틀림)");
				break;
			case 22:
				// 아이디 없을때 (경고창 띄운후 재시도)
				sendTo(name, "22");
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " 로그인 실패 (아이디 틀림)");
				break;
			}
		}
	}

	public void run() {
		String name = null;
		try {
			// 환영메세지 출력후, 접속자 정보를 해쉬맵에 저장				
			name = dis.readUTF();
			g1Server.appendServerLog("채팅이름:"+name);
			connectedUserList.put(name, dos);				
//			sendUserStatus();
			dos.writeUTF("접속하신것을 환영합니다");
			sendToAll("서버", name + " 님이 입장 하셨습니다");				
			// 입력 스트림 내용을 반복하여 클라이언트 전체에 전송한다
			while (dis != null) {					
				classfyMessage(name, dis.readUTF());
			}
		} catch (Exception e) {
			// TODO: handle exception				
		} finally { // 퇴장시 처리
			connectedUserList.remove(name);
			sendUserStatus();
			sendToAll("서버", name + " 님이 퇴장 하셨습니다");
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + socket.getInetAddress() + ":"
			 + socket.getPort() + " 연결 끊김");	
			
		}
	}
	
	//메인서버로부터 온 SO본문

	public void sendUserStatus() {
//		g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + "현재 접속자 수는 : " + connectedUserList.size() + "명 입니다");
//		g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER+"접속 유저 목록 전송");
		// 접속한 유저 상황에 변동이 있을경우 (입장, 퇴장시)
		// 새 접속 유저정보를 전체에 전송한다
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
				dos.writeUTF("[접속유저]@" + connectedUser);
//				g1Server.appendServerLog("[접속유저]@" + connectedUser);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	// 1:1메시지 - 보낸사람과 받는사람 두명에게만 전송한다
	private void sendTo(String from, String to, String msg) {
		// Iterator는 1회용?
		Iterator<String> clientsName = connectedUserList.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				String name = clientsName.next();
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

	private void sendTo(String to, String msg) {
		Iterator<String> clientsName = tempUserList.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				String name = clientsName.next();
				if (name.equals(to)) {
					DataOutputStream dos = (DataOutputStream) tempUserList
							.get(name);
					dos.writeUTF("[" + "로그인서버" + "]@" + msg);
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