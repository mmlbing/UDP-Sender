package com.example.s.udpsender;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class settingActivity extends AppCompatActivity {
    boolean configRight = true;
    EditText editTextIP, editTextPort;
    String ipStr, portStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        editTextIP =  (EditText) findViewById(R.id.EditTextIP);
        editTextPort =  (EditText) findViewById(R.id.EditTextPort);

        SharedPreferences read = getSharedPreferences("Setting", MODE_PRIVATE);
        ipStr = read.getString("IP", "");
        portStr = read.getString("Port", "");
        editTextIP.setText(ipStr);
        editTextPort.setText(portStr);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_setting_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if( saveSetting() ) {
                    //Intent intent = new Intent(this, MainActivity.class); //启动设置界面//!!不需要这样，直接结束当前activity即可返回前一activity
                    //startActivity(intent);
                    settingActivity.this.finish();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean saveSetting() {
        ipStr = editTextIP.getText().toString();
        portStr = editTextPort.getText().toString();

        if (!ipStr.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}")) {
            Toast.makeText(getApplicationContext(), "Error Address!", Toast.LENGTH_SHORT).show();
            configRight = false;
        }
        if (!portStr.matches("[0-9]{1,5}")) {
            Toast.makeText(getApplicationContext(), "Error PortNumber!", Toast.LENGTH_SHORT).show();
            configRight = false;
        }
        if(configRight) {
            SharedPreferences.Editor editor = getSharedPreferences("Setting", MODE_PRIVATE).edit();
            editor.putString("IP", ipStr);
            editor.putString("Port", portStr);
            editor.apply();
            return true;
        }
        return false;
    }
}
