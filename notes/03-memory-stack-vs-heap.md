# Chapter 3 — Memory in Java: Stack vs Heap 🧠

> **Goal:** Know exactly WHERE your variables live. This one chapter makes objects, references, and `new` finally click.

---

## 1. Two areas of memory

When your program runs, the JVM divides memory into two main working areas:

**Analogy 🏢:** Think of an office:
- **Stack** = your **desk** 🗂️ — small, super fast, only YOUR current work papers. When a task finishes, its papers are thrown away instantly.
- **Heap** = the office **warehouse/godown** 🏬 — big, shared by everyone, stores all the actual heavy stuff. Cleaning staff (garbage collector) removes things nobody uses anymore.

| | Stack 🗂️ | Heap 🏬 |
|--|---------|--------|
| Stores | local variables, method calls, **references** | **objects** (created with `new`) |
| Speed | very fast | slower |
| Size | small | large |
| Cleaned | automatically when method ends | by **Garbage Collector** |
| Full → error | `StackOverflowError` | `OutOfMemoryError` |

## 2. What goes where? (the golden rule)

```java
public static void main(String[] args) {
    int age = 20;                    // STACK (primitive local variable)
    String name = "Ashu";           // reference on STACK → object on HEAP
    int[] marks = new int[3];        // reference on STACK → array on HEAP
}
```

```
        STACK (desk)                HEAP (warehouse)
   ┌────────────────┐          ┌──────────────────┐
   │ age   = 20     │          │                    │
   │ name  = ●─────┼────────▶│  "Ashu"           │
   │ marks = ●─────┼────────▶│  [0, 0, 0]        │
   └────────────────┘          └──────────────────┘
```

> 🔑 **Golden rule:** Primitives live directly on the stack. Objects live on the heap; the stack only holds the **address slip (reference)** pointing to them.

## 3. `new` = Dynamic Memory Allocation (DMA)

Every time you write `new`, you're saying: *"JVM bhai, warehouse me jagah de do!"* — memory is allocated on the **heap at runtime**. This is called **dynamic memory allocation**.

```java
Student s1 = new Student();   // new → heap allocation, s1 holds the address
Student s2 = s1;              // COPY OF ADDRESS — both point to the SAME object!
s2.name = "Rahul";
System.out.println(s1.name);  // "Rahul" 😱 — because s1 and s2 = same warehouse box
```

This is the #1 source of confusion for beginners — **copying a reference does NOT copy the object**, only the address slip.

## 4. Method calls & the stack (why it's called "stack")

Each method call gets its own **frame** (papers on the desk), stacked one over another — Last In, First Out:

```java
main() → calls calculate() → calls add()

   │ add()       │  ◀ currently running (top of stack)
   │ calculate() │  ◀ waiting
   │ main()      │  ◀ waiting
   └─────────────┘
```

When `add()` finishes, its frame (and ALL its local variables) is destroyed instantly. That's why a method can't see another method's local variables!

> 💥 A method calling itself forever → frames pile up → desk overflows → `StackOverflowError`. (Yes, the website is named after this error!)

## 5. Garbage Collector (GC) — the cleaning staff 🧹

When no reference points to an object anymore, it becomes **garbage**:

```java
String name = new String("Ashu");
name = null;    // nobody points to "Ashu" object now → GC will remove it
```

Java does this **automatically** — no `free()`/`delete` like C/C++. This is why Java rarely has memory leaks from forgotten deallocation.

## 6. ❌ Common Confusions Cleared

1. "Objects are stored in variables" → **No.** Variables hold *references* (addresses); objects sit on the heap.
2. "`==` compares objects" → **No.** For objects, `==` compares *addresses*. Use `.equals()` for content.
3. "Java has pointers?" → References ARE safe pointers — you just can't do address math with them.

---

## 🏋️ Practice

**Q1. Predict the output:**
```java
int[] a = {1, 2, 3};
int[] b = a;
b[0] = 99;
System.out.println(a[0]);
```

<details><summary>✅ Answer + why</summary>

`99` — `b = a` copies only the **address**, so both point to the same array on the heap. Changing through `b` changes the one and only array.
</details>

**Q2. Which error — and why?**
```java
static void hello() { hello(); }
```

<details><summary>✅ Answer</summary>

`StackOverflowError` — every call adds a new frame to the stack and none ever finishes, so the desk overflows.
</details>

**Q3. After this code, how many objects are on the heap, and how many are garbage?**
```java
String s1 = new String("A");
String s2 = new String("B");
s1 = s2;
```

<details><summary>✅ Answer</summary>

2 objects were created ("A" and "B"). After `s1 = s2`, **nobody** references "A" → it's garbage (1 garbage, 1 alive with two references pointing to it).
</details>

---

⬅️ [Prev: Variables](02-variables-and-datatypes.md) | ➡️ [Next: Arrays](04-arrays.md)
