package LeetCode.DataStructrue;


import java.util.LinkedList;

public class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {
    }

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }

    public static void firstOrder(Node t) {
        if (t != null) {
            firstOrder(t.left);
            System.out.println(t.val);
            firstOrder(t.right);
        }
    }

    public static Node buildByArray(Integer[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        Node[] nodes = new Node[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != null) {
                nodes[i] = new Node(nums[i]);
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

    public static void levelTraversal(Node root) {
        if (root == null) {
            return;
        }
        LinkedList<Node> current = new LinkedList<>(), nextLine = new LinkedList<>(), tmp;
        current.addLast(root);
        while (current.size() > 0) {
            Node node = current.removeFirst();
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

    public static void main(String[] args) {
        Node root = buildByArray(new Integer[]{0, 1, 2, 3, 4, 5, 6});
        firstOrder(root);
    }
}
