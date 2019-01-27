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
        boolean[][] paintedMap = new boolean[H][];
        for (int r = 0; r < H; r++) {
            real_grid[r] = new int[W];
            paintedMap[r] = new boolean[W];
            for (int c = 0; c < W; c++) {
                int num = grid[r].charAt(c) - '0';
                real_grid[r][c] = num;
            }
        }

        int[] hist = new int[10];
        for (int sr = 0; sr < h; sr++) {
            for (int sc = 0; sc < w; sc++) {
                hist[real_grid[sr][sc]]++;
            }
        }

        int[] canUse = new int[10];
        {
            int rest = Kmax;
            for (int border = 9; border >= 0; border--) {
                int use = Math.min(hist[border], rest);
                rest -= use;
                canUse[border] += use;
            }
        }

        boolean[][] stamp = new boolean[h][];
        for (int sr = 0; sr < h; sr++) {
            stamp[sr] = new boolean[w];
            for (int sc = 0; sc < w; sc++) {
                int num = real_grid[sr][sc];
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