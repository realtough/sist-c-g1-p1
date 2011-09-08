import java.awt.Toolkit;

import javax.swing.*;

//로비화면의 실제 폼과 기능 구현
public class LobbyMain extends JFrame {

	JPanel jpUserInfo = new JPanel();
	JPanel jpChat = new JPanel();
	JPanel jpGameList = new JPanel();
	JPanel jpPlayerList = new JPanel();
	
	Toolkit tkMyTool = Toolkit.getDefaultToolkit();

	public LobbyMain() {
		super("Mini Game Paradise");
		
		//Test//
		JMenu jmGameMenu = new JMenu("Test");
		JButton jb = new JButton("화면 크기 시험용");
		JMenuBar jmb = new JMenuBar();
		jmb.add(jmGameMenu);
		setJMenuBar(jmb);
		setLayout(null);
		
		jb.setBounds(0, 0, 600, 430);
		add(jb);
		//Test//			

		int x = tkMyTool.getScreenSize().width;
		int y = tkMyTool.getScreenSize().height;
		
		//프레임 기본설정 (크기는 800*600, 위치는 항상 중앙, 리사이징 불가)
		setBounds(x / 2 - 400, y / 2 - 300, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new LobbyMain();
	}
}
