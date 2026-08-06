# Chapter 5 — Operators & Conditionals 🚦

> **Goal:** Teach your program to make decisions — and never fall for the `==` vs `.equals()` trap again.

---

## 1. Operators — your toolbox 🧰

| Type | Operators | Example | Result |
|------|-----------|---------|--------|
| Arithmetic | `+  -  *  /  %` | `7 / 2` | `3` (int division!) |
| Arithmetic | `%` = remainder | `7 % 2` | `1` |
| Relational | `>  <  >=  <=  ==  !=` | `5 > 3` | `true` |
| Logical | `&&` (AND), `\|\|` (OR), `!` (NOT) | `true && false` | `false` |
| Assignment | `=  +=  -=  *=  /=` | `x += 5` | same as `x = x + 5` |

> 💡 `%` (modulo) is secretly the interview favourite: `n % 2 == 0` → even number, `n % 10` → last digit of n.

## 2. `++` and `--` — the sneaky twins

```java
int i = 5;
System.out.println(i++);  // 5  (print FIRST, then i becomes 6)
System.out.println(++i);  // 7  (increase FIRST, then print)
```

**Analogy 🎫:** `i++` = "use the ticket, then upgrade". `++i` = "upgrade first, then use".

## 3. 💥 The Big Trap: `==` vs `.equals()`

```java
String a = "hello";
String b = new String("hello");

System.out.println(a == b);        // false 😱 (compares ADDRESSES)
System.out.println(a.equals(b));   // true  ✅ (compares CONTENT)
```

Remember Chapter 3: reference variables store **addresses**. `==` asks "same house?", `.equals()` asks "same stuff inside the house?"

**Rule: numbers → `==`, Strings/objects → `.equals()`. Always.**

## 4. if / else if / else

**Analogy 🚦:** A traffic signal checks conditions top to bottom and takes the FIRST road that's green.

```java
int marks = 76;

if (marks >= 90) {
    System.out.println("A grade");
} else if (marks >= 75) {
    System.out.println("B grade");   // ← this runs, checking stops here!
} else {
    System.out.println("Keep practicing");
}
```

- Conditions are checked **top to bottom** — order matters!
- Only the **first true** block runs; the rest are skipped.

## 5. switch — cleaner than 10 else-ifs

```java
int day = 3;
switch (day) {
    case 1: System.out.println("Mon"); break;
    case 2: System.out.println("Tue"); break;
    case 3: System.out.println("Wed"); break;   // ← runs
    default: System.out.println("Weekend?");
}
```

> 💥 Forget `break` → **fall-through**: execution keeps running into the NEXT cases too. Classic tricky-output question!

Modern Java (14+) fixes this with arrows — no `break` needed:

```java
switch (day) {
    case 1 -> System.out.println("Mon");
    case 3 -> System.out.println("Wed");
    default -> System.out.println("Other");
}
```

## 6. Ternary — if-else in one line

```java
int age = 20;
String result = (age >= 18) ? "Adult" : "Minor";   // "Adult"
```

Format: `condition ? valueIfTrue : valueIfFalse` — great for tiny decisions, terrible for complex ones.

## 7. ❌ Common Beginner Mistakes

1. `if (x = 5)` instead of `if (x == 5)` — assignment vs comparison. Java usually saves you with a compile error… unless `x` is boolean 😈
2. Comparing Strings with `==` — it works *sometimes* (string pool!), which makes the bug even harder to find
3. Missing `break` in switch → mysterious extra output
4. `if (a > b && > c)` — invalid! Write both sides fully: `a > b && a > c`
5. Semicolon after if: `if (x > 5); { ... }` — that `;` IS the body, so the block below runs **always**!

---

## 🏋️ Practice

**Q1. Check if a number is even or odd.**

<details><summary>✅ Solution + logic</summary>

```java
int n = 7;
if (n % 2 == 0) {
    System.out.println("Even");
} else {
    System.out.println("Odd");     // 7 % 2 = 1 → Odd
}
```
**Logic:** `% 2` gives remainder 0 or 1. Even numbers are divisible by 2 → remainder 0.
</details>

**Q2. Find the largest of three numbers.**

<details><summary>✅ Solution + dry run</summary>

```java
int a = 25, b = 42, c = 17;
int max = a;                 // assume a (25)
if (b > max) max = b;        // 42 > 25 → max = 42
if (c > max) max = c;        // 17 > 42? no → no change
System.out.println(max);     // 42
```
Same "tallest person in the line" trick from Chapter 4!
</details>

**Q3. Predict the output:**
```java
int i = 10;
System.out.println(i++ + ++i);
```

<details><summary>✅ Answer + dry run</summary>

`22` — `i++` gives **10** (then i becomes 11), `++i` makes i = 12 and gives **12**. So `10 + 12 = 22`.
</details>

**Q4. Predict the output:**
```java
String s1 = "java";
String s2 = "java";
System.out.println(s1 == s2);
```

<details><summary>✅ Answer</summary>

`true` 😱 — both literals point to the **same object** in the string pool (Chapter 8 explains this magic). But `new String("java") == "java"` would be `false`. This unpredictability is exactly why the rule is: **always use `.equals()` for Strings**.
</details>

---

⬅️ [Prev: Arrays](04-arrays.md) | 🏠 [Index](../README.md) | ➡️ [Next: Loops](06-loops.md)
