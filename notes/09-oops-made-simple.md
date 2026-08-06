# Chapter 9 — OOPs Made Simple 🏗️

> **Goal:** Classes, objects and the famous 4 pillars — explained so simply you'll wonder why textbooks make it scary.

---

## 1. Class = blueprint, Object = actual house 🏠

A **class** describes what something HAS (fields) and what it DOES (methods). An **object** is a real thing built from that description.

```java
class Student {
    // HAS (fields)
    String name;
    int marks;

    // DOES (methods)
    void introduce() {
        System.out.println("Hi, I'm " + name + " with " + marks + " marks");
    }
}

// One blueprint → many houses:
Student s1 = new Student();   // object 1 on the heap
Student s2 = new Student();   // object 2 on the heap
s1.name = "Ashu";  s1.marks = 92;
s2.name = "Riya";  s2.marks = 88;
s1.introduce();   // Hi, I'm Ashu with 92 marks
```

Each object gets its **own copy** of the fields (Chapter 3: each `new` = new box on the heap).

## 2. Constructor — the object's birth ceremony 🎂

A special method that runs **automatically** at `new`. Same name as the class, NO return type.

```java
class Student {
    String name;
    int marks;

    Student(String name, int marks) {   // constructor
        this.name = name;      // this.name = FIELD, name = PARAMETER
        this.marks = marks;
    }
}

Student s1 = new Student("Ashu", 92);   // build + fill in one shot!
```

> 💡 `this` = "THIS object's own field" — needed when parameter and field share a name. No constructor written? Java gifts a default empty one. Write ANY constructor → the gift is cancelled!

## 3. The 4 Pillars 🏛️

### Pillar 1: Encapsulation 💊 (protect your data)

Make fields `private`, control access via methods:

```java
class Account {
    private double balance;                  // nobody touches directly

    public double getBalance() { return balance; }
    public void deposit(double amt) {
        if (amt > 0) balance += amt;         // validation = the whole point!
    }
}
// account.balance = -99999;  ❌ compile error — protected!
```

**Analogy:** medicine capsule — contents sealed, consumed only the intended way.

### Pillar 2: Inheritance 👨‍👦 (reuse a class)

```java
class Animal {
    void eat() { System.out.println("eating..."); }
}
class Dog extends Animal {
    void bark() { System.out.println("woof!"); }
}

Dog d = new Dog();
d.eat();    // inherited free of cost!
d.bark();   // its own
```

`extends` = "Dog IS-A Animal, gets everything Animal has". Java allows only ONE parent class (no multiple inheritance drama — interfaces solve that, see [Q&A #28](../28-interfaces-vs-abstract-classes.md)).

### Pillar 3: Polymorphism 🎭 (same call, different behaviour)

```java
class Animal {
    void sound() { System.out.println("some sound"); }
}
class Dog extends Animal {
    @Override
    void sound() { System.out.println("woof!"); }     // overRIDE: replace parent's version
}
class Cat extends Animal {
    @Override
    void sound() { System.out.println("meow!"); }
}

Animal a = new Dog();   // parent reference, child object — 100% legal!
a.sound();              // woof! — the OBJECT decides at runtime, not the reference
```

- **Overloading** (Chapter 7) = same name, different parameters — decided at **compile time**
- **Overriding** = child replaces parent's method — decided at **runtime**

### Pillar 4: Abstraction 👁️ (show what, hide how)

You press the bike's brake — you don't know (or care) about the hydraulics inside. In code: `abstract` classes and `interface`s expose WHAT can be done, hiding HOW.

```java
abstract class Shape {
    abstract double area();               // WHAT: every shape has area. HOW? Each decides.
}
class Circle extends Shape {
    double r;
    Circle(double r) { this.r = r; }
    double area() { return 3.14 * r * r; }   // HOW, for circles
}
```

`new Shape()` ❌ — abstract classes can't be instantiated. Full comparison with interfaces in [Q&A #28](../28-interfaces-vs-abstract-classes.md).

## 4. ❌ Common Beginner Mistakes

1. Forgetting `new`: `Student s; s.name = "x";` → NullPointerException-style compile error — the reference points nowhere
2. Confusing class with object — you never "set marks of the class", only of an object
3. Writing a return type on a constructor: `void Student() {...}` → that's just a weird method, NOT a constructor!
4. Overriding without `@Override` — works, but a tiny typo silently creates a NEW method instead of overriding. Always write `@Override`
5. Thinking `private` protects from other objects of the SAME class — it's per-CLASS, not per-object

---

## 🏋️ Practice

**Q1. Create a `Book` class with title & price, a constructor, and a `display()` method.**

<details><summary>✅ Solution</summary>

```java
class Book {
    private String title;
    private double price;

    Book(String title, double price) {
        this.title = title;
        this.price = price;
    }

    void display() {
        System.out.println(title + " costs ₹" + price);
    }
}

new Book("Java Made Easy", 299).display();   // Java Made Easy costs ₹299.0
```
</details>

**Q2. Predict the output:**
```java
Animal a = new Dog();   // Dog overrides sound() to print "woof!"
a.sound();
```

<details><summary>✅ Answer</summary>

`woof!` — the reference type (`Animal`) decides what you CAN call; the object type (`Dog`) decides WHICH version runs. This one line summarises runtime polymorphism — memorize it!
</details>

**Q3. Predict the output:**
```java
class A {
    A() { System.out.println("A born"); }
}
class B extends A {
    B() { System.out.println("B born"); }
}
new B();
```

<details><summary>✅ Answer</summary>

```
A born
B born
```
Parent constructor runs FIRST — Java secretly calls `super()` as the first line of every constructor. House foundation before walls! 🏗️
</details>

**Q4. Spot the bug:**
```java
class Counter {
    private int count;
    public void increment() { count++; }
}
Counter c1 = new Counter();
Counter c2 = new Counter();
c1.increment(); c1.increment(); c2.increment();
// How much is c1's count? c2's count?
```

<details><summary>✅ Answer</summary>

c1 → 2, c2 → 1. No bug — trick question 😉 Each object has its OWN `count`. If you wanted a shared counter across all objects, you'd make it `static int count;` — that's the static story continuing in Chapter 10 and [final & static Q&A](../26-final-static-keywords.md).
</details>

---

⬅️ [Prev: Strings](08-strings.md) | 🏠 [Index](../README.md) | ➡️ [Next: Collections](10-collections.md)
