# 21 — Lambda & Functional Interfaces λ

Java 8 ka sabse bada feature — lambdas. Ye clear hai to Streams bhi aasaan.

## Q1. What is a functional interface?
**Answer:** Interface with **exactly one abstract method**. `@FunctionalInterface` annotation lagao to compiler enforce karta hai.

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b); // sirf ek abstract method
}
```
Default/static methods allowed hain — count sirf abstract ka hota hai.

## Q2. What is a lambda expression?
**Answer:** Anonymous function — functional interface ka short implementation.

```java
// Old: anonymous class
Calculator c = new Calculator() {
    public int add(int a, int b) { return a + b; }
};
// New: lambda
Calculator c = (a, b) -> a + b;
```

## Q3. Which built-in functional interfaces does Java 8 provide?
**Answer:** (`java.util.function` package)

| Interface | Method | Use |
|-----------|--------|-----|
| `Predicate<T>` | `test(T)` → boolean | filter karna |
| `Function<T,R>` | `apply(T)` → R | transform |
| `Consumer<T>` | `accept(T)` → void | kuch karo, return nahi |
| `Supplier<T>` | `get()` → T | value do, input nahi |

```java
Predicate<String> isEmpty = s -> s.isEmpty();
Function<String, Integer> len = String::length;
Consumer<String> print = System.out::println;
Supplier<Double> random = Math::random;
```

## Q4. What is a method reference?
**Answer:** Lambda ka aur shortcut jab existing method directly kaam aa jaye.

4 types:
1. Static → `Math::max`
2. Instance (specific object) → `System.out::println`
3. Instance (arbitrary object) → `String::toUpperCase`
4. Constructor → `ArrayList::new`

## Q5. Can lambdas use local variables?
**Answer:** Haan, lekin variable **final ya effectively final** hona chahiye (assign hone ke baad change nahi hota). Reason: lambda apni enclosing scope se variables **capture** karta hai — changeable variable capture karne se thread-safety issues aate.

```java
int factor = 2; // effectively final
nums.forEach(n -> System.out.println(n * factor));
// factor = 3; // ❌ uncomment kiya to compile error
```

## Q6. Lambda vs anonymous class — kya fark?
**Answer:**
- Lambda: sirf functional interfaces ke liye; `this` = enclosing class
- Anonymous class: kisi bhi interface/abstract class ke liye; `this` = khud ki instance
- Lambda bytecode me `invokedynamic` use karta hai (lighter); anonymous class ki alag `.class` file banti hai

## Q7. What is a default method in an interface?
**Answer:** Method with body inside an interface — purani interfaces me naye methods add karne ke liye bina implementations todo (Java 8 ne isliye introduce kiya taaki `Collection` me `stream()` add ho sake).

```java
interface Vehicle {
    default void start() { System.out.println("Starting..."); }
}
```
⭐ **Diamond problem:** do interfaces me same default method → class ko override karna **compulsory**.