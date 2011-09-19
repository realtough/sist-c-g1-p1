package com.sist.server;

import java.io.*;
import java.net.*;

public class LoginServer extends Thread{
	
	public LoginServer(G1GameServer g1Server) {
		// TODO Auto-generated constructor stub
	}
	
	//유저 가입, 로그인등의 유저정보를 다룰 별도 서버 생성
	private void userServerStart(){
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(10001);
			InetAddress inet = InetAddress.getLocalHost();
//			appendServerLog("[로그인서버] " + inet.getHostAddress() + ":"
//					+ serverSocket.getLocalPort());

			while (true) { // 무한반복하며 연결이 들어올 경우 리시버 쓰레드를 생성해 연결
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
//					+ " 연결");
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
				// 환영메세지 출력후, 접속자 정보를 해쉬맵에 저장
				name = dis.readUTF()
						+ (Long.toString(System.currentTimeMillis()).substring(
								9, 12));
				clients.put(name, dos);
				dos.writeUTF("접속하신것을 환영합니다");
				sendToAll("서버", name + " 님이 입장 하셨습니다");
				showUserNumber();
				sendConnectedUser();
				// 입력 스트림 내용을 반복하여 클라이언트 전체에 전송한다
				while (dis != null) {
					classfyMessage(name, dis.readUTF());
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			} finally { // 퇴장시 처리
				clients.remove(name);
				sendToAll("서버", name + " 님이 퇴장 하셨습니다");
				appendServerLog(socket.getInetAddress() + ":"
						+ socket.getPort() + " 연결 끊김");
				showUserNumber();
				sendConnectedUser();
			}
			*/
		}
	}//ServerReceiver
}
