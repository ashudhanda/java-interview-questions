# 53 — Concurrent Collections 🔐

Topic 06 me threads the, topic 31 me ExecutorService. Ab wo collections jo **multiple threads** ke saath safe hain — aur `synchronized` se behtar hain.

## Q1. `HashMap` multithreading me kyun kharab hai?
**Answer:** Do problem hoti hain:

1. **Data loss** — do threads ek saath `put()` karein to ek entry gum ho sakti hai
2. **Infinite loop** — Java 7 me resize ke waqt linked list me cycle ban jaati thi, CPU 100% chala jaata tha

Java 8 me infinite loop wala bug theek ho gaya (tail insertion aa gaya), par **data loss ab bhi hota hai**. `HashMap` thread-safe nahi hai, bas itni baat.

```java
Map<String, Integer> map = new HashMap<>();
// 2 threads ek saath put() → count galat aayega
```

## Q2. `Hashtable` vs `Collections.synchronizedMap()` vs `ConcurrentHashMap`
**Answer:**

| | `Hashtable` | `synchronizedMap()` | `ConcurrentHashMap` |
|---|---|---|---|
| Locking | Poori map pe | Poori map pe | **Sirf bucket pe** ✅ |
| Ek waqt me | 1 thread | 1 thread | **Kai threads** ✅ |
| `null` key/value | ❌ dono nahi | ✅ allowed | ❌ dono nahi |
| Performance | Slow | Slow | **Fast** ✅ |
| Status | Legacy ❌ | Rarely used | **Yahi use karo** ✅ |

👉 **Jawab hamesha `ConcurrentHashMap` hai.** Baaki do sirf history ke liye jaanne hain.

## Q3. `ConcurrentHashMap` andar se kaise kaam karta hai?
**Answer:** Ye poocha hi jaata hai.

**Java 7 me:** *Segment locking* — map 16 segments me batti thi, har segment ka apna lock. 16 threads ek saath kaam kar sakte the.

**Java 8 me (ab):** Segments hata diye. Ab:
- **`synchronized` sirf us ek bucket (bin) ke pehle node pe** lagta hai
- Khaali bucket me insert **CAS** (Compare-And-Swap) se hota hai — lock hi nahi lagta
- Bucket bada ho jaaye to linked list → **red-black tree** ban jaati hai (jaise `HashMap` me)

👉 **Matlab:** alag-alag buckets pe kaam kar rahe threads ek dusre ko **bilkul block nahi karte**. Isliye ye itna tez hai.

## Q4. `ConcurrentHashMap` me `null` kyun allowed nahi?
**Answer:** **Ambiguity** ki wajah se.

```java
map.get(key);   // null aaya — ab iska matlab kya?
                // (a) key hai hi nahi?
                // (b) key hai par value null hai?
```

Single-thread me `containsKey()` se pata kar lete. Par multi-thread me do calls ke **beech me** koi aur thread value badal sakta hai — to check bekaar ho jaata hai. Isliye `null` ban kar diya.

👉 Ye design decision wala jawab interviewer ko bahut pasand aata hai.

## Q5. Atomic operations — counter theek se kaise banayein?
**Answer:**

```java
// ❌ GALAT — race condition
if (map.containsKey(key)) {
    map.put(key, map.get(key) + 1);      // check aur update ke beech gap!
} else {
    map.put(key, 1);
}

// ✅ SAHI — atomic
map.merge(key, 1, Integer::sum);
map.compute(key, (k, v) -> v == null ? 1 : v + 1);
map.computeIfAbsent(key, k -> new ArrayList<>());
map.putIfAbsent(key, value);
```

⚠️ **Yaad rakho:** `ConcurrentHashMap` har **individual method** ko atomic banata hai, par **do methods ka combination** atomic nahi hota. Isliye `merge`/`compute` wale single-call methods use karo.

## Q6. `CopyOnWriteArrayList` kya hai?
**Answer:** Har **write** pe poori array ki **nayi copy** banti hai.

```java
List<String> list = new CopyOnWriteArrayList<>();
```

| | Read | Write |
|---|---|---|
| Speed | **Bahut tez** ✅ (koi lock nahi) | **Bahut slow** ❌ (`O(n)` copy) |

👉 **Kab use karein:** jab reads **bahut zyada** aur writes **na ke barabar** hon. Classic example: **event listeners** ki list — ek baar register hoti hai, hazaaron baar padhi jaati hai.

⚠️ **Kabhi use mat karo** jab loop me `add()` kar rahe ho — `n` elements add karne me `O(n²)` lag jaayega.

## Q7. `ConcurrentModificationException` kya hai?
**Answer:** Iterate karte waqt collection modify kar do → ye exception.

```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));

for (String s : list) {
    if (s.equals("b")) list.remove(s);      // ❌ ConcurrentModificationException
}
```

⚠️ **Naam dhokha deta hai** — ye **single thread** me bhi aata hai! "Concurrent" ka matlab yahan "iterate aur modify ek saath".

**Teen sahi tareeke:**
```java
// 1. Iterator ka remove
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("b")) it.remove();     // ✅
}

// 2. removeIf — sabse clean
list.removeIf(s -> s.equals("b"));              // ✅

// 3. CopyOnWriteArrayList — iterator snapshot pe chalta hai
```

## Q8. Fail-fast vs Fail-safe iterator
**Answer:**

| | Fail-fast | Fail-safe |
|---|---|---|
| Kaun | `ArrayList`, `HashMap` | `ConcurrentHashMap`, `CopyOnWriteArrayList` |
| Modify hone pe | **Exception** phenkta hai | Chalta rehta hai |
| Kis pe chalta hai | Asli collection | **Snapshot / weakly consistent** |
| Naye changes dikhte hain | — | Zaroori nahi |

👉 Fail-safe ka matlab "hamesha latest data" nahi — matlab "crash nahi karega". Ye farak samajhna zaroori hai.

## Q9. `BlockingQueue` kya hai?
**Answer:** Producer-Consumer pattern ka ready-made solution. Queue khaali ho to consumer **wait** karta hai, bhari ho to producer **wait** karta hai.

```java
BlockingQueue<Task> queue = new LinkedBlockingQueue<>(100);

// Producer
queue.put(task);        // bhari hai to wait karo

// Consumer
Task t = queue.take();  // khaali hai to wait karo
```

**Main implementations:**

| Class | Khaasiyat |
|---|---|
| `ArrayBlockingQueue` | Fixed size, array based |
| `LinkedBlockingQueue` | Optionally bounded, zyada throughput |
| `PriorityBlockingQueue` | Priority order me nikalta hai |
| `SynchronousQueue` | Size **zero** — direct handoff |
| `DelayQueue` | Element tabhi nikalta hai jab delay poora ho |

👉 `ExecutorService` andar se yahi use karta hai tasks hold karne ke liye (topic 31).

⚠️ **`put()`/`take()` vs `offer()`/`poll()`:** pehle wale **block** karte hain, doosre wale turant return kar dete hain. Galat choose karoge to ya to deadlock ya busy-waiting.

## Q10. Atomic classes kya hain?
**Answer:** Lock ke bina thread-safe counters.

```java
// ❌ GALAT — ++ atomic nahi hai (read-modify-write, 3 steps)
int count = 0;
count++;                        // race condition

// ✅ SAHI
AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();
count.addAndGet(5);
count.compareAndSet(10, 20);    // 10 ho to 20 kar do
```

**Andar CAS (Compare-And-Swap)** hai — CPU ka hardware instruction. Lock nahi lagta, isliye `synchronized` se tez hai.

👉 **Bahut zyada contention** ho to **`LongAdder`** use karo — wo multiple cells me count rakhta hai aur end me jodta hai. `AtomicLong` se kaafi tez hota hai.

## Q11. Kaunsa collection kab use karein?
**Answer:**

| Zaroorat | Use karo |
|---|---|
| Thread-safe map | `ConcurrentHashMap` |
| Sorted thread-safe map | `ConcurrentSkipListMap` |
| Reads bahut zyada, writes kam | `CopyOnWriteArrayList` |
| Producer-Consumer | `BlockingQueue` |
| Thread-safe counter | `AtomicInteger` / `LongAdder` |
| Thread-safe set | `ConcurrentHashMap.newKeySet()` |
| Single thread | Normal `HashMap` / `ArrayList` ✅ |

⚠️ **Sabse important:** agar single thread hai to **normal collections hi use karo**. Concurrent collections me overhead hota hai — bina zaroorat use karna performance kharab karta hai.

---

> 💡 **Interview tip:** "`HashMap` thread-safe kaise banayein?" ka jawab **kabhi `Hashtable` mat bolna** — wo legacy hai. Bolo: *"`ConcurrentHashMap` use karunga, kyunki wo poori map pe lock nahi lagata, sirf bucket level pe `synchronized` aur CAS use karta hai — isliye kai threads parallel me kaam kar sakte hain."* Ye ek line pura difference dikha deti hai.
