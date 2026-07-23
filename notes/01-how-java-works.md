# Chapter 1 — How Java Actually Works ☕

> **Goal:** After this chapter, `public static void main` will never scare you again.

---

## 1. What is Java?

Java is a programming language — a way to give instructions to a computer. But computers only understand **machine code** (0s and 1s), and every OS (Windows, Mac, Linux) speaks a *different* machine code.

So how does one Java program run everywhere? 🤔

## 2. The Magic: Bytecode + JVM

**Analogy 🎬:** Imagine you write a movie script in English. Instead of re-writing it for every country, you write it once, and every country has its own **local translator** who performs it in the local language.

- Your script = **Java code** (`.java` file)
- The universal version = **bytecode** (`.class` file)
- The local translator = **JVM** (Java Virtual Machine)

```
Hello.java  ──javac (compiler)──▶  Hello.class (bytecode)  ──JVM──▶  runs on ANY OS
```

That's why Java's slogan is **"Write Once, Run Anywhere."**

### JDK vs JRE vs JVM (one line each)

- **JVM** — runs bytecode (the translator)
- **JRE** — JVM + libraries needed to **run** programs (translator + their dictionary)
- **JDK** — JRE + compiler & tools needed to **write** programs (the full studio) — *this is what you install to code*

## 3. Your First Program — Every Word Explained

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");
    }
}
```

| Word | Why it's there |
|------|----------------|
| `class Hello` | In Java, ALL code lives inside a class. Think of a class as a **container/file cabinet**. |
| `public` | Visible to everyone — the JVM (an outsider) must be able to find and call it. |
| `static` | JVM can call `main` **without creating an object** first. (Objects come later — don't worry yet!) |
| `void` | `main` returns nothing back to the JVM. |
| `main` | The **entry gate** 🚪 — JVM always starts execution here. Fixed name. |
| `String[] args` | A box for extra info you can pass from the command line. Usually empty — ignore for now. |
| `System.out.println(...)` | "Hey System, on the OUTput screen, PRINT this LiNe." |

> 💡 **Beginner tip:** For now, treat `public static void main(String[] args)` as the **fixed opening line of every program** — like "Once upon a time..." in stories. By Chapter 9 (OOPs), every word will make full sense.

## 4. How it runs (step by step)

1. You save the file as `Hello.java` (file name **must** match class name!)
2. `javac Hello.java` → compiler checks grammar & creates `Hello.class` (bytecode)
3. `java Hello` → JVM loads bytecode and starts executing from `main`
4. Output appears: `Hello, Java!`

## 5. ❌ Common Beginner Mistakes

1. **File name ≠ class name** → `class Hello` must be in `Hello.java`
2. **Missing semicolon** `;` — every statement ends with one
3. **Wrong capitalization** — Java is case-sensitive: `System` ≠ `system`, `String` ≠ `string`
4. Writing `Void` or `Main` — must be exactly `void` and `main`

---

## 🏋️ Practice (try before peeking!)

**Q1. Print your name and college on two separate lines.**

<details><summary>✅ Solution + explanation</summary>

```java
public class Intro {
    public static void main(String[] args) {
        System.out.println("Ashu Dhanda");
        System.out.println("XYZ College");
    }
}
```
Each `println` automatically moves to the next line (`ln` = line). If you used `print` instead, both would appear on one line.
</details>

**Q2. What error do you get if you save `class Hello` inside a file named `Test.java`? Try it!**

<details><summary>✅ Answer</summary>

Compile error: `class Hello is public, should be declared in a file named Hello.java`. Java forces one public class per file, with matching names — so the JVM can always find classes by filename.
</details>

**Q3. Predict the output:**
```java
System.out.print("Java ");
System.out.print("is ");
System.out.println("easy!");
System.out.println("Seriously.");
```

<details><summary>✅ Answer</summary>

```
Java is easy!
Seriously.
```
`print` stays on the same line; `println` prints then jumps to the next line.
</details>

---

🏠 [Index](../README.md) | ➡️ [Next: Variables & Data Types](02-variables-and-datatypes.md)
