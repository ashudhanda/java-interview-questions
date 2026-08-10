// Create and start a thread using Runnable
public class ThreadExample {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            System.out.println("Running in: " + Thread.currentThread().getName());
        });
        t.start();
        System.out.println("Main thread: " + Thread.currentThread().getName());
    }
}
