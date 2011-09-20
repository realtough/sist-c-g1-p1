package com.sist.client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class CatchMind extends JFrame implements MouseMotionListener {

	JTextField tf1,tf2,tf3,tf4;
	JButton bt1,bt2,bt3;
	JTextPane jp1;
	//Canvas cv1;
	JPanel p5=new JPanel();
	
	int x=0;
	int y=0;
	
	Image img=null;
	Graphics gImg=null;
	
	public CatchMind(){
		tf1=new JTextField();  //정답 텍스트 필드
		tf2=new JTextField(30); //정답 쓰는 텍스트필드
		tf3=new JTextField(15); //아이디 
		tf4=new JTextField(15); //성별
		
		
		bt1=new JButton("지우개"); //지우개 버튼
		bt2=new JButton("전체지움");//전체지움 버튼
		bt3=new JButton("나가기");  //나가기 버튼	
		jp1=new JTextPane();
		//cv1=new Canvas();
		
		
		p5.setBackground(Color.gray); 
	
		JPanel p1=new JPanel();
		p1.add(bt1);
		p1.add(bt2);
		p1.add(tf2);
		
		JPanel p2=new JPanel();	
		p2.setLayout(new BorderLayout());
		p2.add("Center",p5);
		p2.add("South",p1);
		
		JPanel p3=new JPanel();
		p3.setLayout(new GridLayout(3,1));
		p3.add(tf3);
		p3.add(tf4);
		p3.add(bt3);
		
		JPanel p4=new JPanel();
		p4.setLayout(new BorderLayout());
		p4.add("Center",jp1);
		p4.add("South",p3);
		
		add("Center",p2);
		add("East",p4);
		
		
		
		setSize(800,600);
		setVisible(true);	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		p5.addMouseMotionListener(this);
		
		img=createImage(500,500);
		gImg=img.getGraphics();
		gImg.drawString("왼쪽버튼을 누른 채로 마우스를 움지경보세요", 10, 50);
		repaint();
		
	}
	
	public static void main(String[] args) {
	new CatchMind();	

	}

	
	public void paint(Graphics g){
		if(img==null)
			return;
		g.drawImage(img,0,0,p5);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	if(e.getModifiers()!=MouseEvent.BUTTON1_DOWN_MASK)return;
	System.out.println(e.getX() + ":" + e.getY());
	gImg.drawLine(x, y, e.getX(), e.getY());
	
	x=e.getX();
	y=e.getY();
	
	repaint();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x=e.getX();
		y=e.getY();
		
	}

}
