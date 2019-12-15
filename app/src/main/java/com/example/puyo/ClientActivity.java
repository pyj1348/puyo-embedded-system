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

public class ClientActivity extends AppCompatActivity {
    private static List<String> server_IP = new ArrayList<>(
            Arrays.asList("192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5"));
    private static int server_port = 5000;

    private String client_IP;
    private BufferedReader in;
    private PrintWriter out;
    private String s;
    private ClientThread client_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.try_to_join);

        try {
            //client_IP = InetAddress.getLocalHost().getHostAddress();
            Socket Socket = new Socket("192.168.0.1", 80);
            client_IP = Socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server_IP.remove(client_IP); // 성공했다고 가정

        ((Button) findViewById(R.id.menu_button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try_to_join(0);
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

    private void try_to_join(int order){ // 만약 틀렸다면?
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(server_IP.get(order), 5000));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.write("JOIN:" + "\n");
            out.flush();

            while (true) {
                s = in.readLine();
                String[] tokens = s.split(":");
                if ("BUILD:".equals(tokens[0])) {
                    // client_number, client_slot 정보를 바탕으로 game 을 생성한다.
                    /*
                    Intent intent = new Intent(ClientActivity.this, RoomActivity.class);
                    intent.putExtra("client_number", client_number);
                    intent.putExtra("slot", slot);
                    startActivity(intent);
                    */
                    break;
                }
            }
            client_thread = new ClientThread(socket);
            client_thread.start();

            // READY or EXIT 는 Button 내부에서 out.flush.
            // START 이후로는 Device 입력마다 갱신된 Board 를 out.flush.

            // client_thread.stop();

        } catch (IOException e) {
            e.printStackTrace();
        }/*finally {
            try{
                if( socket != null && !socket.isClosed()){
                    socket.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }*/
    }

    public static class ClientThread extends Thread { // public class -> java
        /*
        private static String GIVEN_SERVER;
        private static int GIVEN_PORT;
        private static final int TIMEOUT = 5000;
        */
        private Socket socket;
        final private BufferedReader in;
        final private PrintWriter out;
        private String data;

        /*
        private boolean ShouldStop;

        public ClientThread(String server_IP, int port){
            GIVEN_SERVER = server_IP;
            GIVEN_PORT = port;

            socket = null;
            ShouldStop = true;
        }
        */
        public ClientThread(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run(){
            try {
                /*
                socket = new Socket(GIVEN_SERVER, GIVEN_PORT);
                socket.setSoTimeout(TIMEOUT);

                ShouldStop = false;

                while (!ShouldStop) {
                    out.write(socket.getLocalAddress() + "\n");
                    out.flush();
                    s = in.readLine();
                    if (s == null) {
                        break;
                    }
                    Thread.sleep(1000);
                    }
                }
                */
                while(true) {
                    // Board data 를 view 하기.
                    data = in.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }/* catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (socket != null){
                    try{
                        socket.close();
                    } catch (IOException e){
                        // ignore
                    }
                }
                socket = null;
                ShouldStop = true;
            }*/
        }
        /*
        public void close_connection() throws IOException{
            ShouldStop = true;
            if (socket != null){
                socket.close();
            }
            socket = null;
        }
        */

        private void send_data(){
            // 어떻게 data 를 가져와서
            out.write("DATA:" + data + "\r\n");
            out.flush();
        }
    }
}


