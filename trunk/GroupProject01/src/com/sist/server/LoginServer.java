package com.sist.server;

import java.io.*;
import java.net.*;

import com.sist.common.UserInfoManager;

//���� �Էµ� ���̵�� �н����带 ������� UIM�� �̿� �������� ��ȸ
//���� ������ �г����� �޾� LobbyLogin�� ����
public class LoginServer extends Thread{
	G1GameServer g1Server;	
	// �������� ó���� ����� uiManager��ü ����
	private UserInfoManager uiManager = new UserInfoManager();
	
	public LoginServer(G1GameServer g1Server) {
		this.g1Server = g1Server;					 
	}
	
	//���� ����, �α��ε��� ���������� �ٷ� ���� ���� ����
	private void loginServerStart(){		
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(10001);
			InetAddress inet = InetAddress.getLocalHost();
			g1Server.appendServerLog("[�α��μ���] " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort());

			while (true) { // ���ѹݺ��ϸ� ������ ���� ��� ���ù� �����带 ������ ����
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
