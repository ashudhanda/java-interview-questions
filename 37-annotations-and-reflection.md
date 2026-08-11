# 37 — Annotations & Reflection 🔍

Spring, Hibernate, JUnit — sab isi pe khade hain. "`@Autowired` andar se kaam kaise karta hai?" ka jawab yahi topic hai.

## Q1. Annotation kya hai?
**Answer:** Code ke baare me **metadata** — extra information jo compiler ya framework padh sake. Annotation khud kuch **karta nahi**; koi na koi use padh ke action leta hai.

```java
@Override        // compiler check karega
@Deprecated      // warning dega
@FunctionalInterface   // ek hi abstract method enforce karega
```

👉 Ye important point hai: `@Autowired` likhne se jaadu nahi hota — **Spring** us annotation ko reflection se dhoondhta hai aur value inject karta hai.

## Q2. Built-in annotations kaunse hain?
**Answer:**
- `@Override` — method sach me override kar raha hai ya nahi, compiler check karega. Typo pakda jaata hai.
- `@Deprecated` — purana hai, use mat karo
- `@SuppressWarnings("unchecked")` — warning dabao
- `@FunctionalInterface` — sirf ek abstract method allowed
- `@SafeVarargs` — generic varargs safe hai

## Q3. Custom annotation kaise banate hain?
**Answer:**

```java
@Retention(RetentionPolicy.RUNTIME)   // runtime pe available rahe
@Target(ElementType.METHOD)           // sirf methods pe lag sake
public @interface LogExecutionTime {
    String value() default "";
    int threshold() default 1000;
}
```

Use:
```java
@LogExecutionTime(threshold = 500)
public void processData() { ... }
```

## Q4. `@Retention` ki policies kya hain?
**Answer:** Ye **sabse important** meta-annotation hai.

| Policy | Kab tak zinda | Use case |
|---|---|---|
| `SOURCE` | Sirf compile tak, `.class` me nahi | `@Override`, Lombok |
| `CLASS` | `.class` file me, par runtime pe load nahi | Bytecode tools (default) |
| `RUNTIME` | Runtime pe reflection se readable | **Spring, JUnit, Hibernate** |

⚠️ **Common bug:** custom annotation banaya par `@Retention(RUNTIME)` lagana bhool gaye → reflection se milega hi nahi, framework silently ignore kar dega. Default `CLASS` hai, `RUNTIME` nahi.

## Q5. `@Target` kya karta hai?
**Answer:** Annotation **kahan lag sakta hai** ye restrict karta hai.

```java
ElementType.TYPE        // class, interface, enum
ElementType.METHOD      // method
ElementType.FIELD       // field
ElementType.PARAMETER   // method parameter
ElementType.CONSTRUCTOR
ElementType.LOCAL_VARIABLE
ElementType.ANNOTATION_TYPE   // doosre annotation pe
```

Galat jagah lagaoge to **compile-time error** milega — yahi iska fayda hai.

## Q6. Reflection kya hai?
**Answer:** **Runtime pe** class ki structure inspect karne aur usse manipulate karne ki ability — bina compile time pe naam jaane.

```java
Class<?> clazz = Class.forName("com.example.User");

for (Method m : clazz.getDeclaredMethods()) {
    System.out.println(m.getName());
}

Object obj = clazz.getDeclaredConstructor().newInstance();
Method setter = clazz.getMethod("setName", String.class);
setter.invoke(obj, "Ashu");
```

## Q7. `getMethods()` vs `getDeclaredMethods()`?
**Answer:** Ye chhota par poocha jaane wala difference hai.

| | `getMethods()` | `getDeclaredMethods()` |
|---|---|---|
| Inherited methods | ✅ milte hain | ❌ nahi |
| Private methods | ❌ nahi | ✅ milte hain |
| Access level | Sirf `public` | Sab (private, protected, public) |

Yahi pattern `getFields()`/`getDeclaredFields()` pe bhi lagta hai.

## Q8. Reflection se private field access kaise karte hain?
**Answer:**

```java
Field field = obj.getClass().getDeclaredField("secret");
field.setAccessible(true);        // encapsulation bypass!
Object value = field.get(obj);
```

⚠️ Ye **encapsulation tod deta hai**. Testing aur frameworks ke liye theek hai, normal application code me nahi. Java 9+ me modules ki wajah se `setAccessible(true)` internal JDK classes pe **fail** ho sakta hai.

## Q9. Annotation ko reflection se kaise padhte hain?
**Answer:** Ye poora picture jodta hai — annotation + reflection = framework.

```java
for (Method m : clazz.getDeclaredMethods()) {
    if (m.isAnnotationPresent(LogExecutionTime.class)) {
        LogExecutionTime ann = m.getAnnotation(LogExecutionTime.class);

        long start = System.currentTimeMillis();
        m.invoke(obj);
        long time = System.currentTimeMillis() - start;

        if (time > ann.threshold()) {
            System.out.println(m.getName() + " slow: " + time + "ms");
        }
    }
}
```

👉 **JUnit exactly yahi karta hai** — saare methods scan karta hai, `@Test` wale dhoondhta hai, aur unhe `invoke()` kar deta hai.

## Q10. Reflection ke nuksaan kya hain?
**Answer:**
- **Slow** — direct call se kaafi dheema. JIT optimize nahi kar paata.
- **No compile-time safety** — method ka naam string me hai. Rename kiya to compiler nahi batayega, runtime pe crash hoga.
- **Encapsulation break** — private data expose ho jaata hai
- **Refactoring tootta hai** — IDE string ke andar wala naam nahi badalta
- **Security** — security manager block kar sakta hai

✅ **Use karo:** frameworks, libraries, testing tools, serialization me.
❌ **Mat karo:** normal business logic me. Wahan interface/polymorphism better hai.

## Q11. Reflection ka real-world use kahan hai?
**Answer:**
- **Spring** — `@Component` scan karke beans banata hai, `@Autowired` fields inject karta hai
- **Hibernate/JPA** — `@Entity` classes ko table se map karta hai
- **JUnit** — `@Test` methods dhoondh ke chalata hai
- **Jackson/Gson** — fields padh ke JSON banata hai
- **Lombok** — (thoda alag — ye compile time pe AST modify karta hai, runtime reflection nahi)

---

> 💡 **Interview me impress karne wala jawab:** *"Annotation khud kuch nahi karta — wo sirf marker hai. Framework `RUNTIME` retention wale annotations ko reflection se scan karta hai aur tab action leta hai. Isliye custom annotation me `@Retention(RUNTIME)` lagana zaroori hai, warna framework use dekh hi nahi paayega."*
