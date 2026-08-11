# 43 — Rapid Fire Mixed Questions ⚡

Interview ke aakhir me 10-15 chhote-chhote questions tez-tez pooche jaate hain. Sochne ka time nahi milta — jawab **turant** aana chahiye. Ye poora set ek baar din me revise kar lo.

---

## 🔹 Core Language

**Q. Java pure object-oriented hai?**
❌ Nahi — primitives (`int`, `char`, `boolean`) object nahi hote. Isliye "pure OOP" nahi kehte.

**Q. `main()` method `private` bana sakte hain?**
Compile ho jaayega, par JVM use dhoondh nahi paayega → runtime pe error. `public static void main(String[])` hi chahiye.

**Q. `main()` ko overload kar sakte hain?**
✅ Haan, par JVM sirf `String[]` wala hi call karega. Baaki ko tum khud call kar sakte ho.

**Q. `main()` ko `final` bana sakte hain?**
✅ Haan, bilkul chalega. Koi problem nahi.

**Q. Java me multiple inheritance kyun nahi hai?**
**Diamond problem** — do parent me same method ho to kaunsa chale? Confusion se bachne ke liye classes me nahi diya. Par **interfaces se multiple inheritance ho sakti hai**.

**Q. Java 8 me interface me method body likh sakte hain?**
✅ Haan — `default` aur `static` methods. Java 9 se `private` methods bhi.

**Q. `int` ka default value kya hai?**
Field me `0`. **Local variable ka koi default nahi hota** — initialize kiye bina use karoge to compile error.

**Q. `String` immutable kyun hai?**
Security (file paths, DB URLs badle na jaa sakein), **String pool** possible ho sake, hashCode cache ho sake, aur thread-safety mile.

---

## 🔹 Tricky Output

**Q. `System.out.println(0.1 + 0.2 == 0.3);`**
`false` — floating point binary me exactly represent nahi hota. Paise ke liye hamesha **`BigDecimal`** use karo.

**Q. `System.out.println('a' + 1);`**
`98` — `char` `int` me promote ho gaya. String chahiye to `(char)('a' + 1)` → `b`.

**Q. `System.out.println("5" + 3 + 2);`**
`532` — left to right chalta hai, sab string ban gaya.
**Aur `System.out.println(3 + 2 + "5");`** → `55` — pehle `3+2=5`, phir string.

**Q. `Integer a = 127, b = 127; a == b;`**
`true` — **Integer cache** `-128` se `127` tak same object deta hai.
**Aur `128` pe?** `false` — cache se bahar, naye objects bane. Isliye wrapper compare karne ke liye hamesha `.equals()`.

**Q. `System.out.println(10 / 3);`**
`3` — integer division. `3.33` chahiye to `10 / 3.0`.

**Q. `System.out.println(10 % -3);` aur `-10 % 3`?**
`1` aur `-1` — result ka sign **dividend** (pehla number) se aata hai.

---

## 🔹 OOP

**Q. Overloading vs Overriding — ek line me?**
Overloading = **same naam, alag parameters, same class** (compile time). Overriding = **same signature, parent-child class** (runtime).

**Q. `static` method override ho sakta hai?**
❌ Nahi — wo **hide** hota hai (method hiding). Static method class se bandha hai, object se nahi, isliye polymorphism lagta hi nahi.

**Q. `private` method override ho sakta hai?**
❌ Nahi — child ko wo dikhta hi nahi. Same naam ka method banaoge to wo bilkul naya method hoga.

**Q. Constructor override ho sakta hai?**
❌ Nahi — constructor inherit hi nahi hota. Overload zaroor kar sakte ho.

**Q. Abstract class me constructor ho sakta hai?**
✅ Haan — child object banne pe wo call hota hai (`super()` se), fields initialize karne ke liye.

**Q. Abstract class ka object bana sakte hain?**
❌ Direct nahi. Par **anonymous class** se bana sakte ho: `new AbstractClass() { ... }`.

**Q. Interface me constructor ho sakta hai?**
❌ Nahi — interface ki koi state hi nahi hoti, to initialize kya karoge.

**Q. Abstract class me saare methods concrete ho sakte hain?**
✅ Haan — ek bhi abstract method zaroori nahi. `abstract` keyword ka matlab bas "iska direct object mat banao".

---

## 🔹 Collections

**Q. `ArrayList` vs `LinkedList` — ek line?**
`ArrayList` = random access fast (`O(1)`), beech me insert slow. `LinkedList` = insert/delete fast (agar node mil gaya), access slow (`O(n)`).

**Q. `HashMap` ka default capacity aur load factor?**
**16** aur **0.75**. Yaani 12 entries pe resize hoga (capacity double).

**Q. `HashMap` me `null` key daal sakte hain?**
✅ Ek `null` key (bucket 0 me jaati hai) aur multiple `null` values. `ConcurrentHashMap` me **dono allowed nahi**.

**Q. `HashMap` vs `Hashtable`?**
`Hashtable` legacy + synchronized + null allowed nahi. Aaj `HashMap` (single thread) ya `ConcurrentHashMap` (multi thread) use karo.

**Q. `Array` vs `ArrayList`?**
Array = fixed size, primitives rakh sakta hai, `.length`. ArrayList = resizable, sirf objects, `.size()`.

**Q. `fail-fast` vs `fail-safe` iterator?**
**Fail-fast** (`ArrayList`, `HashMap`) — iterate karte waqt collection badla to `ConcurrentModificationException`. **Fail-safe** (`CopyOnWriteArrayList`, `ConcurrentHashMap`) — copy pe kaam karta hai, exception nahi.

**Q. Iterate karte hue element remove kaise karein?**
`iterator.remove()` use karo, ya `collection.removeIf(condition)`. Seedha `list.remove()` karoge to `ConcurrentModificationException`.

---

## 🔹 Strings

**Q. `==` vs `.equals()` String me?**
`==` reference compare karta hai, `.equals()` content. Hamesha `.equals()` use karo.

**Q. `new String("abc") == "abc"`?**
`false` — `new` hamesha **heap** me naya object banata hai, pool me nahi jaata. `.intern()` se pool wala reference milta hai.

**Q. `StringBuilder` vs `StringBuffer`?**
Dono mutable. `StringBuffer` **synchronized** (thread-safe, dheema), `StringBuilder` nahi (tez). Single thread me hamesha `StringBuilder`.

**Q. `str.length()` vs `array.length` vs `list.size()`?**
String me **method** `length()`, array me **field** `length`, collection me **method** `size()`. Ye chhota sa confusion bahut baar pakda jaata hai.

---

## 🔹 Exceptions

**Q. `Error` aur `Exception` me farak?**
`Error` = JVM level, recover nahi kar sakte (`OutOfMemoryError`, `StackOverflowError`). `Exception` = application level, handle kar sakte ho.

**Q. `final`, `finally`, `finalize()` — teeno alag hain?**
`final` = keyword (constant/no-override/no-inherit). `finally` = block jo hamesha chalta hai. `finalize()` = purana GC method, **Java 9 se deprecated**, Java 18 me hata diya gaya.

**Q. `try` bina `catch` ke ho sakta hai?**
✅ Haan — `try-finally` ya `try-with-resources`. Bas akela `try` nahi chalega.

---

## 🔹 Threads

**Q. `t.start()` ki jagah `t.run()` call kiya to?**
Nayi thread banegi hi nahi — normal method call ki tarah **same thread** me chalega. Ye favourite trick question hai.

**Q. Ek thread pe do baar `start()` call karein?**
`IllegalThreadStateException` — thread ek hi baar start ho sakti hai.

**Q. `wait()` aur `sleep()` me farak?**
`wait()` — **lock chhod deta hai**, `Object` class ka hai, `synchronized` block me hi chalta hai. `sleep()` — **lock pakde rehta hai**, `Thread` class ka static method hai.

**Q. Deadlock kya hai?**
Do threads ek doosre ka lock pakde baithi hain aur dono wait kar rahi hain — kabhi aage nahi badhengi. **Bachne ka tareeka:** saari threads locks **same order** me lein.

---

## 🔹 Java 8+

**Q. Functional interface kya hai?**
Exactly **ek abstract method** wala interface. `@FunctionalInterface` lagao to compiler check kar dega. Example: `Runnable`, `Comparator`, `Function`.

**Q. `map()` vs `flatMap()`?**
`map` = 1 in → 1 out. `flatMap` = nested structure ko **flat** kar deta hai (`List<List<T>>` → `List<T>`).

**Q. Stream reuse kar sakte hain?**
❌ Nahi — ek baar terminal operation chalne ke baad stream **band** ho jaata hai. Dobara use karoge to `IllegalStateException`.

**Q. Intermediate vs terminal operation?**
Intermediate (`filter`, `map`, `sorted`) = **lazy**, stream return karte hain. Terminal (`collect`, `forEach`, `count`) = actually chalate hain. Terminal na ho to kuch execute hi nahi hoga.

---

> 💡 **Rapid fire ka rule:** jawab **chhota aur confident** do. Pata nahi hai to *"iska exact answer yaad nahi, par mera guess ye hai..."* bolna galat bakwaas karne se kaafi better hai.
