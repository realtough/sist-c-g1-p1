import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

//�κ�ȭ���� ���� ���� ��� ����
public class LobbyMain extends JFrame implements ActionListener{
	
	// ����ȿ���� ī�巹�̾ƿ� ����
	Border bdMainEdge = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	CardLayout card = new CardLayout();

	// �޴� ���� �� ����
	JMenuBar jmb = new JMenuBar();
	JMenu jmGameMenu = new JMenu("����");
	JMenuItem jmLogin = new JMenuItem("�α���");
	JMenuItem jmExit = new JMenuItem("����");	
	
	JPanel jpMain = new JPanel();
	JPanel jpRightTab = new JPanel();
	JPanel jpUserInfo = new JPanel();
	JTable jtUserList;
	DefaultTableModel dtModel;

	JPanel jpGameMain = new JPanel();

	JPanel jpChat = new JPanel();
	JTextPane jtpChatList = new JTextPane();
	JTextField jtfChatInput = new JTextField();

	//JFrame���� ��ġ�� ������� ��Ŷ�� ��ǥ ����
	Toolkit tkMyTool = Toolkit.getDefaultToolkit();
	int positionX = tkMyTool.getScreenSize().width / 2 - 405;
	int positionY = tkMyTool.getScreenSize().height / 2 - 325;
	
	public LobbyMain() {
		super("Mini Game Paradise");

		// �޴��� ����
		jmGameMenu.add(jmLogin);
		jmGameMenu.add(jmExit);
		jmb.add(jmGameMenu);
		setJMenuBar(jmb);
		
		// ���� ���� ���� ���̺� ����
		String col[] = { "���̵�", "�г���" };
		String row[][] = new String[0][2];
		dtModel = new DefaultTableModel(row, col);
		jtUserList = new JTable(dtModel);

		// Test//
		jpRightTab.setBackground(Color.RED);
		jpChat.setBackground(Color.GREEN);
		jpGameMain.setBackground(Color.BLUE);
		jpGameMain.setLayout(card);		
			
		jpMain.setLayout(null);		
		jpGameMain.setBounds(5, 5, 600, 430);		
		jpMain.add(jpGameMain);	
		jpChat.setBounds(5, 440, 600, 150);
		jpMain.add(jpChat);
		jpRightTab.setBounds(610, 5, 190, 585);
		jpMain.add(jpRightTab);
		jpMain.setBorder(bdMainEdge);
		add(jpMain);
		// Test//

		// ������ �⺻���� (��ġ�� �׻� �߾�, ������¡ �Ұ�)
		setBounds(positionX, positionY, 810, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		//���� ����� �α���â ���
		new LobbyLogin(positionX, positionY);
		
		//�׼��̺�Ʈ ����
		jmLogin.addActionListener(this);
		jmExit.addActionListener(this);
	}

	public static void main(String[] args) {
		new LobbyMain();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object ob = e.getSource();
		if(ob==jmLogin){
			new LobbyLogin(positionX, positionY);
		}
		if(ob==jmExit){
			System.exit(0);
		}
	}
}
