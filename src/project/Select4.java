package project;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

import project.road.Finish;

public class Select4 {
	public static LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	public static LwjglApplicationConfiguration config1 = new LwjglApplicationConfiguration();
	public static LwjglApplication app,app1;

	public static int map=0;
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Select4();
			}
		});//invokeLater
	}

	public Select4() {
		EventQueue.invokeLater(new Runnable() {
	           @Override
	           public void run() {
	               try {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	               } catch (Exception ex) {
	               }

	               JFrame frame = new JFrame("맵 선택");
				   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	  
				   frame.setBackground(Color.BLACK);
          
				   new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							JLabel countdown3 = new JLabel("3");
							countdown3.setFont(new Font("NanumSquare",Font.BOLD,200));
							countdown3.setHorizontalAlignment(SwingConstants.CENTER);
							frame.add(countdown3);
							frame.pack();
							Thread.sleep(1000);
							
							countdown3.setVisible(false);
							JLabel countdown2 = new JLabel("2");
							countdown2.setFont(new Font("NanumSquare",Font.BOLD,200));
							countdown2.setHorizontalAlignment(SwingConstants.CENTER);
							frame.add(countdown2);
							frame.pack();
							Thread.sleep(1000);					
							
							countdown2.setVisible(false);
							JLabel countdown1 = new JLabel("1");
							countdown1.setFont(new Font("NanumSquare",Font.BOLD,200));
							countdown1.setHorizontalAlignment(SwingConstants.CENTER);
							frame.add(countdown1);
							frame.pack();
							
							 config.title = "다함께 미림붕";
							 config.width = 1024;
							 config.height = 768;	    
							 config.forceExit = false;
							 
							 config1.title = "다함께 미림붕 재시작";
							 config1.width = 1024;
							 config1.height = 768;	    
							 config1.forceExit = false;
							 
							 if(Finish.appCheck == 1) app1 = new LwjglApplication(new SpeedGame(),config1);
							 else app = new LwjglApplication(new SpeedGame(), config);
							frame.setVisible(false);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				           
					}
				}).start();
				   //frame.setVisible(true);
				  
       
				   frame.setPreferredSize(new Dimension(1024,768));
				   frame.setVisible(true);
				   frame.setLocation(450,150);
	           }
	       });
	}

}
