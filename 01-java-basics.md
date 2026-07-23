# 🟢 Java Basics — 20 Questions

### Q1. What are JDK, JRE, and JVM?
**Answer:**
- **JVM (Java Virtual Machine)** — runs Java bytecode; makes Java platform-independent.
- **JRE (Java Runtime Environment)** — JVM + libraries needed to *run* Java programs.
- **JDK (Java Development Kit)** — JRE + compiler (`javac`) + tools needed to *develop* Java programs.

> JDK ⊃ JRE ⊃ JVM

---

### Q2. Why is Java called platform-independent?
**Answer:** Java source code compiles to **bytecode** (`.class` files), not machine code. Bytecode runs on any OS that has a JVM — *"Write Once, Run Anywhere"*.

---

### Q3. Why is the `main` method `public static void`?
**Answer:**
- `public` — JVM must call it from outside the class.
- `static` — JVM calls it **without creating an object** of the class.
- `void` — it returns nothing to the JVM.

```java
public static void main(String[] args) { }
```

---

### Q4. What is the difference between `==` and `.equals()`?
**Answer:** `==` compares **references** (memory addresses) for objects; `.equals()` compares **content** (if overridden).

```java
String a = new String("hi");
String b = new String("hi");
System.out.println(a == b);      // false (different objects)
System.out.println(a.equals(b)); // true  (same content)
```

---

### Q5. What are the 8 primitive data types in Java?
**Answer:** `byte` (1B), `short` (2B), `int` (4B), `long` (8B), `float` (4B), `double` (8B), `char` (2B), `boolean` (~1 bit, JVM-dependent).

---

### Q6. What are wrapper classes? What is autoboxing/unboxing?
**Answer:** Wrapper classes wrap primitives into objects: `int → Integer`, `char → Character`, etc.
- **Autoboxing:** primitive → object automatically (`Integer x = 5;`)
- **Unboxing:** object → primitive automatically (`int y = x;`)

---

### Q7. What does the `final` keyword do?
**Answer:**
- `final` variable → constant (can't be reassigned)
- `final` method → can't be overridden
- `final` class → can't be extended (e.g. `String`)

---

### Q8. What does the `static` keyword do?
**Answer:** `static` members belong to the **class**, not to objects. One copy is shared by all instances. Static methods can be called without creating an object, and can only directly access other static members.

---

### Q9. Difference between local, instance, and static variables?
**Answer:**
| Type | Declared | Lifetime | Default value |
|------|----------|----------|---------------|
| Local | Inside method | Method call | ❌ must initialize |
| Instance | Inside class, no `static` | Object lifetime | ✅ (0, null, false) |
| Static | Inside class, with `static` | Class lifetime | ✅ (0, null, false) |

---

### Q10. What is type casting? Implicit vs explicit?
**Answer:**
- **Implicit (widening):** small → big, automatic. `int → long → double`
- **Explicit (narrowing):** big → small, manual, may lose data.

```java
int i = 100;
long l = i;          // implicit
int back = (int) l;  // explicit
```

---

### Q11. Can we overload the `main` method?
**Answer:** **Yes** — but JVM only calls `main(String[] args)`. Other overloads must be called manually.

```java
public static void main(String[] args) { main(10); }
public static void main(int x) { System.out.println(x); }
```

---

### Q12. What happens during Java compilation and execution?
**Answer:** `javac` compiles `.java` → `.class` (bytecode). JVM loads the class (ClassLoader), verifies bytecode, and executes it — interpreting it and JIT-compiling hot code paths to native machine code.

---

### Q13. What are default values of data types?
**Answer:** For instance/static variables: numbers → `0`/`0.0`, `char` → `'\u0000'`, `boolean` → `false`, objects → `null`. **Local variables have no defaults** — using them uninitialized is a compile error.

---

### Q14. What is the `var` keyword (Java 10+)?
**Answer:** Local variable type inference — the compiler infers the type from the right-hand side. It is still **statically typed**, not dynamic.

```java
var list = new ArrayList<String>(); // inferred as ArrayList<String>
```

---

### Q15. Difference between `break` and `continue`?
**Answer:** `break` exits the loop entirely; `continue` skips the current iteration and moves to the next one.

---

### Q16. What is the ternary operator?
**Answer:** A one-line if-else: `condition ? valueIfTrue : valueIfFalse`

```java
int max = (a > b) ? a : b;
```

---

### Q17. Is Java pass-by-value or pass-by-reference?
**Answer:** **Always pass-by-value.** For objects, the *value of the reference* is copied — so you can modify the object's state through it, but reassigning the parameter doesn't affect the caller's reference.

---

### Q18. What is the difference between `++i` and `i++`?
**Answer:** `++i` increments **then** returns the value; `i++` returns the value **then** increments.

```java
int i = 5;
System.out.println(++i); // 6
int j = 5;
System.out.println(j++); // 5 (j is now 6)
```

---

### Q19. What are Java access modifiers?
**Answer:**
| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `private` | ✅ | ❌ | ❌ | ❌ |
| *default* | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

---

### Q20. What is garbage collection in Java?
**Answer:** Automatic memory management — the JVM removes objects that are **no longer reachable** from memory. You can't force it; `System.gc()` is only a *request*. This prevents most memory leaks and dangling-pointer bugs.

---

⬅️ [Back to Index](README.md) | ➡️ [Next: OOPs Concepts](02-oops-concepts.md)
