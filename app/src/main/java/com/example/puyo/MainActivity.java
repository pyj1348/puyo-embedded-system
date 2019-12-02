package com.example.puyo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private Button up, down, left, right;
    private Board board;
    private ImageView[][] board_Image;

    private int[][] idArray = {
            {R.id.iv00, R.id.iv01, R.id.iv02, R.id.iv03, R.id.iv04, R.id.iv05, R.id.iv06, R.id.iv07},
            {R.id.iv08, R.id.iv09, R.id.iv10, R.id.iv11, R.id.iv12, R.id.iv13, R.id.iv14, R.id.iv15},
            {R.id.iv16, R.id.iv17, R.id.iv18, R.id.iv19, R.id.iv20, R.id.iv21, R.id.iv22, R.id.iv23},
            {R.id.iv24, R.id.iv25, R.id.iv26, R.id.iv27, R.id.iv28, R.id.iv29, R.id.iv30, R.id.iv31},
            {R.id.iv32, R.id.iv33, R.id.iv34, R.id.iv35, R.id.iv36, R.id.iv37, R.id.iv38, R.id.iv39},
            {R.id.iv40, R.id.iv41, R.id.iv42, R.id.iv43, R.id.iv44, R.id.iv45, R.id.iv46, R.id.iv47},
            {R.id.iv48, R.id.iv49, R.id.iv50, R.id.iv51, R.id.iv52, R.id.iv53, R.id.iv54, R.id.iv55},
            {R.id.iv56, R.id.iv57, R.id.iv58, R.id.iv59, R.id.iv60, R.id.iv61, R.id.iv62, R.id.iv63},
            {R.id.iv64, R.id.iv65, R.id.iv66, R.id.iv67, R.id.iv68, R.id.iv69, R.id.iv70, R.id.iv71},
            {R.id.iv72, R.id.iv73, R.id.iv74, R.id.iv75, R.id.iv76, R.id.iv77, R.id.iv78, R.id.iv79},
            {R.id.iv80, R.id.iv81, R.id.iv82, R.id.iv83, R.id.iv84, R.id.iv85, R.id.iv86, R.id.iv87},
            {R.id.iv88, R.id.iv89, R.id.iv90, R.id.iv91, R.id.iv92, R.id.iv93, R.id.iv94, R.id.iv95},
            {R.id.iv96, R.id.iv97, R.id.iv98, R.id.iv99, R.id.iv100, R.id.iv101, R.id.iv102, R.id.iv103},
            {R.id.iv104, R.id.iv105, R.id.iv106, R.id.iv107, R.id.iv108, R.id.iv109, R.id.iv110, R.id.iv111}

    };


    static final int ROW = 14;
    static final int COL = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        board_Image = new ImageView[ROW][COL];
        for(int i=0; i < ROW; i++){
                for(int j = 0; j < COL; j++){
                    board_Image[i][j] = (ImageView) findViewById(idArray[i][j]);
            }
        }


        board = new Board();
        board.gen_puyo();
        drawBoard();

        up = (Button) findViewById(R.id.button_up);
        up.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.spin();
                drawBoard();
            }

        });

        down = (Button) findViewById(R.id.button_down);
        down.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_down();
                drawBoard();
            }

        });

        left = (Button) findViewById(R.id.button_left);
        left.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_left();
                drawBoard();
            }

        });

        right = (Button) findViewById(R.id.button_right);
        right.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_right();
                drawBoard();
            }

        });

        for (int q = 0; q < 100; q++) {
            //at first generate puyo

            for (int k = 0; k < board.getY(); k++) {
                for (int i = 0; i < board.getX(); i++) {
                    System.out.print(board.getboard()[i][k]);
                    //Log.d("", String.valueOf(board.getboard()[i][k]));
                }
                System.out.println();
            }

/*
            //after move please endwith end_step
            //board.end_step();

            for (int k = 0; k < board.getY(); k++) {
                for (int i = 0; i <board.getX(); i++) {
                    System.out.print(board.getboard()[i][k]);
                    // Log.d( "", String.valueOf(board.getboard()[i][k]));
                }
                System.out.println();
            }
            System.out.println();

            //drop every puyo inthe air
            board.update_map();

            //score/point part clear_board n times to clear whole board
            int point = board.clear_board();
            int multiplier = 1;
            int stepscore = point;
            while (point != 0) {                                       
                point = multiplier*board.clear_board();                 
                stepscore = stepscore+point;
                for (int k = 0; k < board.getY(); k++) {
                    for (int i = 0; i < board.getX(); i++) {
                        System.out.print(board.getboard()[i][k]);
                    }
                    System.out.println();
                }
                System.out.println();
                multiplier = multiplier*4;
                board.update_map();
            }
            for (int k = 0; k < board.getY(); k++) {
                for (int i = 0; i < board.getX(); i++) {
                    System.out.print(board.getboard()[i][k]);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("a.getboard()[i][k]");

        }
*/
            if (board.gen_puyo() == 0)
                return;

        }

    }
    private void drawBoard() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (board.getboard()[j][i] == 1) {
                    board_Image[i][j].setImageResource(R.drawable.puyo_blue);
                } else if (board.getboard()[j][i] == 2) {
                    board_Image[i][j].setImageResource(R.drawable.puyo_green);
                } else if (board.getboard()[j][i] == 3) {
                    board_Image[i][j].setImageResource(R.drawable.puyo_red);
                } else if (board.getboard()[j][i] == 4) {
                    board_Image[i][j].setImageResource(R.drawable.puyo_yellow);
                } else {
                    board_Image[i][j].setImageResource(R.drawable.none);
                }


            }
        }

        /* wall */
        for(int i = 0 ; i < COL ; i++){
            board_Image[0][i].setImageResource(R.drawable.wall);
            board_Image[13][i].setImageResource(R.drawable.wall);
        }

        for(int i = 0 ; i < ROW ; i++){
            board_Image[i][0].setImageResource(R.drawable.wall);
            board_Image[i][7].setImageResource(R.drawable.wall);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {

        switch(keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                board.move_left();
                drawBoard();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                board.move_right();
                drawBoard();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                board.spin();
                drawBoard();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                board.move_down();
                drawBoard();
                break;
        }

        return super.onKeyDown(keyCode, msg);
    }
}
