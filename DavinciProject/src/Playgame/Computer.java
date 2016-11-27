package Playgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.example.davinciproject.GameActivity;

public class Computer {
	Random random ;
	public Computer(){
		random = new Random();
	}

	//동작 1 : 가져올 카드 색 선태하기
	public String getPlay1(Card BlackCardNum,Card WhiteCardNum){
		int buff;
		String color = null;

		if(BlackCardNum.getCard().size()!=0 && WhiteCardNum.getCard().size()!=0){// 둘 다 카드가 아직 있으면
			buff = random.nextInt(2);//0 또는 1
			//0이면 블랙 1이면 화이트
			if(buff==0){
				color = "b";
			}else color="w";
		}
		else if(BlackCardNum.getCard().size()==0)//블랙 카드가 없으면
			color="w";
		else color ="b";//화이트 카드가 없으면

		return color;
	}

	//동작 2 : 상대방 카드 선택하기
	public int getPlay2(Player p,HashMap <Integer,String> IsBlackOpen,HashMap <Integer,String> IsWhiteOpen){
		int index = 0;

		while(true){
			int size = p.number.size();
			index = random.nextInt(size);//선택 할 상대방 카드의 인덱스 번호
			if(p.color.get(index).equals("b")){ //검정카드일때 오픈여부검사
				if(IsBlackOpen.get(p.number.get(index)).equals("f")) 
					break;
			}else if(p.color.get(index).equals("w")){ //흰색카드일때 오픈여부검사
				if(IsWhiteOpen.get(p.number.get(index)).equals("f"))
					break;
			}
		}
		return index;
	}

	//동작 3 : 선택한 상대 카드 번호 추리하기
	public int getPlay3(Player p,int index){
		int correct; //정답
		int answer =0;
		correct = p.number.get(index);

		if(GameActivity.GameLevel ==1){//컴퓨터가 30퍼의확률로 맞춤.
			int a = random.nextInt(10);
			if(a>=0 && a<3) answer = correct;
			else{
				int size;
				size = correct +2;
				answer = random.nextInt(size);
			}
		}else if(GameActivity.GameLevel ==5){//컴퓨터가 50퍼의 확률로 맞춤.
			int a = random.nextInt(10);
			if(a>=0&& a<5) answer = correct;
			else{
				int size;
				size = correct +2;
				answer = random.nextInt(size);
			}
		}else if(GameActivity.GameLevel==10){//컴퓨터가 80퍼의 확률로 맞춤.
			int a = random.nextInt(10);
			if(a>=0 && a<8) answer = correct;
			else{
				int size;
				size = correct +2;
				answer = random.nextInt(size);
			}
		}else{
			//정답 맞출 확률설정------------맞힌 카드수가 많아질수록 맞힐 확률이 높게 나주에 설정해보기. 진짜 확률 계산 이용해보기.
			int size;
			size = correct+2;

			//확률에 따라 정답 추리하기

			answer=random.nextInt(size);
		}
		return answer;
	}
}
