package LeetCode;

import java.util.Arrays;

public class MergeSort {

    private static void merge(int[] num, int s, int e) {
        if (s == e) {
            return;
        } else {
            int mid = (s + e) / 2;
            merge(num, s, mid);
            merge(num, mid + 1, e);
            int[] tmp = new int[e - s + 1];
            int i = 0, p = s, q = mid + 1;
            while (p <= mid && q <= e) {
                if (num[p] < num[q]) {
                    tmp[i++] = num[p++];
                } else {
                    tmp[i++] = num[q++];
                }
            }
            while (p <= mid) {
                tmp[i++] = num[p++];
            }
            while (q <= e) {
                tmp[i++] = num[q++];
            }
            if (tmp.length >= 0) System.arraycopy(tmp, 0, num, s, tmp.length);
        }
    }

    public static void sort(int[] num) {
        if (num == null || num.length == 0) {
            return;
        }
        merge(num, 0, num.length - 1);
    }

    public static void main(String[] args) {
        int[] l = {5, 3, 2, -1, 0, 3, 7, 9, 1, 4};
        sort(l);
        System.out.println(Arrays.toString(l));
    }
}
