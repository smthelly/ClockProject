<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="org.json.simple.*"
	import="database.LoginJoin"%>

<%
	JSONObject jsonMain = new JSONObject();//객체
	JSONArray jArray = new JSONArray();//배열
	JSONObject jObject = new JSONObject();//Json 내용을 담을 객체
	System.out.println("Login서버 시작");
	try {
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String type = request.getParameter("type");
		//싱글톤 방식으로 자바 클래스를 불러옵니다.
		LoginJoin LJdb = LoginJoin.getInstance();
		if (type.equals("login")) {
			String returns = LJdb.logindb(id, pwd);
			String returnSex = LJdb.getSex(id);
			String returnNick = LJdb.getNick(id);
			//true / false / noId
			System.out.println("DB에서 받아온login 결과 : " + returns + returnSex + returnNick);
			jObject.put("msg1", returns);
			jObject.put("msg2", returnSex);
			jObject.put("msg3", returnNick);
		}
		jArray.add(0, jObject);

		jsonMain.put("Login", jArray); //jason 제목 지정

		out.println(jsonMain.toJSONString());
	} catch (Exception e) {
	}
%>


