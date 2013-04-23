package com.mobidevday.demo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.mobidevday.demo.activities.Basic;
import com.mobidevday.demo.activities.Form;
import com.mobidevday.demo.activities.Windows;

import java.util.ArrayList;

public class Main extends Activity {

    private AccountManager mAccountManager;
    private String mToken;
    private Button mFormButton;
    private Button mBasicButton;
    private Button mWindowsButton;
    private Button mDeviceButton;
    private ListView mPersonList;
    private ArrayList<Person> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mFormButton = (Button) findViewById(R.id.forms);
        mFormButton.setOnClickListener(accountListener);

        mBasicButton = (Button) findViewById(R.id.basic);
        mBasicButton.setOnClickListener(accountListener);

        mWindowsButton = (Button) findViewById(R.id.windows);
        mWindowsButton.setOnClickListener(accountListener);

        mDeviceButton = (Button) findViewById(R.id.device);
        mDeviceButton.setOnClickListener(accountListener);

    }

    private View.OnClickListener accountListener = new View.OnClickListener(){
        public void onClick(View v){

            if(!WebHelper.isOnline(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Not currently online", Toast.LENGTH_SHORT).show();
                return;
            }
            String action;
            int id = v.getId();
            Intent intent;

            switch (id){
                case R.id.device:
                    //Send an intent to the service to get data
                    String[] accounts = getAccountNames();
                    intent = new Intent(Main.this, AuthService.class);
                    intent.setAction("google-auth");
                    intent.putExtra("account", accounts[0]);
                    startService(intent);
                    break;
                case R.id.basic:
                    intent = new Intent(Main.this, Basic.class);
                    intent.putExtra("title", "HTTP Basic");
                    startActivity(intent);
                    break;
                case R.id.forms:
                    intent = new Intent(Main.this, Form.class);
                    intent.putExtra("title", "Web Form");
                    startActivity(intent);
                    break;
                case R.id.windows:
                    intent = new Intent(Main.this, Windows.class);
                    intent.putExtra("title", "Windows");
                    startActivity(intent);
                    break;
            }
        }
    };

    private View.OnClickListener oauthListener = new View.OnClickListener(){
        public void onClick(View v){

            if(WebHelper.isOnline(getApplicationContext())) {
                //Send an intent to the service to get data
                Intent intent = new Intent(Main.this, AuthService.class);
                intent.setAction("oauth-data");
                intent.putExtra("token", mToken);
                intent.putExtra("url", "http://localhost:8080/api/names/20");
                startService(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Not currently online", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener basicListener = new View.OnClickListener(){
        public void onClick(View v){

            if(WebHelper.isOnline(getApplicationContext())) {
                //Send an intent to the service to get data
                Intent intent = new Intent(Main.this, AuthService.class);
                intent.setAction("basic-data");
                intent.putExtra("user", "mdd");
                intent.putExtra("password", "password123");
                intent.putExtra("url", "http://localhost:8080/api/names/20");
                startService(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Not currently online", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private String[] getAccountNames() {
        try {
            mAccountManager = AccountManager.get(this);
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
    /*
     * Hookup the BroadcastManager to listen to service returns
     */
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(AuthService.AUTH_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(onAuthenticate, filter);

    }

    /*
     * The listener that responds to intents sent back from the service
     */
    private BroadcastReceiver onAuthenticate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int serviceResult = intent.getIntExtra("result", -1);
            if (serviceResult == RESULT_OK) {
                String call = intent.getStringExtra("call");

                if(call.equalsIgnoreCase("AuthToken")) {
                    mToken = intent.getStringExtra("data");
                    Toast.makeText(Main.this, "Authenticate call succeeded.", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(Main.this, "Rest call failed.", Toast.LENGTH_LONG).show();
            }

            Log.d("BroadcastReceiver", "onReceive called");
        }
    };
}
