# 62 — OOP Design Problems 🏗️

Ye **LLD (Low Level Design)** ka introduction hai — "Parking Lot design karo", "LRU Cache banao", "Snake & Ladder likhna hai". Coding round se alag hota hai — yahan **class structure** matter karta hai.

## Q1. Design round me kya expect hota hai?
**Answer:** Algorithm nahi — **class relationships**:

1. **Requirements clear karo** — kaunse operations chahiye?
2. **Classes identify karo** — nouns = classes, verbs = methods
3. **Relationships banao** — inheritance, composition, has-a
4. **Code likho** — core functionality

👉 **Interview me bolo:** *"Pehle main requirements confirm karta hoon, phir class diagram banata hoon, phir code likhta hoon."* Seedha code mat likhna.

## Q2. Design a Parking Lot
**Answer:** Classic LLD question.

**Step 1: Requirements**
- Multiple floors, har floor pe spots
- Spot types: Compact, Large, Handicapped
- Vehicle types: Bike, Car, Truck
- Entry pe ticket, exit pe payment

**Step 2: Classes**

```java
enum VehicleType { BIKE, CAR, TRUCK }
enum SpotType { COMPACT, LARGE, HANDICAPPED }

abstract class Vehicle {
    String plateNumber;
    VehicleType type;
}

class Car extends Vehicle {
    Car(String plate) { this.type = VehicleType.CAR; this.plateNumber = plate; }
}

class ParkingSpot {
    int spotNumber;
    SpotType type;
    Vehicle currentVehicle;        // null = khaali
    boolean isAvailable() { return currentVehicle == null; }
}

class ParkingFloor {
    int floorNumber;
    List<ParkingSpot> spots;
    ParkingSpot findSpot(VehicleType type) { /* ... */ }
}

class ParkingLot {
    List<ParkingFloor> floors;

    Ticket park(Vehicle v) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findSpot(v.type);
            if (spot != null) {
                spot.currentVehicle = v;
                return new Ticket(spot, v);
            }
        }
        return null;    // full hai
    }

    double unpark(Ticket t) {
        t.spot.currentVehicle = null;
        return calculateFee(t);
    }
}
```

👉 **Composition use kiya:** `ParkingLot` has `ParkingFloor`s, floor has `Spot`s. Inheritance sirf `Vehicle` hierarchy me.

## Q3. Design an LRU Cache
**Answer:** Most frequently asked. `HashMap` + `DoublyLinkedList`.

```java
class LRUCache {
    class Node {
        int key, value;
        Node prev, next;
        Node(int k, int v) { key = k; value = v; }
    }

    Map<Integer, Node> map = new HashMap<>();
    Node head = new Node(0, 0), tail = new Node(0, 0);
    int capacity;

    LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;
        remove(node);
        addToFront(node);
        return node.value;
    }

    void put(int key, int value) {
        if (map.containsKey(key)) {
            remove(map.get(key));
        }
        Node node = new Node(key, value);
        addToFront(node);
        map.put(key, node);

        if (map.size() > capacity) {
            Node lru = tail.prev;
            remove(lru);
            map.remove(lru.key);
        }
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addToFront(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
}
```

👉 **`HashMap` = O(1) lookup, `DoublyLinkedList` = O(1) reorder.** Dono milke `get()` aur `put()` dono `O(1)`.

⚠️ **Dummy head aur tail nodes** use karo — boundary cases handle karne me aasani.

**Java me built-in:** `LinkedHashMap` me `removeEldestEntry()` override karo — LRU ready.

## Q4. Design a Rate Limiter
**Answer:** Fixed window / sliding window / token bucket.

```java
class RateLimiter {
    private final int maxRequests;
    private final long windowMillis;
    private final Map<String, Queue<Long>> userRequests = new ConcurrentHashMap<>();

    RateLimiter(int maxRequests, long windowMillis) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
    }

    boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();
        Queue<Long> requests = userRequests.computeIfAbsent(userId, k -> new LinkedList<>());

        // Purane requests hatao (window se bahar)
        while (!requests.isEmpty() && now - requests.peek() > windowMillis) {
            requests.poll();
        }

        if (requests.size() < maxRequests) {
            requests.offer(now);
            return true;
        }
        return false;
    }
}
```

👉 **Sliding window** approach — purane timestamps hatao, count check karo. Production me Redis se hota hai, interview me in-memory.

## Q5. Design a URL Shortener
**Answer:**

```java
class URLShortener {
    private Map<String, String> shortToLong = new ConcurrentHashMap<>();
    private Map<String, String> longToShort = new ConcurrentHashMap<>();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private AtomicLong counter = new AtomicLong(0);

    String shorten(String longUrl) {
        if (longToShort.containsKey(longUrl)) {
            return longToShort.get(longUrl);    // already shortened
        }
        String shortCode = encode(counter.incrementAndGet());
        shortToLong.put(shortCode, longUrl);
        longToShort.put(longUrl, shortCode);
        return "https://short.ly/" + shortCode;
    }

    String expand(String shortCode) {
        return shortToLong.get(shortCode);
    }

    private String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(CHARS.charAt((int) (num % 62)));
            num /= 62;
        }
        return sb.reverse().toString();
    }
}
```

👉 **Base-62 encoding** — `a-z, A-Z, 0-9` = 62 characters. Counter ko base-62 me convert karo. 6 characters me `62^6 = 56 billion` URLs fit ho jaate hain.

## Q6. Composition vs Inheritance — design me
**Answer:**

| | Inheritance | Composition |
|---|---|---|
| Relationship | **is-a** | **has-a** |
| Coupling | Tight ❌ | Loose ✅ |
| Runtime change | Nahi | ✅ Possible |
| Flexibility | Kam | **Zyada** ✅ |

```java
// ❌ Inheritance — Car IS-A Engine? Galat!
class Car extends Engine { }

// ✅ Composition — Car HAS-A Engine. Sahi!
class Car {
    private Engine engine;
}
```

👉 **"Favor composition over inheritance"** — ye design principle hai. Interview me inheritance overuse mat karo.

## Q7. Singleton Pattern — thread safe
**Answer:** Design pattern (topic 13) me tha, ab **thread-safe** version:

```java
class Singleton {
    private static volatile Singleton instance;

    private Singleton() { }

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

👉 **Double-checked locking** — pehla check lock ke bina (performance), dusra lock ke andar (safety). `volatile` zaroori hai — warna partially constructed object dikh sakta hai.

**Better way — enum:**
```java
enum Singleton {
    INSTANCE;
    void doSomething() { }
}
```
Java me **enum Singleton** sabse safe hai — reflection se bhi nahi toot-ta.

## Q8. Observer Pattern
**Answer:** One-to-many — ek badle to sabko pata chale.

```java
interface Observer {
    void update(String news);
}

class NewsAgency {
    private List<Observer> observers = new ArrayList<>();
    private String news;

    void addObserver(Observer o) { observers.add(o); }
    void removeObserver(Observer o) { observers.remove(o); }

    void setNews(String news) {
        this.news = news;
        notifyAll();    // ← sabko batao
    }

    private void notifyAll() {
        for (Observer o : observers) o.update(news);
    }
}

class NewsChannel implements Observer {
    private String name;
    NewsChannel(String name) { this.name = name; }

    public void update(String news) {
        System.out.println(name + " received: " + news);
    }
}
```

👉 **Real world:** event listeners, pub-sub, React state management — sab Observer pattern.

## Q9. Factory Pattern
**Answer:** Object creation alag class me:

```java
interface Shape { void draw(); }
class Circle implements Shape { public void draw() { } }
class Square implements Shape { public void draw() { } }

class ShapeFactory {
    static Shape create(String type) {
        return switch (type) {
            case "circle" -> new Circle();
            case "square" -> new Square();
            default -> throw new IllegalArgumentException("Unknown: " + type);
        };
    }
}

// Use
Shape s = ShapeFactory.create("circle");    // client ko type nahi pata
```

👉 **Faayda:** client ko `new Circle()` nahi karna padta. Naya shape add karo — sirf factory me change, client code nahi badlega.

## Q10. Builder Pattern
**Answer:** Bahut parameters hone pe constructor ugly ho jaata hai.

```java
// ❌ Telescoping constructor
new User("Ashu", "ashu@email.com", 22, "Delhi", "9999999999", true, false, "Male");
// 8 parameters — kaunsa kya hai?!?!?

// ✅ Builder
User user = new User.Builder("Ashu", "ashu@email.com")
        .age(22)
        .city("Delhi")
        .phone("9999999999")
        .build();
```

```java
class User {
    private final String name, email, city, phone;
    private final int age;

    private User(Builder b) {
        this.name = b.name; this.email = b.email;
        this.age = b.age; this.city = b.city; this.phone = b.phone;
    }

    static class Builder {
        private String name, email, city, phone;
        private int age;

        Builder(String name, String email) {   // required fields
            this.name = name; this.email = email;
        }

        Builder age(int age) { this.age = age; return this; }
        Builder city(String city) { this.city = city; return this; }
        Builder phone(String phone) { this.phone = phone; return this; }
        User build() { return new User(this); }
    }
}
```

👉 **`return this`** — method chaining possible hoti hai. Lombok ka `@Builder` ye automatically generate kar deta hai.

## Q11. SOLID quick recap (design context me)
**Answer:**

| Principle | Design me matlab |
|---|---|
| **S** — Single Responsibility | Ek class = ek kaam |
| **O** — Open/Closed | Extend karo, modify mat karo |
| **L** — Liskov Substitution | Subclass parent ki jagah fit honi chahiye |
| **I** — Interface Segregation | Chhote interfaces > ek bada |
| **D** — Dependency Inversion | Concrete pe nahi, interface pe depend |

👉 Design round me inhe consciously apply karo — interviewer specifically notice karta hai.

---

> 💡 **Interview tip:** design question me **pehle requirements confirm karo** — *"Kya multi-threading support chahiye? Scale kitna hai? Persistence chahiye ya in-memory?"* — ye questions dikhate hain tum sirf code nahi, **system** soch rahe ho. Phir class diagram banao (verbally bhi chalega), phir code. Seedha code likhne wale fail hote hain.
