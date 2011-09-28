package com.sist.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import com.sist.common.*;

//�α��� ������ ��� �Է¹��� ���������� ����
public class LobbyRegister extends JDialog implements ActionListener {

	Dimension dSize = new Dimension(320, 300);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);

	LobbyLogin lbLogin;
	private JLabel jlID = new JLabel("�� �� ��", JLabel.RIGHT);
	private JLabel jlPW = new JLabel("��й�ȣ", JLabel.RIGHT);
	private JLabel jlName = new JLabel(" �� �� ", JLabel.RIGHT);
	private JLabel jlNickName = new JLabel(" �� �� ", JLabel.RIGHT);
	private JLabel jlBirthDate = new JLabel(" �� �� ", JLabel.RIGHT);
	private JLabel jlSex = new JLabel(" �� �� ", JLabel.RIGHT);

	private JTextField jtfID = new JTextField();
	private JPasswordField jpfPW = new JPasswordField();
	private JTextField jtfName = new JTextField();
	private JTextField jtfNickName = new JTextField();
	private JButton jbRegister = new JButton("����");
	private JButton jbCancel = new JButton("���");
	private ButtonGroup bgSex = new ButtonGroup();
	private JRadioButton jrMan = new JRadioButton("��");
	private JRadioButton jrWoman = new JRadioButton("��");
	private SpinnerDateModel sdModel = new SpinnerDateModel();
	private JSpinner jsBirthDate = new JSpinner(sdModel);
	private JSpinner.DateEditor jsDateEditor = new JSpinner.DateEditor(
			jsBirthDate, "YYYY:MM:dd");

	private JButton jbCheckID = new JButton("�ߺ�Ȯ��");
	private JButton jbCheckNick = new JButton("�ߺ�Ȯ��");
	private JPanel jpBody = new JPanel();
//	private JPanel jpBottom = new JPanel();
	private JPanel jpSex = new JPanel();
	
	public LobbyRegister(LobbyLogin lbLogin) {
		super(lbLogin, "�ű� ����", ModalityType.APPLICATION_MODAL);
		this.lbLogin = lbLogin;
		bgSex.add(jrMan);
		bgSex.add(jrWoman);
		jpSex.add(jrMan);
		jpSex.add(jrWoman);

		JSpinner.DefaultEditor jsDefaultEditor = new JSpinner.DefaultEditor(
				jsBirthDate);
		jsDefaultEditor.getTextField().setAlignmentY(CENTER_ALIGNMENT);
		jsBirthDate.setEditor(jsDateEditor);

		jpBody.setLayout(new GridBagLayout());
		Tools.insert(jpBody, jlID, 0, 0, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jtfID, 1, 0, 1, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jbCheckID, 2, 0, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jlPW, 0, 1, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jpfPW, 1, 1, 1, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jlName, 0, 2, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jtfName, 1, 2, 1, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jlNickName, 0, 3, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jtfNickName, 1, 3, 1, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jbCheckNick, 2, 3, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jlSex, 0, 4, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jpSex, 1, 4, 1, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jlBirthDate, 0, 5, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jsBirthDate, 1, 5, 1, 1, 0.9, 0.5, 5);
		// Tools.insert(jpBody, new JLabel(""), 2, 5, 1, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jbRegister, 2, 4, 1, 1);
		Tools.insert(jpBody, jbCancel, 2, 5, 1, 1);
		// jpBottom.setLayout(new GridLayout(1, 5, 2, 2));
		// jpBottom.add(new JLabel());
		// jpBottom.add(jbRegister);
		// jpBottom.add(new JLabel());
		// jpBottom.add(jbCancel);
		// jpBottom.add(new JLabel());

		add("Center", jpBody);
		// add("South", jpBottom);
		setTitle("�ű԰���");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		sdf.format(date);
		
		sdModel.setValue(new Date());
		sdModel.setStart(new Date());
		sdModel.setEnd(new Date());
		
		jbRegister.addActionListener(this);
		jbCancel.addActionListener(this);
		jbCheckID.addActionListener(this);
		jbCheckNick.addActionListener(this);

		setBounds(dPosition.width, dPosition.height, dSize.width, dSize.height);
		setResizable(false);
		
		clearAllForm();
	}	
	
	public void registUser() {
		UserInfoVO uiVO = new UserInfoVO();

		String id = jtfID.getText().trim();
		if (id.length() == 0) {
			JOptionPane.showMessageDialog(this, "���̵� �Է��ϼ���");
			return;
		} else {
			uiVO.setId(id);
		}

		String pw = String.valueOf(jpfPW.getPassword());
		if (pw.length() == 0) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �Է� �ϼ���");
			return;
		} else {
			uiVO.setPw(pw);
		}

		String nick = jtfNickName.getText().trim();
		if (pw.length() == 0) {
			JOptionPane.showMessageDialog(this, "�г����� �Է� �ϼ���");
			return;
		} else {
			uiVO.setNickname(nick);
		}

		String name = jtfName.getText().trim();
		if (pw.length() == 0) {
			JOptionPane.showMessageDialog(this, "�̸��� �Է� �ϼ���");
			return;
		} else {
			uiVO.setName(name);
		}

		char sex = ' ';
		if (jrMan.isSelected()) {
			sex = '1';
		} else if (jrWoman.isSelected()) {
			sex = '2';
		}
		if (sex == ' ') {
			JOptionPane.showMessageDialog(this, "������ ���� �ϼ���");
			return;
		} else {
			uiVO.setSex(sex);
		}
		Date birthDate = sdModel.getDate();
		uiVO.setBirth(birthDate);
		uiVO.setJoinus(new Date());
		lbLogin.sendMessage("/regist " + uiVO.toString());
		setVisible(false);
		lbLogin.setVisible(true);
		dispose();
	}

	public void clearIDForm(){
		jtfID.setText("");
	}
	
	public void clearNickForm(){
		jtfNickName.setText("");
	}
	
	public void clearAllForm(){
		clearIDForm();
		jpfPW.setText("");
		jtfName.setText("");
		clearNickForm();
		jrMan.setSelected(false);
		jrWoman.setSelected(false);
		clearBirthForm();
	}	
	
	private void clearBirthForm(){
		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		cal.add(Calendar.YEAR, -80);
		Date startDate = cal.getTime();
		cal.add(Calendar.YEAR, 60);
		Date nowDate = cal.getTime();
		sdModel.setCalendarField(Calendar.YEAR);
		sdModel.setStart(startDate);
		sdModel.setEnd(endDate);
		sdModel.setValue(nowDate);
		lockInput();
	}
	
	public void lockInput(){
		jpfPW.setEnabled(false);
		jtfName.setEnabled(false);
		jrMan.setEnabled(false);
		jrWoman.setEnabled(false);
		jtfNickName.setEnabled(false);
		jsBirthDate.setEnabled(false);
		jbRegister.setEnabled(false);
		jbCheckNick.setEnabled(false);
	}
	
	public void unlockInput(){
		jpfPW.setEnabled(true);
		jtfName.setEnabled(true);
		jrMan.setEnabled(true);
		jrWoman.setEnabled(true);
		jtfNickName.setEnabled(true);
		jsBirthDate.setEnabled(true);
		jbRegister.setEnabled(true);
		jbCheckNick.setEnabled(true);
	}
	
	private void checkExist(String ident, String msg) {
		if(msg.length()==0){
			JOptionPane.showMessageDialog(this, ident.toUpperCase() + " �� �Է��ϼ���");
		}
		lbLogin.sendMessage("/check" + " " + ident + " " + msg);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jbRegister) {
			registUser();
		} else if (ob == jbCancel) {
			setVisible(false);
			lbLogin.setVisible(true);
			dispose();
		} else if (ob == jbCheckID) {
			checkExist("id", jtfID.getText().trim());
		} else if (ob == jbCheckNick) {
			checkExist("nickname", jtfNickName.getText().trim());
		}
	}

}
