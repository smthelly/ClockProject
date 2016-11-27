package Playgame;

import com.example.davinciproject.R;
import com.example.davinciproject.R.drawable;

import android.util.Log;

public class CardInfo {
   public int [][][] CloseCard = new int[13][2][2]; //[카드숫자(0~11),뒷면(12)][색깔(흰,검)][선택여부]
   public int [][] OpenCard = new int[12][2]; //오픈된카드(숫자에빨간테두리이미지)
   public static int BLACK = 0;//검은색카드
   public static int WHITE = 1;//흰색카드
   public static int NONE = 0; //선택안된카드
   public static int SELECTED =1;//선택된카드(카드바깥빨간테두리)
 
   public CardInfo(){
      Log.d("","3 카드 설정에 들어옴");
     //상대에게 공개되지않은 내검은카드
      CloseCard[0][BLACK][NONE]=R.drawable.b0;
      CloseCard[1][BLACK][NONE]=R.drawable.b1;
      CloseCard[2][BLACK][NONE]=R.drawable.b2;
      CloseCard[3][BLACK][NONE]=R.drawable.b3;
      CloseCard[4][BLACK][NONE]=R.drawable.b4;
      CloseCard[5][BLACK][NONE]=R.drawable.b5;
      CloseCard[6][BLACK][NONE]=R.drawable.b6;
      CloseCard[7][BLACK][NONE]=R.drawable.b7;
      CloseCard[8][BLACK][NONE]=R.drawable.b8;
      CloseCard[9][BLACK][NONE]=R.drawable.b9;
      CloseCard[10][BLACK][NONE]=R.drawable.b10;
      CloseCard[11][BLACK][NONE]=R.drawable.b11;
      CloseCard[12][BLACK][NONE]=R.drawable.b12; //뒷면(로고그려잇음)

      
      //상대에게 공개되지않은 내하얀카드
      CloseCard[0][WHITE][NONE]=R.drawable.w0;
      CloseCard[1][WHITE][NONE]=R.drawable.w1;
      CloseCard[2][WHITE][NONE]=R.drawable.w2;
      CloseCard[3][WHITE][NONE]=R.drawable.w3;
      CloseCard[4][WHITE][NONE]=R.drawable.w4;
      CloseCard[5][WHITE][NONE]=R.drawable.w5;
      CloseCard[6][WHITE][NONE]=R.drawable.w6;
      CloseCard[7][WHITE][NONE]=R.drawable.w7;
      CloseCard[8][WHITE][NONE]=R.drawable.w8;
      CloseCard[9][WHITE][NONE]=R.drawable.w9;
      CloseCard[10][WHITE][NONE]=R.drawable.w10;
      CloseCard[11][WHITE][NONE]=R.drawable.w11;
      CloseCard[12][WHITE][NONE]=R.drawable.w12;

      //컴퓨터에게 선택당한 검은카드ㅏ
      CloseCard[0][BLACK][SELECTED]=R.drawable.b0r; 
      CloseCard[1][BLACK][SELECTED]=R.drawable.b1r;
      CloseCard[2][BLACK][SELECTED]=R.drawable.b2r;
      CloseCard[3][BLACK][SELECTED]=R.drawable.b3r;
      CloseCard[4][BLACK][SELECTED]=R.drawable.b4r;
      CloseCard[5][BLACK][SELECTED]=R.drawable.b5r;
      CloseCard[6][BLACK][SELECTED]=R.drawable.b6r;
      CloseCard[7][BLACK][SELECTED]=R.drawable.b7r;
      CloseCard[8][BLACK][SELECTED]=R.drawable.b8r;
      CloseCard[9][BLACK][SELECTED]=R.drawable.b9r;
      CloseCard[10][BLACK][SELECTED]=R.drawable.b10r;
      CloseCard[11][BLACK][SELECTED]=R.drawable.b11r;
      CloseCard[12][BLACK][SELECTED]=R.drawable.b12r; //내가 선택한 컴퓨터검정카드뒷면
      
      //컴퓨터에게 선택당한 하얀카드
      CloseCard[0][WHITE][SELECTED]=R.drawable.w0r;
      CloseCard[1][WHITE][SELECTED]=R.drawable.w1r;
      CloseCard[2][WHITE][SELECTED]=R.drawable.w2r;
      CloseCard[3][WHITE][SELECTED]=R.drawable.w3r;
      CloseCard[4][WHITE][SELECTED]=R.drawable.w4r;
      CloseCard[5][WHITE][SELECTED]=R.drawable.w5r;
      CloseCard[6][WHITE][SELECTED]=R.drawable.w6r;
      CloseCard[7][WHITE][SELECTED]=R.drawable.w7r;
      CloseCard[8][WHITE][SELECTED]=R.drawable.w8r;
      CloseCard[9][WHITE][SELECTED]=R.drawable.w9r;
      CloseCard[10][WHITE][SELECTED]=R.drawable.w10r;
      CloseCard[11][WHITE][SELECTED]=R.drawable.w11r;
      CloseCard[12][WHITE][SELECTED]=R.drawable.w12r; //내가선택한 컴퓨터의 하얀카드뒷면
      
      //상대에게 공개된 내 검은카드
      OpenCard[0][BLACK]=R.drawable.b0v;//공개된검은색카드
      OpenCard[1][BLACK]=R.drawable.b1v;
      OpenCard[2][BLACK]=R.drawable.b2v;
      OpenCard[3][BLACK]=R.drawable.b3v;
      OpenCard[4][BLACK]=R.drawable.b4v;
      OpenCard[5][BLACK]=R.drawable.b5v;
      OpenCard[6][BLACK]=R.drawable.b6v;
      OpenCard[7][BLACK]=R.drawable.b7v;
      OpenCard[8][BLACK]=R.drawable.b8v;
      OpenCard[9][BLACK]=R.drawable.b9v;
      OpenCard[10][BLACK]=R.drawable.b10v;
      OpenCard[11][BLACK]=R.drawable.b11v;

      //상대에게 공개된 내 하얀카드
      OpenCard[0][WHITE]=R.drawable.w0v;//공개된하얀색카드
      OpenCard[1][WHITE]=R.drawable.w1v;
      OpenCard[2][WHITE]=R.drawable.w2v;
      OpenCard[3][WHITE]=R.drawable.w3v;
      OpenCard[4][WHITE]=R.drawable.w4v;
      OpenCard[5][WHITE]=R.drawable.w5v;
      OpenCard[6][WHITE]=R.drawable.w6v;
      OpenCard[7][WHITE]=R.drawable.w7v;
      OpenCard[8][WHITE]=R.drawable.w8v;
      OpenCard[9][WHITE]=R.drawable.w9v;
      OpenCard[10][WHITE]=R.drawable.w10v;
      OpenCard[11][WHITE]=R.drawable.w11v;
   }

}