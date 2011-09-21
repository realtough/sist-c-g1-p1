package com.sist.client;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
public class CatchMind extends JFrame implements MouseMotionListener {

	JTextField answer,write,id,sex;
	JButton delete,alldelete,outbt;
	JTextPane chatting;
	JPanel board;
	
	int x=0;
	int y=0;
	Image img;
	Graphics gImg;
	
	public CatchMind(){
		answer=new JTextField();  //���� �ؽ�Ʈ �ʵ�
		write=new JTextField(30); //���� ���� �ؽ�Ʈ�ʵ�
		id=new JTextField(15); //���̵� 
		sex=new JTextField(15); //����
			
		delete=new JButton("���찳"); //���찳 ��ư
		alldelete=new JButton("��ü����");//��ü���� ��ư
		outbt=new JButton("������");  //������ ��ư	
		chatting=new JTextPane(); //ä���̳����� �г�
		board=new JPanel(); //�׸���
		
	
		JPanel p1=new JPanel();
		p1.add(delete);
		p1.add(alldelete);
		p1.add(write);
		
		JPanel p2=new JPanel();	
		p2.setLayout(new BorderLayout());
		p2.add("Center",board);
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
		
		img=createImage(625,530);
		gImg=img.getGraphics();
		gImg.drawString("���ʹ�ư�� ���� ä�� ���콺�� ������������.",10,30);
		repaint();	
		
		board.addMouseMotionListener(this);
			
	}
	public static void main(String[] args) {
		new CatchMind();	

		}
	
	public void paint (Graphics g){     //����Ʈ
		if(img==null)return;
		g.drawImage(img, 2, 30, board);
		board.setBackground(Color.yellow); 
	
	}

	@Override
	public void mouseDragged(MouseEvent me) {   //���콺 �巡�� �����ε�
		
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
