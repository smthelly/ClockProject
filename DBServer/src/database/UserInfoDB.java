package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoDB {
   public   int level=0;
   private static UserInfoDB instance = new UserInfoDB();

   public static UserInfoDB getInstance() {
      return instance;
   }

   public UserInfoDB() {
      System.out.println("UserInfo DB connect");
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
   int returnChip = 0;
   int returnHow = 0;

   public void UpdateChip(String id, String num, String result){
      int chipN=0;
      int calcnum = Integer.parseInt(num);
      try{
         System.out.println("chip개수 업데이트하기");
         Class.forName(driver);
         conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
         sql = "select chips from score where id=?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         rs = pstmt.executeQuery();

         if (rs.next()) {
            returnChip = rs.getInt("chips");
            chipN=returnChip;
            sql2 = "update score set chips=? where id=?";
            pstmt2 = conn.prepareStatement(sql2);
            if(result.equals("win")){
               pstmt2.setInt(1,chipN+calcnum);
               pstmt2.setString(2, id);
               pstmt2.executeUpdate();

            }else if(result.equals("lose")){
               pstmt2.setInt(1, chipN-calcnum);
               pstmt2.setString(2,id);
               pstmt2.executeUpdate();
            }
         }
      }catch(Exception e){

      }finally {
         if (rs != null)try {rs.close();} catch (SQLException ex) {}
         if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
         if (conn != null)try {conn.close();} catch (SQLException ex) {}
      }
   }

   public int getChipHow(String id){
      try{
         System.out.println("chip 상태 먼저 체크!");
         Class.forName(driver);
         conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
         sql = "select chips from score where id=?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         rs = pstmt.executeQuery();
         if (rs.next()) {
            if (rs.getInt("chips")>=5){
               returnHow = 0;//일반 로그인
            }else{
               returnHow = 2;//선물
            }
         }else { 
            returnHow = 1;//첫 로그인
         }
      }catch(Exception e){

      }finally {
         if (rs != null)try {rs.close();} catch (SQLException ex) {}
         if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
         if (conn != null)try {conn.close();} catch (SQLException ex) {}
      }
      return returnHow;
   }

   public int getChip(String id){
      Level lv = new Level();
      int chipN=0;
      try{
         System.out.println("chip개수 가져오기");
         Class.forName(driver);
         conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
         sql = "select chips from score where id=?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         rs = pstmt.executeQuery();
         if (rs.next()) {
            if (rs.getInt("chips")>=5){
               returnChip = rs.getInt("chips");
               chipN=returnChip;
            }else{
               //칩이 적은 유저에게 구원의 선물 칩 주기
               sql2 = "update score set chips=? where id=?";
               pstmt2 = conn.prepareStatement(sql2);
               pstmt2.setInt(1,chipN+10);
               pstmt2.setString(2, id);
               pstmt2.executeUpdate();

               returnChip = chipN+10;
            }
         }else { // 입력한 아이디가 없는 경우 첫 로그인 임으로 chip 30개를 무료로 준다.
            sql2 = "insert into score values(?,?)";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setString(1, id);
            pstmt2.setInt(2, 30);
            pstmt2.executeUpdate();

            returnChip = 30;
         }
      }catch(Exception e){

      }finally {
         if (rs != null)try {rs.close();} catch (SQLException ex) {}
         if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
         if (conn != null)try {conn.close();} catch (SQLException ex) {}
      }
      level=lv.setLevel(level);
      return returnChip;
   }
}