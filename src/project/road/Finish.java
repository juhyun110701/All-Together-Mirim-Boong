package project.road;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import project.DesktopLauncher;
import project.GameScreen;
import project.GameWay;
import project.Select;
import project.Select3;
import project.Select4;
import project.SpeedGame;
import project.DB.User;
import project.DB.UserDAO;
import project.DB.data_add;
import project.road.improad.SimpleRoad;


class WindowHandler extends WindowAdapter {
	public void windowClosing(WindowEvent e) {
		SpeedGame.dispose1();
	}
}
public class Finish {
	public static LwjglApplicationConfiguration config1 = new LwjglApplicationConfiguration();
	public static LwjglApplication app1;
	public static int appCheck = 0;
	public static void main (String[] arg) throws SQLException {	   		
		   SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					//new BGM();
					new DesktopLauncher();	
				}
				
			});//invokeLater

		}//main
	   
	   public Finish() {
	       EventQueue.invokeLater(new Runnable() {
	           @Override
	           public void run() {
	               try {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	               } catch (Exception ex) {
	               }

	               try {
	                   // Load the background image
	                   BufferedImage img = ImageIO.read(new File("img/game_finish.png"));

	                   // Create the frame...
	                   JFrame frame = new JFrame("다함께 미림붕");
	                   frame.addWindowListener(new WindowHandler());
	                   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	                    
	                   frame.setContentPane(new JLabel(new ImageIcon(img)));
	                   frame.pack();
	                   frame.setLocationRelativeTo(null);
	                   frame.setVisible(true);
	                   new Thread(new Runnable() {       					
	       					@Override
	       					public void run() {
	       						try {
									Thread.sleep(3000);
									int reply = JOptionPane.showConfirmDialog(null, "데이터를 저장하시겠습니까??", "데이터 저장", JOptionPane.YES_NO_OPTION);
									   if (reply == JOptionPane.YES_OPTION) {
									    	   try {
												data_add ad = new data_add();
												 JOptionPane.showMessageDialog(null, "안녕히 가세요~");
											} catch (ClassNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}		               	                 	   
									    	   
									    	   System.exit(0);
									   }
									   else {
//										   int rep = JOptionPane.showConfirmDialog(null, "다시 시작하겠습니까??", "게임 다시 시작", JOptionPane.YES_NO_OPTION);
//										   if(rep == JOptionPane.YES_OPTION) {
//											   Select4.app.exit();
//											  
//											   appCheck =1;
//					                		   config1.title = "달려라 마리오 재시작";
//					        				   config1.width = 1024;  //1024
//					        				   config1.height = 768;	    
//					        				   config1.forceExit = false;
//					                		   GameScreen.i=0; GameScreen.time=0;	                	
//					                		   app1 = new LwjglApplication(new SpeedGame(), config1);	
//					                		   frame.setVisible(false);
//					                		   Select3 re3 = new Select3();
//											   
//										   }else {										   
//											   System.exit(0);
//										   }
									      JOptionPane.showMessageDialog(null, "안녕히 가세요~");
									      System.exit(0);
									   }
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	       				           
	       					}
	       				}).start();
	                   		   				
		   				
	               } catch (IOException exp) {
	                   exp.printStackTrace();
	               } 
			
	             
	           }
	       });
	   }
}



	
