package com.example.davinciproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class GameIntro1 extends ActionBarActivity {
	ImageButton how01,how02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro1);
		

		how01 = (ImageButton) findViewById(R.id.how01);
		how02 = (ImageButton) findViewById(R.id.how02);
		
		how01.setOnClickListener(btnListener);
		how02.setOnClickListener(btnListener);
	}
	View.OnClickListener btnListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.how01 : 
				Intent how1 = new Intent(getApplicationContext(), GameIntro2.class);
				startActivity(how1);
				finish();
				break;
			case R.id.how02 : 
				Intent how2 = new Intent(getApplicationContext(), GameIntro4.class);
				startActivity(how2);
				finish();
				break;

		}}
	};
}
