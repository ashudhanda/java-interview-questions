# 30 — Java Memory Model & `volatile` 🧠

Multithreading ka sabse confusing part. Interview me `volatile` vs `synchronized` almost guaranteed hai.

## Q1. Java Memory Model (JMM) kya hai?
**Answer:** JMM ek **set of rules** hai jo batata hai ki ek thread ne memory me jo likha, wo doosre thread ko **kab dikhega**.

Problem ye hai ki performance ke liye CPU har thread ko variables ki **apni local copy (CPU cache / register)** me rakhne deta hai. Isliye Thread A ne value badli, par Thread B ko purani value dikh sakti hai. JMM define karta hai ki visibility kab guarantee hai.

## Q2. Visibility problem ka real example do
**Answer:**

```java
class Task implements Runnable {
    private boolean running = true;   // volatile nahi hai

    public void run() {
        while (running) { /* kaam */ }
        System.out.println("Stopped");
    }

    public void stop() { running = false; }
}
```

Main thread `stop()` call kare, phir bhi loop **hamesha ke liye chal sakta hai** — worker thread apni cached `running = true` hi padhta rehta hai. `running` ko `volatile` banate hi problem khatam.

## Q3. `volatile` exactly karta kya hai?
**Answer:** Do cheezein guarantee karta hai:

1. **Visibility** — read/write hamesha **main memory** se hoga, CPU cache se nahi. Ek thread ne likha, sab threads ko turant dikhega.
2. **Ordering (happens-before)** — compiler/CPU us variable ke aas-paas ke instructions **reorder nahi** karega.

Jo **NAHI** karta: **atomicity**. Ye sabse important point hai (agla question).

## Q4. `volatile int count; count++;` thread-safe hai kya?
**Answer:** ❌ **Nahi.** Ye classic trap question hai.

`count++` actually **teen operations** hain: read → increment → write. `volatile` sirf ye guarantee karta hai ki har individual read/write fresh hai, par teeno ke beech me doosra thread ghus sakta hai.

```java
volatile int count = 0;
count++;   // NOT atomic — do threads mil ke count 1 hi badha sakte hain
```

**Fix:** `AtomicInteger` use karo (`incrementAndGet()`) ya `synchronized` block.

## Q5. `volatile` vs `synchronized` — difference?
**Answer:**

| | `volatile` | `synchronized` |
|---|---|---|
| Visibility | ✅ | ✅ |
| Atomicity | ❌ | ✅ |
| Locking | Koi lock nahi | Lock lagta hai |
| Blocking | Thread block nahi hoti | Doosri thread wait karti hai |
| Kahan lagta hai | Sirf variable pe | Method / block pe |
| Speed | Fast | Comparatively slow |

**Rule of thumb:** sirf ek thread likhti hai aur baaki padhti hain (flag jaisa case) → `volatile` kaafi. Read-modify-write karna hai → `synchronized` ya `Atomic*`.

## Q6. `happens-before` relationship kya hai?
**Answer:** Ek guarantee ki agar **A happens-before B**, to A ka kiya hua kaam B ko **zaroor dikhega**.

Kuch built-in happens-before rules:
- Ek hi thread me upar wali line **happens-before** neeche wali line
- `unlock` **happens-before** usi lock pe agla `lock`
- `volatile` write **happens-before** usi variable ka agla `volatile` read
- `Thread.start()` **happens-before** us thread ka koi bhi kaam
- Thread ka last statement **happens-before** `join()` return hone se

## Q7. Double-checked locking me `volatile` kyun zaroori hai?
**Answer:** Bina `volatile` ke, **instruction reordering** ki wajah se doosri thread ko **aadhi bani hui (partially constructed)** object mil sakti hai.

```java
class Singleton {
    private static volatile Singleton instance;   // volatile MUST

    static Singleton getInstance() {
        if (instance == null) {                    // 1st check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) {            // 2nd check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

`new Singleton()` internally teen kaam karta hai: memory allocate → constructor run → reference assign. JVM 2 aur 3 ko **reorder** kar sakta hai, isliye reference set ho jaata hai par object abhi bana hi nahi. `volatile` ye reordering rok deta hai.

## Q8. `volatile` kab use NAHI karna chahiye?
**Answer:**
- Jab counter/accumulator badhana ho → `AtomicInteger`/`AtomicLong` use karo
- Jab multiple variables ko ek saath consistent rakhna ho → `synchronized` chahiye, kyunki `volatile` sirf **ek** variable pe kaam karta hai
- Sirf performance ke liye har jagah `volatile` thok dena galat hai — har read/write main memory se hoti hai, wo bhi cost hai

---

> 💡 **Interview me ek line me:** *"`volatile` visibility aur ordering deta hai, atomicity nahi — isliye `count++` ke liye wo kaafi nahi hota."* Itna bol dena impression bana deta hai.
