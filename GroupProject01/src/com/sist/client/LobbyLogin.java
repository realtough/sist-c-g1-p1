package com.sist.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.sist.common.Tools;

//�α��� ������ ���. �Է¹��� ���̵�� �н����带 �ϳ��� ���ڿ��� ���� ����
//���� �޾����� �����κ��� �г����� ���޹޾� LobbyMain�� ����
public class LobbyLogin extends JDialog implements ActionListener {
	private Dimension dSize = new Dimension(250, 150);
	private Dimension dPosition = new Dimension(getParent().getX()
			+ getParent().getSize().width / 2 - dSize.width / 2, getParent()
			.getY() + getParent().getSize().height / 2 - dSize.height / 2);
	private JPanel jpButtonPanel = new JPanel();
	private JLabel jlID = new JLabel("���̵�   ", JLabel.RIGHT);
	private JLabel jlPW = new JLabel("��й�ȣ   ", JLabel.RIGHT);
	private JTextField jtfID = new JTextField();
	private JPasswordField jpfPW = new JPasswordField();
	private JButton jbAccept = new JButton("�α���");
	private JButton jbCancel = new JButton("������");
	private JButton jbRegister = new JButton("����");
	JFrame jfParent = new JFrame();
	
	public LobbyLogin(LobbyMain parent) {
		super(parent, "�α��� �ϼ���", ModalityType.APPLICATION_MODAL);		
		jfParent = parent;
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

		// �̺�Ʈ ���
		jtfID.addActionListener(this);
		jpfPW.addActionListener(this);
		jbAccept.addActionListener(this);
		jbRegister.addActionListener(this);
		jbCancel.addActionListener(this);

		setResizable(false);		
	}

	private void userLogin() {
		// ���̵� ������
		// ���̵�� ������ ��й�ȣ�� Ʋ����
		// ������ ��� �ʿ�
		String id = jtfID.getText().trim();
		String pw = jpfPW.getPassword().toString().trim();
		if(id.length()==0 || pw.length()==0){
			JOptionPane.showMessageDialog(this, "���̵�� �н����带 �Է��ϼ���");
		} else{
//			JOptionPane.showMessageDialog(this, "�������� �ʴ� ���̵� �Դϴ�");
//			JOptionPane.showMessageDialog(this, "�н����尡 Ʋ���ϴ�");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jbAccept) {
			userLogin();
		} else if (ob == jbCancel) {
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
}
