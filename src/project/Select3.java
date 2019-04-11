package project;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


//∏ º±≈√
public class Select3 {

	public static int map=0;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Select3();
			}
			
		});//invokeLater
	}

	public Select3() {
		EventQueue.invokeLater(new Runnable() {
	           @Override
	           public void run() {
	               try {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	               } catch (Exception ex) {
	               }

	               try {
	                   BufferedImage img = ImageIO.read(new File("img/game_map.png"));

	                   JFrame frame = new JFrame("∏  º±≈√");
	                   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	                    
	                   frame.setContentPane(new JLabel(new ImageIcon(img)));
	                   
	                   JButton back = new JButton("");
	                   back.setBounds(900, 40, 70, 80);
	                   back.setContentAreaFilled(false);
	                   back.setFocusPainted(false);
	                   back.setBorderPainted(false);
	                   frame.add(back);
	                   
	                   JButton but = new JButton("");  
	                   but.setBounds(60, 220, 280, 280);
	                   but.setContentAreaFilled(false);
	                   but.setFocusPainted(false);
	                   but.setBorderPainted(false);
	                   frame.add(but);
	                   
	                   JButton but1 = new JButton(""); 
	                   but1.setBounds(380, 220, 280, 280);
	                   but1.setContentAreaFilled(false);
	                   but1.setFocusPainted(false);
	                   but1.setBorderPainted(false);
	                   frame.add(but1);
	                   
	                   JButton but2 = new JButton(""); 
	                   but2.setBounds(680, 220, 280, 280);
	                   but2.setContentAreaFilled(false);
	                   but2.setFocusPainted(false);
	                   but2.setBorderPainted(false);
	                   frame.add(but2);

	                   frame.pack();
	                   frame.setLocationRelativeTo(null);
	                   frame.setVisible(true);
	                                   
	                   back.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								Select2 ce = new Select2();
								frame.setVisible(false);
							}
		                	   
		                   });
	                  
		   				but.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {
		   						map=1; //π–∏≤		   				
		   						Select4 s4 = new Select4();
		   						//app = new LwjglApplication(new SpeedGame(), config);		   							   						
		   						frame.setVisible(false);
		   					}
		   				});
		   				but1.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {
		   						map=4; //ªÁ∏∑		 
		   						Select4 s4 = new Select4();
		   						frame.setVisible(false);
		   					}
		   				});
		   				but2.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {
		   						map=2; //∫œ±ÿ		   			
		   						Select4 s4 = new Select4();		   						
		   						frame.setVisible(false);
		   					}
		   				});
		   				
	               } catch (IOException exp) {
	                   exp.printStackTrace();
	               }
	           }
	       });
	}

}
