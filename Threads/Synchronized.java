public class Synchronized { 
    private int count = 0; 
    // The entire method is synchronized. 
    public synchronized void increment() { 
        System.out.println("Synchronized Method - Start increment: " + Thread.currentThread().getName()); 
        // Critical section: updating the shared counter 
        count++; 
        System.out.println("Synchronized Method - Counter value after increment: " + count); 
        System.out.println("Synchronized Method - End increment: " + Thread.currentThread().getName()); 
    } 

    public int getCount() { 
        return count; 
    } 

    // Main method to test the synchronized method 
    public static void main(String[] args) { 
        Synchronized counter = new Synchronized(); 
        int numberOfThreads = 5; 
        Thread[] threads = new Thread[numberOfThreads]; 
        // Create and start threads that call the synchronized increment method. 
        for (int i = 0; i < numberOfThreads; i++) { 
            threads[i] = new Thread(new Runnable() { 
                public void run() { 
                    counter.increment(); 
                } 
            }, "Thread-" + (i + 1)); 


            threads[i].start(); 
        } 
        // Wait for all threads to complete. 
        for (int i = 0; i < numberOfThreads; i++) { 
            try { 
                System.out.println("Waiting for " + threads[i].getName() + " to finish.");
                threads[i].join(); 
                System.out.println(threads[i].getName() + " has finished execution.");
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            } 
        } 
        // Display the final counter value. 
        System.out.println("Final counter value: " + counter.getCount()); 
    } 
}