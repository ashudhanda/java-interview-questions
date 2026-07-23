# 🟡 Collections Framework — 15 Questions

### Q1. ArrayList vs LinkedList?
**Answer:**
| | ArrayList | LinkedList |
|--|-----------|------------|
| Backed by | Dynamic array | Doubly linked list |
| get(i) | **O(1)** | O(n) |
| add/remove middle | O(n) (shifting) | O(1) once at node |
| Memory | Compact | Extra node pointers |

> Default choice: **ArrayList** (cache-friendly, faster in practice).

---

### Q2. How does HashMap work internally?
**Answer:**
1. `put(key, value)` → compute `hashCode()` of key → index = hash & (capacity-1).
2. Store entry in that **bucket**; collisions form a linked list.
3. Java 8+: a bucket's list converts to a **red-black tree** when it exceeds 8 entries (O(n) → O(log n)).
4. When size > capacity × load factor (0.75), the table **resizes** (doubles).

---

### Q3. HashMap vs Hashtable?
**Answer:** HashMap: not synchronized, allows one `null` key and `null` values, faster. Hashtable: legacy, synchronized, no nulls. For thread-safety today use **ConcurrentHashMap**, not Hashtable.

---

### Q4. How does HashSet ensure uniqueness?
**Answer:** Internally it's a **HashMap** where elements are keys (value is a dummy object). Uniqueness comes from `hashCode()` + `equals()` — duplicates map to the same bucket and are rejected by `equals`.

---

### Q5. What is the equals() and hashCode() contract?
**Answer:**
1. If `a.equals(b)` → `a.hashCode() == b.hashCode()` **must** hold.
2. Same hashCode does NOT mean equal (collisions allowed).
3. Override **both together** — else HashMap/HashSet break (you can `put` an object and never find it again).

---

### Q6. Comparable vs Comparator?
**Answer:**
- **Comparable** — natural order, class implements `compareTo()` itself. One order only.
- **Comparator** — external, multiple custom orders possible.

```java
list.sort(Comparator.comparing(Student::getMarks).reversed());
```

---

### Q7. Fail-fast vs fail-safe iterators?
**Answer:**
- **Fail-fast** (ArrayList, HashMap): throw `ConcurrentModificationException` if collection is modified during iteration.
- **Fail-safe** (ConcurrentHashMap, CopyOnWriteArrayList): iterate over a snapshot/live view without throwing — but may not reflect latest changes.

---

### Q8. How does ConcurrentHashMap achieve thread-safety?
**Answer:** Java 8+: lock-free **CAS** operations for inserts into empty buckets and fine-grained `synchronized` **per bucket head** for updates — so multiple threads can write to different buckets in parallel. Reads are non-blocking. Much faster than locking the whole map.

---

### Q9. What is a TreeMap? When to use it?
**Answer:** A sorted map backed by a **red-black tree** — keys kept in sorted order, O(log n) operations. Use for range queries (`headMap`, `tailMap`, `firstKey`) or when you need sorted iteration.

---

### Q10. Iterator vs ListIterator?
**Answer:** `Iterator` — forward only, any collection, can `remove()`. `ListIterator` — lists only, **both directions**, can `add()`, `set()`, and get indices.

---

### Q11. ArrayList vs Vector?
**Answer:** Vector is legacy and synchronized (slow); grows 100% on resize vs ArrayList's 50%. Prefer ArrayList, and `Collections.synchronizedList()` or `CopyOnWriteArrayList` if thread-safety is needed.

---

### Q12. What are load factor and initial capacity in HashMap?
**Answer:** Initial capacity = number of buckets (default 16). Load factor (default 0.75) = how full the map gets before **resizing**. 16 × 0.75 = 12 entries triggers a resize to 32 buckets. Higher load factor saves memory but increases collisions.

---

### Q13. What is LinkedHashMap? Give a real use case.
**Answer:** HashMap + a doubly-linked list preserving **insertion order** (or access order). Classic use case: **LRU cache** —

```java
new LinkedHashMap<K, V>(16, 0.75f, true) {
    protected boolean removeEldestEntry(Map.Entry<K, V> e) {
        return size() > MAX_ENTRIES;
    }
};
```

---

### Q14. What is a PriorityQueue?
**Answer:** A queue where elements come out by **priority** (natural order or Comparator), not FIFO. Backed by a binary **heap** — peek O(1), add/poll O(log n). Used for Dijkstra, top-K problems, schedulers.

---

### Q15. How do you sort a list of objects by multiple fields?
**Answer:**
```java
list.sort(
    Comparator.comparing(Employee::getDept)
              .thenComparing(Employee::getSalary, Comparator.reverseOrder())
              .thenComparing(Employee::getName)
);
```

---

⬅️ [Prev: Strings & Arrays](03-strings-and-arrays.md) | ➡️ [Next: Exception Handling](05-exception-handling.md)
