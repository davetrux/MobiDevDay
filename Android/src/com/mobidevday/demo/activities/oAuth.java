package com.mobidevday.demo.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.mobidevday.demo.AuthService;
import com.mobidevday.demo.R;


public class oAuth extends BaseActivity {

    private Button mLogin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.oauth);

        String title = getIntent().getStringExtra("title");

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText(title);

        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(loginListener);

        mPersonList = (ListView) findViewById(R.id.results);
    }

    private View.OnClickListener loginListener = new View.OnClickListener(){
            public void onClick(View v){

                String[] accounts = getAccountNames();
                //Send credentials via intent
                Intent intent = new Intent(oAuth.this, AuthService.class);
                intent.setAction("google-auth");
                intent.putExtra("account", accounts[0]);
                intent.putExtra("url", "http://mobidevdaydetroit.appspot.com/api/names/11");
                startService(intent);
            }
    };

    private String[] getAccountNames() {
        try {
            AccountManager mAccountManager = AccountManager.get(this);
            Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            String[] names = new String[accounts.length];
            for (int i = 0; i < names.length; i++) {
                names[i] = accounts[i].name;
            }
            return names;
        }
        catch(Exception ex) {
            Log.d("account error", ex.getMessage());
                return null;
        }
    }
}