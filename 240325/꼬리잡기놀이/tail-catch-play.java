import java.util.*;
import java.io.*;

public class Main {
    private static int n, m, k;
    private static int[][] map;
    private static Queue<Node> queue = new LinkedList<>();
    private static int score = 0;
    private static int[] dx = {-1, 1, 0, 0}; //상, 하
    private static int[] dy = {0, 0, -1, 1}; //좌, 우
    private static boolean[][] visited;
    private static List<Node> list;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        map = new int[n][n];
        for (int i = 0; i<n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j<n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if (map[i][j] == 1) {
                    queue.add(new Node(i, j));
                }
            }
        }
        
        for (int i = 1; i<=k; i++) {
            //매 라운드마다 게임 진행
            playGame(i);
        }

        System.out.println(score);
    }

    private static void playGame(int round) {
        //1. 각 팀은 머리사람을 따라서 한 칸 이동한다.
        visited = new boolean[n][n];
        while (!queue.isEmpty()) {
            list = new ArrayList<>();
            Node head = queue.poll();
            list.add(head);
            visited[head.x][head.y] = true;
            move(head.x, head.y, 0);
            
            int x = 0;
            int y = 0;
            for (int i = 0; i<4; i++) {
                int nx = head.x + dx[i];
                int ny = head.y + dy[i];
                if (nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if (map[nx][ny] == 4 || map[nx][ny] == 3) {
                    x = nx;
                    y = ny;
                    break;
                }
            }

            int tmp = map[x][y];
            for (int i = 0; i<list.size() - 1; i++) {
                Node node = list.get(i);
                System.out.println(x + " " + y + " ----" + map[node.x][node.y]);
                map[x][y] = map[node.x][node.y];
                x = node.x;
                y = node.y;

                if (tmp == 3 && i == list.size() - 2) {
                    map[x][y] = 3;
                }
            }

            Node tmpNode = list.get(list.size() - 1);
            if (tmp == 4) {
                map[tmpNode.x][tmpNode.y] = 3;
            } else {
                map[tmpNode.x][tmpNode.y] = 1;
            }
        }

        for (int i = 0; i<n; i++) {
            for (int j = 0; j<n; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }


        //2. 라운드에 맞게 공이 정해진 선을 따라서 던져진다.
        throwBall(round);

        //3. 큐에 다시 머리 채우기
        for (int i = 0; i<n; i++) {
            for (int j = 0; j<n; j++) {
                if (map[i][j] == 1) {
                    queue.add(new Node(i, j));
                }
            }
        }

    }

    private static void move(int x, int y, int idx) {
        for (int i = 0; i<4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx < 0 || ny < 0 || nx >=n || ny >=n) continue;
            if (map[nx][ny] == 0 || map[nx][ny] == 4) continue;
            if (visited[nx][ny]) continue;
            if (idx == 0 && map[nx][ny] != 2) continue;
            list.add(new Node(nx, ny));
            visited[nx][ny] = true;
            move(nx, ny, idx + 1);
        }       
    }


    private static void throwBall(int round) {
        round = round % (4 * n);
        if (round == 0) round = 4 * n;

        int dir = 0; //상, 하, 좌, 우
        int row = 0;
        int col = 0;

        if (round <= n) {
            row = round - 1;
            col = 0;
            dir = 3;
            
        } else if (round <= 2 * n) {
            row = n - 1;
            col = round % (n + 1);
            dir = 0;

        } else if (round <= 3 * n) {
            row = Math.abs(round % (n * 2 + 1) - (n - 1));
            col = n - 1;
            dir = 2;

        } else {
            row = 0;
            col = Math.abs(round % (n * 3 + 1) - (n - 1));
            dir = 1;
        }

        //3. 공 발사
        int nRow = row - dx[dir];
        int nCol = col - dy[dir];
        while (true) {
            nRow += dx[dir];
            nCol += dy[dir];
            if (nRow < 0 || nCol < 0 || nRow >= n || nCol >= n) break;
            if (map[nRow][nCol] == 0 || map[nRow][nCol] == 4) continue;        
            //공에 맞은 사람 점수 계산
            //해당 사람의 위치를 시작으로 머리 사람까지 dist 계산하면 점수 계산 가능
            score += findHead(nRow, nCol);
            break;
        }
    }

    private static int findHead(int x, int y) {
        int[][] dists = new int[n][n];
        Queue<Node> q = new LinkedList<>();
        q.add(new Node(x, y));
        dists[x][y] = 1;
        int tmpScore = 0;
        int tail_x = 0;
        int tail_y = 0;
        int head_x = 0;
        int head_y = 0;

        while (!q.isEmpty()) {
            Node node = q.poll();
            if (map[node.x][node.y] == 1) {
                head_x = node.x;
                head_y = node.y;
                tmpScore = (int)Math.pow(dists[node.x][node.y], 2);
            }

            if (map[node.x][node.y] == 3) {
                tail_x = node.x;
                tail_y = node.y;
            }

            for (int i = 0; i < 4; i++) {
                int nx = node.x + dx[i];
                int ny = node.y + dy[i];
                if (nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if (dists[nx][ny] != 0) continue;
                if (map[nx][ny] == 0 || map[nx][ny] == 4) continue;
                q.add(new Node(nx, ny));
                dists[nx][ny] = dists[node.x][node.y] + 1;
            }
        }
        map[tail_x][tail_y] = 1;
        map[head_x][head_y] = 3;
        return tmpScore;
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