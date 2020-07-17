package LeetCode;

import java.rmi.MarshalException;
import java.util.*;

public class L127 {


    class MapNode{
        // 标记与目标节点的距离 为-1时表示未访问到，或不联通
        int len = -1;
        List<String> lastNodes = new LinkedList<>();
        List<String> connectionNodes = new LinkedList<>();
    }


    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        if (beginWord == null || endWord == null || beginWord.length() == 0 || endWord.length() == 0) {
            return 0;
        }
        boolean findEndWord = false;
        boolean findBeginWord = false;

        Map<String, MapNode> map = new HashMap<>();
        for (int i = 0; i < wordList.size(); i++) {
            if (endWord.equals(wordList.get(i))) {
                findEndWord = true;
            }
            if (beginWord.equals(wordList.get(i))) {
                findBeginWord = true;
            }
            map.put(wordList.get(i), new MapNode());
        }

        if (!findEndWord) {
            // endWord 不在集合内
            return 0;
        }
        if (!findBeginWord) {
            map.put(beginWord, new MapNode());
            wordList.add(beginWord);
        }

        // 建图
        String a, b;
        for (int i = 0; i < wordList.size(); i++) {
            for (int j = i + 1; j < wordList.size(); j++) {
                a = wordList.get(i);
                b = wordList.get(j);
                if (checkStringPair(a, b)) {
                    map.get(a).connectionNodes.add(b);
                    map.get(b).connectionNodes.add(a);
                }
            }
        }

        // 计算最短距离
        List<String> currentLine = new LinkedList<>(), nextLine = new LinkedList<>(), tmp;
        MapNode fatherNode, subNode;
        currentLine.add(beginWord);
        map.get(beginWord).len = 0;
        while (currentLine.size() > 0) {
            for (String fa: currentLine) {
                // 取出当前节点
                fatherNode = map.get(fa);
                for (String connectionSubNode : fatherNode.connectionNodes) {
                    // 为所有未访问过的子节点设置length
                    subNode = map.get(connectionSubNode);
                    if (subNode.len == -1 || subNode.len == fatherNode.len + 1) {
                        if (subNode.len == -1) {
                            subNode.len = fatherNode.len + 1;
                            nextLine.add(connectionSubNode);
                        }
                        subNode.lastNodes.add(fa);
                    }
                }
            }
            tmp = currentLine;
            currentLine = nextLine;
            nextLine = tmp;
            nextLine.clear();
        }

        return map.get(endWord).len + 1;
    }

    private boolean checkStringPair(String s1, String s2) {
        // 检查两个字串是否只差一位
        int unSameCount = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                unSameCount++;
                if (unSameCount > 1) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void main(String[] args) {
        String beginWord = "hit", endWord = "cog";
        String[] wordList = {"hot", "dot", "dog", "lot", "log", "cog"};
        L127 runner = new L127();
        System.out.println(runner.ladderLength(beginWord, endWord, new ArrayList<String>(Arrays.asList(wordList))));
    }
}
