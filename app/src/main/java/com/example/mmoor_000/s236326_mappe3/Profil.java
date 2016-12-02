package com.example.mmoor_000.s236326_mappe3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
    TextView notatdata;
    TextView r;
    Context context;
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
        notatdata = (TextView) findViewById(R.id.notatdata);
        r = (TextView) findViewById(R.id.rekord);
        calendar = Calendar.getInstance();

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

    public void Save(View view)
    {
        String note = notat.getText().toString();
        String dato = calendar.get(Calendar.DAY_OF_MONTH) + "." + ((calendar.get(Calendar.MONTH))+1) + "." + calendar.get(Calendar.YEAR);
        String fil = "notat";
        String utskrift = dato + "  " + note;
        try {
            FileOutputStream fileOutputStream = openFileOutput(fil, MODE_PRIVATE);
            fileOutputStream.write(utskrift.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();;
            notat.setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load(View view)
    {
        try {
            String melding = "";
            FileInputStream fileInputStream = openFileInput("notat");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((melding=bufferedReader.readLine()) != null) {
                stringBuffer.append(melding + "\n");
            }
            notatdata.setText(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                /*File file = new File(path + "/notat.txt");
                String [] lagre = String.valueOf(notat.getText()).split(System.getProperty("line.seperator"));

                notat.setText("");

                Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();

                Save(file, lagre);*/

            case R.id.test2:
                /*File file2 = new File(path + "/notat.txt");

                String [] sn = Load(file2);
                String notat = "";

                for(int i = 0; i < sn.length; i++)
                {
                    notat += sn[i] + System.getProperty("line.seperator");
                }

                notatdata.setText(notat);*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
