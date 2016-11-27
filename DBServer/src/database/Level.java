package database;

public class Level {
	public Level() {
		System.out.println("레벨 지정중");
	}	
	public int setLevel(int chip){
		if(chip>=100){
			System.out.println("레벨 : 상");
			return 3;
		}
		else if(chip>=50){
			System.out.println("레벨 : 중");
			return 2;
		}
		else{
			System.out.println("레벨 : 하");
			return 1;
		}
	}
}
