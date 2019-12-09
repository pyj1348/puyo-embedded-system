package com.example.puyo;

/**
 * Homework Assignment #: "8-Puzzle"
 *
 *  - Board class for solving "8-Puzzle" Programming Assignment
 *  - 2018 version
 *
 *  Compilation:  javac Board.java Queue.java
 *
 * @ Student ID :20151363
 * @ Name       :juseokcheon
 **/

import android.graphics.Point;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Base64;
import java.util.BitSet;

public class Board {
    static final int hasharr[] = { 000, 001, 002, 003, 004, 010, 011, 012, 013, 014, 020, 021, 022, 023, 024, 030, 031,
            032, 033, 034, 040, 041, 042, 043, 044, 100, 101, 102, 103, 104, 110, 111, 112, 113, 114, 120, 121, 122,
            123, 124, 130, 131, 132, 133, 134, 140, 141, 142, 143, 144, 200, 201, 202, 203, 204, 210, 211, 212, 213,
            214, 220, 221, 222, 223, 224, 230, 231, 232, 233, 234, 240, 241, 242, 243, 244, 300, 301, 302, 303, 304,
            310, 311, 312, 313, 314, 320, 321, 322, 323, 324, 330, 331, 332, 333, 334, 340, 341, 342, 343, 344, 400,
            401, 402, 403, 404, 410, 411, 412, 413, 414, 420, 421, 422, 423, 424, 430, 431, 432, 433, 434, 440, 441,
            442, 443, 444 };

    private int[][] mTiles;
    private int x = 8;
    private int y = 14;
    private Puyo handling_puyo;
    private Point position_puyo;
    private Point initial_puyo;
    private PuyoQueue<Puyo> puyolist;
    static int[] dx = { -1, 0, 1, 0 };
    static int[] dy = { 0, -1, 0, 1 };
    static boolean[][] visited;
    static ArrayList<Point> poplist;
    HashMap<Integer, Integer> arrMap;

    // create a board from an n-by-n array of tiles
    // {where tiles[row][col] = tile at (row, col)

    public Board() {
        arrMap = new HashMap<>();
        for (int i = 0; i < 125; i++)
            arrMap.put(i, hasharr[i]);
        puyolist = new PuyoQueue<Puyo>();
        initial_puyo = new Point(3, 1);
        puyolist.enqueue(new Puyo());
        puyolist.enqueue(new Puyo());

        mTiles = new int[x][y];
        for (int i = 0; i < x; i++)
            for (int k = 0; k < y; k++)
                mTiles[i][k] = 0;
    }

    public Board(int[][] tiles) {
        puyolist = new PuyoQueue<Puyo>();
        initial_puyo = new Point(3, 1);
        puyolist.enqueue(new Puyo());
        puyolist.enqueue(new Puyo());

        if (tiles == null)
            throw new NullPointerException();

        mTiles = new int[x][y];
        for (int i = 0; i < x; i++)
            System.arraycopy(tiles[i], 0, mTiles[i], 0, tiles[i].length);

    }

    public Board(int[][] tiles, PuyoQueue<Puyo> retrived) {
        puyolist = new PuyoQueue<Puyo>(retrived);
        initial_puyo = new Point(3, 1);
        if (tiles == null)
            throw new NullPointerException();
        mTiles = new int[x][y];
        for (int i = 0; i < x; i++)
            System.arraycopy(tiles[i], 0, mTiles[i], 0, tiles[i].length);
    }

    // tile at (row, col) or 0 if blank
    public int tileAt(int row, int col) {
        if (row < 0 || col < 0 || row > x - 1 || col > y - 1)
            throw new IllegalArgumentException("invalid row/col index");

        return mTiles[row][col];
    }

    public int[][] getboard() {
        int[][] tiles = new int[x][y];
        for (int i = 0; i < x; i++)
            for (int k = 0; k < y; k++)
                tiles[i][k] = mTiles[i][k];
        if (handling_puyo != null) {
            for (int i = -1; i < 2; i++) {
                for (int k = -1; k < 2; k++) {
                    if (position_puyo.x + i >= 0 && position_puyo.y + k >= 0 && position_puyo.y + k < y
                            && position_puyo.x + i < x)
                        tiles[position_puyo.x + i][position_puyo.y
                                + k] = mTiles[position_puyo.x + i][position_puyo.y + k]
                                        + handling_puyo.Getpos()[1 + k][1 + i];

                }
            }
        }
        return tiles;
    }

    public PuyoQueue<Puyo> getPuyoQueue() {
        return puyolist;
    }

    public void move_down() {
        int radius = 0;
        if (this.check(radius)) {
            position_puyo.y = position_puyo.y + 1;
        }
    }

    public void move_left() {
        int radius = 1;
        if (this.check(radius)) {
            position_puyo.x = position_puyo.x - 1;
        }
    }

    public void move_right() {
        int radius = 2;
        if (this.check(radius)) {
            position_puyo.x = position_puyo.x + 1;
        }
    }

    public void spin() {
        int radius = handling_puyo.Getspin() + 2;
        if (this.check(radius)) {
            handling_puyo.spin();
        } else {
            int res = this.extra_spin_check(radius);
            if (radius == 3) {
                if (res == 1)
                    position_puyo.x++;
                else if (res == 2) {
                    position_puyo.y--;
                }
                handling_puyo.spin();

            } else if (radius == 4) {
                position_puyo.y++;
                handling_puyo.spin(6);

            } else if (radius == 5) {
                if (res == 1)
                    position_puyo.x--;
                else if (res == 2) {
                    position_puyo.y++;
                }
                handling_puyo.spin();

            } else if (radius == 6) {
                position_puyo.y--;
                handling_puyo.spin(4);

            }

        }
    }

    public boolean check(int radius) {
        for (int i = -1; i < 2; i++) {
            for (int k = -1; k < 2; k++) {
                if (radius == 6) {
                    if (position_puyo.y >= 0) {
                        if (mTiles[position_puyo.x][position_puyo.y - 1] != 0
                                || mTiles[position_puyo.x + 1][position_puyo.y - 1] != 0)
                            return false;
                    } else
                        return false;
                } else if (radius == 3) {
                    if (position_puyo.x - 1 >= 1) {

                        if (position_puyo.y + i < 0 || mTiles[position_puyo.x - 1][position_puyo.y - 1] != 0
                                || mTiles[position_puyo.x - 0][position_puyo.y - 1] != 0)
                            return false;
                    } else
                        return false;
                } else if (radius == 4) {
                    if (position_puyo.y + 1 <= 12) {

                        if (mTiles[position_puyo.x + 1][position_puyo.y - 1] != 0
                                || mTiles[position_puyo.x + 1][position_puyo.y + 0] != 0)
                            return false;
                    } else
                        return false;
                } else if (radius == 5) {
                    if (position_puyo.x + 1 <= 6) {
                        if (mTiles[position_puyo.x + 1][position_puyo.y + 1] != 0
                                || mTiles[position_puyo.x + 1][position_puyo.y + 0] != 0)
                            return false;
                    } else
                        return false;
                } else if (radius == 1) {
                    if (handling_puyo.Getpos()[1 + k][1 + i] != 0
                            && (position_puyo.x + i <= 1 || mTiles[position_puyo.x + i - 1][position_puyo.y + k] != 0))
                        return false;
                } else if (radius == 2) {
                    if (handling_puyo.Getpos()[1 + k][1 + i] != 0
                            && (position_puyo.x + i >= 6 || mTiles[position_puyo.x + i + 1][position_puyo.y + k] != 0))
                        return false;
                } else if (radius == 0) {
                    if (handling_puyo.Getpos()[1 + k][1 + i] != 0
                            && (position_puyo.y + k >= 11 || mTiles[position_puyo.x + i][position_puyo.y + k + 1] != 0))
                        return false;
                }
            }
        }

        return true;
    }

    public int extra_spin_check(int radius) {
        if (radius == 3) {
            if (position_puyo.x <= 6) {
                if (mTiles[position_puyo.x - 1][position_puyo.y + 0] == 0)
                    return 1;
                if (mTiles[position_puyo.x - 1][position_puyo.y + 0] != 0
                        && mTiles[position_puyo.x - 1][position_puyo.y - 1] == 0)
                    return 2;
            }
        } else if (radius == 5) {
            if (position_puyo.x >= 1) {
                if (mTiles[position_puyo.x + 1][position_puyo.y + 0] == 0)
                    return 1;
                if (mTiles[position_puyo.x + 1][position_puyo.y + 1] == 0
                        && mTiles[position_puyo.x + 1][position_puyo.y] != 0)
                    return 2;
            }
        }
        return 0;
    }

    public int gen_puyo() {
        if (mTiles[3][1] != 0)
            return 0;
        handling_puyo = puyolist.dequeue();
        position_puyo = new Point(initial_puyo.x, initial_puyo.y);

        puyolist.enqueue(new Puyo());
        return 1;
    }

    public void end_step() {
        for (int i = -1; i < 2; i++) {
            for (int k = -1; k < 2; k++) {
                if (position_puyo.x + i >= 0 && position_puyo.y + k >= 0 && position_puyo.y + k < this.y
                        && position_puyo.x + i < x)
                    mTiles[position_puyo.x + i][position_puyo.y + k] = mTiles[position_puyo.x + i][position_puyo.y + k]
                            + handling_puyo.Getpos()[1 + k][1 + i];
            }
        }
        position_puyo = initial_puyo;
        handling_puyo = null;
    }

    public int clear_board() {
        visited = new boolean[this.x][this.y];
        for (int i = 0; i < this.x; i++)
            for (int k = 0; k < this.y; k++)
                visited[i][k] = false;

        int point = 0;
        for (int i = 0; i < x; i++) {
            for (int k = 0; k < y; k++) {
                poplist = new ArrayList<Point>();
                if (mTiles[i][k] != 0)
                    dfs(i, k, mTiles[i][k]);
                if (poplist.size() >= 4) {
                    point = point + poplist.size();
                    for (Point p : poplist) {
                        mTiles[p.x][p.y] = 0;
                    }
                }
            }
        }
        return point;
    }

    public void update_map() {
        for (int i = 0; i < this.x; i++) {
            for (int k = this.y - 2; k >= 0; k--) {
                if (mTiles[i][k] == 0) {
                    for (int a = k; a >= 0; a--) {
                        if (mTiles[i][a] != 0) {
                            mTiles[i][k] = mTiles[i][a];
                            mTiles[i][a] = 0;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void dfs(int x, int y, int puyo_color) {
        for (int i = 0; i < 4; i++) {
            int nx = dx[i] + x;
            int ny = dy[i] + y;
            if (0 <= nx && nx < this.x && 0 <= ny && ny < this.y) {
                if (!visited[nx][ny] && mTiles[nx][ny] == puyo_color) {
                    poplist.add(new Point(nx, ny));
                    visited[nx][ny] = true;
                    dfs(nx, ny, puyo_color);
                }
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String tostring() {
        BitSet bitset = new BitSet(200);
        int[][] tempboard = this.getboard();
        int stamp = 0;
        for (int i = 0; i < 13; i++)
            for (int k = 0; k < 2; k++) {
                int temp = tempboard[1 + k * 3][i] * 100 + tempboard[2 + k * 3][i] * 10 + tempboard[3 + k * 3][i];
                int temp_res = getKey(arrMap, temp);
                if (temp_res != -1) {
                    String res = Integer.toBinaryString(temp_res);
                    StringBuilder res_temp = new StringBuilder("");

                    for (int q = 0; q < 7 - res.length(); q++)
                        res_temp.append("0");
                    res_temp.append(res);
                    res = res_temp.toString();
                    for (int a = 0; a < 7; a++) {
                        if (a < res.length())
                            if (res.charAt(a) == '1')
                                bitset.set(a + stamp * 7);
                    }
                    stamp++;
                }
            }
        // 00 01 10 11 four
        StringBuilder res_temp = new StringBuilder("");
        Iterator<Puyo> iter = puyolist.iterator();
        for (int a = 0; a < 2; a++) {
            Puyo first = iter.next();
            if (first != null) {
                if (first.Getfirst() - 1 == 1 || first.Getfirst() - 1 == 0)
                    res_temp.append("0");
                res_temp.append(Integer.toBinaryString(first.Getfirst() - 1));
                if (first.Getsecond() - 1 == 1 || first.Getsecond() - 1 == 0)
                    res_temp.append("0");
                res_temp.append(Integer.toBinaryString(first.Getsecond() - 1));
            }
        }
        String qresult = res_temp.toString();
        for (int a = 0; a < 8; a++) {
            if (a < qresult.length())
                if (qresult.charAt(a) == '1')
                    bitset.set(a + stamp * 7);
        }
        byte[] bytes = bitset.toByteArray();
        String result = Base64.getEncoder().encodeToString(bytes);
        return this.compress(result);
    }

    public void board_update(String string) {
        String decompressed = this.decompress(string);
        byte[] bytes = Base64.getDecoder().decode(decompressed);
        BitSet bitset = BitSet.valueOf(bytes);

        mTiles = new int[x][y];
        int stamp = 0;
        for (int i = 0; i < y - 1; i++) {
            for (int k = 0; k < 2; k++) {
                BitSet temp = bitset.get(stamp * 7, stamp * 7 + 7);
                StringBuilder res = new StringBuilder("0000000");
                for (String token : temp.toString().replace("{", "").replace("}", "").replace(" ", "").split(",")) {
                    if (!token.equals("")) {
                        res.setCharAt(Integer.parseInt(token), '1');
                    }
                }

                int tempres = Integer.parseInt(res.toString(), 2);
                int hashres = arrMap.get(tempres);
                mTiles[k * 3 + 1][i] = hashres / 100;
                mTiles[k * 3 + 1 + 1][i] = hashres % 100 / 10;
                mTiles[k * 3 + 1 + 2][i] = hashres % 10;
                stamp++;
            }
        }
        BitSet temp = bitset.get(stamp * 7, stamp * 7 + 8);
        StringBuilder res = new StringBuilder("00000000");
        System.out.print(temp + "" + stamp * 7 + "" + (stamp * 7 + 8));
        for (String token : temp.toString().replace("{", "").replace("}", "").replace(" ", "").split(",")) {
            if (!token.equals("")) {
                res.setCharAt(Integer.parseInt(token), '1');
            }
        }
        int temp1, temp2 = 0;
        String queuestring = res.toString();
        puyolist = new PuyoQueue<Puyo>();
        for (int a = 0; a < 8; a = a + 4) {
            temp1 = Integer.parseInt(queuestring.substring(a, a + 2), 2) + 1;
            temp2 = Integer.parseInt(queuestring.substring(a + 2, a + 4), 2) + 1;
            Puyo temporary = new Puyo(temp1, temp2);
            puyolist.enqueue(temporary);
        }
    }

    public static Integer getKey(HashMap<Integer, Integer> m, Integer value) {
        for (Integer o : m.keySet()) {
            if (m.get(o).equals(value)) {
                return o;
            }
        }
        return -1;
    }
    private static int DICT_SIZE = 256;

    /** Compress a string to a list of output symbols. */
    public String compress(String uncompressed) {
        uncompressed = uft8Encode(uncompressed);
        // Build the dictionary.
        int index = DICT_SIZE;
        Map<String,Integer> dictionary = new HashMap<String,Integer>();
        for (int i = 0; i < DICT_SIZE; i++)
            dictionary.put("" + (char)i, i);

        String w = "";
        StringBuilder res = new StringBuilder();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                int i = dictionary.get(w);
                res.append((char) i);
                // Add wc to the dictionary.
                dictionary.put(wc, index++);
                w = "" + c;
            }
        }

        // Output the code for w.
        if (!w.equals("")) {
            int i = dictionary.get(w);
            res.append((char) i);
        }
        return res.toString();
    }

    /** Decompress a list of output ks to a string. */
    public String decompress(String compressed) {
        // Build the dictionary.
        int index = DICT_SIZE;
        Map<Integer,String> dictionary = new HashMap<Integer,String>();
        for (int i = 0; i < DICT_SIZE; i++)
            dictionary.put(i, "" + (char)i);

        char[] chars = compressed.toCharArray();
        String w = "" + chars[0];
        StringBuffer result = new StringBuffer(w);
        for (int j = 1; j < chars.length; j++) {
            int k = (int) chars[j];
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == index)
                entry = w + w.charAt(0);
            else
                throw new IllegalArgumentException("Bad compressed k: " + k);

            result.append(entry);

            // Add w+entry[0] to the dictionary.
            dictionary.put(index++, w + entry.charAt(0));

            w = entry;
        }
        return utf8Decode(result.toString());
    }

    /**
     * encode utf8 string into char 0 ~ 127
     * @param text
     * @return
     */
    public String uft8Encode(String text) {
        StringBuilder res = new StringBuilder();
        for (char c : text.toCharArray()) {
            int i = (int) c;
            if (i < 128) {
                res.append((char) i);
            } else if (i > 127 && i < 2048) {
                int j = (i >> 6) | 192;
                res.append((char) j);
                j = (i & 63) | 128;
                res.append((char) j);
            } else {
                int j = (i >> 12) | 224;
                res.append((char) j);
                j = ((i >> 6) & 63) | 128;
                res.append((char) j);
                j = (c & 63) | 128;
                res.append((char) j);
            }
        }
        return res.toString();
    }

    /**
     * decode char 0 ~ 127 to utf8 string
     * @param text
     * @return
     */
    public static String utf8Decode(String text) {
        StringBuilder res = new StringBuilder();
        int i = 0;
        char[] chars = text.toCharArray();
        while (i < chars.length) {
            int c = chars[i++];
            if (c > 191 && c < 224) {
                int c1 = chars[i++];
                c = ((c & 31) << 6) | (c1 & 63);
            } else if (c > 127) {
                int c1 = chars[i++];
                int c2 = chars[i++];
                c = ((c & 15) << 12) | ((c1 & 63) << 6) | (c2 & 63);
            }
            res.append((char) c);
        }
        return res.toString();
    }

    /*
     * // unit tests (DO NOT MODIFY) public static void main(String[] args) { Board
     * a = new Board();
     * 
     * for (int q = 0; q < 100; q++) { //at first generate puyo if (a.gen_puyo() ==
     * 0) return; for (int k = 0; k < a.y; k++) { for (int i = 0; i < a.x; i++) {
     * System.out.print(a.getboard()[i][k]); } System.out.println(); }
     * System.out.println(); a.move_left(); a.move_left(); a.move_left();
     * a.move_left(); a.move_left(); a.move_left();
     * 
     * a.move_down(); a.move_down(); a.move_down(); a.move_down();
     * 
     * a.move_down(); a.move_down(); a.move_down(); a.move_down(); a.move_down();
     * a.move_down(); a.move_down(); a.move_down(); a.move_down(); a.move_right();
     * a.move_right(); a.move_right(); a.move_right(); a.move_right();
     * a.move_right(); a.move_right(); a.move_right();
     * 
     * //after move please endwith end_step a.end_step();
     * 
     * for (int k = 0; k < a.y; k++) { for (int i = 0; i < a.x; i++) {
     * System.out.print(a.getboard()[i][k]); } System.out.println(); }
     * System.out.println();
     * 
     * //drop every puyo inthe air a.update_map();
     * 
     * //score/point part clear_board n times to clear whole board int point =
     * a.clear_board(); int multiplier = 1; while (point != 0) { point =
     * multiplier*a.clear_board(); for (int k = 0; k < a.y; k++) { for (int i = 0; i
     * < a.x; i++) { System.out.print(a.getboard()[i][k]); } System.out.println(); }
     * System.out.println(); multiplier = multiplier*4; a.update_map(); } for (int k
     * = 0; k < a.y; k++) { for (int i = 0; i < a.x; i++) {
     * System.out.print(a.getboard()[i][k]); } System.out.println(); }
     * System.out.println(); System.out.println("a.getboard()[i][k]");
     * 
     * }
     * 
     * }
     */
}
