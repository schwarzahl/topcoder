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
        int slideH = H - h + 1;
        int slideW = W - w + 1;

        int[] hist = new int[10];

        int[][] real_grid = new int[H][];
        boolean[][] paintedMap = new boolean[H][];
        for (int r = 0; r < H; r++) {
            real_grid[r] = new int[W];
            paintedMap[r] = new boolean[W];
            for (int c = 0; c < W; c++) {
                int num = grid[r].charAt(c) - '0';
                real_grid[r][c] = num;
                hist[num]++;
            }
        }

        int initNum = Kmax * H * W / h / w;
        int paintSum = 0;
        int paintLowerLimit;
        for (paintLowerLimit = 9; paintSum < initNum; paintLowerLimit--) {
            paintSum += hist[paintLowerLimit];
        }

        int[][] paintCount = new int[H][];
        for (int r = 0; r < H; r++) {
            paintCount[r] = new int[W];
            for (int c = 0; c < W; c++) {
                if (real_grid[r][c] > paintLowerLimit) {
                    paintedMap[r][c] = true;
                    for (int tr = Math.max(0, r - h + 1); tr <= r; tr++) {
                        for (int tc = Math.max(0, c - w + 1); tc <= c; tc++) {
                            paintCount[tr][tc]++;
                        }
                    }
                }
            }
        }

        for (int tr = 0; tr <= H - h; tr++) {
            for (int tc = 0; tc <= W - w; tc++) {
                if (paintCount[tr][tc] > Kmax) {
                    for (int r = tr; r < Math.min(H, tr + h); r++) {
                        for (int c = tc; c < Math.min(W, tc + w); c++) {
                            if (paintCount[tr][tc] <= Kmax) {
                                break;
                            }
                            if (paintedMap[r][c]) {
                                paintedMap[r][c] = false;
                                for (int sr = Math.max(0, r - h + 1); sr <= r; r++) {
                                    for (int sc = Math.max(0, c - w + 1); sc <= c; sc++) {
                                        paintCount[sr][sc]--;
                                    }
                                }
                            }
                        }
                    }
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