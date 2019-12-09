package com.example.puyo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class Players4Activity extends AppCompatActivity {
    static final int ROW = 14;
    static final int COL = 8;

    private Board board;
    private Board[] subBoards;

    private ImageView[][] m_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub1_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub2_board_Image = new ImageView[ROW][COL];
    private ImageView[][] sub3_board_Image = new ImageView[ROW][COL];

    private boolean[] isConnected = new boolean[]{false, false, false};

    private int[][] idArray = {
            {R.id.iv_4p_00, R.id.iv_4p_01, R.id.iv_4p_02, R.id.iv_4p_03, R.id.iv_4p_04, R.id.iv_4p_05, R.id.iv_4p_06, R.id.iv_4p_07},
            {R.id.iv_4p_08, R.id.iv_4p_09, R.id.iv_4p_10, R.id.iv_4p_11, R.id.iv_4p_12, R.id.iv_4p_13, R.id.iv_4p_14, R.id.iv_4p_15},
            {R.id.iv_4p_16, R.id.iv_4p_17, R.id.iv_4p_18, R.id.iv_4p_19, R.id.iv_4p_20, R.id.iv_4p_21, R.id.iv_4p_22, R.id.iv_4p_23},
            {R.id.iv_4p_24, R.id.iv_4p_25, R.id.iv_4p_26, R.id.iv_4p_27, R.id.iv_4p_28, R.id.iv_4p_29, R.id.iv_4p_30, R.id.iv_4p_31},
            {R.id.iv_4p_32, R.id.iv_4p_33, R.id.iv_4p_34, R.id.iv_4p_35, R.id.iv_4p_36, R.id.iv_4p_37, R.id.iv_4p_38, R.id.iv_4p_39},
            {R.id.iv_4p_40, R.id.iv_4p_41, R.id.iv_4p_42, R.id.iv_4p_43, R.id.iv_4p_44, R.id.iv_4p_45, R.id.iv_4p_46, R.id.iv_4p_47},
            {R.id.iv_4p_48, R.id.iv_4p_49, R.id.iv_4p_50, R.id.iv_4p_51, R.id.iv_4p_52, R.id.iv_4p_53, R.id.iv_4p_54, R.id.iv_4p_55},
            {R.id.iv_4p_56, R.id.iv_4p_57, R.id.iv_4p_58, R.id.iv_4p_59, R.id.iv_4p_60, R.id.iv_4p_61, R.id.iv_4p_62, R.id.iv_4p_63},
            {R.id.iv_4p_64, R.id.iv_4p_65, R.id.iv_4p_66, R.id.iv_4p_67, R.id.iv_4p_68, R.id.iv_4p_69, R.id.iv_4p_70, R.id.iv_4p_71},
            {R.id.iv_4p_72, R.id.iv_4p_73, R.id.iv_4p_74, R.id.iv_4p_75, R.id.iv_4p_76, R.id.iv_4p_77, R.id.iv_4p_78, R.id.iv_4p_79},
            {R.id.iv_4p_80, R.id.iv_4p_81, R.id.iv_4p_82, R.id.iv_4p_83, R.id.iv_4p_84, R.id.iv_4p_85, R.id.iv_4p_86, R.id.iv_4p_87},
            {R.id.iv_4p_88, R.id.iv_4p_89, R.id.iv_4p_90, R.id.iv_4p_91, R.id.iv_4p_92, R.id.iv_4p_93, R.id.iv_4p_94, R.id.iv_4p_95},
            {R.id.iv_4p_96, R.id.iv_4p_97, R.id.iv_4p_98, R.id.iv_4p_99, R.id.iv_4p_100, R.id.iv_4p_101, R.id.iv_4p_102, R.id.iv_4p_103},
            {R.id.iv_4p_104, R.id.iv_4p_105, R.id.iv_4p_106, R.id.iv_4p_107, R.id.iv_4p_108, R.id.iv_4p_109, R.id.iv_4p_110, R.id.iv_4p_111}
    };

    /* do not be confused the number */

    private int[][] sub1_idArray = {
            {R.id.iv_4p_00_2, R.id.iv_4p_01_2, R.id.iv_4p_02_2, R.id.iv_4p_03_2, R.id.iv_4p_04_2, R.id.iv_4p_05_2, R.id.iv_4p_06_2, R.id.iv_4p_07_2},
            {R.id.iv_4p_08_2, R.id.iv_4p_09_2, R.id.iv_4p_10_2, R.id.iv_4p_11_2, R.id.iv_4p_12_2, R.id.iv_4p_13_2, R.id.iv_4p_14_2, R.id.iv_4p_15_2},
            {R.id.iv_4p_16_2, R.id.iv_4p_17_2, R.id.iv_4p_18_2, R.id.iv_4p_19_2, R.id.iv_4p_20_2, R.id.iv_4p_21_2, R.id.iv_4p_22_2, R.id.iv_4p_23_2},
            {R.id.iv_4p_24_2, R.id.iv_4p_25_2, R.id.iv_4p_26_2, R.id.iv_4p_27_2, R.id.iv_4p_28_2, R.id.iv_4p_29_2, R.id.iv_4p_30_2, R.id.iv_4p_31_2},
            {R.id.iv_4p_32_2, R.id.iv_4p_33_2, R.id.iv_4p_34_2, R.id.iv_4p_35_2, R.id.iv_4p_36_2, R.id.iv_4p_37_2, R.id.iv_4p_38_2, R.id.iv_4p_39_2},
            {R.id.iv_4p_40_2, R.id.iv_4p_41_2, R.id.iv_4p_42_2, R.id.iv_4p_43_2, R.id.iv_4p_44_2, R.id.iv_4p_45_2, R.id.iv_4p_46_2, R.id.iv_4p_47_2},
            {R.id.iv_4p_48_2, R.id.iv_4p_49_2, R.id.iv_4p_50_2, R.id.iv_4p_51_2, R.id.iv_4p_52_2, R.id.iv_4p_53_2, R.id.iv_4p_54_2, R.id.iv_4p_55_2},
            {R.id.iv_4p_56_2, R.id.iv_4p_57_2, R.id.iv_4p_58_2, R.id.iv_4p_59_2, R.id.iv_4p_60_2, R.id.iv_4p_61_2, R.id.iv_4p_62_2, R.id.iv_4p_63_2},
            {R.id.iv_4p_64_2, R.id.iv_4p_65_2, R.id.iv_4p_66_2, R.id.iv_4p_67_2, R.id.iv_4p_68_2, R.id.iv_4p_69_2, R.id.iv_4p_70_2, R.id.iv_4p_71_2},
            {R.id.iv_4p_72_2, R.id.iv_4p_73_2, R.id.iv_4p_74_2, R.id.iv_4p_75_2, R.id.iv_4p_76_2, R.id.iv_4p_77_2, R.id.iv_4p_78_2, R.id.iv_4p_79_2},
            {R.id.iv_4p_80_2, R.id.iv_4p_81_2, R.id.iv_4p_82_2, R.id.iv_4p_83_2, R.id.iv_4p_84_2, R.id.iv_4p_85_2, R.id.iv_4p_86_2, R.id.iv_4p_87_2},
            {R.id.iv_4p_88_2, R.id.iv_4p_89_2, R.id.iv_4p_90_2, R.id.iv_4p_91_2, R.id.iv_4p_92_2, R.id.iv_4p_93_2, R.id.iv_4p_94_2, R.id.iv_4p_95_2},
            {R.id.iv_4p_96_2, R.id.iv_4p_97_2, R.id.iv_4p_98_2, R.id.iv_4p_99_2, R.id.iv_4p_100_2, R.id.iv_4p_101_2, R.id.iv_4p_102_2, R.id.iv_4p_103_2},
            {R.id.iv_4p_104_2, R.id.iv_4p_105_2, R.id.iv_4p_106_2, R.id.iv_4p_107_2, R.id.iv_4p_108_2, R.id.iv_4p_109_2, R.id.iv_4p_110_2, R.id.iv_4p_111_2}
    };


    private int[][] sub2_idArray = {
            {R.id.iv_4p_00_3, R.id.iv_4p_01_3, R.id.iv_4p_02_3, R.id.iv_4p_03_3, R.id.iv_4p_04_3, R.id.iv_4p_05_3, R.id.iv_4p_06_3, R.id.iv_4p_07_3},
            {R.id.iv_4p_08_3, R.id.iv_4p_09_3, R.id.iv_4p_10_3, R.id.iv_4p_11_3, R.id.iv_4p_12_3, R.id.iv_4p_13_3, R.id.iv_4p_14_3, R.id.iv_4p_15_3},
            {R.id.iv_4p_16_3, R.id.iv_4p_17_3, R.id.iv_4p_18_3, R.id.iv_4p_19_3, R.id.iv_4p_20_3, R.id.iv_4p_21_3, R.id.iv_4p_22_3, R.id.iv_4p_23_3},
            {R.id.iv_4p_24_3, R.id.iv_4p_25_3, R.id.iv_4p_26_3, R.id.iv_4p_27_3, R.id.iv_4p_28_3, R.id.iv_4p_29_3, R.id.iv_4p_30_3, R.id.iv_4p_31_3},
            {R.id.iv_4p_32_3, R.id.iv_4p_33_3, R.id.iv_4p_34_3, R.id.iv_4p_35_3, R.id.iv_4p_36_3, R.id.iv_4p_37_3, R.id.iv_4p_38_3, R.id.iv_4p_39_3},
            {R.id.iv_4p_40_3, R.id.iv_4p_41_3, R.id.iv_4p_42_3, R.id.iv_4p_43_3, R.id.iv_4p_44_3, R.id.iv_4p_45_3, R.id.iv_4p_46_3, R.id.iv_4p_47_3},
            {R.id.iv_4p_48_3, R.id.iv_4p_49_3, R.id.iv_4p_50_3, R.id.iv_4p_51_3, R.id.iv_4p_52_3, R.id.iv_4p_53_3, R.id.iv_4p_54_3, R.id.iv_4p_55_3},
            {R.id.iv_4p_56_3, R.id.iv_4p_57_3, R.id.iv_4p_58_3, R.id.iv_4p_59_3, R.id.iv_4p_60_3, R.id.iv_4p_61_3, R.id.iv_4p_62_3, R.id.iv_4p_63_3},
            {R.id.iv_4p_64_3, R.id.iv_4p_65_3, R.id.iv_4p_66_3, R.id.iv_4p_67_3, R.id.iv_4p_68_3, R.id.iv_4p_69_3, R.id.iv_4p_70_3, R.id.iv_4p_71_3},
            {R.id.iv_4p_72_3, R.id.iv_4p_73_3, R.id.iv_4p_74_3, R.id.iv_4p_75_3, R.id.iv_4p_76_3, R.id.iv_4p_77_3, R.id.iv_4p_78_3, R.id.iv_4p_79_3},
            {R.id.iv_4p_80_3, R.id.iv_4p_81_3, R.id.iv_4p_82_3, R.id.iv_4p_83_3, R.id.iv_4p_84_3, R.id.iv_4p_85_3, R.id.iv_4p_86_3, R.id.iv_4p_87_3},
            {R.id.iv_4p_88_3, R.id.iv_4p_89_3, R.id.iv_4p_90_3, R.id.iv_4p_91_3, R.id.iv_4p_92_3, R.id.iv_4p_93_3, R.id.iv_4p_94_3, R.id.iv_4p_95_3},
            {R.id.iv_4p_96_3, R.id.iv_4p_97_3, R.id.iv_4p_98_3, R.id.iv_4p_99_3, R.id.iv_4p_100_3, R.id.iv_4p_101_3, R.id.iv_4p_102_3, R.id.iv_4p_103_3},
            {R.id.iv_4p_104_3, R.id.iv_4p_105_3, R.id.iv_4p_106_3, R.id.iv_4p_107_3, R.id.iv_4p_108_3, R.id.iv_4p_109_3, R.id.iv_4p_110_3, R.id.iv_4p_111_3}
    };

    private int[][] sub3_idArray = {
            {R.id.iv_4p_00_4, R.id.iv_4p_01_4, R.id.iv_4p_02_4, R.id.iv_4p_03_4, R.id.iv_4p_04_4, R.id.iv_4p_05_4, R.id.iv_4p_06_4, R.id.iv_4p_07_4},
            {R.id.iv_4p_08_4, R.id.iv_4p_09_4, R.id.iv_4p_10_4, R.id.iv_4p_11_4, R.id.iv_4p_12_4, R.id.iv_4p_13_4, R.id.iv_4p_14_4, R.id.iv_4p_15_4},
            {R.id.iv_4p_16_4, R.id.iv_4p_17_4, R.id.iv_4p_18_4, R.id.iv_4p_19_4, R.id.iv_4p_20_4, R.id.iv_4p_21_4, R.id.iv_4p_22_4, R.id.iv_4p_23_4},
            {R.id.iv_4p_24_4, R.id.iv_4p_25_4, R.id.iv_4p_26_4, R.id.iv_4p_27_4, R.id.iv_4p_28_4, R.id.iv_4p_29_4, R.id.iv_4p_30_4, R.id.iv_4p_31_4},
            {R.id.iv_4p_32_4, R.id.iv_4p_33_4, R.id.iv_4p_34_4, R.id.iv_4p_35_4, R.id.iv_4p_36_4, R.id.iv_4p_37_4, R.id.iv_4p_38_4, R.id.iv_4p_39_4},
            {R.id.iv_4p_40_4, R.id.iv_4p_41_4, R.id.iv_4p_42_4, R.id.iv_4p_43_4, R.id.iv_4p_44_4, R.id.iv_4p_45_4, R.id.iv_4p_46_4, R.id.iv_4p_47_4},
            {R.id.iv_4p_48_4, R.id.iv_4p_49_4, R.id.iv_4p_50_4, R.id.iv_4p_51_4, R.id.iv_4p_52_4, R.id.iv_4p_53_4, R.id.iv_4p_54_4, R.id.iv_4p_55_4},
            {R.id.iv_4p_56_4, R.id.iv_4p_57_4, R.id.iv_4p_58_4, R.id.iv_4p_59_4, R.id.iv_4p_60_4, R.id.iv_4p_61_4, R.id.iv_4p_62_4, R.id.iv_4p_63_4},
            {R.id.iv_4p_64_4, R.id.iv_4p_65_4, R.id.iv_4p_66_4, R.id.iv_4p_67_4, R.id.iv_4p_68_4, R.id.iv_4p_69_4, R.id.iv_4p_70_4, R.id.iv_4p_71_4},
            {R.id.iv_4p_72_4, R.id.iv_4p_73_4, R.id.iv_4p_74_4, R.id.iv_4p_75_4, R.id.iv_4p_76_4, R.id.iv_4p_77_4, R.id.iv_4p_78_4, R.id.iv_4p_79_4},
            {R.id.iv_4p_80_4, R.id.iv_4p_81_4, R.id.iv_4p_82_4, R.id.iv_4p_83_4, R.id.iv_4p_84_4, R.id.iv_4p_85_4, R.id.iv_4p_86_4, R.id.iv_4p_87_4},
            {R.id.iv_4p_88_4, R.id.iv_4p_89_4, R.id.iv_4p_90_4, R.id.iv_4p_91_4, R.id.iv_4p_92_4, R.id.iv_4p_93_4, R.id.iv_4p_94_4, R.id.iv_4p_95_4},
            {R.id.iv_4p_96_4, R.id.iv_4p_97_4, R.id.iv_4p_98_4, R.id.iv_4p_99_4, R.id.iv_4p_100_4, R.id.iv_4p_101_4, R.id.iv_4p_102_4, R.id.iv_4p_103_4},
            {R.id.iv_4p_104_4, R.id.iv_4p_105_4, R.id.iv_4p_106_4, R.id.iv_4p_107_4, R.id.iv_4p_108_4, R.id.iv_4p_109_4, R.id.iv_4p_110_4, R.id.iv_4p_111_4}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players4);


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
