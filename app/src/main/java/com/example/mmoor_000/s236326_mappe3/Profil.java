package com.example.mmoor_000.s236326_mappe3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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


        File mappe = new File(path);
        mappe.mkdir();

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

        /*lagre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                File file = new File(path + "/notat.txt");
                String [] lagre = String.valueOf(notat.getText()).split(System.getProperty("line.seperator"));

                notat.setText("");
                Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();
                Save(file, lagre);
            }
        });
        skrive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                File file = new File(path + "/notat.txt");

                String [] sn = Load(file);
                String notat = "";

                for(int i = 0; i < sn.length; i++)
                {
                    notat += sn[i] + System.getProperty("line.seperator");
                }

                notatdata.setText(notat);

            }
        });*/
    }

    public static void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }


    public static String[] Load(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
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
                File file = new File(path + "/notat.txt");
                String [] lagre = String.valueOf(notat.getText()).split(System.getProperty("line.seperator"));

                notat.setText("");

                Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_LONG).show();

                Save(file, lagre);
            case R.id.test2:
                File file2 = new File(path + "/notat.txt");

                String [] sn = Load(file2);
                String notat = "";

                for(int i = 0; i < sn.length; i++)
                {
                    notat += sn[i] + System.getProperty("line.seperator");
                }

                notatdata.setText(notat);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
