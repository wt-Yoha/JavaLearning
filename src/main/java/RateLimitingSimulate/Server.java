package RateLimitingSimulate;

import java.util.HashMap;

public class Server {
    HashMap<String, ServerApi> apiMap;
    HashMap<String, AccessLog> apiCountMap;
    public static final String SERVER_A_PATH = "/api/A";
    public static final String SERVER_B_PATH = "/api/B";

    private static class AccessLog{
        long count;
        long startTime;

        AccessLog() {
            reset();
        }

        void reset() {
            count = 1;
            startTime = System.currentTimeMillis();
        }
    }

    // config
    private boolean showRejectLogs = false;
    private long limitingPeriod = 1000;
    private long limitTimes = 200;

    private static class ServerA extends ServerApi{
        ServerA() {
            super(SERVER_A_PATH);
        }

        @Override
        public void invoke() {
            System.out.println("Server A " + count);
        }
    }

    private static class ServerB extends ServerApi{
        ServerB() {
            super(SERVER_B_PATH);
        }

        @Override
        public void invoke() {
            System.out.println("api B counts: " + this.count);
        }
    }

    Server(){
        apiMap = new HashMap<>();
        apiCountMap = new HashMap<>();
        setApiMap(new ServerA());
        setApiMap(new ServerB());
    }

    public void setApiMap(ServerApi serverApi) {
        apiMap.put(serverApi.path, serverApi);
    }

    public void doInvoke(String path) {
        if (!rateLimiting(path)) {
            return;
        }
        apiMap.get(path).api();
    }

    private void reject() {
        if (showRejectLogs) {
            System.out.println("api request reject");
        }
    }

    private boolean rateLimiting(String path) {
        AccessLog accessLog = apiCountMap.get(path);
        if (accessLog == null) {
            apiCountMap.put(path, new AccessLog());
        } else {
            long current = System.currentTimeMillis();
            if (current - accessLog.startTime <= limitingPeriod) {
                if (accessLog.count >= limitTimes) {
                    reject();
                    return false;
                }
                accessLog.count++;
            } else {
                accessLog.reset();
            }
        }
        return true;
    }

    public boolean isShowRejectLogs() {
        return showRejectLogs;
    }

    public void setShowRejectLogs(boolean showRejectLogs) {
        this.showRejectLogs = showRejectLogs;
    }

    public long getLimitingPeriod() {
        return limitingPeriod;
    }

    public void setLimitingPeriod(long limitingPeriod) {
        this.limitingPeriod = limitingPeriod;
    }

    public long getLimitTimes() {
        return limitTimes;
    }

    public void setLimitTimes(long limitTimes) {
        this.limitTimes = limitTimes;
    }
}
