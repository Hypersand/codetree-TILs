import java.util.*;
import java.io.*;

public class Main {
    private static int n, m;
    private static int[] answerArr;
    private static List<Point> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        answerArr = new int[n];
        int maxM = 0;
        for (int i = 0; i<m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            list.add(new Point(b - 1, a - 1));
            maxM = Math.max(maxM, b);
        }

        int[][] arr = new int[maxM][n];
        for (int i = 0; i<m; i++) {
            Point p = list.get(i);
            arr[p.x][p.y] = 1;
        }

        answerArr = validate(arr);

        for (int i = 0; i <= m; i++) {
            comb(0, i, new int[maxM][n], 0);
        }
 
    }

    private static int[] validate(int[][] map) {
        int[] result = new int[n];
        //1번 사람부터 n번 사람까지의 결과를 추출한다.
        for (int i = 0; i<n; i++) {
            int nx = 0;
            int ny = i;
            while (nx < map.length) {
                if (map[nx][ny] == 1) {
                    ny += 1;
                    nx += 1;
                    continue;
                }
                if (ny - 1 >= 0 && map[nx][ny - 1] == 1) {
                    ny -= 1;
                    nx += 1;
                    continue;
                }
                nx += 1;
            }

            result[i] = ny;
        }

        return result;
    }

    private static void comb(int idx, int max, int[][] map, int cnt) {
        if (cnt == max) {
            int[] result = validate(map);
            if (Arrays.equals(result, answerArr)) {
                System.out.println(max);
                System.exit(0);
            }
            return;
        }

        for (int i = idx; i < m; i++) {
            map[list.get(i).x][list.get(i).y] = 1;
            comb(i + 1, max, map, cnt + 1);
            map[list.get(i).x][list.get(i).y] = 0;
        }
    }

    private static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}