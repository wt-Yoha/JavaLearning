package LeetCode;

import java.util.*;

public class L130 {
    public void solve(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }

        checkBoard(board, 0, 0, "row");
        checkBoard(board, 0, 0, "clo");
        checkBoard(board, 0, board[0].length - 1, "clo");
        checkBoard(board, board.length - 1, 0, "row");

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                switch (board[i][j]) {
                    case '*' -> board[i][j] = 'O';
                    case 'O' -> board[i][j] = 'X';
                }
            }
        }

    }

    private void checkBoard(char[][] board, int x, int y, String direct) {
        if ("row".equals(direct)) {
            // 扫描行
            for (int j = y; j < board[0].length; j++) {
                if (board[x][j] == 'O') {
                    markLinked(board, x, j);
                }
            }
        } else {
            // 扫描列
            for (int i = 0; i < board.length; i++) {
                if (board[i][y] == 'O') {
                    markLinked(board, i, y);
                }
            }
        }
    }

    class Pos{
        int x;
        int y;

        Pos() {

        }

        Pos(int i, int j) {
            x = i;
            y = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pos pos = (Pos) o;
            return x == pos.x &&
                    y == pos.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private void markLinked(char[][] board, int x, int y) {
        Pos start = new Pos(x, y), tmp, link;
        LinkedHashSet<Pos> linkedArea = new LinkedHashSet<>();
        linkedArea.add(start);
        while (linkedArea.size()>0) {
            tmp = linkedArea.iterator().next();
            linkedArea.remove(tmp);

            board[tmp.x][tmp.y] = '*';

            link = new Pos(tmp.x - 1, tmp.y);
            checkPos(board, link, linkedArea);

            link = new Pos(tmp.x + 1, tmp.y);
            checkPos(board, link, linkedArea);

            link = new Pos(tmp.x, tmp.y - 1);
            checkPos(board, link, linkedArea);


            link = new Pos(tmp.x, tmp.y + 1);
            checkPos(board, link, linkedArea);

        }
    }

    private void checkPos(char[][] board, Pos link, LinkedHashSet<Pos> linkedArea) {
        if (link.x >= 0 && link.x < board.length && link.y >= 0 && link.y < board[0].length) {
            if (board[link.x][link.y] == 'O') {
                linkedArea.add(link);
            }
        }
    }


    public static void main(String[] args) {
//        String s = "X X X X\n" +
//                "X O O X\n" +
//                "X X O X\n" +
//                "X O X X";

        String s = "OOO\nOOO\nOOO";

        s = s.replace(" ", "");
        String[] map = s.split("\n");
        char[][] board = new char[map.length][map[0].length()];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = map[i].charAt(j);
            }
        }
        L130 runner = new L130();
        runner.solve(board);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

}
