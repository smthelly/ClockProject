package Playgame;
import java.util.ArrayList;

public class Card {
	private ArrayList <Integer> card;


	public Card(){
		//card.clear();
		card = new ArrayList<Integer>();
		for(int i=0; i<12;i++){
			card.add(i);
		}
	}
	public ArrayList <Integer> getCard(){
		ArrayList <Integer> buff =new ArrayList<Integer>();
		for(int i=0; i<card.size();i++){
			buff.add(card.get(i));
		}
		return buff;
	}
	public void removeCard(int value) {
		ArrayList <Integer>arr = new ArrayList<Integer>();
		arr.add(value);
		System.out.print(value+ "번 Card : ");
		card.removeAll(arr);
		System.out.println(value+"카드를 삭제함");
		// TODO Auto-generated method stub
	}
}