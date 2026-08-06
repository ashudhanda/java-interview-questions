# Chapter 7 — Methods 📦

> **Goal:** Stop copy-pasting code — write it once, name it, and call it from anywhere. Plus: finally understand *pass by value*.

---

## 1. Why methods?

Same 10 lines needed in 5 places? Copy-paste = 5× the bugs. A **method** is a named block of code you write once and call whenever needed.

**Analogy ☕:** A coffee machine — you press a button (call), maybe insert coins (arguments), it does its internal work, and hands you coffee (return value). You don't care HOW it works inside.

## 2. Anatomy of a method

```java
//  who can    what it
//  access it  gives back   name      ingredients it needs
//     │          │          │              │
public static  int      addNumbers(int a, int b) {
    int sum = a + b;
    return sum;          // hand back the result & exit
}
```

Calling it:

```java
int result = addNumbers(5, 3);   // result = 8
```

- **Parameters** = the ingredient list in the recipe (`int a, int b`)
- **Arguments** = the actual ingredients you pass (`5, 3`)
- `void` = "this method returns NOTHING" — no `return value;` needed

> 💡 Why `static` for now? So `main` (which is static) can call it directly. The full static story was in Chapter 1 — and gets deeper in OOPs (Chapter 9).

## 3. The call stack — how Java tracks calls 🥞

Remember the **stack** from Chapter 3? Every method call = a new plate on the pile. `return` = that plate is removed.

```java
main() calls greet() calls getName()

┌───────────┐  ← getName() (top, runs now)
├───────────┤
│  greet()  │  ← waiting…
├───────────┤
│  main()   │  ← waiting…
└───────────┘
```

When `getName()` returns → its plate pops → `greet()` resumes. This is why infinite recursion gives **StackOverflowError** — the pile of plates hits the ceiling!

## 4. 💥 Pass by VALUE — Java's most misunderstood rule

Java **always copies** what you pass into a method.

```java
static void change(int x) {
    x = 100;                    // changes the COPY only
}

int a = 5;
change(a);
System.out.println(a);          // still 5!
```

But with objects/arrays, the **address is copied** — both copies point to the SAME object:

```java
static void update(int[] arr) {
    arr[0] = 99;                // follows the address → changes the real array!
}

int[] nums = {1, 2, 3};
update(nums);
System.out.println(nums[0]);    // 99 😱
```

**One-liner for interviews:** *"Java is always pass-by-value — but for objects, the value being copied is the reference."*

## 5. Method Overloading — same name, different ingredients

```java
static int    area(int side)          { return side * side; }        // square
static int    area(int l, int b)      { return l * b; }              // rectangle
static double area(double radius)     { return 3.14 * radius * radius; } // circle
```

Java picks the right one by looking at the **arguments** (count + types). Return type alone is NOT enough to overload!

## 6. ❌ Common Beginner Mistakes

1. Method returns `int` but you forget `return` on some path → compile error "missing return statement"
2. Calling a non-static method from `main` without an object → the classic confusing error (fixed properly in Chapter 9)
3. Expecting `change(a)` to modify your primitive variable — nope, copies!
4. `return` in a `void` method with a value → error (plain `return;` is fine to exit early)
5. Code written after `return` → "unreachable statement" error

---

## 🏋️ Practice

**Q1. Write a method to check if a number is prime.**

<details><summary>✅ Solution + logic</summary>

```java
static boolean isPrime(int n) {
    if (n <= 1) return false;              // 0, 1, negatives → not prime
    for (int i = 2; i * i <= n; i++) {     // only check up to √n
        if (n % i == 0) return false;      // found a divisor → done, exit early
    }
    return true;
}
```
**Logic:** if `n` has a divisor bigger than √n, its partner is smaller than √n — so checking till √n is enough. Early `return` = clean exit, no `break` gymnastics.
</details>

**Q2. Predict the output:**
```java
static void swap(int a, int b) {
    int t = a; a = b; b = t;
}
int x = 1, y = 2;
swap(x, y);
System.out.println(x + " " + y);
```

<details><summary>✅ Answer</summary>

`1 2` — unchanged! `swap` shuffles **copies** of x and y. This exact question appears in every viva. (To actually swap, do it inline or use an array/object.)
</details>

**Q3. Write factorial using a method, then dry run fact(4).**

<details><summary>✅ Solution + dry run</summary>

```java
static long fact(int n) {
    long result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;    // 1→2→6→24
    }
    return result;
}
```
**Dry run fact(4):** i=2 result=2 → i=3 result=6 → i=4 result=24 → return 24. (Recursive version lives in [Recursion Q&A](../27-recursion-coding-questions.md)!)
</details>

**Q4. Which overload runs?**
```java
static void show(int x)    { System.out.println("int"); }
static void show(double x) { System.out.println("double"); }
show(5);
show(5.0);
show('A');
```

<details><summary>✅ Answer</summary>

```
int
double
int
```
Surprise on the third: `char` fits into `int` (widening: char → int) before it would widen to `double`. Java always picks the **narrowest fitting** overload.
</details>

---

⬅️ [Prev: Loops](06-loops.md) | 🏠 [Index](../README.md) | ➡️ [Next: Strings](08-strings.md)
