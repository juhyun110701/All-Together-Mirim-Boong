package project.DB;

public class User {
	public String username;
	public int time;
	public int mapcount;
	
	public User() {}
	
	public User(String name, int time,int mapcount) {
		this.username = name;
		this.time = time;
		this.mapcount=mapcount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public int getMapcount() {
		return mapcount;
	}
	
	public void setMapcount(int mapcount) {
		this.mapcount = mapcount;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", time=" + time + ", mapcount=" + mapcount + "]";
	}
	

}
