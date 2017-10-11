package com.example.chuck_csp.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    protected static final String FILE_NAME = "com.example.app";
    private static final String KEY_EMAIL = "Default email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME,"In onCreate()");

        final SharedPreferences sharedP = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        final EditText editText = (EditText)findViewById(R.id.editText);
        final SharedPreferences.Editor editor = sharedP.edit();

        editText.setText(sharedP.getString(KEY_EMAIL, "email@domain.com"));

        Button button = (Button)findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(KEY_EMAIL, editText.getText().toString());
                editor.commit();

                Intent getResult = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(getResult);
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onCreate()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }
}
