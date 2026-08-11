# 39 — Custom Exceptions & Best Practices ⚠️

Exception handling ki basics topic 05 me hain. Ye topic un galtiyon pe hai jo real projects me hoti hain aur senior interviewer specifically poochta hai.

## Q1. Custom exception kaise banate hain?
**Answer:** `Exception` (checked) ya `RuntimeException` (unchecked) extend karo.

```java
// Unchecked — programming error ya recover na ho paane wali situation
public class UserNotFoundException extends RuntimeException {
    private final int userId;

    public UserNotFoundException(int userId) {
        super("User nahi mila, id: " + userId);
        this.userId = userId;
    }

    public int getUserId() { return userId; }
}
```

👉 **Extra field rakhna accha practice hai** — caller ko sirf message parse karke ID nikalni na pade.

## Q2. Custom exception checked banayein ya unchecked?
**Answer:** Decision rule simple hai:

- **Checked** — caller **recover kar sakta hai** aur usse handle karna hi chahiye. Example: `FileNotFoundException` → dusri file try kar sakte ho.
- **Unchecked** — programming bug hai ya recovery possible nahi. Example: `IllegalArgumentException`.

👉 **Modern practice:** zyadatar log **unchecked** prefer karte hain. Spring ka poora exception hierarchy unchecked hai. Wajah: checked exceptions method signatures ko gandha kar dete hain aur log unhe khali `catch` me daba dete hain.

## Q3. Sabse badi exception handling galti kya hai?
**Answer:** **Exception swallow karna** — pakad ke kuch na karna.

```java
// ❌ Sabse bada crime
try {
    riskyOperation();
} catch (Exception e) {
    // kuch nahi
}

// ❌ Ye bhi utna hi bura
catch (Exception e) {
    e.printStackTrace();   // production me kahin log hi nahi hota
}
```

Bug chup-chaap chhup jaata hai aur ghanton debug karna padta hai.

```java
// ✅ Sahi
catch (SQLException e) {
    log.error("User save nahi hua, id={}", id, e);
    throw new DataAccessException("User save failed", e);
}
```

## Q4. Exception chaining kya hai aur kyun zaroori hai?
**Answer:** Original exception ko **cause** ke roop me pass karna — taaki asli root cause stack trace me dikhe.

```java
// ❌ Original cause kho gaya — debugging impossible
catch (SQLException e) {
    throw new ServiceException("Save failed");
}

// ✅ Cause preserved
catch (SQLException e) {
    throw new ServiceException("Save failed", e);   // 'e' pass kiya
}
```

Dusre case me log me `Caused by: java.sql.SQLException: ...` dikhega — wahi asli problem batata hai.

Isliye custom exception me **hamesha** ye constructor rakho:
```java
public ServiceException(String message, Throwable cause) {
    super(message, cause);
}
```

## Q5. `catch (Exception e)` kyun bura hai?
**Answer:** Wo sab kuch pakad leta hai — jo tumne socha bhi nahi tha, aur jo tumhe handle nahi karna chahiye tha.

```java
catch (Exception e)   // NullPointerException bhi pakad lega — wo to bug hai!
```

✅ **Specific exceptions pakdo:**
```java
catch (SQLException | IOException e) {   // multi-catch (Java 7+)
    log.error("External resource fail hua", e);
}
```

❌ **`catch (Throwable t)` to bilkul mat karo** — wo `OutOfMemoryError` aur `StackOverflowError` bhi pakad lega, jinse recover karna possible hi nahi.

## Q6. `finally` block me `return` karne pe kya hota hai?
**Answer:** 💥 `try` ka `return` **overwrite ho jaata hai** — aur exception bhi **nigal** jaata hai.

```java
int test() {
    try {
        throw new RuntimeException("boom");
    } finally {
        return 42;      // ❌ exception gayab! method 42 return karega
    }
}
```

👉 **Rule:** `finally` me kabhi `return`, `break`, ya `throw` mat likho. Sirf cleanup karo.

## Q7. try-with-resources kya hai?
**Answer:** Resources ko **automatically band** karta hai — `finally` likhne ki zaroorat hi nahi.

```java
// ❌ Purana tareeka — lamba aur bug-prone
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("a.txt"));
    return br.readLine();
} finally {
    if (br != null) br.close();   // close() khud exception phenk sakta hai!
}

// ✅ try-with-resources
try (BufferedReader br = new BufferedReader(new FileReader("a.txt"))) {
    return br.readLine();
}   // automatically close ho gaya
```

Koi bhi class jo **`AutoCloseable`** implement karti hai, yahan chal sakti hai. Multiple resources semicolon se separate karo — wo **reverse order** me band hote hain.

## Q8. "Suppressed exception" kya hota hai?
**Answer:** Ye advanced point hai. Agar `try` block me exception aaya **aur** `close()` bhi exception phenke, to purane tareeke me `close()` wala exception original ko **chhupa deta tha**.

try-with-resources me original exception hi aage jaata hai, aur `close()` wala **suppressed** list me attach ho jaata hai:

```java
catch (Exception e) {
    for (Throwable sup : e.getSuppressed()) {
        log.warn("Close karte waqt bhi error: ", sup);
    }
}
```

## Q9. Exception handling ke aur best practices?
**Answer:**
- **Fail fast** — method ke shuru me hi validate karo
  ```java
  Objects.requireNonNull(user, "user null nahi ho sakta");
  if (age < 0) throw new IllegalArgumentException("age negative: " + age);
  ```
- **Message me context daalo** — `"User nahi mila"` se behtar `"User nahi mila, id=42"`
- **Flow control ke liye exception mat use karo** — exception banana mehnga hai (stack trace fill hota hai). Loop tod-ne ke liye `if` use karo.
- **Exception me sensitive data mat daalo** — password, token log me chala jaayega
- **Ek hi jagah handle karo** — Spring me `@ControllerAdvice` + `@ExceptionHandler`
- **Standard exceptions prefer karo** — `IllegalArgumentException`, `IllegalStateException` already exist karte hain, unke liye naya mat banao

## Q10. `throw` vs `throws` — quick revision
**Answer:**
- **`throw`** — actually exception phenkna, method ke **andar**. `throw new RuntimeException()`
- **`throws`** — method **signature** me declare karna ki ye exception aa sakta hai. `void read() throws IOException`

---

> 💡 **Interview me ye bolo:** *"Main exception kabhi swallow nahi karta, aur wrap karte waqt hamesha original cause pass karta hoon — warna root cause stack trace se gayab ho jaata hai aur debugging namumkin ho jaati hai."*
