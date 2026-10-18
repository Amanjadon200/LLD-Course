import java.util.concurrent.*;

public class ThreadStarvationDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<?> parent = executor.submit(new ParentTask(executor));

        try {
            parent.get(); // wait for the parent task to finish
        } catch (ExecutionException e) {
            e.getCause().printStackTrace();
        }
        
        executor.shutdownNow();

        // executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}

class ParentTask implements Runnable {
    private final ExecutorService executor;

    ParentTask(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void run() {
        System.out.println("Parent started on " + Thread.currentThread().getName());

        Future<?> child = executor.submit(new ChildTask());

        try {
            System.out.println("Parent is waiting for child");
            child.get(2, TimeUnit.SECONDS);
            System.out.println("Child finished");
        } catch (TimeoutException e) {
            System.out.println("Child did not get a worker in time: starvation");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("Child failed: " + e.getCause());
        }

        System.out.println("Parent finished");
    }
}

class ChildTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Child started on " + Thread.currentThread().getName());
    }
}
