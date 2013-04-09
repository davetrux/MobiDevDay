package com.mobidevday.demo;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Key {

    private String mGoogleKey;

    public Key() {
        loadProperties();
    }

    public String getGoogleKey() {
        return mGoogleKey;
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream fis;
        try {
            fis = this.getClass().getResourceAsStream("/com/mobidevday/demo/Key.properties");
            properties.load(fis);

            mGoogleKey = properties.getProperty("google");

        } catch (FileNotFoundException e) {
            Log.d("Property File Missing", Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Property File Load Error", Arrays.toString(e.getStackTrace()));
        }

    }
}
