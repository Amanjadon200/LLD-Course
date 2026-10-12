import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadBasic {
    public static void main(String[] args) {
        // MyThread thread1 = new MyThread("Thread-1");
        // MyThread thread2 = new MyThread("Thread-2");

        // thread1.start();
        // thread1.start();
        // thread2.start();



        // MyCallableTask task = new MyCallableTask();
        // ExecutorService executor = Executors.newFixedThreadPool(2);
        // Future<String> future = executor.submit(task);
        // System.out.println("Task submitted, waiting for result..."+ future);
        // try {
        //     String result = future.get(); // This will block until the task is complete
        //     System.out.println("Result from callable: " + result);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // } finally {
        //     executor.shutdown();
        // }
        // System.out.println("Main thread finished execution.");
        MyThread1 t = new MyThread1(); 
        t.start(); // Starts a separate thread 
        System.out.println("Main thread is running"); 
    }
}
class MyThread extends Thread {
    public MyThread(String name) {
        super(name);
    }
    void doWork() throws InterruptedException {
    // A method that may be interrupted
}

    @Override
    public void run() {
        System.out.println(getName() + ")))) has started execution.");
        for (int i = 0; i < 5; i++) {
            // try {
                // System.out.println(getName() + " is running, iteration: " + i);
                // doWork(); // Call the method that may be interrupted
                // MyThread.sleep(1000); // Sleep for 1 second
            // } 
        }
        // throw new CompileTimeException("Simulated exception in " + getName());
        // System.out.println(getName() + " has finished execution.");
    }
}
class MyCallableTask implements Callable<String>{
    public String call() throws Exception {
        // Simulate some work
        Thread.sleep(1000);
        return "Task completed";
    }
}

class MyThread1 extends Thread { 
    public void run() { 
        try { 
            throw new RuntimeException("Exception in thread"); 
        } catch (Exception e) { 
            System.out.println("Caught exception in thread: " + e.getMessage()); 
        } 
    } 
} 
