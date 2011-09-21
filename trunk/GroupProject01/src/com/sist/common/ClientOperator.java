package com.sist.common;

import java.io.*;
import java.net.Socket;

abstract public class ClientOperator extends Thread {
	protected String userName;
	protected Socket socket;
	protected DataInputStream diStream;
	protected DataOutputStream doStream;

	protected String outputString = "";
	protected String inputString = "";

	protected boolean isSuspend = false;
	protected boolean isStop = false;
	public boolean sendSuspend = false;
	protected boolean receiveSuspend = false;
	protected boolean sendStop = false;
	protected boolean receiveStop = false;
	protected boolean isFirstRun = true;

	public ClientOperator(String userName, Socket socket) {
		this.userName = userName;
		this.socket = socket;

		try {
			diStream = new DataInputStream(socket.getInputStream());
			doStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receive() {
		try {
			inputString = diStream.readUTF();
		} catch (IOException ioe) {
			// TODO: handle exception
		}
	}

	public void send() {
		try {
			if (isFirstRun) {
				doStream.writeUTF(userName);
				isFirstRun = false;
			} else {
				doStream.writeUTF(outputString);
			}			
		} catch (IOException ioe) {
			// TODO: handle exception
		}
	}

	// 리시버가 넘겨받은 메시지를 처리한다
	// 식별자와 본문을 분리해 사용
	public abstract void classfyMessage(String msg);

	abstract public void run();
	// {
	// sendSuspend = false;
	// while(true){
	// if(doStream != null){
	// if(!sendSuspend) send();
	// sendSuspend = true;
	// }
	// if(diStream != null){
	// if(!receiveSuspend) receive();
	// }
	// }
	// }

}// class