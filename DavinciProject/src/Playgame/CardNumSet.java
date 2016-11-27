package Playgame;

import java.util.ArrayList;

public class CardNumSet {
	static ArrayList <Integer> WhiteCard;
	static ArrayList <Integer> BlackCard;
	static ArrayList <Integer> Mycard;
	static ArrayList <Integer> ComputerCard;

	public CardNumSet(){
		WhiteCard = new ArrayList<Integer>();
		BlackCard = new ArrayList<Integer>();
		Mycard = new ArrayList<Integer>();
		ComputerCard = new ArrayList<Integer>();
		for(int i=0; i<12;i++){
			WhiteCard.add(i);
			BlackCard.add(i);
		}
		

	}
	public ArrayList <Integer> getWhiteCard(){
		return WhiteCard;
	}
	public ArrayList <Integer> getBlackCard(){
		return BlackCard;
	}
	public void start(){
		
	}

}
