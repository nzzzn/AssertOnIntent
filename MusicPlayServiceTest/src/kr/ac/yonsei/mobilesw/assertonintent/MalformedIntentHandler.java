package kr.ac.yonsei.mobilesw.assertonintent;

import android.content.Intent;

public interface MalformedIntentHandler {
	public void handle(Intent intent, MalformedIntentException m);
	
}