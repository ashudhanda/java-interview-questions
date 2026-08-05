# 24 — Garbage Collection 🗑️

"GC kaise kaam karta hai?" — JVM internals ka favorite question. Ye file padh ke confidently answer kar sakte ho.

## Q1. What is garbage collection?
**Answer:** JVM ka automatic memory management — **unreachable objects** (jinki koi live reference nahi) heap se free ho jate hain. C/C++ jaisi manual `free()` ki zaroorat nahi.

## Q2. When is an object eligible for GC?
**Answer:** Jab usse **koi bhi live thread ya GC root se reach nahi** kiya ja sakta.

```java
Student s = new Student();
s = null;              // ab unreachable → eligible
// ya
s = new Student();     // purana object eligible
```
⚠️ Eligible ≠ turant delete — GC **kab** chalega, JVM decide karta hai.

## Q3. What are GC roots?
**Answer:** Starting points jinse reachability trace hoti hai:
- Active threads ke **stack frames** ke local variables
- **Static** fields of loaded classes
- JNI references

Inse chain se jo objects reachable hain wo **live**; baaki garbage.

## Q4. How does generational GC work?
**Answer:** Heap divided into generations — "most objects die young" assumption pe based.

1. **Young Generation** (Eden + 2 Survivor spaces) — new objects yahan bante hain. Minor GC frequently chalta hai, bahut fast.
2. **Old Generation** — jo objects kai Minor GC survive kar lein, promote ho jate hain. Major GC rare but slow.
3. **Metaspace** (Java 8+) — class metadata (purana PermGen hata diya gaya).

Object ka safar: Eden → (survive) → Survivor S0/S1 → (kai cycles survive) → Old Gen.

## Q5. Mark-and-Sweep algorithm kya hai?
**Answer:**
1. **Mark** — GC roots se traverse karke live objects mark karo
2. **Sweep** — unmarked objects ki memory free karo
3. (Optional) **Compact** — fragmentation hatane ke liye live objects ek side shift karo

"Stop-the-world" pause hota hai is dauran — app threads ruk jati hain.

## Q6. Which garbage collectors does Java provide?
**Answer:**

| GC | Best for |
|----|---------|
| **Serial** (`-XX:+UseSerialGC`) | Small apps, single CPU |
| **Parallel** (throughput) | Batch processing |
| **G1** (default since Java 9) | Balanced — most apps |
| **ZGC / Shenandoah** | Ultra-low latency (pause < 1ms), huge heaps |

Interview line: *"G1 region-based hai, incremental pauses, aur predictable pause target set kar sakte ho."*

## Q7. Can you force garbage collection?
**Answer:** ❌ Nahi. `System.gc()` sirf **suggestion** hai — JVM ignore kar sakta hai. Interview trick question: *"finalize() call karne se object turant delete hoga?"* — Nahi, aur `finalize()` Java 9 se **deprecated** hai.

## Q8. What is a memory leak in Java (GC ke baawajood)?
**Answer:** Objects jo logically dead hain par **references abhi bhi live** hain:
- Static collections me add karte jaana (cache bina eviction)
- Listeners/callbacks unregister na karna
- `HashMap` keys jinke `equals/hashCode` galat overridden hain
- Unclosed resources (GC memory to free karega, par OS handles nahi — isliye try-with-resources!)