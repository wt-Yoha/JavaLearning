package LeetCode.DataStructrue;

import java.util.LinkedList;

public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    TreeNode(int x) {
        val = x;
    }

    public static TreeNode buildByArray(Integer[] nums){
        if (nums == null || nums.length == 0) {
            return null;
        }
        TreeNode[] nodes = new TreeNode[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != null) {
                nodes[i] = new TreeNode(nums[i]);
            }
        }
        int father = 0, son = 1;
        for (; son < nums.length; father++) {
            if (nodes[father] != null) {
                nodes[father].left = nodes[son++];
                if (son >= nums.length) {
                    break;
                }
                nodes[father].right = nodes[son++];
            }
        }
        return nodes[0];
    }


    public static void levelTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        LinkedList<TreeNode> current = new LinkedList<>(), nextLine = new LinkedList<>(), tmp;
        current.addLast(root);
        while (current.size() > 0) {
            TreeNode node = current.removeFirst();
            if (node.left != null) {
                nextLine.addLast(node.left);
            }
            if (node.right != null) {
                nextLine.addLast(node.right);
            }
            System.out.print(node.val+" ");
            if (current.size() == 0) {
                System.out.println();
                tmp = current;
                current = nextLine;
                nextLine = tmp;
            }
        }
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "val=" + val +
                '}';
    }
}
