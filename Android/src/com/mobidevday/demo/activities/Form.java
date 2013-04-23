package com.mobidevday.demo.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobidevday.demo.*;

import java.util.ArrayList;

public class Form extends Activity {

    private WebView mWeb;
    private Button mCallService;
    private ListView mPersonList;
    private ArrayList<Person> mData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);

        String title = getIntent().getStringExtra("title");

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText(title);

        mPersonList = (ListView) findViewById(R.id.results);

        mCallService = (Button) findViewById(R.id.callService);
        mCallService.setOnClickListener(doneListener);

        mWeb = (WebView) findViewById(R.id.web_view);

        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view, url);
                }

            });

        CookieManager.getInstance().removeSessionCookie();

        mWeb.loadUrl("http://192.168.0.33/mddf/");
    }

    private View.OnClickListener doneListener = new View.OnClickListener(){
            public void onClick(View v){
                CookieManager cookieManager = CookieManager.getInstance();
                String url = mWeb.getUrl();
                final String cookie = cookieManager.getCookie(url);

                //Send cookie value via intent
                Intent intent = new Intent(Form.this, AuthService.class);
                intent.setAction("forms-auth");
                intent.putExtra("cookie", cookie);
                intent.putExtra("url", "http://192.168.0.33/mddf/api/names/11");
                startService(intent);

                RelativeLayout parent = (RelativeLayout) findViewById(R.id.parentContainer);
                cookieManager.removeAllCookie();
                parent.removeView(mWeb);
            }
    };

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
                String json = intent.getStringExtra("data");
                Gson parser = new Gson();
                mData = parser.fromJson(json, new TypeToken<ArrayList<Person>>(){}.getType());

                BindPersonList();

            } else {
                Toast.makeText(Form.this, "Rest call failed.", Toast.LENGTH_LONG).show();
            }

            Log.d("BroadcastReceiver", "onReceive called");
        }
    };

        /*
     * Helper method to put the list of persons into the ListView
     */
    private void BindPersonList() {
        PersonAdapter adapter = new PersonAdapter(Form.this, mData);
        mPersonList.setAdapter(adapter);
    }
}