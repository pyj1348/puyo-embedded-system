package com.example.puyo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button up, down, left, right;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        board = new Board();

       up = (Button) findViewById(R.id.button_up) ;
       up.setOnClickListener(new Button.OnClickListener() {
           @Override
           public void onClick(View v) {
               board.spin();
           }

        });

        down = (Button) findViewById(R.id.button_down) ;
        down.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_down();
            }

        });

        left = (Button) findViewById(R.id.button_left) ;
        left.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_left();
            }

        });

        right = (Button) findViewById(R.id.button_right) ;
        right.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_right();
            }

        });

        for (int q = 0; q < 100; q++) {
            //at first generate puyo
            if (board.gen_puyo() == 0)
                return;
            for (int k = 0; k < board.getY(); k++) {
                for (int i = 0; i < board.getX(); i++) {
                    System.out.print(board.getboard()[i][k]);
                }
                System.out.println();
            }

            //after move please endwith end_step
            board.end_step();

            for (int k = 0; k < board.getY(); k++) {
                for (int i = 0; i <board.getX(); i++) {
                    System.out.print(board.getboard()[i][k]);
                }
                System.out.println();
            }
            System.out.println();

            //drop every puyo inthe air
            board.update_map();

            //score/point part clear_board n times to clear whole board
            int point = board.clear_board();
            int multiplier = 1;
            while (point != 0) {
                point = multiplier*board.clear_board();
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


    }

    private void drawBoard(){


    }
}
