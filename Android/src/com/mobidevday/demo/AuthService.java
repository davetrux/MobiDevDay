package com.mobidevday.demo;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.IOException;

public class AuthService extends IntentService {

    public static final String AUTH_RESULT = "AUTH-RESULT";

    public AuthService() {
        super("AuthService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if ("google-auth".equals(intent.getAction())) {
            authenticateGoogle(intent.getStringExtra("account"));
        }
        else if ("forms-auth".equals(intent.getAction())) {
            getFormsData(intent.getStringExtra("url"), intent.getStringExtra("cookie"));
        }
        else {
            String url = intent.getStringExtra("url");

            if ("oauth-data".equals(intent.getAction())) {
                getOauthData(intent.getStringExtra("token"), url);
            } else if ("basic-data".equals(intent.getAction())) {
                getBasicData(intent.getStringExtra("user"), intent.getStringExtra("password"), url);
            }
        }
    }

    private void getOauthData(String token, String url) {
        WebHelper http = new WebHelper();
        String webResult;
        int result = -1;
        try {
            webResult = http.getHttp(url, token);
            if(!webResult.equalsIgnoreCase("")) {
                result = Activity.RESULT_OK;
            }
        } catch (IOException e) {
            webResult = "";
            Log.d(getClass().getName(), "Exception calling service", e);
        }

        sendResult(webResult, AUTH_RESULT, "oauth-data", result);
    }

    private void getFormsData(String url, String cookie){
        WebHelper http = new WebHelper();
        String webResult;
        int result = -1;
        try {
            webResult = http.getHttp(url, cookie);
            if(!webResult.equalsIgnoreCase("")) {
                result = Activity.RESULT_OK;
            }
        } catch (IOException e) {
            webResult = "";
            Log.d(getClass().getName(), "Exception calling service", e);
        }

        sendResult(webResult, AUTH_RESULT, "forms-data", result);
    }

    private void getBasicData(String user, String password, String url) {
        WebHelper http = new WebHelper();
        String webResult;
        int result = -1;
        try {
            webResult = http.getHttp(url, user, password);
            if(!webResult.equalsIgnoreCase("")) {
                result = Activity.RESULT_OK;
            }
        } catch (IOException e) {
            webResult = "";
            Log.d(getClass().getName(), "Exception calling service", e);
        }

        sendResult(webResult, AUTH_RESULT, "oauth-data", result);
    }


    private void authenticateGoogle(String accountName) {
        String token;
        try {
            Key props = new Key();
            token = GoogleAuthUtil.getToken(this, accountName, props.getGoogleKey(), null);
            sendResult(token, AUTH_RESULT, "AuthToken", Activity.RESULT_OK);
        } catch (IOException e) {
            Log.d("IO error", e.getMessage());
        } catch (GoogleAuthException ge) {
            Log.d("Google auth error", ge.getMessage());
        } catch (Exception ex) {
            Log.d("error", ex.getMessage());
        }
    }

    /*
     * Place the results into an intent and return it to the caller
     */
    private void sendResult(String data, String name, String action, int result) {

        Intent sendBack = new Intent(name);

        sendBack.putExtra("call", action);
        sendBack.putExtra("result", result);
        sendBack.putExtra("data", data);

        //Keep the intent local to the application
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendBack);
    }
}
