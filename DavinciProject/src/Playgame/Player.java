package Playgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

public class Player {
   private HashMap <Integer,String> MyBlackCard = new HashMap<Integer,String>();
   private HashMap <Integer,String> MyWhiteCard = new HashMap<Integer,String>();

   public ArrayList <Integer> number = new ArrayList<Integer>(); //흰색,검은색 카드를 합쳐놓은 덱의 숫자.
   public ArrayList <String> color = new ArrayList<String>();//흰색,검은색을 알려준다.
   public ArrayList <String> select = new ArrayList<String>(); //카드의 선택여부를 알려준다.
   private MyCard card;
   private SelectCard selectCard;

   public Player(){
      card= new MyCard();
      selectCard= new SelectCard();
   }

   public void myturn(Card card,String color){


      int index = selectCard.selectindex(card);
      card.removeCard(index);
      this.card.addMyCard(selectCard.value);
      if(color.equals("b")){
         MyBlackCard.put(selectCard.value,color);
         select.add("n");
      }else{
         MyWhiteCard.put(selectCard.value,color);
         select.add("n");
      }
   }
   public void setting(){
      TreeMap settingBCard = new TreeMap(MyBlackCard);//숫자가 작은 값부터정렬!
      TreeMap settingWCard = new TreeMap(MyWhiteCard);
      number.clear();
      color.clear();
      Iterator<Integer> keySetIterator = settingBCard.keySet().iterator();
      while (keySetIterator.hasNext()) {
         Integer key = keySetIterator.next();
         System.out.println("key: " + key + " value: " + settingBCard.get(key));
      }
      Iterator<Integer> WkeySetIterator = settingWCard.keySet().iterator();
      while (WkeySetIterator.hasNext()) {
         Integer key = WkeySetIterator.next();
         System.out.println("key: " + key + " value: " + settingWCard.get(key));
      }

      Iterator<Integer> keySetIterators = settingBCard.keySet().iterator();
      Iterator<Integer> WkeySetIterators = settingWCard.keySet().iterator();

      ArrayList <Integer> tempnumB = new ArrayList <Integer> ();
      ArrayList <Integer> tempnumW = new ArrayList <Integer> ();
      while(keySetIterators.hasNext()){
         Integer key = keySetIterators.next();
         tempnumB.add(key);
      }
      while(WkeySetIterators.hasNext()){
         Integer keys = WkeySetIterators.next();
         tempnumW.add(keys);
      }

      int BCount = 0;
      int WCount = 0;
      int totalB = tempnumB.size();
      int totalW = tempnumW.size();
      System.out.println("정렬된 B:"+tempnumB);
      System.out.println("정렬된 W:"+tempnumW);
      while(tempnumB.size()!=0 &&tempnumW.size()!=0){
         System.out.println(tempnumB.get(0)+"와"+tempnumW.get(0)+"비교");
         if(tempnumB.get(0)<=tempnumW.get(0)){
            number.add(tempnumB.get(0));
            color.add("b");
             BCount ++;
            tempnumB.remove(0);
         }else{
            number.add(tempnumW.get(0));
            color.add("w");
            WCount ++;
            tempnumW.remove(0);
         }
         if(tempnumB.size()==0 || tempnumW.size()==0) break;
      }

      while(tempnumB.size()>0){
         number.add(tempnumB.get(0));
         color.add("b");
         tempnumB.remove(0);
      }


      while(tempnumW.size()>0){
         number.add(tempnumW.get(0));
         color.add("w");
         tempnumW.remove(0);

      }
      System.out.println("number arraylist : "+ number);
      System.out.println("color arraylist : " + color);
      System.out.println("select arraylist : "+ select);
   }
}