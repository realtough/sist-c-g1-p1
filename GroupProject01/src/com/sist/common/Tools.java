package com.sist.common;

import java.awt.*;
import java.io.*;
import java.net.*;

public class Tools {
	private static Toolkit myTK = Toolkit.getDefaultToolkit();
	public static int centerX = myTK.getScreenSize().width / 2;
	public static int centerY = myTK.getScreenSize().height / 2;
	public static String serverIp = "211.238.142.116";
	public static int portChatServer = 10000;
	public static int portLoginServer = 10001;
	private boolean sendSuspend = true;
	
	//�׸���� ���̾ƿ� �� �޼ҵ�
	public static void insert(Container cnt, Component cmp, int x, int y,
			int w, int h) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.insets = new Insets(2, 2, 2, 2);
		cnt.add(cmp, gbc);
	}

	public static void insert(Container cnt, Component cmp, int x, int y,
			int w, int h, double wx, double wy) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.insets = new Insets(2, 2, 2, 2);
		cnt.add(cmp, gbc);
	}

	public static void insert(Container cnt, Component cmp, int x, int y,
			int w, int h, double wx, double wy, int insetSize) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.insets = new Insets(insetSize, insetSize, insetSize, insetSize);
		cnt.add(cmp, gbc);
	}
	
	//ClientReceiver �� ClientSender �� common��Ű���� �з� �õ���
	
	public class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream dis;

		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (dis != null) {					
					dis.readUTF();
				}
			} catch (IOException ioe) {
				// TODO: handle exception
				ioe.printStackTrace();
			}
		}

	}

	public class ClientSender extends Thread {
		Socket socket;
		DataOutputStream dos;
		String name;

		public ClientSender(String userName, Socket socket) {
			// socket�� output ��Ʈ���� write�Ѵ�
			this.socket = socket;
			this.name = userName;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				if (dos != null) {
					dos.writeUTF(name); // ���� ���ӽ� �̸��� ���� �����Ѵ�
				}
				while (dos != null) {
					if (!sendSuspend) {
						dos.writeUTF("");						
						sendSuspend = true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
