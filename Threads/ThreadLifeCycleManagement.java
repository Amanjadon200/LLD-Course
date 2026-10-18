public class ThreadLifeCycleManagement {
    public static void main(String[] args) {
        Thread thread = new Thread(new Task(1), "MyThread");
        System.out.println("Thread state after creation: " + thread.getState());
        thread.start();
        System.out.println("Thread state after start: " + thread.getState());
        try {
            Thread.sleep(3000);
            thread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting for MyThread to finish.");
            e.printStackTrace();
        }
        System.out.println("Thread state after completion: " + thread.getState()); // this is thread state after completion, it should be TERMINATED
    }
}