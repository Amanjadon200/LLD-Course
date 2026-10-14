import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableMain {
    public static void main (String args[]){
        Task1 task=new Task1();
        ExecutorService es=Executors.newFixedThreadPool(4);
        try{
                Future<String> f= es.submit(task);
                // System.out.println(f.get()); // it blocks the main thread until the task is completed and then it will print the statement.
                System.out.println("Main thread is running"+f);// Main thread is runningjava.util.concurrent.FutureTask@23fc625e[Not completed, task = Task1@f6f4d33]
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
class Task1 implements Callable<String> {
    @Override 
    public String call() throws Exception{
        Thread.sleep(2000);
        System.out.println("Task is running");
        return "Task Completed";
    }
}