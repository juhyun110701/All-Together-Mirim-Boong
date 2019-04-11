package project.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class UserDAO {
	private static Connection conn = null;
	
	private static final String USER_TABLE_NAME = "usertable";
	
	private UserDAO() {}
	
	public static void init(Connection c) {
		conn = c;
	}
	
	public static User add(User user) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(String.format("INSERT INTO %s VALUES(?,?,?)", USER_TABLE_NAME));
		ps.setString(1, user.username);
		ps.setInt(2, user.time);
		ps.setInt(3, user.mapcount);
		int res = ps.executeUpdate();
		ps.close();
		
		return res == 1 ? user : null;
	}
	public static List<User> get(int mapcount) throws SQLException {
		List<User> lst = new ArrayList<>();
		PreparedStatement ps = conn.prepareStatement(String.format("SELECT * FROM %s WHERE mapcount = ? order by time asc", USER_TABLE_NAME));
		ps.setInt(1, mapcount);
		ResultSet rs = ps.executeQuery();
		int i=0;
		while(rs.next()) {
			if(i == 10) break;
			lst.add(new User(rs.getString("username"), rs.getInt("time"),rs.getInt("mapcount")));
			i++;
		}
		ps.close();
		rs.close();
		return lst;
	}
	
	public static List<User> getLists() throws SQLException {
		List<User> lst = new ArrayList<>();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(String.format("SELECT * FROM %s", USER_TABLE_NAME));
		while(rs.next()) {
			lst.add(new User(rs.getString("username"), rs.getInt("time"),rs.getInt("mapcount")));
		}
		s.close();
		rs.close();
		return lst;
	}
	
	public static boolean delete(User User) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(String.format("DELETE FROM %s WHERE username = ?", USER_TABLE_NAME));
		ps.setString(1, User.username);
		int res = ps.executeUpdate();
		ps.close();
		
		return res == 1 ? true : false;
	}

	
}
