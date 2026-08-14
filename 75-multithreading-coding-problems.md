# 75 — Multithreading Coding Problems ⚙️

Theory ke baad ab **code** — "do threads se even-odd print karo", "FizzBuzz multithreaded likhna hai". Ye live coding me aate hain.

## Q1. Do threads se Even-Odd print karo
**Answer:** Sabse classic. Ek thread even, ek odd — **alternate** me print ho.

```java
class EvenOddPrinter {
    private int num = 1;
    private final int max = 10;
    private final Object lock = new Object();

    void printOdd() {
        while (num <= max) {
            synchronized (lock) {
                while (num % 2 == 0) {           // mera turn nahi
                    try { lock.wait(); } catch (InterruptedException e) { }
                }
                if (num <= max) System.out.println("Odd:  " + num++);
                lock.notifyAll();                // doosre ko jagaao
            }
        }
    }

    void printEven() {
        while (num <= max) {
            synchronized (lock) {
                while (num % 2 != 0) {           // mera turn nahi
                    try { lock.wait(); } catch (InterruptedException e) { }
                }
                if (num <= max) System.out.println("Even: " + num++);
                lock.notifyAll();
            }
        }
    }
}

// Main
EvenOddPrinter printer = new EvenOddPrinter();
new Thread(printer::printOdd).start();
new Thread(printer::printEven).start();
```

👉 **Pattern:** `while (not my turn) wait()` → kaam karo → `notifyAll()`. Ye **turn-based coordination** ka template hai.

⚠️ `if` nahi, **`while`** use karo — spurious wakeup ke liye (topic 65).

## Q2. Teen threads se "ABC ABC ABC" print karo
**Answer:** Turn variable se control:

```java
class ABCPrinter {
    private int turn = 0;              // 0=A, 1=B, 2=C
    private final Object lock = new Object();
    private final int times = 3;

    void printLetter(int myTurn, String letter) {
        for (int i = 0; i < times; i++) {
            synchronized (lock) {
                while (turn != myTurn) {
                    try { lock.wait(); } catch (InterruptedException e) { }
                }
                System.out.print(letter);
                turn = (turn + 1) % 3;       // agle ka turn
                lock.notifyAll();
            }
        }
    }
}

new Thread(() -> p.printLetter(0, "A")).start();
new Thread(() -> p.printLetter(1, "B")).start();
new Thread(() -> p.printLetter(2, "C")).start();
```

👉 `turn` variable hi conductor hai — kiska turn hai wahi chalega, baaki wait.

## Q3. FizzBuzz Multithreaded
**Answer:** 4 threads — fizz, buzz, fizzbuzz, number.

```java
class FizzBuzz {
    private int n = 15;
    private int num = 1;
    private final Object lock = new Object();

    void print(Runnable task, int checkType) {
        while (true) {
            synchronized (lock) {
                while (num <= n && !isMyTurn(checkType)) {
                    try { lock.wait(); } catch (InterruptedException e) { }
                }
                if (num > n) { lock.notifyAll(); return; }
                task.run();
                num++;
                lock.notifyAll();
            }
        }
    }

    boolean isMyTurn(int type) {
        return switch (type) {
            case 1 -> num % 3 == 0 && num % 5 == 0;   // fizzbuzz
            case 2 -> num % 3 == 0;                    // fizz
            case 3 -> num % 5 == 0;                    // buzz
            default -> num % 3 != 0 && num % 5 != 0;   // number
        };
    }
}
```

👉 Same turn-based pattern — bas condition complex hai.

## Q4. Semaphore se Even-Odd (modern tareeka)
**Answer:** `wait/notify` ki jagah `Semaphore` — zyada clean:

```java
class EvenOddSemaphore {
    private final Semaphore oddSem = new Semaphore(1);    // odd pehle jaayega
    private final Semaphore evenSem = new Semaphore(0);   // even locked
    private final int max = 10;

    void printOdd() {
        for (int i = 1; i <= max; i += 2) {
            try {
                oddSem.acquire();
                System.out.println("Odd:  " + i);
                evenSem.release();
            } catch (InterruptedException e) { }
        }
    }

    void printEven() {
        for (int i = 2; i <= max; i += 2) {
            try {
                evenSem.acquire();
                System.out.println("Even: " + i);
                oddSem.release();
            } catch (InterruptedException e) { }
        }
    }
}
```

👉 **Semaphore = permits ka counter.** `acquire()` = permit lo (0 ho to wait), `release()` = permit wapas do. `evenSem` ke 0 permits se shuru — even thread pehle nahi chal sakta.

## Q5. CountDownLatch — sab ka wait karo
**Answer:** "Jab tak saare threads kaam khatam na karein, main thread ruko."

```java
CountDownLatch latch = new CountDownLatch(3);

for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        doWork();
        latch.countDown();          // main kaam khatam
    }).start();
}

latch.await();                      // jab tak count 0 na ho, wait
System.out.println("Sab threads done!");
```

👉 **Use case:** 3 API calls parallel karo, sab ke results aane pe proceed karo. `ExecutorService.submit()` ke saath combine karo.

⚠️ `CountDownLatch` **reuse nahi** hota — count 0 pe aake khatam. Reuse chahiye to `CyclicBarrier`.

## Q6. CyclicBarrier — sab ek point pe milein
**Answer:** "Sab threads ek point pe ruko, sab aa jaayein to saath chalo."

```java
CyclicBarrier barrier = new CyclicBarrier(3, () -> {
    System.out.println("Sab pahunch gaye — next phase!");
});

for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        doPhase1();
        barrier.await();            // yahan wait karo
        doPhase2();                 // sab aane pe yeh chalega
    }).start();
}
```

👉 **CountDownLatch vs CyclicBarrier:**

| | CountDownLatch | CyclicBarrier |
|---|---|---|
| Kaun wait karta hai | Main thread | Saare worker threads |
| Reuse | ❌ | ✅ |
| Barrier action | Nahi | ✅ Runnable chala sakte ho |

## Q7. BlockingQueue se Producer-Consumer (asli tareeka)
**Answer:** Topic 65 me wait/notify se kiya tha — ab **production-level**:

```java
BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(5);

// Producer
new Thread(() -> {
    for (int i = 1; i <= 10; i++) {
        queue.put(i);                       // full hai to khud wait karega
        System.out.println("Produced: " + i);
    }
}).start();

// Consumer
new Thread(() -> {
    for (int i = 1; i <= 10; i++) {
        int item = queue.take();            // khaali hai to khud wait karega
        System.out.println("Consumed: " + item);
    }
}).start();
```

👉 **Koi wait/notify nahi, koi synchronized nahi** — `BlockingQueue` sab handle karti hai. Interview me **pehle wait/notify wala dikhao**, phir bolo *"production me BlockingQueue use karunga."*

## Q8. ExecutorService se parallel tasks
**Answer:**

```java
try (ExecutorService executor = Executors.newFixedThreadPool(3)) {
    List<Future<String>> futures = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
        int taskId = i;
        futures.add(executor.submit(() -> {
            Thread.sleep(1000);
            return "Task " + taskId + " by " + Thread.currentThread().getName();
        }));
    }

    for (Future<String> f : futures) {
        System.out.println(f.get());        // result ka wait
    }
}
```

👉 **`submit()` = task do, `Future` lo.** `future.get()` = result ka wait. 10 tasks, 3 threads — queue me wait karenge.

## Q9. `invokeAll()` vs `submit()` loop
**Answer:**

```java
// ❌ Ek-ek karke submit + get — partially sequential
for (task : tasks) {
    Future<Result> f = executor.submit(task);
    results.add(f.get());          // YAHAN block ho jaata hai!
}

// ✅ Sab submit karo, phir sab collect karo — truly parallel
List<Future<Result>> futures = executor.invokeAll(tasks);
for (Future<Result> f : futures) results.add(f.get());
```

⚠️ Loop me `submit` + turant `get` = tasks parallel **nahi** chalte. Pehle sab submit, phir sab collect.

## Q10. CompletableFuture — async chaining
**Answer:**

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> fetchUser(userId))           // async fetch
    .thenApply(user -> user.getOrders())             // transform
    .thenApply(orders -> orders.size())              // transform again
    .exceptionally(ex -> 0);                         // error handling

int count = future.join();                           // result lo

// Do futures combine
CompletableFuture<String> combined = userFuture
    .thenCombine(ordersFuture, (user, orders) -> user + " has " + orders);
```

👉 **Callback hell nahi** — chain of transformations. Error bhi chain me handle hota hai.

## Q11. Common multithreading bugs — checklist
**Answer:**

```java
// ❌ Shared counter bina sync
count++;                          // race condition

// ❌ wait() ko if me wrap kiya
if (!ready) wait();               // spurious wakeup pe toot jaayega
// ✅ while (!ready) wait();

// ❌ notify() instead of notifyAll()
notify();                         // galat thread jaag sakta hai
// ✅ notifyAll();                // safe

// ❌ Deadlock — locks alag order me
synchronized(a) { synchronized(b) { } }   // thread 1
synchronized(b) { synchronized(a) { } }   // thread 2 → DEADLOCK

// ❌ Exception me lock release bhool gaye
lock.lock();
doWork();                         // exception → unlock kabhi nahi!
// ✅ try { doWork(); } finally { lock.unlock(); }
```

---

> 💡 **Interview tip:** even-odd wala question aaye to **pehle wait/notify wala** likho — ye dikhata hai tum fundamentals jaante ho. Phir bolo *"ise Semaphore se bhi kar sakta hoon, zyada clean hota hai"* — ye dikhata hai tum modern APIs bhi jaante ho. Dono dikhana = full marks.
