# 28 — Interfaces vs Abstract Classes 🤝

> The #1 most-asked OOPs comparison in interviews. Read [Chapter 9](notes/09-oops-made-simple.md) first if abstraction is new to you.

---

## Q1. What is an interface?

A **100% contract**: WHAT must be done, zero opinion on HOW.

```java
interface Payable {
    double calculatePay();               // no body — just the promise
}

class Employee implements Payable {
    public double calculatePay() {       // fulfilling the promise
        return 50000;
    }
}
```

**Analogy 🔌:** A power socket — it defines the SHAPE every plug must have. It doesn't care if the plug belongs to a phone charger or a fridge.

## Q2. What is an abstract class?

A **half-built class**: some methods finished, some left for children.

```java
abstract class Vehicle {
    void startEngine() {                     // finished — shared by all children
        System.out.println("vroom");
    }
    abstract int wheels();                   // unfinished — each child MUST answer
}

class Bike extends Vehicle {
    int wheels() { return 2; }
}
```

**Analogy 🍕:** A pizza base — the dough (common code) is ready, toppings (specifics) are up to each pizza.

## Q3. The comparison table (memorize this 📌)

| Feature | Interface | Abstract class |
|---------|-----------|----------------|
| Keyword | `implements` | `extends` |
| How many can a class have? | MANY ✅ | only ONE |
| Fields | only `public static final` constants | normal instance fields ✅ |
| Constructor | ❌ never | ✅ yes (called via child's `super()`) |
| Method bodies | only via `default`/`static` (Java 8+) | any mix of done/abstract |
| Purpose | CAN-DO ability (Comparable, Runnable) | IS-A family base (Vehicle, Shape) |

## Q4. Why does Java forbid multiple inheritance of classes but allow many interfaces?

**The diamond problem 💎:** if `class C extends A, B` and both A and B have `show()` with different bodies — which one does C inherit? Ambiguity → banned.

Interfaces (classically) carry NO state and no method bodies — nothing to clash → safe to implement many:

```java
class SmartWatch implements Wearable, Chargeable, Bluetooth { ... }   // ✅ totally fine
```

## Q5. What are `default` methods? (Java 8+)

Method WITH a body inside an interface — added so old interfaces could evolve without breaking every implementing class:

```java
interface Payable {
    double calculatePay();
    default String currency() { return "INR"; }   // free gift to all implementers
}
```

**Follow-up trap:** *"What if a class implements TWO interfaces with the SAME default method?"* → Compile error, UNLESS the class overrides it and picks: `Wearable.super.currency()`.

## Q6. When do I actually choose which? 🧭

- Unrelated classes need the same ABILITY → **interface** (`Comparable`, `Runnable`)
- Related classes share CODE and fields → **abstract class** (`Shape` with a stored `color`)
- Need both? Common combo: `abstract class AbstractList implements List` — interface for the contract, abstract class for shared plumbing
- Modern default: **start with an interface**; upgrade to abstract class only when shared state/code appears

## Q7. Predict the output 💥

```java
interface A { default void hi() { System.out.println("A"); } }
interface B { default void hi() { System.out.println("B"); } }

class C implements A, B {
    public void hi() {
        A.super.hi();
        System.out.println("C");
    }
}
new C().hi();
```

<details><summary>✅ Answer</summary>

```
A
C
```
C MUST override `hi()` (diamond clash between A and B), and `A.super.hi()` explicitly runs A's version first. Without the override, this code wouldn't even compile.
</details>

## Q8. Rapid-fire one-liners ⚡

- Can an interface extend another interface? → **Yes**, even multiple: `interface C extends A, B`
- Can an abstract class have ZERO abstract methods? → **Yes** (just means: "don't instantiate me")
- Can you do `new Payable()`? → No… unless it's an **anonymous class**: `new Payable() { public double calculatePay() { return 0; } };`
- Interface with exactly ONE abstract method = **functional interface** → lambda territory! ([Q&A #21](21-lambda-functional-interfaces.md))
- Are interface methods `public` by default? → **Yes**, abstract interface methods are implicitly `public abstract`

🏠 [Back to Index](README.md) | Related: [OOPs Made Simple](notes/09-oops-made-simple.md) · [Design Patterns](13-design-patterns.md) · [Lambdas](21-lambda-functional-interfaces.md)
