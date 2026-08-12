# 57 — Clean Code & Java Best Practices ✨

Code chalna kaafi nahi hai — **padha jaana** bhi zaroori hai. Interview me code review round hota hai, aur job me tumhara 80% waqt dusron ka code padhne me jaata hai.

## Q1. Naming — sabse zaroori cheez
**Answer:** Achha naam comment ki zaroorat khatam kar deta hai.

```java
// ❌ Bura
int d;                          // days
List<int[]> list;
void proc(int a, int b) { }
boolean flag;

// ✅ Achha
int elapsedDays;
List<int[]> activeUserCoordinates;
void calculateDiscount(int price, int percentage) { }
boolean isEligibleForRefund;
```

**Rules:**

| Cheez | Convention | Example |
|---|---|---|
| Class | `PascalCase`, **noun** | `OrderService` |
| Method | `camelCase`, **verb** | `calculateTotal()` |
| Variable | `camelCase`, **noun** | `userName` |
| Constant | `UPPER_SNAKE` | `MAX_RETRY_COUNT` |
| Boolean | `is/has/can` se shuru | `isActive`, `hasPermission` |
| Package | `lowercase` | `com.app.service` |

👉 **Rule:** naam itna clear ho ki comment likhne ki zaroorat na pade. Naam lamba ho jaaye to chalega — `daysSinceLastLogin` `dsll` se hazaar guna behtar hai.

## Q2. Magic numbers kya hote hain?
**Answer:** Code me seedha likhe hue numbers jinka matlab pata nahi chalta.

```java
// ❌ Magic numbers
if (user.getAge() > 18 && order.getTotal() > 500) {
    applyDiscount(order, 0.15);
}

// ✅ Named constants
private static final int LEGAL_AGE = 18;
private static final double MIN_ORDER_FOR_DISCOUNT = 500.0;
private static final double DISCOUNT_RATE = 0.15;

if (user.getAge() > LEGAL_AGE && order.getTotal() > MIN_ORDER_FOR_DISCOUNT) {
    applyDiscount(order, DISCOUNT_RATE);
}
```

👉 **Do faayde:** (1) matlab saaf, (2) badalna ho to **ek jagah** badlo. `0.15` agar 5 jagah likha hai to ek bhoolne pe bug pakka.

## Q3. Method chhota kyun hona chahiye?
**Answer:** **Ek method = ek kaam** (Single Responsibility, topic 20).

```java
// ❌ 50 line ka method — sab kuch kar raha hai
void processOrder(Order order) {
    // validate
    if (order == null) throw ...;
    if (order.getItems().isEmpty()) throw ...;
    // calculate
    double total = 0;
    for (Item i : order.getItems()) total += i.getPrice() * i.getQty();
    // discount
    if (total > 500) total *= 0.85;
    // save
    db.save(order);
    // email
    emailService.send(...);
}

// ✅ Chhote methods
void processOrder(Order order) {
    validate(order);
    double total = calculateTotal(order);
    total = applyDiscount(total);
    save(order, total);
    sendConfirmation(order);
}
```

👉 **Test:** agar method ke andar `// validate`, `// calculate` jaise comments likhne pad rahe hain — wo hisse **alag method** hone chahiye. Comment ki jagah method ka naam kaam kar dega.

**Guideline:** method screen pe **ek saath dikhna** chahiye (~20 line max).

## Q4. Comments kab likhein?
**Answer:** Comment **"kya"** nahi, **"kyun"** batana chahiye.

```java
// ❌ Bekaar — code khud bata raha hai
i++;                        // i ko badhao
// user ka naam nikalo
String name = user.getName();

// ✅ Kaam ka — WHY bata raha hai
// Payment gateway 3 se zyada retry pe account block kar deta hai
private static final int MAX_RETRIES = 3;

// Legacy API dates ko IST me bhejta hai, isliye UTC me convert kar rahe hain
date = convertToUtc(date);
```

⚠️ **Sabse bura comment = purana comment.** Code badal gaya, comment nahi — ab wo **jhooth** bol raha hai aur galat direction deta hai. Isliye kam comment likho, achhe naam zyada.

## Q5. Nesting kam kaise karein?
**Answer:** **Guard clauses** — ye trick code turant saaf kar deti hai.

```java
// ❌ Deep nesting — "arrow code"
void process(User user) {
    if (user != null) {
        if (user.isActive()) {
            if (user.hasPermission()) {
                // asli kaam yahan, 3 level andar
                doWork(user);
            }
        }
    }
}

// ✅ Guard clauses — pehle nikal jao
void process(User user) {
    if (user == null) return;
    if (!user.isActive()) return;
    if (!user.hasPermission()) return;

    doWork(user);              // main logic, zero indentation
}
```

👉 **Rule:** exceptional cases pehle handle karo aur **jaldi return** karo. Main logic hamesha sabse kam indentation pe rahe.

## Q6. Null handling ki best practice
**Answer:** `null` return karna dusron ke liye bug banata hai.

```java
// ❌ null return
List<Order> getOrders(String userId) {
    if (notFound) return null;      // caller NPE khayega
    ...
}

// ✅ Empty collection return karo
List<Order> getOrders(String userId) {
    if (notFound) return Collections.emptyList();
    ...
}

// ✅ Single object ke liye Optional (topic 32)
Optional<User> findById(String id) {
    return Optional.ofNullable(user);
}
```

👉 **Empty list** return karne se caller seedha loop chala sakta hai — null check ki zaroorat hi nahi.

⚠️ **`Optional` field ya parameter me mat use karo** — wo sirf **return type** ke liye design hua hai.

## Q7. Exception handling ki galtiyan
**Answer:** Ye teen galtiyan sabse zyada hoti hain:

```java
// ❌ 1. Exception nigalna — sabse bada crime
try {
    riskyOp();
} catch (Exception e) {
    // kuch nahi — error gayab, debug karna namumkin
}

// ❌ 2. Generic Exception catch karna
try {
    ...
} catch (Exception e) { }        // NPE, IOException sab ek saath

// ❌ 3. Flow control ke liye exception
try {
    return list.get(index);
} catch (IndexOutOfBoundsException e) {
    return null;                  // if se check karo na!
}

// ✅ Sahi tareeka
try {
    riskyOp();
} catch (IOException e) {
    log.error("File read fail: {}", path, e);
    throw new DataAccessException("Config load nahi hua", e);   // cause rakho
}
```

⚠️ **Original exception ko `cause` me zaroor pass karo** — warna asli stack trace kho jaata hai (topic 39, 55).

## Q8. Immutability prefer karo
**Answer:** Jo badal nahi sakta, wo bug nahi de sakta (topic 33).

```java
// ✅ final use karo jahan possible ho
private final String name;
final List<Item> items = getItems();

// ✅ Java 16+ me record — automatically immutable
public record Point(int x, int y) { }

// ✅ Collection return karte waqt copy ya unmodifiable
public List<Item> getItems() {
    return Collections.unmodifiableList(items);
}
```

👉 **`final` ka faayda:** compiler galti se reassign hone se rokta hai, aur reader ko turant pata chal jaata hai ki ye value badlegi nahi.

## Q9. DRY, KISS, YAGNI — ye kya hain?
**Answer:** Teen famous principles:

| Principle | Full form | Matlab |
|---|---|---|
| **DRY** | Don't Repeat Yourself | Same code copy-paste mat karo, method banao |
| **KISS** | Keep It Simple, Stupid | Simple solution hi best hai |
| **YAGNI** | You Aren't Gonna Need It | "Future me kaam aayega" wala code mat likho |

⚠️ **DRY ka overdose bhi bura hai.** Do cheezein **dikhne me** same hain par **matlab alag** hai, to unhe merge mat karo — kal alag-alag badalni padengi.

👉 **Rule of three:** ek baar likho, dusri baar copy chalega, **teesri baar** aaye to refactor karo.

## Q10. Common Java galtiyan
**Answer:** Ye har code review me pakdi jaati hain:

```java
// ❌ String comparison == se
if (name == "Ashu")              // reference compare! (topic 03)
// ✅
if ("Ashu".equals(name))         // null-safe bhi hai

// ❌ Loop me string concat
String s = "";
for (...) s += item;             // O(n²)
// ✅
StringBuilder sb = new StringBuilder();
for (...) sb.append(item);

// ❌ float/double se paisa
double price = 0.1 + 0.2;        // 0.30000000000000004
// ✅
BigDecimal price = new BigDecimal("0.1").add(new BigDecimal("0.2"));

// ❌ Resource band nahi kiya
FileReader fr = new FileReader(f);
// ✅ try-with-resources (topic 12)
try (FileReader fr = new FileReader(f)) { ... }

// ❌ Interface ki jagah implementation
ArrayList<String> list = new ArrayList<>();
// ✅
List<String> list = new ArrayList<>();
```

## Q11. Code review me kya dekha jaata hai?
**Answer:** Ye checklist khud pe bhi apply karo:

- [ ] Naam clear hain?
- [ ] Method chhote aur single-purpose hain?
- [ ] Magic numbers to nahi?
- [ ] Duplicate code to nahi?
- [ ] Exceptions theek se handle hue?
- [ ] Null cases socha?
- [ ] Edge cases — empty list, zero, negative?
- [ ] Test likhe?
- [ ] Sensitive data log me to nahi ja raha?
- [ ] Complexity theek hai ya optimize ho sakta hai?

## Q12. Ek chhota refactoring example
**Answer:** Sab principles ek saath:

```java
// ❌ Pehle
public double calc(List<int[]> l) {
    double t = 0;
    for (int i = 0; i < l.size(); i++) {
        if (l.get(i) != null) {
            if (l.get(i)[1] > 0) {
                t += l.get(i)[0] * l.get(i)[1];
            }
        }
    }
    if (t > 500) { t = t - (t * 0.15); }
    return t;
}

// ✅ Baad me
private static final double DISCOUNT_THRESHOLD = 500.0;
private static final double DISCOUNT_RATE = 0.15;

public double calculateOrderTotal(List<OrderItem> items) {
    double subtotal = items.stream()
            .filter(Objects::nonNull)
            .filter(item -> item.getQuantity() > 0)
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();

    return applyDiscount(subtotal);
}

private double applyDiscount(double amount) {
    if (amount <= DISCOUNT_THRESHOLD) return amount;
    return amount * (1 - DISCOUNT_RATE);
}
```

**Kya-kya theek hua:** naam clear hue, `int[]` ki jagah proper class, magic numbers constants bane, nesting khatam, discount logic alag method me, streams se readable.

---

> 💡 **Interview tip:** coding round me **sirf sahi answer kaafi nahi** — clean code likhne wale ko interviewer alag note karta hai. Do chhoti aadatein turant farak dikhati hain: (1) variable ka naam `a`, `x`, `temp` ki jagah **matlab wala** rakho, (2) code likhne ke baad khud bolo — *"ise main is tarah refactor kar sakta hoon"*. Ye dikhata hai ki tum production code likhne layak ho, sirf puzzle solve karne layak nahi.
