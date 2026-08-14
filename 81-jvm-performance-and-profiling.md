# 81 — JVM Performance & Profiling 🔧

Topic 10 me JVM basics the, 24 me GC. Ab **practical side** — app slow hai to kya karein, kaunse tools use karein, kaunse flags set karein.

## Q1. App slow hai — pehla step kya?
**Answer:** **Guess mat karo, measure karo.**

```
1. REPRODUCE  — slow kab hota hai? load pe? specific API pe?
2. MEASURE    — profiler se dekho asli waqt kahan lag raha hai
3. FIX        — bottleneck fix karo
4. VERIFY     — dobara measure karo, farak prove karo
```

⚠️ **Sabse badi galti:** bina measure kiye optimize karna. 90% baar wo hissa slow hota hai jo tumne socha bhi nahi tha.

👉 **Famous rule:** *"Premature optimization is the root of all evil"* — Donald Knuth. Pehle sahi code likho, phir slow ho to measure karke fix karo.

## Q2. JVM memory flags
**Answer:**

```bash
java -Xms512m -Xmx2g -jar app.jar
     ↑            ↑
     start heap   max heap
```

| Flag | Kaam |
|---|---|
| `-Xms` | Shuru me kitna heap |
| `-Xmx` | Maximum heap |
| `-Xss` | Har thread ka stack size |
| `-XX:+UseG1GC` | G1 garbage collector use karo |

👉 **`-Xms` aur `-Xmx` same rakhna** production me — heap resize ki overhead nahi hogi.

## Q3. Garbage Collectors — kaunsa kab?
**Answer:**

| GC | Kab use |
|---|---|
| **Serial** | Chhoti apps, ek CPU |
| **Parallel** | Batch jobs — throughput important, pause chalega |
| **G1** (default) | ✅ Zyada tar apps — balanced |
| **ZGC / Shenandoah** | Bahut bade heaps (100GB+), ultra-low pause |

```bash
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar app.jar
```

👉 **G1 default hai** (Java 9+) — zyada tar cases me wahi theek. GC tuning tab karo jab measure karke pata ho ki GC hi problem hai.

## Q4. OutOfMemoryError ke types
**Answer:** Sab alag hain — ye interview me poochte hain:

| Error | Matlab | Fix |
|---|---|---|
| `Java heap space` | Objects zyada, heap kam | `-Xmx` badhao / leak dhoondo |
| `Metaspace` | Bahut saari classes load ho gayi | `-XX:MaxMetaspaceSize` |
| `GC overhead limit` | GC 98% waqt kar raha, kaam 2% | Heap badhao / leak fix |
| `unable to create native thread` | Bahut zyada threads | Thread pool use karo |

```java
// Heap space error ka classic example
List<byte[]> list = new ArrayList<>();
while (true) {
    list.add(new byte[10_000_000]);   // 10MB har loop — kabhi release nahi
}
```

## Q5. Memory leak ka pata kaise lagayein?
**Answer:**

```bash
# Heap dump lo (live app se!)
jmap -dump:live,format=b,file=heap.hprof <pid>

# Ya automatic — crash pe dump ban jaaye
java -XX:+HeapDumpOnOutOfMemoryError -jar app.jar
```

Dump ko **Eclipse MAT** (Memory Analyzer Tool) me kholo — dikhata hai:
- Kaunse objects sabse zyada memory le rahe hain
- Kaun unko hold kar raha hai (retained heap)
- Leak suspects report

👉 **Common leak sources:**
- `static` collections jo kabhi clear nahi hote
- Unclosed connections/streams
- Listeners jo unregister nahi hue
- `ThreadLocal` jo remove nahi kiya (topic 66)

## Q6. Thread dump — kab aur kaise?
**Answer:** App **hang** hai ya slow hai → thread dump:

```bash
jstack <pid> > threads.txt
# 3 baar lo, 5 sec gap me — compare karo
```

**Kya dhoondho:**
- `BLOCKED` threads — kis lock pe ruke hain → **deadlock** (topic 65)
- `RUNNABLE` bahut saare → CPU busy
- `WAITING`/`TIMED_WAITING` — kis cheez ka wait?

👉 3 dumps compare karo — jo thread teeno me same line pe hai, wahi stuck hai.

## Q7. Profiling tools
**Answer:**

| Tool | Kaam | Kahan |
|---|---|---|
| `jconsole` | Memory, threads live dekho | JDK ke saath free |
| `jvisualvm` | CPU + memory profiling | JDK ke saath free |
| **JFR** (Flight Recorder) | Production profiling, low overhead | Built-in ✅ |
| **Async-profiler** | CPU + allocation flame graphs | Production-safe |
| Eclipse MAT | Heap dump analysis | Free |

```bash
# JFR se 60 second ka recording
jcmd <pid> JFR.start duration=60s filename=app.jfr
```

👉 **JFR production me chal sakta hai** — overhead sirf ~1-2%. Ye senior-level knowledge hai.

## Q8. Flame graph kya hai?
**Answer:** Ek visualization — dikhata hai **CPU time kahan ja raha hai**.

```
        ┌──────┐
        │ main │
     ┌──┴──────┴───┐
     │ processOrder │
  ┌──┴───┐    ┌────┴────┐
  │validate│    │  DB call  │   ← sabse WIDE = sabse zyada time
  └───────┘    └─────────┘
```

👉 **Jitna wide box, utna zyada time.** Sabse wide dhoondo — wahi optimize karo. DB call wide hai to query fix karo, JVM settings mat chhedo.

## Q9. String concatenation performance
**Answer:** Micro-optimization ka classic:

```java
// ❌ Loop me — O(n²), har baar nayi String
String s = "";
for (int i = 0; i < 10000; i++) s += i;

// ✅ StringBuilder — O(n)
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) sb.append(i);
```

⚠️ **Single line me `+` theek hai** — compiler khud StringBuilder bana deta hai:
```java
String msg = "Hello " + name + "!";   // ✅ ye fine hai
```
Problem sirf **loop me `+=`** hai.

## Q10. Logging ki performance cost
**Answer:**

```java
// ❌ String concat hamesha hota hai — DEBUG off ho tab bhi
log.debug("Processing " + user.getName() + " with " + items.size() + " items");

// ✅ Parameterized — sirf tab banta hai jab DEBUG on ho
log.debug("Processing {} with {} items", user.getName(), items.size());

// ✅ Expensive call ho to guard lagao
if (log.isDebugEnabled()) {
    log.debug("Heavy: {}", expensiveComputation());
}
```

👉 Topic 55 me bhi tha — performance ke context me yaad rakho: **logging galat karne se fast app bhi slow ho jaata hai.**

## Q11. Quick wins checklist
**Answer:** Performance issue aaye to ye order me dekho:

```
1. DB queries     — N+1? missing index? (80% cases yahi hota hai)
2. External APIs  — synchronous calls? timeout set hai?
3. Memory         — heap bhar raha? GC pauses lambi?
4. Threads        — pool chhota? deadlock?
5. Code           — loop me heavy kaam? String +=?
```

👉 **80% performance problems database se hote hain** — pehle wahan dekho. JVM flags baad me.

---

> 💡 **Interview tip:** *"app slow hai to kya karoge?"* pe ye framework bolo: *"Pehle reproduce karke profiler se measure karunga — guess nahi. Zyada tar cases me DB queries (N+1, missing index) culprit hoti hain. Heap issue lage to heap dump leke MAT me analyze karunga. Aur fix ke baad dobara measure karke prove karunga ki farak aaya."* Measure → Fix → Verify — ye loop bolna hi senior thinking hai.
