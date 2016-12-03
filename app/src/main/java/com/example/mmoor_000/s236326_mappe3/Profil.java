package com.example.mmoor_000.s236326_mappe3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.io.File;
import java.util.Calendar;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Profil extends AppCompatActivity {

    Database db = new Database(this);
    EditText n;
    EditText v;
    EditText h;
    EditText notat;
    DatePicker d;
    TextView r;
    Calendar calendar;

    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Notater";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        n = (EditText) findViewById(R.id.navn);
        v = (EditText) findViewById(R.id.vekt);
        h = (EditText) findViewById(R.id.hoyde);
        d = (DatePicker) findViewById(R.id.dato);
        notat = (EditText) findViewById(R.id.notat);
        r = (TextView) findViewById(R.id.rekord);
        calendar = Calendar.getInstance();
        notat.setText("");

        Cursor cur = db.Finn(1);
        if (cur.moveToFirst())
        do {
            n.setText(cur.getString(0));
            v.setText(cur.getString(1));
            h.setText(cur.getString(2));
            d.updateDate(Integer.valueOf(cur.getString(5)), Integer.valueOf(cur.getString(4)) - 1, Integer.valueOf(cur.getString(3)));
            r.setText(cur.getString(7));
        }while (cur.moveToNext());
        cur.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_info,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save:
                String navn = n.getText().toString();
                int vekt = Integer.valueOf(v.getText().toString());
                int hoyde = Integer.valueOf(h.getText().toString());
                int aar = d.getYear();
                int maned = d.getMonth()+1;
                int dag = d.getDayOfMonth();
                db.Oppdater(navn,vekt,hoyde,dag,maned,aar);
                startActivity(new Intent(Profil.this, MainActivity.class));
                return true;
            case R.id.test:
                String dato = calendar.get(Calendar.DAY_OF_MONTH) + "." + ((calendar.get(Calendar.MONTH))+1) + "." + calendar.get(Calendar.YEAR);
                    if (Fil.saveToFile(dato + ":" + "\t\t\t" + notat.getText().toString())) {
                        Toast.makeText(Profil.this, "Notat er lagret!", Toast.LENGTH_SHORT).show();
                        notat.setText("");
                    } else {
                        Toast.makeText(Profil.this, "Greide ikke skrive til fil!", Toast.LENGTH_SHORT).show();
                    }
                return true;

            case R.id.test2:
                startActivity(new Intent(Profil.this, Notat.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
