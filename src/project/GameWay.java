package project;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class GameWay {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GameWay();
			}
			
		});//invokeLater

	}
	
	public GameWay() {
		EventQueue.invokeLater(new Runnable() {
	           @Override
	           public void run() {
	               try {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	               } catch (Exception ex) {
	               }

	               try {
	                   // Load the background image
	                   BufferedImage img = ImageIO.read(new File("img/game_instruction.jpg"));

	                   // Create the frame...
	                   JFrame frame = new JFrame("게임방법");
	                   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                    
	                   frame.setContentPane(new JLabel(new ImageIcon(img)));

	                   // Add stuff...
	                   JButton but = new JButton("");
	                   but.setBounds(815, 65, 120, 70);
	                   but.setContentAreaFilled(false);
	                   but.setFocusPainted(false);
	                   but.setBorderPainted(false);
	                   frame.add(but);

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
	               }
	           }
	       });
	}



}
