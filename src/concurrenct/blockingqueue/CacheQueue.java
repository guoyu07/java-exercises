package concurrenct.blockingqueue;

import java.util.Map;
import java.util.concurrent.*;
import static util.OutHelper.*;

/**
 * Created by huhai on 2015/2/20.
 */
public class CacheQueue<K, V> {
    private DelayQueue<DelayedItem> q;
    private Map<K, CacheValue<V>> cache;

    private ExecutorService threadPool;

    public CacheQueue(ExecutorService threadPool) {
        this.threadPool = threadPool;
        q = new DelayQueue();
        cache = new ConcurrentHashMap<>();
        Thread cleaner = new Thread(){
            @Override
            public void run() {
                clearTimeoutElement();
            }
        };
//        cleaner.setDaemon(true);
        threadPool.submit(cleaner);
//        cleaner.start();
    }

    private void clearTimeoutElement(){
        while (true){
            try {
                DelayedItem item = q.take();
                if(null != item){
                    println("remove " + item.key + " after " + (System.currentTimeMillis() - item.time) + "ms");
                    cache.remove(item.key);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public boolean add(K key , V value, long expires){
        CacheValue<V> oldValue = cache.get(key);
        if(null == oldValue){
            cache.put(key, new CacheValue<>(value));
            q.put(new DelayedItem(key, expires));
            return true;
        }
        return false;
    }

    public V set(K key, V value){
        CacheValue<V> oldValue = cache.get(key);
        if(oldValue != null){
            cache.put(key, new CacheValue<>(value));
            return oldValue.getValue();
        }
        return  null;
    }

    public V get(K key){
        CacheValue<V> value = cache.get(key);
        if(value != null){
            return value.getValue();
        }
        return  null;
    }

    private class CacheValue<V>{
        private V value;

        public CacheValue(V v) {
            this.value = v;
        }

        public V getValue() {
            return value;
        }
    }

    private class DelayedItem implements Delayed{
        private K key;

        /**
         * 缓存创建时间（毫秒）
         */
        private long time;

        /**
         * 缓存过期时间（毫秒）
         */
        private long timeout;

        /**
         *
         * @param key 键名
         * @param timeout 缓存时间（毫秒）
         */
        public DelayedItem(K key, long timeout) {
            time = System.currentTimeMillis();
            this.key = key;
            this.timeout = timeout;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time + timeout - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            DelayedItem other = (DelayedItem)o;
            long diff = time + timeout - other.time - other.timeout;
            if(diff > 0){
                return 1;
            }else if(diff < 0){
                return  -1;
            }
            return  0;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService cleanerPool = Executors.newFixedThreadPool(1);
        final CacheQueue<String, Integer> cache = new CacheQueue<>(cleanerPool);
        cache.add("a", 123, 2000);
        cache.add("b", 456, 3000);
        cache.add("c", 789, 6000);
        ExecutorService workPool = Executors.newFixedThreadPool(5);
        for (int j = 0; j < 5; j++){
            workPool.submit(()->{
                long threadId = Thread.currentThread().getId();
                try {
                    for(int i = 1; i <= 10; i++){
                        Thread.sleep(1000);
                        println(threadId + " after " + i +"s a=" + cache.get("a") + " b=" + cache.get("b") + " c=" +
                                cache.get("c"));
                        if(i == 1 && threadId % 10 == 2){
                            println(threadId + " set a=1");
                            cache.set("a", 1);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        workPool.awaitTermination(15, TimeUnit.SECONDS);
        workPool.shutdown();
        cleanerPool.shutdownNow();
    }
}
