package com.example.s.udpsender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private Button buttonSend;
    private EditText editTextIP, editTextPort, editTextTx;
    private String ipStr, dataStr, portStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        editTextIP =  (EditText) findViewById(R.id.EditTextIP);
        editTextPort =  (EditText) findViewById(R.id.EditTextPort);
        editTextTx =  (EditText) findViewById(R.id.EditTextTx);
        editTextIP.setText("219.230.110.234");

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean readytoSend = true;
                ipStr = editTextIP.getText().toString();
                dataStr = editTextTx.getText().toString();
                portStr = editTextPort.getText().toString();

                if (!ipStr.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}")) {
                    Toast.makeText(getApplicationContext(), "Error Address!", Toast.LENGTH_SHORT).show();
                    readytoSend = false;
                }
                if (!portStr.matches("[0-9]{1,5}")) {
                    Toast.makeText(getApplicationContext(), "Error PortNumber!", Toast.LENGTH_SHORT).show();
                    readytoSend = false;
                }
                if(dataStr.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Nothing to Send!", Toast.LENGTH_SHORT).show();
                    readytoSend = false;
                }

                if(readytoSend) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                txUDP(ipStr, Integer.parseInt( portStr ), dataStr);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    Toast.makeText(getApplicationContext(), "Sent.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
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
}
