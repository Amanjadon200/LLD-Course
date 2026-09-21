import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
public class CustomThreadPoolExecutor {
    // psvm
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, // core pool size
                4, // maximum pool size
                60, // keep-alive time
                java.util.concurrent.TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(4)
        );

        // Submit tasks to the executor
        for (int i = 1; i <= 10; i++) {
            executor.submit(new Task(i));
        }

        // Shutdown the executor
        executor.shutdown();
    }
}
class Task implements Runnable {
    private final int taskId;

    public Task(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println("Task " + taskId + " is being executed by " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000); // Simulate some work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task " + taskId + " completed by " + Thread.currentThread().getName());
    }
}
