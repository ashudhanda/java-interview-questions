# 40 — Regex & String Formatting 🔣

Validation, parsing, text cleaning — regex har jagah kaam aata hai. Aur `String.format()` ka theek use interview me chhota par accha impression banata hai.

## Q1. Java me regex kaise use karte hain?
**Answer:** Do tareeke hain.

**Chhote kaam ke liye — `String` ke methods:**
```java
"abc123".matches("[a-z]+\\d+");        // true
"a,b;c".split("[,;]");                  // [a, b, c]
"hello world".replaceAll("o", "0");     // hell0 w0rld
```

**Baar-baar use ke liye — `Pattern` + `Matcher`:**
```java
Pattern p = Pattern.compile("\\d+");     // ek baar compile
Matcher m = p.matcher("a1b22c333");

while (m.find()) {
    System.out.println(m.group());       // 1, 22, 333
}
```

⚠️ **Performance point:** `str.matches()` har call pe pattern **dobara compile** karta hai. Loop ke andar hai to `Pattern.compile()` bahar nikaal ke `static final` bana do — ye real interview optimization question hai.

## Q2. Common regex symbols yaad rakhne layak
**Answer:**

| Symbol | Matlab |
|---|---|
| `.` | Koi bhi ek character |
| `\d` / `\D` | Digit / non-digit |
| `\w` / `\W` | Word char (`a-zA-Z0-9_`) / non-word |
| `\s` / `\S` | Whitespace / non-whitespace |
| `^` / `$` | String ka start / end |
| `*` | 0 ya zyada |
| `+` | 1 ya zyada |
| `?` | 0 ya 1 (optional) |
| `{n,m}` | n se m baar |
| `[abc]` | a, b, ya c me se koi ek |
| `[^abc]` | a, b, c ke alawa kuch bhi |
| `\|` | OR |
| `()` | Group (capture karta hai) |

⚠️ Java strings me **double backslash** lagta hai: regex `\d` ko Java me `"\\d"` likhna padta hai.

## Q3. `matches()` vs `find()` — bada trap hai
**Answer:**
- **`matches()`** — **poori string** pattern se match honi chahiye
- **`find()`** — string me **kahin bhi** match mil jaaye to bas

```java
Pattern p = Pattern.compile("\\d+");

p.matcher("abc123").matches();   // false — poori string digits nahi hai
p.matcher("abc123").find();      // true  — andar 123 mil gaya
```

👉 Validation ke liye `matches()`, extraction ke liye `find()`.

## Q4. Groups kaise use karte hain?
**Answer:** `()` se hissa capture karo, phir `group(n)` se nikaalo.

```java
Pattern p = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
Matcher m = p.matcher("Date: 2026-08-11");

if (m.find()) {
    m.group();    // "2026-08-11"  — group(0) = poora match
    m.group(1);   // "2026"
    m.group(2);   // "08"
    m.group(3);   // "11"
}
```

**Named groups** zyada readable hote hain:
```java
Pattern p = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})");
m.group("year");    // "2026"
```

## Q5. Greedy vs Lazy quantifier?
**Answer:** Ye classic confusion hai.

- **Greedy** (`*`, `+`) — jitna zyada ho sake utna khaa jaata hai
- **Lazy** (`*?`, `+?`) — jitna kam se kam chalega utna hi leta hai

```java
String html = "<b>bold</b> and <i>italic</i>";

"<.+>"   → "<b>bold</b> and <i>italic</i>"   // greedy — poora nigal gaya!
"<.+?>"  → "<b>"                             // lazy — pehla tag hi
```

👉 Tags ya quotes ke beech ka content nikalna ho to **lazy** chahiye.

## Q6. Kuch practical regex patterns
**Answer:**

```java
// Email (simple, practical)
"^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"

// Indian mobile (10 digit, 6-9 se start)
"^[6-9]\\d{9}$"

// Strong password: 8+, 1 upper, 1 lower, 1 digit, 1 special
"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$"

// Extra spaces hatao
text.replaceAll("\\s+", " ").trim();

// Sirf digits nikalo
text.replaceAll("[^0-9]", "");
```

`(?=...)` ko **lookahead** kehte hain — check karta hai par consume nahi karta. Password validation me isi se multiple conditions ek saath lagti hain.

⚠️ **Email regex ka sach:** 100% RFC-compliant email regex **bahut** complex hai. Real projects me simple regex + actual verification email bhejna hi sahi approach hai.

## Q7. `split()` ke edge cases?
**Answer:**

```java
"a,b,,".split(",");        // [a, b] — trailing khaali strings hat jaate hain!
"a,b,,".split(",", -1);    // [a, b, , ] — limit -1 se sab milte hain
"a.b.c".split(".");        // [] — '.' regex me "koi bhi char" hai!
"a.b.c".split("\\.");      // [a, b, c] — escape karna zaroori
```

👉 Ye do trap (trailing empty aur `.` escape) interview me directly poochhe jaate hain.

## Q8. `String.format()` kaise use karte hain?
**Answer:**

```java
String.format("Naam: %s, Umar: %d", "Ashu", 21);
String.format("%.2f", 3.14159);        // "3.14" — 2 decimal
String.format("%,d", 1234567);         // "1,234,567" — comma separator
String.format("%05d", 42);             // "00042" — zero padding
String.format("%-10s|", "hi");         // "hi        |" — left align
String.format("%10s|", "hi");          // "        hi|" — right align
String.format("%x", 255);              // "ff" — hexadecimal
String.format("%%");                    // "%" — literal percent
```

Java 15+ me instance method bhi hai:
```java
"Naam: %s".formatted("Ashu");
```

## Q9. `String.format()` vs concatenation vs `StringBuilder`?
**Answer:**
- **Concatenation (`+`)** — 2-3 strings ke liye theek. Compiler khud optimize kar deta hai.
- **`StringBuilder`** — **loop ke andar** hamesha yahi. `+` loop me `O(n²)` ban jaata hai kyunki har baar nayi String banti hai.
- **`String.format()`** — sabse **readable** jab template fix ho, par sabse **dheema** (pattern parse karta hai).

```java
// ❌ Loop me
for (String s : list) result += s;              // O(n²)

// ✅ Loop me
StringBuilder sb = new StringBuilder();
for (String s : list) sb.append(s);
String result = sb.toString();
```

## Q10. Text block (Java 15+) kya hai?
**Answer:** Multi-line strings bina `\n` aur escape ke.

```java
String json = """
        {
            "name": "Ashu",
            "role": "developer"
        }
        """;
```

Quotes escape karne ki zaroorat nahi — JSON, SQL, HTML likhne me bahut aaram.

---

> 💡 **Interview tip:** Regex likhne ke baad ye zaroor bolo — *"aur agar ye loop me chal raha hai to main `Pattern` ko `static final` compile kar leta hoon, kyunki `String.matches()` har call pe re-compile karta hai."* Ye performance awareness dikhata hai.
