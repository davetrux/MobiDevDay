package com.mobidevday.demo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.mobidevday.demo.R;

public class Form extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        String title = getIntent().getStringExtra("title");

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText(title);
    }
}