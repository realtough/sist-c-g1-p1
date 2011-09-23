package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sist.common.*;

//최초 입력된 아이디와 패스워드를 기반으로 UIM을 이용 유저정보 조회
//정보 인증시 닉네임을 받아 LobbyLogin에 전달
public class LoginServer extends Thread implements G1Server{
	ServerForm g1Server;
	String userID;
	String userPW;
	String dbID;
	String dbPW;
	String tempName;	

	// 유저정보 처리를 담당할 uiManager객체 생성
	private UserInfoManagerDAO uiManager = new UserInfoManagerDAO();
	private HashMap<String, DataOutputStream> tempUserList;

	public LoginServer(ServerForm g1Server) {
		tempUserList = new HashMap<String, DataOutputStream>();
		Collections.synchronizedMap(tempUserList);
		this.g1Server = g1Server;
	}

	// 유저 가입, 로그인등의 유저정보를 다룰 별도 서버 생성
	private void loginServerStart() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(Tools.LOGIN_SERVER_PORT);
			InetAddress inet = InetAddress.getLocalHost();
			g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort());

			while (true) { // 무한반복하며 연결이 들어올 경우 리시버 쓰레드를 생성해 연결
				socket = serverSocket.accept();
//				System.out.println("Login Connected");
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		loginServerStart();
	}

	private class ServerOperator extends Thread {

		Socket sockett;
		DataInputStream dis;
		DataOutputStream dos;
		boolean isOperatorOn = true;
		
		public ServerOperator(Socket socket) {
			this.sockett = socket;
			try {
				dis = new DataInputStream(sockett.getInputStream());
				dos = new DataOutputStream(sockett.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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

		public void run() {
			String name = null;
			try { 
				name = dis.readUTF();
				g1Server.appendServerLog("로그인:"+name);
				tempUserList.put(name, dos);
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " 로그인 세션 열림");
				while (dis != null) {
					if (!isOperatorOn) {
						break;
					}
					classfyMessage(name, dis.readUTF());
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally { // 퇴장시 처리
				tempUserList.remove(name);
				sendTo(name, " 로그인 서버와 연결 종료");
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name + " 로그인 세션 종료");
			}
		}
	}// ServerOperator
}