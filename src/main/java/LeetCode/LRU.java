package LeetCode;

import java.util.*;

public class LRU extends LinkedHashMap<Integer, Integer>{
    // 存储对应页面的下标
    int maxCapacity = 3;

    public LRU(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > maxCapacity;
    }

    public void getPage(Integer integer) {
        if (containsKey(integer)) {
            remove(integer);
        }
        put(integer, integer);
        System.out.println(this);
    }

    public String toString(){
        StringBuilder res = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : entrySet()) {
            res.append(entry.getKey());
            res.append(" ");
        }
        res.reverse();
        return res.toString();
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4));
        int capacity = 3;
        LRU lru = new LRU(capacity);
        for (Integer integer : list) {
            lru.getPage(integer);
        }
    }
}
