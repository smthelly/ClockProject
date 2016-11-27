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

import com.example.davinciproject.MainActivity.CustomTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Lobbypage extends ActionBarActivity {
   TextView nickT,chipT,levelT;
   Button GameStart1,GameStart2,GameStart3,Intro,Score;
   ImageView ImgWM,levelView;
   static int chips ;
   public static int bettings =0;
   @Override
   protected void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.activity_lobbypage);

      nickT = (TextView) findViewById(R.id.UserNick);
      chipT=(TextView) findViewById(R.id.UserChip);
      levelT =(TextView) findViewById(R.id.UserLevel);
      GameStart1 = (Button) findViewById(R.id.GameStart1);
      GameStart2 = (Button) findViewById(R.id.GameStart2);
      GameStart3 = (Button) findViewById(R.id.GameStart3);
      ImgWM = (ImageView)findViewById(R.id.ImgWM);
      levelView=(ImageView)findViewById(R.id.levelView1);
      Score = (Button) findViewById(R.id.Score);
      Intro = (Button) findViewById(R.id.Intro);
      GameStart1.setOnClickListener(btnListener);
      GameStart2.setOnClickListener(btnListener);
      GameStart3.setOnClickListener(btnListener);
      Score.setOnClickListener(btnListener);
      Intro.setOnClickListener(btnListener);
      //성별에 따라 이미지 변환
      if(MainActivity.uSex.equals("w")){
         ImgWM.setImageResource(R.drawable.woman);
      }else{
         ImgWM.setImageResource(R.drawable.manicon);
      }
      //정보 보여주기
      nickT.setText("닉네임 : "+MainActivity.uNick);
      String result;
      int chip, al,level;
      try {
         result = new CustomTask().execute(MainActivity.uId,"info").get();
         int[][] parsedData = jsonParserList(result);
         chip = parsedData[0][1];
         al=parsedData[0][0];
         level=parsedData[0][2];
         setchips(chip,al);//칩 개수 저장
         Log.d("데이터전송", result);
         chipT.setText("보유 칩 개수 : "+chip);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (ExecutionException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      setLevel(chips);
   }
   public void setchips(int chips, int alim){

      this.chips=chips;
      if (alim==2){
         AlertDialog.Builder gift = new AlertDialog.Builder(this);
         gift.setTitle("선물을 드립니다.");
         gift.setMessage(MainActivity.uNick + "님! chip이 부족해 보이는군요. \n "
               + "선물로 chip 10개를 드리겠습니다^^");
         gift.setPositiveButton("확인",null);
         gift.show();
      }
      else if(alim==1){
         AlertDialog.Builder gift = new AlertDialog.Builder(this);
         gift.setTitle("Welcom to Davinci Code Game");
         gift.setMessage(MainActivity.uNick + "님! 환영합니다!! \n "
               + "첫 로그인 선물로 chip 30개를 드리겠습니다^^ 즐거운 시간되세용가리!");
         gift.setPositiveButton("확인",null);
         gift.show();
      }
   }
   public void setLevel(int level){
      if(level==3){//상
         levelView.setImageResource(R.drawable.lv_high);
      }else if(level==2)//중
         levelView.setImageResource(R.drawable.lv_middle);
      else{//하
         levelView.setImageResource(R.drawable.lv_low);
      }
   }
   class CustomTask extends AsyncTask<String, Void, String> {
      String sendMsg, receiveMsg;

      @Override
      protected String doInBackground(String... strings) {

         String URL = "http://192.168.0.5:8081/DBServer/JspServer/Useinfo.jsp"; //자신의 웹서버 주소를 저장합니다.
         HttpClient client = new DefaultHttpClient();//HttpClient 통신을 합니다.
         sendMsg = "id="+strings[0]+"&type="+strings[1];
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


   View.OnClickListener btnListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
         switch (view.getId()) {
         case R.id.GameStart1 : // 게임시작버튼
            Intent gamestart1 = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(gamestart1);
            bettings = 1;
            finish();
            break;
         case R.id.GameStart2 : // 게임시작버튼
            Intent gamestart2 = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(gamestart2);
            bettings = 5;
            finish();
            break;
         case R.id.GameStart3 : // 게임시작버튼
            Intent gamestart3 = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(gamestart3);
            bettings = 10;
            finish();
            break;
         case R.id.Score : // 랭킹보기
            Intent Ranking = new Intent(getApplicationContext(), HighScore.class);
            startActivity(Ranking);
            break;
         case R.id.Intro ://게임소개
            Intent Intro = new Intent(getApplicationContext(), GameIntro1.class);
            startActivity(Intro);
            break;
         }
      }
   };
   public int[][] jsonParserList(String pRecvServerPage){
      Log.i("서버에서 받은 전체 내용", pRecvServerPage);
      try{

         JSONObject json = new JSONObject(pRecvServerPage);
         JSONArray jArr = json.getJSONArray("Chips");

         String[] jsonName = {"msg1","msg2","msg3"};
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
   //종료시 인사
   public boolean onKeyDown(int KeyCode,KeyEvent event){
      switch(KeyCode){
      case KeyEvent.KEYCODE_BACK:
         new AlertDialog.Builder(this).setTitle("종료").setMessage("로그아웃하시겠습니까?").setPositiveButton("예",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
               finish();
            }
         }).setNegativeButton("아니오",null).show();
         return false;
      default : return false;
      }
   }
}