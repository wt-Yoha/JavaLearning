package RateLimitingSimulate;

public class Client {
    Server server = new Server();

    public void connectApi(long period, long times, boolean balance){
        long s = System.currentTimeMillis(), e;
        int count = 0;
        while (true) {
            ++count;
            System.out.println("client " + count);
            server.doInvoke(Server.SERVER_A_PATH);
            e = System.currentTimeMillis();
            if (count >= times || period <= (e - s)) {
                break;
            }
            if (balance) {
                try {
                    Thread.sleep(period/times);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        System.out.println("Close connect " + (e - s) +" ms");
    }

    public static void main(String[] args) {
        new Client().connectApi(2000, 1000, true);
    }
}
