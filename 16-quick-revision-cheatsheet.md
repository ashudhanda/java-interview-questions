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

## Memory
- Stack = local vars + frames; Heap = objects
- `StackOverflowError` = deep recursion; `OutOfMemoryError` = heap full
- GC unreachable objects free karta hai; `System.gc()` sirf request hai
