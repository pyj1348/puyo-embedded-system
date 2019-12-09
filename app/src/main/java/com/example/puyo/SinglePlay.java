package com.example.puyo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SinglePlay extends AppCompatActivity {

    static final int ROW = 14;
    static final int COL = 8;

    private Board board;

    private ImageView[][] m_board_Image = new ImageView[ROW][COL];


    private int[][] idArray = {
            {R.id.iv_sp_00, R.id.iv_sp_01, R.id.iv_sp_02, R.id.iv_sp_03, R.id.iv_sp_04, R.id.iv_sp_05, R.id.iv_sp_06, R.id.iv_sp_07},
            {R.id.iv_sp_08, R.id.iv_sp_09, R.id.iv_sp_10, R.id.iv_sp_11, R.id.iv_sp_12, R.id.iv_sp_13, R.id.iv_sp_14, R.id.iv_sp_15},
            {R.id.iv_sp_16, R.id.iv_sp_17, R.id.iv_sp_18, R.id.iv_sp_19, R.id.iv_sp_20, R.id.iv_sp_21, R.id.iv_sp_22, R.id.iv_sp_23},
            {R.id.iv_sp_24, R.id.iv_sp_25, R.id.iv_sp_26, R.id.iv_sp_27, R.id.iv_sp_28, R.id.iv_sp_29, R.id.iv_sp_30, R.id.iv_sp_31},
            {R.id.iv_sp_32, R.id.iv_sp_33, R.id.iv_sp_34, R.id.iv_sp_35, R.id.iv_sp_36, R.id.iv_sp_37, R.id.iv_sp_38, R.id.iv_sp_39},
            {R.id.iv_sp_40, R.id.iv_sp_41, R.id.iv_sp_42, R.id.iv_sp_43, R.id.iv_sp_44, R.id.iv_sp_45, R.id.iv_sp_46, R.id.iv_sp_47},
            {R.id.iv_sp_48, R.id.iv_sp_49, R.id.iv_sp_50, R.id.iv_sp_51, R.id.iv_sp_52, R.id.iv_sp_53, R.id.iv_sp_54, R.id.iv_sp_55},
            {R.id.iv_sp_56, R.id.iv_sp_57, R.id.iv_sp_58, R.id.iv_sp_59, R.id.iv_sp_60, R.id.iv_sp_61, R.id.iv_sp_62, R.id.iv_sp_63},
            {R.id.iv_sp_64, R.id.iv_sp_65, R.id.iv_sp_66, R.id.iv_sp_67, R.id.iv_sp_68, R.id.iv_sp_69, R.id.iv_sp_70, R.id.iv_sp_71},
            {R.id.iv_sp_72, R.id.iv_sp_73, R.id.iv_sp_74, R.id.iv_sp_75, R.id.iv_sp_76, R.id.iv_sp_77, R.id.iv_sp_78, R.id.iv_sp_79},
            {R.id.iv_sp_80, R.id.iv_sp_81, R.id.iv_sp_82, R.id.iv_sp_83, R.id.iv_sp_84, R.id.iv_sp_85, R.id.iv_sp_86, R.id.iv_sp_87},
            {R.id.iv_sp_88, R.id.iv_sp_89, R.id.iv_sp_90, R.id.iv_sp_91, R.id.iv_sp_92, R.id.iv_sp_93, R.id.iv_sp_94, R.id.iv_sp_95},
            {R.id.iv_sp_96, R.id.iv_sp_97, R.id.iv_sp_98, R.id.iv_sp_99, R.id.iv_sp_100, R.id.iv_sp_101, R.id.iv_sp_102, R.id.iv_sp_103},
            {R.id.iv_sp_104, R.id.iv_sp_105, R.id.iv_sp_106, R.id.iv_sp_107, R.id.iv_sp_108, R.id.iv_sp_109, R.id.iv_sp_110, R.id.iv_sp_111}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_play);

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                m_board_Image[i][j] = (ImageView) findViewById(idArray[i][j]);
            }
        }


        board = new Board();


        board.gen_puyo();
        drawMyBoard();

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
