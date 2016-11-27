package Playgame;

import java.util.ArrayList;

public class MyCard {
   private ArrayList <Integer> Mycard;
   
   public MyCard(){
      Mycard = new ArrayList<Integer>();
   }
   
   //내 카드 세팅
   public void addMyCard(int value ){
      System.out.print("내 카드에 " +value);
      Mycard.add(value);
      System.out.println(" 추가");
   }
   
   public ArrayList <Integer> getMyCard(){
      ArrayList <Integer> buff =new ArrayList<Integer>();
      for(int i=0; i<Mycard.size();i++){
         buff.add(Mycard.get(i));
      }
      return buff;
   }
}