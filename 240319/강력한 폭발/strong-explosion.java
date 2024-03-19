import java.util.*;
import java.io.*;

public class Main {
    private static int n;
    private static int[][] arr;
    private static List<Node> bombs = new ArrayList<>();
    private static int[] bombTypes;
    private static int answer = 0;
    private static int[] dx1 = {1, 2, -1, -2};
    private static int[] dx2 = {-1, 1, 0, 0};
    private static int[] dy2 = {0, 0, -1, 1};
    private static int[] dx3 = {-1, 1, 1, -1};
    private static int[] dy3 = {-1, 1, -1, 1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        arr = new int[n][n];

        for (int i = 0; i<n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j<n; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
                if (arr[i][j] == 1) {
                    bombs.add(new Node(i, j));
                }
            }
        }
        bombTypes = new int[bombs.size()];

        select(0);

        System.out.println(answer);
    }


    private static void select(int idx) {
        if (idx == bombs.size()) {
            //폭탄 터뜨리기
            explode();
            return;
        }

        for (int i = 1; i<=3; i++) {
            bombTypes[idx] = i;
            select(idx + 1);
        }
    }

    private static void explode() {
        int cnt = 0;
        boolean[][] isExploded = new boolean[n][n];
        for (int i = 0; i < bombs.size(); i++) {
            Node node = bombs.get(i);
            int curX = node.x;
            int curY = node.y;
            if (!isExploded[curX][curY]) {
                isExploded[curX][curY] = true;
                cnt++;
            }
            
            if (bombTypes[i] == 1) {
                for (int j = 0; j<dx1.length; j++) {
                    int nx = curX + dx1[j];
                    int ny = curY;
                    if (isNotOutOfBounds(nx, ny) && !isExploded[nx][ny]) {
                        isExploded[nx][ny] = true;
                        cnt++;
                    }
                }

            }

            else if (bombTypes[i] == 2) {
                for (int j = 0; j<dx2.length; j++) {
                    int nx = curX + dx2[j];
                    int ny = curY + dy2[j];
                    if (isNotOutOfBounds(nx, ny) && !isExploded[nx][ny]) {
                        isExploded[nx][ny] = true;
                        cnt++;
                    }
                }

            }

            else {
                for (int j = 0; j<dx3.length; j++) {
                    int nx = curX + dx3[j];
                    int ny = curY + dy3[j];
                    if (isNotOutOfBounds(nx, ny) && !isExploded[nx][ny]) {
                        isExploded[nx][ny] = true;
                        cnt++;
                    }
                }

            }
        }

        answer = Math.max(answer, cnt);
    }

    private static boolean isNotOutOfBounds(int x, int y) {
        return (x < n && y < n && x >= 0 && y >= 0);
    }



    private static class Node {
        private int x;
        private int y;
         
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}