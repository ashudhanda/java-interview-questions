# 65 — Deadlock, wait/notify & Locks 🔒

Topic 06 me threads the, topic 31 me ExecutorService, topic 53 me concurrent collections. Ab **threads aapas me kaise baat karte hain** — aur sabse famous villain: **deadlock**.

## Q1. Deadlock kya hai?
**Answer:** Do (ya zyada) threads **ek dusre ka wait** kar rahe hain — aur koi aage nahi badh sakta. Hamesha ke liye atak gaye.

**Real life:** Do gaadiyan narrow bridge pe aamne-saamne — dono bolte hain "pehle tu peeche hat". Koi nahi hilta.

```java
Object lockA = new Object();
Object lockB = new Object();

// Thread 1: A pakdo, phir B chahiye
new Thread(() -> {
    synchronized (lockA) {
        sleep(100);                          // ← dusre ko chance mila
        synchronized (lockB) { /* kaam */ }  // B ka wait... forever
    }
}).start();

// Thread 2: B pakdo, phir A chahiye
new Thread(() -> {
    synchronized (lockB) {
        sleep(100);
        synchronized (lockA) { /* kaam */ }  // A ka wait... forever
    }
}).start();
```

Thread 1 ne A pakda, Thread 2 ne B. Ab dono ek dusre ke lock ka wait kar rahe hain — **program hang, CPU 0%** (busy nahi, bas wait).

## Q2. Deadlock ki 4 conditions
**Answer:** Interview me ye **chaaron** yaad hone chahiye (Coffman conditions):

| # | Condition | Matlab |
|---|---|---|
| 1 | **Mutual Exclusion** | Lock ek waqt pe ek hi thread ke paas |
| 2 | **Hold and Wait** | Ek lock pakde hue, doosre ka wait |
| 3 | **No Preemption** | Lock zabardasti cheena nahi ja sakta |
| 4 | **Circular Wait** | A→B→A ka circle bana hua hai |

👉 **Ek bhi condition todo — deadlock khatam.** Sabse aasan: **Circular Wait** todo.

## Q3. Deadlock se kaise bachein?
**Answer:**

```java
// ✅ FIX: dono threads SAME order me locks lo (hamesha A pehle)
new Thread(() -> {
    synchronized (lockA) {
        synchronized (lockB) { /* kaam */ }
    }
}).start();

new Thread(() -> {
    synchronized (lockA) {                   // B pehle nahi, A pehle!
        synchronized (lockB) { /* kaam */ }
    }
}).start();
```

**Doosre tareeke:**
- **`tryLock()` with timeout** — lock na mile to chhod do, hamesha wait mat karo
- **Kam locks** — jitne kam locks, utna kam risk
- **`java.util.concurrent` use karo** — raw synchronized se bachne ka best tareeka hi ye hai

## Q4. `tryLock()` se deadlock kaise tootta hai?
**Answer:**

```java
Lock lockA = new ReentrantLock();
Lock lockB = new ReentrantLock();

void doWork() {
    while (true) {
        if (lockA.tryLock()) {
            try {
                if (lockB.tryLock()) {
                    try {
                        return;              // dono mil gaye — kaam karo
                    } finally { lockB.unlock(); }
                }
            } finally { lockA.unlock(); }    // B nahi mila → A bhi chhod do
        }
        sleep(50);                           // thoda ruk ke phir try
    }
}
```

👉 Lock na mile to **jo mila tha wo bhi chhod do** — "hold and wait" condition toot gayi. Koi hamesha wait nahi karta.

## Q5. `wait()`, `notify()`, `notifyAll()` kya hain?
**Answer:** Threads ka **baat karne** ka tareeka — "abhi meri baari nahi, tum karo, mujhe bula lena."

```java
synchronized (lock) {
    while (!conditionMet) {
        lock.wait();         // lock chhod do, so jaao
    }
    // ... kaam karo
    lock.notifyAll();        // sabko jaga do
}
```

| Method | Kaam |
|---|---|
| `wait()` | Lock chhod do, notify ka wait karo |
| `notify()` | Ek waiting thread jagaao (kaunsa — guarantee nahi) |
| `notifyAll()` | **Saare** waiting threads jagaao ✅ |

⚠️ **Teen zaroori baatein:**
1. Ye methods **sirf `synchronized` block ke andar** chal sakte hain — warna `IllegalMonitorStateException`
2. Ye **`Object` class** ke methods hain, `Thread` ke nahi (kyunki lock object pe wait hota hai)
3. `wait()` hamesha **`while` loop** me rakho, `if` me nahi — **spurious wakeup** ho sakta hai (bina notify ke jaag sakta hai!)

## Q6. Producer-Consumer — wait/notify se
**Answer:** Classic interview question:

```java
class SharedBuffer {
    private Queue<Integer> buffer = new LinkedList<>();
    private int capacity = 5;

    synchronized void produce(int item) throws InterruptedException {
        while (buffer.size() == capacity) {
            wait();                          // full hai — consumer ka wait
        }
        buffer.offer(item);
        System.out.println("Produced: " + item);
        notifyAll();                         // consumer ko jagaao
    }

    synchronized int consume() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();                          // khaali hai — producer ka wait
        }
        int item = buffer.poll();
        System.out.println("Consumed: " + item);
        notifyAll();                         // producer ko jagaao
        return item;
    }
}
```

👉 **Modern code me ye mat likho** — `BlockingQueue` same kaam karti hai, bug-free (topic 53). Par interview me wait/notify se likhna poocha jaata hai.

## Q7. `synchronized` vs `ReentrantLock`
**Answer:**

| | `synchronized` | `ReentrantLock` |
|---|---|---|
| Lock release | Automatic ✅ | `finally` me `unlock()` khud karo |
| Timeout ke saath try | ❌ | ✅ `tryLock(1, SECONDS)` |
| Interruptible wait | ❌ | ✅ `lockInterruptibly()` |
| Fairness option | ❌ | ✅ `new ReentrantLock(true)` |
| Seekhna | Aasan ✅ | Thoda mushkil |

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();          // ← finally me ZAROOR, warna lock atak jaayega
}
```

👉 **Default choice `synchronized`** — simple aur safe. Advanced features chahiye tab hi `ReentrantLock`.

## Q8. `volatile` vs `synchronized` — final clarity
**Answer:** (Topic 30 ka quick recap)

| | `volatile` | `synchronized` |
|---|---|---|
| Visibility | ✅ | ✅ |
| Atomicity | ❌ | ✅ |
| Speed | Tez ✅ | Dheema |
| `count++` safe? | ❌ Nahi | ✅ Haan |

```java
volatile boolean running = true;     // ✅ flag ke liye perfect
volatile int count = 0;              // ❌ count++ abhi bhi unsafe!
```

👉 Flag ke liye `volatile`, counter ke liye `synchronized` ya `AtomicInteger`.

## Q9. Race condition vs Deadlock vs Livelock vs Starvation
**Answer:**

| Problem | Kya hota hai |
|---|---|
| **Race condition** | Do threads same data pe — result order pe depend |
| **Deadlock** | Dono wait me — koi nahi chalta |
| **Livelock** | Dono **active** hain par baar-baar ek dusre ko raasta de rahe — kaam zero |
| **Starvation** | Ek thread ko kabhi chance hi nahi milta (low priority) |

👉 Livelock ka example: do log galle me ek dusre ko side dene me dono same side shift ho jaate hain — hilti rehte hain, nikalte nahi.

## Q10. `Thread.sleep()` vs `wait()`
**Answer:** Classic confusion:

| | `sleep()` | `wait()` |
|---|---|---|
| Class | `Thread` | `Object` |
| Lock chhodta hai? | ❌ **Nahi** | ✅ Haan |
| synchronized chahiye? | Nahi | **Haan** |
| Jaagta kaise? | Time khatam | `notify()` |
| Use case | Bas rukna hai | Thread coordination |

⚠️ **`sleep()` lock pakde hue sota hai** — synchronized block me sleep = baaki threads block. Ye common bug hai.

## Q11. Deadlock ka pata kaise lagayein?
**Answer:**

```bash
jps                    # Java process ki ID nikalo
jstack <pid>           # thread dump — "Found one Java-level deadlock" dikhega
```

IDE me: debugger se threads pause karo — deadlock me saare `BLOCKED` dikhenge.

👉 Interview me: *"main `jstack` se thread dump lunga aur `BLOCKED` threads ke lock owners dekhunga — circular chain dikhi to deadlock confirm."*

---

> 💡 **Interview tip:** deadlock ka code likhwao to **pehle broken version mat likho.** Bolo: *"Deadlock tab hota hai jab do threads opposite order me locks lete hain — isliye main hamesha fixed order me locks leta hoon"* — aur seedha sahi version likho. Prevention jaanna solution likhne se zyada valuable hai.
