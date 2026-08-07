# 13 — Design Patterns 🏗️

## Q1. What is a design pattern?
**Answer:** A reusable, proven solution to a common software design problem. Not code to copy-paste — a template to adapt.

## Q2. Explain the Singleton pattern.
**Answer:** Ensures a class has only **one instance** with a global access point (e.g. config, logger, DB connection pool).

```java
public class Singleton {
    private static volatile Singleton instance;
    private Singleton() {}
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) instance = new Singleton();
            }
        }
        return instance;
    }
}
```
Double-checked locking + `volatile` = thread-safe.

## Q3. What is the Factory pattern?
**Answer:** A method decides which subclass object to create — caller doesn't use `new` directly.
```java
Shape s = ShapeFactory.create("circle");
```
Benefit: object creation logic ek jagah, easy to extend.

## Q4. What is the Builder pattern?
**Answer:** Step-by-step construction for objects with many optional fields — avoids telescoping constructors.
```java
User u = new User.Builder("Ashu").age(21).city("Delhi").build();
```

## Q5. What is the Observer pattern?
**Answer:** One-to-many dependency — when the subject changes, all observers get notified (e.g. event listeners, pub-sub).

## Q6. Where does Java itself use these patterns?
**Answer:**
- Singleton → `Runtime.getRuntime()`
- Factory → `Integer.valueOf()`, `Calendar.getInstance()`
- Builder → `StringBuilder`, `Stream.builder()`
- Observer → AWT/Swing event listeners

## Q7. Builder vs Factory — kab kaunsa use karein?
**Answer:** **Factory** jab *kaunsa* object banana hai wo decide karna ho (type choose hota hai). **Builder** jab ek hi object ko *step-by-step* configure karna ho (bahut saare optional fields). Simple rule: type selection → Factory, configuration → Builder.
