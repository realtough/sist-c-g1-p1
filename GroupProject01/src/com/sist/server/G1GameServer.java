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

//로그인과 유저정보 관리를 별도 서버로 분리할지 고려할것
public class G1GameServer extends JFrame implements ActionListener {	
	Dimension dSize = new Dimension(640, 600);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);
	
	// 유저정보 처리를 담당할 uiManager객체 생성
	private UserInfoManager uiManager = new UserInfoManager();

	boolean isServerClose = true; 
	
	private JTextArea jtaServerLog = new JTextArea();
	private JScrollPane jsPane = new JScrollPane(jtaServerLog);
	private JTextField jtfServerInput = new JTextField();

	private JMenuBar jmb = new JMenuBar();
	private JMenu jmServer = new JMenu("서버");
	private JMenuItem jmStart = new JMenuItem("시작");
	private JMenuItem jmClose = new JMenuItem("종료");

	LoginServer liServer = new LoginServer(this);
	ChattingServer ctServer = new ChattingServer(this);
	
	public G1GameServer() {

		jtaServerLog.setEditable(false);

		add("Center", jsPane);
		add("South", jtfServerInput);

		// 메뉴바 설정
		jmb.add(jmServer);
		jmServer.add(jmStart);
		jmServer.add(jmClose);

		// 프레임 설정
		setJMenuBar(jmb);
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
			ctServer.sendToAll("서버", jtfServerInput.getText());
			jtfServerInput.setText("");
		}
	}
}// class
