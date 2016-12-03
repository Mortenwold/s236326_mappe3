package com.example.mmoor_000.s236326_mappe3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;

/**
 * Created by mmoor_000 on 14-Nov-16.
 */

public class Settings extends AppCompatActivity {

    Database db = new Database(this);
    NumberPicker np;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        np = (NumberPicker) findViewById(R.id.daglig);
        np.setMinValue(2500);
        np.setMaxValue(20000);
        np.setWrapSelectorWheel(false);

        String[] nums = new String[7];
        int teller = 0;
        for(int i=0; i<nums.length; i++) {
            teller += 2500;
            nums[i] = Integer.toString(teller);
        }
        np.setDisplayedValues(nums);


        Cursor cur = db.Finn(1);

        if (cur.moveToFirst())
            do {
                String test = cur.getString(6);
                int j = Integer.valueOf(test);
                np.setValue(j);
            } while (cur.moveToNext());
        cur.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mal:
                db.Daglig(np.getValue());
                startActivity(new Intent(Settings.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
