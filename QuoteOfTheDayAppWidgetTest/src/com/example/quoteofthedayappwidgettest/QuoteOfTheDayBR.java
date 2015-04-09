package com.example.quoteofthedayappwidgettest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kr.ac.yonsei.mobilesw.assertonintent.*;

public class QuoteOfTheDayBR extends BroadcastReceiver {

	public QuoteOfTheDayBR() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		new AssertOnIntent().assertOnIntent(intent, 
				"{ act = android.appwidget.provider cmp = com.example.quoteofthedayappwidgettest/.QuoteOfTheDayBR [quote = String] } || " +
				"{ act = android.intent.action.QUOTES_OF_THE_DAY_RECEIVED cmp = com.example.quoteofthedayappwidgettest/.QuoteOfTheDayBR [quote = String] }",
				null);
		
		int[] count = intent.getIntArrayExtra("count");
		int countSum = count[0] + 1;
		
		/*
		Notification n_msg = new Notification.Builder(context)
				.setContentTitle("Quote of the Day")
				.setContentText(intent.getStringExtra("quote"))
				.setSmallIcon(R.drawable.ic_launcher).build();

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		nm.notify(0, n_msg);
		*/
	}

}
