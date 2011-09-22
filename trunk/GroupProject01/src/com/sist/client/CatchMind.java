package com.sist.client;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
public class CatchMind extends JFrame  {

	PaintBoard pb = new PaintBoard();
	
	JTextField answer,write,id,sex;
	JButton delete,alldelete,outbt;
	JTextPane chatting;
	JPanel board;//그림판
	
	
	
	public CatchMind(){
		answer=new JTextField();  //정답 텍스트 필드
		write=new JTextField(30); //정답 쓰는 텍스트필드
		id=new JTextField(15); //아이디 
		sex=new JTextField(15); //성별
			
		delete=new JButton("지우개"); //지우개 버튼
		alldelete=new JButton("전체지움");//전체지움 버튼
		outbt=new JButton("나가기");  //나가기 버튼	
		chatting=new JTextPane(); //채팅이나오는 패널
		board=new JPanel(); //그림판
		
	
		JPanel p1=new JPanel();
		p1.add(delete);
		p1.add(alldelete);
		p1.add(write);
		
		JPanel p2=new JPanel();	
		p2.setLayout(new BorderLayout());
		p2.add("Center",pb);
		p2.add("South",p1);
		
		JPanel p3=new JPanel();
		p3.setLayout(new GridLayout(3,1));
		p3.add(id);
		p3.add(sex);
		p3.add(outbt);
		
		JPanel p4=new JPanel();
		p4.setLayout(new BorderLayout());
		p4.add("Center",chatting);
		p4.add("South",p3);
		
		
		add("Center",p2);
		add("East",p4);	
		
		
		setSize(800,600);
		setVisible(true);	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
			
		
		
			
	}
	public static void main(String[] args) {
		new CatchMind();	

		}
	
	
	class PaintBoard extends JPanel implements MouseMotionListener {
		int x=0;
		int y=0;
		Image img=null;
		Graphics gImg=null;
		
		public void PaintBoard(){
			
			img=createImage(625,530);
			gImg=img.getGraphics();
			gImg.drawString("왼쪽버튼을 누른 채로 마우스를 움직여보세요.",10,60);
			repaint();	
			addMouseMotionListener(this);
			
		}	
	
		 public void paint(Graphics g){
			  // super.paintComponent(g);   
			   g.drawOval(x, y, 10, 10);  
			   setBackground(Color.yellow);
			  }
		 
		/*public void paintComponent (Graphics g){     //패인트
		super.paintComponents(g);
		if(img!=null)		 
		g.drawImage(img, 0, 00,this);
		
		
	
	}*/

	@Override
	public void mouseDragged(MouseEvent me) {   //마우스 드래그 오버로딩
		if(me.getModifiersEx()!=MouseEvent.BUTTON1_DOWN_MASK)return;//마우스 왼쪽만 클릭 된다!
		gImg.drawLine(x, y, me.getX(), me.getY());
		x=me.getX();
		y=me.getY();
		repaint();
		
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		x=me.getX();
		y=me.getY();	
	}
	
}
}
