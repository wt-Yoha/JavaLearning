package LeetCode;

public class L122 {
    public int maxProfitOnece(int[] prices, int s, int e) {
        if (s >= e) {
            return 0;
        }
        int minPrice = Integer.MAX_VALUE, profit = 0;
        for (int i = s; i <= e; i++) {
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
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            int tmpProfit = maxProfitOnece(prices, 0, i) + maxProfitOnece(prices, i + 1, prices.length - 1);
            if (tmpProfit > maxProfit) {
                maxProfit = tmpProfit;
            }
        }
        return maxProfit;
    }

    public static void main(String[] args) {
        int[] stock = {3, 3, 5, 0, 0, 3, 1, 4};
        L122 demo = new L122();
        System.out.println(demo.maxProfit(stock));
    }
}
