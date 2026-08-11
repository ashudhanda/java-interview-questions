# 34 — Comparable vs Comparator (Sorting) 🔢

Custom objects ko sort karna — ye coding round me bhi aata hai aur theory round me bhi. Difference clear hona chahiye.

## Q1. `Comparable` kya hai?
**Answer:** Object ka **natural / default order** define karta hai. Class **khud** ye interface implement karti hai.

```java
class Student implements Comparable<Student> {
    String name;
    int marks;

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.marks, other.marks);   // marks se sort
    }
}

Collections.sort(students);   // ab seedha sort ho jaayega
```

Package: `java.lang` — method: `compareTo(T o)` — sirf **ek** ordering mil sakti hai.

## Q2. `Comparator` kya hai?
**Answer:** **Bahar se** diya gaya alag ordering logic. Class ko chhedna nahi padta, aur **kitne bhi** comparators bana sakte ho.

```java
Comparator<Student> byName = Comparator.comparing(s -> s.name);
Comparator<Student> byMarks = Comparator.comparingInt(s -> s.marks);

students.sort(byName);
students.sort(byMarks);
```

Package: `java.util` — method: `compare(T a, T b)`.

## Q3. Difference table
**Answer:**

| | `Comparable` | `Comparator` |
|---|---|---|
| Package | `java.lang` | `java.util` |
| Method | `compareTo(o)` | `compare(a, b)` |
| Kahan likha jaata hai | Class ke **andar** | Class ke **bahar** |
| Kitne ordering | Sirf 1 (natural) | Unlimited |
| Class modify karni padti? | ✅ Haan | ❌ Nahi |
| Kab use karo | Ek obvious default order ho (age, id) | Multiple / situational ordering |

👉 **Third-party class** (jiska code tumhare paas nahi) sort karna ho to `Comparator` hi ek option hai.

## Q4. `compareTo()` kya return karta hai?
**Answer:**
- **Negative** → `this` pehle aayega
- **Zero** → dono barabar
- **Positive** → `this` baad me aayega

⚠️ **Bada trap:** subtraction mat karo — integer overflow ho sakta hai.

```java
return this.marks - other.marks;          // ❌ overflow risk
return Integer.compare(this.marks, other.marks);   // ✅ hamesha safe
```

Agar `marks` bahut bada positive aur doosra bahut bada negative ho, to subtraction wrap hoke **galat sign** de dega.

## Q5. Multiple fields pe sort kaise karein?
**Answer:** `thenComparing()` chain karo — ye modern Java ka sabse kaam ka feature hai.

```java
students.sort(
    Comparator.comparingInt(Student::getMarks).reversed()   // marks descending
              .thenComparing(Student::getName)              // tie pe name ascending
              .thenComparing(Student::getId)                // phir bhi tie? id se
);
```

## Q6. Reverse order kaise karein?
**Answer:** Teen tareeke:

```java
comparator.reversed()                    // sabse clean
Comparator.reverseOrder()                // natural order ka ulta
Collections.reverseOrder(comparator)     // purana style
```

⚠️ `.reversed()` **poori chain** pe lagta hai agar aakhir me lagaya. Isliye placement dhyan se karo:

```java
// marks desc, phir name asc
Comparator.comparingInt(Student::getMarks).reversed().thenComparing(Student::getName)

// dono desc
Comparator.comparingInt(Student::getMarks).thenComparing(Student::getName).reversed()
```

## Q7. `null` values ke saath sort kaise karein?
**Answer:** Seedha sort karoge to `NullPointerException` aayega. `nullsFirst` / `nullsLast` use karo.

```java
Comparator<String> safe = Comparator.nullsLast(Comparator.naturalOrder());
list.sort(safe);   // null values end me chali jaayengi
```

## Q8. `Collections.sort()` vs `list.sort()` vs `Arrays.sort()`?
**Answer:**
- `Collections.sort(list)` — purana static method, andar se `list.sort(null)` hi call karta hai
- `list.sort(comparator)` — Java 8 se, **preferred** tareeka
- `Arrays.sort(array)` — arrays ke liye
- `stream().sorted(cmp).toList()` — original list ko chhede bina **nayi sorted list** deta hai

⚠️ Pehle teen **in-place** sort karte hain (original list badal jaati hai), stream wala nahi.

## Q9. Sorting algorithm kaunsa use hota hai andar?
**Answer:**
- **Objects** → **TimSort** (merge sort + insertion sort ka mix). **Stable** hai, worst case `O(n log n)`.
- **Primitives** (`int[]`, `double[]`) → **Dual-Pivot QuickSort**. **Stable nahi**, par fast aur extra memory nahi lagti.

👉 Primitives pe stability matter hi nahi karti — do equal `int` me farak hi kya hai. Isliye alag algorithm chuna gaya.

## Q10. "Stable sort" ka matlab kya hai?
**Answer:** Jo elements **barabar** hain, unka **aapsi order pehle jaisa hi rehta hai**.

```java
// Pehle name se sort kiya, phir marks se
list.sort(byName);
list.sort(byMarks);
// Stable hone ki wajah se: same marks wale ab bhi name order me hain
```

Isi property ki wajah se multi-level sorting alag-alag passes me bhi kaam kar jaati hai.

## Q11. `equals()` aur `compareTo()` consistent hone chahiye?
**Answer:** Strongly recommended hai: `a.compareTo(b) == 0` hone pe `a.equals(b)` bhi `true` hona chahiye.

Agar consistent nahi hai to **`TreeSet` aur `TreeMap` ajeeb behave karenge** — wo `equals()` nahi, `compareTo()` dekhte hain.

```java
// compareTo sirf marks dekhta hai
TreeSet<Student> set = new TreeSet<>();
set.add(new Student("Ashu", 90));
set.add(new Student("Ravi", 90));   // add hi nahi hoga — "duplicate" mana gaya!
System.out.println(set.size());     // 1
```

`HashSet` me dono aa jaate (wo `equals`/`hashCode` dekhta hai) — yahi inconsistency ka danger hai.

---

> 💡 **Interview one-liner:** *"`Comparable` natural order class ke andar deta hai, `Comparator` bahar se multiple orders. Aur `compareTo` me subtraction ki jagah `Integer.compare()` use karta hoon taaki overflow na ho."*
