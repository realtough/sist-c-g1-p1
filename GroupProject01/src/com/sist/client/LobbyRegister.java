package com.sist.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;
import com.sist.common.*;

//서버와의 통신은 가능한한 Main클래스로 한정하도록
public class LobbyRegister extends JDialog implements ActionListener {

	Dimension dSize = new Dimension(320, 300);
	Dimension dPosition = new Dimension(Tools.centerX - dSize.width / 2,
			Tools.centerY - dSize.height / 2);

	JDialog jdParent = new JDialog();
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
	private JSpinner.DateEditor jsDateEditor = new JSpinner.DateEditor(jsBirthDate,
			"YYYY:MM:dd");

	private JPanel jpBody = new JPanel();
	private JPanel jpBottom = new JPanel();
	private JPanel jpSex = new JPanel();
	
	public LobbyRegister(JDialog parent) {
		super(parent, "신규 가입", ModalityType.APPLICATION_MODAL);
		jdParent = parent;
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
		Tools.insert(jpBody, jtfID, 1, 0, 2, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jlPW, 0, 1, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jpfPW, 1, 1, 2, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jlName, 0, 2, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jtfName, 1, 2, 2, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jlNickName, 0, 3, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jtfNickName, 1, 3, 2, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jlSex, 0, 4, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jpSex, 1, 4, 1, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, jlBirthDate, 0, 5, 1, 1, 0.1, 0.5, 5);
		Tools.insert(jpBody, jsBirthDate, 1, 5, 1, 1, 0.9, 0.5, 5);
		Tools.insert(jpBody, new JLabel(""), 2, 5, 1, 1, 0.9, 0.5, 5);
		jpBottom.setLayout(new GridLayout(1, 5, 2, 2));
		jpBottom.add(new JLabel());
		jpBottom.add(jbRegister);
		jpBottom.add(new JLabel());
		jpBottom.add(jbCancel);
		jpBottom.add(new JLabel());

		add("Center", jpBody);
		add("South", jpBottom);
		setTitle("신규가입");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		jbRegister.addActionListener(this);
		jbCancel.addActionListener(this);
		
		setBounds(dPosition.width, dPosition.height, dSize.width, dSize.height);
		setResizable(false);		
	}

	public void registUser() {
		String id = jtfID.getText().trim();
		String pw = jpfPW.getPassword().toString().trim();
		String nick = jtfNickName.getText().trim();
		String name = jtfName.getText().trim();
		int sex = 0;
		if (jrMan.isSelected()) {
			sex = 1;
		} else if (jrWoman.isSelected()) {
			sex = 2;
		}
		Date birthDate = sdModel.getDate();
		Date registDate = new Date();

		UserInfo ui = new UserInfo(id, pw, name, birthDate, sex, registDate,
				nick);
		new UserInfoManager().insertUser(ui);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if (ob == jbRegister) {
			registUser();
			setVisible(false);
			jdParent.setVisible(true);
			dispose();
		} else if (ob == jbCancel) {
			setVisible(false);
			jdParent.setVisible(true);
			dispose();			
		}		
	}
	
}
