package com.sist.server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import com.sist.common.Tools;
import com.sist.common.UserInfoManager;

//�α��ΰ� �������� ������ ���� ������ �и����� ����Ұ�
public class G1GameServer extends JFrame implements ActionListener {	
	Dimension dSize = new Dimension(640, 600);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);
	
	// �������� ó���� ����� uiManager��ü ����
	private UserInfoManager uiManager = new UserInfoManager();

	boolean isServerClose = true; 
	
	private JTextArea jtaServerLog = new JTextArea();
	private JScrollPane jsPane = new JScrollPane(jtaServerLog);
	private JTextField jtfServerInput = new JTextField();

	private JMenuBar jmb = new JMenuBar();
	private JMenu jmServer = new JMenu("����");
	private JMenuItem jmStart = new JMenuItem("����");
	private JMenuItem jmClose = new JMenuItem("����");

	LoginServer liServer = new LoginServer(this);
	ChattingServer ctServer = new ChattingServer(this);
	
	public G1GameServer() {

		jtaServerLog.setEditable(false);

		add("Center", jsPane);
		add("South", jtfServerInput);

		// �޴��� ����
		jmb.add(jmServer);
		jmServer.add(jmStart);
		jmServer.add(jmClose);

		// ������ ����
		setJMenuBar(jmb);
		setTitle("G1 Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(dPosition.width, dPosition.height, dSize.width, dSize.height);
		setResizable(false);
		setVisible(true);

		// �̺�Ʈ ���
		jmStart.addActionListener(this);
		jmClose.addActionListener(this);
		jtfServerInput.addActionListener(this);
	}	

	private void appendServerLog(String msg) {
		jtaServerLog.append(msg + "\n");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		G1GameServer myServer = new G1GameServer();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jmStart) {	
			isServerClose=false;
			liServer.start();
			ctServer.start();
		} else if (ob == jmClose) {
			isServerClose = true;
		}
		if (ob == jtfServerInput) {
			ctServer.sendToAll("����", jtfServerInput.getText());
			jtfServerInput.setText("");
		}
	}
}// class
