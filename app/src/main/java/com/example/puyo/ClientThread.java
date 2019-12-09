package com.example.puyo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{

    private static String GIVEN_SERVER;
    private static int GIVEN_PORT;
    private static final int TIMEOUT = 5000;

    private Socket mSocket;
    private boolean mShouldStop;

    public ClientThread(String addr, int port){
        GIVEN_SERVER = addr;
        GIVEN_PORT = port;

        mSocket = null;
        mShouldStop = true;
    }

    @Override
    public void run(){
        try {
            Log.d("CLIENT THREAD", "connecting to " + GIVEN_SERVER + ":" + GIVEN_PORT);
            mSocket = new Socket(GIVEN_SERVER, GIVEN_PORT);
            mSocket.setSoTimeout(TIMEOUT);

            final BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            final PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);

            Log.d("CLIENT THREAD", "connected?" + mSocket.isConnected());

            String recv;
            mShouldStop = false;

            while (!mShouldStop){
                Log.d("CLIENT THREAD", "write to server");
                out.write(mSocket.getLocalAddress() + "\n");
                out.flush();
                Log.d("CLIENT THREAD", "read from server");
                recv = in.readLine();
                Log.d("CLIENT THREAD","--recv--> " + recv);
                if(recv == null){
                    break;
                }

                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (mSocket != null){
                try{
                    mSocket.close();
                } catch (IOException e){

                }
            }
            mSocket = null;
            mShouldStop = true;
        }
    }

    public void closeConnection() throws IOException{
        mShouldStop = true;
        if (mSocket != null){
            mSocket.close();
        }
        mSocket = null;
    }
}
