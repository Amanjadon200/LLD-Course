public class WaitAndNotifyRealLife {
    public static void main(String[] args) {
        Object lock=new Object();
        Thread waiter=new Thread(new Waiter(lock));
        waiter.start();
        Thread chef=new Thread(new Chef(lock));
        chef.start();
    }
}
class Waiter implements  Runnable{
    private Object lock;
    public Waiter(Object lock){
        this.lock=lock;
    }
    public void run(){
        synchronized(lock){
            try {
                System.out.println("Waiter has accepted an order");
                lock.wait();
                System.out.println("Waiter served");
            } catch (InterruptedException e) {
                System.out.println(e + " Exception in run method of Waiter ");
            }
        }
    }
}
class Chef implements  Runnable{
    Object lock;
    public Chef(Object lock){
        this.lock=lock;
    }
    public void run(){
        synchronized(lock){
              try {
                System.out.println("chef has started an order prep");
                Thread.sleep(2000);
                System.out.println("chef has prepared an order , inform waiter");
                lock.notify();
            } catch (InterruptedException e) {
                System.out.println(e + " Exception in run method of Chef ");
            }

            
        }
    }
}