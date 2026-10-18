public class WaitAndNotifyRealLife {
    private static final long START = System.nanoTime();

    static void log(String message) {
        double seconds = (System.nanoTime() - START) / 1_000_000_000.0;
        System.out.printf("[+%.3f s] %s%n", seconds, message);
    }

    static int mealsReady = 0;

    public static void main(String[] args) {

        Object lock = new Object();
        Thread waiter = new Thread(new Waiter(lock), "Waiter1");
        waiter.start();
        Thread waiter2 = new Thread(new Waiter(lock), "Waiter2");
        waiter2.start();
        Thread chef = new Thread(new Chef(lock), "Chef");
        chef.start();
    }
}

class Waiter implements Runnable {
    private Object lock;

    public Waiter(Object lock) {
        this.lock = lock;
    }

    public void run() {
        synchronized (lock) {
            try {
                while (WaitAndNotifyRealLife.mealsReady == 0) {
                    WaitAndNotifyRealLife.log(Thread.currentThread().getName() + " waiting for an order");
                    lock.wait();
                }
                WaitAndNotifyRealLife.mealsReady--;
                WaitAndNotifyRealLife.log(Thread.currentThread().getName() + " has served an order");
            } catch (InterruptedException e) {
                WaitAndNotifyRealLife.log(Thread.currentThread().getName() + " Exception in run method of Waiter ");
            }
        }
    }
}

class Chef implements Runnable {
    Object lock;

    public Chef(Object lock) {
        this.lock = lock;
    }

    public void run() {
        try {
            for (int i = 0; i < 2; i++) {
                Thread.sleep(2000);
                synchronized (lock) {
                    WaitAndNotifyRealLife.log(Thread.currentThread().getName() + " has started an order prep" + i);
                    WaitAndNotifyRealLife.mealsReady++;
                    WaitAndNotifyRealLife
                            .log(Thread.currentThread().getName() + " has prepared an order , inform waiter");
                    lock.notifyAll();
                }
                // Thread.sleep(100);
            }
        } catch (Exception e) {
            WaitAndNotifyRealLife.log(Thread.currentThread().getName() + " Exception in run method of Chef ");

        }

    }
}
