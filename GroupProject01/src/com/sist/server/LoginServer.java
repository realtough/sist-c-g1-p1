package com.sist.server;

import java.io.*;
import java.net.*;

import com.sist.common.UserInfoManager;

//최초 입력된 아이디와 패스워드를 기반으로 UIM을 이용 유저정보 조회
//정보 인증시 닉네임을 받아 LobbyLogin에 전달
public class LoginServer extends Thread{
	G1GameServer g1Server;	
	// 유저정보 처리를 담당할 uiManager객체 생성
	private UserInfoManager uiManager = new UserInfoManager();
	
	public LoginServer(G1GameServer g1Server) {
		this.g1Server = g1Server;					 
	}
	
	//유저 가입, 로그인등의 유저정보를 다룰 별도 서버 생성
	private void loginServerStart(){		
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(10001);
			InetAddress inet = InetAddress.getLocalHost();
			g1Server.appendServerLog("[로그인서버] " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort());

			while (true) { // 무한반복하며 연결이 들어올 경우 리시버 쓰레드를 생성해 연결
				socket = serverSocket.accept();
//				ServerReceiver srThread = new ServerReceiver(socket);
//				srThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		loginServerStart();
	}
}
