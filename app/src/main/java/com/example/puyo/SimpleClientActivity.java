package com.example.puyo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class SimpleClientActivity extends AppCompatActivity {
    private static String[] GIVEN_SERVER;
    private static int[] GIVEN_PORT = {8080, 8081, 8082};

    private Thread mClientThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.try_to_join);

        /// GIVEN_SERVER = 전체 IP 목록
        /// 본인 IP
        /// 본인 IP 제외한 나머지 목록 try_to_join 에서 출력?
        /// 반복문으로
        /// 3개의 다른 IP 마다
        /// 3개의 PORT 를 순차적으로 접속 시도

    }

    private void client_do_undo(String addr, int port){
        if(mClientThread == null){
            mClientThread = new SimpleClientThread(addr, port);
            mClientThread.start();
        } else {
            try {
                ((SimpleClientThread) mClientThread).closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mClientThread = null;
        }
    }

    private static class SimpleClientThread extends Thread {

        private static String GIVEN_SERVER;
        private static int GIVEN_PORT;
        private static final int TIMEOUT = 5000;

        private Socket mSocket;
        private boolean mShouldStop;

        public SimpleClientThread(String addr, int port){
            GIVEN_SERVER = addr;
            GIVEN_PORT = port;

            mSocket = null;
            mShouldStop = true;
        }

        @Override
        public void run(){
            try {
                mSocket = new Socket(GIVEN_SERVER, GIVEN_PORT);
                mSocket.setSoTimeout(TIMEOUT);

                final BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                final PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);

                String s;
                mShouldStop = false;

                while (!mShouldStop) {
                    out.write(mSocket.getLocalAddress() + "\n");
                    out.flush();
                    s = in.readLine();
                    if (s == null) {
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
                        // ignore
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

    public static String getIPaddress(boolean useIPv4){
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%');
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return "";
    }
}