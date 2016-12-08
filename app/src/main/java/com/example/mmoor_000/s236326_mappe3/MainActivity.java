package com.example.mmoor_000.s236326_mappe3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.Calendar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor cs;
    private Sensor ds;
    Database db;
    private TextView meter;
    private TextView skritt;
    private TextView goal;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(this);
        db.getWritableDatabase();

        skritt = (TextView) findViewById(R.id.skritt);
        meter = (TextView) findViewById(R.id.meter);
        goal = (TextView) findViewById(R.id.goal);


            Cursor cur = db.Finn(1);
            int k;
            if (cur.moveToFirst()) {
                do {
                    String test = cur.getString(6);
                    k = Integer.valueOf(test);
                    //int i = ((k + 1) - 2500) * 2500;
                    goal.setText(k + "");
                } while (cur.moveToNext());
            }
            else
                goal.setText("2500");
            cur.close();

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        cs = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        ds = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;
        int value = -1;
        calendar = Calendar.getInstance();


        if (values.length > 0) {
            value = (int) values[0];
        }
        db.setSkritt(value);

        if (calendar.getTimeInMillis() == 1000 * 60 * 60 * 24) {

            db.setSkritt(value);
            Cursor cur = db.Finn(1);
            String test = "";
            int j = 0;
            if (cur.moveToFirst())
                do {
                    test = cur.getString(7);
                    j = Integer.valueOf(test);
                } while (cur.moveToNext());
            if (Integer.valueOf(skritt.getText().toString()) > j) {
                db.setRekord(j);
            }
            cur.close();
            value = -1;
        }

        Cursor hoy = db.Finn(1);
        int j = 1;
        if (hoy.moveToFirst())
            do {
                String test = hoy.getString(2);
                j = Integer.valueOf(test);
            } while (hoy.moveToNext());
        hoy.close();

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            double m = j*0.45*value/100;
            int me = (int) m;
            skritt.setText("" + value);
            meter.setText(me+"");
            Cursor cur = db.Finn(1);
            String test = "";
            int a = 0;
            if (cur.moveToFirst())
                do {
                    test = cur.getString(7);
                    a = Integer.valueOf(test);
                } while (cur.moveToNext());
            cur.close();
            if (Integer.valueOf(skritt.getText().toString()) > a) {
                db.setRekord(Integer.valueOf(skritt.getText().toString()));
            }
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            double m = j*0.45*value/100;
            int me = (int) m;
            skritt.setText("" + value);
            meter.setText(me+"");
            Cursor cur = db.Finn(1);
            String test = "";
            int a = 0;
            if (cur.moveToFirst())
                do {
                    test = cur.getString(7);
                    a = Integer.valueOf(test);
                } while (cur.moveToNext());
            if (Integer.valueOf(skritt.getText().toString()) > a) {
                db.setRekord(a);
            }
            cur.close();
        }
        int s = Integer.valueOf(skritt.getText().toString());
        int g = Integer.valueOf(goal.getText().toString());

        int mangler = g-s;

        if(s < g*0.25) {
            skritt.setTextColor(Color.RED);
        }
        else if(s <= g*0.75 && s >= g*0.25)  {
            skritt.setTextColor(Color.parseColor("#ffbf00"));
        }
        else if (s >= g) {
            skritt.setTextColor(Color.parseColor("#00e600"));
        }

        if (s==g) {
            NotificationCompat.Builder not =
                    new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.run )
                            .setContentTitle("Skritt oppdatering").setContentText("Du greide målet!");
            Intent resultat = new Intent(this, MainActivity.class);
            TaskStackBuilder sb = TaskStackBuilder.create(this);
            sb.addParentStack(MainActivity.class);
            sb.addNextIntent(resultat);
            PendingIntent pending =
                    sb.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            not.setContentIntent(pending);
            NotificationManager nm =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            nm.notify(1, not.build());
        }

        if (calendar.getTimeInMillis() == ((1000 * 24 * 60 * 60) - 1) &&
                s < g) {
            NotificationCompat.Builder not =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.run)
                            .setContentTitle("Skritt oppdatering")
                            .setContentText("Du har ikke nådd ditt daglige mål\n" + "Du hadde " + mangler + " skritt igjen");
            Intent resultat = new Intent(this, MainActivity.class);
            TaskStackBuilder sb = TaskStackBuilder.create(this);
            sb.addParentStack(MainActivity.class);
            sb.addNextIntent(resultat);
            PendingIntent pending =
                    sb.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            not.setContentIntent(pending);
            NotificationManager nm =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            nm.notify(1, not.build());
        }
        if (calendar.getTimeInMillis() == ((1000 * 24 * 60 * 60) * 0.75) &&
                s < g) {
            NotificationCompat.Builder not =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.run)
                            .setContentTitle("Skritt oppdatering")
                            .setContentText("Du mangler " + mangler + " for å nå målet!");
            Intent resultat = new Intent(this, MainActivity.class);
            TaskStackBuilder sb = TaskStackBuilder.create(this);
            sb.addParentStack(MainActivity.class);
            sb.addNextIntent(resultat);
            PendingIntent pending =
                    sb.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            not.setContentIntent(pending);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1, not.build());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        sm.registerListener(this, cs,

                SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(this, ds,

                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_forside,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.info:
                startActivity(new Intent(MainActivity.this, Profil.class));
                return true;
            case R.id.instillinger:
                startActivity(new Intent(MainActivity.this, Settings.class));
                return true;
            case R.id.exit:
                this.finish();
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
