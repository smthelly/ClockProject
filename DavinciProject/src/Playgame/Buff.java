package Playgame;

import java.util.ArrayList;

public class Buff {
   public ArrayList <Integer> numbuff;
   public ArrayList <String> colorbuff;
   public Buff(){
      numbuff = new ArrayList <Integer>();
      colorbuff = new ArrayList <String>();
   }
   
   public void setBuff(Player p){
      numbuff.clear();
      colorbuff.clear();
      //숫자
      for(int i=0; i<p.number.size(); i++){
         numbuff.add(p.number.get(i));
      }
      //컬러
      for(int i=0; i<p.color.size(); i++){
         colorbuff.add(p.color.get(i));
      }
      
   }

}