# 🟢 OOPs Concepts — 15 Questions

### Q1. What are the 4 pillars of OOP?
**Answer:**
1. **Encapsulation** — bundling data + methods, hiding internals (`private` fields + getters/setters).
2. **Inheritance** — child class reuses parent's code (`extends`).
3. **Polymorphism** — one interface, many forms (overloading + overriding).
4. **Abstraction** — exposing *what* something does, hiding *how* (abstract classes, interfaces).

---

### Q2. What is the difference between a class and an object?
**Answer:** A **class** is a blueprint/template; an **object** is a real instance of that blueprint in memory.

```java
class Car { String color; }        // blueprint
Car myCar = new Car();             // object
```

---

### Q3. What types of inheritance does Java support?
**Answer:** Single, multilevel, and hierarchical inheritance with classes. **Multiple inheritance of classes is NOT supported** (diamond problem) — but a class can implement multiple **interfaces**.

---

### Q4. Why doesn't Java support multiple inheritance of classes?
**Answer:** The **diamond problem** — if two parents have the same method, the compiler can't decide which one the child inherits. Interfaces avoid this because (before default methods) they carry no implementation, and with default methods Java **forces you to override** the conflicting method.

---

### Q5. Method overloading vs method overriding?
**Answer:**
| | Overloading | Overriding |
|--|------------|------------|
| Where | Same class | Parent-child |
| Signature | Different params | Same signature |
| Binding | Compile-time | Runtime |
| Return type | Can differ | Same or covariant |

---

### Q6. What is runtime polymorphism?
**Answer:** Method overriding resolved at runtime based on the **actual object type**, not the reference type.

```java
Animal a = new Dog();
a.sound(); // "Woof" — Dog's version runs, decided at runtime
```

---

### Q7. Abstract class vs interface?
**Answer:**
| | Abstract class | Interface |
|--|---------------|-----------|
| Methods | Abstract + concrete | Abstract + default/static (Java 8+) |
| Fields | Any | `public static final` only |
| Constructor | ✅ | ❌ |
| Multiple inheritance | ❌ | ✅ (implement many) |
| Use when | Shared code + state | Contract/capability |

---

### Q8. What is encapsulation? Give an example.
**Answer:** Hiding fields behind `private` and exposing controlled access via methods.

```java
class Account {
    private double balance;
    public void deposit(double amt) {
        if (amt > 0) balance += amt; // validation possible!
    }
    public double getBalance() { return balance; }
}
```

---

### Q9. What is a constructor? How is it different from a method?
**Answer:** A constructor initializes a new object. It has the **same name as the class**, **no return type**, and is called automatically by `new`. If you define none, Java adds a no-arg **default constructor**.

---

### Q10. What is constructor overloading and constructor chaining?
**Answer:** Multiple constructors with different parameters = overloading. Calling one constructor from another = chaining: `this(...)` for same class, `super(...)` for parent (must be the first statement).

```java
class Pizza {
    Pizza() { this("Medium"); }        // chains to below
    Pizza(String size) { /* ... */ }
}
```

---

### Q11. Difference between `this` and `super`?
**Answer:** `this` refers to the **current object** (fields, methods, constructor of the same class). `super` refers to the **parent class** (parent's fields, methods, constructor).

---

### Q12. Can we override static methods?
**Answer:** **No.** Static methods belong to the class, not objects. Declaring the same static method in a child is **method hiding**, not overriding — the version called depends on the *reference type*, not the object.

---

### Q13. What is a `final` class? Name a real example.
**Answer:** A class that **cannot be extended**. Example: `java.lang.String` — final so its immutability and security guarantees can't be broken by subclassing.

---

### Q14. Composition vs inheritance — which to prefer?
**Answer:** **Favor composition** ("has-a") over inheritance ("is-a"). Composition is more flexible, avoids fragile base-class problems, and lets you change behavior at runtime.

```java
class Engine { }
class Car { private Engine engine; } // Car HAS-A Engine
```

---

### Q15. What is the `instanceof` operator?
**Answer:** Checks if an object is an instance of a class/interface. Java 16+ adds **pattern matching**:

```java
if (obj instanceof String s) {
    System.out.println(s.length()); // no explicit cast needed
}
```

---

⬅️ [Prev: Java Basics](01-java-basics.md) | ➡️ [Next: Strings & Arrays](03-strings-and-arrays.md)
