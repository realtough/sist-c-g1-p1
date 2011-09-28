package com.sist.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sist.common.Tools;

//�α��� ���� ��� ��� ó��. ��ü, 1:1, 1:M ���� ��� ����
//Ŭ���̾�Ʈ�� �г�����  Ű��, �޽��� ���޿� ��½�Ʈ���� ������ �Ͽ� �ؽ��ʿ� ����
public class MainServer extends Thread implements G1Server {
	ServerForm g1Server;
	boolean isServerOn = false;
	private HashMap<String, BufferedWriter> clients; // ������������

	public MainServer(ServerForm g1Server) {
		this.g1Server = g1Server;
		clients = new HashMap<String, BufferedWriter>();
		Collections.synchronizedMap(clients);	// Thread Safe ���·� �����
	}

	private void chatServerStart() {
		isServerOn = true;
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(10000);
			InetAddress inet = InetAddress.getLocalHost();
			// �������� �˸�. �����ǿ� ��Ʈ ǥ��
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
					+ inet.getHostAddress() + ":" + serverSocket.getLocalPort());

			while (true) { // ���ѹݺ��ϸ� ������ ���� ��� ���ù� �����带 ������ ����
				socket = serverSocket.accept();
				ServerOperator soThread = new ServerOperator(socket);
				soThread.start();
			}
		} catch (IOException e) {
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + e.getMessage());
		}
	}

	public void run() {
		chatServerStart();
	}

	class ServerOperator extends Thread {

		private Socket socket;
		private BufferedReader bfReader;
		private BufferedWriter bfWriter;

		public ServerOperator(Socket socket) {
			this.socket = socket;
			String userip = (this.socket.getInetAddress() + ":" + this.socket
					.getPort()).substring(1);
			g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER + userip + " ����");
			try {
				 bfReader = new BufferedReader(new
				 InputStreamReader(this.socket.getInputStream()));
				 bfWriter = new BufferedWriter(new
				 OutputStreamWriter(this.socket.getOutputStream()));
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}

		private void sendTo(String from, String to, String msg,
				boolean isWhisper) {
			try {
				String message = "";
				BufferedWriter bw = clients.get(to);
				if (from.equals("server")) {					
					message = "[" + from + "]#" + msg + "\n"; 
				} else if (isWhisper) {
					BufferedWriter bwf = clients.get(from);
					message = "[" + to + "] �Կ��� �ӼӸ� : " + msg + "\n";
					bwf.write(message);
					bwf.flush();
					message = "[" + from + "] ���� �ӼӸ� : " + msg + "\n";
				} else {	
					message = "[" + from + "] " + msg + "\n";					
				}
				bw.write(message);
				if(bw != null) bw.flush();
				message = "";
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}

		// ��ü ���� ��� ����
		public void sendUserStatus() {
			g1Server.appendServerLog("[���μ���] ���� ������ �� : " + clients.size());
			// ������ ���� ��Ȳ�� ������ ������� (����, �����)
			// �� ���� ���������� ��ü�� �����Ѵ�			
			Iterator<String> clientsName = clients.keySet().iterator();
			String userList[] = clients.keySet().toArray(new String[0]);

			String connectedUser = "userlist#";
			for (int i = 0; i < userList.length; i++) {
				connectedUser += userList[i] + "@";				
			}						
			while (clientsName.hasNext()) {
				sendTo("server", clientsName.next(), connectedUser, false);
			}
		}
		
		// ������ �������� ����
		void sendToAll(String from, String msg) {
			Iterator<String> clientsName = clients.keySet().iterator();
			while (clientsName.hasNext()) {
				sendTo(from, clientsName.next(), msg, false);
			}
		}

		// ���۹��� �޽����� ó���� ���� �з�
		private void classfyMessage(String name, String msg) {
			String temp[] = msg.split(" ", 3);
			if (temp[0].equals("/w")) {
				sendTo(name, temp[1], temp[2], true);
			} else {
				sendToAll(name, msg);
			}
		}

		public void run() {
			String name = null;
			try {
				// ȯ���޼��� �����, ������ ������ �ؽ��ʿ� ����				
				name = bfReader.readLine();				
				clients.put(name, bfWriter);				
				bfWriter.write("�����ϽŰ��� ȯ���մϴ�\n");
				bfWriter.flush();
				sendToAll("server", name + " ���� ���� �ϼ̽��ϴ�");
				sendUserStatus();
				// �ݺ��ϸ� �Է¹��� �޽����� �з� 
				while (bfReader != null){					
					classfyMessage(name, bfReader.readLine());
				}
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			} finally { // ����� ó��
				clients.remove(name);
				sendToAll("����", name + " ���� ���� �ϼ̽��ϴ�");
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ name +" - " +socket.getInetAddress() + ":" + socket.getPort()
						+ " ���� ����");
				closeStream();
				sendUserStatus();
			}
		}

		private void closeStream() {
			try {
				bfReader.close();
				bfWriter.close();
				socket.close();
			} catch (IOException e) {
				g1Server.appendServerLog(Tools.MAIN_SERVER_HEADER
						+ e.getMessage());
			}
		}
	}// ServerOperator
}// class