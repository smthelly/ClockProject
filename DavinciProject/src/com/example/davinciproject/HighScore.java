package com.example.davinciproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.davinciproject.MainActivity.CustomTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class HighScore extends ActionBarActivity {
	TextView[] nick;
	TextView[] chip;
	ImageView[] level;
	public static ArrayList <Integer> scorelist = new ArrayList<Integer>();
	public static ArrayList <Integer> levellist = new ArrayList<Integer>();
	public static ArrayList <String> namelist = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_high_score);

		nick = new TextView[10];
		chip = new TextView[10];
		level = new ImageView[10];
		nick[0]=(TextView) findViewById(R.id.lank1);
		nick[1]=(TextView) findViewById(R.id.lank2);
		nick[2]=(TextView) findViewById(R.id.lank3);
		nick[3]=(TextView) findViewById(R.id.lank4);
		nick[4]=(TextView) findViewById(R.id.lank5);
		nick[5]=(TextView) findViewById(R.id.lank6);
		nick[6]=(TextView) findViewById(R.id.lank7);
		nick[7]=(TextView) findViewById(R.id.lank8);
		nick[8]=(TextView) findViewById(R.id.lank9);
		nick[9]=(TextView) findViewById(R.id.lank10);

		chip[0]=(TextView) findViewById(R.id.Chips1);
		chip[1]=(TextView) findViewById(R.id.Chips2);
		chip[2]=(TextView) findViewById(R.id.Chips3);
		chip[3]=(TextView) findViewById(R.id.Chips4);
		chip[4]=(TextView) findViewById(R.id.Chips5);
		chip[5]=(TextView) findViewById(R.id.Chips6);
		chip[6]=(TextView) findViewById(R.id.Chips7);
		chip[7]=(TextView) findViewById(R.id.Chips8);
		chip[8]=(TextView) findViewById(R.id.Chips9);
		chip[9]=(TextView) findViewById(R.id.Chips10);

		level[0]=(ImageView)findViewById(R.id.lvView1);
		level[1]=(ImageView)findViewById(R.id.lvView2);
		level[2]=(ImageView)findViewById(R.id.lvView3);
		level[3]=(ImageView)findViewById(R.id.lvView4);
		level[4]=(ImageView)findViewById(R.id.lvView5);
		level[5]=(ImageView)findViewById(R.id.lvView6);
		level[6]=(ImageView)findViewById(R.id.lvView7);
		level[7]=(ImageView)findViewById(R.id.lvView8);
		level[8]=(ImageView)findViewById(R.id.lvView9);
		level[9]=(ImageView)findViewById(R.id.lvView10);
		setHighScore();
	}
	public void setHighScore(){
		String result;
		try {
			result = new CustomTask().execute("highscore").get();
			setjsonParserList(result);
			
			//nick값설정
			for(int i=0; i<namelist.size(); i++){
					nick[i].setText("닉네임 : "+namelist.get(i));
			}
			//chip값 설정
			for(int i=0; i<scorelist.size(); i++){
				chip[i].setText("보유 칩 수 : "+scorelist.get(i));
			}
			
			for(int i=0; i<levellist.size(); i++){
				if(levellist.get(i)==3){//레벨 상
					level[i].setImageResource(R.drawable.lv_high);
				}else if(levellist.get(i)==2){//레벨 중
					level[i].setImageResource(R.drawable.lv_middle);
				}else{ //레벨 하
					level[i].setImageResource(R.drawable.lv_low);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class CustomTask extends AsyncTask<String, Void, String> {
		String sendMsg, receiveMsg;

		@Override
		protected String doInBackground(String... strings) {

			String URL = "http://192.168.0.5:8081/DBServer/JspServer/HighScore.jsp"; //자신의 웹서버 주소를 저장합니다.
			HttpClient client = new DefaultHttpClient();//HttpClient 통신을 합니다.
			sendMsg = "&type="+strings[0];
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
				receiveMsg = result;
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

	public void setjsonParserList(String pRecvServerPage){
		Log.i("서버에서 받은 전체 내용", "내용:"+pRecvServerPage);
		try{

			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("HighScore");
			String[] jsonName = {"nick","chips","level"};
			String[] parseredData = new String[jsonName.length];
			json = jArr.getJSONObject(0);
			for (int i=0;i<jsonName.length; i++){
				parseredData[i] = json.getString(jsonName[i]);
				Log.d("ㅇㅇㅇ1", "파싱이름"+i+parseredData[i]);
			}
			//[]안의 값들 풀기위해
			JSONArray name = new JSONArray(parseredData[0]);
			JSONArray chip = new JSONArray(parseredData[1]);
			JSONArray level = new JSONArray(parseredData[2]);

			//Arraylist에 넣어줌!!!!!
			namelist.clear();
			scorelist.clear();
			levellist.clear();
			for (int i = 0; i < name.length(); i++){
				namelist.add(name.getString(i)) ;
				scorelist.add(chip.getInt(i)) ;
				levellist.add(level.getInt(i)) ;
			}

			Log.d("ㅇㅇㅇ1", "파ㅏㅏ싱"+parseredData[0]);
			Log.d("ㅇㅇㅇ1", "파ㅏㅏ싱"+namelist.get(0));
			Log.d("ㅇㅇㅇ1", "파ㅏㅏ싱"+scorelist.get(0));
			//Log.d("ㅇㅇㅇ1", "파ㅏㅏ싱"+levellist.get(0));
			//확인
			//잘 파싱된 데이터를 넘깁니다.
		}catch (JSONException e){
			e.printStackTrace();

		}
		// 그리곤 SONArray를 받아서 바로 JSONObject로 형변환해서 쓰면.. 끝입니다. ^^;
	}

}
