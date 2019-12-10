package com.example.puyo;

import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Players2Activity extends AppCompatActivity {

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
    static int speed = 0;

    private Board board;
    private Board subBoards;

    private ImageView[][] m_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub1_board_Image = new ImageView[ROW][COL];

    private boolean isConnected = false;

    private int[][] idArray = {
            {R.id.iv_2p_00, R.id.iv_2p_01, R.id.iv_2p_02, R.id.iv_2p_03, R.id.iv_2p_04, R.id.iv_2p_05, R.id.iv_2p_06, R.id.iv_2p_07},
            {R.id.iv_2p_08, R.id.iv_2p_09, R.id.iv_2p_10, R.id.iv_2p_11, R.id.iv_2p_12, R.id.iv_2p_13, R.id.iv_2p_14, R.id.iv_2p_15},
            {R.id.iv_2p_16, R.id.iv_2p_17, R.id.iv_2p_18, R.id.iv_2p_19, R.id.iv_2p_20, R.id.iv_2p_21, R.id.iv_2p_22, R.id.iv_2p_23},
            {R.id.iv_2p_24, R.id.iv_2p_25, R.id.iv_2p_26, R.id.iv_2p_27, R.id.iv_2p_28, R.id.iv_2p_29, R.id.iv_2p_30, R.id.iv_2p_31},
            {R.id.iv_2p_32, R.id.iv_2p_33, R.id.iv_2p_34, R.id.iv_2p_35, R.id.iv_2p_36, R.id.iv_2p_37, R.id.iv_2p_38, R.id.iv_2p_39},
            {R.id.iv_2p_40, R.id.iv_2p_41, R.id.iv_2p_42, R.id.iv_2p_43, R.id.iv_2p_44, R.id.iv_2p_45, R.id.iv_2p_46, R.id.iv_2p_47},
            {R.id.iv_2p_48, R.id.iv_2p_49, R.id.iv_2p_50, R.id.iv_2p_51, R.id.iv_2p_52, R.id.iv_2p_53, R.id.iv_2p_54, R.id.iv_2p_55},
            {R.id.iv_2p_56, R.id.iv_2p_57, R.id.iv_2p_58, R.id.iv_2p_59, R.id.iv_2p_60, R.id.iv_2p_61, R.id.iv_2p_62, R.id.iv_2p_63},
            {R.id.iv_2p_64, R.id.iv_2p_65, R.id.iv_2p_66, R.id.iv_2p_67, R.id.iv_2p_68, R.id.iv_2p_69, R.id.iv_2p_70, R.id.iv_2p_71},
            {R.id.iv_2p_72, R.id.iv_2p_73, R.id.iv_2p_74, R.id.iv_2p_75, R.id.iv_2p_76, R.id.iv_2p_77, R.id.iv_2p_78, R.id.iv_2p_79},
            {R.id.iv_2p_80, R.id.iv_2p_81, R.id.iv_2p_82, R.id.iv_2p_83, R.id.iv_2p_84, R.id.iv_2p_85, R.id.iv_2p_86, R.id.iv_2p_87},
            {R.id.iv_2p_88, R.id.iv_2p_89, R.id.iv_2p_90, R.id.iv_2p_91, R.id.iv_2p_92, R.id.iv_2p_93, R.id.iv_2p_94, R.id.iv_2p_95},
            {R.id.iv_2p_96, R.id.iv_2p_97, R.id.iv_2p_98, R.id.iv_2p_99, R.id.iv_2p_100, R.id.iv_2p_101, R.id.iv_2p_102, R.id.iv_2p_103},
            {R.id.iv_2p_104, R.id.iv_2p_105, R.id.iv_2p_106, R.id.iv_2p_107, R.id.iv_2p_108, R.id.iv_2p_109, R.id.iv_2p_110, R.id.iv_2p_111}
    };

    /* do not be confused the number */

    private int[][] sub1_idArray = {
            {R.id.iv_2p_00_2, R.id.iv_2p_01_2, R.id.iv_2p_02_2, R.id.iv_2p_03_2, R.id.iv_2p_04_2, R.id.iv_2p_05_2, R.id.iv_2p_06_2, R.id.iv_2p_07_2},
            {R.id.iv_2p_08_2, R.id.iv_2p_09_2, R.id.iv_2p_10_2, R.id.iv_2p_11_2, R.id.iv_2p_12_2, R.id.iv_2p_13_2, R.id.iv_2p_14_2, R.id.iv_2p_15_2},
            {R.id.iv_2p_16_2, R.id.iv_2p_17_2, R.id.iv_2p_18_2, R.id.iv_2p_19_2, R.id.iv_2p_20_2, R.id.iv_2p_21_2, R.id.iv_2p_22_2, R.id.iv_2p_23_2},
            {R.id.iv_2p_24_2, R.id.iv_2p_25_2, R.id.iv_2p_26_2, R.id.iv_2p_27_2, R.id.iv_2p_28_2, R.id.iv_2p_29_2, R.id.iv_2p_30_2, R.id.iv_2p_31_2},
            {R.id.iv_2p_32_2, R.id.iv_2p_33_2, R.id.iv_2p_34_2, R.id.iv_2p_35_2, R.id.iv_2p_36_2, R.id.iv_2p_37_2, R.id.iv_2p_38_2, R.id.iv_2p_39_2},
            {R.id.iv_2p_40_2, R.id.iv_2p_41_2, R.id.iv_2p_42_2, R.id.iv_2p_43_2, R.id.iv_2p_44_2, R.id.iv_2p_45_2, R.id.iv_2p_46_2, R.id.iv_2p_47_2},
            {R.id.iv_2p_48_2, R.id.iv_2p_49_2, R.id.iv_2p_50_2, R.id.iv_2p_51_2, R.id.iv_2p_52_2, R.id.iv_2p_53_2, R.id.iv_2p_54_2, R.id.iv_2p_55_2},
            {R.id.iv_2p_56_2, R.id.iv_2p_57_2, R.id.iv_2p_58_2, R.id.iv_2p_59_2, R.id.iv_2p_60_2, R.id.iv_2p_61_2, R.id.iv_2p_62_2, R.id.iv_2p_63_2},
            {R.id.iv_2p_64_2, R.id.iv_2p_65_2, R.id.iv_2p_66_2, R.id.iv_2p_67_2, R.id.iv_2p_68_2, R.id.iv_2p_69_2, R.id.iv_2p_70_2, R.id.iv_2p_71_2},
            {R.id.iv_2p_72_2, R.id.iv_2p_73_2, R.id.iv_2p_74_2, R.id.iv_2p_75_2, R.id.iv_2p_76_2, R.id.iv_2p_77_2, R.id.iv_2p_78_2, R.id.iv_2p_79_2},
            {R.id.iv_2p_80_2, R.id.iv_2p_81_2, R.id.iv_2p_82_2, R.id.iv_2p_83_2, R.id.iv_2p_84_2, R.id.iv_2p_85_2, R.id.iv_2p_86_2, R.id.iv_2p_87_2},
            {R.id.iv_2p_88_2, R.id.iv_2p_89_2, R.id.iv_2p_90_2, R.id.iv_2p_91_2, R.id.iv_2p_92_2, R.id.iv_2p_93_2, R.id.iv_2p_94_2, R.id.iv_2p_95_2},
            {R.id.iv_2p_96_2, R.id.iv_2p_97_2, R.id.iv_2p_98_2, R.id.iv_2p_99_2, R.id.iv_2p_100_2, R.id.iv_2p_101_2, R.id.iv_2p_102_2, R.id.iv_2p_103_2},
            {R.id.iv_2p_104_2, R.id.iv_2p_105_2, R.id.iv_2p_106_2, R.id.iv_2p_107_2, R.id.iv_2p_108_2, R.id.iv_2p_109_2, R.id.iv_2p_110_2, R.id.iv_2p_111_2}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players2);

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                m_board_Image[i][j] = (ImageView) findViewById(idArray[i][j]);
                sub1_board_Image[i][j] = (ImageView) findViewById(sub1_idArray[i][j]);
            }
        }




        /* i think i need to modify this part */

        /*  need to add sockect and to draw
         *   if connected -> change isConnected[index] to true
         *   and then send Board data to subBoard array( subBoard[] )
         *   and then excute drawSubBoard();
         * */

        drawSubBoard(); // like this


        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
            }
        };

        board = new Board();
        subBoards = new Board();

        drawMyBoard();
        drawSubBoard();

        Point curpoint = board.get_puyoposition();
        Point latterpoint = board.get_puyoposition();
        int score = 0;
        int stagescore = 0;
        int compare = counter;
        Timer timer = new Timer();
        timer.schedule(tt, 0, 1000);
        while (board.gen_puyo()!=0) {
            if (compare != counter) {
                if (latterpoint == curpoint) {
                    board.move_down();
                    curpoint = board.get_puyoposition();
                    if (latterpoint == curpoint) {
                        board.end_step();
                        board.update_map();
                        stagescore = board.clear_board();
                        drawMyBoard();
                        drawSubBoard();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int multiplier = 1;
                        while (stagescore != 0) {
                            stagescore = stagescore + multiplier * board.clear_board();
                            multiplier = multiplier * 4;
                            board.update_map();
                            drawMyBoard();
                            drawSubBoard();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        score = score +stagescore;
                    }
                }
            }
            if ((int) (counter / 30) > 0) {
                speed++;
                timer.schedule(tt, 0, 1000 / speed);
            }
            drawMyBoard();
            drawSubBoard();
        }

        ///// JNI

        LCD_write("2 players ", "165.194.15.1");
        segment_write(1010);
        LED_write(210);
        matrix_write(0);

        ExampleThread thread = new ExampleThread();
        thread.start();

        ///// JNI
    }

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
        // botton_close();
    }

    private void drawMyBoard() {

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
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

    private void drawSubBoard() {

        /* sub board 1 */
        if (isConnected) {
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
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
            for (int i = 0; i < COL; i++) {
                sub1_board_Image[0][i].setImageResource(R.drawable.wall);
                sub1_board_Image[13][i].setImageResource(R.drawable.wall);
            }

            for (int i = 0; i < ROW; i++) {
                sub1_board_Image[i][0].setImageResource(R.drawable.wall);
                sub1_board_Image[i][7].setImageResource(R.drawable.wall);
            }

        } else { // not connected
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    sub1_board_Image[i][j].setImageResource(R.drawable.wall);
                }
            }

        }

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
