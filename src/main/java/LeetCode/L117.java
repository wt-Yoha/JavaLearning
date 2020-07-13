package LeetCode;

import LeetCode.DataStructrue.Node;

public class L117 {
    public Node connect(Node root) {
        if (root == null) {
            return null;
        }

        root.next = null;
        Node tmp = root, lineFirst = getFirstChild(tmp);
        while (lineFirst != null) {
            if (tmp.left != null) {
                if (tmp.right == null) {
                    tmp.left.next = getUncleFirstSon(tmp);
                } else {
                    tmp.left.next = tmp.right;
                }
            }
            if (tmp.right != null) {
                tmp.right.next = getUncleFirstSon(tmp);
            }
            tmp = tmp.next;
            if (tmp == null) {
                tmp = lineFirst;
                lineFirst = getNextLineFirst(lineFirst);
            }
        }
        return root;
    }

    private Node getFirstChild(Node t) {
        if (t == null) {
            return null;
        }
        return t.left != null ? t.left : t.right;
    }

    private Node getUncleFirstSon(Node father) {
        Node uncle = father.next, uncleFirstSon = getFirstChild(uncle);
        while (uncleFirstSon == null && uncle != null) {
            uncle = uncle.next;
            uncleFirstSon = getFirstChild(uncle);
        }
        return uncleFirstSon;
    }

    private Node getNextLineFirst(Node currentFirst) {
        Node currentTmp = currentFirst, nextLineFirst = getFirstChild(currentTmp);
        while (nextLineFirst == null && currentTmp != null) {
            currentTmp = currentTmp.next;
            nextLineFirst = getFirstChild(currentTmp);
        }
        return nextLineFirst;
    }

    public static void main(String[] args) {
//        Node root = Node.buildByArray(new Integer[]{2, 1, 3, 0, 7, 9, 1, 2, null, 1, 0, null, null, 8, 8, null, null, null, null, 7});
//        Node root = Node.buildByArray(new Integer[]{-2,-9,0,3,5,-1,9,5,2,null,null,-3,null,-7,6,-6,null,null,null,-1,null,null,null,-9,9,null,null,null,null,8,null,-2,5});
        Node root = Node.buildByArray(new Integer[]{5,2,-2,4,-4,-9,2,7,2,null,-9,-9,null,null,3,null,7,null,null,null,null,null,null,null,3});
        L117 demo = new L117();
        demo.connect(root);

        Node.levelTraversal(root);
        System.out.println("------------");

        Node t = root, nextLineFirst = demo.getFirstChild(t);
        while (t != null) {
            while (t != null) {
                System.out.print(t.val+" ");
                t = t.next;
            }
            System.out.println("#");
            t = nextLineFirst;
            nextLineFirst = demo.getNextLineFirst(nextLineFirst);
        }

    }
}
