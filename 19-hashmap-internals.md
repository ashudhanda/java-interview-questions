# 19 — HashMap Internals 🔍

"HashMap internally kaise kaam karta hai?" — ye THE most asked Java interview question hai. Isko solid kar lo.

## Q1. How does HashMap work internally?
**Answer:**
1. HashMap = **array of buckets** (default size 16).
2. `put(key, value)` → key ka `hashCode()` → hash function → bucket index.
3. Bucket pe entries **linked list** (Java 8+ me 8+ entries hone pe **red-black tree**) me store hoti hain.
4. `get(key)` → same index → list/tree me `equals()` se search.

```
index = hash(key) & (capacity - 1)
```

## Q2. What happens when two keys get the same bucket (collision)?
**Answer:** Collision → entries chain hoti hain same bucket me. Java 7 tak linked list; Java 8+ me **TREEIFY_THRESHOLD = 8** cross hone pe bucket red-black tree ban jata hai (search O(n) → O(log n)).

## Q3. Why must you override `equals()` AND `hashCode()` together?
**Answer:**
- `hashCode()` decides **kaunsa bucket**.
- `equals()` decides **bucket ke andar kaunsi entry**.

Sirf `equals()` override kiya → equal objects different buckets me → `get()` null dega. Sirf `hashCode()` → saare objects ek bucket me → O(n) performance. **Contract:** equal objects ka same hashCode hona chahiye.

## Q4. Why are String and Integer good HashMap keys?
**Answer:** Kyunki wo **immutable** hain. Key ka hashCode put ke time pe fix ho jata hai — agar key mutable hui aur uska hashCode badla, entry "lost" ho jayegi (get fail). Isliye keys immutable rakhna best practice hai.

## Q5. What is load factor and when does HashMap resize?
**Answer:**
- **Load factor 0.75** → capacity 16 hai to 12 entries pe **resize**.
- Resize = capacity double (32) + saari entries **rehash**.
- Resize expensive hai — initial size estimate ho to constructor me de do: `new HashMap<>(1000)`.

## Q6. HashMap vs Hashtable vs ConcurrentHashMap?
**Answer:**
| Feature | HashMap | Hashtable | ConcurrentHashMap |
|---------|---------|-----------|-------------------|
| Thread-safe | ❌ | ✅ (poora lock) | ✅ (bucket-level) |
| Null key | 1 allowed | ❌ | ❌ |
| Null values | ✅ | ❌ | ❌ |
| Speed | Fastest | Slow | Fast under concurrency |

## Q7. How does ConcurrentHashMap achieve thread safety?
**Answer:** Poore map pe lock nahi! Java 8+ me **bucket-level CAS + synchronized** — sirf wahi bucket lock hoti hai jisme write ho raha hai. Reads lock-free. Isliye Hashtable se kahin zyada scalable.

## Q8. What is the fail-fast behavior of HashMap?
**Answer:** Iteration ke dauran structurally modify kiya (put/remove) → `ConcurrentModificationException`. Iterator internal `modCount` check karta hai. Safe removal ke liye `iterator.remove()` use karo.