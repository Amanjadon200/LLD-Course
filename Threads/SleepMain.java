public class SleepMain {
    public static void main(String[] args) {

        Car car = new Car();

        Thread second = new Thread(() -> {
            try {
                car.nonSynchronizedWork();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        second.start();
        Thread first = new Thread(() -> {
                car.synchronizedWork();
        });
        first.start();
         Thread third = new Thread(() -> {
            try {
                car.synchronizedWork1();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });
        third.start();
    }
}
class Car {
    void nonSynchronizedWork() throws InterruptedException {
        System.out.println("Non-synchronized work started");
        Thread.sleep(2000);
        System.out.println("Non-synchronized work finished");
    }

    synchronized void synchronizedWork() {
        System.out.println("Synchronized work running");
    }
    
    synchronized void synchronizedWork1() {
        System.out.println("Synchronized 1 work running");
    }
}
// A non-synchronized method does not acquire the object’s intrinsic lock—even if it calls Thread.sleep().
// So while that thread sleeps, another thread can enter a synchronized method on the same object and acquire its lock, as long as no other thread currently holds that lock. The sleeping thread holds the lock only if it entered a synchronized method or block and hasn’t exited it yet.


// so when you enter in syn block only then thread will hold the lock and if it sleeps then also it will hold the lock and no other thread can enter in that block until the thread exits from that block.