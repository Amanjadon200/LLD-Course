import java.util.concurrent.ExecutorService; 
import java.util.concurrent.Executors; 
 
public class ThreadMain{ 
    private static final int NUM_CORES = Runtime.getRuntime().availableProcessors(); // Get CPU core count  
    public static void main(String[] args) { 
        ExecutorService fixedPool = Executors.newFixedThreadPool(NUM_CORES); // Optimize for CPU-intensive tasks 
        System.out.println("Number of CPU cores: " + NUM_CORES);
        for (int i = 0; i < 20; i++) { 
            fixedPool.execute(() -> { 
                int result = performComputation(); 
                System.out.println(Thread.currentThread().getName() + " computed result: " + result); 
            }); 
        } 
        for (int i = 0; i < 100000; i++) { 
            System.out.print(""); // Simulating main thread work
        }
        fixedPool.shutdown(); 
    } 

    private static int performComputation() { 
        int sum = 0; 
        for (int i = 0; i < 1_000_000; i++) { 
            sum += Math.sqrt(i); // Simulating heavy computation 
        } 
        return sum; 
    } 
}