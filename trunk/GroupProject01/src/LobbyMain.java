import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

//로비화면의 실제 폼과 기능 구현
public class LobbyMain extends JFrame implements ActionListener{
	
	// 보더효과와 카드레이아웃 설정
	Border bdMainEdge = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	CardLayout card = new CardLayout();

	// 메뉴 선언 및 생성
	JMenuBar jmb = new JMenuBar();
	JMenu jmGameMenu = new JMenu("게임");
	JMenuItem jmLogin = new JMenuItem("로그인");
	JMenuItem jmExit = new JMenuItem("종료");	
	
	JPanel jpMain = new JPanel();
	JPanel jpRightTab = new JPanel();
	JPanel jpUserInfo = new JPanel();
	JTable jtUserList;
	DefaultTableModel dtModel;

	JPanel jpGameMain = new JPanel();

	JPanel jpChat = new JPanel();
	JTextPane jtpChatList = new JTextPane();
	JTextField jtfChatInput = new JTextField();

	//JFrame시작 위치를 잡기위한 툴킷과 좌표 설정
	Toolkit tkMyTool = Toolkit.getDefaultToolkit();
	int positionX = tkMyTool.getScreenSize().width / 2 - 405;
	int positionY = tkMyTool.getScreenSize().height / 2 - 325;
	
	public LobbyMain() {
		super("Mini Game Paradise");

		// 메뉴바 설정
		jmGameMenu.add(jmLogin);
		jmGameMenu.add(jmExit);
		jmb.add(jmGameMenu);
		setJMenuBar(jmb);
		
		// 접속 유저 정보 테이블 생성
		String col[] = { "아이디", "닉네임" };
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

		// 프레임 기본설정 (위치는 항상 중앙, 리사이징 불가)
		setBounds(positionX, positionY, 810, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		//최초 실행시 로그인창 띄움
		new LobbyLogin(positionX, positionY);
		
		//액션이벤트 설정
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
