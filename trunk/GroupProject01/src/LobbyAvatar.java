
import javax.swing.*;

//�ƹ�Ÿ ����â
public class LobbyAvatar extends JFrame{
	//������ ������ -> 110x185
	ImageIcon male01 = new ImageIcon(".\\image\\avatar\\male01.gif");
	JButton jbTest = new JButton(male01);
	 				
	public LobbyAvatar(){				
		add(jbTest);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(110, 185);
		setVisible(true);
	}

	public static void main(String[] args) {
		new LobbyAvatar();
	}
}
