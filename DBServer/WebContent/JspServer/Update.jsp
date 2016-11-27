<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="org.json.simple.*"
	import="database.UserInfoDB"%>

<%
	JSONObject jsonMain = new JSONObject();//객체
	JSONArray jArray = new JSONArray();//배열
	JSONObject jObject = new JSONObject();//Json 내용을 담을 객체
	System.out.println("Update서버 동작");
	try {
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		String result = request.getParameter("result");
		String level = request.getParameter("level");
		//싱글톤 방식으로 자바 클래스를 불러옵니다.
		UserInfoDB Chipdb = UserInfoDB.getInstance();
		if (type.equals("finish")) {
			Chipdb.UpdateChip(id, level, result);
		}
	} catch (Exception e) {
	}
%>