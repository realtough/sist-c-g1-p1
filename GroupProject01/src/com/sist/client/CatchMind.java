package com.sist.client;
import javax.security.auth.callback.LanguageCallback;
import javax.swing.*;

import com.sist.common.G1Server;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
public class CatchMind extends JFrame implements ActionListener {

	PaintBoard pb = new PaintBoard();
	
	JTextField answer2,write,id,sex;
	JButton alldelete,outbt;
	JLabel answer;
	JTextPane chatting;
	JPanel board;//�׸���
	String realanswer=new String();
	
	 
	
	
	public CatchMind(){
		answer2=new JTextField(10);
		answer=new JLabel("����", JLabel.LEFT);  //���� �ؽ�Ʈ �ʵ�
		write=new JTextField(30); //���� ���� �ؽ�Ʈ�ʵ�
		id=new JTextField(15); //���̵� 
		sex=new JTextField(15); //����
			
		
		alldelete=new JButton("��ü����");//��ü���� ��ư
		outbt=new JButton("������");  //������ ��ư	
		chatting=new JTextPane(); //ä���̳����� �г�
		board=new JPanel(); //�׸���
		
	
		
		JPanel p1=new JPanel();
//		p1.setLayout(null);
//		answer.setBounds(0, 20, 50, 20);
//		alldelete.setBounds(70, 20, 50, 20);
//		write.setBounds(130, 20, 50, 20);
		
		p1.add(answer);
		p1.add(answer2);
		p1.add(alldelete);
		p1.add(write);
		
		JPanel p2=new JPanel();	
		p2.setLayout(new BorderLayout());
	//	p2.setOpaque(false);
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
		
		alldelete.addActionListener(this);
		answer2.addActionListener(this);
		outbt.addActionListener(this);
		
			
			
			
	}
	public static void main(String[] args) {
		new CatchMind();	

		}
	
	
	class PaintBoard extends JPanel implements MouseMotionListener {
		
		int x=0;
		int y=0;
		BufferedImage image = new BufferedImage(625, 530,2);	
		Graphics2D imageG = image.createGraphics(); 
		
		public void painting(){
			imageG.setColor(Color.yellow);//�۾���		
			imageG.fillRect(0, 0, 625, 530);		
			repaint();
			imageG.setColor(Color.blue);
		}
		
		
		public PaintBoard(){
			imageG.drawString("���ʹ�ư�� ���� ä�� ���콺�� ������������.",10,60);
			painting();
			addMouseMotionListener(this);
			
		}	
	
		 public void paintComponent(Graphics g){
			   super.paintComponent(g);   
			   Graphics2D tempg = (Graphics2D)g;			   
			   tempg.drawImage(image, 0, 0, this);
			  			   
			  }
		 
		/*public void paintComponent (Graphics g){     //����Ʈ
		super.paintComponents(g);
		if(img!=null)		 
		g.drawImage(img, 0, 00,this);
		
		
	
	}*/

	@Override
	public void mouseDragged(MouseEvent me) {   //���콺 �巡�� �����ε�
		if(me.getModifiersEx()!=MouseEvent.BUTTON1_DOWN_MASK)return;//���콺 ���ʸ� Ŭ�� �ȴ�!
		imageG.drawLine(x, y, me.getX(), me.getY());
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==alldelete){
			pb.painting();	
		}
		else if(e.getSource()==answer2){
			answer2.setEditable(false);
			realanswer=answer2.getText().trim();
			if(realanswer.length()==0){
				JOptionPane.showMessageDialog(this,"������ �Է��ϼ���");
				answer2.setEditable(true);
				
			}else
				answer2.setEditable(false);
			answer2.setFocusable(false);			
		}
		if(e.getSource()==outbt){    //���� �г� �Ѱܹ���
			
		}
	}
}
