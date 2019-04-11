package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import project.DB.User;
import project.DB.UserDAO;

public class GameHistory {
	public Connection conn = null;
	int i=0;
	public static void main(String[] args) {		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GameHistory();
			}
			
		});//invokeLater

	}
	
	public GameHistory() {
		EventQueue.invokeLater(new Runnable() {
	           @Override
	           public void run() {
	               try {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	               } catch (Exception ex) {
	               }

	               try {
	                   // Load the background image
	                   BufferedImage img = ImageIO.read(new File("img/game_history.jpg"));

	                   // Create the frame...
	                   JFrame frame = new JFrame("게임전적");
	                   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	                    
	                   frame.setContentPane(new JLabel(new ImageIcon(img)));
	                   
	                   JButton but = new JButton("");
	                   but.setBounds(870, 70, 60, 60);
	                   but.setContentAreaFilled(false);
	                   but.setFocusPainted(false);
	                   but.setBorderPainted(false);
	                   frame.add(but);
	                    
	                   JLabel view1 = new JLabel();	view1.setFont(new Font("NanumSquare",0, 18));
	                   JLabel view2 = new JLabel(); view2.setFont(new Font("NanumSquare",0, 18));
	                   JLabel view3 = new JLabel(); view3.setFont(new Font("NanumSquare", Font.BOLD, 20));
	                   view1.setOpaque(false);  view2.setOpaque(false); view3.setOpaque(false);
	                   view1.setBounds(90, 250, 150, 350);     
	                   view2.setBounds(240, 250, 100, 350);
	                   view3.setBounds(100, 200, 240, 40);
	                   view1.setHorizontalAlignment(SwingConstants.CENTER); view1.setVerticalAlignment(SwingConstants.TOP);                
	                   view2.setHorizontalAlignment(SwingConstants.CENTER); view2.setVerticalAlignment(SwingConstants.TOP);
	                   view3.setHorizontalAlignment(SwingConstants.CENTER); 
	                   view1.getBackground();
	                   view1.setForeground(Color.WHITE); view2.setForeground(Color.WHITE); view3.setForeground(Color.WHITE);
	                   
	                   JLabel view4 = new JLabel();	view4.setFont(new Font("NanumSquare",0, 18));
	                   JLabel view5 = new JLabel(); view5.setFont(new Font("NanumSquare",0, 18));
	                   JLabel view6 = new JLabel(); view6.setFont(new Font("NanumSquare", Font.BOLD, 20));
	                   view4.setOpaque(false);  view5.setOpaque(false); view6.setOpaque(false);
	                   view4.setBounds(380,250,150,350);
	                   view5.setBounds(540,250,100,350);
	                   view6.setBounds(400,200,240,40);	                   
	                   view4.setHorizontalAlignment(SwingConstants.CENTER); view4.setVerticalAlignment(SwingConstants.TOP);
	                   view5.setHorizontalAlignment(SwingConstants.CENTER); view5.setVerticalAlignment(SwingConstants.TOP);
	                   view6.setHorizontalAlignment(SwingConstants.CENTER);
	                   view4.setForeground(Color.WHITE); view5.setForeground(Color.WHITE); view6.setForeground(Color.WHITE);
	                   
	                   JLabel view7 = new JLabel();	view7.setFont(new Font("NanumSquare",0, 18));
	                   JLabel view8 = new JLabel(); view8.setFont(new Font("NanumSquare",0, 18));
	                   JLabel view9 = new JLabel(); view9.setFont(new Font("NanumSquare", Font.BOLD, 20));
	                   view7.setOpaque(false);  view8.setOpaque(false); view9.setOpaque(false);
	                   view7.setBounds(680,250,150,350);  
	                   view8.setBounds(840,250,100,350);
	                   view9.setBounds(700,200,240,40);	                   
	                   view7.setHorizontalAlignment(SwingConstants.CENTER); view7.setVerticalAlignment(SwingConstants.TOP);
	                   view8.setHorizontalAlignment(SwingConstants.CENTER); view8.setVerticalAlignment(SwingConstants.TOP);
	                   view9.setHorizontalAlignment(SwingConstants.CENTER);
	                   view7.setForeground(Color.WHITE); view8.setForeground(Color.WHITE); view9.setForeground(Color.WHITE);
	                   
	                   conn = DriverManager.getConnection("jdbc:sqlite:sample.db");
	       			   UserDAO.init(conn);
	       			   
	                   List<User> users = UserDAO.get(1);       
	                   String jname="<html>",jtime="<html>";
		               for(User s : users) {		           
		            	   i++;
		            	   jname += i+". "+s.getUsername()+"<br>";
		   				   jtime += s.getTime()+" 초<br>";		   				 
		   			   }                  
		               jname+="</html>"; jtime +="</html>"; view1.setText(jname); view2.setText(jtime); view3.setText("운동장맵");
		               
		               List<User> user1 = UserDAO.get(4);       
	                   String dname="<html>",dtime="<html>"; i=0;
		               for(User s : user1) {		
		            	   i++;
		   				 dname += i+". "+s.getUsername()+"<br>";
		   				 dtime += s.getTime()+" 초<br>";		   				 
		   			   }                  
		               dname+="</html>"; dtime+="</html>";  view4.setText(dname); view5.setText(dtime); view6.setText("정원맵");
		               
		               List<User> user2 = UserDAO.get(2);       
	                   String nname="<html>",ntime="<html>"; i=0;
		               for(User s : user2) {	
		            	   i++;
		   				 nname +=i+". "+ s.getUsername()+"<br>";
		   				 ntime+=s.getTime()+" 초<br>";		   				 
		   			   }                  
		               nname+="</html>";  ntime+="</html>"; view7.setText(nname); view8.setText(ntime); view9.setText("강당맵");
		               
		               
	                   frame.add(view1);  frame.add(view2); frame.add(view3);
	                   frame.add(view4);  frame.add(view5); frame.add(view6);
	                   frame.add(view7);  frame.add(view8); frame.add(view9);
	                   frame.pack();
	                   frame.setLocationRelativeTo(null);
	                   frame.setVisible(true);
	                   
	                
		   				but.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {		   						
		   						DesktopLauncher de = new DesktopLauncher();
		   						frame.setVisible(false);
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
	}//gamehistory

	
}
