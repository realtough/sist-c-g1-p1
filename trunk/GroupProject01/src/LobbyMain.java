import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

//�κ�ȭ���� ���� ���� ��� ����
public class LobbyMain extends JFrame {

	JPanel jpUserInfo = new JPanel();
	JPanel jpChat = new JPanel();
	JPanel jpGameList = new JPanel();
	JPanel jpPlayerList = new JPanel();

	Toolkit tkMyTool = Toolkit.getDefaultToolkit();

	public LobbyMain() {
		super("Mini Game Paradise");

		int x = tkMyTool.getScreenSize().width;
		int y = tkMyTool.getScreenSize().height;
		
		//������ �⺻���� (ũ��� 800*600, ��ġ�� �׻� �߾�, ������¡ �Ұ�)
		setBounds(x / 2 - 400, y / 2 - 300, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		new LobbyMain();
	}
}
