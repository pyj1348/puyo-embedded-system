package com.example.puyo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleServerActivity extends AppCompatActivity {
    private static final int server_port = 5000;

    ServerSocket server_socket;
    Socket Socket;
    String server_IP;
    int players;
    int players_check;
    int died;
    boolean started = false;
    boolean terminated = false;
    private List<PrintWriter> client_write = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_a_room);

        new ServerThread().start(); // 계속 요청만 받는다.

        ((Button) findViewById(R.id.menu_button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players = 2;
                players_check = 1;
                died = 0;
                Intent intent = new Intent(SimpleServerActivity.this, MultiActivity.class);
                intent.putExtra("server_IP", server_IP.toString());
                intent.putExtra("client_IP", server_IP.toString());
                intent.putExtra("number", 2);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players = 3;
                players_check = 1;
                died = 0;
                Intent intent = new Intent(SimpleServerActivity.this, MultiActivity.class);
                intent.putExtra("server_IP", server_IP.toString());
                intent.putExtra("client_IP", server_IP.toString());
                intent.putExtra("number", 3);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players = 4;
                players_check = 1;
                died = 0;
                Intent intent = new Intent(SimpleServerActivity.this, MultiActivity.class);
                intent.putExtra("server_IP", server_IP.toString());
                intent.putExtra("client_IP", server_IP.toString());
                intent.putExtra("number", 4);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.menu_button0)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimpleServerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                InetAddress addr = InetAddress.getByName("192.168.0.1");

                server_socket = new ServerSocket(5000,50,addr);
                Log.d("server", (InetAddress.getLocalHost().toString()));
                server_IP = "192.168.0.1";
                while (true) {
                    // 연결 요청이 들어올 때까지 block 상태이다.
                    Socket socket = server_socket.accept();

                    new SocketThread(socket).start();
                    players_check++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketThread extends Thread {
        Socket socket;

        public SocketThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                final BufferedReader in =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                String s;

                client_write.add(out);

                while (true) {
                    s = in.readLine();
                    String[] tokens = s.split(":");

                    if ("JOIN".equals(tokens[0])) {
                        out.write("NUMBER:" + players + "\n");
                        out.flush();
                    } else if ("DATA".equals(tokens[0])) {
                        synchronized (client_write) {
                            for (PrintWriter start : client_write) {
                                start.write("VIEW:" + tokens[1] + tokens[2] + "\n");
                                start.flush();
                            }
                        }
                    } else if ("DIED".equals(0)) {
                        died++;
                    }

                    if (!started && players_check == players) {
                        synchronized (client_write) {
                            for (PrintWriter start : client_write) {
                                start.write("START:" + "\n");
                                start.flush();
                            }
                        }
                        started = true;
                    } else if (!terminated && died == players) {
                        for (PrintWriter rank : client_write) {
                            //rank.write("RANK:"+"\n");
                            rank.flush();
                        }
                        // 종료절차
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // client 가 연결을 끊었다.
            }
        }
    }
}