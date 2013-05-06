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

    /*
     * Decide which web service is being requested
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        if ("google-auth".equals(intent.getAction())) {
            String token = authenticateGoogle(intent.getStringExtra("account"));
            getOauthData(intent.getStringExtra("url"), token);
        }
        else if ("forms-auth".equals(intent.getAction())) {
            getFormsData(intent.getStringExtra("url"), intent.getStringExtra("cookie"));
        }
        else if ("windows-auth".equals(intent.getAction())) {
            getWindowsData(intent.getStringExtra("url"), intent.getStringExtra("username"), intent.getStringExtra("password"), intent.getStringExtra("domain"));
        }
        else if ("basic-auth".equals(intent.getAction())){
            getBasicData(intent.getStringExtra("url"), intent.getStringExtra("username"), intent.getStringExtra("password"));
        }
    }

    /*
     * oAuth
     */
    private void getOauthData(String url, String token) {
        WebHelper http = new WebHelper();
        String webResult;
        int result = -1;
        try {
            String urlPlusToken = String.format("%s?token=%s", url, token);
            webResult = http.getHttp(urlPlusToken);
            if(!webResult.equalsIgnoreCase("")) {
                result = Activity.RESULT_OK;
            }
        } catch (IOException e) {
            webResult = "";
            Log.d(getClass().getName(), "Exception calling service", e);
        }

        sendResult(webResult, AUTH_RESULT, "oauth-data", result);
    }

    /*
     * Windows Auth
     */
    private void getWindowsData(String url, String userName, String password, String domain){
        WebHelper http = new WebHelper();
        String webResult;
        int result = -1;
        try {
            webResult = http.getHttp(url, userName, password, domain);
            if(!webResult.equalsIgnoreCase("")) {
                result = Activity.RESULT_OK;
            }
        } catch (IOException e) {
            webResult = "";
            Log.d(getClass().getName(), "Exception calling service", e);
        }

        sendResult(webResult, AUTH_RESULT, "windows-data", result);
    }

    /*
     * Forms Auth
     */
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

    /*
     * Basic Auth
     */
    private void getBasicData(String url, String user, String password) {
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

    /*
     * Get Device Token
     */
    private String authenticateGoogle(String accountName) {
        String token = "";
        try {
            Key props = new Key();
            token = GoogleAuthUtil.getToken(this, accountName, props.getGoogleKey(), null);

        } catch (IOException e) {
            Log.d("IO error", e.getMessage());
        } catch (GoogleAuthException ge) {
            Log.d("Google auth error", ge.getMessage());
        } catch (Exception ex) {
            Log.d("error", ex.getMessage());
        }
        return token;
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
