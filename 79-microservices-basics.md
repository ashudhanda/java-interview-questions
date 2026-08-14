# 79 — Microservices Basics 🧩

Monolith vs Microservices — ye ab har backend interview me aata hai. Fresher se production design nahi maangte, par **concepts aur trade-offs** zaroor poochte hain.

## Q1. Monolith kya hai?
**Answer:** Poora application **ek hi codebase + ek hi deployable unit**.

```
┌─────────────────────────────┐
│         MY APP              │
│  Users + Orders + Payments  │
│  + Products + Notifications │
└─────────────────────────────┘
           ↓
      Ek database
```

**Faayde:** simple, develop/test/deploy aasan, chhoti team ke liye best
**Problems:**
- Ek feature ka bug → poori app down
- Ek module ko scale karna ho → poori app scale karni padti hai
- 50 developers ek codebase pe → merge conflicts daily

## Q2. Microservices kya hain?
**Answer:** App ko **chhote independent services** me todo — har service ek business capability.

```
┌──────────┐ ┌──────────┐ ┌──────────┐
│  User    │ │  Order   │ │ Payment  │
│ Service  │ │ Service  │ │ Service  │
└────┬─────┘ └────┬─────┘ └────┬─────┘
     │            │            │
  own DB       own DB       own DB
```

**Har service:**
- Apna codebase, apna database
- Alag deploy hoti hai
- Doosri se **API (REST/gRPC)** ya **events (Kafka)** se baat karti hai

## Q3. Monolith vs Microservices — kya choose karein?
**Answer:**

| | Monolith | Microservices |
|---|---|---|
| Develop karna | Aasan ✅ | Complex |
| Deploy | Ek unit — simple | Kai services — complex |
| Scale | Poori app ek saath | Sirf zaroori service ✅ |
| Team | Chhoti team | Badi teams, independent ✅ |
| Debug | Ek jagah logs ✅ | Distributed tracing chahiye |
| Data consistency | ACID transactions ✅ | Eventual consistency |
| Startup ke liye | ✅ Best | ❌ Overkill |

👉 **Senior-level jawab:** *"Main monolith se start karunga — microservices tab jab team badi ho ya scaling needs aayein. Premature microservices sirf complexity badhate hain."* Yahi sahi answer hai.

## Q4. Services aapas me kaise baat karti hain?
**Answer:** Do tareeke:

**1. Synchronous (REST/gRPC)**
```
Order Service → Payment Service: "charge karo"
                ← response ka wait karta hai
```
- Simple, par **coupling** — Payment down → Order bhi fail

**2. Asynchronous (Events/Message Queue)**
```
Order Service → Kafka: "OrderCreated event"
Payment Service: apne time pe consume karke process karega
```
- **Loosely coupled** — Payment down ho to event queue me wait karega
- Par complex — event ordering, duplicates handle karne padte hain

## Q5. Service Discovery aur API Gateway
**Answer:**

**API Gateway** — sab requests ka ek entry point:
```
Client → API Gateway → User Service
                     → Order Service
                     → Payment Service
```
Kaam: routing, auth, rate limiting, logging. (Spring Cloud Gateway)

**Service Discovery** — services ek dusre ko dhoondhein kaise:
- Har service registry (Eureka/Consul) me register hoti hai
- Kisi ko call karna ho → registry se address poochho
- Kyunki cloud me instances up/down hote rehte hain, IPs dynamic hoti hain

## Q6. Circuit Breaker kya hai?
**Answer:** Ek service down hai → usse **baar-baar call mat karo** — fail fast.

```
Normal:     Payment → [call] → OK
Down hui:   Payment → [call] → timeout (5 sec waste!)
Circuit OPEN: Payment → [turant fallback] → "Payment abhi unavailable"
```

**Teen states:**
- **CLOSED** — normal, calls jaati hain
- **OPEN** — calls block, fallback return (failures threshold cross hone pe)
- **HALF-OPEN** — thodi der baad ek test call — pass hui to CLOSED

👉 **Resilience4j** library Java me ye deti hai. Bina circuit breaker ke ek slow service **poori chain** ko hang kar sakti hai (cascading failure).

## Q7. Distributed transactions — Saga pattern
**Answer:** Microservices me ek transaction kai services pe phaili ho:

```
Order banao → Payment kato → Inventory ghatao → Ship karo
```

Agar **inventory step fail** ho jaaye? Payment to ho chuki! 😱

**Saga = har step ka ek compensating action:**
```
1. Order banao      (undo: order cancel)
2. Payment kato     (undo: refund)
3. Inventory fail!  → compensating: refund + order cancel
```

👉 Har step fail pe **ulte actions** chalao. Isliye microservices me **eventual consistency** hoti hai — turant nahi, thodi der me sab theek.

## Q8. Database per Service — kyun?
**Answer:** Har service ka **apna database** — shared database = tight coupling.

```
❌ Sab services ek DB share      → schema change = sab toot jaate hain
✅ Har service apna DB           → independent deploy/change
```

**Problem:** doosri service ka data chahiye to? 
- ❌ Uski DB directly mat kholo
- ✅ Uski API call karo, ya events se apni local copy maintain karo

## Q9. Distributed tracing
**Answer:** Ek request 5 services se guzri aur fail hui — **kahan?**

**Correlation ID se track karo:**
```
Request aayi → ID: abc123
User Service   [abc123] ...
Order Service  [abc123] ...
Payment        [abc123] ERROR! ← yahan mili
```

Tools: **Sleuth + Zipkin** / OpenTelemetry. Har log me same ID — poori journey ek jagah dikh jaati hai.

## Q10. Microservices ki challenges
**Answer:** Interview me ye bhi poochte hain:

| Challenge | Solution |
|---|---|
| Service dhoondhna | Service Discovery (Eureka) |
| Ek down → sab down | Circuit Breaker (Resilience4j) |
| Cross-service transactions | Saga pattern |
| Config har jagah | Config Server |
| Logs bikhar jaana | Centralized logging (ELK) |
| Testing mushkil | Contract testing |
| Deploy complex | Docker + Kubernetes |

## Q11. Interview me kaise bolein?
**Answer:**

> *"Microservices har business capability ki alag service hai — apna DB, apna deploy. Fayda: independent scaling aur teams. Par complexity badhti hai — service discovery, circuit breakers, distributed tracing sab chahiye. Main chhote project me monolith se start karunga, aur jab scale ya team ki zaroorat ho tab services todunga."*

⚠️ **Kabhi mat bolna** "microservices hamesha best hain" — ye junior lagta hai. Trade-offs samajhna hi asli answer hai.

---

> 💡 **Interview tip:** fresher ho to production microservices ka experience fake mat karo. Bolo: *"maine ek Spring Boot monolith banaya hai, aur microservices ke concepts — API gateway, circuit breaker, saga — samajhta hoon aur ek chhote demo me try kiye hain."* Honesty + concepts = perfect combination.
