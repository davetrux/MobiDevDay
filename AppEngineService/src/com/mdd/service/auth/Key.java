package com.mdd.service.auth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Key {

    private String mGoogleKey;
    private String mAudience;

    public Key() {
        loadProperties();
    }

    public String getGoogleKey() {
        return mGoogleKey;
    }

    public String getAudience(){
        return mAudience;
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream fis;
        try {
            fis = this.getClass().getResourceAsStream("/com/mdd/service/auth/Key.properties");
            properties.load(fis);

            mGoogleKey = properties.getProperty("google");
            mAudience = properties.getProperty("audience");

        } catch (FileNotFoundException e) {
            //Should have actual logging here
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

