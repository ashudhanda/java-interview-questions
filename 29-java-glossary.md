# 29 — Java Glossary: A–Z Quick Reference 📖

> Every scary Java word, explained in ONE line. Perfect for last-minute revision or when a tutorial throws jargon at you.

---

| Term | One-line meaning | Deep dive |
|------|------------------|-----------|
| **Abstraction** | Show WHAT can be done, hide HOW it's done | [Ch 9](notes/09-oops-made-simple.md) |
| **Access modifier** | Visibility switch: `public` → everyone, `private` → this class only, `protected` → family + package, *(default)* → package | [Ch 9](notes/09-oops-made-simple.md) |
| **ArrayList** | Growable array — the train that can add coaches | [Ch 10](notes/10-collections.md) |
| **Autoboxing** | Java auto-wrapping `int` → `Integer` (and back) for collections | [Ch 10](notes/10-collections.md) |
| **Bytecode** | The `.class` output — universal language the JVM speaks | [Ch 1](notes/01-how-java-works.md) |
| **Casting** | Converting one type to another: `(int) 3.9` → 3 | [Ch 2](notes/02-variables-and-datatypes.md) |
| **Checked exception** | Compiler FORCES you to handle it (`IOException`) | [Q&A 5](05-exception-handling.md) |
| **Constructor** | Special method that runs at `new` — the object's birth ceremony | [Ch 9](notes/09-oops-made-simple.md) |
| **Encapsulation** | Private fields + public getters/setters = protected data | [Ch 9](notes/09-oops-made-simple.md) |
| **Exception** | Runtime problem you can catch and survive | [Q&A 5](05-exception-handling.md) |
| **final** | Lock it: variable → constant, method → no override, class → no extend | [Q&A 26](26-final-static-keywords.md) |
| **Functional interface** | Interface with exactly 1 abstract method → lambda-ready | [Q&A 21](21-lambda-functional-interfaces.md) |
| **Garbage Collector (GC)** | JVM's cleaner — frees heap objects nobody points to | [Q&A 24](24-garbage-collection.md) |
| **Generics** | Type label on containers: `ArrayList<String>` | [Q&A 11](11-generics.md) |
| **HashMap** | Key → value locker room with near-instant lookup | [Ch 10](notes/10-collections.md) |
| **Heap** | Memory area where all objects live | [Ch 3](notes/03-memory-stack-vs-heap.md) |
| **Immutable** | Cannot be changed after creation (Strings!) | [Ch 8](notes/08-strings.md) |
| **Inheritance** | Child class gets parent's fields + methods via `extends` | [Ch 9](notes/09-oops-made-simple.md) |
| **Interface** | Pure contract: WHAT to do, zero HOW | [Q&A 28](28-interfaces-vs-abstract-classes.md) |
| **JDK** | Development kit: compiler + tools + JRE (you install this) | [Ch 1](notes/01-how-java-works.md) |
| **JRE** | Runtime: JVM + libraries (enough to RUN, not build) | [Ch 1](notes/01-how-java-works.md) |
| **JVM** | The virtual machine that executes bytecode — 'write once, run anywhere' | [Ch 1](notes/01-how-java-works.md) |
| **Lambda** | Short form of a one-method object: `x -> x * 2` | [Q&A 21](21-lambda-functional-interfaces.md) |
| **Method overloading** | Same name, different parameters — compile-time choice | [Ch 7](notes/07-methods.md) |
| **Method overriding** | Child replaces parent's method — runtime choice | [Ch 9](notes/09-oops-made-simple.md) |
| **NullPointerException** | You followed a reference that points to NOTHING 💥 | [Q&A 5](05-exception-handling.md) |
| **Object** | A real instance built from a class blueprint | [Ch 9](notes/09-oops-made-simple.md) |
| **Package** | Folder system for classes (`java.util`, `com.myapp`) | [Ch 1](notes/01-how-java-works.md) |
| **Polymorphism** | One call, many behaviours: `a.sound()` → woof or meow | [Ch 9](notes/09-oops-made-simple.md) |
| **Primitive** | The 8 basic value types: byte, short, int, long, float, double, char, boolean | [Ch 2](notes/02-variables-and-datatypes.md) |
| **Recursion** | A method calling itself, with a base case to stop | [Q&A 27](27-recursion-coding-questions.md) |
| **Reference** | A variable holding an object's ADDRESS, not the object | [Ch 3](notes/03-memory-stack-vs-heap.md) |
| **Stack** | Memory for method calls + local variables (plates pile) | [Ch 3](notes/03-memory-stack-vs-heap.md) |
| **static** | Belongs to the CLASS, not to any object — shared by all | [Q&A 26](26-final-static-keywords.md) |
| **Stream API** | Assembly line for data: `filter → map → collect` | [Q&A 18](18-streams-api-coding.md) |
| **String pool** | Shared storage where equal string literals live once | [Ch 8](notes/08-strings.md) |
| **StringBuilder** | Mutable (editable) string — fast in loops | [Ch 8](notes/08-strings.md) |
| **super** | Handle to the parent: `super()` constructor, `super.method()` | [Ch 9](notes/09-oops-made-simple.md) |
| **this** | Handle to the current object's own members | [Ch 9](notes/09-oops-made-simple.md) |
| **Thread** | An independent lane of execution inside your program | [Q&A 6](06-multithreading.md) |
| **Wrapper class** | Object versions of primitives: `int` → `Integer` | [Ch 10](notes/10-collections.md) |

---

💡 **How to use:** Ctrl+F the confusing word → read the one-liner → click the deep dive if you need more.

🏠 [Back to Index](README.md) | Quick revision buddy: [Cheatsheet](16-quick-revision-cheatsheet.md)
