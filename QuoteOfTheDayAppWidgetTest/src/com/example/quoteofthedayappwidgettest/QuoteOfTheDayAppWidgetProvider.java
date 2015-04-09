package com.example.quoteofthedayappwidgettest;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class QuoteOfTheDayAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String quotes = intent.getStringExtra("quote");

		if (quotes == null)
			quotes = "Quotes of the Day";

		RemoteViews remote = new RemoteViews(context.getPackageName(),
				R.layout.quoteview);

		remote.setTextViewText(R.id.textview1, quotes);

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		appWidgetManager.updateAppWidget(new ComponentName(
				"com.example.quoteofthedayappwidgettest",
				"com.example.quoteofthedayappwidgettest."
						+ "QuoteOfTheDayAppWidgetProvider"), remote);

		super.onReceive(context, intent);
	}

}
