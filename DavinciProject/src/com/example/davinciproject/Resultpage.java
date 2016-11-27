package com.example.davinciproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.davinciproject.Lobbypage.CustomTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Resultpage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_resultpage);
		UpdateChip(GameActivity.GameLevel,GameActivity.Result);
		setResult();
	}
	public void UpdateChip(int num, String results){
		Integer chip = num;
		try {
			String result = new CustomTask().execute(MainActivity.uId,"finish",results,chip.toString()).get();
			int[][] parsedData = jsonParserList(result);
			Log.d("데이터전송", result);
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

			String URL = "http://192.168.0.5:8081/DBServer/JspServer/Update.jsp"; //자신의 웹서버 주소를 저장합니다.
			HttpClient client = new DefaultHttpClient();//HttpClient 통신을 합니다.
			sendMsg = "id="+strings[0]+"&type="+strings[1]+"&result="+strings[2]+"&level="+strings[3];
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
	public int[][] jsonParserList(String pRecvServerPage){
		Log.i("서버에서 받은 전체 내용", pRecvServerPage);
		try{

			JSONObject json = new JSONObject(pRecvServerPage);
			JSONArray jArr = json.getJSONArray("Chips");

			String[] jsonName = {"msg1","msg2"};
			int[][] parseredData = new int[jArr.length()][jsonName.length];
			for(int i = 0; i<jArr.length();i++){
				json = jArr.getJSONObject(i);
				for (int j=0;j<jsonName.length; j++){
					parseredData[i][j] = json.getInt(jsonName[j]);
				}
			}
			//확인
			return parseredData;
			//잘 파싱된 데이터를 넘깁니다.
		}catch (JSONException e){
			e.printStackTrace();
			return null;
		}
	}
	public void setResult(){

		String result = GameActivity.Result;
		if (result.equals("win")){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("승리");
			alert.setMessage(MainActivity.uNick + "님 축하합니다!\n "
					+ "chip"+ GameActivity.GameLevel + "개를 획득하셨습니다!");
			alert.setPositiveButton("확인",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					Intent Gohome = new Intent(getApplicationContext(), Lobbypage.class);
					startActivity(Gohome);
				}
			});
			alert.show();
		}else if (result.equals("lose")){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("패배");
			alert.setMessage(MainActivity.uNick + "님 아쉽네요 ㅠㅠ\n "
					+ "chip"+ GameActivity.GameLevel + "개를 잃으셨습니다!\n"
					+"다시 한 번 도전해보세요!");
			alert.setPositiveButton("확인",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					Intent Gohome = new Intent(getApplicationContext(), Lobbypage.class);
					startActivity(Gohome);
				}
			});
			alert.show();
		}
	}
}
