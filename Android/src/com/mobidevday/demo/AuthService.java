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

    public static final String AUTH_RESULT="AUTH-RESULT";

    public AuthService(){
        super("AuthService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if("google-auth".equals(intent.getAction())){
            authenticateGoogle(intent.getStringExtra("account"));
        }
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
        } catch(Exception ex) {
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
