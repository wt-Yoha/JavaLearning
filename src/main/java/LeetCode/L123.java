package LeetCode;

import LeetCode.DataStructrue.TreeNode;

import java.util.HashMap;
import java.util.Map;

public class L123 {

    private void setNodePath(TreeNode root, Map<TreeNode, Integer> nodeMaxPath) {
        if (root != null) {
            setNodePath(root.left, nodeMaxPath);
            setNodePath(root.right, nodeMaxPath);
            int leftSubMaxPath = 0, rightSubMaxPath = 0;
            if (root.left != null) {
                leftSubMaxPath = Math.max(nodeMaxPath.get(root.left), 0);
            }
            if (root.right != null) {
                rightSubMaxPath = Math.max(nodeMaxPath.get(root.right), 0);
            }
            nodeMaxPath.put(root, Math.max(leftSubMaxPath, rightSubMaxPath) + root.val);
        }
    }

    public int maxPathSum(TreeNode root) {
        Map<TreeNode, Integer> nodeMaxPath = new HashMap<>();
        nodeMaxPath.put(null, 0);
        setNodePath(root, nodeMaxPath);
        int maxPath = Integer.MIN_VALUE, tmpPath, tmpPathWithSub, leftMaxPath, rightMaxPath;

        for (TreeNode node : nodeMaxPath.keySet()) {
            if (node != null) {
                leftMaxPath = Math.max(0, nodeMaxPath.get(node.left));
                rightMaxPath = Math.max(0, nodeMaxPath.get(node.right));
                tmpPath = node.val + leftMaxPath + rightMaxPath;
                if (tmpPath > maxPath) {
                    maxPath = tmpPath;
                }
            }
        }
        return maxPath;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.buildByArray(new Integer[]{-10, 9, 20, null, null, 15, 7});
//        TreeNode root = TreeNode.buildByArray(new Integer[]{1, 2, 3});
//        TreeNode root = TreeNode.buildByArray(new Integer[]{-3});
//        TreeNode root = TreeNode.buildByArray(new Integer[]{2, -1});
//        TreeNode root = TreeNode.buildByArray(new Integer[]{1, -2, 3});
//        TreeNode root = TreeNode.buildByArray(new Integer[]{9, 6, -3, null, null, -6, 2, null, null, 2, null, -6, -6, -6});
        TreeNode.levelTraversal(root);
        System.out.println("=====================");
        L123 demo = new L123();
        System.out.println(demo.maxPathSum(root));
    }
}
