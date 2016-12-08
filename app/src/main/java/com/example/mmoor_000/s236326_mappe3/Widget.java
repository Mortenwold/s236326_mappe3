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

    public void onUpdate(Context context, AppWidgetManager awm, int[] id) {
        db = new Database(context);
        db.getWritableDatabase();
        Cursor cur = db.Finn(1);
        String skritt;
        String mal;
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
        RemoteViews oppdater = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.widget_layout);
        oppdater.setTextViewText(R.id.wskritt, a+"");
        Intent klikk = new Intent(context,Widget.class);
        klikk.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        klikk.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,id);
        PendingIntent pending = PendingIntent.getBroadcast(context,0,klikk,PendingIntent.FLAG_UPDATE_CURRENT);
        oppdater.setOnClickPendingIntent(R.id.refresh,pending);
        awm.updateAppWidget(id,oppdater);
        //int i = ((b + 1) - 2500) * 2500;
        oppdater.setTextViewText(R.id.dm, b+"");
        Intent klikk2 = new Intent(context,Widget.class);
        klikk2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        klikk2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,id);
        PendingIntent pending2 = PendingIntent.getBroadcast(context,0,klikk2,PendingIntent.FLAG_UPDATE_CURRENT);
        oppdater.setOnClickPendingIntent(R.id.refresh,pending2);
        awm.updateAppWidget(id,oppdater);

    }
}
