package com.mobidevday.demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.*;
import com.mobidevday.demo.AuthService;
import com.mobidevday.demo.R;

public class Windows extends BaseActivity {

    private Button mLogin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        String title = getIntent().getStringExtra("title");

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText(title);

        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(loginListener);

        mPersonList = (ListView) findViewById(R.id.results);
    }

    private View.OnClickListener loginListener = new View.OnClickListener(){
            public void onClick(View v){

                EditText domainField = (EditText) findViewById(R.id.domain);
                EditText userNameField = (EditText) findViewById(R.id.user_name);
                EditText passwordField = (EditText) findViewById(R.id.password);

                //Send cookie value via intent
                Intent intent = new Intent(Windows.this, AuthService.class);
                intent.setAction("windows-auth");
                intent.putExtra("domain", domainField.getText().toString().trim());
                intent.putExtra("username", userNameField.getText().toString().trim());
                intent.putExtra("password", passwordField.getText().toString().trim());
                intent.putExtra("url", "http://192.168.0.33/mddw/api/names/11");
                startService(intent);
            }
    };
}