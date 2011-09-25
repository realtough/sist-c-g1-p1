package com.sist.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserListForm extends JPanel implements ActionListener{	
	private JPanel jpListBoard = new JPanel();
	private JPanel jpButtonBoard = new JPanel();
	private JTable jtUserList;
	private DefaultTableModel dtModel;	
	private JComboBox<String> jcFindBy;	
	private JTextField jtfFindWord = new JTextField(15);
	private JButton jbSearch = new JButton("ã��");
	private JButton jbPrev = new JButton("������");
	private JButton jbNext = new JButton("������");
	JButton jbClose = new JButton("�ݱ�");	
	private int currentPage;
	private int totalPage;
	private JLabel jlPageNo = new JLabel(currentPage +" / " + totalPage, JLabel.CENTER);
	
	public UserListForm() {		
		setLayout(new BorderLayout());
		String col[] = {"���̵�", "��й�ȣ", "�̸�", "����", "����", "������", "�г���"};
		String row[][] = new String[0][7];
		dtModel = new DefaultTableModel(row, col){
			@Override
			public boolean isCellEditable(int row, int column) {			
				return false;
			}
		};			
		jtUserList = new JTable(dtModel);
		
		String com[] = {"���̵�", "�̸�", "�г���"};
		jcFindBy = new JComboBox<String>(com);
		
		jpButtonBoard.add(jcFindBy);
		jpButtonBoard.add(jtfFindWord);
		jpButtonBoard.add(jbSearch);
		jpButtonBoard.add(jbPrev);
		jpButtonBoard.add(jbNext);
		jpButtonBoard.add(jlPageNo);
		
		add("North", jbClose);
		add("Center", new JScrollPane(jtUserList));
		add("South", jpButtonBoard);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setSize(640, 500);
//		setVisible(true);
	}

	public void removeTable(){
		
	}
	
	public void addTable(ArrayList aList){
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if(obj==jbClose){
			
		}
	}	
}
