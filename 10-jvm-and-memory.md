# 10 — JVM & Memory Management 🧠

## Q1. What is the JVM and how is it different from JRE and JDK?
**Answer:** JVM (Java Virtual Machine) executes bytecode. JRE = JVM + core libraries (to *run* programs). JDK = JRE + compiler & dev tools (to *build* programs).

## Q2. What are the main memory areas of the JVM?
**Answer:**
- **Heap** — objects live here (shared across threads)
- **Stack** — per-thread; method frames + local variables
- **Metaspace** — class metadata (Java 8+, replaced PermGen)
- **PC Register** — current instruction per thread
- **Native Method Stack** — for native (JNI) calls

## Q3. Stack vs Heap?
**Answer:** Stack stores local variables and references (fast, auto-freed when method returns). Heap stores actual objects (managed by Garbage Collector).

## Q4. What is Garbage Collection?
**Answer:** Automatic process that frees heap memory used by objects that are no longer reachable from any live reference.

## Q5. Can you force garbage collection?
**Answer:** No. `System.gc()` is only a *request* — JVM may ignore it.

## Q6. What causes `OutOfMemoryError` vs `StackOverflowError`?
**Answer:** `OutOfMemoryError` → heap full (too many live objects / memory leak). `StackOverflowError` → stack full (usually infinite/deep recursion).

## Q7. What is a memory leak in Java if GC exists?
**Answer:** When objects are no longer needed but still *reachable* (e.g. stored in a static collection), GC can't reclaim them — memory keeps growing.

## Q8. What is a ClassLoader?
**Answer:** JVM ka wo part jo `.class` files ko runtime pe load karta hai. Built-in loaders: Bootstrap → Extension → Application classloader. Custom ClassLoader bhi bana sakte ho (plugins, hot-reload ke liye).
