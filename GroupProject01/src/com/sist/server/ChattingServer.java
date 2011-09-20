package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.*;

//로그인 이후 모든 통신 처리. 전체, 1:1, 1:M 전달 기능 구현할것
//클라이언트로부터 받은 닉네임을  키로, 전달받은 메시지를 값으로 받는 해쉬맵에
//유저정보 저장
public class ChattingServer extends Thread {
	G1GameServer g1Server;
	boolean isServerOn = false;
	private HashMap<String, DataOutputStream> clients; // 현재접속유저

	public ChattingServer(G1GameServer g1Server) {
		// 클라이언트의 정보를 저장할 해쉬맵 clients생성 - key는 id, value는 메시지
		// Thread Safe 상태로 만든다
		this.g1Server = g1Server; 
		clients = new HashMap<String, DataOutputStream>();
		Collections.synchronizedMap(clients);				
	}

	private void chatServerStart() {
		isServerOn = true;
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(10000);
			InetAddress inet = InetAddress.getLocalHost();
			// 서버시작 알림. 아이피와 포트 표시
			g1Server.appendServerLog("[채팅서버] " + inet.getHostAddress() + ":"
			 + serverSocket.getLocalPort());			

			while (true) { // 무한반복하며 연결이 들어올 경우 리시버 쓰레드를 생성해 연결
				socket = serverSocket.accept();
				System.out.println("Connected");
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		chatServerStart();
	}

	private void sendUserStatus() {
		g1Server.appendServerLog("현재 접속자 수는 : " + clients.size() + "명 입니다");
		// 접속한 유저 상황에 변동이 있을경우 (입장, 퇴장시)
		// 새 접속 유저정보를 전체에 전송한다
		Iterator<String> clientsName = clients.keySet().iterator();
		String userList[] = clients.keySet().toArray(new String[0]);

		String connectedUser = "";
		for (int i = 0; i < userList.length; i++) {
			connectedUser += userList[i] + "|";
		}
		while (clientsName.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream) clients
						.get(clientsName.next());
				dos.writeUTF("/sys " + connectedUser);				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	// 1:1메시지 - 보낸사람과 받는사람 두명에게만 전송한다
	private void sendTo(String from, String to, String msg) {
		// Iterator는 1회용?
		Iterator<String> clientsName = clients.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				String name = clientsName.next();
				if (name.equals(from)) {
					DataOutputStream dos = (DataOutputStream) clients.get(name);
					dos.writeUTF("[" + to + "] 님에게 귓속말 : " + msg);
				}
				if (name.equals(to)) {
					DataOutputStream dos = (DataOutputStream) clients.get(name);
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

	class ServerOperator extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

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

		private void classfyMessage(String name, String msg) {
			String temp[] = msg.split(" ", 3);
			if (temp[0].equals("/w")) {
				sendTo(name, temp[1], temp[2]);
			} else {
				sendToAll(name, msg);
			}
		}

		public void run() {
			String name = null;
			try {
				// 환영메세지 출력후, 접속자 정보를 해쉬맵에 저장
				name = dis.readUTF()
						+ (Long.toString(System.currentTimeMillis()).substring(
								9, 12));
				clients.put(name, dos);
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
				clients.remove(name);
				sendToAll("서버", name + " 님이 퇴장 하셨습니다");
				g1Server.appendServerLog(socket.getInetAddress() + ":"
				 + socket.getPort() + " 연결 끊김");	
				sendUserStatus();
			}
		}
	}//ServerReceiver
}//class
