# 🧠 Tricky Output Questions — Guess the Output!

> These short programs look simple but trip up 90% of candidates. **Predict the output first**, then open the answer. No cheating! 😄

---

### Q1. String pool trap

```java
String a = "java";
String b = "java";
String c = new String("java");

System.out.println(a == b);
System.out.println(a == c);
System.out.println(a.equals(c));
```

<details><summary>✅ Answer</summary>

```
true
false
true
```

`a` and `b` point to the **same object** in the string pool. `new String()` always creates a **new object** in the heap, so `a == c` is `false`. `.equals()` compares content, so it's `true`.

</details>

---

### Q2. Integer caching

```java
Integer x = 127, y = 127;
Integer p = 128, q = 128;

System.out.println(x == y);
System.out.println(p == q);
```

<details><summary>✅ Answer</summary>

```
true
false
```

Java **caches** Integer objects from -128 to 127. Within that range, autoboxing returns the same cached object. Outside it, new objects are created — so `==` fails. Always use `.equals()` for wrapper classes!

</details>

---

### Q3. Post vs pre increment

```java
int i = 5;
i = i++;
System.out.println(i);
```

<details><summary>✅ Answer</summary>

```
5
```

`i++` returns the **old value** (5), then increments `i` to 6 — but the assignment immediately **overwrites** `i` with the returned 5. Classic!

</details>

---

### Q4. Floating point surprise

```java
System.out.println(0.1 + 0.2 == 0.3);
System.out.println(0.1 + 0.2);
```

<details><summary>✅ Answer</summary>

```
false
0.30000000000000004
```

Binary floating point can't represent 0.1 and 0.2 exactly. Never compare doubles with `==` — use a small epsilon or `BigDecimal` for money.

</details>

---

### Q5. char arithmetic

```java
char ch = 'A';
System.out.println(ch + 1);
System.out.println((char)(ch + 1));
System.out.println("" + ch + 1);
```

<details><summary>✅ Answer</summary>

```
66
B
A1
```

`ch + 1` promotes char to **int** (65 + 1 = 66). Casting back to char gives `'B'`. But `"" + ch + 1` is **string concatenation**, evaluated left to right → `"A"` then `"A1"`.

</details>

---

### Q6. String concatenation order

```java
System.out.println(1 + 2 + "3");
System.out.println("1" + 2 + 3);
```

<details><summary>✅ Answer</summary>

```
33
123
```

Left to right: `1 + 2` is int addition (3), then `3 + "3"` → `"33"`. In the second line, `"1" + 2` is already string concat → `"12"` then `"123"`.

</details>

---

### Q7. Integer overflow

```java
int max = Integer.MAX_VALUE;
System.out.println(max + 1);
```

<details><summary>✅ Answer</summary>

```
-2147483648
```

No error, no warning — int silently **wraps around** to `Integer.MIN_VALUE`. Use `long` or `Math.addExact()` (throws on overflow) when values can get large.

</details>

---

### Q8. Short-circuit evaluation

```java
int a = 5;
boolean result = (a < 3) && (++a > 5);
System.out.println(a);
System.out.println(result);
```

<details><summary>✅ Answer</summary>

```
5
false
```

`a < 3` is `false`, so `&&` **skips the right side entirely** — `++a` never runs. `a` stays 5. With `&` (non-short-circuit) it would become 6.

</details>

---

### Q9. Array default values

```java
int[] nums = new int[3];
String[] names = new String[3];
System.out.println(nums[0]);
System.out.println(names[0]);
```

<details><summary>✅ Answer</summary>

```
0
null
```

Arrays are auto-initialized: numeric types → `0`, boolean → `false`, **object types → `null`**. Calling `names[0].length()` here would throw `NullPointerException`.

</details>

---

### Q10. Ternary type promotion

```java
Object result = true ? 1 : 2.0;
System.out.println(result);
```

<details><summary>✅ Answer</summary>

```
1.0
```

Mind-bending one! The ternary's two branches (`int` and `double`) are unified to a **common type** (`double`) *before* evaluation. So `1` becomes `1.0`. 

</details>

---

**Score yourself:** 8–10 correct = interview-ready 🏆 · 5–7 = solid, revise promotions & pooling 💪 · <5 = re-read [Java Basics](01-java-basics.md) and come back!
