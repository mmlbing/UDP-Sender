package com.example.s.udpsender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    String ipStr, portStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText dataSend = (EditText) findViewById(R.id.TxEdit);   //设置EditText的按键监控
        dataSend.setOnKeyListener(onKey);

        SharedPreferences read = getSharedPreferences("Setting", MODE_PRIVATE);
        ipStr = read.getString("IP", "");
        portStr = read.getString("Port", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent intent = new Intent(this, settingActivity.class); //启动设置界面
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    View.OnKeyListener onKey = new View.OnKeyListener() {           //按键监控
       // private long exitTime = 0;
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    if( event.getAction() == KeyEvent.ACTION_DOWN ) {
                        EditText editTextData = (EditText) findViewById(R.id.TxEdit);
                        final String dataStr = editTextData.getText().toString();
                        if (dataStr.length() > 0) {
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        txUDP(ipStr, Integer.parseInt(portStr), dataStr);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                            sentDataShowFresh(dataStr);        //刷新已发送数据的显示
                        } else
                            Toast.makeText(getApplicationContext(), "No data to send!", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                default:
                    return false;
            }
        }
    };

    private void txUDP(String destip, int port, String txdata) throws IOException {
        InetAddress address = InetAddress.getByName(destip);
        byte[] data = txdata.getBytes();
        // 2.创建数据报，包含发送的数据信息
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        // 3.创建DatagramSocket对象
        DatagramSocket socket = new DatagramSocket();
        // 4.向服务器端发送数据报
        socket.send(packet);
        // 5.关闭资源
        socket.close();
    }
    private void sentDataShowFresh(String thisData) {
        TextView sentData = (TextView) findViewById(R.id.dataSentTextView);
        String oldData = sentData.getText().toString();
        if(oldData.length() != 0) {
        oldData = oldData.concat("\n");
        oldData = oldData.concat(thisData);
        }
        else {
            oldData = thisData;
        }
        sentData.setText(oldData);
    }
}
