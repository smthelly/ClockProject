package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginJoin {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static LoginJoin instance = new LoginJoin();

	public static LoginJoin getInstance() {
		return instance;
	}

	public LoginJoin() {
		System.out.println("Login & JoinDB connect");
	}
	private String driver        = "org.mariadb.jdbc.Driver";
	private String jdbcUrl = "jdbc:mariadb://localhost:3306/davinci"; 
	private String dbId = "root";
	private String dbPw = "haeri"; // 비밀번호
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql2 = "";
	String returns = "";
	String returns2 = "";
	String returnSex ="";
	String returnNick = "";

	// 데이터베이스와 통신하기 위한 코드가 들어있는 메서드
	public String joindb(String id, String pwd, String sex, String nick) {
		try {
			System.out.println("joinDB");
			Class.forName(driver);
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select id from joins where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("id").equals(id)) { // 이미 아이디가 있는 경우
					returns = "id";
				} 
			} else { // 입력한 아이디가 없는 경우
				sql2 = "insert into joins values(?,?,?,?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, id);
				pstmt2.setString(2, pwd);
				pstmt2.setString(3, sex);
				pstmt2.setString(4, nick);
				pstmt2.executeUpdate();

				returns = "ok";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
		if (conn != null)try {conn.close();} catch (SQLException ex) {}
		if (pstmt2 != null)try {pstmt2.close();} catch (SQLException ex) {}
		if (rs != null)try {rs.close();} catch (SQLException ex) {}
		}
		System.out.println("회원가입 결과 : "+ returns);
		return returns;
	}

	public String logindb(String id, String pwd) {
		try {
			System.out.println("logignDB");
			Class.forName(driver);
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select * from joins where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				// 이미 아이디가 있는 경우 비밀번호검사
				if(rs.getString(1).equals(id) && rs.getString(2).equals(pwd)){
					returns2 = "true";
				}
				else {
					returns2 ="false";
				}
			}else{
				returns2 = "noId"; //아이디가 없는경우 
			}

		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
		if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
		if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		System.out.println("로그인 결과 : "+ returns2);
		return returns2;
	}
	public String getSex(String id){
		try{
			System.out.println("성별판별중");
			Class.forName(driver);
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select sex from joins where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				returnSex = rs.getString("sex");// 성별 가져오기
			}
		}catch(Exception e){

		}finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returnSex;
	}
	public String getNick(String id){
		try{
			System.out.println("닉네임 가져오기");
			Class.forName(driver);
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select nick from joins where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				returnNick = rs.getString("nick");// 성별 가져오기
			}
		}catch(Exception e){

		}finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returnNick;
	}

}

