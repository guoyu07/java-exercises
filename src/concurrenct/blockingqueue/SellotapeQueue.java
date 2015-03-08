package concurrenct.blockingqueue;

import concurrenct.model.Sellotape;

import java.util.concurrent.*;

import static util.OutHelper.*;

/**
 * Created by huhai on 2015/2/18.
 */
public class SellotapeQueue {

    private BlockingQueue<Sellotape> queue;

    public SellotapeQueue(BlockingQueue<Sellotape> queue) {
        if (null == queue) {
            throw new IllegalArgumentException("queue should not be null");
        }
        this.queue = queue;
    }

    /**
     * 撕胶带并放入队列
     */
    public void tearSellotape() {
        Sellotape sellotape = Sellotape.create();
        try {
            Thread.sleep(5);
            queue.put(sellotape);
            println("tear " + sellotape);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用胶带
     */
    public void useSellotape() {
        Sellotape sellotape;
        try {
            Thread.sleep(5000);
            sellotape = queue.take();
            println("use " + sellotape);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showSize() {
        println("size:" + queue.size());
    }

    public static void runArrayBlockingQueue() throws InterruptedException {
        int PRODUCER_LIMIT = 500;
        int CONSUMER_LIMIT = 300;
        int QUEUE_SIZE = 3;
        final SellotapeQueue sellotapeQueue = new SellotapeQueue(new ArrayBlockingQueue<>(QUEUE_SIZE));
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < PRODUCER_LIMIT; i++) {
            threadPool.submit(sellotapeQueue::tearSellotape);
        }
        for (int i = 0; i < CONSUMER_LIMIT; i++) {
            threadPool.execute(sellotapeQueue::useSellotape);
        }
        println("await");
//        long t1 = System.currentTimeMillis();
//        threadPool.awaitTermination(10, TimeUnit.SECONDS);
//        println(System.currentTimeMillis() - t1);
        threadPool.shutdown();
    }

    public static void runLinkedBlockingQueue() throws InterruptedException {
        final int PRODUCER_LIMIT = 10;
        final int CONSUMER_LIMIT = 2;
//        final int QUEUE_SIZE = 3;
        final SellotapeQueue sellotapeQueue = new SellotapeQueue(new LinkedBlockingQueue<>());
        final ExecutorService producerPool = Executors.newFixedThreadPool(PRODUCER_LIMIT);
        final ExecutorService consumerPool = Executors.newFixedThreadPool(CONSUMER_LIMIT);
        ExecutorService monitorPool = Executors.newCachedThreadPool();
        for (int i = 0; i < PRODUCER_LIMIT; i++) {
            producerPool.submit(() -> {
                while (true) sellotapeQueue.tearSellotape();
            });
        }

        println("-----------------------------------------------------------------------");

        for (int i = 0; i < CONSUMER_LIMIT; i++) {
            consumerPool.submit(() -> {
                while (true) {
                    sellotapeQueue.useSellotape();
                }
            });
        }

        monitorPool.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    sellotapeQueue.showSize();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        println("await");
//        long t1 = System.currentTimeMillis();
//        threadPool.awaitTermination(4, TimeUnit.SECONDS);
//        println(System.currentTimeMillis() - t1);
//        producerPool.awaitTermination(1, TimeUnit.DAYS);
        producerPool.shutdown();

//        consumerPool.awaitTermination(1, TimeUnit.DAYS);
        consumerPool.shutdown();

//        monitorPool.awaitTermination(1, TimeUnit.DAYS);
        monitorPool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
//        SellotapeQueue.runArrayBlockingQueue();
        SellotapeQueue.runLinkedBlockingQueue();
    }
}
