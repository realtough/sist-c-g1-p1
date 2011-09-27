package com.sist.server;

import java.io.*;
import java.net.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.sist.common.*;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

//최초 입력된 아이디와 패스워드를 기반으로 UIM을 이용 유저정보 조회
//정보 인증시 닉네임을 받아 LobbyLogin에 전달
public class LoginServer extends Thread implements G1Server {
	private ServerForm g1Server;

	// 유저정보 처리를 담당할 uiManager객체 생성
	private UserInfoDAO uiManager = new UserInfoDAO();
	private HashMap<String, DataOutputStream> tempUserList = new HashMap<String, DataOutputStream>();
//	private HashMap<String, BufferedWriter> tempUserList = new HashMap<String, BufferedWriter>();

	public LoginServer(ServerForm g1Server) {
		this.g1Server = g1Server;
	}

	// 유저 가입, 로그인등의 유저정보를 다룰 별도 서버 생성
	private void loginServerStart() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(Tools.LOGIN_SERVER_PORT);
			InetAddress inet = InetAddress.getLocalHost();
			g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER
					+ inet.getHostAddress() + ":" + serverSocket.getLocalPort());

			while (true) { // 무한반복하며 연결이 들어올 경우 리시버 쓰레드를 생성해 연결
				socket = serverSocket.accept();
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
		} catch (IOException e) {
			g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + e.getMessage());
		}
	}

	public void run() {
		loginServerStart();
	}

	class ServerOperator extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;
		BufferedReader bfReader;
		BufferedWriter bfWriter;
		boolean isOperatorOn = true;

		public ServerOperator(Socket socket) {
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				// bfReader = new BufferedReader(new
				// InputStreamReader(socket.getInputStream()));
				// bfWriter = new BufferedWriter(new
				// OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER
						+ e.getMessage());
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
					sendTo("login", name, "11#" + uiManager.getUserInfo(temp[1]));
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
							+ " 로그인 성공");
					isOperatorOn = false;
					break;
				case 12:
					// 아이디 일치, 패스워드 틀릴때 (경고창 띄운후 재시도)
					sendTo("login", name, "12");
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
							+ " 로그인 실패 (비밀번호 틀림)");
					break;
				case 22:
					// 아이디 없을때 (경고창 띄운후 재시도)
					sendTo("login", name, "22");
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
							+ " 로그인 실패 (아이디 틀림)");
					break;
				default:
					sendTo("login", name, "33");
					g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
							+ " 로그인 실패 (비정상 오류)");
					break;
				}
			} else if (temp[0].equals("/regist")) {
				// DAO를 불러와 가입절차 처리
				UserInfoVO uiVO = Tools.stringToUserInfo(temp[1]);
				uiManager.insertUser(uiVO);
			} else if (temp[0].equals("/check")) {
				// DAO를 불러와 가입절차 처리								
				if(uiManager.isExist(temp[1], temp[2])){
					sendTo("regist", name, "check#11");
				}else{
					sendTo("regist", name, "check#22");
				}
			}
		}

		private void sendTo(String from, String to, String msg) {
			try {
				DataOutputStream dos2 = tempUserList.get(to);
				dos2.writeUTF("["+from+"]#" + msg);				
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}

		public void run() {
			String name = null;
			try {
				// 환영메세지 출력후, 접속자 정보를 해쉬맵에 저장
				name = dis.readUTF();
				tempUserList.put(name, dos);
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
						+ " 로그인 세션 열림");
				while (dis != null) {
					if (!isOperatorOn) {
						break;
					}
					classfyMessage(name, dis.readUTF());
				}
			} catch (Exception e) {
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER
						+ e.getMessage());
			} finally { // 퇴장시 처리
				tempUserList.remove(name);
				// sendTo(name, " 로그인 서버와 연결 종료");
				closeStream();
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER + name
						+ " 로그인 세션 종료");
			}
		}

		private void closeStream() {
			try {
				dis.close();
				dos.close();
				// bfReader.close();
				// bfWriter.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				g1Server.appendServerLog(Tools.LOGIN_SERVER_HEADER
						+ e.getMessage());
			}
		}
	}// ServerOperator
}