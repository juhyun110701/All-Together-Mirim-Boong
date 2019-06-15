package project.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import project.GameScreen;
import project.Select;
import project.Select3;

public class data_add {
	public static Connection conn = null;
	
	public data_add() throws ClassNotFoundException{
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:sample.db");
			UserDAO.init(conn);
			  User u = new User(Select.Username,GameScreen.time,1);
		 	   if(Select3.map ==1) {  //밀림
		 		   u.setUsername(Select.Username);
		 		   u.setTime(GameScreen.time);
		 		   u.setMapcount(1);
		 	   }else if(Select3.map==4) {//사막
		 		  u.setUsername(Select.Username);
		 		   u.setTime(GameScreen.time);
		 		   u.setMapcount(4);
		 	   }else if(Select3.map==2) { //북극
		 		  u.setUsername(Select.Username);
		 		   u.setTime(GameScreen.time);
		 		   u.setMapcount(2);
		 	   }else {
		 		   JOptionPane.showMessageDialog(null, "값이 없습니다.");
		 	   }
		 	   System.out.println(u.getUsername()+" "+u.getTime()+" "+u.getMapcount());
		 	   
		 	   UserDAO.add(u);
		 	   
				conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//data_add()
	
	
	
}
