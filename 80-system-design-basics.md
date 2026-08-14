# 80 — System Design Basics (Freshers ke liye) 🏗️

"URL shortener design karo" / "Instagram kaise banaoge?" — freshers se **HLD ka deep design** nahi, par **building blocks** zaroor poochhe jaate hain. Ye wo blocks hain.

## Q1. System design me kya dekha jaata hai?
**Answer:** Interviewer ye dekhta hai:

1. Requirements poochhte ho ya seedha code me kood jaate ho?
2. Scale sochte ho — 100 users vs 100 million?
3. Building blocks jaante ho — cache, queue, load balancer?
4. Trade-offs samajhte ho — har choice ki cost?

👉 **Kabhi seedha design mat shuru karo.** Pehle poochho: *"Kitne users? Read-heavy ya write-heavy? Latency kitna important hai?"*

## Q2. Scaling — vertical vs horizontal
**Answer:**

| | Vertical (Scale Up) | Horizontal (Scale Out) |
|---|---|---|
| Kya | Server me aur RAM/CPU | **Aur servers** add karo |
| Limit | Ek machine ki max limit | Almost unlimited ✅ |
| Cost | Tez par mehenga | Saste servers, zyada |
| Failure | Ek gaya to sab gaya ❌ | Ek gaya to baaki chal rahe ✅ |

👉 Real systems **horizontal** scale karte hain — isliye stateless services (topic 76) itni important hain.

## Q3. Load Balancer kya hai?
**Answer:** Traffic ko servers me baantne wala:

```
         Users
           ↓
    ┌─────────────┐
    │ Load Balancer│
    └─────────────┘
      ↓     ↓     ↓
   Server1 Server2 Server3
```

**Kaam:**
- Requests distribute (round-robin / least-connections)
- Server down → uspe traffic bhejna band (health checks)
- Ek server add/remove karna aasan

👉 Iske bina ek server pe sab load → crash.

## Q4. Caching — sabse important block
**Answer:** **Fast storage me frequently-used data** rakho, DB hit mat do baar-baar.

```
Request → Cache check → mila? → return (fast! ~1ms)
                      → nahi? → DB se lao → cache me daalo → return
```

**Redis** sabse popular hai — in-memory, isliye tez.

**Kya cache karein:**
- User sessions
- Product catalog (rarely badalta)
- Baar-baar same query ke results
- Rate limiting counters

⚠️ **Cache invalidation** — DB me data badla par cache purana → stale data. Isliye TTL (time-to-live) set karo: `cache.set(key, value, 300 sec)`

👉 **Famous quote:** *"There are only two hard things in Computer Science: cache invalidation and naming things."*

## Q5. Database scaling
**Answer:** Teen levels:

**1. Indexing** — pehla step
```sql
CREATE INDEX idx_email ON users(email);
-- WHERE email = ? ab full table scan nahi karega
```

**2. Replication** — read replicas
```
Writes → Master DB
Reads  → Replica 1, Replica 2, Replica 3
```
Zyada tar apps **read-heavy** hoti hain — reads ko replicas pe baant do.

**3. Sharding** — data ko alag-alag DBs me baanto
```
users 1-1M     → DB1
users 1M-2M    → DB2
```
Powerful par complex — joins mushkil, rebalancing hard.

## Q6. SQL vs NoSQL — kab kya?
**Answer:**

| | SQL (MySQL/Postgres) | NoSQL (MongoDB/Cassandra) |
|---|---|---|
| Data | Structured, tables | Flexible, documents |
| Relations | ✅ Joins | ❌ / limited |
| Consistency | Strong (ACID) ✅ | Eventual |
| Scale | Vertical mostly | Horizontal ✅ |
| Kab | Transactions, money, inventory | Logs, feeds, catalogs, sessions |

👉 **Interview jawab:** *"Money/orders ke liye SQL (consistency zaroori), activity feed/sessions ke liye NoSQL (scale zaroori)."*

## Q7. Message Queue kyun?
**Answer:** Slow kaam ko **baad me** karo — user ko turant response do.

```
❌ Bina queue: Order place → email bhejo (2 sec) → payment SMS (2 sec) → response (user 5 sec wait!)

✅ Queue ke saath: Order place → queue me daalo → turant response
                   Email worker: apne time pe bhejega
```

**Use cases:** emails, notifications, image processing, report generation
**Tools:** Kafka (high-throughput events), RabbitMQ (task queues)

## Q8. CDN kya hai?
**Answer:** Static content (images, videos, CSS/JS) ko **user ke paas** wale servers se serve karo.

```
User in Delhi → Delhi CDN server se image (5ms)
               instead of US server se (200ms)
```

Cloudflare, CloudFront. Static content ke liye must-have.

## Q9. URL Shortener — mini design
**Answer:** Classic fresher question. Approach:

```
1. POST /shorten {longUrl}
   → counter++ → Base62 encode → "xyz12"
   → DB: xyz12 → longUrl
   → return short.ly/xyz12

2. GET /xyz12
   → Cache check (Redis) → miss? → DB lookup → cache me daalo
   → 302 Redirect to longUrl
```

**Key points bolne hain:**
- Base62 encoding (62 chars = compact)
- Cache pe redirects (read-heavy system!)
- 301 vs 302 redirect (302 = analytics track ho jaayega)

## Q10. CAP theorem — ek line me
**Answer:** Distributed system me teeno ek saath nahi mil sakte:

- **C**onsistency — sabko same data dikhe
- **A**vailability — har request ka jawab mile
- **P**artition tolerance — network toot-ne pe bhi chale

Network fail **hoga hi** (P to chahiye hi) — to choice hai: **CP** (consistency, kuch requests fail) ya **AP** (availability, thoda stale data).

👉 Banking → CP. Social media feed → AP (thoda purana post chalega).

## Q11. Design approach — 5 steps
**Answer:** Koi bhi design question me ye order follow karo:

```
1. REQUIREMENTS  — kya banana hai? kitne users? features?
2. ESTIMATION    — QPS, storage ka andaza
3. API DESIGN    — endpoints kya honge
4. HIGH LEVEL    — LB → App servers → Cache → DB diagram
5. DEEP DIVE     — 1-2 components detail me + trade-offs
```

⚠️ Step 1 skip mat karna — requirements na poochna sabse badi galti hai.

---

> 💡 **Interview tip:** fresher ho to bolo — *"maine production-scale systems design nahi kiye, par building blocks samajhta hoon: load balancer traffic baant-ta hai, cache DB load kam karti hai, queue slow kaam async karti hai."* Concepts + honesty, fake experience se hamesha better hai. Aur design me hamesha trade-off bolo: *"ye approach fast hai par eventually consistent hai — money wale flow me use nahi karunga."*
