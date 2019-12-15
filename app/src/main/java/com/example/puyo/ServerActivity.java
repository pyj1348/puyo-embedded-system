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

public class ServerActivity extends AppCompatActivity {
    private static final int server_port = 5000;
    private ServerSocket server_socket = null;
    private ServerThread server_thread = null;

    private int client_number;
    private int client_check;
    private List<PrintWriter> client_write = new ArrayList<>();
    private Map<String, Integer> client_slot = new HashMap<>();
    private Map<String, Integer> client_score = new HashMap<>();

    int slot = 0;
    int ready = -1;
    boolean start = false;
    String server_IP;
    int died = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_a_room);

        ((Button) findViewById(R.id.menu_button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_a_room(1, 0);
            }
        });

        ((Button) findViewById(R.id.menu_button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ((Button) findViewById(R.id.menu_button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ((Button) findViewById(R.id.menu_button0)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void make_a_room(int client_number, int client_check){
        this.client_number = client_number;
        this.client_check = client_check;

        /*
        Intent intent = new Intent(ServerActivity.this, RoomActivity.class);
        intent.putExtra("client_number", client_number);
        intent.putExtra("slot", slot);
        startActivity(intent);
        */

        //if(server_thread == null){
        server_thread = new ServerThread();
        server_thread.start();
        //}
    }

    private class ServerThread extends Thread {
        boolean[] visited = new boolean[client_number]; // 기본값 false

        @Override
        public void run() {
            if(!start) {
                if (client_check < client_number) {
                    try {
                        server_socket = new ServerSocket();

                        //server_IP = InetAddress.getLocalHost().getHostAddress();
                        Socket Socket = new Socket("192.168.0.1", 80);
                        server_IP = Socket.getLocalAddress().getHostAddress(); // getInetAddress
                        server_socket.bind(new InetSocketAddress(server_IP, server_port));

                        client_slot.put(server_IP, 0);
                        Log.d("server IP", server_IP);

                        while (true) {
                            // 연결 요청이 들어올 때까지 block 상태이다.
                            Socket socket = server_socket.accept();

                            for (int i = 0; i < client_number; i++) { // queue 를 이용할 수도 있다.
                                if (!visited[i]) {
                                    slot = i+1;
                                    visited[i] = true;
                                }
                            }

                            InetSocketAddress client = (InetSocketAddress)socket.getRemoteSocketAddress();
                            String client_IP = client.getAddress().getHostAddress();
                            client_slot.put(client_IP, slot);
                            Log.d("client IP", client_IP);

                            new SocketThread(socket, slot).start();

                            if (++client_check == client_number)
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } /*finally{
                        try{
                            if(server_socket != null && !server_socket.isClosed()){
                                server_socket.close();
                            }
                        } catch(IOException e){
                            e.printStackTrace();
                        }
                    }*/
                } else {
                    if (!server_socket.isClosed()) {
                        try {
                            server_socket.close(); // time-out 을 사용하자?
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else{
                try {
                    Socket socket = new Socket(server_IP, 5050);
                    new SocketThread(socket, 0).start();
                    new ClientActivity.ClientThread(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SocketThread extends Thread {
        Socket socket;
        int slot;

        public SocketThread(Socket socket, int slot) {
            this.socket = socket;
            this.slot = slot;
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
                    s = in.readLine(); // 반환 형식은 String 으로 고정돼있다.
                    // data 입력 전까지 block 상태이며 더이상 없으면 -1을 반환한다.

                    String[] tokens = s.split(":");
                    // if(s == null){ }
                    if(ready == client_number) {
                        if(!start){
                            start = true;
                            // 모든 socket thread 에게 START 명령.
                        }
                        else if(died == client_number) {
                                // Rank 를 종합해서 보내준다.
                                // '나가기'를 활성화한다.
                            break;
                        }
                        else{
                            if("DATA:".equals(tokens[0])){
                                // 직렬화된 Board 들을 서로 주고받는다.
                            }
                            else if("DIED:".equals(tokens[0])) {
                                died++; // 종료
                            }
                        }
                    }
                    else{
                        if("JOIN".equals(tokens[0])) {
                            // out.write("BUILD:"+client_number+":"+slot+"\n");
                            out.flush();
                            // main 이 slot 인 참가자 수가 client_number 인 방을 만들어라.
                            // 각 참가자의 slot 과 IP (nickname)은 client_slot 에서.
                        }
                        else if("EXIT".equals(tokens[0])) {
                            // 참가자에 변동이 생겼으므로 client_write 를 갱신하고
                            // client_slot 도 갱신해서 client 들에게 알려준다.
                        }
                        else if("READY".equals(tokens[0])) {
                            ready++; // 시작
                            // '나가기'를 비활성화한다.
                        }
                    }
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace(); // client 가 연결을 끊었다.
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}