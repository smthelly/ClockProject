package com.example.davinciproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import Playgame.*;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class GameActivity extends Activity {
   public static String Result ="None"; //게임결과
   public static String CardState ="HAS";//가져가야할 카드 상태
   public static int GameLevel = Lobbypage.bettings; 
   Card WhiteCardNum;
   Card BlackCardNum;//전체 카드 수
   HashMap <Integer,String> IsBlackOpen;//검정카드의 오픈여부를 알려줌
   HashMap <Integer,String> IsWhiteOpen;//하얀카드의 오픈여부를 알려줌.
   static CardInfo Spreadcard; //무슨 카든지 고를 떄 쓰임
   static Player Me, Com;//나랑 컴퓨터가 있음
   static Computer computer;// 인공지능? 생성
   static int Turn;// 턴 지정!!!
   //핸들러에 사용 할 메세지
   public static final int SEND_CARD_COUNT = 0; //게임 시작 전 가져올 각각의 카드 수 표시
   public static final int SEND_STOP=1;//위 동작을 위해 작동시킨 스레드 종료
   public static final int SEND_CARD_COUNT2 =3;//남은 블랙카드와 화이트 카드 개수 표시
   public static final int SEND_COM_CARD_SET =4;//?
   Buff buff;//뽑기 이전카드 넣어놓을 버퍼
   int Bready;//시작 전 세팅 할 카드 수
   int Wready;

   //버퍼1- 추리한 카드 정보
   int CheckCardNum;//추리한 카드 값
   boolean selectState;//추리할 카드 선택 여부
   View selectedCard;//선택한 카드
   int selectedColor;//선택한 카드가 블랙이면 0
   //버퍼 2 -가져온 카드 정보 
   View addCardView;//선택한 카드
   int addCardNum;
   int addCardColor;

   Thread thread; //핸들러1을 위한 스레드
   Thread2 thread2;//핸들러 3을 위한 스래드

   ImageButton BlackCard,WhiteCard;//가져오는 카드 선택 할 버튼
   Button Bup,Wup,readyBtn,selectCardOK; //게임 시작 전 가져올 카드 수 조정, 준비완료, 추리할 카드 선택 완료 버튼
   Button num0,num1,num2,num3,num4,num5,num6,num7,num8,num9,num10,num11; //추리할 카드의 추리한 숫자를 보내줄 변수
   TextView B_count, W_count, bn,wn, a,b,c;

   static LinearLayout mycardlayout;//내 카드가 놓일 곳
   LinearLayout Firstcardlayout;//상대 카드가 놓일곳

   GridLayout GameStartView;//게임 시작 전 카드 선택 화면
   ViewFlipper flip;//게임 흐름에 때라 화면을 바꿔줌

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //전체화면
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.activity_game);
      IsBlackOpen= new HashMap<Integer,String>();
      IsWhiteOpen= new HashMap<Integer,String>();
      WhiteCardNum= new Card();//전체 카드 생성
      BlackCardNum =new Card();
      Me = new Player();//내 카드
      Com = new Player();//컴퓨터 카드
      computer = new Computer();
      CardState ="HAS";
      Turn=0; ///0이면 나 --> 다음에 3인 이상 모드에서 다른사람이면 순서대로 하게 할수있을듯!

      Spreadcard = new CardInfo(); //무슨 카든지 정할떄 씀
      buff= new Buff();// 어가져온카드를 어디에 넣어야 할지 비교 할 떄 쓰임

      //총 4개
      Bready=2;//시작 전 가져올 블랙 카드
      Wready=2;//시작 전 가져올 화이트 카드

      //비교를 위한 전역변수 값 (버퍼)
      selectState=false;
      CheckCardNum=0;//추리할 카드 값
      selectedCard=null; //카드 선택 없음으로 초기화
      selectedColor=0;//블랙으로 초기화

      //버퍼 2 - 가져온 카드 정보 초기화 
      addCardView=null;
      addCardNum= 0;
      addCardColor=0;

      thread = new Thread();
      thread2 = new Thread2();
      thread.setDaemon(true);//메인 스레드 종료시 함꼐 종료 설정
      thread2.setDaemon(true);

      //스레드1 시작
      thread.start();

      //화면에서 동작할 뷰 등록
      Bup=(Button)findViewById(R.id.BPlus);
      Wup=(Button)findViewById(R.id.WPlus);
      B_count=(TextView)findViewById(R.id.BlackN);
      W_count=(TextView)findViewById(R.id.WhiteN);
      bn=(TextView)findViewById(R.id.BNum);
      wn=(TextView)findViewById(R.id.WNum);
      a=(TextView)findViewById(R.id.atextView1);
      b=(TextView)findViewById(R.id.atextView2);
      c=(TextView)findViewById(R.id.RedyState);
      readyBtn=(Button)findViewById(R.id.ReadyOK);
      BlackCard= (ImageButton) findViewById(R.id.BlackCardBtn);
      WhiteCard=(ImageButton) findViewById(R.id.WhiteCardBtn);
      readyBtn.setOnClickListener(btnListener);
      Bup.setOnClickListener(btnListener);
      Wup.setOnClickListener(btnListener);
      mycardlayout =(LinearLayout)findViewById(R.id.MyCardLayout);
      Firstcardlayout =(LinearLayout)findViewById(R.id.Cardlayout1);
      flip= (ViewFlipper) findViewById(R.id.viewFlipper1);
      selectCardOK= (Button) findViewById((R.id.selectCardOK1));
      selectCardOK.setOnClickListener(btnListener);
      GameStartView = (GridLayout)findViewById(R.id.GameStart);

      num0=(Button)findViewById(R.id.num0);
      num1=(Button)findViewById(R.id.num1);
      num2=(Button)findViewById(R.id.num2);
      num3=(Button)findViewById(R.id.num3);
      num4=(Button)findViewById(R.id.num4);
      num5=(Button)findViewById(R.id.num5);
      num6=(Button)findViewById(R.id.num6);
      num7=(Button)findViewById(R.id.num7);
      num8=(Button)findViewById(R.id.num8);
      num9=(Button)findViewById(R.id.num9);
      num10=(Button)findViewById(R.id.num10);
      num11=(Button)findViewById(R.id.num11);

      num0.setOnClickListener(btnListener);
      num1.setOnClickListener(btnListener);
      num2.setOnClickListener(btnListener);
      num3.setOnClickListener(btnListener);
      num4.setOnClickListener(btnListener);
      num5.setOnClickListener(btnListener);
      num6.setOnClickListener(btnListener);
      num7.setOnClickListener(btnListener);
      num8.setOnClickListener(btnListener);
      num9.setOnClickListener(btnListener);
      num10.setOnClickListener(btnListener);
      num11.setOnClickListener(btnListener);
      //초기화
      for(int i=0; i<12; i++){
         IsBlackOpen.put(i, "f");
         IsWhiteOpen.put(i, "f");
      }
   }
   View.OnClickListener btnListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
         switch (view.getId()) {
         //게임 준비시 
         case R.id.BPlus://검은카드 +
            setBCard();
            break;
         case R.id.WPlus://화이트카드+
            setWCard();
            break;
         case R.id.ReadyOK://게임 준비 완료
            //스레드1 종료, 스레드 2 시작   
            BlackCard.setOnClickListener(btnListener);
            WhiteCard.setOnClickListener(btnListener);
            handler.sendEmptyMessage(SEND_STOP);
            thread2.start();
            B_count.setVisibility(View.VISIBLE);//블랙카드 총 개수 보이게 하기
            W_count.setVisibility(View.VISIBLE);//화이트 카드 총 개수 보이게 하기
            B_count.setVisibility(View.VISIBLE);//블랙카드 총 개수 보이게 하기
            W_count.setVisibility(View.VISIBLE);//화이트 카드 총 개수 보이게 하기
            bn.setVisibility(View.INVISIBLE);
            wn.setVisibility(View.INVISIBLE);
            a.setVisibility(View.INVISIBLE);
            b.setVisibility(View.INVISIBLE);
            c.setVisibility(View.INVISIBLE);
            Bup.setVisibility(View.INVISIBLE);
            Wup.setVisibility(View.INVISIBLE);
            readyBtn.setVisibility(View.INVISIBLE);

            setPlay();//시작 전 선택한 카드에 숫자 부여
            ReadyCard();//각 숫자와 색깔에 맞는 카드 불러와서 세팅

            break;
            //            

            //게임 시작 후
         case R.id.BlackCardBtn : // 블랙카드선택

            if(Turn==0){//내 턴일 때
               buff.setBuff(Me);
               addCard(Me,BlackCardNum,"b");
               GameStartView.setVisibility(View.INVISIBLE);
               flip.setVisibility(View.VISIBLE);
               flip.showNext();
            }
            else{
               Toast.makeText(GameActivity.this,"상대가 플레이 중입니다.",Toast.LENGTH_SHORT).show();
            }
            int num;
            for(int i=0; i < Com.number.size(); i++ ){
               num = Com.number.get(i);
               if(Com.color.get(i).equals("b")){//블랙이면
                  if(IsBlackOpen.get(num).equals("f")){
                     Firstcardlayout.getChildAt(i).setOnClickListener(CardListener);
                  }
               }else{//화이트이면
                  if(IsWhiteOpen.get(num).equals("f")){
                     Firstcardlayout.getChildAt(i).setOnClickListener(CardListener);
                  }//f인 카드리스너만 
               }
            }
            break;
         case R.id.WhiteCardBtn : // 화이트카드선택
            if(Turn==0){//내 턴일 때
               buff.setBuff(Me);

               addCard(Me,WhiteCardNum,"w");
               GameStartView.setVisibility(View.INVISIBLE);
               flip.setVisibility(View.VISIBLE);
               flip.showNext();

            }
            else{
               Toast.makeText(GameActivity.this,"상대가 플레이 중입니다.",Toast.LENGTH_SHORT).show();
               /*
               buff.setBuff(Computer);
               addCard(Computer,WhiteCardNum,"b");
               GameStartView.setVisibility(View.INVISIBLE);
               flip.setVisibility(View.VISIBLE);
               flip.showNext();*/
            }
            for(int i=0; i < Com.number.size(); i++ ){
               int Num = Com.number.get(i);
               if(Com.color.get(i).equals("b")){//블랙이면
                  if(IsBlackOpen.get(Num).equals("f")){
                     Firstcardlayout.getChildAt(i).setOnClickListener(CardListener);
                  }
               }else{//화이트이면
                  if(IsWhiteOpen.get(Num).equals("f")){
                     Firstcardlayout.getChildAt(i).setOnClickListener(CardListener);
                  }//f인 카드리스너만 
               }
            }
            break;
         case  R.id.selectCardOK1:   // 상대 카드 고르기 완료
            selectState=false;//골랐으면 상태를 false로 놓고 다음 흐름에도 고를 수 있게 하기 OK!
            flip.showNext();//다음 흐름화면으로 넘어감 (숫자 키 보여줌)
            selectCardOK.setVisibility(View.INVISIBLE);//다시 숨김

            for(int i=0; i<Com.number.size(); i++){
               Firstcardlayout.getChildAt(i).setOnClickListener(null);
            }
            break;

            //추리한 답을 누르면 해당 값을 줌
         case R.id.num0:
            int checkNUm=0;
            FinishTurnset(checkNUm);
            break;
         case R.id.num1:
            checkNUm=1;
            FinishTurnset(checkNUm);
            break;
         case R.id.num2:
            checkNUm=2;
            FinishTurnset(checkNUm);
            break;
         case R.id.num3:
            checkNUm=3;
            FinishTurnset(checkNUm);
            break;
         case R.id.num4:
            checkNUm=4;
            FinishTurnset(checkNUm);
            break;
         case R.id.num5:
            checkNUm=5;
            FinishTurnset(checkNUm);
            break;
         case R.id.num6:
            checkNUm=6;
            FinishTurnset(checkNUm);
            break;
         case R.id.num7:
            checkNUm=7;
            FinishTurnset(checkNUm);
            break;
         case R.id.num8:
            checkNUm=8;
            FinishTurnset(checkNUm);
            break;
         case R.id.num9:
            checkNUm=9;
            FinishTurnset(checkNUm);
            break;
         case R.id.num10:
            checkNUm=10;
            FinishTurnset(checkNUm);
            break;
         case R.id.num11:
            checkNUm=11;
            FinishTurnset(checkNUm);
            break;
         }
      }
   };
   public void FinishTurnset(int checkNUm){
      numCheck(Me,checkNUm);//선택한 값 맞는지 확인하기
      if(CardState.equals("Been")){

      }else{
         //처름 화면으로 감
         GameStartView.setVisibility(View.VISIBLE);
      }
      BlackCard.setOnClickListener(null);
      WhiteCard.setOnClickListener(null);

      Log.d("","로그 떼어내기 확인");
      checkWin();
   }
   public void checkWin(){
      int countC =0;
      int countM =0;
      //컴퓨터카드가 다 오픈됐는지 검사.
      for(int i=0;i<Com.number.size();i++){ 
         if(Com.color.get(i).equals("b")){//블랙일때
            if(IsBlackOpen.get(Com.number.get(i)).equals("t")){
               countC++ ;
            }
         }
         else{//화이트일때
            if(IsWhiteOpen.get(Com.number.get(i)).equals("t")){
               countC++ ;
            }
         }
      }
      //내카드가 다 오픈됐는지 검사.
      for(int i=0;i< Me.number.size();i++){  
         if(Me.color.get(i).equals("b")){//블랙일때
            if(IsBlackOpen.get(Me.number.get(i)).equals("t")){
               countM++ ;
            }
         }
         else{//화이트일때
            if(IsWhiteOpen.get(Me.number.get(i)).equals("t")){
               countM++ ;
            }
         }
      }
      //
      if(countC==Com.number.size()) {
         Result="win";
         Intent finish = new Intent(getApplicationContext(), Resultpage.class);
         startActivity(finish);
         finish();
      }
      else if(countM==Me.number.size()) {
         Result="lose";
         Intent finish = new Intent(getApplicationContext(), Resultpage.class);
         startActivity(finish);
         finish();
      }
      else{
         Turnset();
      }
   }


   public void checkHallCard(){
      //남은 카드가 없으면 안보이게 해놨는데 다중에 로고로 넣어줘도 될듯!!

      if(BlackCardNum.getCard().size()==0){
         BlackCard.setBackgroundResource(R.drawable.b12);
         BlackCard.setOnClickListener(null);

         //Toast.makeText(GameActivity.this,"",Toast.LENGTH_SHORT).show();
      }

      if(WhiteCardNum.getCard().size()==0){
         WhiteCard.setBackgroundResource(R.drawable.w12);
         WhiteCard.setOnClickListener(null);
      }
      //더이상 선택 할 카드가 없는데 게임이 안끝났으면
      Log.d("","블랙 카드가 빔?->"+BlackCardNum.getCard().size());
      Log.d("","화이트 카드가 빔?->"+WhiteCardNum.getCard().size());

      if(BlackCardNum.getCard().size()==0&&WhiteCardNum.getCard().size()==0&&Result.equals("None")){
         //GameStartView.setVisibility(View.INVISIBLE);
         //flip.setVisibility(View.VISIBLE);
         CardState = "Been";//모든 카드가 빔
         Log.d("","카드상태 가 BEEN??->"+CardState);
         for(int i=0; i < Com.number.size(); i++ ){
            int num = Com.number.get(i);
            if(Com.color.get(i).equals("b")){//블랙이면
               if(IsBlackOpen.get(num).equals("f")){
                  Firstcardlayout.getChildAt(i).setOnClickListener(CardListener);
               }
            }else{//화이트이면
               if(IsWhiteOpen.get(num).equals("f")){
                  Firstcardlayout.getChildAt(i).setOnClickListener(CardListener);
               }//f인 카드리스너만 
            }
         }
      }
      mycardlayout.invalidate();
      Firstcardlayout.invalidate();
   }
   ////추리한 수 체크
   public void numCheck(Player p,int checknum){
      Log.d("","정답:"+CheckCardNum);
      Log.d("","내답:"+checknum);
      //정답이면 카드 바꿔주기
      if(p.equals(Me)){
         if(CheckCardNum==checknum){//정답이면
            Log.d("","정답입니당");
            //오픈하는 카드
            selectedCard.setBackgroundResource(Spreadcard.OpenCard[CheckCardNum][selectedColor]);//해당 카드를 보이게 업데이트
            Com.select.set(((ViewGroup) selectedCard.getParent()).indexOfChild(selectedCard), "n");//해당 카드의 선택상태를 n으로 재 등록
            selectedCard.setOnClickListener(null);//오픈이니까 리스너를 없앤다
            Log.d("","내가 오흔한 카드색은 " + selectedColor);
            if(selectedColor==0) IsBlackOpen.put(CheckCardNum,"t");
            else IsWhiteOpen.put(CheckCardNum,"t");
            Log.d("","내가 오픈한 카드 " + CheckCardNum + " 는 t 여야함.");

            //클로우즈 카드
            addCardView.setBackgroundResource(Spreadcard.CloseCard[addCardNum][addCardColor][0]);
            //selectedCard.invalidate();

            if(CardState.equals("Been")){
               Toast.makeText(GameActivity.this,"정답!!!이길 수 있어요!",Toast.LENGTH_SHORT).show();
            }else{
               Toast.makeText(GameActivity.this,"정답입니다!!!상대의 카드를 공개합니다.",Toast.LENGTH_SHORT).show();
            }
            //시간 나면 또 할지 패스할지 선택하는거 띄우기
         }
         else{//틀리면 내가 가져온 카드 보여주기.
            Log.d("","틀렸습니당.");
            selectedCard.setBackgroundResource(Spreadcard.CloseCard[12][selectedColor][0]);//해당 카드를 선택해제그림으로 업데이트
            Com.select.set(((ViewGroup) selectedCard.getParent()).indexOfChild(selectedCard), "n");

            //오픈하는 카드
            addCardView.setBackgroundResource(Spreadcard.OpenCard[addCardNum][addCardColor]);//가져왔던 카드를 보이게 업데이트
            addCardView.setOnClickListener(null);//오픈이니까 리스너를 없앤다.
            Log.d("","내가 오픈한 카드색은 " + addCardColor);
            if(addCardColor==0) IsBlackOpen.put(addCardNum,"t");
            else IsWhiteOpen.put(addCardNum,"t");
            Log.d("","내가 오픈한 카드 " + addCardNum + " 는 t 여야함.");
            //selectedCard.invalidate();
            //addCardView.invalidate();
            if(CardState.equals("Been")){
               Toast.makeText(GameActivity.this,"틀렸습니다...ㅠㅠ",Toast.LENGTH_SHORT).show();
            }else{
               Toast.makeText(GameActivity.this,"틀렸습니다...ㅠㅠ 가져왔던 카드를 공개합니다!",Toast.LENGTH_SHORT).show();
            }
         }
      }else{//상대일때
         if(CheckCardNum==checknum){//정답이면 본인카드는 안보이게, 내 카드는 보이게
            Log.d("","정답입니당");
            //오픈하는 카드
            selectedCard.setBackgroundResource(Spreadcard.OpenCard[CheckCardNum][selectedColor]);//해당 카드를 보이게 업데이트
            selectedCard.setOnClickListener(null);
            Log.d("","컴퓨터가 오픈한 카드색은 " + selectedColor);
            if(selectedColor==0) IsBlackOpen.put(CheckCardNum,"t");
            else IsWhiteOpen.put(CheckCardNum,"t");
            Log.d("","컴퓨터가 오픈한  카드의 " + CheckCardNum +" 은 t여야 함");
            if(CardState.equals("Been")){
               Toast.makeText(GameActivity.this,"앗 들킴!이러다 지겠어요!",Toast.LENGTH_SHORT).show();
            }else{
               Toast.makeText(GameActivity.this,"앗 들킴! 당신의 카드를 공개합니다.",Toast.LENGTH_SHORT).show();
            }
            addCardView.setBackgroundResource(Spreadcard.CloseCard[12][addCardColor][0]);
            //시간 나면 또 할지 패스할지 선택하는거 띄우기
         }
         else{//틀리면 상대가 가져온 카드 보여주기.
            Log.d("","틀렸습니당."); 
            selectedCard.setBackgroundResource(Spreadcard.CloseCard[CheckCardNum][selectedColor][0]);//해당 카드를 선택해제그림으로 업데이트

            //오픈하는 카드
            addCardView.setBackgroundResource(Spreadcard.OpenCard[addCardNum][addCardColor]);//가져왔던 카드를 보이게 업데이트
            addCardView.setOnClickListener(null);
            Log.d("","컴퓨터가 추가한 카드색은 " + addCardColor);
            if(addCardColor==0) IsBlackOpen.put(addCardNum,"t");
            else IsWhiteOpen.put(addCardNum,"t");
            Log.d("","컴퓨터가 선택한 카드의 " + addCardNum +" 은 t여야 함");
            if(CardState.equals("Been")){
               Toast.makeText(GameActivity.this,"휴..안들켰어요!",Toast.LENGTH_SHORT).show();
            }else{
               Toast.makeText(GameActivity.this,"개이득!!상대의 카드를 공개합니다.",Toast.LENGTH_SHORT).show();
            }
         }
      }
      System.out.println("내 황트 오픈 : "+IsWhiteOpen);
      System.out.println("내 블랙 오픈 : " +IsBlackOpen);
      System.out.println("컴퓨터 황트 오픈 : "+IsWhiteOpen);
      System.out.println("텀퓨터 블랙 오픈 : "+IsBlackOpen);

   }
   //순서 넘기기
   public void ComputerPlaying(){
      final int index;
      final int answer ;
      
      //GameStartView.setVisibility(View.VISIBLE);
      flip.setVisibility(View.INVISIBLE);//카드 선택 화면으로 넘기기
      //flip.showNext();//처음 화면으로 셋팅해놓기
      Toast.makeText(GameActivity.this,"Computer가 게임을 시작합니다.",Toast.LENGTH_SHORT).show();


      // 1. 카드 선택하기
      if(CardState.equals("Been")){
         Log.d("","더이상 가져갈 카드가 없음");
         Toast.makeText(GameActivity.this,"더이상 선택할 카드가 없어 바로 다음 동작으로 넘어갑니다..",Toast.LENGTH_SHORT).show();
      }else{
         new Handler().postDelayed(new Runnable() {// 5초 후에 실행
            @Override
            public void run() {
               Toast.makeText(GameActivity.this,"상대방이 카드를 가져옵니다.",Toast.LENGTH_SHORT).show();
               buff.setBuff(Com);
               String play1 =computer.getPlay1(BlackCardNum, WhiteCardNum);
               if(play1.equals("b")){//black 이면
                  addCard(Com,BlackCardNum,"b");
                  Log.d("","1. 블랙카드 선택");
               }
               else{//흰색이면
                  addCard(Com,WhiteCardNum,"w");
                  Log.d("","1. 화이트 카드 선택");
               }
               addCardView.setOnClickListener(null);
            }
         }, 4000);   
      }
      index = computer.getPlay2(Me,IsBlackOpen,IsWhiteOpen);

      // 2. 추리할 카드 고르기
      new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
            // 실행할 동작 코딩
            Toast.makeText(GameActivity.this,"상대방이 당신의 카드를 선택합니다.",Toast.LENGTH_SHORT).show();
            //선택한 카드색을 담음
            int ImgColor;
            if(Me.color.get(index).equals("b")){//블랙이면
               ImgColor=0;//블랙
            }
            else {
               ImgColor=1;//흰색이면 흰색으로.
            }

            //선택한 카드의 숫자값
            int ImgValue=Me.number.get(index);

            mycardlayout.getChildAt(index).setBackgroundResource(Spreadcard.CloseCard[ImgValue][ImgColor][1]);//바꾸기OK!
            mycardlayout.invalidate();

            //버퍼 초기화
            CheckCardNum = ImgValue;
            selectedCard=mycardlayout.getChildAt(index);
            selectedColor=ImgColor;
            selectState=false; //선택 초기화    필요없을수도 있으니 체크해보기
         }
      }, 8000);   
      answer = computer.getPlay3(Me, index);


      // 3. 카드값 추리하기
      new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
            numCheck(Com,answer);//맞는지 확인하기
         }
      }, 12000);   
      new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
            // 실행할 동작 코딩
            Turn++;//턴 넘겨주기
            Log.d("","Turn= "+Turn);
            checkWin();
         }
      }, 15000);    
   }
   //턴 바꾸기
   public void Turnset() {
      if(Turn==1){
         Log.d("","이제 내 차례ㅖㅖㅖㅖ");
         if(BlackCardNum.getCard().size()==0&&WhiteCardNum.getCard().size()==0){
            GameStartView.setVisibility(View.INVISIBLE);
            flip.setVisibility(View.VISIBLE);
            flip.showNext();
         }
         Toast.makeText(GameActivity.this,"Your Turn!",Toast.LENGTH_SHORT).show();
         //리스너 재 등록하기
         BlackCard.setOnClickListener(btnListener);
         WhiteCard.setOnClickListener(btnListener);


         Turn--;
      }
      else {
         Log.d("","이제 컴퓨터 차례ㅖㅖㅖㅖ");
         ComputerPlaying();//인공지능 실행
      }
   }
   //게임 준비중
   public void setBCard(){
      if(Bready<4){
         Bready++;
         Wready--;
      }
      else
         Toast.makeText(GameActivity.this,"총4개까지 선택 할 수 있습니다.",Toast.LENGTH_SHORT).show();
   }
   public void setWCard(){
      if(Wready<4){
         Wready++;
         Bready--;
      }else
         Toast.makeText(GameActivity.this,"총4개까지 선택 할 수 있습니다.",Toast.LENGTH_SHORT).show();
   }
   //////////////////////////////////////////////////////////////////////////////////선택한 4개 카드 세팅 하기
   public void setPlay(){
      //내 카드
      for(int i=0; i<Bready; i++){
         Me.myturn(BlackCardNum,"b");
      }

      for(int i=0; i<Wready; i++){
         Me.myturn(WhiteCardNum,"w");
      }
      //컴퓨터 카드
      Random random = new Random();
      int r = random.nextInt(4)+1;
      for(int i=0; i<r; i++){
         Com.myturn(BlackCardNum,"b");
      }
      for(int i=0; i<4-r; i++){
         Com.myturn(WhiteCardNum,"w");
      }
      Log.d("","카드 4개  가져오기");
      Me.setting();
      Com.setting();
      Log.d("","카드 4개  정렬하기");
   }

   ///////////////////////////////////////////////////////////////////////////////게임 준비 ///위에서 세팅한 카드 숫자값과 color값을 리스트에서 가져오기
   public void ReadyCard(){
      for(int i=0; i<Me.number.size(); i++){
         mycardlayout.addView(getCard(Me,Me.number.get(i),Me.color.get(i),"c","n"));
         Firstcardlayout.addView(getCard(Com,12,Com.color.get(i),"c","n"));
      }
      //리스너 없애기
      for(int i=0; i<Com.number.size(); i++){
         Firstcardlayout.getChildAt(i).setOnClickListener(null);
      }
      mycardlayout.invalidate();
      Firstcardlayout.invalidate();
      Log.d("","카드 4개 세팅성공");
   }


   /////////////////////////////////////////////////////////////////////////////////카드 추가하기 OK!
   public void addCard(Player p,Card card,String color ){
      p.myturn(card,color);
      p.setting();      
      int index = compare(p);//buff와 비교해서 인덱스 값을 찾아냄

      Log.d("","넘버값 : "+p.number.get(index));

      if(p.equals(Me)){
         mycardlayout.addView(getCard(Me,p.number.get(index),color,"c","s"),index);
         mycardlayout.invalidate();
         addCardNum= p.number.get(index);
         addCardView=mycardlayout.getChildAt(index);
      }
      else if(p.equals(Com)){//
         Firstcardlayout.addView(getCard(Com,p.number.get(index),color,"c","s"),index);//새로운 카드가 어디 들어갔는지 알아야 하니까!
         Firstcardlayout.invalidate();
         addCardNum= p.number.get(index);
         addCardView=Firstcardlayout.getChildAt(index);
      }

      //버퍼 설정;
      if(color.equals("b"))
         addCardColor=0;
      else addCardColor=1;
   }
   //비교OK!
   public int compare(Player p){
      int index=0;
      for(int i=0; i<buff.numbuff.size();i++){
         System.out.println(buff.numbuff.get(i)+" = "+p.number.get(i)+" ?");
         if(buff.numbuff.get(i).equals(p.number.get(i))){
            if(buff.colorbuff.get(i).equals(p.color.get(i))){//숫자와 칼라값이 모두 같으면 다음 인덱스
               System.out.println("oo");
               index++;
            }
            else if(buff.colorbuff.get(i).equals("b")){//새로 들어온 값이 같은 숫자인 화이트 이므로   
               index++;
               break;
            }
            else{//새로 들어온 값이 같은숫자 블랙 이므로
               break;
            }
         }
         else {

            break;
         }
      }
      Log.d("","새로운 카드는"+p.color.get(index)+", "+p.number.get(index)+"인지 확인");
      Log.d("","인덱스 값 : "+index);
      return index;
   }
   /////////////////////////////////////////////////////////////////////누군지,카드숫자,오픈여부,선택 여부 알려주기
   public ImageButton getCard(Player player, int cardnum, String color, String state, String select){
      ImageButton cardView = new ImageButton(this);
      CardInfo Spreadcard = new CardInfo();
      //int cardnum=0;
      //Player player = null;
      //String state="";//open close
      //String color="";//black white
      if(player.equals(Me)){//나일때
         if(state.equals("c")){//닫힘
            if(color.equals("b")){//블랙
               if(select.equals("n")){//비선택
                  cardView.setBackgroundResource(Spreadcard.CloseCard[cardnum][0][0]);
               }else{//선택
                  cardView.setBackgroundResource(Spreadcard.CloseCard[cardnum][0][1]);
               }

            }else{//화이트
               if(select.equals("n")){//비선택
                  cardView.setBackgroundResource(Spreadcard.CloseCard[cardnum][1][0]);
               }else{//선택
                  cardView.setBackgroundResource(Spreadcard.CloseCard[cardnum][1][1]);
               }
            }
         }else{//열림
            if(select.equals("b")){//블랙
               cardView.setBackgroundResource(Spreadcard.OpenCard[cardnum][0]);
            }else{//화이트
               cardView.setBackgroundResource(Spreadcard.OpenCard[cardnum][1]);
            }

         }
      }else{//상대방일때
         if(state.equals("c")){//닫힘
            if(color.equals("b")){//블랙
               if(select.equals("n")){//비선택
                  cardView.setBackgroundResource(Spreadcard.CloseCard[12][0][0]);
               }else{//선택
                  cardView.setBackgroundResource(Spreadcard.CloseCard[12][0][1]);
               }

            }else{//화이트
               if(select.equals("n")){//비선택
                  cardView.setBackgroundResource(Spreadcard.CloseCard[12][1][0]);
               }else{//선택
                  cardView.setBackgroundResource(Spreadcard.CloseCard[12][1][1]);
               }
            }
         }else{//열림
            if(color.equals("b")){//블랙
               cardView.setBackgroundResource(Spreadcard.OpenCard[12][0]);
            }else{//화이트
               cardView.setBackgroundResource(Spreadcard.OpenCard[12][1]);
            }

         }
         cardView.setOnClickListener(CardListener);//상대방 카드에다만 리스너 달기
      }
      return cardView;
   }
   //카드 선택시!!
   View.OnClickListener CardListener = new View.OnClickListener(){
      int index=0;
      public int getColor(){
         String color;
         //사람구별
         if(Turn==0){//나일때
            color=Com.color.get(index);
         }
         else{
            color=Me.color.get(index);
         }
         Log.d("",index+" 번째 선택한 카드 색깔 : "+color);
         //컬러값 별 숫자 구별
         if(color.equals("b"))return 0;
         else return 1;
      }
      @Override
      public void onClick(View view) {
         //1개만 선택하기 기능 OK!
         if(Turn==0){//나일때
            //상대 카드배치 레이아웃안에 추가된 자식(카드)의 index번호 알아내기 
            index = ((ViewGroup) view.getParent()).indexOfChild(view);
            Log.d("","선택한 카드 인덱스 : "+index);
            //선택한 상태가 아니면
            if(selectState==false){
               //상대 카드 선택여부가 n이면
               if(Com.select.get(index).equals("n")){
                  view.setBackgroundResource(Spreadcard.CloseCard[12][getColor()][1]);//비선택이었으면 선택넣기
                  Com.select.set(index, "s");//선택으로 상태 업데이트
                  Log.d("",Com.select.get(index)+"는 s여야 함");
                  //버퍼들 설정
                  CheckCardNum=Com.number.get(index);
                  selectedCard=view;//현재 선택한 카드
                  selectedColor=getColor();//블랙이면 b
                  selectCardOK.setVisibility(View.VISIBLE);//선택 되면 보이게함
               }
               //체크해보고 필요없으면 지우기 ㅎ0ㅎ
               else{
                  Toast.makeText(GameActivity.this,"이런 경우가 있나?체크!",Toast.LENGTH_SHORT).show();
               }

               //셀렉트ㄱ가 없으면 다시 false러 만들기
               for(int i=0; i<Com.select.size(); i++){
                  if(Com.select.get(i).equals("s")){
                     selectState=true;
                     break;
                  }
               }
               Log.d("","셀렉트 상태 : "+selectState);//완료
            }
            else{ 
               if(Com.select.get(index).equals("s")){
                  view.setBackgroundResource(Spreadcard.CloseCard[12][getColor()][0]);//선택이었으면 비선택 넣기
                  Com.select.set(index, "n");//상태 업데이트
                  Log.d("",Com.select.get(index)+"는 n이여야 함");
                  selectCardOK.setVisibility(View.INVISIBLE);//선택 취소하면 안보이게 함
                  selectState=false;
               }
               else//이미 선택한 카드가 있는데 또 다른걸 선택 하려 할 때
                  Toast.makeText(GameActivity.this,"* 한개만 선택 가능합니다.",Toast.LENGTH_SHORT).show();
            }
         }
         else{
            Toast.makeText(GameActivity.this,"상대방이 Play 중입니다.",Toast.LENGTH_SHORT).show();
         }
         Log.d("","정답 : "+CheckCardNum);
      }
      private ViewGroup getParent() {
         // TODO Auto-generated method stub
         return null;
      }

   };


   Handler mHandler = new Handler() {
      public void handleMessage(Message msg) {  // 실행이 끝난후 확인 가능

      }
   };   
   Handler handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
         switch(msg.what){//숫자
         case SEND_CARD_COUNT://게임 시작 전 카드 선택
            bn.setText(String.valueOf(Bready));
            wn.setText(String.valueOf(Wready));
            break;
         case SEND_STOP:
            thread.stopThread();
            break;
         case SEND_CARD_COUNT2://총 카드 수
            B_count.setText(String.valueOf("남은개수 : "+BlackCardNum.getCard().size()));
            W_count.setText(String.valueOf("남은개수 : "+WhiteCardNum.getCard().size()));
            checkHallCard();
            if(B_count.getText().equals("0")) BlackCard.setEnabled(false);
            else if(W_count.getText().equals("0")) WhiteCard.setEnabled(false);
            break;
         case SEND_COM_CARD_SET://인공지능 화면전환 느리게
            //mycardlayout.invalidate();
            //Firstcardlayout.invalidate();
         default:
            break;
         }
      }
   };
   class Thread extends java.lang.Thread {
      boolean stopped = false;
      int i = 0;
      public Thread(){
         stopped = false;
      }
      public void stopThread() {
         stopped = true;
      }
      @Override
      public void run() {
         super.run();
         while(stopped == false) {  
            try {
               handler.sendEmptyMessage(SEND_CARD_COUNT);
               // 1초 씩 딜레이 부여
               sleep(1000);
            } catch (InterruptedException e)
            {
               e.printStackTrace();
            }
         }
      }
   }
   class Thread2 extends java.lang.Thread {

      @Override
      public void run() {
         super.run();
         while(true) {  
            try {
               handler.sendEmptyMessage(SEND_CARD_COUNT2);
               // 3초 씩 딜레이 부여
               sleep(1000);
            } catch (InterruptedException e)
            {
               e.printStackTrace();
            }
         }
      }
   }
   //컴퓨터모드의 화면전환을 위해
   class MyGraphicView extends View{
      public MyGraphicView(Context context) {
         super(context);
      }
      @Override
      protected void onDraw(Canvas canvas){
         super.onDraw(canvas);
         Paint paint = new Paint();

         RectF rec3 = new RectF(250, 50, 130+100, 50+100);
         canvas.drawRect(rec3, paint);
      }

   }
   //뒤로가기로 게임방 나가지는것방지.
   public boolean onKeyDown(int KeyCode,KeyEvent event){
      switch(KeyCode){
      case KeyEvent.KEYCODE_BACK:
         new AlertDialog.Builder(this).setTitle("종료").setMessage("종료하시겠어요?").setPositiveButton("예",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
               Intent gotolobby = new Intent(getApplicationContext(), Lobbypage.class);
               startActivity(gotolobby);
               finish();
            }
         }).setNegativeButton("아니오",null).show();
         return false;
      default : return false;
      }
   }
}