package LeetCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class L120 {
    public int minimumTotal(List<List<Integer>> triangle) {
        if (triangle == null || triangle.size() == 0) {
            return 0;
        }
        int[] lastLine = new int[triangle.size()], currentLine = new int[triangle.size()], tmp;
        Arrays.fill(lastLine, Integer.MAX_VALUE);
        Arrays.fill(currentLine, Integer.MAX_VALUE);
        lastLine[0] = triangle.get(0).get(0);
        int currentLevel = 1;
        for (; currentLevel < triangle.size(); currentLevel++) {
            for (int i = 0; i <= currentLevel; i++) {
                if (i > 0) {
                    currentLine[i] = Math.min(lastLine[i], lastLine[i - 1]) + triangle.get(currentLevel).get(i);
                } else {
                    currentLine[i] = lastLine[i] + triangle.get(currentLevel).get(i);
                }
            }
            tmp = currentLine;
            currentLine = lastLine;
            lastLine = tmp;
        }
        int min = lastLine[0];
        for (int i = 0; i < lastLine.length; i++) {
            if (lastLine[i] < min) {
                min = lastLine[i];
            }
        }
        return min;
    }

    public static void main(String[] args) {
        int[][] map = {
                {2},
                {3, 4},
                {6, 5, 7},
                {4, 1, 8, 3}
        };

        List triangle = new ArrayList();
        for (int i = 0; i < map.length; i++) {
            List line = new ArrayList();
            for (int j = 0; j < map[i].length; j++) {
                line.add(map[i][j]);
            }
            triangle.add(line);
        }

        L120 demo = new L120();
        System.out.println(demo.minimumTotal(triangle));
    }
}
