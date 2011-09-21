package com.sist.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.*;

import com.sist.common.ClientOperatorTest;

public class LobbyMainTest extends JFrame implements ActionListener {
	JTextArea jtaChatList = new JTextArea();
	JTextField jtfChatInput = new JTextField();
	JList jlUserList = new JList();

	boolean suspend = true;

	public LobbyMainTest() {
		super("Client");

		jtaChatList.setEditable(false);
		jtfChatInput.requestFocus();
		jtfChatInput.addActionListener(this);

		add("Center", new JScrollPane(jtaChatList));
		add("East", jlUserList);
		add("South", jtfChatInput);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(640, 480, 640, 480);
		setResizable(false);
		setVisible(true);

		jtfChatInput.addActionListener(this);
	}

	void start() {
		try {
			String serverIp = "127.0.0.1";
			Socket socket = new Socket(serverIp, 10000);
			String userName = Integer.toString(socket.getLocalPort());
			jtaChatList.append("서버에 연결 되었습니다\n");
			//수신과 송신을 별도 쓰레드로 만들어 클라이언트 시작과 동시에 실행			
			ChatOperator co = new ChatOperator(userName, socket);
			co.start();
		} catch (ConnectException e) {
			// TODO: handle exception
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) {
		new LobbyMainTest().start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jtfChatInput) {
			//boolean값을 이용해 송신 쓰레드를 제어 (평상시에는 suspend상태로 대기)
			//채팅창에 입력값이 들어오면 suspend를 해제해서 송신 쓰레드를 실행한다
			suspend = false;
		}
	}
	
	class ChatOperator extends ClientOperatorTest{

		public ChatOperator(String userName, Socket socket) {
			super(userName, socket);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void classfyMessage(String msg) {
			String msgtemp[] = msg.split(" ", 3);
			if (msgtemp[0].equals("/sys")) {
				
			} else {
				jtaChatList.append(msg + "\n");
			}
		}
		@Override
		public void run() {
			while (true) {

				if (doStream != null) {		
					if(!suspend){					
						System.out.println("send called in thread");
						outputString = jtfChatInput.getText().trim();
						jtfChatInput.setText("");
						send(outputString);	
						suspend = true;
					}										
				}
				
				if (diStream != null) {
					if (!receiveSuspend)
						receive();
					classfyMessage(inputString);
				}				
			}
		}		
	}
}// class
