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

//카트 선택하기
public class Select2 {
	public static int cart=0;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Select2();
			}
			
		});//invokeLater
	}
	
	public Select2() {
		EventQueue.invokeLater(new Runnable() {
	           @Override
	           public void run() {
	               try {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	               } catch (Exception ex) {
	               }

	               try {
	                   BufferedImage img = ImageIO.read(new File("img/cart_select.jpg"));

	                   JFrame frame = new JFrame("카트 선택");
	                   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	                    
	                   frame.setContentPane(new JLabel(new ImageIcon(img)));
	                   System.out.println(Select.Username);
	                   
	                   JButton back = new JButton("");
	                   back.setBounds(815, 65, 120, 70);
	                   back.setContentAreaFilled(false);
	                   back.setFocusPainted(false);
	                   back.setBorderPainted(false);
	                   frame.add(back);
	                   
	                   JButton but = new JButton("");//마우스
	                   but.setBounds(130, 310, 220, 160);
	                   but.setContentAreaFilled(false);
	                   but.setFocusPainted(false);
	                   but.setBorderPainted(false);
	                   frame.add(but);
	                   
	                   JButton but1 = new JButton("");  //의자
	                   but1.setBounds(430, 220, 180, 280);
	                   but1.setContentAreaFilled(false);
	                   but1.setFocusPainted(false);
	                   but1.setBorderPainted(false);
	                   frame.add(but1);
	                   
	                   JButton but2 = new JButton("");  //수레
	                   but2.setBounds(660, 220, 220, 280);
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
								Select ce = new Select();
								frame.setVisible(false);
							}
		                	   
		                   });
	                
		   				but.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {
		   						cart=1;
		   						Select3 s3 = new Select3();
		   						frame.setVisible(false);
		   					}
		   				});
		   				but1.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {
		   						cart=2;
		   						Select3 s3 = new Select3();
		   						frame.setVisible(false);
		   					}
		   				});
		   				but2.addActionListener( new ActionListener() {
		   					@Override
		   					public void actionPerformed(ActionEvent e) {
		   						cart=2;
		   						Select3 s3 = new Select3();
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
