# 73 — Design Patterns Part 2 🎨

Topic 13 me basic patterns the, topic 62 me Singleton/Observer/Factory/Builder. Ab baaki 5 jo interview me aate hain: **Strategy, Adapter, Decorator, Template, Command**.

## Q1. Strategy Pattern
**Answer:** Algorithm ko **runtime pe swap** karna — if-else ki chain khatam.

```java
// ❌ Bina strategy — har naya payment method = naya if
void pay(String type, double amount) {
    if (type.equals("card")) { /* card logic */ }
    else if (type.equals("upi")) { /* upi logic */ }
    else if (type.equals("wallet")) { /* wallet logic */ }
}

// ✅ Strategy pattern
interface PaymentStrategy {
    void pay(double amount);
}

class CardPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("Card: " + amount); }
}
class UpiPayment implements PaymentStrategy {
    public void pay(double amount) { System.out.println("UPI: " + amount); }
}

class CheckoutService {
    private PaymentStrategy strategy;
    void setStrategy(PaymentStrategy s) { this.strategy = s; }
    void checkout(double amount) { strategy.pay(amount); }
}

// Use — runtime pe switch
checkout.setStrategy(new UpiPayment());
checkout.checkout(500);
```

👉 **Real world:** `Comparator` interface hi Strategy pattern hai — `list.sort(comparator)` me tum algorithm baahar se dete ho.

**Kab use:** jab behavior runtime pe badalna ho, ya `if-else` ki lambi chain ho.

## Q2. Adapter Pattern
**Answer:** Do **incompatible interfaces** ko jodna — charger ka converter jaise.

```java
// Purana system — ye interface expect karta hai
interface MediaPlayer {
    void play(String file);
}

// Nayi library — iska interface ALAG hai
class AdvancedPlayer {
    void playMp4(String file) { System.out.println("MP4: " + file); }
    void playMkv(String file) { System.out.println("MKV: " + file); }
}

// ✅ Adapter — beech ka translator
class MediaAdapter implements MediaPlayer {
    private AdvancedPlayer advanced = new AdvancedPlayer();

    public void play(String file) {
        if (file.endsWith(".mp4")) advanced.playMp4(file);
        else if (file.endsWith(".mkv")) advanced.playMkv(file);
    }
}
```

👉 **Real world:** `Arrays.asList()` array ko List me convert karta hai — adapter. Indian plug ko US socket me lagane wala converter — wahi concept.

## Q3. Decorator Pattern
**Answer:** Object me **features wrap** karo, bina class badle. Pizza pe toppings jaise.

```java
interface Coffee {
    double cost();
    String description();
}

class SimpleCoffee implements Coffee {
    public double cost() { return 50; }
    public String description() { return "Coffee"; }
}

// Decorator base
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    CoffeeDecorator(Coffee c) { this.coffee = c; }
}

class Milk extends CoffeeDecorator {
    Milk(Coffee c) { super(c); }
    public double cost() { return coffee.cost() + 20; }
    public String description() { return coffee.description() + " + Milk"; }
}

class Sugar extends CoffeeDecorator {
    Sugar(Coffee c) { super(c); }
    public double cost() { return coffee.cost() + 10; }
    public String description() { return coffee.description() + " + Sugar"; }
}

// Use — wrapping!
Coffee c = new Sugar(new Milk(new SimpleCoffee()));
System.out.println(c.description());  // "Coffee + Milk + Sugar"
System.out.println(c.cost());          // 80
```

👉 **Real world:** Java ka I/O — `new BufferedReader(new InputStreamReader(new FileInputStream(f)))`. Har layer ek decorator!

**Decorator vs Inheritance:** inheritance se har combination ki alag class chahiye (MilkCoffee, SugarCoffee, MilkSugarCoffee...). Decorator me runtime pe wrap karo — combinations infinite.

## Q4. Template Method Pattern
**Answer:** Algorithm ka **skeleton fix**, steps subclasses bharein.

```java
abstract class DataProcessor {
    // Template method — final, order fix hai
    final void process() {
        readData();
        processData();
        saveData();
    }

    abstract void readData();       // subclasses define karenge
    abstract void processData();

    void saveData() {               // common — sabke liye same
        System.out.println("Saving to DB");
    }
}

class CsvProcessor extends DataProcessor {
    void readData() { System.out.println("Reading CSV"); }
    void processData() { System.out.println("Parsing CSV"); }
}

class JsonProcessor extends DataProcessor {
    void readData() { System.out.println("Reading JSON"); }
    void processData() { System.out.println("Parsing JSON"); }
}
```

👉 **"Hollywood principle":** *"Don't call us, we'll call you"* — parent class flow control karta hai, child sirf steps deta hai.

## Q5. Command Pattern
**Answer:** Request ko **object** bana do — undo/redo, queue, logging sab possible.

```java
interface Command {
    void execute();
    void undo();
}

class LightOnCommand implements Command {
    private Light light;
    LightOnCommand(Light l) { this.light = l; }
    public void execute() { light.on(); }
    public void undo() { light.off(); }
}

class RemoteControl {
    private Command command;
    void setCommand(Command c) { this.command = c; }
    void pressButton() { command.execute(); }
    void pressUndo() { command.undo(); }
}
```

👉 **Real world:** text editor ka undo, job queues, `Runnable` interface bhi command pattern hi hai.

## Q6. Sab patterns ka quick map
**Answer:**

| Pattern | Ek line me |
|---|---|
| **Strategy** | Algorithm runtime pe swap |
| **Adapter** | Incompatible interfaces ko jodo |
| **Decorator** | Features wrap karo, class mat badlo |
| **Template** | Skeleton fix, steps subclasses |
| **Command** | Request = object (undo possible) |
| **Observer** | Ek badle, sabko pata chale |
| **Factory** | Object creation alag class me |
| **Builder** | Complex object step-by-step |
| **Singleton** | Sirf ek instance |

## Q7. Kahan use hote hain — Java/Spring me
**Answer:**

| Pattern | Java/Spring example |
|---|---|
| Strategy | `Comparator`, Spring's `PaymentService` interfaces |
| Adapter | `Arrays.asList()`, InputStreamReader |
| Decorator | `BufferedReader`, `Collections.synchronizedList()` |
| Template | `JdbcTemplate`, `RestTemplate` |
| Command | `Runnable`, `Callable` |
| Observer | `EventListener`, Spring Events |
| Factory | Spring's `BeanFactory` |
| Builder | `StringBuilder`, Lombok `@Builder` |
| Singleton | Spring beans (default scope) |

👉 Interview me pattern bolo to **Java/Spring ka example** zaroor do — theory rattne se zyada impress karta hai.

## Q8. Pattern overuse ka khatra
**Answer:**

⚠️ **Har cheez pe pattern mat lagao.** Simple problem ko pattern se complex mat banao.

- Do payment methods hain → Strategy theek hai
- Ek hi hai → interface banane ki zaroorat nahi
- Ye **over-engineering** hai

👉 **Rule:** jab do baar variation aaye tab pattern socho. Pehli baar me simple code likho. (YAGNI — topic 57)

---

> 💡 **Interview tip:** pattern poochhein to teen cheezein bolo — (1) problem kya solve karta hai, (2) Java/Spring me kahan use hota hai, (3) kab use NAHI karna chahiye. Teesri baat sabse kam log bolte hain — wahi tumhe alag karegi. Jaise: *"Decorator file I/O me use hota hai, par simple formatting ke liye overkill hai — wahan inheritance ya method chalega."*
