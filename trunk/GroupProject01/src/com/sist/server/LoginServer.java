package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;

import com.sist.common.*;

//최초 입력된 아이디와 패스워드를 기반으로 UIM을 이용 유저정보 조회
//정보 인증시 닉네임을 받아 LobbyLogin에 전달
public class LoginServer extends Thread implements G1Server{
	ServerForm g1Server;
	
	String userID;
	String userPW;
	String tempName;	

	boolean isServerOn = false;
	boolean isOperatorOn = false;
	// 유저정보 처리를 담당할 uiManager객체 생성
	private UserInfoManagerDAO uiManager = new UserInfoManagerDAO();
	private HashMap<String, DataOutputStream> tempUserList = new HashMap<String, DataOutputStream>();

	public LoginServer(ServerForm g1Server) {
		this.g1Server = g1Server;
	}

	// 유저 가입, 로그인등의 유저정보를 다룰 별도 서버 생성
	private void loginServerStart() {
		isServerOn = true;
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(Tools.portLoginServer);
			InetAddress inet = InetAddress.getLocalHost();
			g1Server.appendServerLog("[로그인서버] " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort() + " 오픈");
			ServerOperator soThread;
			while (true) { // 무한반복하며 연결이 들어올 경우 리시버 쓰레드를 생성해 연결
				if(!isServerOn){									
					serverSocket.close();
					break;
				}
				socket = serverSocket.accept();
				soThread = new ServerOperator(socket);
				soThread.start();
			}
			g1Server.appendServerLog("[로그인서버] " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort() + " 종료");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopServer(){
		isServerOn = false;
	}
	
	public void run() {
		loginServerStart();
	}

	class ServerOperator extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;
//		boolean isOperatorOn = true;
		
		public ServerOperator(Socket socket) {
			this.socket = socket;
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
			if (temp[0].equals("/login")) {
				String result[] = uiManager.verifyUser(temp[1], temp[2]).split(
						" ", 2);
				switch (Integer.parseInt(result[0])) {
				case 11:
					// 아이디와 패스워드 일치 (로그인)
					// sendTo(유저, 메시지)
					sendTo(name, "11@" + result[1]);
					g1Server.appendServerLog(name + " 로그인 성공");
					isOperatorOn = false;
					break;
				case 12:
					// 아이디 일치, 패스워드 틀릴때 (경고창 띄운후 재시도)
					sendTo(name, "12");
					g1Server.appendServerLog(name + " 로그인 실패 (비밀번호 틀림)");
					break;
				case 22:
					// 아이디 없을때 (경고창 띄운후 재시도)
					sendTo(name, "22");
					g1Server.appendServerLog(name + " 로그인 실패 (아이디 틀림)");
					break;
				}
			} else if(temp[0].equals("/regist")){
				//가입처리
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
			isOperatorOn = true;
			String name = null;
			try {
				name = dis.readUTF();
				tempUserList.put(name, dos);
				g1Server.appendServerLog(name + " 로그인 세션 열림");
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
//				sendTo(name, " 로그인 서버와 연결 종료");
				g1Server.appendServerLog(name + " 로그인 세션 종료");
			}
		}
	}// ServerOperator
}
