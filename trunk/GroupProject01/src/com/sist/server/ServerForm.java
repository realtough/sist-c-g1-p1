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
public class ServerForm extends JFrame implements ActionListener{	
	Dimension dSize = new Dimension(640, 600);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);

	boolean isServerOn = false; 

	private JScrollBar jbScrollBar;
	private JTextArea jtaServerLog = new JTextArea();
	private JScrollPane jsPane = new JScrollPane(jtaServerLog);
	private JTextField jtfServerInput = new JTextField();

	private JMenuBar jmb = new JMenuBar();
	private JMenu jmServer = new JMenu("����");
	private JMenuItem jmStart = new JMenuItem("����");
	private JMenuItem jmClose = new JMenuItem("����");

	LoginServer liServer = new LoginServer(this);
	MainServer ctServer = new MainServer(this);
	
	public ServerForm() {
		jbScrollBar = jsPane.getVerticalScrollBar();
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

	protected void appendServerLog(String msg) {
		jtaServerLog.append(msg + "\n");
		jbScrollBar.setValue(jbScrollBar.getMaximum());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServerForm();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jmStart) {	
			isServerOn = true;
			liServer.start();		
			ctServer.start();
		} else if (ob == jmClose) {
			isServerOn = false;
		}
		if (ob == jtfServerInput) {
//			ctServer.sendToAll("����", jtfServerInput.getText());			
			jtfServerInput.setText("");
		}
	}
}// class
