# 🔴 Multithreading & Concurrency — 12 Questions

### Q1. What are the ways to create a thread?
**Answer:**
1. Extend `Thread`, override `run()`.
2. Implement `Runnable` (preferred — leaves inheritance free).
3. Implement `Callable` + `FutureTask` (returns a value, can throw).
4. Use an **ExecutorService** (production standard).

```java
new Thread(() -> System.out.println("running")).start();
```

---

### Q2. Runnable vs Callable?
**Answer:** `Runnable.run()` returns nothing and can't throw checked exceptions. `Callable.call()` **returns a value** (`Future<T>`) and can throw. Use Callable when you need a result back.

---

### Q3. What is the thread lifecycle?
**Answer:** `NEW → RUNNABLE → (BLOCKED / WAITING / TIMED_WAITING) → TERMINATED`. `start()` moves NEW→RUNNABLE; the OS scheduler decides actual running; locks/waits move it to blocked states.

---

### Q4. What does `synchronized` do?
**Answer:** Only one thread at a time can hold an object's **monitor lock** — protecting critical sections from race conditions. It also establishes *happens-before*: changes made inside are visible to the next thread acquiring the lock.

---

### Q5. What is `volatile`?
**Answer:** Guarantees **visibility** — reads/writes go to main memory, never a thread-local cache, and prevents instruction reordering around it. It does **NOT** make compound operations like `count++` atomic (use `AtomicInteger` for that).

---

### Q6. `wait()` vs `sleep()`?
**Answer:**
| | wait() | sleep() |
|--|--------|--------|
| Class | Object | Thread |
| Releases lock? | ✅ yes | ❌ no |
| Wakes on | notify()/notifyAll() | timeout |
| Must hold lock? | ✅ (synchronized) | ❌ |

---

### Q7. What is a deadlock? How do you prevent it?
**Answer:** Two+ threads each hold a lock the other needs — all wait forever.
**Prevention:** always acquire locks in a **fixed global order**, use `tryLock` with timeout, keep lock scope small, or use higher-level concurrency utilities.

```java
// Thread 1: lock A → lock B   |   Thread 2: lock B → lock A  💥 deadlock
```

---

### Q8. What is ExecutorService? Why use it over raw threads?
**Answer:** A **thread pool** manager — reuses threads instead of creating one per task (creation is expensive), gives you Futures, scheduling, and graceful shutdown.

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
Future<Integer> f = pool.submit(() -> heavyComputation());
pool.shutdown();
```

---

### Q9. Synchronized method vs synchronized block?
**Answer:** A synchronized method locks the whole method on `this` (or the Class for static). A synchronized block locks **only the critical lines** on **any chosen object** — finer-grained = better throughput.

---

### Q10. What is AtomicInteger? How does it work?
**Answer:** A lock-free thread-safe integer using **CAS (compare-and-swap)** CPU instructions. `incrementAndGet()` retries until the swap succeeds — much faster than `synchronized` for simple counters.

---

### Q11. Explain the producer-consumer problem.
**Answer:** Producers add to a shared buffer, consumers remove; must block when full/empty. Classic solution is `wait()/notifyAll()`, modern solution is a **BlockingQueue**:

```java
BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
// producer: queue.put(item);  — blocks if full
// consumer: queue.take();     — blocks if empty
```

---

### Q12. What is ThreadLocal?
**Answer:** A variable where **each thread has its own independent copy** — no synchronization needed. Used for per-request state (user context, date formatters, DB connections). Remember to `remove()` in thread pools to avoid leaks.

---

⬅️ [Prev: Exceptions](05-exception-handling.md) | ➡️ [Next: Java 8+](07-java8-and-beyond.md)
