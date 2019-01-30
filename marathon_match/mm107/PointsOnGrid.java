package topcoder.marathon_match.mm107;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
sampleInput

8
8
3
4
0
5
8
60856174
75928915
89719086
16627659
32982909
13815950
55717858
91260290

*/

public class PointsOnGrid {
    public String[] findSolution(int H, int W, int h, int w, int Kmin, int Kmax, String[] grid)
    {
        int[][] real_grid = new int[H][];
        int[][] virtual_grid = new int[h][];
        boolean[][] paintedMap = new boolean[H][];
        for (int r = 0; r < H; r++) {
            real_grid[r] = new int[W];
            if (r < h) {
                virtual_grid[r] = new int[w];
            }
            paintedMap[r] = new boolean[W];
            for (int c = 0; c < W; c++) {
                int num = grid[r].charAt(c) - '0';
                real_grid[r][c] = num;
                virtual_grid[r % h][c % w] += num;
            }
        }

        int[] hist = new int[100000];
        for (int sr = 0; sr < h; sr++) {
            for (int sc = 0; sc < w; sc++) {
                hist[virtual_grid[sr][sc]]++;
            }
        }

        int[] canUse = new int[100000];
        {
            int rest = Kmax;
            for (int border = 99999; border >= 0; border--) {
                int use = Math.min(hist[border], rest);
                rest -= use;
                canUse[border] += use;
            }
        }

        boolean[][] stamp = new boolean[h][];
        for (int sr = 0; sr < h; sr++) {
            stamp[sr] = new boolean[w];
            for (int sc = 0; sc < w; sc++) {
                int num = virtual_grid[sr][sc];
                if (canUse[num] > 0) {
                    canUse[num]--;
                    stamp[sr][sc] = true;
                }
            }
        }

        long score = 0;
        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) {
                int ir = r % h;
                int ic = c % w;
                if (stamp[ir][ic]) {
                    paintedMap[r][c] = true;
                    score += real_grid[r][c];
                }
            }
        }

        BinaryIndexedTree2D bit = new BinaryIndexedTree2D(H, W);
        for (int r = 0; r < H; r++) {
            for (int c = 1; c < W - 1; c++) {
                if (paintedMap[r][c - 1] && !paintedMap[r][c] && paintedMap[r][c + 1]) {
                    if (real_grid[r][c - 1] + real_grid[r][c + 1] < real_grid[r][c]) {
                        if (bit.getSum(r, c - 1) > Kmin - Kmax && bit.getSum(r, c + 1) > Kmin - Kmax) {
                            paintedMap[r][c - 1] = false;
                            paintedMap[r][c] = true;
                            paintedMap[r][c + 1] = false;
                            score -= real_grid[r][c - 1];
                            score += real_grid[r][c];
                            score -= real_grid[r][c + 1];
                            addRect(bit, -1, r, c - 1, h, w, H, W);
                            addRect(bit, 1, r, c, h, w, H, W);
                            addRect(bit, -1, r, c + 1, h, w, H, W);
                        }
                    }
                }
            }
        }

        for (int c = 0; c < W; c++) {
            for (int r = 1; r < H - 1; r++) {
                if (paintedMap[r - 1][c] && !paintedMap[r][c] && paintedMap[r + 1][c]) {
                    if (real_grid[r - 1][c] + real_grid[r + 1][c] < real_grid[r][c]) {
                        if (bit.getSum(r - 1, c) > Kmin - Kmax && bit.getSum(r + 1, c) > Kmin - Kmax) {
                            paintedMap[r - 1][c] = false;
                            paintedMap[r][c] = true;
                            paintedMap[r + 1][c] = false;
                            score -= real_grid[r - 1][c];
                            score += real_grid[r][c];
                            score -= real_grid[r + 1][c];
                            addRect(bit, -1, r - 1, c, h, w, H, W);
                            addRect(bit, 1, r, c, h, w, H, W);
                            addRect(bit, -1, r + 1, c, h, w, H, W);
                        }
                    }
                }
            }
        }

        System.err.println(score);

        String[] ret = new String[H];
        for (int r = 0; r < H; r++) {
            ret[r] = "";
            for (int c = 0; c < W; c++) {
                if (paintedMap[r][c]) {
                    ret[r] += 'x';
                } else {
                    ret[r] += '.';
                }
            }
        }
        return ret;
    }

    private void addRect(BinaryIndexedTree2D bit, int value, int r, int c, int h, int w, int H, int W) {
        int top = Math.max(0, r - h + 1);
        int left = Math.max(0, c - w + 1);
        bit.add(top, left, value);
        if (r + h < H) {
            bit.add(r + h, left, -value);
        }
        if (c + w < W) {
            bit.add(top, c + w, -value);
        }
        if (r + h < H && c + w < W) {
            bit.add(r + h, c + w, value);
        }
    }

    /**
     * 0-indexedの2次元BIT配列
     */
    class BinaryIndexedTree2D {
        private long[][] array;

        public BinaryIndexedTree2D(int size1, int size2) {
            this.array = new long[size1 + 1][];
            for (int i = 1; i <= size1; i++) {
                this.array[i] = new long[size2 + 1];
            }
        }

        /**
         * 指定した要素に値を加算する
         * 計算量はO(logN * logN)
         * @param index1 加算する要素の1次元目の添字
         * @param index2 加算する要素の2次元目の添字
         * @param value 加算する量
         */
        public void add(int index1, int index2, long value) {
            index1++;
            index2++;
            for (int i1 = index1; i1 < array.length; i1 += (i1 & -i1)) {
                for (int i2 = index2; i2 < array.length; i2 += (i2 & -i2)) {
                    array[i1][i2] += value;
                }
            }
        }

        /**
         * (0,0)〜指定した要素までの矩形和を取得する
         * 計算量はO(logN * logN)
         * @param index1 和の終端となる要素の1次元目の添字
         * @param index2 和の終端となる要素の2次元目の添字
         * @return (1,1)〜(index1,index2)までの矩形和
         */
        public long getSum(int index1, int index2) {
            index1++;
            index2++;
            long sum = 0L;
            for (int i1 = index1; i1 > 0; i1 -= (i1 & -i1)) {
                for (int i2 = index2; i2 > 0; i2 -= (i2 & -i2)) {
                    sum += array[i1][i2];
                }
            }
            return sum;
        }
    }

    // -------8<------- end of solution submitted to the website -------8<-------

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            int H = Integer.parseInt(br.readLine());
            int W = Integer.parseInt(br.readLine());
            int h = Integer.parseInt(br.readLine());
            int w = Integer.parseInt(br.readLine());
            int Kmin = Integer.parseInt(br.readLine());
            int Kmax = Integer.parseInt(br.readLine());
            String[] grid=new String[Integer.parseInt(br.readLine())];
            for (int i=0; i<H; i++)
                grid[i]=br.readLine();

            PointsOnGrid pog = new PointsOnGrid();
            String[] ret = pog.findSolution(H,W,h,w,Kmin,Kmax,grid);

            System.out.println(ret.length);
            for (int i = 0; i < ret.length; ++i) {
                System.out.println(ret[i]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}