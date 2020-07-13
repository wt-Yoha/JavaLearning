package LeetCode;

import redis.clients.jedis.*;

import java.util.List;

public class RedisPipeLine implements Runnable {
    JedisPool jedisPool;

    RedisPipeLine(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
//        Jedis jedis = jedisPool.getResource();
        Jedis jedis = new Jedis("www.wtyoha.cn", 6379);
        jedis.auth("996895");
        Pipeline muiltPipeline = jedis.pipelined();
        Pipeline watchAndGet = jedis.pipelined();
        int a = 0;

        Object o = null;
        List<Object> objects = null, watchAndGetRes = null;
        int retryTimes = 0, casTimes = 0;
        while (o == null) {
            watchAndGet.watch("a");
            watchAndGet.get("a");
            watchAndGetRes = watchAndGet.syncAndReturnAll();
            watchAndGet.clear();

            if (watchAndGetRes.size() == 2) {
                a = Integer.parseInt((String) watchAndGetRes.get(1));
            } else {
                System.out.println(Thread.currentThread().getName() + " retry to get and watch " + retryTimes);
                retryTimes++;
                if (retryTimes == 20) {
                    break;
                }
                continue;
            }
            a = a + 2;

            muiltPipeline.multi();
            muiltPipeline.set("a", String.valueOf(a));
            muiltPipeline.get("a");
            muiltPipeline.exec();

            objects = muiltPipeline.syncAndReturnAll();
            if (objects.size() > 0 && (o = objects.get(objects.size() - 1)) != null) {
                System.out.println(Thread.currentThread().getName() + " a: " + (a - 2) + " " + objects);
                break;
            } else {
                casTimes++;
//                System.out.println(Thread.currentThread().getName() +" try cas "+ casTimes);
            }

            muiltPipeline.clear();
        }
    }

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(300);
        jedisPoolConfig.setMinIdle(100);
        jedisPoolConfig.setMaxWaitMillis(1000 * 20);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "www.wtyoha.cn", 6379, 60, "996895");

        int num = 100;
        for (int i = 0; i < num; i++) {
            new Thread(new RedisPipeLine(jedisPool)).start();
        }
    }
}
