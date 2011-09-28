package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sist.common.Tools;

//로그인 이후 모든 통신 처리. 전체, 1:1, 1:M 전달 기능 구현
//클라이언트의 닉네임을  키로, 메시지 전달용 출력스트림을 값으로 하여 해쉬맵에 저장
public class MainServer extends Thread implements G1Server {
	ServerForm g1Server;
	boolean isServerOn = false;
	private HashMap<String, BufferedWriter> clients; // 현재접속유저

	public MainServer(ServerForm g1Server) {
		this.g1Server = g1Server;
		clients = new HashMap<String, BufferedWriter>();
		Collections.synchronizedMap(clients);	// Thread Safe 상태로 만든다
	}

	private void chatServerStart() {
		isServerOn = true;
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(10000);
			InetAddress inet = InetAddress.getLocalHost();
			// 서버시작 알림. 아이피와 포트 표시
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
					+ inet.getHostAddress() + ":" + serverSocket.getLocalPort());

			while (true) { // 무한반복하며 연결이 들어올 경우 리시버 쓰레드를 생성해 연결
				socket = serverSocket.accept();
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
		} catch (IOException e) {
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + e.getMessage());
		}
	}

	public void run() {
		chatServerStart();
	}

	class ServerOperator extends Thread {

		private Socket socket;
		private BufferedReader bfReader;
		private BufferedWriter bfWriter;

		public ServerOperator(Socket socket) {
			this.socket = socket;
			String userip = (this.socket.getInetAddress() + ":" + this.socket
					.getPort()).substring(1);
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + userip + " 연결");
			try {
				 bfReader = new BufferedReader(new
				 InputStreamReader(this.socket.getInputStream()));
				 bfWriter = new BufferedWriter(new
				 OutputStreamWriter(this.socket.getOutputStream()));
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}

		private void sendTo(String from, String to, String msg,
				boolean isWhisper) {
			try {
				String message = "";
				BufferedWriter bw = clients.get(to);
				if (from.equals("server")) {					
					message = "[" + from + "]#" + msg + "\n"; 
				} else if (isWhisper) {
					BufferedWriter bwf = clients.get(from);
					message = "[" + to + "] 님에게 귓속말 : " + msg + "\n";
					bwf.write(message);
					bwf.flush();
					message = "[" + from + "] 님의 귓속말 : " + msg + "\n";
				} else {	
					message = "[" + from + "] " + msg + "\n";					
				}
				bw.write(message);
				if(bw != null) bw.flush();
				message = "";
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}

		// 전체 유저 목록 전송
		public void sendUserStatus() {
			g1Server.appendServerLog("[메인서버] 현재 접속자 수 : " + clients.size());
			// 접속한 유저 상황에 변동이 있을경우 (입장, 퇴장시)
			// 새 접속 유저정보를 전체에 전송한다			
			Iterator<String> clientsName = clients.keySet().iterator();
			String userList[] = clients.keySet().toArray(new String[0]);

			String connectedUser = "userlist#";
			for (int i = 0; i < userList.length; i++) {
				connectedUser += userList[i] + "@";				
			}						
			while (clientsName.hasNext()) {
				sendTo("server", clientsName.next(), connectedUser, false);
			}
		}
		
		// 접속한 전원에게 전송
		void sendToAll(String from, String msg) {
			Iterator<String> clientsName = clients.keySet().iterator();
			while (clientsName.hasNext()) {
				sendTo(from, clientsName.next(), msg, false);
			}
		}

		// 전송받은 메시지의 처리를 위한 분류
		private void classfyMessage(String name, String msg) {
			String temp[] = msg.split(" ", 3);
			if (temp[0].equals("/w")) {
				sendTo(name, temp[1], temp[2], true);
			} else {
				sendToAll(name, msg);
			}
		}

		public void run() {
			String name = null;
			try {
				// 환영메세지 출력후, 접속자 정보를 해쉬맵에 저장				
				name = bfReader.readLine();				
				clients.put(name, bfWriter);				
				bfWriter.write("접속하신것을 환영합니다\n");
				bfWriter.flush();
				sendToAll("server", name + " 님이 입장 하셨습니다");
				sendUserStatus();
				// 반복하며 입력받은 메시지를 분류 
				while (bfReader != null){					
					classfyMessage(name, bfReader.readLine());
				}
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			} finally { // 퇴장시 처리
				clients.remove(name);
				sendToAll("서버", name + " 님이 퇴장 하셨습니다");
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ name +" - " +socket.getInetAddress() + ":" + socket.getPort()
						+ " 연결 끊김");
				closeStream();
				sendUserStatus();
			}
		}

		private void closeStream() {
			try {
				bfReader.close();
				bfWriter.close();
				socket.close();
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}
	}// ServerOperator
}// class