# 33 — Immutability & Defensive Copying 🔒

"Immutable class kaise banate ho?" — ye question har 2nd interview me aata hai. Aur zyadatar log **defensive copy** wala part bhool jaate hain, wahi asli marking point hai.

## Q1. Immutable object kya hota hai?
**Answer:** Aisa object jiski state **banne ke baad kabhi change nahi hoti**. Ek baar bana, hamesha wahi rahega.

Examples JDK me: `String`, `Integer` aur saare wrapper classes, `LocalDate`, `LocalDateTime`, `BigDecimal`, `UUID`.

## Q2. Immutable class banane ke rules kya hain?
**Answer:** Paanch rules hain — interview me ginwa dena:

1. Class ko **`final`** banao → koi subclass banake behavior na todhe
2. Saare fields **`private final`** rakho
3. **Koi setter mat do**
4. Constructor me aane wale **mutable objects ki copy** banao (defensive copy in)
5. Getter se mutable field ka **direct reference mat lautao** — copy lautao (defensive copy out)

Points 4 aur 5 hi wo hain jo log miss karte hain.

## Q3. Poora example dikhao
**Answer:**

```java
public final class Student {                  // 1. final class
    private final String name;                // 2. private final
    private final Date joinDate;              // mutable type!
    private final List<String> subjects;      // mutable type!

    public Student(String name, Date joinDate, List<String> subjects) {
        this.name = name;                     // String immutable hai, safe
        this.joinDate = new Date(joinDate.getTime());        // 4. copy IN
        this.subjects = new ArrayList<>(subjects);           // 4. copy IN
    }

    public String getName() { return name; }

    public Date getJoinDate() {
        return new Date(joinDate.getTime());                 // 5. copy OUT
    }

    public List<String> getSubjects() {
        return Collections.unmodifiableList(subjects);       // 5. safe view
    }
    // 3. koi setter nahi
}
```

## Q4. Defensive copy na karein to kya toot jaata hai?
**Answer:** Bahar se object ki state badal di jaayegi — immutability ka naam hi jhooth ho jaayega.

```java
List<String> subs = new ArrayList<>(List.of("Java"));
Student s = new Student("Ashu", new Date(), subs);

subs.add("Python");   // 💥 copy nahi ki to student ke andar bhi add ho gaya!

s.getSubjects().add("C++");   // 💥 getter ne direct reference diya to yahan se bhi
```

Dono jagah copy karne se dono attacks band ho jaate hain.

## Q5. `final` field ka matlab object bhi immutable hai?
**Answer:** ❌ **Nahi.** Ye sabse bada confusion hai.

`final` sirf itna kehta hai ki **reference dobara assign nahi hoga**. Jis object ko wo point kar raha hai, wo andar se badal sakta hai.

```java
final List<String> list = new ArrayList<>();
list.add("hello");          // ✅ bilkul allowed — object badla
list = new ArrayList<>();   // ❌ compile error — reference badla
```

## Q6. Immutability ke fayde kya hain?
**Answer:**
- **Thread-safe by default** — state badalti hi nahi, to race condition ka sawaal hi nahi. Koi lock nahi chahiye.
- **HashMap key ke liye safe** — hashCode kabhi badlega nahi (agla question dekho)
- **Caching aasan** — value badal nahi sakti to bindaas cache karo
- **Debugging simple** — "ye value kisne badli?" wala sawaal hi nahi banta
- **Failure atomicity** — aadha-adhoora updated object kabhi nahi milega

## Q7. Mutable object ko HashMap key banane pe kya hota hai?
**Answer:** Object **kho jaata hai** map ke andar. Ye classic interview demo hai.

```java
List<String> key = new ArrayList<>(List.of("a"));
Map<List<String>, String> map = new HashMap<>();
map.put(key, "value");

key.add("b");                 // hashCode badal gaya!

map.get(key);                 // null — galat bucket me dhoond raha hai
map.containsKey(key);         // false — par entry andar padi hai!
```

Isi wajah se `String` ko immutable banaya gaya — taaki wo perfect HashMap key ban sake.

## Q8. `record` (Java 16+) se kaam aasan hota hai?
**Answer:** Haan, kaafi. `record` automatically `private final` fields, constructor, getters, `equals()`, `hashCode()`, `toString()` de deta hai.

```java
public record Student(String name, List<String> subjects) { }
```

⚠️ **Par record "shallow" immutable hai.** Andar wali `List` phir bhi mutable hai! Poori safety ke liye compact constructor me copy karo:

```java
public record Student(String name, List<String> subjects) {
    public Student {
        subjects = List.copyOf(subjects);   // ab andar wali list bhi safe
    }
}
```

## Q9. `Collections.unmodifiableList()` vs `List.copyOf()`?
**Answer:**
- `Collections.unmodifiableList(list)` — ek **view** deta hai. Original list badli to view me bhi dikhega. Sirf modification block karta hai.
- `List.copyOf(list)` — ek **naya independent snapshot** banata hai. Original badalne se koi farak nahi padta.

Getter me sabse safe: `List.copyOf(subjects)`.

## Q10. Immutability ka nuksaan kya hai?
**Answer:** Har chhote change pe **naya object** banta hai, to memory aur GC pressure badh sakta hai. Isi wajah se loop me `String` concatenate karna slow hai — wahan `StringBuilder` (mutable) use karte hain.

👉 Balanced answer: *"Default me immutable banao, aur performance ka proven problem ho tabhi mutable pe jao."*

---

> 💡 **Interview me differentiator:** paanch rules bolne ke baad ye add karo — *"aur mutable fields pe constructor aur getter dono me defensive copy karni padti hai, warna immutability sirf kaagaz pe rehti hai."*
