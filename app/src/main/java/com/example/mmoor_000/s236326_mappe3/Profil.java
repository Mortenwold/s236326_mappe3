package com.example.mmoor_000.s236326_mappe3;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.File;
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
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        n = (EditText) findViewById(R.id.navn);
        v = (EditText) findViewById(R.id.vekt);
        h = (EditText) findViewById(R.id.hoyde);
        d = (DatePicker) findViewById(R.id.dato);
        notat = (EditText) findViewById(R.id.notat);
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
                writeToFile(notat.getText().toString());
                startActivity(new Intent(Profil.this, MainActivity.class));
                return true;
            case R.id.test:
                notat.setText(readFromFile());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void writeToFile(String data) {
        //File path = context.getFilesDir();
        //File file = new File(path, "notat.txt");
        /*try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("notat.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }*/
        /*FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write("text-to-write".getBytes());
        } finally {
            stream.close();
        }*/
        File f = new File("test.txt");

        String nameToWrite = fn;
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(f);
            outStream.write(nameToWrite.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("notat.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
