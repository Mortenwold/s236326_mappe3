package com.example.mmoor_000.s236326_mappe3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

public class Profil extends AppCompatActivity {

    Database db = new Database(this);
    EditText n;
    EditText v;
    EditText h;
    DatePicker d;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        n = (EditText) findViewById(R.id.navn);
        v = (EditText) findViewById(R.id.vekt);
        h = (EditText) findViewById(R.id.hoyde);
        d = (DatePicker) findViewById(R.id.dato);
        Cursor cur = db.Finn(1);
        if (cur.moveToFirst())
        do {
            n.setText(cur.getString(0));
            v.setText(cur.getString(1));
            h.setText(cur.getString(2));
            d.updateDate(Integer.valueOf(cur.getString(5)), Integer.valueOf(cur.getString(4)) - 1, Integer.valueOf(cur.getString(3)));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
