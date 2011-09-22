package com.sist.common;

import java.io.*;
import java.net.Socket;

import com.sist.client.LobbyMain;

public class ClientOperator extends Thread {
	LobbyMain g1Lobby;
	private boolean isOperatorOn = false;
	private String inputString = "";
	private String outputString = "";
	String userName;
	Socket socket;
	ClientReceiver crThread;
	ClientSender csThread;

	public ClientOperator(LobbyMain g1Lobby, String userName, Socket socket) {
		this.g1Lobby = g1Lobby;
		this.userName = userName;
		this.socket = socket;
		startOperator();
	}

	public void startOperator() {
		isOperatorOn = true;
		crThread = new ClientReceiver(socket);
		csThread = new ClientSender(userName, socket);
		crThread.start();
		csThread.start();
	}

	public void stopOperator() {
		try {
			crThread.dis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		crThread.stopReceiver();
		csThread.stopSender();
		isOperatorOn = false;
	}

	public void sendMessage(String msg) {
		outputString = msg;
		csThread.unsuspendSender();
	}

	public void receiveMessage() {		
		String msgtemp[] = inputString.split("@");
		if (msgtemp[0].equals("[로그인서버]")) {
			g1Lobby.lLogin.classfyMessage(inputString);
		} else {			
			g1Lobby.classfyMessage(inputString);
		}
		inputString = "";
	}

	public void run() {		
		System.out.println("Operator Start");
		try {
			sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			if (!isOperatorOn) {
				break;
			}
			if (inputString.length() != 0) {
				receiveMessage();
			}
		}
		try {
			sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Operator stop");
	}

	class ClientReceiver extends Thread {
		boolean isReceiverOn;
		// private boolean isReceiverSuspend = false;
		Socket socket;
		DataInputStream dis;

		public ClientReceiver(Socket socket) {
			isReceiverOn = true;
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void stopReceiver() {
			isReceiverOn = false;
		}

		public void run() {
			try {
				sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Receiver Start");
			try {				
				while (dis != null) {					
					if (!isReceiverOn) {					
						break;
					}				
					inputString = dis.readUTF();					
				}
			} catch (IOException ioe) {
				// TODO: handle exception
				ioe.printStackTrace();			
			} 
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Receiver stop");
		}
	}// ClientReceiver

	class ClientSender extends Thread {
		boolean isSenderOn = false;
		boolean isSenderSuspend = true;

		Socket socket;
		DataOutputStream dos;
		String name;

		public ClientSender(String userName, Socket socket) {
			// socket의 output 스트림에 write한다
			isSenderOn = true;
			this.socket = socket;
			this.name = userName;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void unsuspendSender() {
			isSenderSuspend = false;
		}

		public void stopSender() {
			isSenderOn = false;
		}

		public void run() {
			try {
				sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Sender Start");
			try {
				if (dos != null) {
					dos.writeUTF(name); // 최초 접속시 이름을 먼저 전송한다					
				}

				while (dos != null) {
					if (!isSenderOn) {
						break;
					}
					if (!isSenderSuspend) {
						dos.writeUTF(outputString);
						outputString = "";
						isSenderSuspend = true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Sender stop");
		}
	}// ClientSender
}// class
