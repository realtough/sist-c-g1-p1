package com.sist.server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

import com.sist.common.Tools;

// �α��ΰ� ���μ����� �α׸� ����ϴ� ��
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
	private JMenu jmServer = new JMenu("����");
	private JMenuItem jmStart = new JMenuItem("����");
	private JMenuItem jmClose = new JMenuItem("����");
	private JMenu jmLog = new JMenu("�α�");
	private JMenuItem jmSave = new JMenuItem("����");
	private JMenuItem jmClear = new JMenuItem("����");
	private JMenu jmUser = new JMenu("����");
	private JMenuItem jmAllUser = new JMenuItem("��ü����");
	private JMenuItem jmConnUser = new JMenuItem("��������");
	private JMenuItem jmSendUser = new JMenuItem("��������");

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

		// �޴��� ����
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

		// �ӽ� ���Ҵ� ��ɵ�
		jmClose.setEnabled(false);
		jmSendUser.setEnabled(false);
		jtfServerInput.setEnabled(false);

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
				JOptionPane.showMessageDialog(this, "������ �̹� ���� �� �Դϴ�");
			}
		} else if (ob == jmClose) {
			// appendServerLog("������ ���� �Ǿ����ϴ�");
			// isServerOn = false;
			// liServer.stopServer();
			// mnServer.stopServerOperator();
		} else if (ob == jmSave) {
			// �α׸� ���Ϸ� ����
			saveLog();
		} else if (ob == jmClear) {
			jtaServerLog.setText("");
		} else if (ob == jmAllUser) {
			// ��ü ���� ��� �ε�
			// ī�巹�̾ƿ��� �̿��� ������ �гο� ���̺�� ǥ��
			card.show(getContentPane(), "LIST");
		} else if (ob == jmConnUser) {
			// ���� ���� ���� ��� �ε�
			// �г������� ������ ǥ��?
		} else if (ob == jmSendUser) {
			
		} else if (ob == ulForm.jbClose) {
			card.show(getContentPane(), "LOG");
		}
		// if (ob == jtfServerInput) {
		// mnServer.sendToAll("����", jtfServerInput.getText());
		// jtfServerInput.setText("");
		// }
	}
}// class
