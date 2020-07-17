package LeetCode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class L128 {
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], -1);
        }
        int maxContinueSequence = 0, next, tmpLen;
        LinkedList<Integer> stack = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            // 扫描所有未处理的数字
            if (map.get(nums[i]) != -1) {
                continue;
            }
            next = nums[i] + 1;
            if (map.containsKey(next)) {
                // 存在下一个数字
                tmpLen = map.get(next);
                if (tmpLen != -1) {
                    // 下一个数字已经获取了最长连续串
                    map.put(nums[i], ++tmpLen);
                    maxContinueSequence = Math.max(maxContinueSequence, tmpLen);
                } else {
                    // 下一个数字未获取最长串，开始迭代
                    int deep = -1;
                    stack.addFirst(nums[i]);
                    stack.addFirst(next);
                    next++;
                    while (map.containsKey(next) && (deep = map.get(next)) == -1) {
                        stack.addFirst(next++);
                    }
                    while (stack.size() > 0) {
                        map.put(stack.removeFirst(), ++deep);
                        maxContinueSequence = Math.max(maxContinueSequence, deep);
                    }
                }
            } else {
                // 不存在下一个数字
                map.put(nums[i], 0);
            }
        }


        return maxContinueSequence + 1;
    }

    public static void main(String[] args) {
//        int[] nums = new int[]{100, 4, 200, 1, 3, 2};
        int[] nums = new int[]{-1, 1, 0};
        L128 runner = new L128();
        System.out.println(runner.longestConsecutive(nums));
    }
}
