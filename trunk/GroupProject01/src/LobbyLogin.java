import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LobbyLogin extends JFrame implements ActionListener{
	
	private JPanel jpButtonPanel = new JPanel();
	private JLabel jlID = new JLabel("���̵�   ", JLabel.RIGHT);
	private JLabel jlPW = new JLabel("��й�ȣ   ", JLabel.RIGHT);
	private JTextField jtfID = new JTextField();
	private JPasswordField jpfPW = new JPasswordField();
	private JButton jbAccept = new JButton("�α���");
	private JButton jbCancel = new JButton("������");

	public LobbyLogin(int frameX, int frameY) {
		super("�α��� �ϼ���");
		setLayout(new GridBagLayout());
		jpButtonPanel.setLayout(new GridLayout(1, 3, 2, 2));

		jpButtonPanel.add(jbAccept);
		jpButtonPanel.add(new JLabel());
		jpButtonPanel.add(jbCancel);

		insert(jlID, 0, 0, 1, 1, 0.0, 0.5);
		insert(jlPW, 0, 1, 1, 1, 0.0, 0.5);
		insert(jtfID, 1, 0, 2, 1, 0.9, 0.5);
		insert(jpfPW, 1, 1, 2, 1, 0.9, 0.5);

		insert(jpButtonPanel, 0, 3, 4, 1, 0.5, 0.5);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(frameX + 405 - 125, frameY + 325 - 75, 250, 150);
		setResizable(false);
		setVisible(true);
		
		jbAccept.addActionListener(this);
		jbCancel.addActionListener(this);
	}

	private void insert(Component cmp, int x, int y, int w, int h, double wx,
			double wy) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.insets = new Insets(5, 5, 5, 5);
		this.add(cmp, gbc);
	}

	private void checkInfo(String userId, String userPw){
		//���̵� ������
		//���̵�� ������ ��й�ȣ�� Ʋ����
		//������ ��� �ʿ�
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object ob = e.getSource();
		if(ob==jbAccept){
			
		}else if(ob==jbCancel){
			setVisible(false);
			dispose();
		}				
	}
}
