package com.example.puyo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Players3Activity extends AppCompatActivity {

    static final int ROW = 14;
    static final int COL = 8;

    private Board board;
    private Board[] subBoards;

    private ImageView[][] m_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub1_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub2_board_Image = new ImageView[ROW][COL];

    private boolean[] isConnected = new boolean[]{false, false};

    private int[][] idArray = {
            {R.id.iv_3p_00, R.id.iv_3p_01, R.id.iv_3p_02, R.id.iv_3p_03, R.id.iv_3p_04, R.id.iv_3p_05, R.id.iv_3p_06, R.id.iv_3p_07},
            {R.id.iv_3p_08, R.id.iv_3p_09, R.id.iv_3p_10, R.id.iv_3p_11, R.id.iv_3p_12, R.id.iv_3p_13, R.id.iv_3p_14, R.id.iv_3p_15},
            {R.id.iv_3p_16, R.id.iv_3p_17, R.id.iv_3p_18, R.id.iv_3p_19, R.id.iv_3p_20, R.id.iv_3p_21, R.id.iv_3p_22, R.id.iv_3p_23},
            {R.id.iv_3p_24, R.id.iv_3p_25, R.id.iv_3p_26, R.id.iv_3p_27, R.id.iv_3p_28, R.id.iv_3p_29, R.id.iv_3p_30, R.id.iv_3p_31},
            {R.id.iv_3p_32, R.id.iv_3p_33, R.id.iv_3p_34, R.id.iv_3p_35, R.id.iv_3p_36, R.id.iv_3p_37, R.id.iv_3p_38, R.id.iv_3p_39},
            {R.id.iv_3p_40, R.id.iv_3p_41, R.id.iv_3p_42, R.id.iv_3p_43, R.id.iv_3p_44, R.id.iv_3p_45, R.id.iv_3p_46, R.id.iv_3p_47},
            {R.id.iv_3p_48, R.id.iv_3p_49, R.id.iv_3p_50, R.id.iv_3p_51, R.id.iv_3p_52, R.id.iv_3p_53, R.id.iv_3p_54, R.id.iv_3p_55},
            {R.id.iv_3p_56, R.id.iv_3p_57, R.id.iv_3p_58, R.id.iv_3p_59, R.id.iv_3p_60, R.id.iv_3p_61, R.id.iv_3p_62, R.id.iv_3p_63},
            {R.id.iv_3p_64, R.id.iv_3p_65, R.id.iv_3p_66, R.id.iv_3p_67, R.id.iv_3p_68, R.id.iv_3p_69, R.id.iv_3p_70, R.id.iv_3p_71},
            {R.id.iv_3p_72, R.id.iv_3p_73, R.id.iv_3p_74, R.id.iv_3p_75, R.id.iv_3p_76, R.id.iv_3p_77, R.id.iv_3p_78, R.id.iv_3p_79},
            {R.id.iv_3p_80, R.id.iv_3p_81, R.id.iv_3p_82, R.id.iv_3p_83, R.id.iv_3p_84, R.id.iv_3p_85, R.id.iv_3p_86, R.id.iv_3p_87},
            {R.id.iv_3p_88, R.id.iv_3p_89, R.id.iv_3p_90, R.id.iv_3p_91, R.id.iv_3p_92, R.id.iv_3p_93, R.id.iv_3p_94, R.id.iv_3p_95},
            {R.id.iv_3p_96, R.id.iv_3p_97, R.id.iv_3p_98, R.id.iv_3p_99, R.id.iv_3p_100, R.id.iv_3p_101, R.id.iv_3p_102, R.id.iv_3p_103},
            {R.id.iv_3p_104, R.id.iv_3p_105, R.id.iv_3p_106, R.id.iv_3p_107, R.id.iv_3p_108, R.id.iv_3p_109, R.id.iv_3p_110, R.id.iv_3p_111}
    };

    /* do not be confused the number */

    private int[][] sub1_idArray = {
            {R.id.iv_3p_00_2, R.id.iv_3p_01_2, R.id.iv_3p_02_2, R.id.iv_3p_03_2, R.id.iv_3p_04_2, R.id.iv_3p_05_2, R.id.iv_3p_06_2, R.id.iv_3p_07_2},
            {R.id.iv_3p_08_2, R.id.iv_3p_09_2, R.id.iv_3p_10_2, R.id.iv_3p_11_2, R.id.iv_3p_12_2, R.id.iv_3p_13_2, R.id.iv_3p_14_2, R.id.iv_3p_15_2},
            {R.id.iv_3p_16_2, R.id.iv_3p_17_2, R.id.iv_3p_18_2, R.id.iv_3p_19_2, R.id.iv_3p_20_2, R.id.iv_3p_21_2, R.id.iv_3p_22_2, R.id.iv_3p_23_2},
            {R.id.iv_3p_24_2, R.id.iv_3p_25_2, R.id.iv_3p_26_2, R.id.iv_3p_27_2, R.id.iv_3p_28_2, R.id.iv_3p_29_2, R.id.iv_3p_30_2, R.id.iv_3p_31_2},
            {R.id.iv_3p_32_2, R.id.iv_3p_33_2, R.id.iv_3p_34_2, R.id.iv_3p_35_2, R.id.iv_3p_36_2, R.id.iv_3p_37_2, R.id.iv_3p_38_2, R.id.iv_3p_39_2},
            {R.id.iv_3p_40_2, R.id.iv_3p_41_2, R.id.iv_3p_42_2, R.id.iv_3p_43_2, R.id.iv_3p_44_2, R.id.iv_3p_45_2, R.id.iv_3p_46_2, R.id.iv_3p_47_2},
            {R.id.iv_3p_48_2, R.id.iv_3p_49_2, R.id.iv_3p_50_2, R.id.iv_3p_51_2, R.id.iv_3p_52_2, R.id.iv_3p_53_2, R.id.iv_3p_54_2, R.id.iv_3p_55_2},
            {R.id.iv_3p_56_2, R.id.iv_3p_57_2, R.id.iv_3p_58_2, R.id.iv_3p_59_2, R.id.iv_3p_60_2, R.id.iv_3p_61_2, R.id.iv_3p_62_2, R.id.iv_3p_63_2},
            {R.id.iv_3p_64_2, R.id.iv_3p_65_2, R.id.iv_3p_66_2, R.id.iv_3p_67_2, R.id.iv_3p_68_2, R.id.iv_3p_69_2, R.id.iv_3p_70_2, R.id.iv_3p_71_2},
            {R.id.iv_3p_72_2, R.id.iv_3p_73_2, R.id.iv_3p_74_2, R.id.iv_3p_75_2, R.id.iv_3p_76_2, R.id.iv_3p_77_2, R.id.iv_3p_78_2, R.id.iv_3p_79_2},
            {R.id.iv_3p_80_2, R.id.iv_3p_81_2, R.id.iv_3p_82_2, R.id.iv_3p_83_2, R.id.iv_3p_84_2, R.id.iv_3p_85_2, R.id.iv_3p_86_2, R.id.iv_3p_87_2},
            {R.id.iv_3p_88_2, R.id.iv_3p_89_2, R.id.iv_3p_90_2, R.id.iv_3p_91_2, R.id.iv_3p_92_2, R.id.iv_3p_93_2, R.id.iv_3p_94_2, R.id.iv_3p_95_2},
            {R.id.iv_3p_96_2, R.id.iv_3p_97_2, R.id.iv_3p_98_2, R.id.iv_3p_99_2, R.id.iv_3p_100_2, R.id.iv_3p_101_2, R.id.iv_3p_102_2, R.id.iv_3p_103_2},
            {R.id.iv_3p_104_2, R.id.iv_3p_105_2, R.id.iv_3p_106_2, R.id.iv_3p_107_2, R.id.iv_3p_108_2, R.id.iv_3p_109_2, R.id.iv_3p_110_2, R.id.iv_3p_111_2}
    };


    private int[][] sub2_idArray = {
            {R.id.iv_3p_00_3, R.id.iv_3p_01_3, R.id.iv_3p_02_3, R.id.iv_3p_03_3, R.id.iv_3p_04_3, R.id.iv_3p_05_3, R.id.iv_3p_06_3, R.id.iv_3p_07_3},
            {R.id.iv_3p_08_3, R.id.iv_3p_09_3, R.id.iv_3p_10_3, R.id.iv_3p_11_3, R.id.iv_3p_12_3, R.id.iv_3p_13_3, R.id.iv_3p_14_3, R.id.iv_3p_15_3},
            {R.id.iv_3p_16_3, R.id.iv_3p_17_3, R.id.iv_3p_18_3, R.id.iv_3p_19_3, R.id.iv_3p_20_3, R.id.iv_3p_21_3, R.id.iv_3p_22_3, R.id.iv_3p_23_3},
            {R.id.iv_3p_24_3, R.id.iv_3p_25_3, R.id.iv_3p_26_3, R.id.iv_3p_27_3, R.id.iv_3p_28_3, R.id.iv_3p_29_3, R.id.iv_3p_30_3, R.id.iv_3p_31_3},
            {R.id.iv_3p_32_3, R.id.iv_3p_33_3, R.id.iv_3p_34_3, R.id.iv_3p_35_3, R.id.iv_3p_36_3, R.id.iv_3p_37_3, R.id.iv_3p_38_3, R.id.iv_3p_39_3},
            {R.id.iv_3p_40_3, R.id.iv_3p_41_3, R.id.iv_3p_42_3, R.id.iv_3p_43_3, R.id.iv_3p_44_3, R.id.iv_3p_45_3, R.id.iv_3p_46_3, R.id.iv_3p_47_3},
            {R.id.iv_3p_48_3, R.id.iv_3p_49_3, R.id.iv_3p_50_3, R.id.iv_3p_51_3, R.id.iv_3p_52_3, R.id.iv_3p_53_3, R.id.iv_3p_54_3, R.id.iv_3p_55_3},
            {R.id.iv_3p_56_3, R.id.iv_3p_57_3, R.id.iv_3p_58_3, R.id.iv_3p_59_3, R.id.iv_3p_60_3, R.id.iv_3p_61_3, R.id.iv_3p_62_3, R.id.iv_3p_63_3},
            {R.id.iv_3p_64_3, R.id.iv_3p_65_3, R.id.iv_3p_66_3, R.id.iv_3p_67_3, R.id.iv_3p_68_3, R.id.iv_3p_69_3, R.id.iv_3p_70_3, R.id.iv_3p_71_3},
            {R.id.iv_3p_72_3, R.id.iv_3p_73_3, R.id.iv_3p_74_3, R.id.iv_3p_75_3, R.id.iv_3p_76_3, R.id.iv_3p_77_3, R.id.iv_3p_78_3, R.id.iv_3p_79_3},
            {R.id.iv_3p_80_3, R.id.iv_3p_81_3, R.id.iv_3p_82_3, R.id.iv_3p_83_3, R.id.iv_3p_84_3, R.id.iv_3p_85_3, R.id.iv_3p_86_3, R.id.iv_3p_87_3},
            {R.id.iv_3p_88_3, R.id.iv_3p_89_3, R.id.iv_3p_90_3, R.id.iv_3p_91_3, R.id.iv_3p_92_3, R.id.iv_3p_93_3, R.id.iv_3p_94_3, R.id.iv_3p_95_3},
            {R.id.iv_3p_96_3, R.id.iv_3p_97_3, R.id.iv_3p_98_3, R.id.iv_3p_99_3, R.id.iv_3p_100_3, R.id.iv_3p_101_3, R.id.iv_3p_102_3, R.id.iv_3p_103_3},
            {R.id.iv_3p_104_3, R.id.iv_3p_105_3, R.id.iv_3p_106_3, R.id.iv_3p_107_3, R.id.iv_3p_108_3, R.id.iv_3p_109_3, R.id.iv_3p_110_3, R.id.iv_3p_111_3}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players3);

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                m_board_Image[i][j] = (ImageView) findViewById(idArray[i][j]);
                sub1_board_Image[i][j] = (ImageView) findViewById(sub1_idArray[i][j]);
                sub2_board_Image[i][j] = (ImageView) findViewById(sub2_idArray[i][j]);
            }
        }


        board = new Board();
        subBoards = new Board[]{new Board(), new Board()};


        board.gen_puyo();
        drawMyBoard();

        /* i think i need to modify this part */
        subBoards[0].getboard();
        subBoards[1].getboard();

        /*  need to add sockect and to draw
         *   if connected -> change isConnected[index] to true
         *   and then send Board data to subBoard array( subBoard[] )
         *   and then excute drawSubBoard();
         * */

        drawSubBoard(); // like this

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

    private void drawSubBoard() {

        /* sub board 1 */
        if (isConnected[0]) {
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

        } else { // not connected
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    sub1_board_Image[i][j].setImageResource(R.drawable.wall);
                }
            }

        }


        /* sub board 2 */
        if (isConnected[1]) {
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

        } else { // not connected
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    sub2_board_Image[i][j].setImageResource(R.drawable.wall);
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