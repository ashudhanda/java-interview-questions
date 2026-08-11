# 36 — Inner & Anonymous Classes 🏠

Nested classes ke 4 types hain aur log inme confuse ho jaate hain. "Static nested vs inner ka difference?" — ye pakka wala question hai.

## Q1. Nested classes ke kitne types hain?
**Answer:** Chaar:

| Type | Kahan | `static`? | Outer instance chahiye? |
|---|---|---|---|
| **Static nested** | Class ke andar | ✅ | ❌ Nahi |
| **Inner (non-static)** | Class ke andar | ❌ | ✅ Haan |
| **Local** | Method ke andar | — | Method pe depend |
| **Anonymous** | Expression me, bina naam | — | — |

## Q2. Static nested vs Inner class — asli difference?
**Answer:** **Inner class outer object ka hidden reference rakhti hai, static nested nahi rakhti.** Bas yahi core difference hai, baaki sab isi se nikalta hai.

```java
class Outer {
    private int x = 10;

    static class StaticNested {
        // x access NAHI kar sakta — koi outer instance hi nahi
        void show() { System.out.println("static nested"); }
    }

    class Inner {
        void show() { System.out.println(x); }   // ✅ outer ka private bhi access
    }
}
```

Banane ka tareeka bhi alag hai:
```java
Outer.StaticNested a = new Outer.StaticNested();      // outer object nahi chahiye

Outer outer = new Outer();
Outer.Inner b = outer.new Inner();                    // ajeeb syntax, par yahi hai
```

## Q3. Inner class memory leak kaise karti hai?
**Answer:** Ye ek **bahut accha interview point** hai. Inner class ke paas outer object ka **implicit strong reference** hota hai. Agar inner class ka object lamba jeeta hai (listener, static map, background thread), to **poora outer object bhi GC nahi ho paata**.

```java
class BigActivity {
    byte[] hugeData = new byte[10_000_000];

    class Listener { }   // inner — BigActivity ko zinda rakhega
}

staticRegistry.add(outer.new Listener());   // 💥 10MB kabhi free nahi hoga
```

**Fix:** agar outer ki state ki zaroorat nahi, to class ko **`static` nested** bana do. Android me ye classic leak hai.

👉 Rule: **default me `static` nested banao.** Non-static tabhi jab outer instance genuinely chahiye.

## Q4. Anonymous class kya hai?
**Answer:** Bina naam ki class jo **wahin declare aur instantiate** ho jaati hai. Ek hi baar use hone wale implementation ke liye.

```java
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("chal raha hai");
    }
};
```

Compiler iske liye `Outer$1.class` naam ki file banata hai — isliye `$1`, `$2` wale class files dikhte hain.

## Q5. Anonymous class vs Lambda — kab kya?
**Answer:**

```java
// Anonymous class
Runnable a = new Runnable() {
    public void run() { System.out.println(this); }   // 'this' = anonymous object
};

// Lambda
Runnable b = () -> System.out.println(this);          // 'this' = ENCLOSING object
```

| | Anonymous class | Lambda |
|---|---|---|
| Methods | Kitne bhi | Sirf 1 (functional interface) |
| `this` ka matlab | Khud anonymous object | **Bahar wali class** |
| State (fields) rakh sakta? | ✅ | ❌ |
| Abstract class implement? | ✅ | ❌ Sirf interface |
| `.class` file | Banti hai | Nahi banti (`invokedynamic`) |

👉 **`this` wala difference** sabse zyada poocha jaata hai. Lambda apna scope nahi banata.

✅ Functional interface hai → **lambda** use karo (chhota, fast). Multiple methods ya abstract class → **anonymous class**.

## Q6. Local class kya hoti hai?
**Answer:** Method ke **andar** define ki gayi named class. Kam use hoti hai, par exist karti hai.

```java
void process() {
    class Validator {           // sirf is method ke andar visible
        boolean isValid(String s) { return s != null && !s.isBlank(); }
    }
    Validator v = new Validator();
}
```

## Q7. Inner/anonymous class local variable use kar sakti hai?
**Answer:** Haan, par variable **`final` ya "effectively final"** hona chahiye — yaani banne ke baad kabhi reassign na ho.

```java
void demo() {
    int count = 5;              // effectively final — kabhi badla nahi
    Runnable r = () -> System.out.println(count);   // ✅

    int total = 0;
    total = 10;                 // reassign kar diya
    Runnable r2 = () -> System.out.println(total);  // ❌ compile error
}
```

**Kyun ye rule hai?** Local variable **stack** pe rehta hai aur method khatam hote hi mit jaata hai. Par inner class object **heap** pe hai aur baad me bhi chal sakta hai. Isliye Java us value ki **copy** bana leta hai. Agar variable badalta rehta, to copy aur original alag ho jaate — confusing bug. Isliye badalna hi allowed nahi.

👉 **Workaround:** value badalni hai to array ya `AtomicInteger` use karo — reference same rehta hai, andar ki value badalti hai.
```java
int[] counter = {0};
Runnable r = () -> counter[0]++;   // ✅ chalega
```

## Q8. Inner class ke fayde kya hain?
**Answer:**
- **Encapsulation** — helper class ko bahar expose karne ki zaroorat nahi
- **Logical grouping** — `Map.Entry` ka `Map` ke andar hona logical hai
- **Readability** — chhoti helper ke liye alag file banane se bachte ho
- Outer class ke **private members** tak seedha access (inner class ko)

## Q9. Real JDK example do
**Answer:** `HashMap` ke andar `Node` ek **static nested** class hai:

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```

`static` kyun? Kyunki har `Node` ko `HashMap` object ka reference rakhne ki **zaroorat nahi** hai. Lakhon nodes hote hain — har ek me extra reference rakhna memory ki barbaadi hoti. Yahi design lesson hai.

---

> 💡 **Interview one-liner:** *"Inner class outer instance ka reference rakhti hai, static nested nahi — isliye default me static nested banata hoon, warna memory leak ho sakta hai."*
