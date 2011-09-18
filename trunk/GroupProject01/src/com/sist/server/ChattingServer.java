package com.sist.server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import com.sist.common.Tools;

public class ChattingServer extends JFrame implements ActionListener {
	Dimension dSize = new Dimension(640, 600);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);
	// boolean isServerOn = false;
	private HashMap<String, DataOutputStream> clients;

	private JTextArea jtaServerLog = new JTextArea();
	private JScrollPane jsPane = new JScrollPane(jtaServerLog);
	private JTextField jtfServerInput = new JTextField();

	private JMenuBar jmb = new JMenuBar();
	private JMenu jmServer = new JMenu("서버");
	private JMenuItem jmStart = new JMenuItem("시작");
	private JMenuItem jmClose = new JMenuItem("종료");

	public ChattingServer() {
		// 클라이언트의 정보를 저장할 해쉬맵 clients생성 - key는 id, value는 메시지
		// Thread Safe 상태로 만든다
		clients = new HashMap<String, DataOutputStream>();
		Collections.synchronizedMap(clients);

		jtaServerLog.setEditable(false);

		add("Center", jsPane);
		add("South", jtfServerInput);

		// 메뉴바 설정
		jmb.add(jmServer);
		jmServer.add(jmStart);
		jmServer.add(jmClose);

		// 프레임 설정
		// setJMenuBar(jmb);
		setTitle("G1 Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(dPosition.width, dPosition.height, dSize.width, dSize.height);			
		setResizable(false);
		setVisible(true);

		// 이벤트 등록
		jmStart.addActionListener(this);
		jmClose.addActionListener(this);
		jtfServerInput.addActionListener(this);
	}

	private void serverStart() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(10000);
			InetAddress inet = InetAddress.getLocalHost();
			// 서버시작 알림. 아이피와 포트 표시
			appendServerLog("서버가 시작되었습니다 - " + inet.getHostAddress() + ":"
					+ serverSocket.getLocalPort());

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

	private void appendServerLog(String msg) {
		jtaServerLog.append(msg + "\n");
	}

	private void showUserNumber() {
		appendServerLog("현재 접속자 수는 : " + clients.size() + "명 입니다");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChattingServer().serverStart();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jmStart) {
			// serverStart();
		} else if (ob == jmClose) {
			// setVisible(false);
			// dispose();
			// isServerOn = false;
		}
		if (ob == jtfServerInput) {
			sendToAll("서버", jtfServerInput.getText());
			jtfServerInput.setText("");
		}
	}

	private void sendConnectedUser(){	
		//접속한 유저 상황에 변동이 있을경우 (입장, 퇴장시) 
		//새 접속 유저정보를 전체에 전송한다
		Iterator<String> clientsName = clients.keySet().iterator();
		String userList[] = clients.keySet().toArray(new String[0]);
		
		String connectedUser = "";
		for(int i=0; i<userList.length; i++){
			connectedUser += userList[i] + "|";
		}
		while (clientsName.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream) clients
						.get(clientsName.next());
				dos.writeUTF("[" + "접속유저" + "] " + connectedUser);				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	// 1:1메시지 - 보낸사람과 받는사람 두명에게만 전송한다
	private void sendTo(String from, String to, String msg) {
		//Iterator는 1회용?
		Iterator<String> clientsName = clients.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				String name = clientsName.next();
				if (name.equals(from)) {
					DataOutputStream dos = (DataOutputStream) clients
							.get(name);
					dos.writeUTF("[" + to + "] 님에게 귓속말 : " + msg);
				}
				if (name.equals(to)) {
					DataOutputStream dos = (DataOutputStream) clients
							.get(name);
					dos.writeUTF("[" + from + "] 님의 귓속말 : " + msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 일반 채팅 - 메시지가 들어오면 접속한 전원에게 전송한다
	private void sendToAll(String from, String msg) {
		Iterator<String> clientsName = clients.keySet().iterator();
		while (clientsName.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream) clients
						.get(clientsName.next());
				dos.writeUTF("[" + from + "] " + msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class ServerReceiver extends Thread {

		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		public ServerReceiver(Socket socket) {
			this.socket = socket;
			appendServerLog(socket.getInetAddress() + ":" + socket.getPort()
					+ " 연결");
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
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
			} finally { // 퇴장시 처리
				sendToAll("서버", name + " 님이 퇴장 하셨습니다");
				appendServerLog(socket.getInetAddress() + ":"
						+ socket.getPort() + " 연결 끊김");
				clients.remove(name);
				showUserNumber();
				sendConnectedUser();
			}
		}

		// 메시지 분류
		// /w 닉네임 할말 => 귓속말
		private void classfyMessage(String name, String msg) {
			String temp[] = msg.split(" ", 3);
			if (temp[0].equals("/w")) {
				sendTo(name, temp[1], temp[2]);
			} else {
				sendToAll(name, msg);
			}
		}
	}// ServerReceiver

}// class
