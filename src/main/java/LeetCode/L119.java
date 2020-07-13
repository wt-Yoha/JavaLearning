package LeetCode;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class L119 {
    public List<Integer> getRow(int rowIndex) {
        if (rowIndex <= 0) {
            return new ArrayList<>(Arrays.asList(1));
        }
        Integer[] lastLine = new Integer[rowIndex], currentLine = new Integer[rowIndex + 1];
        Arrays.fill(lastLine, 0);
        Arrays.fill(currentLine, 0);
        int currentLevel = 1;
        lastLine[0] = 1;
        currentLine[0] = 1;
        for (; currentLevel <= rowIndex; currentLevel++) {
            for (int i = 1; i <= currentLevel; i++) {
                currentLine[i] = lastLine[i-1] + (i >= lastLine.length ? 0 : lastLine[i]);
            }
            if (currentLevel == rowIndex) {
                break;
            }
            System.arraycopy(currentLine, 1, lastLine, 1, currentLevel);
        }
        return new ArrayList<>(Arrays.asList(currentLine));
    }

    public static void main(String[] args) {
        int k = 2;
        L119 demo = new L119();
        System.out.println(demo.getRow(k));
    }
}
