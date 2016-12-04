package com.example.mmoor_000.s236326_mappe3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mmoor_000 on 03-Dec-16.
 */

public class Widget extends AppWidgetProvider {

    Database db;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        db = new Database(context);
        db.getWritableDatabase();
        Cursor cur = db.Finn(1);
        String skritt = "";
        String mal = "";
        int a = 0;
        int b = 0;
        if (cur.moveToFirst())
            do {
                skritt = cur.getString(8);
                a = Integer.valueOf(skritt);
                mal = cur.getString(6);
                b = Integer.valueOf(mal);
            } while (cur.moveToNext());
        cur.close();
        RemoteViews updateViews = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_layout);
        updateViews.setTextViewText(R.id.wskritt, a+"");
        Intent clickIntent = new Intent(context,Widget.class);
        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,clickIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.refresh,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds,updateViews);
        int i = ((b + 1) - 2500) * 2500;
        updateViews.setTextViewText(R.id.dm, i+"");
        Intent clickIntent2 = new Intent(context,Widget.class);
        clickIntent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,0,clickIntent2,PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.refresh,pendingIntent2);
        appWidgetManager.updateAppWidget(appWidgetIds,updateViews);

    }
}
