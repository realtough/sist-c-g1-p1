package com.sist.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;
import com.sist.common.*;

//로그인 서버와 통신 입력받은 유저정보를 전달
public class LobbyRegister extends JDialog implements ActionListener {

	Dimension dSize = new Dimension(320, 300);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);

	LobbyLogin lbLogin;
	private JLabel jlID = new JLabel("아 이 디", JLabel.RIGHT);
	private JLabel jlPW = new JLabel("비밀번호", JLabel.RIGHT);
	private JLabel jlName = new JLabel(" 이 름 ", JLabel.RIGHT);
	private JLabel jlNickName = new JLabel(" 별 명 ", JLabel.RIGHT);
	private JLabel jlBirthDate = new JLabel(" 생 일 ", JLabel.RIGHT);
	private JLabel jlSex = new JLabel(" 성 별 ", JLabel.RIGHT);

	private JTextField jtfID = new JTextField();
	private JPasswordField jpfPW = new JPasswordField();
	private JTextField jtfName = new JTextField();
	private JTextField jtfNickName = new JTextField();
	private JButton jbRegister = new JButton("가입");
	private JButton jbCancel = new JButton("취소");
	private ButtonGroup bgSex = new ButtonGroup();
	private JRadioButton jrMan = new JRadioButton("남");
	private JRadioButton jrWoman = new JRadioButton("여");
	private SpinnerDateModel sdModel = new SpinnerDateModel();
	private JSpinner jsBirthDate = new JSpinner(sdModel);
	private JSpinner.DateEditor jsDateEditor = new JSpinner.DateEditor(
			jsBirthDate, "YYYY:MM:dd");

	private JButton jbCheckID = new JButton("중복확인");
	private JButton jbCheckNick = new JButton("중복확인");
	private JPanel jpBody = new JPanel();
	private JPanel jpBottom = new JPanel();
	private JPanel jpSex = new JPanel();

	public LobbyRegister(LobbyLogin lbLogin) {
		super(lbLogin, "신규 가입", ModalityType.APPLICATION_MODAL);
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
		setTitle("신규가입");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		jbRegister.addActionListener(this);
		jbCancel.addActionListener(this);
		jbCheckID.addActionListener(this);
		jbCheckNick.addActionListener(this);

		setBounds(dPosition.width, dPosition.height, dSize.width, dSize.height);
		setResizable(false);
	}

	public void registUser() {
		UserInfoVO uiVO = new UserInfoVO();

		String id = jtfID.getText().trim();
		if (id.length() == 0) {
			JOptionPane.showMessageDialog(this, "아이디를 입력하세요");
			return;
		} else {
			uiVO.setId(id);
		}

		String pw = String.valueOf(jpfPW.getPassword());
		if (pw.length() == 0) {
			JOptionPane.showMessageDialog(this, "비밀번호를 입력 하세요");
			return;
		} else {
			uiVO.setPw(pw);
		}

		String nick = jtfNickName.getText().trim();
		if (pw.length() == 0) {
			JOptionPane.showMessageDialog(this, "닉네임을 입력 하세요");
			return;
		} else {
			uiVO.setNickname(nick);
		}

		String name = jtfName.getText().trim();
		if (pw.length() == 0) {
			JOptionPane.showMessageDialog(this, "이름을 입력 하세요");
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
			JOptionPane.showMessageDialog(this, "성별을 선택 하세요");
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
			lbLogin.sendMessage("/check c_name " + "");
		} else if (ob == jbCheckNick) {
			lbLogin.sendMessage("/check nname " + "");
		}
	}

}
