package com.mobidevday.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.mobidevday.demo.activities.Basic;
import com.mobidevday.demo.activities.Form;
import com.mobidevday.demo.activities.Windows;
import com.mobidevday.demo.activities.oAuth;

public class Main extends Activity {

    private Button mFormButton;
    private Button mBasicButton;
    private Button mWindowsButton;
    private Button mDeviceButton;

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

            int id = v.getId();
            Intent intent;

            switch (id){
                case R.id.device:
                    intent = new Intent(Main.this, oAuth.class);
                    intent.putExtra("title", "Device Account");
                    startActivity(intent);
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
}
