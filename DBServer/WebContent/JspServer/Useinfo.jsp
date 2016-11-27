<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8" import="org.json.simple.*"
   import="database.UserInfoDB"%>

<%
   JSONObject jsonMain = new JSONObject();//객체
   JSONArray jArray = new JSONArray();//배열
   JSONObject jObject = new JSONObject();//Json 내용을 담을 객체
   System.out.println("Chip서버 동작");
   try {
      request.setCharacterEncoding("UTF-8");
      String id = request.getParameter("id");
      String type = request.getParameter("type");
      //싱글톤 방식으로 자바 클래스를 불러옵니다.
      UserInfoDB Chipdb = UserInfoDB.getInstance();
      if (type.equals("info")) {
         int return1 = Chipdb.getChipHow(id);
         int return2 = Chipdb.getChip(id);
         int level=Chipdb.level;
         //true / false / noId
         System.out.println("DB에서 받아온 Chip 개수 : " + return2);

         jObject.put("msg1", return1);
         jObject.put("msg2", return2);
         jObject.put("msg3", level);
      }
      jArray.add(0, jObject);

      jsonMain.put("Chips", jArray); //jason 제목 지정

      out.println(jsonMain.toJSONString());
   } catch (Exception e) {
   }
%>
