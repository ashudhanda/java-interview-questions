# 32 — Optional & Null Handling 🛡️

`NullPointerException` Java ka sabse common crash hai. Iska naam hi "**The Billion Dollar Mistake**" pad gaya hai. Ye topic interview me Java 8 round me aata hai.

## Q1. `Optional` kya hai aur kyun aaya?
**Answer:** `Optional<T>` ek **container** hai jo ya to ek value rakhta hai ya khaali hota hai. Iska maqsad hai method ke **return type me hi bata dena** ki "value ho bhi sakti hai, nahi bhi".

Pehle:
```java
User findUser(int id) { ... }   // null aa sakta hai? pata nahi!
findUser(5).getName();          // 💥 NPE ka risk
```

Ab:
```java
Optional<User> findUser(int id) { ... }   // signature khud bata raha hai
```

Caller ko **majboor** karta hai ki empty case ke baare me soche.

## Q2. `Optional` banane ke tareeke?
**Answer:**

```java
Optional.of(value)              // value NULL nahi honi chahiye, warna NPE
Optional.ofNullable(value)      // null ho to empty Optional milega
Optional.empty()                // khaali Optional
```

⚠️ **Trap:** `Optional.of(null)` khud `NullPointerException` phenk deta hai. Jab pakka na ho to hamesha `ofNullable()` use karo.

## Q3. Value nikalne ka sahi tareeka kya hai?
**Answer:**

```java
opt.orElse("default")                      // default value
opt.orElseGet(() -> computeDefault())      // default lazily banega
opt.orElseThrow(() -> new NotFoundException())   // exception phenko
opt.ifPresent(v -> print(v))               // value ho to hi chalega
opt.ifPresentOrElse(v -> print(v), () -> print("nahi mila"))  // Java 9+
```

❌ **`opt.get()` kabhi seedha mat use karo** — empty hone pe `NoSuchElementException` deta hai. Wahi purana NPE, bas naye naam se.

## Q4. `orElse()` vs `orElseGet()` — ye trap question hai
**Answer:** `orElse()` ka argument **hamesha evaluate hota hai**, chahe value present ho ya na ho. `orElseGet()` ka lambda **sirf tab chalta hai jab Optional empty ho**.

```java
Optional<String> name = Optional.of("Ashu");

name.orElse(expensiveCall());        // expensiveCall() CHALEGA (waste!)
name.orElseGet(() -> expensiveCall());  // NAHI chalega — value present hai
```

👉 Rule: default value **already ready** hai → `orElse()`. Default **banane me cost** lagti hai (DB call, object creation) → `orElseGet()`.

## Q5. `map()` aur `flatMap()` Optional me?
**Answer:** Nested null checks ki chain ko ek line me badal dete hain.

```java
// Purana tareeka — pyramid of doom
if (user != null) {
    Address addr = user.getAddress();
    if (addr != null) {
        City city = addr.getCity();
        if (city != null) return city.getName();
    }
}
return "Unknown";

// Optional ke saath
return Optional.ofNullable(user)
        .map(User::getAddress)
        .map(Address::getCity)
        .map(City::getName)
        .orElse("Unknown");
```

`flatMap()` tab use karo jab tumhara method khud `Optional` return kare — warna `Optional<Optional<T>>` ban jaayega.

## Q6. `filter()` ka use?
**Answer:** Condition pass na ho to Optional empty ban jaata hai.

```java
Optional.ofNullable(user)
        .filter(u -> u.getAge() >= 18)
        .map(User::getName)
        .orElse("Minor ya user hi nahi");
```

## Q7. `Optional` kahan use NAHI karna chahiye?
**Answer:** Ye bahut poocha jaata hai, kyunki log `Optional` ko har jagah thok dete hain.

❌ **Field me** — `private Optional<String> name;` galat hai. `Optional` `Serializable` nahi hai, aur har object pe extra wrapper ka memory cost lagta hai.
❌ **Method parameter me** — `void save(Optional<User> u)` galat. Caller ko `Optional.of(...)` wrap karna pade, ye bekaar hai. Method overload karo.
❌ **Constructor me** — same reason.
❌ **Collection me** — `List<Optional<String>>` ki jagah khaali list return karo.

✅ **Sirf return type ke liye** — yahi iska asli design purpose hai.

## Q8. Collection return karte waqt `Optional` chahiye?
**Answer:** ❌ Nahi. Empty collection **khud hi** "kuch nahi mila" bata deta hai.

```java
// Galat
Optional<List<User>> getUsers();

// Sahi
List<User> getUsers();   // kuch na mile to Collections.emptyList()
```

## Q9. `Optional` ke bina NPE se bachne ke aur tareeke?
**Answer:**
- **`Objects.requireNonNull(x, "x null nahi ho sakta")`** — constructor me fail-fast validation
- **`Objects.equals(a, b)`** — dono null ho to bhi safe
- **`Objects.requireNonNullElse(x, default)`** — Java 9+
- **Yoda condition** — `"ADMIN".equals(role)` likho, `role.equals("ADMIN")` nahi. `role` null ho to bhi crash nahi hoga.
- **Empty collection return karo, null nahi**
- **`@NonNull` / `@Nullable` annotations** — IDE aur static analysis warning de deta hai

## Q10. Optional ko stream ki tarah use kar sakte hain?
**Answer:** Haan, Java 9 se `Optional.stream()` aa gaya — empty Optionals ko filter karne ke liye best hai.

```java
List<User> found = ids.stream()
        .map(this::findUser)     // Stream<Optional<User>>
        .flatMap(Optional::stream)  // empty automatically hat gaye
        .toList();
```

---

> 💡 **Interview me ek line:** *"`Optional` sirf return type ke liye banaya gaya tha — fields aur parameters me use karna anti-pattern hai. Aur `orElse` hamesha evaluate hota hai, isliye costly default ke liye `orElseGet` chahiye."*
