package com.sist.server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

import com.sist.common.Tools;
import com.sist.common.UserInfoManagerDAO;

//로그인과 유저정보 관리를 별도 서버로 분리할지 고려할것
public class ServerForm extends JFrame implements ActionListener {
	Dimension dSize = new Dimension(640, 600);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);

	boolean isServerOn = false;

	private JScrollBar jbScrollBar;
	private JTextArea jtaServerLog = new JTextArea();
	private JScrollPane jsPane = new JScrollPane(jtaServerLog);
	private JTextField jtfServerInput = new JTextField();

	private JMenuBar jmb = new JMenuBar();
	private JMenu jmServer = new JMenu("서버");
	private JMenuItem jmStart = new JMenuItem("시작");
	private JMenuItem jmClose = new JMenuItem("종료");
	private JMenu jmLog = new JMenu("로그");
	private JMenuItem jmSave = new JMenuItem("저장");
	private JMenuItem jmClear = new JMenuItem("삭제");
	LoginServer liServer = new LoginServer(this);
	MainServer mnServer = new MainServer(this);

	public ServerForm() {
		jbScrollBar = jsPane.getVerticalScrollBar();
		jtaServerLog.setEditable(false);

		add("Center", jsPane);
		add("South", jtfServerInput);

		// 메뉴바 설정
		jmb.add(jmServer);
		jmServer.add(jmStart);
		jmServer.add(jmClose);
		jmb.add(jmLog);
		jmLog.add(jmSave);
		jmLog.add(jmClear);

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
		jmSave.addActionListener(this);
		jmClear.addActionListener(this);
		jtfServerInput.addActionListener(this);
	}

	public void appendServerLog(String msg) {
		jtaServerLog.append(msg + "\n");
		jbScrollBar.setValue(jbScrollBar.getMaximum());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServerForm();
	}

	public void saveLog(){
		File logFile = new File(".\\serverlog\\"+new Date().toString()+".txt");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jmStart) {
			if (!isServerOn) {
				isServerOn = true;
				appendServerLog("서버가 시작 되었습니다");
				liServer.start();
				mnServer.start();
			} else{
				JOptionPane.showMessageDialog(this, "서버가 이미 가동 중 입니다");
			}
		} else if (ob == jmClose) {
			appendServerLog("서버가 종료 되었습니다");
			isServerOn = false;
			liServer.stopServer();			
//			mnServer.isServerOn = false;
//			mnServer.stopServerOperator();
		} else if (ob == jmSave) {

		} else if (ob == jmClear) {
			jtaServerLog.setText("");
		}
		if (ob == jtfServerInput) {
			// mnServer.sendToAll("서버", jtfServerInput.getText());
			jtfServerInput.setText("");
		}
	}
}// class
