package Playgame;

import java.util.ArrayList;
import java.util.Random;

public class SelectCard {
	int value;
	public SelectCard(){

	}
	public int selectindex(Card card){
		int index;
		int size =card.getCard().size();
		Random random = new Random();
		if(size==2){
			index = 0;
		}else if(size ==1){
			index = 0;	
		}else if(size ==0){
			index = 0;
		}else{
			index = random.nextInt(size);
		}
		value=card.getCard().get(index);
		System.out.println("인덱스 : "+index+" 값:"+value);
		return value;
	}
}