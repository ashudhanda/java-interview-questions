# 🔴 Java 8+ Features — 13 Questions

### Q1. What is a lambda expression?
**Answer:** A concise anonymous function implementing a functional interface:

```java
// Before
Runnable r = new Runnable() { public void run() { System.out.println("hi"); } };
// After
Runnable r2 = () -> System.out.println("hi");
```

---

### Q2. What is a functional interface?
**Answer:** An interface with **exactly one abstract method** (SAM). Marked with `@FunctionalInterface` (optional but recommended). Built-ins: `Function<T,R>`, `Predicate<T>`, `Consumer<T>`, `Supplier<T>`, `BiFunction<T,U,R>`.

---

### Q3. What is the Stream API?
**Answer:** Declarative, functional-style processing of collections:

```java
List<String> names = employees.stream()
    .filter(e -> e.getSalary() > 50000)
    .map(Employee::getName)
    .sorted()
    .collect(Collectors.toList());
```
> Streams don't store data and don't modify the source; they're consumed once.

---

### Q4. map() vs flatMap()?
**Answer:** `map` transforms each element 1→1. `flatMap` transforms each element into a **stream** and flattens all of them into one:

```java
List<List<Integer>> nested = List.of(List.of(1,2), List.of(3,4));
List<Integer> flat = nested.stream()
    .flatMap(List::stream)      // [1, 2, 3, 4]
    .toList();
```

---

### Q5. Intermediate vs terminal operations?
**Answer:** **Intermediate** (filter, map, sorted, distinct) return a stream and are **lazy**. **Terminal** (collect, forEach, count, reduce, findFirst) trigger execution and produce a result. Nothing runs until a terminal op is called.

---

### Q6. What is Optional? Why use it?
**Answer:** A container that may or may not hold a value — an explicit alternative to returning `null`:

```java
Optional<User> user = findById(42);
String name = user.map(User::getName).orElse("Guest");
```
> Avoid `optional.get()` without checking — that just recreates the NPE problem.

---

### Q7. What are method references?
**Answer:** Shorthand for lambdas that just call an existing method. Four kinds: `ClassName::staticMethod`, `object::instanceMethod`, `ClassName::instanceMethod`, `ClassName::new` (constructor).

---

### Q8. What are default and static methods in interfaces?
**Answer:** Java 8 lets interfaces carry implementation: `default` methods (inheritable, overridable) and `static` methods (called on the interface). This is how `List.sort()` was added without breaking every existing implementation.

---

### Q9. What does Collectors.groupingBy do?
**Answer:** SQL GROUP BY for streams:

```java
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept));

Map<String, Long> countByDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));
```

---

### Q10. When should you use parallel streams?
**Answer:** Only for **large datasets** with **CPU-heavy, independent** operations and no shared mutable state. For small lists or I/O-bound work, parallel overhead makes it *slower*. Always measure before using `.parallelStream()`.

---

### Q11. What are records (Java 16+)?
**Answer:** Immutable data classes in one line — auto-generates constructor, getters, `equals`, `hashCode`, `toString`:

```java
record Point(int x, int y) { }
Point p = new Point(1, 2);
p.x(); // 1
```

---

### Q12. What are sealed classes (Java 17+)?
**Answer:** Classes that **restrict which classes may extend them** — the compiler knows all subtypes, enabling exhaustive switch pattern matching:

```java
sealed interface Shape permits Circle, Square, Triangle { }
```

---

### Q13. What are switch expressions and text blocks?
**Answer:**
```java
// Switch expression (Java 14+): returns a value, no fall-through
String type = switch (day) {
    case SATURDAY, SUNDAY -> "Weekend";
    default -> "Weekday";
};

// Text block (Java 15+): multi-line strings
String json = """
    { "name": "Ashu", "role": "dev" }
    """;
```

---

⬅️ [Prev: Multithreading](06-multithreading.md) | ➡️ [Next: Advanced Coding Problems](08-advanced-coding-problems.md)
