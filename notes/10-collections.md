# Chapter 10 — Collections 🎒

> **Goal:** Meet ArrayList and HashMap — the two data structures you'll use in 90% of real code — without fear.

---

## 1. Why not just arrays?

Arrays (Chapter 4) have one painful rule: **fixed size**. Real life isn't fixed — students join mid-semester, items get added to carts.

**Collections = resizable, feature-packed containers.** The famous ones:

| Container | What it is | Analogy |
|-----------|-----------|---------|
| `ArrayList` | growable list (keeps order, allows duplicates) | 🚂 train that can ADD coaches |
| `HashMap` | key → value pairs | 🏪 locker room: locker NUMBER → stuff inside |
| `HashSet` | unique items only, no order promise | 🎟️ entry stamp — you can't enter twice |

## 2. ArrayList — the growable train 🚂

```java
import java.util.ArrayList;

ArrayList<String> names = new ArrayList<>();
names.add("Ashu");            // [Ashu]
names.add("Riya");            // [Ashu, Riya]
names.add("Ashu");            // [Ashu, Riya, Ashu]  — duplicates OK

names.get(0)                  // "Ashu"   (like arr[0])
names.size()                  // 3        (not length, not length()! 😅)
names.remove("Riya");         // [Ashu, Ashu]
names.contains("Riya")        // false

for (String n : names) {      // for-each works!
    System.out.println(n);
}
```

> 💡 `<String>` = **generics** ([Q&A #11](../11-generics.md)) — the label on the container saying what type it holds. Compiler stops you from adding an `int` into a `<String>` list.

**Primitives?** Lists hold OBJECTS only, so Java auto-converts: `list.add(5)` secretly becomes `list.add(Integer.valueOf(5))` — called **autoboxing** 📦.

## 3. HashMap — the locker room 🏪

```java
import java.util.HashMap;

HashMap<String, Integer> marks = new HashMap<>();
marks.put("Ashu", 92);            // locker "Ashu" → 92
marks.put("Riya", 88);
marks.put("Ashu", 95);            // same key → OVERWRITES (no duplicates in keys!)

marks.get("Ashu")                 // 95
marks.get("Sam")                  // null  (no such locker — careful!)
marks.getOrDefault("Sam", 0)      // 0     (safer 👌)
marks.containsKey("Riya")         // true
marks.size()                      // 2

// Loop over lockers:
for (String key : marks.keySet()) {
    System.out.println(key + " → " + marks.get(key));
}
```

Lookup by key is nearly **instant** — no scanning the whole list. How? Hashing magic — full story in [HashMap Internals](../19-hashmap-internals.md).

## 4. HashSet — the "no duplicates" bouncer 🚪

```java
import java.util.HashSet;

HashSet<Integer> seen = new HashSet<>();
seen.add(5);      // true  (added)
seen.add(5);      // false (already inside — rejected!)
seen.size();      // 1
```

Perfect for: "count UNIQUE visitors", "have I seen this before?" questions.

## 5. Choosing cheat-line 🧭

- Ordered list, index access, duplicates fine → **ArrayList**
- Look things up by a name/id → **HashMap**
- Only uniqueness matters → **HashSet**

(Later friends: `LinkedList`, `TreeMap` (sorted keys), `LinkedHashMap` (remembers insert order) — same ideas, different superpowers.)

## 6. ❌ Common Beginner Mistakes

1. `names.get(names.size())` → IndexOutOfBoundsException — last index is `size()-1` (arrays flashback!)
2. `ArrayList<int>` ❌ — generics need wrapper classes: `ArrayList<Integer>`
3. Using `==` to compare list elements — they're objects! `.equals()` (broken record now 😅)
4. Removing items INSIDE a for-each loop → ConcurrentModificationException — use an Iterator or removeIf
5. Forgetting `import java.util.*;` → "cannot find symbol ArrayList"

---

## 🏋️ Practice

**Q1. Count the frequency of each word.**

<details><summary>✅ Solution + dry run</summary>

```java
String[] words = {"java", "is", "fun", "java", "is", "java"};
HashMap<String, Integer> freq = new HashMap<>();
for (String w : words) {
    freq.put(w, freq.getOrDefault(w, 0) + 1);
}
System.out.println(freq);   // {java=3, is=2, fun=1}
```
**Dry run:** java→1 → is→1 → fun→1 → java→2 → is→2 → java→3. The `getOrDefault`+1 pattern = THE most reused interview trick.
</details>

**Q2. Remove duplicates from a list, keep order.**

<details><summary>✅ Solution</summary>

```java
ArrayList<Integer> nums = new ArrayList<>(List.of(1, 3, 1, 2, 3, 5));
LinkedHashSet<Integer> unique = new LinkedHashSet<>(nums);
System.out.println(new ArrayList<>(unique));   // [1, 3, 2, 5]
```
**Why LinkedHashSet?** HashSet kills duplicates but may shuffle order; the Linked version remembers insertion order. One-liner power 💪
</details>

**Q3. Predict the output:**
```java
ArrayList<Integer> list = new ArrayList<>();
list.add(10); list.add(20); list.add(30);
list.remove(1);
System.out.println(list);
```

<details><summary>✅ Answer</summary>

`[10, 30]` — `remove(1)` with an **int** means INDEX 1 (removes 20), not the value 1! To remove the VALUE, use `list.remove(Integer.valueOf(1))`. Autoboxing does NOT happen for remove — nastiest trap in this chapter 😈
</details>

**Q4. Students and marks: store & find the topper.**

<details><summary>✅ Solution</summary>

```java
HashMap<String, Integer> marks = new HashMap<>();
marks.put("Ashu", 92); marks.put("Riya", 88); marks.put("Sam", 95);

String topper = null;
int best = -1;
for (String name : marks.keySet()) {
    if (marks.get(name) > best) {
        best = marks.get(name);
        topper = name;
    }
}
System.out.println(topper + " with " + best);   // Sam with 95
```
Same "tallest in line" pattern, third appearance — arrays, three numbers, now maps. See how patterns > memorising? 🎯
</details>

---

🎉 **Part A complete!** You now know enough Java to solve real problems. Next stop: the [Q&A Bank](../README.md#-part-b--coding-questions-bank) for interview prep, starting with [Java Basics Q&A](../01-java-basics.md).

⬅️ [Prev: OOPs Made Simple](09-oops-made-simple.md) | 🏠 [Index](../README.md)
