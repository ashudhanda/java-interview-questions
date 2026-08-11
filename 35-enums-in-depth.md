# 35 — Enums in Depth 🏷️

Log samajhte hain enum bas constants ki list hai. Actually enum ek **poori class** hai — fields, constructor, methods, even abstract methods rakh sakta hai. Isi depth pe interview me marks milte hain.

## Q1. Enum kya hai?
**Answer:** Ek special class jiske **fixed, known instances** hote hain. Har constant actually us enum class ka ek `public static final` object hai.

```java
enum Day { MON, TUE, WED }
```

Andar se ye kuch aisa banta hai:
```java
final class Day extends Enum<Day> {
    public static final Day MON = new Day("MON", 0);
    ...
}
```

Isi wajah se enum kisi aur class ko **extend nahi kar sakta** — wo pehle se `java.lang.Enum` extend kar chuka hai. Par **interface implement kar sakta hai**.

## Q2. Enum me fields aur constructor kaise add karte hain?
**Answer:**

```java
enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    EARTH(5.976e+24, 6.37814e6);        // semicolon zaroori hai

    private final double mass;
    private final double radius;

    Planet(double mass, double radius) {   // constructor hamesha private
        this.mass = mass;
        this.radius = radius;
    }

    double surfaceGravity() {
        return 6.67300E-11 * mass / (radius * radius);
    }
}
```

⚠️ Enum constructor **kabhi `public` nahi** ho sakta — tum naya instance bana hi nahi sakte. Isliye enum **automatically singleton** hote hain.

## Q3. Enum ke built-in methods kaunse hain?
**Answer:**

```java
Day.MON.name()          // "MON"  — exact constant name
Day.MON.ordinal()       // 0      — declaration position
Day.valueOf("MON")      // Day.MON — galat naam pe IllegalArgumentException
Day.values()            // [MON, TUE, WED] — saare constants ka array
Day.MON.compareTo(Day.TUE)   // ordinal ke hisaab se
```

⚠️ **`ordinal()` ko kabhi business logic ya DB me store mat karo.** Kisi ne beech me naya constant daal diya to saare numbers shift ho jaayenge aur data corrupt. Explicit code field rakho:

```java
enum Status {
    ACTIVE(1), INACTIVE(2);
    private final int code;
    Status(int code) { this.code = code; }
    public int getCode() { return code; }
}
```

## Q4. Har constant ka apna alag behavior kaise dein?
**Answer:** **Constant-specific method** — ye advanced trick hai jo bahut kam log jaante hain.

```java
enum Operation {
    PLUS  { public int apply(int a, int b) { return a + b; } },
    MINUS { public int apply(int a, int b) { return a - b; } },
    TIMES { public int apply(int a, int b) { return a * b; } };

    public abstract int apply(int a, int b);
}

Operation.PLUS.apply(3, 4);   // 7
```

Isse bade `switch` statements poori tarah khatam ho jaate hain — ye OCP (Open/Closed Principle) ka clean example hai.

## Q5. Enum switch me kaise use hota hai?
**Answer:**

```java
switch (day) {
    case MON -> print("Hafte ki shuruaat");
    case WED -> print("Aadha hafta");
    default  -> print("Normal din");
}
```

👉 `case Day.MON` mat likho — sirf `case MON`. Type already pata hai compiler ko.

**Bonus:** enum switch me agar saare constants cover kar liye, to modern Java me `default` ki zaroorat nahi — aur naya constant add karne pe compiler **warning/error** de dega. Ye safety `if-else` me nahi milti.

## Q6. `EnumMap` aur `EnumSet` kya hain?
**Answer:** Enum keys ke liye **specially optimized** collections — normal `HashMap`/`HashSet` se kaafi fast.

```java
EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
schedule.put(Day.MON, "Gym");

EnumSet<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);
EnumSet<Day> weekdays = EnumSet.complementOf(weekend);
EnumSet<Day> all = EnumSet.allOf(Day.class);
```

**Kyun fast hain?**
- `EnumMap` andar se ek simple **array** hai, index = `ordinal()`. Koi hashing nahi, koi collision nahi.
- `EnumSet` ek **bit vector** hai — 64 tak constants ek single `long` me fit ho jaate hain. Union/intersection sirf bitwise operations hain.

👉 Enum key ho to `HashMap` ki jagah **hamesha `EnumMap`** use karo — ye ek accha interview point hai.

## Q7. Singleton banane ka best tareeka enum kyun hai?
**Answer:** Joshua Bloch (Effective Java) ne yahi recommend kiya hai.

```java
public enum DatabaseConnection {
    INSTANCE;

    public void query(String sql) { ... }
}

DatabaseConnection.INSTANCE.query("SELECT 1");
```

Fayde jo normal singleton me manually karne padte hain:
- **Thread-safe** — JVM class loading khud guarantee karta hai
- **Serialization-safe** — deserialize karne pe naya object nahi banta
- **Reflection-safe** — reflection se enum instantiate karna JVM level pe **block** hai

Normal double-checked singleton me ye teeno attacks manually handle karne padte hain.

## Q8. Enum interface implement kar sakta hai?
**Answer:** ✅ Haan. Extend nahi kar sakta, implement kar sakta hai.

```java
interface Describable { String describe(); }

enum Level implements Describable {
    LOW, HIGH;
    public String describe() { return "Level: " + name(); }
}
```

## Q9. Enum me `equals()` ya `==` — kya use karein?
**Answer:** Dono kaam karte hain, par **`==` better hai**:
- **Faster** — seedha reference comparison
- **NPE-safe** — `null == Day.MON` false deta hai, crash nahi. Jabki `day.equals(...)` pe `day` null hua to NPE.
- **Compile-time type check** — galat type compare karoge to compiler pakad lega, `equals()` chup-chaap `false` de dega

## Q10. Enum thread-safe hai?
**Answer:** Constants ka **creation** thread-safe hai (JVM class loading guarantee). Par agar tumne enum me **mutable field** daal diya, to wo thread-safe **nahi** rahega.

```java
enum Counter {
    INSTANCE;
    private int count = 0;          // ❌ mutable — race condition possible
    public void increment() { count++; }
}
```

👉 Enum ke fields hamesha `final` rakho.

---

> 💡 **Interview differentiator:** "enum constants ki list hai" bolne ke baad ye add karo — *"par enum me fields, constructor aur constant-specific methods bhi ho sakte hain, aur enum key ke liye `EnumMap` `HashMap` se tez hai kyunki wo andar se array hai."*
