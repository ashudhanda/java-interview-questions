# 20 — SOLID Principles 🧱

5 design principles by Robert C. Martin (Uncle Bob). Interview me example ke saath explain karna must hai.

## Q1. What does SOLID stand for?
**Answer:**
- **S** — Single Responsibility Principle
- **O** — Open/Closed Principle
- **L** — Liskov Substitution Principle
- **I** — Interface Segregation Principle
- **D** — Dependency Inversion Principle

## Q2. Single Responsibility Principle (SRP)
**Answer:** Ek class ka **sirf ek kaam / ek reason to change** hona chahiye.

❌ Ek `Report` class jo data generate bhi kare, format bhi kare, aur PDF bhi save kare.
✅ Alag-alag: `ReportGenerator`, `ReportFormatter`, `PdfExporter`.

Benefit: ek feature change → sirf ek class chhooti hai.

## Q3. Open/Closed Principle (OCP)
**Answer:** **Open for extension, closed for modification** — naya feature add karna ho to purana tested code chhedo mat, extend karo.

```java
interface Payment { void pay(); }
class UpiPayment implements Payment { public void pay() { ... } }
class CardPayment implements Payment { public void pay() { ... } }
// Naya payment type? Nayi class — purani code untouched.
```

## Q4. Liskov Substitution Principle (LSP)
**Answer:** Subclass objects ko parent ki jagah use kar sako **bina behavior tode**.

Classic violation: `Square extends Rectangle` — `setWidth()` alag behave karta hai → code jo Rectangle expect karta hai, break ho jata hai. Agar substitution safe nahi → inheritance mat use karo.

## Q5. Interface Segregation Principle (ISP)
**Answer:** Moti interface mat do — client ko wo methods implement karne pe force mat karo jo uske kaam ke nahi.

❌ `Worker { work(), eat(), sleep() }` — Robot ko `eat()` implement karna padega (senseless).
✅ Chhoti interfaces: `Workable`, `Eatable`, `Sleepable` — jo chahiye wahi implement.

## Q6. Dependency Inversion Principle (DIP)
**Answer:** High-level modules low-level modules pe depend na karein — **dono abstractions (interfaces) pe depend karein**.

```java
class OrderService {
    private final PaymentGateway gateway; // interface, not StripePayment directly
    OrderService(PaymentGateway gateway) { this.gateway = gateway; }
}
```
Yehi **Dependency Injection** ka foundation hai (Spring isi pe chalta hai).

## Q7. SOLID follow karne ka practical fayda kya hai?
**Answer:** Code **testable, maintainable, aur extendable** rehta hai. Naye requirements aane pe purana code kam tootta hai. Interviews me hamesha real example ke saath bolo — sirf definition se impression nahi banta.