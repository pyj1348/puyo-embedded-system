package com.example.puyo;

import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MultiActivity extends AppCompatActivity {

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

    static final int ROW = 14;
    static final int COL = 8;
    static int counter = 0;
    static int counter2 = 0;
    static int speed = 10;

    private Board board;
    private Board subBoards;

    static Timer timer = new Timer();
    static Timer timer2 = new Timer();

    private final ImageView[][] m_board_Image = new ImageView[ROW][COL];
    private final ImageView[][] sub1_board_Image = new ImageView[ROW][COL];
    private final ImageView[][] sub2_board_Image = new ImageView[ROW][COL];
    private final ImageView[][] sub3_board_Image = new ImageView[ROW][COL];

    static TimerTask tt = new TimerTask() {
        @Override
        public void run() {
            counter++;
        }
    };
    static TimerTask tt2 = new TimerTask() {
        @Override
        public void run() {
            counter2++;
        }
    };

    private final boolean isConnected = false;

    private int[][] idArray;

    /* do not be confused the number */

    private int[][] sub1_idArray;
    private int[][] sub2_idArray;
    private int[][] sub3_idArray;
    private ArrayList<Integer> arrays;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players2);

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                m_board_Image[i][j] = (ImageView) findViewById(idArray[i][j]);
                sub1_board_Image[i][j] = (ImageView) findViewById(sub1_idArray[i][j]);
            }
        }
        componentArray boards = new componentArray();
        arrays = new ArrayList<Integer>();
        int number;
        // number is the number of the player passed from previous activity
        if (number <= 4) {
            sub3_idArray = boards.get_4array(number);
            arrays.add(3);
        }
        if (number <= 3) {
            sub2_idArray = boards.get_3array(number);
            arrays.add(2);
        }
        if (number <= 2) {
            sub1_idArray = boards.get_2array(number);
            arrays.add(1);
        }
        if (number <= 1) {
            idArray = boards.get_1array(number);
        }
        board = new Board();
        subBoards = new Board();
        initBoard(m_board_Image);

        timer.schedule(tt, 0, 100);

        timer2.schedule(tt2, 0, 100);

        board.gen_puyo();
        class NewRunnable implements Runnable {
            int score = 0;
            int stagescore = 0;
            int compare = counter;
            Point curpoint = new Point(board.get_puyoposition().x, board.get_puyoposition().y);
            Point latterpoint = new Point(board.get_puyoposition().x, board.get_puyoposition().y);

            @Override
            public void run() {
                while (true) {
                    drawMyBoard();

                    int temp = counter2;
                    while (temp + 1 > counter2) {

                    }
                    if (compare + speed <= counter) {
                        if (curpoint.equals(latterpoint.x, latterpoint.y)) {
                            board.move_down();
                            drawMyBoard();
                            curpoint = new Point(board.get_puyoposition().x, board.get_puyoposition().y);
                            if (curpoint.equals(latterpoint.x, latterpoint.y)) {
                                board.end_step();
                                board.update_map();
                                drawMyBoard();
                                stagescore = board.clear_board();
                                if (stagescore != 0) {
                                    temp = counter;
                                    while (temp + 5 > counter) {

                                    }
                                    int multiplier = 1;
                                    while (stagescore != 0) {
                                        stagescore = multiplier * board.clear_board();
                                        multiplier = multiplier * 4;
                                        board.update_map();
                                        drawMyBoard();
                                        temp = counter;
                                        while (temp + 5 > counter) {

                                        }
                                        score = score + stagescore;
                                    }
                                }
                                if (board.gen_puyo() == 0)
                                    break;
                            }
                        }
                        latterpoint = new Point(curpoint.x, curpoint.y);
                        compare = counter;
                    }
                    if ((int) (counter / 30) > 10 - speed) {
                        speed--;
                        compare = counter;
                    }

                }
            }
        }

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();

        LCD_write(number+" player", "165.194.15.1");
        segment_write(0010);
        LED_write(1);
        matrix_write(1);

        ButtonThread thread = new ButtonThread();
        thread.start();
    }

    private class ButtonThread extends Thread {
        private static final String TAG = "ButtonThread";

        public ButtonThread() { // initialization
            button_open();
        }

        public void run() {
            int direction;
            int temp = 0;
            while (true) {

                direction = button_read();
                if (temp != direction) {
                    if (direction == 1) {
                        board.spin(); // printf("LEFT ROTATE\n");
                        drawMyBoard();
                    } else if (direction == 2) {
                        board.move_left(); // printf("LEFT MOVE\n");
                        drawMyBoard();
                    } else if (direction == 4) {
                        board.move_right(); // printf("RIGHT MOVE\n");
                        drawMyBoard();
                    } else if (direction == 8) {
                        board.spin(); // printf("RIGHT ROTATE\n");
                        drawMyBoard();
                    }
                }
                temp = direction;
            }
        }
        // button_close();
    }

    private void drawMyBoard() {

        for (int i = 1; i < ROW - 1; i++) {
            for (int j = 1; j < COL - 1; j++) {
                if (board.getboard()[j][i] == 1) {
                    m_board_Image[i][j].setImageResource(R.drawable.puyo_blue);
                } else if (board.getboard()[j][i] == 2) {
                    m_board_Image[i][j].setImageResource(R.drawable.puyo_green);
                } else if (board.getboard()[j][i] == 3) {
                    m_board_Image[i][j].setImageResource(R.drawable.puyo_red);
                } else if (board.getboard()[j][i] == 4) {
                    m_board_Image[i][j].setImageResource(R.drawable.puyo_yellow);
                } else {
                    m_board_Image[i][j].setImageResource(R.drawable.none);
                }
            }
        }

    }

    private void initBoard(ImageView[][] handleimage) {
        /* wall */
        for (int i = 0; i < COL; i++) {
            handleimage[0][i].setImageResource(R.drawable.wall);
            handleimage[13][i].setImageResource(R.drawable.wall);
        }

        for (int i = 0; i < ROW; i++) {
            handleimage[i][0].setImageResource(R.drawable.wall);
            handleimage[i][7].setImageResource(R.drawable.wall);
        }
    }

    private void drawSubBoard(String packet) {
        ImageView[][] handleimage;
        /* sub board 1 */
        for (int a : arrays) {
            if (a == 1 && packet.startsWith(Integer.toString(a)))
                handleimage = sub1_board_Image;
            else if (a == 2 && packet.startsWith(Integer.toString(a)))
                handleimage = sub2_board_Image;
            else if (a == 3 && packet.startsWith(Integer.toString(a)))
                handleimage = sub3_board_Image;
            else
                return;
        }

        subBoards.board_update(packet);
        for (int i = 1; i < ROW - 1; i++) {
            for (int j = 1; j < COL - 1; j++) {
                if (subBoards.getboard()[j][i] == 1) {
                    sub1_board_Image[i][j].setImageResource(R.drawable.puyo_blue);
                } else if (board.getboard()[j][i] == 2) {
                    sub1_board_Image[i][j].setImageResource(R.drawable.puyo_green);
                } else if (board.getboard()[j][i] == 3) {
                    sub1_board_Image[i][j].setImageResource(R.drawable.puyo_red);
                } else if (board.getboard()[j][i] == 4) {
                    sub1_board_Image[i][j].setImageResource(R.drawable.puyo_yellow);
                } else {
                    sub1_board_Image[i][j].setImageResource(R.drawable.none);
                }
            }
        }
        /* wall */
        initBoard(handleimage);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent msg) {

        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            board.move_left();
            drawMyBoard();
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            board.move_right();
            drawMyBoard();
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            board.spin();
            drawMyBoard();
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            board.move_down();
            drawMyBoard();
            break;
        }

        return super.onKeyDown(keyCode, msg);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {

        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            board.move_left();
            drawMyBoard();
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            board.move_right();
            drawMyBoard();
            break;
        case KeyEvent.KEYCODE_DPAD_UP:
            board.spin();
            drawMyBoard();
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            board.move_down();
            drawMyBoard();
            break;
        }

        return super.onKeyDown(keyCode, msg);
    }
}
