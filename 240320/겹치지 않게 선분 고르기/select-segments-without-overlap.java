import java.util.*;
import java.io.*;

public class Main {
    private static List<Node> lines = new ArrayList<>();
    private static List<Node> useLines = new ArrayList<>();
    private static int n;
    private static int answer = 0;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        for (int i = 0; i<n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            lines.add(new Node(x, y));
        }

        for (int i = n; i >= 1; i--) {
            int tmp = answer;
            comb(0, i, 0);

            if (tmp != answer) {
                break;
            }
        }

        System.out.println(answer);
    }

    private static void comb(int idx, int maxSize, int cnt) {
        if (cnt == maxSize) {
            if (validate()) {
                answer = maxSize;
                System.out.println(answer);
                System.exit(0);
            }
            return;
        }

        useLines.add(lines.get(idx));
        comb(idx + 1, maxSize, cnt + 1);
        useLines.remove(useLines.size() - 1);
    }

    //선택한 선분이 겹치는지 검증한다.
    private static boolean validate() {
        for (int i = 0; i<useLines.size() - 1; i++) {
            Node node1 = useLines.get(i);
            for (int j = i + 1; j<useLines.size(); j++) {
                Node node2 = useLines.get(j);
                if (node1.x <= node2.x && node1.y >= node2.x) {
                    return false;
                }

                if (node2.x <= node1.x && node2.y >= node1.x) {
                    return false;
                }
             }
        }
        return true;
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