package LeetCode;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class L126 {
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        LinkedList<List<String>> res = new LinkedList<>();
        LinkedList<String> resTmp = new LinkedList<>();
        boolean[] chosed = new boolean[wordList.size()];
        boolean[][] sTable = new boolean[wordList.size() + 1][wordList.size() + 1];


        int beginIndex = 0, endIndex = -1;
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).equals(endWord)) {
                endIndex = i;
            }
            if (checkStringPair(beginWord, wordList.get(i))) {
                sTable[0][i + 1] = true;
                sTable[i + 1][0] = true;
            }
            for (int j = i + 1; j < wordList.size(); j++) {
                if (checkStringPair(wordList.get(i), wordList.get(j))) {
                    sTable[i + 1][j + 1] = true;
                    sTable[j + 1][i + 1] = true;
                }
            }
        }

        // 字典中没有endWord，直接退出
        if (endIndex == -1) {
            return res;
        }

        // 将初试词加入结果集
        resTmp.addLast(beginWord);

        findLadders(beginIndex, endIndex, sTable, chosed, wordList, res, resTmp);

        // 对结果集做处理，选取其中最短的序列
//        int minLength = Integer.MAX_VALUE;
//        for (List<String> re : res) {
//            if (re.size() < minLength) {
//                minLength = re.size();
//            }
//        }
//        Iterator<List<String>> iterator = res.iterator();
//        while (iterator.hasNext()) {
//            if (iterator.next().size() > minLength) {
//                iterator.remove();
//            }
//        }
        return res;
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

    private void findLadders(int begin, int end, boolean[][] sTable, boolean[] chosed, List<String> wordList, LinkedList<List<String>> res, LinkedList<String> resTmp) {
        if (begin == end) {
            res.addLast(new LinkedList<>(resTmp));
        } else {
            for (int i = 1; i < wordList.size(); i++) {
                if (!chosed[i] && sTable[begin][i]) {
                    chosed[i] = true;
                    resTmp.addLast(wordList.get(i - 1));
                    findLadders(i, end, sTable, chosed, wordList, res, resTmp);
                    chosed[i] = false;
                    resTmp.removeLast();
                }
            }
        }
    }

    public static void main(String[] args) {
        String beginWord = "hit", endWord = "cog";
        String[] wordList = {"hot", "dot", "dog", "lot", "log", "cog"};
//        String[] wordList = {"hot","dot","dog","lot","log"};

//        String beginWord = "qa", endWord = "sq";
//        String[] wordList = {"si", "go", "se", "cm", "so", "ph", "mt", "db", "mb", "sb", "kr", "ln", "tm", "le", "av", "sm", "ar", "ci"
//                "ca", "br", "ti", "ba", "to", "ra", "fa", "yo", "ow", "sn", "ya", "cr", "po", "fe", "ho", "ma", "re", "or", "rn", "au", "ur"
//                , "rh", "sr", "tc", "lt", "lo", "as", "fr", "nb", "yb", "if", "pb", "ge", "th", "pm", "rb", "sh", "co", "ga", "li", "ha", "hz"
//                , "no", "bi", "di", "hi", "qa", "pi", "os", "uh", "wm", "an", "me", "mo", "na", "la", "st", "er", "sc", "ne", "mn", "mi", "am"
//                , "ex", "pt", "io", "be", "fm", "ta", "tb", "ni", "mr", "pa", "he", "lr", "sq", "ye"};

        L126 demo = new L126();
        System.out.println(demo.findLadders(beginWord, endWord, Arrays.asList(wordList)));
    }
}
