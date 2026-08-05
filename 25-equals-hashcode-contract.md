# 25 — equals() & hashCode() Contract ⚖️

Chhota topic par interview me pakka aata hai — especially HashMap wale questions ke saath linked.

## Q1. What is the default behavior of `equals()` and `hashCode()`?
**Answer:** `Object` class se aata hai:
- `equals()` → **reference comparison** (`this == obj`) — same memory address?
- `hashCode()` → memory address based integer (typically)

```java
Student a = new Student(1, "Ashu");
Student b = new Student(1, "Ashu");
System.out.println(a.equals(b)); // false — alag objects!
```
Content compare karna ho to **override karna padega**.

## Q2. How do you correctly override `equals()`?
**Answer:** 5 rules (contract):
1. **Reflexive** — `x.equals(x)` = true
2. **Symmetric** — `x.equals(y)` ⇔ `y.equals(x)`
3. **Transitive** — x=y, y=z → x=z
4. **Consistent** — baar-baar same result
5. **Null** — `x.equals(null)` = false

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Student s = (Student) o;
    return id == s.id && Objects.equals(name, s.name);
}
```

## Q3. And `hashCode()`?
**Answer:** Same fields use karo jo `equals()` me use kiye:

```java
@Override
public int hashCode() {
    return Objects.hash(id, name);
}
```
⭐ **Golden rule:** `equals()` me jo fields participate karti hain, wahi `hashCode()` me bhi. Mismatch = HashMap/HashSet bugs.

## Q4. What is the equals–hashCode contract?
**Answer:**
- `a.equals(b) == true` → `a.hashCode() == b.hashCode()` **must** be true
- Equal hashCode → equals **zaroori nahi** (collisions allowed)
- Unequal hashCode → equals **must** be false

Yaad rakhne ka shortcut: *"Equal objects ⇒ same hash. Same hash ⇏ equal objects."*

## Q5. What breaks if you override `equals()` but not `hashCode()`?
**Answer:**
```java
Set<Student> set = new HashSet<>();
set.add(new Student(1, "Ashu"));
System.out.println(set.contains(new Student(1, "Ashu"))); // false! 😱
```
Dono objects equal hain par **different buckets** me gaye (different hashCode) → lookup fail. Interview me ye scenario bolke poochha jata hai.

## Q6. `==` vs `equals()` — final answer?
**Answer:**
- `==` → references compare (primitives ke liye values)
- `equals()` → content compare (agar overridden hai)

```java
String s1 = new String("hi");
String s2 = new String("hi");
s1 == s2      // false — alag objects
s1.equals(s2) // true — same content
```

## Q7. `Objects.equals(a, b)` kyun use karein?
**Answer:** **Null-safe** comparison — `a.equals(b)` me `a` null ho to NPE; `Objects.equals` dono null handle karta hai:
```java
Objects.equals(null, null) // true
Objects.equals(null, "x")  // false
```
Override likhte time ye cleaner aur safer hai.

## Q8. Should a HashMap key's fields be mutable?
**Answer:** ❌ Nahi! Key banane ke baad agar `equals`/`hashCode` me use hone wala field badla → object galat bucket me rahega → **entry lost** (get/remove fail). Keys hamesha immutable fields pe rakho — isliye `String`, `Integer` perfect keys hain.