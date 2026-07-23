# 🟡 Exception Handling — 10 Questions

### Q1. What is the exception hierarchy in Java?
**Answer:**
```
Throwable
├── Error            (OutOfMemoryError, StackOverflowError — don't catch)
└── Exception
    ├── RuntimeException (unchecked: NullPointerException, ArithmeticException...)
    └── others           (checked: IOException, SQLException...)
```

---

### Q2. Checked vs unchecked exceptions?
**Answer:**
- **Checked** — verified at **compile time**; must be caught or declared with `throws` (e.g. `IOException`).
- **Unchecked** — `RuntimeException` subclasses; compiler doesn't force handling (e.g. `NullPointerException`). Usually indicate programming bugs.

---

### Q3. Does `finally` always execute?
**Answer:** Almost always — even after `return` or an exception in try/catch. Exceptions: `System.exit()`, JVM crash, or infinite loop in try. Fun fact: a `return` in `finally` **overrides** the try's return value (bad practice!).

---

### Q4. What is try-with-resources?
**Answer:** Auto-closes resources implementing `AutoCloseable` — no `finally` boilerplate, no resource leaks:

```java
try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
    return br.readLine();
} // br.close() called automatically, even on exception
```

---

### Q5. `throw` vs `throws`?
**Answer:** `throw` — actually throws an exception object inside a method (`throw new IllegalArgumentException("bad")`). `throws` — declares in the method signature which checked exceptions may escape.

---

### Q6. How do you create a custom exception?
**Answer:**
```java
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
// extend RuntimeException instead for an unchecked version
```

---

### Q7. What is multi-catch?
**Answer:** Catch several exception types in one block (Java 7+):

```java
try { /* ... */ }
catch (IOException | SQLException e) {
    log.error("Data error", e);
}
```
> The types must not be in a parent-child relationship.

---

### Q8. Error vs Exception?
**Answer:** **Error** = serious JVM-level problem (OutOfMemoryError) — don't try to recover. **Exception** = application-level problem that can (and often should) be handled.

---

### Q9. Can we have try without catch?
**Answer:** Yes — `try-finally` and try-with-resources (which needs neither catch nor finally) are valid. A plain `try` alone is a compile error.

---

### Q10. Exception handling best practices?
**Answer:**
1. Catch specific exceptions, not blanket `Exception`.
2. Never swallow exceptions with empty catch blocks.
3. Use try-with-resources for I/O.
4. Don't use exceptions for normal control flow.
5. Preserve the cause: `throw new ServiceException("context", e);`

---

⬅️ [Prev: Collections](04-collections-framework.md) | ➡️ [Next: Multithreading](06-multithreading.md)
