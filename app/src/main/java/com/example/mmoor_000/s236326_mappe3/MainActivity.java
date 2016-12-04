package com.example.mmoor_000.s236326_mappe3;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.Calendar;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;
    Database db;
    private TextView meter;
    private TextView skritt;
    private TextView hoyde;
    private TextView goal;
    Calendar calendar;
    private NumberPicker n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(this);
        db.getWritableDatabase();

        skritt = (TextView) findViewById(R.id.skritt);
        meter = (TextView) findViewById(R.id.meter);
        hoyde = (TextView) findViewById(R.id.hoyde);
        goal = (TextView) findViewById(R.id.goal);
        n = (NumberPicker) findViewById(R.id.daglig);

        db.leggTil(new Info("Morten", 95,184,15,06,1994, 1, 2500, 100, 0));


            Cursor cur = db.Finn(1);
            int k = 1;
            if (cur.moveToFirst())
                do {
                    String test = cur.getString(6);
                    k = Integer.valueOf(test);
                } while (cur.moveToNext());
            cur.close();

            int i = ((k + 1) - 2500) * 2500;
            goal.setText(i + "");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
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
            skritt.setTextColor(Color.YELLOW);
        }
        else if (s >= g) {
            skritt.setTextColor(Color.GREEN);
        }

        if (s==g) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.run )
                            .setContentTitle("Skritt oppdatering").setContentText("Du greide målet!");
            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager nm =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            nm.notify(1, mBuilder.build());
        }

        if (calendar.getTimeInMillis() == ((1000 * 24 * 60 * 60) - 1) &&
                s < g) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.run)
                            .setContentTitle("Skritt oppdatering")
                            .setContentText("Du har ikke nådd ditt daglige mål\n" + "Du hadde " + mangler + " skritt igjen");
            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1, mBuilder.build());
        }
        if (calendar.getTimeInMillis() == ((1000 * 24 * 60 * 60) * 0.75) &&
                s < g) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.run)
                            .setContentTitle("Skritt oppdatering")
                            .setContentText("Du mangler " + mangler + " for å nå målet!");
            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1, mBuilder.build());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }*/

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
