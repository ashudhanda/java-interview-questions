# 63 — String Interning & Tricky Cases 🧵

Topic 03 me String basics the. Ye topic sirf **interning aur pool** ke tricky cases pe hai — interview me yahan sabse zyada log phaste hain.

## Q1. String Pool kya hai?
**Answer:** Heap ke andar ek special area jisme **unique string literals** store hote hain. Same literal dobara mile to **naya object nahi** banta — purane ka reference milta hai.

```java
String a = "hello";
String b = "hello";

System.out.println(a == b);        // true — dono SAME object point karte hain
System.out.println(a.equals(b));   // true
```

👉 **Memory bachane ke liye** — agar 1000 jagah `"hello"` likha hai to 1000 objects nahi, sirf **1** object hai.

## Q2. `new String("hello")` — kitne objects bante hain?
**Answer:** Classic trick question — **2 objects** (ya 1, agar pool me already ho).

```java
String s = new String("hello");
//            1. "hello" literal → String Pool me object (1)
//            2. new String(...)  → Heap me NEW object (2)
```

**Objects:**
1. `"hello"` — String Pool me (agar pehle se nahi tha)
2. `new String("hello")` — Heap me alag object

```java
String a = "hello";              // pool wala
String b = new String("hello");   // heap wala

System.out.println(a == b);        // ❌ FALSE — alag objects!
System.out.println(a.equals(b));   // ✅ true — content same
```

⚠️ **`==` reference compare karta hai** — content nahi. Isliye String comparison me hamesha `.equals()` use karo.

## Q3. `intern()` method kya karta hai?
**Answer:** Heap wale string ko **pool me daalta** hai (ya pool ka reference return karta hai).

```java
String a = new String("hello");   // heap me
String b = a.intern();            // pool wala reference
String c = "hello";               // pool wala

System.out.println(a == b);        // false — a abhi bhi heap me
System.out.println(b == c);        // ✅ TRUE — dono pool wale
```

👉 **`intern()` ke baad `==` true ho jaata hai** — kyunki ab dono pool ka same object point karte hain.

**Use case:** bohot saare duplicate strings memory me hain to `intern()` se memory bacha sakte ho. Par **dhyan se** — pool bhar jaata hai to `OutOfMemoryError` aa sakta hai.

## Q4. Compile-time vs Runtime string
**Answer:**

```java
String a = "hello";
String b = "hel" + "lo";          // compile-time concat → pool me
String c = "hel";
String d = c + "lo";              // runtime concat → heap me NEW object

System.out.println(a == b);        // ✅ TRUE — compiler ne fold kar diya
System.out.println(a == d);        // ❌ FALSE — runtime me naya object bana
System.out.println(a.equals(d));   // ✅ true — content same
```

👉 **Compiler constant folding:** `"hel" + "lo"` ko compile time pe `"hello"` bana deta hai. Par `c + "lo"` runtime pe hota hai — naya object banta hai.

⚠️ Ye **Java Language Specification** me defined hai — implementation detail nahi.

## Q5. String concatenation — kitne objects?
**Answer:**

```java
String s = "a" + "b" + "c";      // compile-time → 1 object ("abc")
```

```java
String s = "";
for (int i = 0; i < 3; i++) {
    s += i;                        // har iteration me NAYA object!
}
// Objects: "", "0", "01", "012" → 4 objects (puraane garbage)
```

👉 **Loop me `+=` = O(n²) + memory waste.** `StringBuilder` use karo:
```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 3; i++) sb.append(i);
String s = sb.toString();          // 1 object
```

## Q6. `String` vs `StringBuilder` vs `StringBuffer`
**Answer:**

| | `String` | `StringBuilder` | `StringBuffer` |
|---|---|---|---|
| Mutable | ❌ | ✅ | ✅ |
| Thread-safe | ✅ (immutable) | ❌ | ✅ (synchronized) |
| Speed | Slow (concat) | **Fast** ✅ | Slow (sync overhead) |
| Kab use | Read-only data | Single thread concat | Multi-thread concat |

👉 **90% cases me `StringBuilder`** — `StringBuffer` ki zaroorat bahut kam hai (multi-thread me string banana ho tab).

## Q7. `String` immutable kyun hai?
**Answer:** Interview me har baar poochte hain. 4 reasons:

1. **String Pool** — mutable hoti to ek change sabke references bigaad deta
2. **Security** — file paths, URLs, passwords String me hote hain; badal nahi sakte
3. **Thread safety** — immutable objects automatically thread-safe
4. **Caching hashcode** — `hashCode()` ek baar calculate, hamesha same rahega

```java
String s = "hello";
s.toUpperCase();                   // ❌ s change NAHI hua!
System.out.println(s);              // "hello" hi hai

s = s.toUpperCase();               // ✅ reassign karna padega
System.out.println(s);              // "HELLO"
```

⚠️ **String ke saare methods NEW string return karte hain** — original kabhi nahi badalta.

## Q8. Tricky `==` cases
**Answer:** Ye sab interview me poochhe jaate hain:

```java
String a = "hello";
String b = "hello";
String c = new String("hello");
String d = new String("hello");

a == b          // true  — same pool object
a == c          // false — pool vs heap
c == d          // false — do alag heap objects
a == c.intern() // true  — intern() pool wala deta hai

"hello" == "hel" + "lo"     // true  — compile-time concat
"hello" == "hel" + "lo".intern()  // N/A — compile error nahi, but check karo

String x = "ja" + "va";
String y = "java";
x == y          // true — compile-time fold

StringBuilder sb = new StringBuilder("ja");
String z = sb + "va";              // StringBuilder concat → heap
x == z          // false — z heap me hai
```

👉 **Pattern:** compile-time constant → pool. Runtime → heap. `intern()` → pool.

## Q9. `substring()` ka memory leak (Java 6)
**Answer:** Java 6 me `substring()` **poori original string** ka reference rakhta tha:

```java
String huge = "... 1GB string ...";
String small = huge.substring(0, 5);    // Java 6: poora 1GB memory me rehta tha!
```

**Java 7+ me fix** — `substring()` ab nayi array banata hai. Purana bug hai par interview me poochte hain "Java memory model ka knowledge hai kya" test karne ke liye.

## Q10. String ki useful methods — cheat sheet
**Answer:**

```java
"hello".length()                    // 5
"hello".charAt(0)                   // 'h'
"hello".substring(1, 3)            // "el" (start inclusive, end exclusive)
"hello".indexOf('l')                // 2
"hello".contains("ell")             // true
"hello".startsWith("he")            // true
"hello".toUpperCase()               // "HELLO"
"hello".trim()                       // whitespace hatao
"hello".replace('l', 'r')           // "herro"
"hello".split("")                   // ["h", "e", "l", "l", "o"]
"hello".isEmpty()                    // false
"hello".isBlank()                    // false (Java 11+)
String.join(", ", "a", "b")         // "a, b"
String.format("%s is %d", "age", 5) // "age is 5"
"hello".repeat(3)                    // "hellohellohello" (Java 11+)
"hello".chars()                      // IntStream
```

## Q11. Text Blocks (Java 15+)
**Answer:** Multi-line strings bina escape ke:

```java
// ❌ Pehle
String json = "{\n  \"name\": \"Ashu\",\n  \"age\": 22\n}";

// ✅ Java 15+
String json = """
    {
      "name": "Ashu",
      "age": 22
    }
    """;
```

👉 `\n` aur `\"` ki zaroorat nahi. SQL queries, JSON templates, HTML — sab me kaam aata hai.

---

> 💡 **Interview tip:** `==` wale questions me **hamesha pehle bolo** — *"ye pool me hai ya heap me?"* — phir jawab do. Compile-time vs runtime ka farak batana dikhata hai ki tum **Java ki internals** samajhte ho, sirf syntax nahi. Aur loop me `+=` wali galti **mat karna** — StringBuilder use karo, interviewer turant notice karta hai.
