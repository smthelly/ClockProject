package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HighScoreDB {
	private static HighScoreDB instance = new HighScoreDB();

	public static HighScoreDB getInstance() {
		return instance;
	}
	public HighScoreDB() {
		System.out.println("HighScore DB connect");
	}

	private String driver        = "org.mariadb.jdbc.Driver";
	private String jdbcUrl = "jdbc:mariadb://localhost:3306/davinci"; 
	private String dbId = "root";
	private String dbPw = "haeri"; // 비밀번호
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";


	public static ArrayList <Integer> scorelist = new ArrayList<Integer>();
	public static ArrayList <Integer> levellist = new ArrayList<Integer>();
	public static ArrayList <String> namelist = new ArrayList<String>();


	public void getScore(){
		namelist.clear();
		scorelist.clear();
		levellist.clear();
		Level lv = new Level();
		
		try{
			System.out.println("chip수에 따른 닉네임값!");
			Class.forName(driver);
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "Select joins.nick,score.chips from score inner join joins on score.id=joins.id order by 2 desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				String nick = rs.getString(1);
				int chip = rs.getInt(2);
				namelist.add(nick);
				scorelist.add(chip);
				levellist.add(lv.setLevel(chip));
			}
			conn.close();
			
			
		}catch(Exception e){

		}finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
	}
}
