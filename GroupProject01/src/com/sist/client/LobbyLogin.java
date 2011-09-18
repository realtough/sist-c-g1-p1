package com.sist.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.sist.common.Tools;

public class LobbyLogin extends JDialog implements ActionListener {
	private Dimension dSize = new Dimension(250, 150);
	private JPanel jpButtonPanel = new JPanel();
	private JLabel jlID = new JLabel("���̵�   ", JLabel.RIGHT);
	private JLabel jlPW = new JLabel("��й�ȣ   ", JLabel.RIGHT);
	private JTextField jtfID = new JTextField();
	private JPasswordField jpfPW = new JPasswordField();
	private JButton jbAccept = new JButton("�α���");
	private JButton jbCancel = new JButton("������");
	private JButton jbRegister = new JButton("����");

	public LobbyLogin(JFrame parent) {
		super(parent, "�α��� �ϼ���", ModalityType.APPLICATION_MODAL);
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
		setBounds(parent.getSize().width / 2 + dSize.width,
				parent.getSize().height / 2 + dSize.height, dSize.width,
				dSize.height);

		// �̺�Ʈ ���
		jbAccept.addActionListener(this);
		jbRegister.addActionListener(this);
		jbCancel.addActionListener(this);

		setResizable(false);
		setVisible(true);
	}

	private void checkInfo(String userId, String userPw) {
		// ���̵� ������
		// ���̵�� ������ ��й�ȣ�� Ʋ����
		// ������ ��� �ʿ�
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object ob = e.getSource();
		if (ob == jbAccept) {
			String id = jtfID.getText().trim(); 
			String pw = jpfPW.getPassword().toString().trim();
			checkInfo(id, pw);
		} else if (ob == jbCancel) {
			setVisible(false);
			dispose();
		} else if (ob == jbRegister) {
			setVisible(false);
			dispose();
			new LobbyRegister().requestFocus();
		}
	}
}
