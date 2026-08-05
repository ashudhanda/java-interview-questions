# 16 — Quick Revision Cheatsheet ⚡

Interview se pehle 10-minute revision ke liye one-liners.

## Core Java
- **JDK** builds, **JRE** runs, **JVM** executes bytecode
- `==` compares references, `.equals()` compares content
- String is **immutable**; use StringBuilder in loops
- Java is always **pass by value** (references bhi copy hoti hain)
- `final` var = constant, `final` method = no override, `final` class = no inheritance

## OOP
- 4 pillars: Encapsulation, Inheritance, Polymorphism, Abstraction
- Overloading = compile-time, Overriding = runtime polymorphism
- Constructor ka naam class jaisa, no return type
- Interface = 100% contract; abstract class = partial implementation + state

## Collections
- `ArrayList` — fast random access; `LinkedList` — fast insert/delete
- `HashMap` — unordered; `LinkedHashMap` — insertion order; `TreeMap` — sorted
- `HashSet` duplicates ignore karta hai; `Set.add()` returns false on duplicate
- `HashMap` allows 1 null key; `Hashtable`/`ConcurrentHashMap` allow none

## HashMap Internals
- Default capacity 16, **load factor 0.75** → resize = double + rehash
- Collision → same bucket chain; Java 8+ me 8+ entries pe **red-black tree**
- Keys **immutable** rakho — mutable key = entry "lost"
- `ConcurrentHashMap` = bucket-level lock, Hashtable = poora lock

## equals & hashCode
- Equal objects ⇒ same hashCode (must); same hashCode ⇏ equal objects
- `equals()` me jo fields, wahi `hashCode()` me
- `Objects.equals(a, b)` = null-safe comparison

## final & static
- `final` reference ≠ immutable object — `list.add()` allowed, reassignment nahi
- `static` = class ki copy; instance = har object ki apni copy
- Static method **override nahi, hide** hota hai — reference type decide karta hai
- Order: static block (1 baar) → instance block → constructor

## Exceptions
- Checked = compile time (IOException); Unchecked = runtime (NullPointerException)
- `throw` = actually throwing; `throws` = declaration in signature
- `finally` almost always runs (except `System.exit()`)
- try-with-resources auto-closes `AutoCloseable`

## Multithreading
- Thread banane ke 2 tarike: `extends Thread` ya `implements Runnable` (Runnable better)
- `start()` naya thread; `run()` direct call = same thread (trick question!)
- `synchronized` = ek time pe ek thread; `volatile` = visibility guarantee

## Java 8+
- Lambda: `(a, b) -> a + b`
- Stream: `list.stream().filter(...).map(...).collect(...)`
- `Optional` — null-safe wrapper; `orElse()` for defaults
- Method reference: `System.out::println`
- Functional interfaces: `Predicate` (test), `Function` (apply), `Consumer` (accept), `Supplier` (get)

## Streams One-Liners
- Counting: `groupingBy(w -> w, counting())`
- Duplicates hatao: `.distinct()` · Flatten: `.flatMap(List::stream)`
- `partitioningBy` = sirf 2 buckets; `groupingBy` = kayi
- `findFirst()` vs `findAny()` — parallel me findAny faster ho sakta hai

## Memory & GC
- Stack = local vars + frames; Heap = objects
- `StackOverflowError` = deep recursion; `OutOfMemoryError` = heap full
- GC unreachable objects free karta hai; `System.gc()` sirf request hai
- Young Gen (Eden + Survivors) → survive kiya to Old Gen promote
- `finalize()` deprecated — kabhi rely mat karo

## Java 17 & 21
- `record Point(int x, int y)` — immutable data class ek line me
- Sealed class: `permits` se subclass control
- Virtual threads (21): JVM-managed, I/O ke liye millions banao
- Switch expression value return karta hai — `->` with no fall-through
