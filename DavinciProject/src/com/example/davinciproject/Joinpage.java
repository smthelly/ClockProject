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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Joinpage extends Activity {
	EditText Idform, Pwdform, Nickform;
	Button OKbtn;
	RadioButton Woman,Man;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_joinpage);
		Idform = (EditText) findViewById(R.id.Idform);//아이디입력칸을찾는다.
		Pwdform = (EditText) findViewById(R.id.Pwdform);//비번입력칸을찾는다.
		Nickform = (EditText) findViewById(R.id.Nickform);//닉네임입력칸찾는다.
		Woman = (RadioButton)findViewById(R.id.radioButtonW);//여자인지
		Man = (RadioButton)findViewById(R.id.radioButtonM);//남자인지
		OKbtn = (Button) findViewById(R.id.JOKbtn);
		OKbtn.setOnClickListener(btnListener);
		


	}

	class CustomTask extends AsyncTask<String, Void, String> {
		String sendMsg, receiveMsg;

		@Override
		protected String doInBackground(String... strings) {

			String URL = "http://192.168.0.5:8081/DBServer/JspServer/Join.jsp"; //자신의 웹서버 주소를 저장합니다.
			HttpClient client = new DefaultHttpClient();//HttpClient 통신을 합니다.
			sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&sex="+strings[2]+"&nick="+strings[3]+"&type="+strings[4];
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

			String id = Idform.getText().toString();
			String pwd = Pwdform.getText().toString();
			boolean woman = Woman.isChecked();
			boolean man = Man.isChecked();
			String sex ="w";
			String nick = Nickform.getText().toString();
			if(id.equals("")||pwd.equals("")||nick.equals("")||(woman==false&&man==false)){
				Toast.makeText(getApplicationContext(),"모든항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
			
			}else{
				
				if(man==true){
					sex = "m";
				}
				try {
					String result  = new CustomTask().execute(id,pwd,sex,nick,"join").get();
					String[][] parsedData = jsonParserList(result);
					result = parsedData[0][0];
					Log.d("결과물!!!", result);

					if(result.equals("id")) {
						Toast.makeText(Joinpage.this,"이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT).show();
						Idform.setText("");
						Pwdform.setText("");
						Nickform.setText("");
					} else if(result.equals("ok")) {
						Idform.setText("");
						Pwdform.setText("");
						Nickform.setText("");
						
						Toast.makeText(Joinpage.this,"회원가입을 축하합니다.",Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(Joinpage.this, MainActivity.class);
						startActivity(intent);
					}
				}catch (Exception e) {}
			}
		}
	};
	public String[][] jsonParserList(String pRecvServerPage){
		Log.i("서버에서 받은 전체 내용", pRecvServerPage);
		//받아온 데이터를 확인합니다. 이 부분을 확인하고 싶으시면, 안드로이드 하단에 쭉쭉 뭐 뜨는거 보이시면 그거 Log들인데 거기게 흔적을 남기는 겁니다. info에서 찾아보시면 찾으실 수 있습니다.
		try{
			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("Join");

			String[] jsonName = {"msg1","msg2"};
			String[][] parseredData = new String[jArr.length()][jsonName.length];
			for(int i = 0; i<jArr.length();i++){
				json = jArr.getJSONObject(i);
				for (int j=0;j<jsonName.length; j++){
					parseredData[i][j] = json.getString(jsonName[j]);
				}
			}
			Log.d("파싱?",parseredData[0][0]);
			return parseredData;
			//잘 파싱된 데이터를 넘깁니다.
		}catch (JSONException e){
			e.printStackTrace();
			return null;
		}

	}

}
