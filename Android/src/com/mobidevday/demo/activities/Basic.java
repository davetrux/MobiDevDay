package com.mobidevday.demo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.mobidevday.demo.AuthService;
import com.mobidevday.demo.R;

public class Basic extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.basic);

        String title = getIntent().getStringExtra("title");

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText(title);

        Button mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(loginListener);

        mPersonList = (ListView) findViewById(R.id.results);
    }

    private View.OnClickListener loginListener = new View.OnClickListener(){
            public void onClick(View v){
                //Hide the keyboard
                InputMethodManager inputManager = (InputMethodManager)
                                                  getSystemService(INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                     InputMethodManager.HIDE_NOT_ALWAYS);

                EditText userNameField = (EditText) findViewById(R.id.user_name);
                EditText passwordField = (EditText) findViewById(R.id.password);

                //Send credentials via intent
                Intent intent = new Intent(Basic.this, AuthService.class);
                intent.setAction("basic-auth");
                intent.putExtra("username", userNameField.getText().toString().trim());
                intent.putExtra("password", passwordField.getText().toString().trim());
                intent.putExtra("url", "http://mobidevdaydetroit.appspot.com/api/names/11");
                startService(intent);
            }
    };
}