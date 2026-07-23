# Chapter 2 — Variables & Data Types 📦

> **Goal:** Understand what variables really are, and never get tricked by `5/2 = 2` again.

---

## 1. What is a variable?

**Analogy 📦:** A variable is a **labeled box** in memory.

```java
int age = 20;
```
- `int` → the **type** of box (this box only holds whole numbers)
- `age` → the **label** on the box
- `20` → the **value** inside the box

Java is **strictly typed**: an `int` box can NEVER hold text. The compiler checks this before your program even runs — that's why Java catches so many mistakes early.

## 2. The 8 Primitive Types (the basic boxes)

| Type | Stores | Size | Example |
|------|--------|------|---------|
| `byte` | tiny whole numbers (-128 to 127) | 1 byte | `byte b = 100;` |
| `short` | small whole numbers | 2 bytes | `short s = 30000;` |
| `int` | whole numbers ⭐ default choice | 4 bytes | `int x = 100000;` |
| `long` | huge whole numbers | 8 bytes | `long l = 15000000000L;` |
| `float` | decimals (less precise) | 4 bytes | `float f = 3.14f;` |
| `double` | decimals ⭐ default choice | 8 bytes | `double d = 3.14159;` |
| `char` | ONE character in single quotes | 2 bytes | `char c = 'A';` |
| `boolean` | only `true` / `false` | ~1 bit | `boolean ok = true;` |

> 💡 Notice: `long` needs `L` at the end, `float` needs `f`. Forgetting these is a classic compile error.

### What about String?

`String` is **not** primitive — it's an **object** (a fancy box that holds many chars + useful powers like `.length()`). Note the capital S, and double quotes:

```java
String name = "Ashu";   // "text" → double quotes
char grade = 'A';        // 'one char' → single quotes
```

## 3. Type Casting (moving between boxes)

**Widening (small → big): automatic, safe** — pouring a glass of water into a bucket →🪣

```java
int marks = 90;
double d = marks;    // 90.0 — no data loss, Java does it silently
```

**Narrowing (big → small): manual, risky** — pouring a bucket into a glass (may spill!)

```java
double price = 99.99;
int rupees = (int) price;   // 99 — decimal part LOST, you must write (int)
```

## 4. ⚠️ The Famous Int Division Trap

```java
System.out.println(5 / 2);     // 2   (int ÷ int = int — decimal thrown away!)
System.out.println(5.0 / 2);   // 2.5 (one double makes the result double)
System.out.println(5 % 2);     // 1   (% gives the REMAINDER)
```

> `int / int` always gives `int`. To get decimals, make at least one side a double.

## 5. ❌ Common Beginner Mistakes

1. `int x = 3.5;` → error — decimals can't fit in an int box without casting
2. `char c = "A";` → error — double quotes make a String; use `'A'`
3. Using a variable before giving it a value → compile error (local variables have no default)
4. `float f = 3.14;` → error — `3.14` is a double by default; write `3.14f`

---

## 🏋️ Practice

**Q1. Swap two numbers using a third variable.**

<details><summary>✅ Solution + dry run</summary>

```java
int a = 5, b = 10;
int temp = a;   // temp = 5   (save a's value before overwriting)
a = b;          // a = 10
b = temp;       // b = 5
System.out.println(a + " " + b);   // 10 5
```
**Why temp?** If you did `a = b` first, a's original value (5) would be lost forever — temp is the extra hand holding your first cup while you pour. ☕
</details>

**Q2. Average of 3 marks — spot the bug:**
```java
int m1 = 90, m2 = 85, m3 = 92;
int avg = (m1 + m2 + m3) / 3;
```

<details><summary>✅ Answer</summary>

`(90+85+92)/3 = 267/3 = 89` — looks fine here, but for marks `90, 85, 91` → `266/3 = 88` (real answer 88.67 — decimal lost!). Fix:
```java
double avg = (m1 + m2 + m3) / 3.0;   // 88.666...
```
</details>

**Q3. Predict the output:**
```java
int x = 7;
double y = x / 2;
System.out.println(y);
```

<details><summary>✅ Answer</summary>

`3.0` — tricky! `x / 2` is int÷int = `3` FIRST, and only then it's stored into double `y` as `3.0`. To get `3.5`: `double y = x / 2.0;`
</details>

---

⬅️ [Prev: How Java Works](01-how-java-works.md) | ➡️ [Next: Memory — Stack vs Heap](03-memory-stack-vs-heap.md)
