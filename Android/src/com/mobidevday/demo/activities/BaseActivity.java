package com.mobidevday.demo.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobidevday.demo.AuthService;
import com.mobidevday.demo.Person;
import com.mobidevday.demo.PersonAdapter;

import java.util.ArrayList;

public class BaseActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected ListView mPersonList;
    protected ArrayList<Person> mData;

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
                String json = intent.getStringExtra("data");
                Gson parser = new Gson();
                mData = parser.fromJson(json, new TypeToken<ArrayList<Person>>(){}.getType());

                BindPersonList(context);

            } else {
                Toast.makeText(context, "Rest call failed.", Toast.LENGTH_LONG).show();
            }

            Log.d("BroadcastReceiver", "onReceive called");
        }
    };

    /*
     * Helper method to put the list of persons into the ListView
     */
    private void BindPersonList(Context context) {
        PersonAdapter adapter = new PersonAdapter(context, mData);
        mPersonList.setAdapter(adapter);
    }
}