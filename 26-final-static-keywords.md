# 26 — final & static Keywords 🔒

Do chhote keywords, par interviews me inke trick questions sabse zyada aate hain.

## Q1. `final` variable kya karta hai?
**Answer:** Value **reassign nahi** ho sakti — ek baar assign, bas.

```java
final int x = 10;
// x = 20; // ❌ compile error
```
⭐ **Trick:** `final` reference variable ka object **mutable reh sakta hai**:
```java
final List<String> list = new ArrayList<>();
list.add("a");   // ✅ allowed — object change ho raha, reference nahi
// list = new ArrayList<>(); // ❌ reference change nahi ho sakta
```

## Q2. `final` method aur `final` class?
**Answer:**
- `final` method → **override nahi** ho sakta (subclass me)
- `final` class → **inherit nahi** ho sakta (`String`, `Integer` final hain — isliye koi String extend nahi kar sakta)

Interview follow-up: *"String final kyun hai?"* — Security + string pool + hashCode caching, sab immutability pe depend karta hai.

## Q3. `static` variable vs instance variable?
**Answer:**

| | `static` | instance |
|---|----------|----------|
| Owner | **Class** (ek copy) | **Object** (har object ki apni copy) |
| Memory | Metaspace/class area | Heap (object ke andar) |
| Access | `ClassName.var` | `object.var` |
| Kab use | Shared counter, constants | Object-specific state |

```java
class Counter {
    static int total = 0;   // sab objects share karenge
    int id;                 // har object ka apna
}
```

## Q4. `static` method ki limitations?
**Answer:**
- **`this`/`super` use nahi** kar sakta (koi object hi nahi)
- Instance members ko **directly access nahi** kar sakta
- **Override nahi** hota — sirf **hide** hota hai (method hiding)

⭐ Trick: `static` method "override" dikhne wala code actually **hiding** hai — compile time pe decide hota hai, runtime polymorphism nahi!

## Q5. `static` block kab chalta hai?
**Answer:** Class **load hote hi** — main method se bhi pehle, aur sirf **ek baar**.

```java
class Demo {
    static {
        System.out.println("Class loading..."); // sabse pehle
    }
}
```
Use case: static config initialize karna, native libraries load karna. Multiple static blocks → order me chalenge (upar se neeche).

## Q6. Order of execution trick question!
```java
class A {
    static { System.out.println("1: static block"); }
    { System.out.println("2: instance block"); }
    A() { System.out.println("3: constructor"); }
}
```
`new A()` pe output: **1 → 2 → 3**
(static block: class load pe ek baar · instance block: har object se pehle · constructor: last)

## Q7. Can we override a static method? (Classic trap)
**Answer:** Nahi — subclass me same signature likhna **method hiding** hai:
```java
Parent p = new Child();
p.staticMethod(); // Parent ka hi chalega — reference type decide karta hai!
```
Instance methods me runtime pe object decide karta hai (polymorphism); static me compile time pe reference type. Ye difference interviewers ko bahut pasand hai.

## Q8. `final static` combo kab use hota hai?
**Answer:** **Constants** ke liye:
```java
public static final double PI = 3.14159;
public static final int MAX_RETRY = 3;
```
`static` = class-level ek copy · `final` = change nahi ho sakta. Naming convention: `UPPER_SNAKE_CASE`.