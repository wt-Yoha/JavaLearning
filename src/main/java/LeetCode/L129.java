package LeetCode;

import LeetCode.DataStructrue.TreeNode;

import java.util.LinkedList;

public class L129 {
    public int sumNumbers(TreeNode root) {
        int[] sum = new int[1];
        sumCore(root, sum, new LinkedList<>());
        return sum[0];
    }

    private void sumCore(TreeNode root, int[] sum, LinkedList<Integer> num) {
        if (root != null) {
            num.addLast(root.val);
            if (root.left == null && root.right == null) {
                sum[0] += Integer.parseInt(num.toString().replace(",","").replace(" ","").substring(1, num.size() + 1));
            } else {
                sumCore(root.left, sum, num);
                sumCore(root.right, sum, num);
            }
            num.removeLast();
        }
    }
    public static void main(String[] args) {
        TreeNode root = TreeNode.buildByArray(new Integer[]{4, 9, 0, 5, 1});
        L129 runner = new L129();
        System.out.println(runner.sumNumbers(root));
    }
}
