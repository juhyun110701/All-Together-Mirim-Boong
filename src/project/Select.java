package project;

import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import project.DB.User;
import project.DB.UserDAO;


//ĳ���� �����ϱ�
public class Select {
	public static String Username="";
	public static int ch=0;
	int cnt=0; 
	public Connection conn = null;
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Select();
			}
			
		});//invokeLater
	}
	
	public Select() {
		EventQueue.invokeLater(new Runnable() {
	           @Override
	           public void run() {
	               try {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	               } catch (Exception ex) {
	               }

	               try {
	                   BufferedImage img = ImageIO.read(new File("img/character_select.jpg"));

	                   JFrame frame = new JFrame("ĳ���� ����");
	                   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	                    
	                   frame.setContentPane(new JLabel(new ImageIcon(img)));
	                   
	                   JButton back = new JButton("");
	                   back.setBounds(815, 65, 120, 70);
	                   back.setContentAreaFilled(false);
	                   back.setFocusPainted(false);
	                   back.setBorderPainted(false);
	                   frame.add(back);

	                   JLabel namelabel = new JLabel("�̸� : ");
	                   namelabel.setFont(new Font("NanumSquare",0,18));
	                   namelabel.setBounds(480, 90, 250, 30);
	                   namelabel.setOpaque(false);
	                   namelabel.setForeground(Color.white);
	                   JTextField textField = new JTextField();	   
	                   textField.setBounds(480,120,150,50);
		   			   Button check = new Button("�����ϱ�"); 
		   			   check.setFont(new Font("NanumSquare",0,18));
		   			   check.setBounds(650,120,100,50);
		   			   check.setBackground(Color.gray);
		   			   check.setForeground(Color.white);
		   			   
		   			   if(Username.equals("")==false) {
		   				   cnt =0;
		   				   namelabel.setText(Username+"�� �ݰ����ϴ�.");
		   			   }
		   			   conn = DriverManager.getConnection("jdbc:sqlite:sample.db");
	       			   UserDAO.init(conn);
	                   List<User> users = UserDAO.getLists();
	                   
		   			   check.addActionListener(new ActionListener() {
		   					public void actionPerformed(ActionEvent e) {
		   						String ename=textField.getText();
		   						String name="";
		   					    for(User s : users) {	
		   					    	name = s.username;	   			    	
			   					    if(name.equals(ename)) {  //������ ��ϵǾ��ִ� �����̸��� ������ �ȵ�
			   					    	namelabel.setText("�ٽ��Է����ּ���");
			   					    	textField.setText("");
			   					    	cnt=1; 
			   					    	break;
			   					    }else {		
			   					    	cnt=0;
			   					    }		   					    
		   					    }//for
		   						if(cnt == 0) {
		   							Username = ename;
		   							namelabel.setText(Username+"�� �ݰ����ϴ�!");		
		   							cnt =2;
		   						}
		   						} 
		   					});
		   		    	frame.add(textField);
		   		    	frame.add(namelabel);
		   		    	frame.add(check);
	   				

	                   JButton but = new JButton("");  //���
	                   but.setBounds(270, 190, 160, 430);
	                   but.setContentAreaFilled(false);
	                   but.setFocusPainted(false);
	                   but.setBorderPainted(false);
	                   frame.add(but);
	                   
	                   JButton but1 = new JButton(""); //�ܹ�
	                   but1.setBounds(530, 190, 160, 430);
	                   but1.setContentAreaFilled(false);
	                   but1.setFocusPainted(false);
	                   but1.setBorderPainted(false);
	                   frame.add(but1);

	                   frame.pack();
	                   frame.setLocationRelativeTo(null);
	                   frame.setVisible(true);
	                
	                   back.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							DesktopLauncher ce = new DesktopLauncher();
							frame.setVisible(false);
						}
	                	   
	                   });
	                
		   				but.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {
		   						if(cnt == 2) {  //���� �̸��� ��ϵǾ����� ���� īƮ �������� �Ѿ
		   							ch=1;
		   							Select2 s2 = new Select2();
		   							frame.setVisible(false);
		   						}
		   						
		   					}
		   				});
		   				but1.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {
		   						if(cnt == 2) {
		   							ch=2;
		   							Select2 s2 = new Select2();
		   							frame.setVisible(false);
		   						}
		   					}
		   				});
	               } catch (IOException exp) {
	                   exp.printStackTrace();
	               } catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	           }
	       });
	}

}
