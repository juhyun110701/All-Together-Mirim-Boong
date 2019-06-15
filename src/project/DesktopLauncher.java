package project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.View;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import project.road.Finish;


public class DesktopLauncher extends ApplicationAdapter {
	public static int reply=0;
	class MyDialog extends JDialog {
		
		public MyDialog(){
			super();
			String media = "./bgm";
			Setting juke = new Setting(media);
	        juke.open();
	        getContentPane().add("Center", juke);
	        setPreferredSize(new Dimension(750,340));
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        setLocation(screenSize.width/2 - 750/2, screenSize.height/2 - 340/2);
	        MyDialog dialog = this;

		}
	}
	
   public static void main (String[] arg) {	   
	   SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new DesktopLauncher();			
			}
			
		});//invokeLater

	}//main
   
   public DesktopLauncher() {
       EventQueue.invokeLater(new Runnable() {
           @Override
           public void run() {
               try {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               } catch (Exception ex) {
               }

               try {
            	   // Load the background images
                   BufferedImage img = ImageIO.read(new File("img/game_start.jpg"));
                   ImageIcon img1 = new ImageIcon("img/finish_all.png");

                   // Create the frame...
                   JFrame frame = new JFrame("다함께 미림붕");
                   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                   frame.setContentPane(new JLabel(new ImageIcon(img)));

                   // Add stuff...
                   JButton setting = new JButton();//음향설정
                   setting.setBounds(325, 305, 170, 50);
                   setting.setContentAreaFilled(false);
                   setting.setFocusPainted(false);
                   setting.setBorderPainted(false);
                   frame.add(setting);
                   
                   JButton but = new JButton("");//게임 시작
                   but.setBounds(125, 305, 170, 50);
                   but.setContentAreaFilled(false);
                   but.setFocusPainted(false);
                   but.setBorderPainted(false);
                   frame.add(but);
                   
                   JButton but1 = new JButton("");//게임 방법
                   but1.setBounds(125, 360, 170, 50);
                   but1.setContentAreaFilled(false);
                   but1.setFocusPainted(false);
                   but1.setBorderPainted(false);
                   frame.add(but1);
                   
                   JButton but2 = new JButton("");//게임 전적
                   but2.setBounds(325, 360, 170,50);
                   but2.setContentAreaFilled(false);
                   but2.setFocusPainted(false);
                   but2.setBorderPainted(false);
                   frame.add(but2);
                   
                   
                   if(reply == 1) {
                	   new Thread(new Runnable() {       					
       					@Override
       					public void run() {
       						try {       						 
       						 JLabel end = new JLabel(img1);					 
       						 end.setBounds(400, 120, 400, 400);
       						 frame.pack();
       						 frame.add(end);
      	                     Thread.sleep(4000);
      	                     SpeedGame.dispose1();
       			
       						} catch (InterruptedException e) {
       							e.printStackTrace();
       						}
       				           
       					}
       				}).start();
                   }

                   frame.pack();
                   frame.setLocationRelativeTo(null);
                   frame.setVisible(true);
                   
                   MyDialog my = new MyDialog();
                   my.setModalityType(JDialog.DEFAULT_MODALITY_TYPE.APPLICATION_MODAL);
                    my.setVisible(false);

                   frame.pack();
                   frame.setLocationRelativeTo(null);
                   frame.setVisible(true);
                   
                   
                   setting.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
//						Setting set = new Setting("Mario.mid");
//						set.open();
//					      set.setSize(300, 200);
//					      set.setLocation(800,300);
//					      set.setVisible(true);
//					   
//					        Setting juke = new Setting("bgm/Mario.mid");
//					        juke.open();
//					        JFrame f = new JFrame("Juke Box");
//					        f.addWindowListener(new WindowAdapter() {
//					            public void windowClosing(WindowEvent e) {System.exit(0);}
//					            public void windowIconified(WindowEvent e) { 
//					                juke.credits.interrupt();
//					                //f.setVisible(false);
//					            }
//					        });
//					        f.getContentPane().add("Center", juke);
//					        f.pack();
//					        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//					        int w = 750;
//					        int h = 340;
//					        f.setLocation(screenSize.width/2 - w/2, screenSize.height/2 - h/2);
//					        f.setSize(w, h);
//					        f.setVisible(true);
					        
					        
//					        if (args.length > 0) {
//					            File file = new File(args[0]);
//					            if (file == null && !file.isDirectory()) {
//					                System.out.println("usage: java Juke audioDirectory");
//					            } 
//					        }
						
						my.pack();
						my.setVisible(true);
						//my.setModalityType(JDialog.DEFAULT_MODALITY_TYPE.APPLICATION_MODAL);
						//my.setVisible(false);
					}
				});
                   
                
	   				but.addActionListener( new ActionListener() {
	   					@Override
	   					public void actionPerformed(ActionEvent e) {	   						
	   						Select ch = new Select();	   						

	   						frame.setVisible(false);
	   					}
	   				});
	   				
	   				but1.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							GameWay way = new GameWay();
							frame.setVisible(false);
						}
	   					
	   				});
	   				
	   				but2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							GameHistory his = new GameHistory();
							frame.setVisible(false);
						}
	   					
	   				});
	   				
	   				
               } catch (IOException exp) {
                   exp.printStackTrace();
               }
           }
       });
   }
   
//   public  void volumeDown(View v){       
//
//	    AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//
//	    // 현재 볼륨 가져오기
//
//	    int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC); //volume은 0~15 사이어야 함
//
//	    // volume이 0보다 클 때만 키우기 동작
//
//	    if(volume > 0) {
//
//	        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume-1, AudioManager.FLAG_PLAY_SOUND);
//
//	    }else {
//
//	        Toast.makeText(getApplicationContext(), "현재 최저음량입니다.", Toast.LENGTH_SHORT).show();
//
//	    }
//
//	}


}
