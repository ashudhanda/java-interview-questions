# Chapter 8 — Strings 🧵

> **Goal:** Understand why Strings are "unchangeable", what the string pool actually is, and when to reach for StringBuilder.

---

## 1. Strings are IMMUTABLE 🔒

Once created, a String object **can never be changed**. Every "change" secretly creates a **new** object.

```java
String s = "hello";
s.toUpperCase();          // creates "HELLO"… and throws it away!
System.out.println(s);    // hello  (original untouched)

s = s.toUpperCase();      // ✅ catch the NEW string
System.out.println(s);    // HELLO
```

**Analogy 📜:** A String is a **printed page**, not a whiteboard. To "edit" it, the printer prints a fresh page — the old one stays in the bin until the garbage collector empties it.

## 2. The String Pool 🏊

Java keeps a special area in the heap where **string literals are shared**:

```java
String a = "java";          // goes to the POOL
String b = "java";          // reuses the SAME pool object!
String c = new String("java"); // new keyword = FORCED new object outside pool

a == b        // true  😲 (same pool object)
a == c        // false (different objects)
a.equals(c)   // true  (same content — always use this!)
```

```
         STRING POOL                    HEAP
        ┌──────────┐              ┌──────────┐
 a ───▶ │  "java"  │       c ───▶ │  "java"  │
 b ───▶ │ (shared) │              └──────────┘
        └──────────┘
```

Why? Strings are used EVERYWHERE — sharing saves tons of memory. And sharing is only safe **because** strings are immutable!

## 3. Methods you'll use daily 🧰

```java
String s = "Java Programming";

s.length()           // 16        (with brackets — it's a method!)
s.charAt(0)          // 'J'
s.substring(5)       // "Programming"
s.substring(0, 4)    // "Java"    (end index NOT included)
s.indexOf("gram")    // 8         (-1 if not found)
s.contains("Java")   // true
s.toLowerCase()      // "java programming"
s.trim()             // removes spaces at both ends
s.split(" ")         // ["Java", "Programming"]
s.replace('a', 'o')  // "Jovo Progromming"
```

> 💥 Flashback: arrays use `.length` (no brackets), Strings use `.length()` (brackets). Interviewers LOVE this.

## 4. Concatenation in a loop = performance trap 🐌

```java
String result = "";
for (int i = 0; i < 10000; i++) {
    result += i;     // creates a NEW string EVERY time — 10,000 dead objects!
}
```

Fix: **StringBuilder** — the whiteboard version of String (mutable):

```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);            // edits in place — fast 🚀
}
String result = sb.toString();
```

Bonus toys: `sb.reverse()`, `sb.insert(0, "hi")`, `sb.deleteCharAt(2)`.

**When to use what?**

| Situation | Use |
|-----------|-----|
| Fixed text, few joins | `String` |
| Building text in a loop | `StringBuilder` |
| Multiple threads editing same text (rare) | `StringBuffer` |

## 5. ❌ Common Beginner Mistakes

1. `s.toUpperCase();` without assigning — immutability! Catch the returned string
2. Comparing with `==` — pool makes it work sometimes, then production breaks 💀
3. `s.charAt(s.length())` → StringIndexOutOfBoundsException (last index = length-1)
4. `substring(0, 4)` confusion — index 4 is EXCLUDED, so you get chars 0,1,2,3
5. Concatenating in big loops with `+=` → slow programs

---

## 🏋️ Practice

**Q1. Reverse a string.**

<details><summary>✅ Solution (2 ways)</summary>

```java
// Way 1: the shortcut
String rev = new StringBuilder("hello").reverse().toString();   // "olleh"

// Way 2: manual (what interviewers actually want)
String s = "hello";
String rev2 = "";
for (int i = s.length() - 1; i >= 0; i--) {
    rev2 += s.charAt(i);    // o → ol → oll → olle → olleh
}
```
</details>

**Q2. Check if a string is a palindrome (madam → yes).**

<details><summary>✅ Solution + dry run</summary>

```java
String s = "madam";
boolean isPalin = true;
int left = 0, right = s.length() - 1;
while (left < right) {
    if (s.charAt(left) != s.charAt(right)) {
        isPalin = false;
        break;
    }
    left++; right--;
}
System.out.println(isPalin);   // true
```
**Dry run:** m==m ✓ → a==a ✓ → left meets right at 'd' → true. Two friends from both ends again — same pattern as array reverse (Chapter 4)!
</details>

**Q3. Count vowels in a string.**

<details><summary>✅ Solution</summary>

```java
String s = "programming";
int count = 0;
for (char c : s.toCharArray()) {
    if ("aeiouAEIOU".indexOf(c) != -1) count++;
}
System.out.println(count);   // 3 (o, a, i)
```
**Trick:** instead of 10 conditions, ask the vowels-string "is this char inside you?" with `indexOf`.
</details>

**Q4. Predict the output:**
```java
String s1 = "abc";
String s2 = "ab" + "c";
String x = "ab";
String s3 = x + "c";
System.out.println(s1 == s2);
System.out.println(s1 == s3);
```

<details><summary>✅ Answer</summary>

```
true
false
```
`"ab" + "c"` is joined by the **compiler** (both literals) → goes to the pool → same object as s1. But `x + "c"` happens at **runtime** → new object outside the pool. Evil, right? 😈 That's why: `.equals()`. Always.
</details>

---

⬅️ [Prev: Methods](07-methods.md) | 🏠 [Index](../README.md) | ➡️ [Next: OOPs Made Simple](09-oops-made-simple.md)
