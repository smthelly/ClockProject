package com.example.davinciproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText userId, userPwd;
	Button loginBtn, joinBtn;
	static String uId;
	static String uSex;
	static String uNick;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		userId = (EditText) findViewById(R.id.Idfield);
		userPwd = (EditText) findViewById(R.id.Pwdfield);
		loginBtn = (Button) findViewById(R.id.Lbtn);
		joinBtn = (Button) findViewById(R.id.Jbtn);
		loginBtn.setOnClickListener(btnListener);
		joinBtn.setOnClickListener(btnListener);
	}
	public void setUserInfo(String uId, String uSex, String uNick){
		this.uId=uId;
		this.uSex = uSex;
		this.uNick=uNick;
	}
	class CustomTask extends AsyncTask<String, Void, String> {
		String sendMsg, receiveMsg;

		@Override
		protected String doInBackground(String... strings) {

		String URL = "http://192.168.0.5:8081/DBServer/JspServer/Login.jsp"; //자신의 웹서버 주소를 저장합니다.

			
			HttpClient client = new DefaultHttpClient();//HttpClient 통신을 합니다.
			sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&type="+strings[2];
			try {
				HttpPost post = new HttpPost(URL +"?"+ sendMsg);
				HttpResponse response = client.execute(post);

				BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
				String line = null;
				String result = "";
				Log.d("데이터전송", "전송 후");
				while ((line = bufreader.readLine()) != null) {
					result += line;
				}
				Log.d("데이터전송결과", result);
				String[][] parsedData = jsonParserList(result);
				receiveMsg = parsedData[0][0];
				Log.d("파싱결과", receiveMsg);
				//이 위에 뭔가 좀 쓸데없는 부분인거같은데 못고치겠슴 ^^하ㅏㅎㅎ
				//buffer를 읽어와서 result에 넣습니다.
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				client.getConnectionManager().shutdown();
			}
			return receiveMsg;
		}
	}

	View.OnClickListener btnListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.Lbtn : // 로그인 버튼 눌렀을 경우
				String loginid = userId.getText().toString();
				String loginpwd = userPwd.getText().toString();

				try {
					String result  = new CustomTask().execute(loginid,loginpwd,"login").get();
					String[][] parsedData = jsonParserList(result);
					result = parsedData[0][0];
					String sex=parsedData[0][1];
					String nick=parsedData[0][2];
					Log.d("결과물!!!", result);
					if(result.equals("true")) {
						setUserInfo(loginid, sex,nick);
						Toast.makeText(MainActivity.this,"'"+nick+"'님 환영합니다!",Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MainActivity.this, Lobbypage.class);
						startActivity(intent);
						finish();
					} else if(result.equals("false")) {
						Toast.makeText(MainActivity.this,"비밀번호가 틀렸음",Toast.LENGTH_SHORT).show();
						userId.setText("");
						userPwd.setText("");
					} else if(result.equals("noId")) {
						Toast.makeText(MainActivity.this,"존재하지 않는 아이디",Toast.LENGTH_SHORT).show();
						userId.setText("");
						userPwd.setText("");
					}
				}catch (Exception e) {}
				break;
			case R.id.Jbtn : // 회원가입
				Intent joinForm = new Intent(getApplicationContext(), Joinpage.class);
				startActivity(joinForm);
				break;
			}
		}
	};
	public String[][] jsonParserList(String pRecvServerPage){
		Log.i("서버에서 받은 전체 내용", pRecvServerPage);
		try{

			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("Login");

			String[] jsonName = {"msg1","msg2","msg3"};
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for(int i = 0; i<jArr.length();i++){
				json = jArr.getJSONObject(i);
				for (int j=0;j<jsonName.length; j++){
					parseredData[i][j] = json.getString(jsonName[j]);
				}
			}
			//확인
			Log.d("파싱[0][0]",parseredData[0][0]);//연결 여부
			Log.d("파싱[0][1]",parseredData[0][1]);//성별
			Log.d("파싱[0][2]",parseredData[0][2]);//닉네임
			return parseredData;
			//잘 파싱된 데이터를 넘깁니다.
		}catch (JSONException e){
			e.printStackTrace();
			return null;
		}

	}

}