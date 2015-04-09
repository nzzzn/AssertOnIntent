package com.example.quoteofthedayappwidgettest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View v) {
		Log.i("MainActivity", "onClick");
		TextView tv = (TextView) findViewById(R.id.textview);
		Intent intent = new Intent();
		intent.setAction("android.intent.action.QUOTES_OF_THE_DAY_RECEIVED");
		intent.putExtra("quote", tv.getText().toString());
		sendBroadcast(intent);
	}
}
