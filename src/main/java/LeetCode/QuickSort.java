package LeetCode;


import java.util.Arrays;

public class QuickSort {

    private static void swap(int[] nums, int a, int b) {
        if (a == b) {
            return;
        }
        nums[a] = nums[a] ^ nums[b];
        nums[b] = nums[a] ^ nums[b];
        nums[a] = nums[a] ^ nums[b];
    }

    private static void sortCore(int[] nums, int s, int e) {
        if (s >= e) {
            return;
        }
        int pivot = ((int) (Math.random() * (e - s + 1))) + s;
        swap(nums, pivot, e);
        int p = s - 1;
        for (int i = s; i < e; i++) {
            if (nums[i] < nums[e]) {
                swap(nums, i, ++p);
            }
        }
        swap(nums, ++p, e);
        sortCore(nums, s, p - 1);
        sortCore(nums, p + 1, e);
    }

    public static void sort(int[] nums) {
        if (nums == null || nums.length == 0) {
            return;
        }
        sortCore(nums, 0, nums.length - 1);
    }

    public static void main(String[] args) {
//        int[] l = {5, 3, 2, -1, 0, 3, 7, 9, 1, 4};
        int[] l = {0};
        sort(l);
        System.out.println(Arrays.toString(l));
    }
}
