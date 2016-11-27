<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="org.json.simple.*" %>

	<%
	Object[][] ex = {{"a",1},{"b",2},{"c",3}};
		JSONObject jsonMain = new JSONObject();//객체
		JSONArray jArray  = new JSONArray();//배열
		JSONObject jObject = new JSONObject();//Json 내용을 담을 객체
		try {
			request.setCharacterEncoding("UTF-8");
			String id = request.getParameter("id");
			//싱글톤 방식으로 자바 클래스를 불러옵니다.
			
			for(int i=0; i<ex.length;i++){
				//true / false / noId
				 jObject.put("nick", ex[i][0]);
				 jObject.put("chops", ex[i][1]);
				 jArray.add(jObject);
			}
			 jObject.put("nick", ex[0][0]);
			 jObject.put("chops", ex[0][1]);
			 jArray.add(jObject);
			jsonMain.put("TOTAL", ex.length);
			jsonMain.put("List", jArray); //jason 제목 지정

			out.println(jsonMain);
		} catch (Exception e) {
		}
	%>


