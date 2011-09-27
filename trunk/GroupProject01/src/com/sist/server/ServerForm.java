package com.sist.server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

import com.sist.common.Tools;

// 로그인과 메인서버의 로그를 출력하는 폼
public class ServerForm extends JFrame implements ActionListener {
	private Dimension dSize = new Dimension(640, 600);
	private Dimension dPosition = new Dimension(
			Tools.centerX - dSize.width / 2, Tools.centerY - dSize.height / 2);
	private CardLayout card = new CardLayout();
	private JPanel jpMainBoard = new JPanel();

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
	private JMenu jmUser = new JMenu("유저");
	private JMenuItem jmAllUser = new JMenuItem("전체유저");
	private JMenuItem jmConnUser = new JMenuItem("접속유저");
	private JMenuItem jmSendUser = new JMenuItem("정보전송");

	boolean isServerOn = false;

	private LoginServer liServer;
	private MainServer mnServer;
	private UserListForm ulForm = new UserListForm();;

	public ServerForm() {
		setLayout(card);

		liServer = new LoginServer(this);
		mnServer = new MainServer(this);
		jbScrollBar = jsPane.getVerticalScrollBar();
		jtaServerLog.setEditable(false);

		jpMainBoard.setLayout(new BorderLayout());
		jpMainBoard.add("Center", jsPane);
		jpMainBoard.add("South", jtfServerInput);

		add("LOG", jpMainBoard);
		add("LIST", ulForm);

		// 메뉴바 설정
		jmb.add(jmServer);
		jmServer.add(jmStart);
		jmServer.add(jmClose);
		jmb.add(jmLog);
		jmLog.add(jmSave);
		jmLog.add(jmClear);
		jmb.add(jmUser);
		jmUser.add(jmAllUser);
		jmUser.add(jmConnUser);
		jmUser.add(jmSendUser);

		// 임시 사용불능 기능들
		jmClose.setEnabled(false);
		jmSendUser.setEnabled(false);
		jtfServerInput.setEnabled(false);

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
		jmAllUser.addActionListener(this);
		jmConnUser.addActionListener(this);
		jmSendUser.addActionListener(this);
		jtfServerInput.addActionListener(this);
		ulForm.jbClose.addActionListener(this);
	}

	public void appendServerLog(String msg) {
		jtaServerLog.append(msg + "\n");
		jbScrollBar.setValue(jbScrollBar.getMaximum());
	}

	public String getServerLog() {
		return jtaServerLog.getText().trim();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServerForm();
	}

	public void saveLog() {
		File logFile = null;
		FileWriter writer = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmmss");
		try {
			logFile = new File(".\\serverlog\\" + sdf.format(new Date())
					+ ".txt");
			logFile.createNewFile();
			writer = new FileWriter(logFile);
			writer.write(getServerLog());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jmStart) {
			if (!isServerOn) {
				isServerOn = true;
				liServer.start();
				mnServer.start();
			} else {
				JOptionPane.showMessageDialog(this, "서버가 이미 가동 중 입니다");
			}
		} else if (ob == jmClose) {
			// appendServerLog("서버가 종료 되었습니다");
			// isServerOn = false;
			// liServer.stopServer();
			// mnServer.stopServerOperator();
		} else if (ob == jmSave) {
			// 로그를 파일로 저장
			saveLog();
		} else if (ob == jmClear) {
			jtaServerLog.setText("");
		} else if (ob == jmAllUser) {
			// 전체 유저 목록 로딩
			// 카드레이아웃을 이용한 별도의 패널에 테이블로 표시
			card.show(getContentPane(), "LIST");
		} else if (ob == jmConnUser) {
			// 접속 중인 유저 목록 로딩
			// 닉네임으로 간단히 표시?
		} else if (ob == jmSendUser) {
			
		} else if (ob == ulForm.jbClose) {
			card.show(getContentPane(), "LOG");
		}
		// if (ob == jtfServerInput) {
		// mnServer.sendToAll("서버", jtfServerInput.getText());
		// jtfServerInput.setText("");
		// }
	}
}// class
