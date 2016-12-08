package com.example.mmoor_000.s236326_mappe3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;


public class Notat extends AppCompatActivity {
    TextView n;
    ScrollView s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notater);
        n = (TextView) findViewById(R.id.notater);
        s = (ScrollView) findViewById(R.id.scroll);
        n.setText(Fil.ReadFile(Notat.this));

        s.post(new Runnable() {

            @Override
            public void run() {
                s.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }
}
