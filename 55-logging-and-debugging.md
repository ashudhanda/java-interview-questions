# 55 — Logging & Debugging 🐛

College me sab `System.out.println()` se debug karte hain. Job me ye pehli cheez hai jo change karni padti hai — aur interviewer ise **experience ka signal** maanta hai.

## Q1. `System.out.println()` se logging kyun nahi karni chahiye?
**Answer:** Paanch bade problems:

1. **Band nahi kar sakte** — production me har line print hoti rahegi
2. **Level nahi hai** — error aur debug message ek jaise dikhte hain
3. **Timestamp / class ka naam nahi** — pata hi nahi chalega kahan se aaya
4. **File me nahi jaata** — console band, log gaya
5. **Slow hai** — `System.out` **synchronized** hai, multi-thread app ko block karta hai

👉 Interview me ye poocha jaata hai: *"tum production me debug kaise karoge?"* — `println` bolne se turant junior lag jaate ho.

## Q2. Log levels kya hote hain?
**Answer:** Sabse zaroori se sabse kam zaroori:

| Level | Kab use karein |
|---|---|
| **ERROR** | Kuch fail ho gaya, action chahiye |
| **WARN** | Gadbad hai par app chal raha hai |
| **INFO** | Important business events ("order placed") |
| **DEBUG** | Developer ke liye details |
| **TRACE** | Bahut detailed, har step |

👉 **Asli faayda:** config me level set karte ho. `INFO` set kiya to `DEBUG` aur `TRACE` **automatically band**. Code badalne ki zaroorat nahi — bas config file change karo.

**Production me:** normally `INFO`. Problem aaye to temporarily `DEBUG` kar do.

## Q3. SLF4J kya hai? Logback se kya farak?
**Answer:** Ye confusion bahut hoti hai:

- **SLF4J** = **interface** (facade). Sirf API deta hai, khud logging nahi karta.
- **Logback / Log4j2** = **implementation**. Asli kaam ye karte hain.

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public void placeOrder(String id) {
        log.info("Order placed: {}", id);
    }
}
```

👉 **Faayda:** code SLF4J pe likha hai. Kal Logback se Log4j2 pe jaana ho to **sirf dependency badlo** — ek line code nahi badlega. Ye **interface pe code karo, implementation pe nahi** wala SOLID principle ka real example hai (topic 20).

⚠️ `static final` hi rakhna logger ko — har object ke saath naya logger banana waste hai.

## Q4. `{}` placeholder kyun use karte hain?
**Answer:** Ye **performance** ka sawaal hai — accha follow-up question hai.

```java
// ❌ GALAT — string hamesha banti hai
log.debug("User " + userId + " ne " + count + " items kharide");

// ✅ SAHI — string tabhi banti hai jab DEBUG on ho
log.debug("User {} ne {} items kharide", userId, count);
```

**Kyun?** Pehle wale me **string concatenation pehle** hota hai, phir method call. Agar `DEBUG` band bhi hai, to bhi string ban chuki hoti hai — CPU waste. Placeholder wale me library pehle check karti hai ki level on hai ya nahi.

👉 Loop me hazaaron baar chale to ye farak **bahut** bada ho jaata hai.

## Q5. Exception log kaise karein?
**Answer:** Sabse common galti yahi hoti hai:

```java
try {
    riskyOperation();
} catch (Exception e) {

    log.error("Kuch galat ho gaya");              // ❌ stack trace hi gaya!
    log.error("Error: " + e.getMessage());        // ❌ sirf message, trace nahi
    e.printStackTrace();                          // ❌ console pe, log file me nahi

    log.error("Order {} process nahi hua", orderId, e);   // ✅ SAHI
}
```

👉 **Exception ko last argument** me pass karo — bina placeholder ke. SLF4J khud samajh jaata hai aur **poora stack trace** print karta hai.

⚠️ `e.getMessage()` aksar `null` hota hai (jaise `NullPointerException` me). Sirf message log karoge to kuch pata nahi chalega.

## Q6. Kya cheez log me kabhi nahi jaani chahiye?
**Answer:** Ye **security** ka question hai — senior level pe poocha jaata hai:

❌ Passwords, API keys, tokens
❌ Credit card / bank details
❌ Aadhaar, PAN, phone numbers (PII)
❌ Poora request body (usme kuch bhi ho sakta hai)

```java
log.info("Login attempt: user={}, password={}", user, password);   // ❌ KABHI NAHI
log.info("Login attempt: user={}", user);                          // ✅
```

👉 Log files backup hoti hain, monitoring tools me jaati hain, kai logon ko dikhti hain. Ek baar likh diya to hata-na mushkil hai.

## Q7. Debugger kaise use karte hain?
**Answer:** IDE debugger `println` se **bahut** tez hai. Ye basics pata hone chahiye:

| Feature | Kaam |
|---|---|
| **Breakpoint** | Line pe rok do (line number pe click) |
| **Step Over** (F8) | Agli line pe jao, method ke andar mat ghuso |
| **Step Into** (F7) | Method ke **andar** ghuso |
| **Step Out** | Current method se bahar nikal jao |
| **Resume** (F9) | Agle breakpoint tak chalao |
| **Watch** | Kisi expression ki value track karo |
| **Evaluate Expression** | Rukte hue koi bhi code chala ke dekho |

👉 **Conditional breakpoint** — game changer. Breakpoint pe right-click → condition daalo:
```java
i == 500 && list.get(i) == null
```
Ab loop sirf **usi case** pe rukega. 500 baar F9 dabane ki zaroorat nahi.

## Q8. Stack trace kaise padhein?
**Answer:** Ye skill sabse zyada kaam aati hai:

```
Exception in thread "main" java.lang.NullPointerException: 
    Cannot invoke "String.length()" because "name" is null
    at com.app.UserService.validate(UserService.java:42)     ← YAHAN error hua
    at com.app.UserService.save(UserService.java:28)
    at com.app.Main.main(Main.java:15)                        ← yahan se shuru hua
Caused by: java.sql.SQLException: Connection timeout           ← ASLI wajah
    at ...
```

**Padhne ka tareeka:**
1. **Sabse upar wali line** = exception ka type aur message
2. **Pehli `at` line** = exactly kahan crash hua
3. **Neeche jao** = kisne kisko call kiya
4. **`Caused by:` sabse zaroori** — asli root cause aksar wahin hota hai

👉 **Apne package ki pehli line dhoondho** — `com.app...` wali. Library ke andar ki lines aksar tumhari galti nahi hoti.

⚠️ **Multiple `Caused by` ho to sabse neeche wala** dekho — wahi original problem hai.

## Q9. Java 14+ ka Helpful NullPointerException
**Answer:** Pehle NPE bekaar hota tha:

```
java.lang.NullPointerException          ← bas itna. Kaun sa null tha?!
    at com.app.Main.main(Main.java:5)
```

Ab Java 14 se:
```
java.lang.NullPointerException: Cannot invoke "Address.getCity()" 
because the return value of "User.getAddress()" is null
```

👉 Ab **exactly** pata chal jaata hai ki chain me kaun sa hissa `null` tha. `user.getAddress().getCity()` me `getAddress()` null tha.

Java 14 me flag se on karna padta tha, **Java 15+ me default on** hai.

## Q10. Logging best practices
**Answer:**

✅ **Karo:**
- Method ke start/end pe DEBUG, important events pe INFO
- Context daalo — `orderId`, `userId` (dhoondhne me aasani)
- Exception ke saath poora object pass karo
- Log rotation set karo (file badi ho ke disk na bhar de)

❌ **Mat karo:**
- Loop ke andar INFO level pe log (log file phatt jaayegi)
- Sensitive data log karna
- `catch` me log **aur** rethrow dono (duplicate logs banenge)
- `e.printStackTrace()` — wo `System.err` pe jaata hai, log file me nahi

```java
// ❌ Double logging
catch (Exception e) {
    log.error("Failed", e);
    throw new ServiceException(e);      // upar wala bhi log karega
}

// ✅ Ya log karo, ya throw — dono nahi
catch (Exception e) {
    throw new ServiceException("Order process failed: " + orderId, e);
}
```

## Q11. Debugging ka systematic tareeka
**Answer:** Random changes karne se accha:

1. **Reproduce karo** — bug consistently aata hai? Kaunse steps se?
2. **Stack trace padho** — `Caused by` tak jao
3. **Assumptions likho** — "mujhe lagta hai `list` khaali hai"
4. **Ek-ek karke verify karo** — breakpoint ya log se
5. **Halve karo** — problem aadhe code me hai? Us aadhe ko aur aadha karo
6. **Fix karo, phir test likho** taaki dobara na aaye

⚠️ **Sabse badi galti:** ek saath 5 cheezein badal dena. Phir pata hi nahi chalta kisne theek kiya. **Ek waqt me ek change.**

---

> 💡 **Interview tip:** jab poochein *"production issue kaise debug karoge?"* — ye framework batao: *"pehle logs dekhunga error ke aas-paas ke timestamp pe, `Caused by` se root cause nikalunga, phir agar zaroorat ho to us specific class ka log level DEBUG kar dunga bina deploy kiye."* Ye jawab practical experience dikhata hai — `println` wale jawab se meelon aage.
