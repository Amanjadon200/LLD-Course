import java.util.concurrent.Semaphore;

class ReadWriteUsingSemaphore {
    public static void main(String[] args) {
        ReadWrite rw = new ReadWrite();
        Thread t1 = new Thread(()->{
            try{
                rw.readLock();
                System.out.println("Thread 1 is reading");
                Thread.sleep(1000);
                rw.readUnlock();
                System.out.println("Thread 1 finished reading");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(()->{
            try{
                rw.readLock();
                System.out.println("Thread 2 is reading");
                Thread.sleep(1000);
                rw.readUnlock();
                System.out.println("Thread 2 finished reading");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t3 = new Thread(()->{
            try{
                rw.writeLock();
                System.out.println("Thread 3 is writing");
                Thread.sleep(1000);
                rw.writeUnlock();
                System.out.println("Thread 3 finished writing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t4 = new Thread(()->{
            try{
                rw.writeLock();
                System.out.println("Thread 4 is writing");
                Thread.sleep(1000);
                rw.writeUnlock();
                System.out.println("Thread 4 finished writing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
class ReadWrite{
    int readCount =0;
    Semaphore wrt = new Semaphore(1);
    Semaphore mutex = new Semaphore(1);
    public void readLock() throws InterruptedException{
        mutex.acquire();
        readCount++;
        if(readCount == 1){
            wrt.acquire();
        }
        mutex.release();
    }
    public void readUnlock() throws InterruptedException {
        mutex.acquire();
        readCount--;
        if(readCount == 0) {
            wrt.release();
        }
        mutex.release();
    }
    public void writeLock() throws InterruptedException{
        wrt.acquire();
    }
    public void writeUnlock() throws InterruptedException {
        wrt.release();
    }
}