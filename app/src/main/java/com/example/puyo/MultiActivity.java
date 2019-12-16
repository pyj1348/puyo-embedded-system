package com.example.puyo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MultiActivity extends AppCompatActivity {

    ///// ROOM

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

    String server_IP, client_IP;
    BufferedReader in;
    PrintWriter out;
    boolean start = false;

    ///// ROOM

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
    private ImageView[] m_puyoque_Image = new ImageView[4];
    private ImageView[] m2_puyoque_Image = new ImageView[4];
    private ImageView[] m3_puyoque_Image = new ImageView[4];
    private ImageView[] m4_puyoque_Image = new ImageView[4];
    private TextView m_score_TextView;
    private TextView m2_score_TextView;
    private TextView m3_score_TextView;
    private TextView m4_score_TextView;

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
    private TextView p4_sub2_score_iv;
    /* do not be confused the number */

    private int[][] sub1_idArray = new int[ROW][COL];
    private int[][] sub2_idArray = new int[ROW][COL];
    private int[][] sub3_idArray = new int[ROW][COL];
    private ArrayList<Integer> arrays;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        int number = myIntent.getIntExtra("number", 1);
        server_IP = myIntent.getStringExtra("server_IP");
        client_IP = myIntent.getStringExtra("client_IP");
        setLayout(number);

        componentArray boards = new componentArray();
        arrays = new ArrayList<Integer>();

        // number is the number of the player passed from previous activity
        if (number >= 4) {
            sub3_idArray = boards.get_4array(number);
            m4_puyoque_Image = get_4arrayq(number);
            m4_score_TextView = get_4arrayt(number);

            arrays.add(3);
        }
        if (number >= 3) {
            sub2_idArray = boards.get_3array(number);
            m3_puyoque_Image = get_3arrayq(number);
            m3_score_TextView = get_3arrayt(number);

            arrays.add(2);
        }
        if (number >= 2) {
            sub1_idArray = boards.get_2array(number);
            m2_puyoque_Image = get_2arrayq(number);
            m2_score_TextView = get_2arrayt(number);

            arrays.add(1);
        }
        if (number >= 1) {
            idArray = boards.get_1array(number);
            m_puyoque_Image = get_1arrayq(number);
            m_score_TextView = get_1arrayt(number);
        }
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                m_board_Image[i][j] = (ImageView) findViewById(idArray[i][j]);
                sub1_board_Image[i][j] = (ImageView) findViewById(sub1_idArray[i][j]);
                sub2_board_Image[i][j] = (ImageView) findViewById(sub2_idArray[i][j]);
                sub3_board_Image[i][j] = (ImageView) findViewById(sub3_idArray[i][j]);
            }
        }
        board = new Board();
        subBoards = new Board();
        initBoard(m_board_Image);
        tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
            }
        };
        tt2 = new TimerTask() {
            @Override
            public void run() {
                counter2++;
            }
        };
        timer = new Timer();
        timer2 = new Timer();
        timer.schedule(tt, 0, 100);
        timer2.schedule(tt2, 0, 100);
        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what != 0) {
                    m_score_TextView.setText(Integer.toString(msg.what));
                } else {
                    drawMyBoard();
                    //drawSubBoard(packet);
                }
            }

        };
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
                    handler.sendEmptyMessage(0);
                    int temp = counter2;
                    while (temp + 1 > counter2) {

                    }
                    if (compare + speed <= counter) {
                        if (curpoint.equals(latterpoint.x, latterpoint.y)) {
                            board.move_down();
                            curpoint = new Point(board.get_puyoposition().x, board.get_puyoposition().y);
                            if (curpoint.equals(latterpoint.x, latterpoint.y)) {
                                board.end_step();
                                board.update_map();
                                stagescore = board.clear_board();
                                if (stagescore != 0) {
                                    score = score + stagescore;
                                    temp = counter;
                                    handler.sendEmptyMessage(0);
                                    handler.sendEmptyMessage(score);  // Draw the score;
                                    while (temp + 5 > counter) {

                                    }
                                    int multiplier = 1;
                                    board.update_map();
                                    stagescore = multiplier * board.clear_board();
                                    while (stagescore != 0) {
                                        score = score + stagescore;
                                        multiplier = multiplier * 4;
                                        board.update_map();
                                        temp = counter;
                                        handler.sendEmptyMessage(0);
                                        handler.sendEmptyMessage(score);  // Draw the score;
                                        while (temp + 5 > counter) {

                                        }
                                        board.update_map();
                                        stagescore = multiplier * board.clear_board();
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

        //while(true){
        //    if(number == 1 || start){
                NewRunnable nr = new NewRunnable();
                Thread t = new Thread(nr);
                t.start();
        //        break;
        //    }
        //}

        ///// ROOM

        LCD_write(number+"players number", "ip address");
        segment_write(0000);
        LED_write(1);
        matrix_write(1);
        // device thread

        new ButtonThread().start();
        new SocketThread().start();
    }

    public class SocketThread extends Thread {
        Socket socket;

        @Override
        public void run() {
            try {
                socket = new Socket(server_IP, 5000);
                //socket.connect(new InetSocketAddress(server_IP, 5000));

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                String s;

                while (true) {
                    s = in.readLine();
                    String[] tokens = s.split(":");

                    if ("START".equals(tokens[0])) {
                        start = true;
                    } else if("VIEW".equals(tokens[0])){ // board 변경 시 out.flush 했던 직렬화 data
                        // 직렬화 data 을 그리기 (작성 단계에서 몇 번인지 적혀있음)
                    } else if("RANK".equals(tokens[0])){ // 사망하면 out.flush 해서 알려주고 종료할 것
                        // 전달 받은 등수 출력
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ///// ROOM

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

    public void setLayout(int num) {
        if (num == 1)
            setContentView(R.layout.single_play);
        if (num == 2)
            setContentView(R.layout.players2);
        if (num == 3)
            setContentView(R.layout.players3);
        if (num == 4)
            setContentView(R.layout.players4);
    }

    private void drawMyBoard() {
        if (board != null) {
            /*      Draw Board     */
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
            drawqueue(board, m_puyoque_Image);
        }
    }

    private void drawqueue(Board board, ImageView[] queueimage) {
        Puyo first = board.getPuyoQueue().getFirstItem();
        Puyo second = board.getPuyoQueue().getNextitem();

        /*      Draw Puyo Queue     */

        /*      First of First  */
        if (first.Getfirst() == 1) {
            queueimage[0].setImageResource(R.drawable.puyo_blue);
        } else if (first.Getfirst() == 2) {
            queueimage[0].setImageResource(R.drawable.puyo_green);
        } else if (first.Getfirst() == 3) {
            queueimage[0].setImageResource(R.drawable.puyo_red);
        } else if (first.Getfirst() == 4) {
            queueimage[0].setImageResource(R.drawable.puyo_yellow);
        } else {
            queueimage[0].setImageResource(R.drawable.none);
        }

        /*      Second of First  */
        if (first.Getsecond() == 1) {
            queueimage[1].setImageResource(R.drawable.puyo_blue);
        } else if (first.Getsecond() == 2) {
            queueimage[1].setImageResource(R.drawable.puyo_green);
        } else if (first.Getsecond() == 3) {
            queueimage[1].setImageResource(R.drawable.puyo_red);
        } else if (first.Getsecond() == 4) {
            queueimage[1].setImageResource(R.drawable.puyo_yellow);
        } else {
            queueimage[1].setImageResource(R.drawable.none);
        }


        /*      First of Second  */
        if (second.Getfirst() == 1) {
            queueimage[2].setImageResource(R.drawable.puyo_blue);
        } else if (second.Getfirst() == 2) {
            queueimage[2].setImageResource(R.drawable.puyo_green);
        } else if (second.Getfirst() == 3) {
            queueimage[2].setImageResource(R.drawable.puyo_red);
        } else if (second.Getfirst() == 4) {
            queueimage[2].setImageResource(R.drawable.puyo_yellow);
        } else {
            queueimage[2].setImageResource(R.drawable.none);
        }

        /*      Second of Second  */
        if (second.Getsecond() == 1) {
            queueimage[3].setImageResource(R.drawable.puyo_blue);
        } else if (second.Getsecond() == 2) {
            queueimage[3].setImageResource(R.drawable.puyo_green);
        } else if (second.Getsecond() == 3) {
            queueimage[3].setImageResource(R.drawable.puyo_red);
        } else if (second.Getsecond() == 4) {
            queueimage[3].setImageResource(R.drawable.puyo_yellow);
        } else {
            queueimage[3].setImageResource(R.drawable.none);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void drawSubBoard(String packet) {
        ImageView[][] handleimage = new ImageView[0][];
        ImageView[] qimage = new ImageView[4];

        /* sub board 1 */
        for (int a : arrays) {
            if (a == 1 && packet.startsWith(Integer.toString(a))) {
                handleimage = sub1_board_Image;
                qimage = m2_puyoque_Image;
            } else if (a == 2 && packet.startsWith(Integer.toString(a))) {
                handleimage = sub2_board_Image;
                qimage = m3_puyoque_Image;
            } else if (a == 3 && packet.startsWith(Integer.toString(a))) {
                handleimage = sub3_board_Image;
                qimage = m4_puyoque_Image;
            } else
                return;
        }

        subBoards.board_update(packet);
        for (int i = 1; i < ROW - 1; i++) {
            for (int j = 1; j < COL - 1; j++) {
                if (subBoards.getboard()[j][i] == 1) {
                    handleimage[i][j].setImageResource(R.drawable.puyo_blue);
                } else if (board.getboard()[j][i] == 2) {
                    handleimage[i][j].setImageResource(R.drawable.puyo_green);
                } else if (board.getboard()[j][i] == 3) {
                    handleimage[i][j].setImageResource(R.drawable.puyo_red);
                } else if (board.getboard()[j][i] == 4) {
                    handleimage[i][j].setImageResource(R.drawable.puyo_yellow);
                } else {
                    handleimage[i][j].setImageResource(R.drawable.none);
                }
            }
        }
        drawqueue(subBoards, qimage);
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

    public ImageView[] get_1arrayq(int number) {
        ImageView[] temp = new ImageView[4];
        switch (number) {
            case 1:
                temp[0] = findViewById(R.id.sp_que_iv_01);
                temp[1] = findViewById(R.id.sp_que_iv_02);
                temp[2] = findViewById(R.id.sp_que_iv_03);
                temp[3] = findViewById(R.id.sp_que_iv_04);
                return temp;
            case 2:
                temp[0] = findViewById(R.id.p2_my_que_iv_01);
                temp[1] = findViewById(R.id.p2_my_que_iv_02);
                temp[2] = findViewById(R.id.p2_my_que_iv_03);
                temp[3] = findViewById(R.id.p2_my_que_iv_04);
                return temp;
            case 3:
                temp[0] = findViewById(R.id.p3_my_que_iv_01);
                temp[1] = findViewById(R.id.p3_my_que_iv_02);
                temp[2] = findViewById(R.id.p3_my_que_iv_03);
                temp[3] = findViewById(R.id.p3_my_que_iv_04);
                return temp;
            case 4:
                temp[0] = findViewById(R.id.p4_my_que_iv_01);
                temp[1] = findViewById(R.id.p4_my_que_iv_02);
                temp[2] = findViewById(R.id.p4_my_que_iv_03);
                temp[3] = findViewById(R.id.p4_my_que_iv_04);
                return temp;
            default:
                return null;
        }
    }

    public ImageView[] get_2arrayq(int number) {
        ImageView[] temp = new ImageView[4];
        switch (number) {
            case 2:
                temp[0] = findViewById(R.id.p2_sub_que_iv_01);
                temp[1] = findViewById(R.id.p2_sub_que_iv_02);
                temp[2] = findViewById(R.id.p2_sub_que_iv_03);
                temp[3] = findViewById(R.id.p2_sub_que_iv_04);
                return temp;
            case 3:
                temp[0] = findViewById(R.id.p3_sub1_que_iv_01);
                temp[1] = findViewById(R.id.p3_sub1_que_iv_02);
                temp[2] = findViewById(R.id.p3_sub1_que_iv_03);
                temp[3] = findViewById(R.id.p3_sub1_que_iv_04);
                return temp;
            case 4:
                temp[0] = findViewById(R.id.p4_sub1_que_iv_01);
                temp[1] = findViewById(R.id.p4_sub1_que_iv_02);
                temp[2] = findViewById(R.id.p4_sub1_que_iv_03);
                temp[3] = findViewById(R.id.p4_sub1_que_iv_04);
                return temp;
            default:
                return null;
        }
    }

    public ImageView[] get_3arrayq(int number) {
        ImageView[] temp = new ImageView[4];
        switch (number) {
            case 3:
                temp[0] = findViewById(R.id.p3_sub2_que_iv_01);
                temp[1] = findViewById(R.id.p3_sub2_que_iv_02);
                temp[2] = findViewById(R.id.p3_sub2_que_iv_03);
                temp[3] = findViewById(R.id.p3_sub2_que_iv_04);
                return temp;
            case 4:
                temp[0] = findViewById(R.id.p4_sub2_que_iv_01);
                temp[1] = findViewById(R.id.p4_sub2_que_iv_02);
                temp[2] = findViewById(R.id.p4_sub2_que_iv_03);
                temp[3] = findViewById(R.id.p4_sub2_que_iv_04);
                return temp;
            default:
                return null;
        }
    }

    public ImageView[] get_4arrayq(int number) {
        ImageView[] temp = new ImageView[4];
        switch (number) {
            case 4:
                temp[0] = findViewById(R.id.p4_sub3_que_iv_01);
                temp[1] = findViewById(R.id.p4_sub3_que_iv_02);
                temp[2] = findViewById(R.id.p4_sub3_que_iv_03);
                temp[3] = findViewById(R.id.p4_sub3_que_iv_04);
                return temp;
            default:
                return null;
        }
    }

    public TextView get_1arrayt(int number) {
        switch (number) {
            case 1:
                return findViewById(R.id.sp_score_iv);
            case 2:
                return findViewById(R.id.p2_my_score_iv);
            case 3:
                return findViewById(R.id.p3_my_score_iv);
            case 4:
                return findViewById(R.id.p4_my_score_iv);
            default:
                return null;
        }
    }

    public TextView get_2arrayt(int number) {
        switch (number) {
            case 2:
                return findViewById(R.id.p2_sub_score_iv);
            case 3:
                return findViewById(R.id.p3_sub1_score_iv);
            case 4:
                return findViewById(R.id.p4_sub1_score_iv);
            default:
                return null;
        }
    }

    public TextView get_3arrayt(int number) {
        switch (number) {
            case 3:
                return findViewById(R.id.p3_sub2_score_iv);
            case 4:
                return findViewById(R.id.p4_sub2_score_iv);
            default:
                return null;
        }
    }

    public TextView get_4arrayt(int number) {
        switch (number) {
            case 4:
                return findViewById(R.id.p4_sub3_score_iv);
            default:
                return null;
        }
    }
}