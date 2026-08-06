# 27 — Recursion Coding Questions 🪜

> A function that calls **itself**. Scary reputation, simple rules. Master the 5 classics below and recursion questions become free marks.

---

## The 2 golden rules of every recursion

1. **Base case** — the STOP condition (no base case → `StackOverflowError`, see the call stack in [Chapter 7](notes/07-methods.md))
2. **Smaller input** — every call must move TOWARD the base case

```java
returnType solve(input) {
    if (smallest case) return direct answer;   // 1️⃣ base case
    return combine( solve(smaller input) );    // 2️⃣ shrink + trust
}
```

**Mindset trick 🧠:** Don't trace every level. ASSUME `solve(n-1)` already works (like a junior who handles the smaller task), and just combine its answer.

---

## Q1. Factorial (the "hello world" of recursion)

```java
static long fact(int n) {
    if (n <= 1) return 1;          // base case
    return n * fact(n - 1);        // n! = n × (n-1)!
}
```

**Dry run `fact(4)`:**
```
fact(4) = 4 * fact(3)
        = 4 * (3 * fact(2))
        = 4 * (3 * (2 * fact(1)))
        = 4 * (3 * (2 * 1)) = 24
```
Each pending `fact` waits on the call stack — 4 plates stacked, then they pop one by one.

## Q2. Fibonacci number

```java
static int fib(int n) {
    if (n <= 1) return n;               // fib(0)=0, fib(1)=1
    return fib(n - 1) + fib(n - 2);     // two branches!
}
// 0 1 1 2 3 5 8 13 ...  → fib(6) = 8
```

⚠️ **Interview follow-up:** "Why is this slow for n=50?" — because it recalculates the same values millions of times (fib(48) computed twice, fib(47) three times…). Fix = memoization or a simple loop. Saying this out loud = instant bonus points 🏆

## Q3. Sum of digits

```java
static int sumDigits(int n) {
    if (n == 0) return 0;
    return (n % 10) + sumDigits(n / 10);   // last digit + sum of the rest
}
```

**Dry run `sumDigits(123)`:** `3 + sumDigits(12)` → `3 + 2 + sumDigits(1)` → `3 + 2 + 1 + sumDigits(0)` → `3+2+1+0 = 6`. Same `% 10` / `/ 10` combo from [Loops Q3](notes/06-loops.md)!

## Q4. Power: xⁿ

```java
static double power(double x, int n) {
    if (n == 0) return 1;              // x⁰ = 1
    return x * power(x, n - 1);
}
```

**Level-up (fast power, O(log n)) — for the "optimize it" follow-up:**
```java
static double fastPower(double x, int n) {
    if (n == 0) return 1;
    double half = fastPower(x, n / 2);
    return (n % 2 == 0) ? half * half : x * half * half;
}
// 2^10 → (2^5)² → 2×(2²)²² ... only ~4 calls instead of 10
```

## Q5. Reverse a string recursively

```java
static String reverse(String s) {
    if (s.isEmpty()) return s;                      // base case
    return reverse(s.substring(1)) + s.charAt(0);   // reverse(rest) + first char
}
```

**Dry run `reverse("abc")`:** `reverse("bc") + 'a'` → `(reverse("c") + 'b') + 'a'` → `(("" + 'c') + 'b') + 'a'` → `"cba"`.

---

## 💥 Predict-the-output corner

```java
static void mystery(int n) {
    if (n == 0) return;
    System.out.print(n + " ");
    mystery(n - 1);
    System.out.print(n + " ");
}
mystery(3);
```

<details><summary>✅ Answer</summary>

`3 2 1 1 2 3` — prints on the way DOWN (before the call), then again on the way UP (after each call returns, in reverse order). Print-before vs print-after recursion = guaranteed viva question!
</details>

---

## ❌ Common mistakes

1. No base case, or base case never reached (`solve(n)` calling `solve(n)`) → StackOverflowError
2. Forgetting to `return` the recursive result: `fact(n-1);` alone does nothing — must be `return n * fact(n-1);`
3. Using recursion where a loop is simpler — recursion is a tool, not a flex 😅

🏠 [Back to Index](README.md) | Related: [Methods & Call Stack](notes/07-methods.md) · [Advanced Coding Problems](08-advanced-coding-problems.md)
