package com.mobidevday.demo;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Activity {

    private AccountManager mAccountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button deviceAuth = (Button) findViewById(R.id.device);

        deviceAuth.setOnClickListener(accountListener);

    }

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

    private View.OnClickListener accountListener = new View.OnClickListener(){
        public void onClick(View v){
            String[] accounts = getAccountNames();

            if(WebHelper.isOnline(getApplicationContext())) {
                //Send an intent to the service to get data
                Intent intent = new Intent(Main.this, AuthService.class);
                intent.setAction("google-auth");
                intent.putExtra("account", accounts[0]);
                startService(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Not currently online", Toast.LENGTH_SHORT).show();
            }
        }
    };

        /*
     * Hookup the BroadcastManager to listen to service returns
     */
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(AuthService.AUTH_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
    }

        /*
     * The listener that responds to intents sent back from the service
     */
    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int serviceResult = intent.getIntExtra("result", -1);
            if (serviceResult == RESULT_OK) {
                Toast.makeText(Main.this, "Rest call succeeded.", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(Main.this, "Rest call failed.", Toast.LENGTH_LONG).show();
            }

            Log.d("BroadcastReciever", "onReceive called");
        }
    };
}
