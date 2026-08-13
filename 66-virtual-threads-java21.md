# 66 — Virtual Threads (Java 21) 🚀

Java 21 ka sabse bada feature. **Project Loom** se aaya — aur ye server-side Java ka poora game badal deta hai. Interviews me ab regularly poocha jaata hai.

## Q1. Problem kya thi purane threads me?
**Answer:** Java ka thread = **OS thread** = mehenga.

```java
// Har thread = OS thread
new Thread(() -> handleRequest()).start();
```

**Problem:**
- Ek OS thread ≈ **1-2 MB** stack memory
- OS maximum ~**kuch hazaar** threads handle karta hai
- Thread switch karna **mehenga** hai (context switch)

👉 **Example:** Server pe 10,000 users ek saath aayein — 10,000 OS threads banane me memory khatam. Isliye thread pools (`ExecutorService`) aaye — par wo bhi limit hai.

## Q2. Virtual Thread kya hai?
**Answer:** JVM ka khud ka **halka thread** — OS thread pe depend nahi karta directly.

```java
// Purana tareeka — OS thread
new Thread(task).start();

// Naya tareeka — virtual thread (Java 21+)
Thread.startVirtualThread(task);

// Ya builder se
Thread vt = Thread.ofVirtual().name("my-vt").start(task);
```

| | Platform Thread (purana) | Virtual Thread (naya) |
|---|---|---|
| Kahan chalta hai | OS thread pe | JVM manage karta hai |
| Memory per thread | ~1-2 MB | **~kuch KB** ✅ |
| Kitne bana sakte ho | Hazaar | **Lakhon** ✅ |
| Blocking pe kya hota hai | OS thread block | JVM usko utha ke doosra chala deta hai ✅ |

👉 **Ek million virtual threads** bana sakte ho normal laptop pe. Ye pehle impossible tha.

## Q3. Andar se kaise kaam karta hai?
**Answer:** JVM virtual threads ko **carrier threads** (chhote set of OS threads) pe chalata hai.

```
10,000 virtual threads
        ↓  (JVM schedule karta hai)
   ~8 carrier (OS) threads    ← CPU cores ke barabar
```

**Jab virtual thread block hota hai** (database call, file read):
- JVM usse carrier se **utha leta hai** (unmount)
- Carrier thread kisi **doosre** virtual thread ko chalata hai
- Blocked kaam complete ho → virtual thread **wapas mount**

👉 Matlab: **blocking code bhi threads waste nahi karta.** Isliye connection pool ki zaroorat kam ho gayi.

## Q4. ExecutorService ke saath
**Answer:**

```java
// Java 21 — har task ko apna virtual thread
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        int taskId = i;
        executor.submit(() -> {
            Thread.sleep(1000);            // blocking OK — thread waste nahi hota
            return "Task " + taskId;
        });
    }
}   // auto-close — saare tasks ka wait
```

👉 **`newVirtualThreadPerTaskExecutor()`** — pool nahi, har task pe naya virtual thread. Thread pool sizing ka tension khatam.

## Q5. Kab virtual threads use karein?
**Answer:**

✅ **Perfect for:**
- **I/O bound kaam** — database calls, API calls, file read/write
- Web servers — har request pe ek thread
- Microservices — thousands of parallel calls

❌ **NAHI for:**
- **CPU bound kaam** — video processing, calculations, hashing
- Kyunki CPU bound me threads ki sankhya se farak nahi padta — cores fixed hain

👉 **Rule:** *"Wait zyada, kaam kam"* → virtual threads. *"Kaam zyada, wait kam"* → platform threads (ya parallel streams).

## Q6. Virtual threads ki limitations
**Answer:** Do important caveats:

**1. Pinning** — `synchronized` block me block hua to carrier bhi block:

```java
// ⚠️ synchronized ke andar blocking call = carrier thread pin ho gaya
synchronized (lock) {
    Thread.sleep(1000);        // ye poora carrier block kar deta hai!
}

// ✅ ReentrantLock use karo — pinning nahi hota
lock.lock();
try {
    Thread.sleep(1000);        // virtual thread unmount ho jaayega
} finally {
    lock.unlock();
}
```

Java 24 ne synchronized wala issue mostly fix kar diya, par jaanna zaroori hai.

**2. ThreadLocal dhyan se:**
- Lakhon threads = lakhon ThreadLocal copies = memory blast
- Virtual threads me ThreadLocal **inheritance** off hoti hai by default

## Q7. Structured Concurrency kya hai?
**Answer:** Java 21 me preview — related tasks ko ek unit ki tarah manage karna.

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Supplier<User> user = scope.fork(() -> fetchUser(id));
    Supplier<Orders> orders = scope.fork(() -> fetchOrders(id));

    scope.join();              // dono ka wait
    scope.throwIfFailed();     // koi fail hua to exception

    return new Dashboard(user.get(), orders.get());
}
```

👉 **Faayda:** ek task fail → baaki **automatic cancel**. Scope band → sab threads clean up. Thread leak ka chance zero.

## Q8. Platform thread se virtual thread kab ban jata hai?
**Answer:** **Kabhi nahi.** Ye do alag cheezein hain:

- Platform thread = OS thread ka wrapper
- Virtual thread = JVM object jo carrier pe **chalta hai**

```java
Thread.currentThread().isVirtual();    // true/false se check karo
```

## Q9. Spring Boot me virtual threads
**Answer:** Ek line ki config:

```properties
# application.properties — bas itna!
spring.threads.virtual.enabled=true
```

👉 Iske baad Spring har HTTP request ko **virtual thread** pe chalata hai. Tomcat, WebFlux sab compatible. High-traffic apps me throughput kaafi badh jaata hai.

## Q10. Interview me kaise explain karein?
**Answer:**

> *"Virtual threads JVM-managed hain, OS threads nahi. Ek million tak bana sakte ho kyunki har thread sirf kuch KB leta hai. Jab virtual thread block hota hai — jaise database call pe — JVM usse carrier thread se hata deta hai aur carrier doosra virtual thread chalata hai. Isliye I/O-heavy apps me thread pool sizing ka tension khatam. Par CPU-bound kaam me fayda nahi, kyunki wahan cores hi limit hain. Aur synchronized blocks pinning kar sakte hain, isliye ReentrantLock better hai."*

👉 Ye 30-second answer **saare important points** cover karta hai.

## Q11. Virtual vs Platform vs Reactive — comparison
**Answer:**

| | Platform Threads | Virtual Threads | Reactive (WebFlux) |
|---|---|---|---|
| Code style | Simple, blocking | Simple, blocking ✅ | Complex, callbacks |
| Scale | ~thousands | ~millions ✅ | ~millions ✅ |
| Debug karna | Aasan | Aasan ✅ | **Mushkil** ❌ |
| Seekhna | Aasan | Aasan ✅ | Steep curve ❌ |
| Stack traces | Clear | Clear ✅ | Confusing ❌ |

👉 **Virtual threads ka asli selling point:** reactive jaisi scale, **simple blocking code ke saath.** Isliye kai teams WebFlux se wapas aa rahi hain.

---

> 💡 **Interview tip:** agar interviewer Java 21 poochhe to virtual threads **sabse pehle** mention karo — ye hot topic hai. Aur yeh line bolna: *"Virtual threads ne reactive programming ki zaroorat kam kar di — ab simple blocking code bhi million requests handle kar sakta hai."* Ye dikhata hai tum **industry trends** follow karte ho, sirf syntax nahi.
