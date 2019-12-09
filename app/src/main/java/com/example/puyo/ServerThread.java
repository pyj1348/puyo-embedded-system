package com.example.puyo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
    private static int GIVEN_PORT;
    private ServerSocket mServerSocket;

    public ServerThread(int port) { GIVEN_PORT = port;}

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

    private static class SimpleWorkerThread extends Thread {
        private Socket mConnection;

        SimpleWorkerThread(Socket mConnection) {
            this.mConnection = mConnection;
        }

        @Override
        public void run(){
            try {
                final BufferedReader in = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));
                final PrintWriter out = new PrintWriter(mConnection.getOutputStream(), true);

                String s;
                while ((s = in.readLine()) != null){
                    Log.d("SERVER WORKER THREAD", "client" + s +" sent data");
                    out.write("hello" + s + "\n");
                    out.flush();
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    mConnection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
