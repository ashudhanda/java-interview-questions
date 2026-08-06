# Chapter 6 — Loops 🔁

> **Goal:** Make the computer repeat work 1000 times while you relax — and dry-run any loop on paper without fear.

---

## 1. Why loops?

Print "Hello" 100 times — are you going to write 100 `println` lines? 😅 A **loop** repeats a block of code while a condition stays true.

**Analogy 🏃:** Running laps. Before every lap you ask the coach: *"one more?"* — if yes, run; if no, stop.

## 2. The `for` loop — anatomy

```java
for (int i = 1; i <= 5; i++) {
    System.out.println("Lap " + i);
}
//    │          │        └── 3️⃣ update: after EVERY lap
//    │          └─────────── 2️⃣ condition: checked BEFORE every lap
//    └──────────────────── 1️⃣ start: runs ONCE
```

**Order of execution:** start → check → body → update → check → body → update → … → check fails → exit.

## 3. `while` and `do-while`

```java
// while — check FIRST, run after (may run 0 times)
int i = 1;
while (i <= 5) {
    System.out.println(i);
    i++;
}

// do-while — run FIRST, check after (runs AT LEAST once)
int n = 100;
do {
    System.out.println("runs once even though 100 > 5!");
} while (n <= 5);
```

**When to use what?**

| Loop | Use when… | Example |
|------|-----------|---------|
| `for` | you know HOW MANY times | print table of 7 |
| `while` | you know the STOP condition | keep reading until user types "exit" |
| `do-while` | must run at least once | show menu, then ask "again?" |

## 4. `break` and `continue`

```java
for (int i = 1; i <= 10; i++) {
    if (i == 4) continue;   // skip 4, jump to next lap
    if (i == 7) break;      // stop the whole loop at 7
    System.out.print(i + " ");
}
// Output: 1 2 3 5 6
```

- `continue` = "skip this lap" ⏭️
- `break` = "leave the race" 🏁

## 5. Nested loops — loops inside loops

**Analogy 🕒:** A clock — for every 1 hour (outer), the minute hand does 60 laps (inner).

```java
for (int i = 1; i <= 3; i++) {          // rows
    for (int j = 1; j <= i; j++) {      // stars in that row
        System.out.print("*");
    }
    System.out.println();
}
// *
// **
// ***
```

> 💡 Pattern questions = 90% of nested-loop practice. Trick: outer loop = ROWS, inner loop = what happens INSIDE one row.

## 6. 💥 The Infinite Loop

```java
int i = 1;
while (i <= 5) {
    System.out.println(i);   // forgot i++ → prints 1 forever!!
}
```

If your program never stops → check: is the loop variable actually moving toward the exit?

## 7. ❌ Common Beginner Mistakes

1. `for (int i = 0; i <= arr.length; i++)` → crashes at the end — use `<` (Chapter 4 flashback!)
2. Forgetting the update step → infinite loop
3. Semicolon after loop: `for (...);  { ... }` — loop runs empty, block runs once
4. Using the loop variable AFTER the loop — `int i` declared in `for` dies with the loop
5. Off-by-one: "print 1 to N" — always test with N=1 and N=2 on paper

---

## 🏋️ Practice

**Q1. Sum of 1 to N.**

<details><summary>✅ Solution + dry run (N=5)</summary>

```java
int n = 5, sum = 0;
for (int i = 1; i <= n; i++) {
    sum += i;   // 0→1→3→6→10→15
}
System.out.println(sum);   // 15
```
**Dry run:** i=1 sum=1 → i=2 sum=3 → i=3 sum=6 → i=4 sum=10 → i=5 sum=15 → i=6 condition fails.
</details>

**Q2. Print the multiplication table of 7.**

<details><summary>✅ Solution</summary>

```java
for (int i = 1; i <= 10; i++) {
    System.out.println("7 x " + i + " = " + (7 * i));
}
```
Note the brackets in `(7 * i)` — without them, `+` does string joining and you get `7 x 1 = 71`… wait no, you'd get the math wrong only without brackets in mixed positions. Rule: **when printing math, always bracket it.**
</details>

**Q3. Reverse the digits of a number (classic!).**

<details><summary>✅ Solution + dry run (n=123)</summary>

```java
int n = 123, rev = 0;
while (n > 0) {
    int digit = n % 10;      // grab last digit
    rev = rev * 10 + digit;  // shift left, attach digit
    n = n / 10;              // drop last digit
}
System.out.println(rev);     // 321
```
**Dry run:** (n=123, rev=0) → digit=3, rev=3, n=12 → digit=2, rev=32, n=1 → digit=1, rev=321, n=0 → stop.
This `% 10` + `/ 10` combo powers palindrome & Armstrong questions too!
</details>

**Q4. Predict the output:**
```java
for (int i = 0; i < 3; i++);
System.out.println("Hi");
```

<details><summary>✅ Answer</summary>

`Hi` printed **once** — the `;` right after `for(...)` is an empty body! The loop spins 3 times doing nothing, then `println` runs once. Mistake #3 from the list above 😉
</details>

---

⬅️ [Prev: Operators & Conditionals](05-operators-and-conditionals.md) | 🏠 [Index](../README.md) | ➡️ [Next: Methods](07-methods.md)
