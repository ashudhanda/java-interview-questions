# 17 — Java 17 & 21 Features 🚀

## Q1. What is a `record`?
**Answer:** A compact class for immutable data carriers — constructor, getters, `equals`, `hashCode`, `toString` sab auto-generate hote hain.

```java
record Point(int x, int y) {}

Point p = new Point(3, 4);
System.out.println(p.x()); // 3
```
Fields are `private final`; a record can't extend any class (but can implement interfaces).

## Q2. What are sealed classes?
**Answer:** Control kaun extend kar sakta hai — `sealed` + `permits`.

```java
sealed class Shape permits Circle, Square {}
final class Circle extends Shape {}
final class Square extends Shape {}
```
Har subclass must be `final`, `sealed`, or `non-sealed`.

## Q3. What is pattern matching for `instanceof`?
**Answer:** Check + cast + variable binding — ek hi line me.

```java
// Old way
if (o instanceof String) {
    String s = (String) o;
}
// New way
if (o instanceof String s) {
    System.out.println(s.length());
}
```

## Q4. What are virtual threads (Java 21)?
**Answer:** Lightweight threads managed by the **JVM**, not the OS — millions bana sakte ho. Perfect for I/O-heavy apps (API calls, DB queries).

```java
Thread.startVirtualThread(() -> System.out.println("Hello"));
```
Platform thread = expensive resource; virtual thread = almost free.

## Q5. What are switch expressions?
**Answer:** `switch` ab value return kar sakta hai with `->` — no fall-through, no `break`.

```java
String day = switch (n) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    default -> "Other";
};
```

## Q6. What are text blocks?
**Answer:** Multi-line strings with `"""` — escaping ki tension khatam.

```java
String json = """
    { "name": "Ashu", "lang": "Java" }
    """;
```

## Q7. Record vs normal class — kab kaunsa?
**Answer:** Jab class sirf **data carry** kar rahi ho (DTO, API response, coordinates) → `record` best hai, boilerplate khatam. Jab **mutable state** ya complex behavior chahiye → normal class. Records se code chhota aur bugs kam.
