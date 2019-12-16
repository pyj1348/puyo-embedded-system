package com.example.puyo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

public class SimpleClientActivity extends AppCompatActivity {
    private static List<String> server_IP = new ArrayList<>(
            Arrays.asList("192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5"));
    private static int server_port = 5000;

    Socket socket1, socket2;
    String client_IP, getIP, s;
    int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.try_to_join);

        new getIP().start();

        ((Button) findViewById(R.id.menu_button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimpleClientActivity.this, MultiActivity.class);
                intent.putExtra("server_IP", "192.168.0.2");
                intent.putExtra("client_IP", client_IP);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimpleClientActivity.this, MultiActivity.class);
                intent.putExtra("server_IP", "192.168.0.3");
                intent.putExtra("client_IP", client_IP);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimpleClientActivity.this, MultiActivity.class);
                intent.putExtra("server_IP", "192.168.0.4");
                intent.putExtra("client_IP", client_IP);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimpleClientActivity.this, MultiActivity.class);
                intent.putExtra("server_IP", "192.168.0.5");
                intent.putExtra("client_IP", client_IP);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button0)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class getIP extends Thread {
        @Override
        public void run() {
            try {
                socket1 = new Socket("192.168.0.1", 80);
                client_IP = socket1.getLocalAddress().getHostAddress();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server_IP.remove(client_IP); // 성공했다고 가정한다. // 복원하기
        }
    }
}