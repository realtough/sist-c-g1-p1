package com.sist.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;

import com.sist.common.ClientOperator;
import com.sist.common.Tools;

//로그인 서버와 통신. 입력받은 아이디와 패스워드를 하나의 문자열로 합쳐 전송
//인증 받았을시 서버로부터 닉네임을 전달받아 LobbyMain에 전달
public class LobbyLogin extends JDialog implements ActionListener {
	private Dimension dSize = new Dimension(250, 150);
	private Dimension dPosition = new Dimension(getParent().getX()
			+ getParent().getSize().width / 2 - dSize.width / 2, getParent()
			.getY() + getParent().getSize().height / 2 - dSize.height / 2);
	private JPanel jpButtonPanel = new JPanel();
	private JLabel jlID = new JLabel("아이디   ", JLabel.RIGHT);
	private JLabel jlPW = new JLabel("비밀번호   ", JLabel.RIGHT);
	private JTextField jtfID = new JTextField(10);
	private JPasswordField jpfPW = new JPasswordField(8);
	private JButton jbAccept = new JButton("로그인");
	private JButton jbCancel = new JButton("나가기");
	private JButton jbRegister = new JButton("가입");
	
	LobbyMain lobby;
	String id;
	String pw;
	String idpwMessage;
	String userName;

	ClientOperator loginThread;

	public LobbyLogin(LobbyMain parent) {
		super(parent, "로그인 하세요", ModalityType.APPLICATION_MODAL);
		lobby = parent;
		setLayout(new GridBagLayout());
		jpButtonPanel.setLayout(new GridLayout(1, 3, 2, 2));

		jpButtonPanel.add(jbAccept);
		jpButtonPanel.add(jbRegister);
		jpButtonPanel.add(jbCancel);

		Tools.insert(this, jlID, 0, 0, 1, 1, 0.0, 0.5, 5);
		Tools.insert(this, jlPW, 0, 1, 1, 1, 0.0, 0.5, 5);
		Tools.insert(this, jtfID, 1, 0, 2, 1, 0.9, 0.5, 5);
		Tools.insert(this, jpfPW, 1, 1, 2, 1, 0.9, 0.5, 5);

		Tools.insert(this, jpButtonPanel, 0, 3, 4, 1, 0.5, 0.5, 5);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(dPosition.width, dPosition.height, dSize.width, dSize.height);

		// 이벤트 등록
		jtfID.addActionListener(this);
		jpfPW.addActionListener(this);
		jbAccept.addActionListener(this);
		jbRegister.addActionListener(this);
		jbCancel.addActionListener(this);

		setResizable(false);
	}

	public void lobbyLoginStart() {
		try {
			Socket socket = new Socket(Tools.serverIp, Tools.portLoginServer);
			userName = socket.getLocalAddress()+":"+socket.getLocalPort();			
			loginThread = new ClientOperator(lobby, userName, socket);
			loginThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void userLogin() {
		// 아이디가 없을때
		// 아이디는 맞지만 비밀번호가 틀릴때
		// 서버와 통신 필요
		if(checkInfo()){
			idpwMessage = "/login " + id + " " + pw;
			loginThread.sendMessage(idpwMessage);			
		}
	}

	private boolean checkInfo() {
		boolean isCorrect = false;
		id = jtfID.getText().trim();
		pw = String.valueOf(jpfPW.getPassword());
		
		if (id.length() == 0 || pw.length() == 0) {
			JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력하세요");
		}else{
			isCorrect = true;
		}
		return isCorrect;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jbAccept) {
			userLogin();
		} else if (ob == jbCancel) {			
			lobby.startChat(userName);
			loginThread.stopOperator();
			setVisible(false);
			dispose();
		} else if (ob == jbRegister) {
			setVisible(false);
			new LobbyRegister(this).setVisible(true);
		} else if (ob == jtfID) {
			jpfPW.requestFocus();
		} else if (ob == jpfPW) {
			userLogin();
		}
	}

	public void classfyMessage(String msg) {				
		String msgtemp[] = msg.split("@");
//		System.out.println(msg);
		if (msgtemp[0].equals("[로그인서버]")) {
			switch(Integer.parseInt(msgtemp[1])){
			case 11:	//아이디와 패스워드 일치 로그인 성공									
				loginThread.stopOperator();
				lobby.startChat(msgtemp[2]);
				setVisible(false);
				dispose();
				break;
			case 12:				
				JOptionPane.showMessageDialog(this, "비밀번호가 틀립니다");					
				break;
			case 22:				
				JOptionPane.showMessageDialog(this, "아이디가 틀립니다");
				break;
			}
		}
	}
}
