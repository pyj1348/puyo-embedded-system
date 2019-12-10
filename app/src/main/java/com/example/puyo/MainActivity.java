package com.example.puyo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ///// JNI

    // Used to load the native library on application startup.
    static {
        // displays game mode, player's name
        System.loadLibrary("text_LCD_test");

        // displays player's game score
        System.loadLibrary("7_segment_test");

        // displays other game status, effects
        System.loadLibrary("8_LED_test");
        System.loadLibrary("dot_matrix_test");

        // Left/Right/Up/Down Control Input
        System.loadLibrary("9_button_test");
    }

    public native int LCD_write(String game_mode, String address);
    public native int segment_write(int score);
    public native int LED_write(int combo); // explosion combo
    public native int matrix_write(int signal); // explosion and termination

    public native int button_open();
    public native int button_read();
    public native int botton_close();

    ///// JNI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_of_game);

        ((Button) findViewById(R.id.btnStartServer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SimpleServerActivity.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.btnStartClient)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SimpleClientActivity.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.btnSinglePlay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Room.class);
                intent.putExtra("player1", 0);
                startActivity(intent);
            }
        });

        ///// JNI

        LCD_write("3 players ", "165.194.1.1");
        segment_write(1010);
        LED_write(210);
        matrix_write(0);

        //ExampleThread thread = new ExampleThread();
        //thread.start();

        // botton_close();

        ///// JNI
    }
    /*
    private class ExampleThread extends Thread {
        private static final String TAG = "ExampleThread";

        public ExampleThread() { // initialization
            button_open();
        }

        public void run() {
            int direction;
            while (true){
                direction = button_read();
                if(direction == 1){
                    board.spin(); //printf("LEFT ROTATE\n");
                    drawMyBoard();
                } else if(direction == 2) {
                    board.move_left(); //printf("LEFT MOVE\n");
                    drawMyBoard();
                } else if(direction == 4) {
                    board.move_right(); //printf("RIGHT MOVE\n");
                    drawMyBoard();
                } else if(direction == 8) {
                    board.spin(); //printf("RIGHT ROTATE\n");
                    drawMyBoard();
                }
            }
        }
    }*/
}