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
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleServerActivity extends AppCompatActivity {
    private static int[] PORT = {8080, 8081, 8082};
    private int players;
    private int check_players = 1;

    private Thread mServerThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_a_room);

        ((Button) findViewById(R.id.menu_button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players = 2;
                server_do_undo(8080);
            }
        });

        ((Button) findViewById(R.id.menu_button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players = 3;
                server_do_undo(8080);
                server_do_undo(8081);
            }
        });

        ((Button) findViewById(R.id.menu_button4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players = 4;
                server_do_undo(8080);
                server_do_undo(8081);
                server_do_undo(8082);
            }
        });

        ((Button) findViewById(R.id.menu_button0)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// garbage collection
                Intent intent = new Intent(SimpleServerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void server_do_undo(int PORT) {
        if(mServerThread == null) {
            mServerThread = new SimpleServerThread(PORT);
            mServerThread.start();
            setLocalIPAddress();
        }
        else {
            try {
                if (mServerThread != null) {
                    ((SimpleServerThread) mServerThread).closeServer();
                }
                mServerThread = null;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //
            }
        }
    }

    private void setLocalIPAddress() {
        new Thread() {
            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket("192.168.0.1", 80);
                    final Socket finalSocket = socket;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //
                        }
                    });
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //
    }
    private static class SimpleServerThread extends Thread {
        private static int GIVEN_PORT;
        private ServerSocket mServerSocket;

        public SimpleServerThread(int port){GIVEN_PORT = port;}

        @Override
        public void run() {
            try{
                mServerSocket = new ServerSocket(GIVEN_PORT);
                while (true){
                    Socket connection = mServerSocket.accept();
                    Thread worker = new SimpleWorkerThread(connection);
                    worker.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void closeServer() throws IOException{
            if(mServerSocket != null && !mServerSocket.isClosed()){
                mServerSocket.close();
            }
            mServerSocket = null;
        }

        private static class SimpleWorkerThread extends Thread{
            private Socket mConnection;

            SimpleWorkerThread(Socket mConnection) {this.mConnection = mConnection;}

            @Override
            public void run(){
                try{
                    final BufferedReader in = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));
                    final PrintWriter out = new PrintWriter(mConnection.getOutputStream(), true);

                    String s;
                    while ((s = in.readLine())!=null){
                        // out.write();
                        out.flush();
                    }

                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        mConnection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
