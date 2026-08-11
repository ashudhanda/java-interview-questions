# 38 — Set, Queue & Deque Deep Dive 📦

Collections me sab `ArrayList` aur `HashMap` padh lete hain, par `TreeSet`, `PriorityQueue` aur `ArrayDeque` pe atak jaate hain. Yahi differentiator hai.

## Q1. `HashSet`, `LinkedHashSet`, `TreeSet` — difference?
**Answer:**

| | `HashSet` | `LinkedHashSet` | `TreeSet` |
|---|---|---|---|
| Order | Koi nahi | **Insertion order** | **Sorted order** |
| add/remove/contains | `O(1)` | `O(1)` | `O(log n)` |
| Andar se | `HashMap` | `LinkedHashMap` | `TreeMap` (Red-Black tree) |
| `null` allowed | ✅ ek | ✅ ek | ❌ (NPE dega) |
| Kya chahiye object me | `equals`+`hashCode` | `equals`+`hashCode` | `Comparable`/`Comparator` |

👉 Speed chahiye → `HashSet`. Order yaad rakhna hai → `LinkedHashSet`. Sorted chahiye → `TreeSet`.

## Q2. `HashSet` andar se kaise kaam karta hai?
**Answer:** Ye ek **`HashMap` ka wrapper** hai — bas itna sa.

```java
private transient HashMap<E, Object> map;
private static final Object PRESENT = new Object();   // dummy value

public boolean add(E e) {
    return map.put(e, PRESENT) == null;
}
```

Tumhara element **key** ban jaata hai, aur value me ek dummy object daal dete hain. Isliye `HashSet` ke saare rules `HashMap` wale hi hain — `equals()` aur `hashCode()` sahi hone chahiye.

## Q3. `TreeSet` me `null` kyun nahi daal sakte?
**Answer:** `TreeSet` har element ko sort karne ke liye `compareTo()` call karta hai. `null.compareTo(...)` → **NPE**.

```java
TreeSet<String> set = new TreeSet<>();
set.add(null);   // 💥 NullPointerException
```

`HashSet` me `null` chalta hai kyunki wo sirf hash nikalta hai, compare nahi karta (`null` ka hash 0 maan leta hai).

## Q4. `TreeSet` ke special methods kaunse hain?
**Answer:** Ye `NavigableSet` implement karta hai — range queries ke liye bahut kaam ka.

```java
TreeSet<Integer> set = new TreeSet<>(List.of(10, 20, 30, 40));

set.first();          // 10
set.last();           // 40
set.floor(25);        // 20  — 25 se chhota ya barabar, sabse bada
set.ceiling(25);      // 30  — 25 se bada ya barabar, sabse chhota
set.lower(20);        // 10  — strictly chhota
set.higher(20);       // 30  — strictly bada
set.headSet(30);      // [10, 20]
set.tailSet(30);      // [30, 40]
set.subSet(15, 35);   // [20, 30]
set.descendingSet();  // [40, 30, 20, 10]
```

👉 "Sabse kareeb wali value dhoondho" type problems me `floor`/`ceiling` life bacha lete hain.

## Q5. `Queue` aur `Deque` me kya farak hai?
**Answer:**
- **`Queue`** — **FIFO** (First In First Out). Ek taraf se aata hai, doosri taraf se jaata hai. Line me lagna.
- **`Deque`** (Double Ended Queue) — **dono taraf** se add/remove kar sakte ho. Isliye ise queue bhi bana sakte ho aur stack bhi.

## Q6. `Queue` ke methods me exception vs null wala confusion?
**Answer:** Har operation ke **do versions** hain — ek exception phenkta hai, doosra special value deta hai. Ye table interview me directly poocha jaata hai:

| Operation | Exception phenkta hai | Special value deta hai |
|---|---|---|
| Insert | `add(e)` | `offer(e)` → `false` |
| Remove | `remove()` | `poll()` → `null` |
| Examine | `element()` | `peek()` → `null` |

👉 **Bounded queue** (limited size) me `offer()` use karo — full hone pe `false` milega, crash nahi. Generally `offer`/`poll`/`peek` hi safer hain.

## Q7. `PriorityQueue` kya hai?
**Answer:** FIFO **nahi** — ye **priority** ke hisaab se nikalta hai. Andar se **binary heap** hai.

```java
// Min-heap (default) — sabse chhota pehle
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Max-heap — sabse bada pehle
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());

minHeap.offer(30); minHeap.offer(10); minHeap.offer(20);
minHeap.poll();   // 10 — sabse chhota
```

**Complexity:** `offer`/`poll` = `O(log n)`, `peek` = `O(1)`.

⚠️ **Bada trap:** `PriorityQueue` ko iterate karoge to elements **sorted order me nahi aayenge!** Sirf `poll()` karne pe order sahi milta hai. Andar array heap structure me hai, fully sorted nahi.

```java
System.out.println(minHeap);   // [10, 30, 20] — sorted nahi dikhega!
```

👉 "Top K elements" wale coding questions ka standard answer `PriorityQueue` hi hai.

## Q8. Stack ke liye `Stack` class use karein?
**Answer:** ❌ **Nahi.** `java.util.Stack` **legacy** hai — `Vector` extend karta hai, isliye har method `synchronized` hai (single thread me bekaar ka overhead).

✅ **`ArrayDeque` use karo:**

```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);      // = addFirst
stack.push(2);
stack.pop();        // 2 — LIFO
stack.peek();       // 1
```

Official JDK docs bhi khud yahi recommend karte hain.

## Q9. `ArrayDeque` vs `LinkedList` — queue ke liye kaunsa?
**Answer:** **`ArrayDeque` almost hamesha better hai.**

| | `ArrayDeque` | `LinkedList` |
|---|---|---|
| Andar se | Circular resizable array | Doubly linked list |
| Memory | Kam | Zyada (har node pe 2 pointers) |
| Cache locality | ✅ Achhi (contiguous) | ❌ Kharab (bikhri hui memory) |
| `null` allowed | ❌ | ✅ |
| Speed | Tez | Dheema |

👉 `LinkedList` tabhi chuno jab beech me se frequently insert/delete karna ho **aur** tumhare paas already iterator ho.

## Q10. Kaunsa collection kab use karein — quick guide
**Answer:**

| Zaroorat | Best choice |
|---|---|
| Unique items, order matter nahi | `HashSet` |
| Unique + insertion order | `LinkedHashSet` |
| Unique + sorted / range queries | `TreeSet` |
| FIFO queue | `ArrayDeque` |
| Stack (LIFO) | `ArrayDeque` |
| Priority ke hisaab se nikalna | `PriorityQueue` |
| Thread-safe queue | `ConcurrentLinkedQueue` |
| Producer-consumer (blocking) | `LinkedBlockingQueue` |

---

> 💡 **Interview me yaad rakho:** *"`Stack` aur `Vector` legacy hain — main `ArrayDeque` use karta hoon. Aur `PriorityQueue` iterate karne pe sorted nahi aata, sirf `poll()` pe order milta hai."* Ye do points hi kaafi hain depth dikhane ke liye.
