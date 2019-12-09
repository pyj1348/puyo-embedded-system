package com.example.puyo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    static final int ROW = 14;
    static final int COL = 8;

    private Button up, down, left, right;
    private Board board;
    private Board[] subBoards;

    private ImageView[][] m_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub1_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub2_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub3_board_Image = new ImageView[ROW][COL];

    private boolean[] isConnected = new boolean[]{false, false, false};

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

    /* do not be confused the number */
    private int[][] sub1_idArray = {
            {R.id.iv00_2, R.id.iv01_2, R.id.iv02_2, R.id.iv03_2, R.id.iv04_2, R.id.iv05_2, R.id.iv06_2, R.id.iv07_2},
            {R.id.iv08_2, R.id.iv09_2, R.id.iv10_2, R.id.iv11_2, R.id.iv12_2, R.id.iv13_2, R.id.iv14_2, R.id.iv15_2},
            {R.id.iv16_2, R.id.iv17_2, R.id.iv18_2, R.id.iv19_2, R.id.iv20_2, R.id.iv21_2, R.id.iv22_2, R.id.iv23_2},
            {R.id.iv24_2, R.id.iv25_2, R.id.iv26_2, R.id.iv27_2, R.id.iv28_2, R.id.iv29_2, R.id.iv30_2, R.id.iv31_2},
            {R.id.iv32_2, R.id.iv33_2, R.id.iv34_2, R.id.iv35_2, R.id.iv36_2, R.id.iv37_2, R.id.iv38_2, R.id.iv39_2},
            {R.id.iv40_2, R.id.iv41_2, R.id.iv42_2, R.id.iv43_2, R.id.iv44_2, R.id.iv45_2, R.id.iv46_2, R.id.iv47_2},
            {R.id.iv48_2, R.id.iv49_2, R.id.iv50_2, R.id.iv51_2, R.id.iv52_2, R.id.iv53_2, R.id.iv54_2, R.id.iv55_2},
            {R.id.iv56_2, R.id.iv57_2, R.id.iv58_2, R.id.iv59_2, R.id.iv60_2, R.id.iv61_2, R.id.iv62_2, R.id.iv63_2},
            {R.id.iv64_2, R.id.iv65_2, R.id.iv66_2, R.id.iv67_2, R.id.iv68_2, R.id.iv69_2, R.id.iv70_2, R.id.iv71_2},
            {R.id.iv72_2, R.id.iv73_2, R.id.iv74_2, R.id.iv75_2, R.id.iv76_2, R.id.iv77_2, R.id.iv78_2, R.id.iv79_2},
            {R.id.iv80_2, R.id.iv81_2, R.id.iv82_2, R.id.iv83_2, R.id.iv84_2, R.id.iv85_2, R.id.iv86_2, R.id.iv87_2},
            {R.id.iv88_2, R.id.iv89_2, R.id.iv90_2, R.id.iv91_2, R.id.iv92_2, R.id.iv93_2, R.id.iv94_2, R.id.iv95_2},
            {R.id.iv96_2, R.id.iv97_2, R.id.iv98_2, R.id.iv99_2, R.id.iv100_2, R.id.iv101_2, R.id.iv102_2, R.id.iv103_2},
            {R.id.iv104_2, R.id.iv105_2, R.id.iv106_2, R.id.iv107_2, R.id.iv108_2, R.id.iv109_2, R.id.iv110_2, R.id.iv111_2}
    };

    private int[][] sub2_idArray = {
            {R.id.iv00_3, R.id.iv01_3, R.id.iv02_3, R.id.iv03_3, R.id.iv04_3, R.id.iv05_3, R.id.iv06_3, R.id.iv07_3},
            {R.id.iv08_3, R.id.iv09_3, R.id.iv10_3, R.id.iv11_3, R.id.iv12_3, R.id.iv13_3, R.id.iv14_3, R.id.iv15_3},
            {R.id.iv16_3, R.id.iv17_3, R.id.iv18_3, R.id.iv19_3, R.id.iv20_3, R.id.iv21_3, R.id.iv22_3, R.id.iv23_3},
            {R.id.iv24_3, R.id.iv25_3, R.id.iv26_3, R.id.iv27_3, R.id.iv28_3, R.id.iv29_3, R.id.iv30_3, R.id.iv31_3},
            {R.id.iv32_3, R.id.iv33_3, R.id.iv34_3, R.id.iv35_3, R.id.iv36_3, R.id.iv37_3, R.id.iv38_3, R.id.iv39_3},
            {R.id.iv40_3, R.id.iv41_3, R.id.iv42_3, R.id.iv43_3, R.id.iv44_3, R.id.iv45_3, R.id.iv46_3, R.id.iv47_3},
            {R.id.iv48_3, R.id.iv49_3, R.id.iv50_3, R.id.iv51_3, R.id.iv52_3, R.id.iv53_3, R.id.iv54_3, R.id.iv55_3},
            {R.id.iv56_3, R.id.iv57_3, R.id.iv58_3, R.id.iv59_3, R.id.iv60_3, R.id.iv61_3, R.id.iv62_3, R.id.iv63_3},
            {R.id.iv64_3, R.id.iv65_3, R.id.iv66_3, R.id.iv67_3, R.id.iv68_3, R.id.iv69_3, R.id.iv70_3, R.id.iv71_3},
            {R.id.iv72_3, R.id.iv73_3, R.id.iv74_3, R.id.iv75_3, R.id.iv76_3, R.id.iv77_3, R.id.iv78_3, R.id.iv79_3},
            {R.id.iv80_3, R.id.iv81_3, R.id.iv82_3, R.id.iv83_3, R.id.iv84_3, R.id.iv85_3, R.id.iv86_3, R.id.iv87_3},
            {R.id.iv88_3, R.id.iv89_3, R.id.iv90_3, R.id.iv91_3, R.id.iv92_3, R.id.iv93_3, R.id.iv94_3, R.id.iv95_3},
            {R.id.iv96_3, R.id.iv97_3, R.id.iv98_3, R.id.iv99_3, R.id.iv100_3, R.id.iv101_3, R.id.iv102_3, R.id.iv103_3},
            {R.id.iv104_3, R.id.iv105_3, R.id.iv106_3, R.id.iv107_3, R.id.iv108_3, R.id.iv109_3, R.id.iv110_3, R.id.iv111_3}
    };

    private int[][] sub3_idArray = {
            {R.id.iv00_4, R.id.iv01_4, R.id.iv02_4, R.id.iv03_4, R.id.iv04_4, R.id.iv05_4, R.id.iv06_4, R.id.iv07_4},
            {R.id.iv08_4, R.id.iv09_4, R.id.iv10_4, R.id.iv11_4, R.id.iv12_4, R.id.iv13_4, R.id.iv14_4, R.id.iv15_4},
            {R.id.iv16_4, R.id.iv17_4, R.id.iv18_4, R.id.iv19_4, R.id.iv20_4, R.id.iv21_4, R.id.iv22_4, R.id.iv23_4},
            {R.id.iv24_4, R.id.iv25_4, R.id.iv26_4, R.id.iv27_4, R.id.iv28_4, R.id.iv29_4, R.id.iv30_4, R.id.iv31_4},
            {R.id.iv32_4, R.id.iv33_4, R.id.iv34_4, R.id.iv35_4, R.id.iv36_4, R.id.iv37_4, R.id.iv38_4, R.id.iv39_4},
            {R.id.iv40_4, R.id.iv41_4, R.id.iv42_4, R.id.iv43_4, R.id.iv44_4, R.id.iv45_4, R.id.iv46_4, R.id.iv47_4},
            {R.id.iv48_4, R.id.iv49_4, R.id.iv50_4, R.id.iv51_4, R.id.iv52_4, R.id.iv53_4, R.id.iv54_4, R.id.iv55_4},
            {R.id.iv56_4, R.id.iv57_4, R.id.iv58_4, R.id.iv59_4, R.id.iv60_4, R.id.iv61_4, R.id.iv62_4, R.id.iv63_4},
            {R.id.iv64_4, R.id.iv65_4, R.id.iv66_4, R.id.iv67_4, R.id.iv68_4, R.id.iv69_4, R.id.iv70_4, R.id.iv71_4},
            {R.id.iv72_4, R.id.iv73_4, R.id.iv74_4, R.id.iv75_4, R.id.iv76_4, R.id.iv77_4, R.id.iv78_4, R.id.iv79_4},
            {R.id.iv80_4, R.id.iv81_4, R.id.iv82_4, R.id.iv83_4, R.id.iv84_4, R.id.iv85_4, R.id.iv86_4, R.id.iv87_4},
            {R.id.iv88_4, R.id.iv89_4, R.id.iv90_4, R.id.iv91_4, R.id.iv92_4, R.id.iv93_4, R.id.iv94_4, R.id.iv95_4},
            {R.id.iv96_4, R.id.iv97_4, R.id.iv98_4, R.id.iv99_4, R.id.iv100_4, R.id.iv101_4, R.id.iv102_4, R.id.iv103_4},
            {R.id.iv104_4, R.id.iv105_4, R.id.iv106_4, R.id.iv107_4, R.id.iv108_4, R.id.iv109_4, R.id.iv110_4, R.id.iv111_4}
    };

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
        setContentView(R.layout.main_activity);


        for(int i=0; i < ROW; i++){
                for(int j = 0; j < COL; j++){
                    m_board_Image[i][j] = (ImageView) findViewById(idArray[i][j]);
                    sub1_board_Image[i][j] = (ImageView) findViewById(sub1_idArray[i][j]);
                    sub2_board_Image[i][j] = (ImageView) findViewById(sub2_idArray[i][j]);
                    sub3_board_Image[i][j] = (ImageView) findViewById(sub3_idArray[i][j]);
            }
        }


        board = new Board();
        subBoards = new Board[]{new Board(), new Board(), new Board()};


        board.gen_puyo();
        drawMyBoard();

        /* i think i need to modify this part */
        subBoards[0].getboard();
        subBoards[1].getboard();
        subBoards[2].getboard();

        /*  need to add sockect and to draw
        *   if connected -> change isConnected[index] to true
        *   and then send Board data to subBoard array( subBoard[] )
        *   and then excute drawSubBoard();
        * */

        drawSubBoard(); // like this

        up = (Button) findViewById(R.id.button_up);
        up.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.spin();
                drawMyBoard();
            }

        });

        down = (Button) findViewById(R.id.button_down);
        down.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_down();
                drawMyBoard();
            }

        });

        left = (Button) findViewById(R.id.button_left);
        left.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_left();
                drawMyBoard();
            }

        });

        right = (Button) findViewById(R.id.button_right);
        right.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                board.move_right();
                drawMyBoard();
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

            if (board.gen_puyo() == 0)
                return;

        }

        ///// JNI

        LCD_write("3 players ", "165.194.1.1");
        segment_write(1010);
        LED_write(210);
        matrix_write(0);

        ExampleThread thread = new ExampleThread();
        thread.start();

        // botton_close();

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
        for(int i = 0 ; i < COL ; i++){
            m_board_Image[0][i].setImageResource(R.drawable.wall);
            m_board_Image[13][i].setImageResource(R.drawable.wall);
        }

        for(int i = 0 ; i < ROW ; i++){
            m_board_Image[i][0].setImageResource(R.drawable.wall);
            m_board_Image[i][7].setImageResource(R.drawable.wall);
        }
    }

    private void drawSubBoard() {

        /* sub board 1 */
        if(isConnected[0]){
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (subBoards[0].getboard()[j][i] == 1) {
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

        }else { // not connected
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    sub1_board_Image[i][j].setImageResource(R.drawable.wall);
                }
            }

        }


        /* sub board 2 */
        if(isConnected[1]){
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (subBoards[1].getboard()[j][i] == 1) {
                        sub2_board_Image[i][j].setImageResource(R.drawable.puyo_blue);
                    } else if (board.getboard()[j][i] == 2) {
                        sub2_board_Image[i][j].setImageResource(R.drawable.puyo_green);
                    } else if (board.getboard()[j][i] == 3) {
                        sub2_board_Image[i][j].setImageResource(R.drawable.puyo_red);
                    } else if (board.getboard()[j][i] == 4) {
                        sub2_board_Image[i][j].setImageResource(R.drawable.puyo_yellow);
                    } else {
                        sub2_board_Image[i][j].setImageResource(R.drawable.none);
                    }
                }
            }
            /* wall */
            for (int i = 0; i < COL; i++) {
                sub2_board_Image[0][i].setImageResource(R.drawable.wall);
                sub2_board_Image[13][i].setImageResource(R.drawable.wall);
            }

            for (int i = 0; i < ROW; i++) {
                sub2_board_Image[i][0].setImageResource(R.drawable.wall);
                sub2_board_Image[i][7].setImageResource(R.drawable.wall);
            }

        }else { // not connected
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    sub2_board_Image[i][j].setImageResource(R.drawable.wall);
                }
            }

        }

        /* sub board 3 */
        if(isConnected[1]){
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if (subBoards[2].getboard()[j][i] == 1) {
                        sub3_board_Image[i][j].setImageResource(R.drawable.puyo_blue);
                    } else if (board.getboard()[j][i] == 2) {
                        sub3_board_Image[i][j].setImageResource(R.drawable.puyo_green);
                    } else if (board.getboard()[j][i] == 3) {
                        sub3_board_Image[i][j].setImageResource(R.drawable.puyo_red);
                    } else if (board.getboard()[j][i] == 4) {
                        sub3_board_Image[i][j].setImageResource(R.drawable.puyo_yellow);
                    } else {
                        sub3_board_Image[i][j].setImageResource(R.drawable.none);
                    }
                }
            }
            /* wall */
            for (int i = 0; i < COL; i++) {
                sub3_board_Image[0][i].setImageResource(R.drawable.wall);
                sub3_board_Image[13][i].setImageResource(R.drawable.wall);
            }

            for (int i = 0; i < ROW; i++) {
                sub3_board_Image[i][0].setImageResource(R.drawable.wall);
                sub3_board_Image[i][7].setImageResource(R.drawable.wall);
            }

        }else { // not connected
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    sub3_board_Image[i][j].setImageResource(R.drawable.wall);
                }
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {

        switch(keyCode)
        {
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
