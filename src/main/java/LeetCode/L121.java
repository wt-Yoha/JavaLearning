package LeetCode;

public class L121 {

    public int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int minPrice = Integer.MAX_VALUE, profit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minPrice) {
                minPrice = prices[i];
            } else {
                if (prices[i] - minPrice > profit) {
                    profit = prices[i] - minPrice;
                }
            }
        }
        return profit;
    }
    public static void main(String[] args) {
        int[] stock = {7,1,5,3,6,4};
        L121 demo = new L121();
        System.out.println(demo.maxProfit(stock));
    }
}
