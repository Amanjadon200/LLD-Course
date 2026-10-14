public class WaitAndNotifyMain {
    public static void main(String[] args) {

        Car1 Car1 = new Car1();

        Thread first = new Thread(() -> {
                Car1.synchronizedWork();
        });
        first.start();
        Thread third = new Thread(() -> {
        try {
            Car1.synchronizedWork1();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        });
        third.start();
    }
}
class Car1 {
    void nonSynchronizedWork() throws InterruptedException {
        System.out.println("Non-synchronized work started");
        Thread.sleep(2000);
        System.out.println("Non-synchronized work finished");
    }

    synchronized void synchronizedWork() {
        try {
            wait(10000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted while waiting");
            Thread.currentThread().interrupt();
            return;
        }
        System.out.println("Synchronized work finished");
    }

    synchronized void synchronizedWork1() {

        System.out.println("Synchronized 1 work running");
        notify();
    }
}
// so in syn function thread acquires the lock and it releases lock as it reaches at wait() method and then after 10 sec it will again acquire the lock and then it will print the statement.