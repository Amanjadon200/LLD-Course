import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceMain {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new Task2("Task " + i));
        }
        executorService.shutdown();
    }
}
class Task2 implements Runnable {
    String id;
    public Task2(String id) {
        this.id = id;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " completed " + id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " was interrupted.");
        }
    }
}
