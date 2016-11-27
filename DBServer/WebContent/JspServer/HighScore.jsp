<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="org.json.simple.*"
	import="database.HighScoreDB"%>
<%
	JSONObject jsonMain = new JSONObject();//객체
	JSONArray jArray = new JSONArray();//배열
	JSONObject jObject = new JSONObject();//Json 내용을 담을 객체
	System.out.println("HIghScore서버 동작");

	try {
		request.setCharacterEncoding("UTF-8");
		String type = request.getParameter("type");
		//싱글톤 방식으로 자바 클래스를 불러옵니다.
		HighScoreDB hs = HighScoreDB.getInstance();

		//if (type.equals("highscore")) {
			hs.getScore();
			for (int i = 0; i < hs.namelist.size(); i++) {
				System.out.println(hs.namelist.get(i));
			}
			if (hs.namelist.size() < 10) {//점수 있는사람이 10명 미만일떄
				jObject.put("nick", hs.namelist);
				jObject.put("level", hs.levellist);
				jObject.put("chips", hs.scorelist);

				jArray.add(jObject);

			} else {
				//10등까지 넘김
				jObject.put("nick", hs.namelist);
				jObject.put("level", hs.levellist);
				jObject.put("chips", hs.scorelist);
				jArray.add(jObject);

			}

		//}
		jsonMain.put("HighScore", jArray); //jason 제목 지정

		out.println(jsonMain.toJSONString());
	} catch (Exception e) {
	}
%>
