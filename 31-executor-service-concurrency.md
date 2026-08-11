# 31 — ExecutorService & Concurrency Utilities ⚙️

`new Thread()` manually banana purana tareeka hai. Real projects me **thread pools** use hote hain. Ye topic multithreading round ka second half hai.

## Q1. `new Thread()` ki jagah thread pool kyun?
**Answer:** Har request pe nayi thread banana **mehnga** hai — memory lagti hai, OS-level context switching badh jaati hai, aur 10,000 requests pe JVM crash bhi ho sakta hai.

Thread pool me **fixed number of threads** pehle se bani rehti hain, aur tasks ek queue me aake un threads pe reuse hote hain. Thread creation cost ek hi baar lagti hai.

## Q2. `ExecutorService` ka basic use dikhao
**Answer:**

```java
ExecutorService pool = Executors.newFixedThreadPool(4);

pool.submit(() -> System.out.println("Task chal raha hai"));

pool.shutdown();   // naye tasks band, purane complete honge
```

⚠️ `shutdown()` call karna **mat bhoolo** — warna JVM exit nahi hoga, threads zinda rehti hain.

## Q3. Kaunse types ke thread pools hote hain?
**Answer:**

| Factory method | Kya karta hai | Kab use karo |
|---|---|---|
| `newFixedThreadPool(n)` | Exactly `n` threads | Load predictable ho |
| `newCachedThreadPool()` | Zaroorat pe threads banata/hataata hai | Bahut saare chhote-chhote tasks |
| `newSingleThreadExecutor()` | Sirf 1 thread, order guaranteed | Tasks sequence me chahiye |
| `newScheduledThreadPool(n)` | Delay ya repeat pe chalta hai | Cron-jaise scheduled kaam |
| `newVirtualThreadPerTaskExecutor()` | Har task pe virtual thread (Java 21+) | Hazaaron blocking I/O calls |

## Q4. `submit()` vs `execute()` — farak?
**Answer:**

- `execute(Runnable)` — kuch return nahi karta. Exception aaye to seedha thread ke uncaught handler pe jaata hai.
- `submit(Runnable/Callable)` — ek **`Future`** return karta hai. Exception `Future` ke andar **chhup jaata hai** aur tabhi dikhta hai jab tum `future.get()` call karo.

```java
Future<?> f = pool.submit(() -> { throw new RuntimeException("boom"); });
// yahan kuch print nahi hoga...
f.get();   // ...exception yahan ExecutionException banke aayega
```

👉 **Interview trap:** "`submit()` use kiya aur exception gayab ho gaya" — kyunki `get()` call hi nahi kiya.

## Q5. `Runnable` vs `Callable`?
**Answer:**

| | `Runnable` | `Callable<V>` |
|---|---|---|
| Method | `void run()` | `V call()` |
| Return value | ❌ | ✅ |
| Checked exception throw | ❌ | ✅ |

```java
Callable<Integer> task = () -> 2 + 2;
Future<Integer> future = pool.submit(task);
System.out.println(future.get());   // 4
```

## Q6. `Future` ki limitation kya hai?
**Answer:** `future.get()` **blocking** hai — result aane tak thread ruki rehti hai. Aur do `Future` ko chain ya combine karne ka koi clean tareeka nahi hai. Isi problem ko solve karne ke liye Java 8 me `CompletableFuture` aaya.

## Q7. `CompletableFuture` kaise kaam karta hai?
**Answer:** Non-blocking, chainable async programming deta hai.

```java
CompletableFuture
    .supplyAsync(() -> fetchUser())        // background me chalega
    .thenApply(user -> user.getName())     // result transform
    .thenAccept(name -> print(name))       // result consume
    .exceptionally(ex -> { log(ex); return null; });   // error handle
```

Kuch kaam ke methods:
- `supplyAsync()` — value return karne wala async task
- `runAsync()` — void async task
- `thenApply()` — result badlo (map jaisa)
- `thenCompose()` — do async calls ko chain karo (flatMap jaisa)
- `thenCombine()` — do independent results ko jodo
- `allOf()` — saare complete hone ka wait

## Q8. `thenApply()` vs `thenCompose()`?
**Answer:** Bilkul `map` vs `flatMap` wala farak hai.

- `thenApply` — tumhara function **normal value** return karta hai
- `thenCompose` — tumhara function khud ek **`CompletableFuture`** return karta hai

```java
// Galat: nested future ban gaya
CompletableFuture<CompletableFuture<User>> bad = cf.thenApply(id -> fetchUser(id));

// Sahi: flat rehta hai
CompletableFuture<User> good = cf.thenCompose(id -> fetchUser(id));
```

## Q9. `shutdown()` vs `shutdownNow()`?
**Answer:**
- `shutdown()` — **graceful**. Naye tasks accept nahi karta, par queue me pade tasks pure karta hai.
- `shutdownNow()` — **forceful**. Running threads ko interrupt karta hai aur pending tasks ki list return karta hai.

Production ka standard pattern:

```java
pool.shutdown();
if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
    pool.shutdownNow();
}
```

## Q10. `CountDownLatch` aur `Semaphore` kya hain?
**Answer:**
- **`CountDownLatch`** — ek thread tab tak wait kare jab tak N kaam pure na ho jaayein. Ek baar use hone ke baad reset nahi hota.
  ```java
  CountDownLatch latch = new CountDownLatch(3);
  // har worker: latch.countDown();
  latch.await();   // teeno khatam hone tak yahan ruko
  ```
- **`Semaphore`** — ek saath kitni threads andar ja sakti hain, ye limit karta hai (jaise 5 DB connections).
  ```java
  Semaphore sem = new Semaphore(5);
  sem.acquire();
  try { useConnection(); } finally { sem.release(); }
  ```

---

> 💡 **Interview tip:** Agar poochein "multithreading kaise handle karte ho?", to `new Thread()` mat bolna. Bolo: *"`ExecutorService` se thread pool banata hoon, async chaining ke liye `CompletableFuture` use karta hoon, aur `awaitTermination` ke saath graceful shutdown karta hoon."*
