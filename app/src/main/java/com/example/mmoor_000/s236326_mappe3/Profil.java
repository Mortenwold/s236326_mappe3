package com.example.mmoor_000.s236326_mappe3;

import android.content.Intent;
import android.database.Cursor;
import java.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Profil extends AppCompatActivity {

    Database db = new Database(this);
    EditText n;
    EditText v;
    EditText h;
    EditText notat;
    DatePicker d;
    TextView r;
    Calendar calendar;

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
        d.setMaxDate(calendar.getTimeInMillis());


        Cursor cur = db.Finn(1);
        if (cur.moveToFirst()) {
            do {
                n.setText(cur.getString(0));
                v.setText(cur.getString(1));
                h.setText(cur.getString(2));
                d.updateDate(Integer.valueOf(cur.getString(5)), Integer.valueOf(cur.getString(4)) - 1, Integer.valueOf(cur.getString(3)));
                r.setText(cur.getString(7));
            } while (cur.moveToNext());
        }
        else
            db.leggTil(new Info("",0,0,1,1,1970,1,2500,1,1));
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
        String navn;
        int vekt;
        int hoyde;
        int aar;
        int maned;
        int dag;
        switch (id) {
            case R.id.save:
                try {
                    navn = n.getText().toString();
                    vekt = Integer.valueOf(v.getText().toString());
                    hoyde = Integer.valueOf(h.getText().toString());
                    aar = d.getYear();
                    maned = d.getMonth() + 1;
                    dag = d.getDayOfMonth();
                    if (n.getText().toString() == "" && h.getText().toString() == "" && v.getText().toString() == "")
                        Toast.makeText(Profil.this, "Skriv noe i alle feltene!", Toast.LENGTH_LONG).show();
                    else {
                        db.Oppdater(navn, vekt, hoyde, dag, maned, aar);
                        startActivity(new Intent(Profil.this, MainActivity.class));
                    }
                }
                catch (NumberFormatException nfe) {
                    Toast.makeText(Profil.this, "Kan ikke skrive vekt eller høyde med tekst!", Toast.LENGTH_LONG).show();
                }
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
