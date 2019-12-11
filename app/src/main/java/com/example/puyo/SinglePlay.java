package com.example.puyo;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SinglePlay extends AppCompatActivity {

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

    //////// JNI
    static final int ROW = 14;
    static final int COL = 8;
    static int counter = 0;
    static int counter2 = 0;
    static int speed = 10;
    static Board board;
    static Timer timer = new Timer();
    static Timer timer2 = new Timer();
    private ImageView[][] m_board_Image = new ImageView[ROW][COL];

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

    private int[][] idArray = {
            { R.id.iv_sp_00, R.id.iv_sp_01, R.id.iv_sp_02, R.id.iv_sp_03, R.id.iv_sp_04, R.id.iv_sp_05, R.id.iv_sp_06,
                    R.id.iv_sp_07 },
            { R.id.iv_sp_08, R.id.iv_sp_09, R.id.iv_sp_10, R.id.iv_sp_11, R.id.iv_sp_12, R.id.iv_sp_13, R.id.iv_sp_14,
                    R.id.iv_sp_15 },
            { R.id.iv_sp_16, R.id.iv_sp_17, R.id.iv_sp_18, R.id.iv_sp_19, R.id.iv_sp_20, R.id.iv_sp_21, R.id.iv_sp_22,
                    R.id.iv_sp_23 },
            { R.id.iv_sp_24, R.id.iv_sp_25, R.id.iv_sp_26, R.id.iv_sp_27, R.id.iv_sp_28, R.id.iv_sp_29, R.id.iv_sp_30,
                    R.id.iv_sp_31 },
            { R.id.iv_sp_32, R.id.iv_sp_33, R.id.iv_sp_34, R.id.iv_sp_35, R.id.iv_sp_36, R.id.iv_sp_37, R.id.iv_sp_38,
                    R.id.iv_sp_39 },
            { R.id.iv_sp_40, R.id.iv_sp_41, R.id.iv_sp_42, R.id.iv_sp_43, R.id.iv_sp_44, R.id.iv_sp_45, R.id.iv_sp_46,
                    R.id.iv_sp_47 },
            { R.id.iv_sp_48, R.id.iv_sp_49, R.id.iv_sp_50, R.id.iv_sp_51, R.id.iv_sp_52, R.id.iv_sp_53, R.id.iv_sp_54,
                    R.id.iv_sp_55 },
            { R.id.iv_sp_56, R.id.iv_sp_57, R.id.iv_sp_58, R.id.iv_sp_59, R.id.iv_sp_60, R.id.iv_sp_61, R.id.iv_sp_62,
                    R.id.iv_sp_63 },
            { R.id.iv_sp_64, R.id.iv_sp_65, R.id.iv_sp_66, R.id.iv_sp_67, R.id.iv_sp_68, R.id.iv_sp_69, R.id.iv_sp_70,
                    R.id.iv_sp_71 },
            { R.id.iv_sp_72, R.id.iv_sp_73, R.id.iv_sp_74, R.id.iv_sp_75, R.id.iv_sp_76, R.id.iv_sp_77, R.id.iv_sp_78,
                    R.id.iv_sp_79 },
            { R.id.iv_sp_80, R.id.iv_sp_81, R.id.iv_sp_82, R.id.iv_sp_83, R.id.iv_sp_84, R.id.iv_sp_85, R.id.iv_sp_86,
                    R.id.iv_sp_87 },
            { R.id.iv_sp_88, R.id.iv_sp_89, R.id.iv_sp_90, R.id.iv_sp_91, R.id.iv_sp_92, R.id.iv_sp_93, R.id.iv_sp_94,
                    R.id.iv_sp_95 },
            { R.id.iv_sp_96, R.id.iv_sp_97, R.id.iv_sp_98, R.id.iv_sp_99, R.id.iv_sp_100, R.id.iv_sp_101,
                    R.id.iv_sp_102, R.id.iv_sp_103 },
            { R.id.iv_sp_104, R.id.iv_sp_105, R.id.iv_sp_106, R.id.iv_sp_107, R.id.iv_sp_108, R.id.iv_sp_109,
                    R.id.iv_sp_110, R.id.iv_sp_111 } };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_play);

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                m_board_Image[i][j] = (ImageView) findViewById(idArray[i][j]);
            }
        }
        initBoard();
        timer.schedule(tt, 0, 100);

        timer2.schedule(tt2, 0, 100);

        board = new Board();
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

        LCD_write("1 player", "165.194.15.1");
        segment_write(0010);
        LED_write(1);
        matrix_write(1);

        ButtonThread thread = new ButtonThread();
        thread.start();
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

    private void initBoard() {
        /* wall */
        for (int i = 0; i < COL; i++) {
            m_board_Image[0][i].setImageResource(R.drawable.wall);
            m_board_Image[13][i].setImageResource(R.drawable.wall);
        }

        for (int i = 0; i < ROW; i++) {
            m_board_Image[i][0].setImageResource(R.drawable.wall);
            m_board_Image[i][7].setImageResource(R.drawable.wall);
        }
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
