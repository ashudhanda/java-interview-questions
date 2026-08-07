# 11 — Generics 🧬

## Q1. What are generics and why do we use them?
**Answer:** Generics let you write classes/methods that work with any type while keeping **compile-time type safety**. They remove the need for casting and prevent `ClassCastException` at runtime.

```java
List<String> names = new ArrayList<>();
names.add("Ashu");
String s = names.get(0); // no cast needed
```

## Q2. What is type erasure?
**Answer:** Generic type info exists only at compile time. At runtime the JVM sees raw types (`List<String>` becomes `List`). That's why you can't do `new T()` or `instanceof List<String>`.

## Q3. Difference between `List<Object>` and `List<?>`?
**Answer:** `List<Object>` accepts only a list declared as Object. `List<?>` (unknown type) accepts a list of *any* type, but you can't add elements to it (except `null`).

## Q4. What are bounded type parameters?
**Answer:** Restricting the allowed types:
```java
<T extends Number> double sum(List<T> list) { ... } // sirf Number ya subclasses
```

## Q5. Explain PECS (Producer Extends, Consumer Super).
**Answer:**
- Data **read** karna hai (producer) → `? extends T`
- Data **add** karna hai (consumer) → `? super T`

```java
void copy(List<? extends T> src, List<? super T> dest)
```

## Q6. Can we use primitives with generics?
**Answer:** No — `List<int>` invalid hai. Wrapper classes use karo (`List<Integer>`); autoboxing handle kar leta hai.

## Q7. What is a generic method?
**Answer:** A method with its own type parameter, independent of the class:
```java
public static <T> void printArray(T[] arr) {
    for (T item : arr) System.out.println(item);
}
```

## Q8. What is the diamond operator `<>`?
**Answer:** Java 7+ me compiler khud type infer kar leta hai, so right side pe type repeat karne ki zaroorat nahi:
```java
Map<String, List<Integer>> map = new HashMap<>(); // pehle new HashMap<String, List<Integer>>() likhna padta tha
```
