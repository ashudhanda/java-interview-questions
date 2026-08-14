# 76 — REST API & HTTP Basics 🌐

Backend Java developer ka interview bina REST ke complete nahi hota. Status codes, methods, aur API design — ye teeno pakke hone chahiye.

## Q1. REST kya hai?
**Answer:** **RE**presentational **S**tate **T**ransfer — APIs banane ka ek style:

- Har cheez ek **resource** hai (user, order, product)
- Resource ka **URL** hota hai: `/users/42`
- Resource pe **HTTP methods** se operate karte ho
- Server **stateless** hai — har request me poori info hoti hai
- Response usually **JSON** me

```
GET    /users        → saare users lao
GET    /users/42     → user 42 lao
POST   /users        → naya user banao
PUT    /users/42     → user 42 poora update
PATCH  /users/42     → user 42 ka kuch update
DELETE /users/42     → user 42 hatao
```

## Q2. HTTP methods — kaun kab?
**Answer:**

| Method | Kaam | Body? | Idempotent? |
|---|---|---|---|
| **GET** | Data lao | ❌ | ✅ |
| **POST** | Naya banao | ✅ | ❌ |
| **PUT** | Poora replace | ✅ | ✅ |
| **PATCH** | Partial update | ✅ | ❌ |
| **DELETE** | Hatao | ❌ | ✅ |

⚠️ **Idempotent ka matlab:** do baar bhejo to bhi result same. `DELETE /users/42` do baar bhejo — user to delete hi hai. Par `POST /users` do baar bhejo — **do users ban jaayenge!**

👉 Interview favourite: *"PUT aur PATCH me farak?"* — PUT me poora object bhejo, PATCH me sirf badla hua field.

## Q3. Status codes — zaroori wale
**Answer:**

| Code | Matlab | Kab |
|---|---|---|
| **200** | OK | Success |
| **201** | Created | POST success |
| **204** | No Content | DELETE success |
| **400** | Bad Request | Client ne galat data bheja |
| **401** | Unauthorized | Login nahi kiya |
| **403** | Forbidden | Login hai, par permission nahi |
| **404** | Not Found | Resource hai hi nahi |
| **409** | Conflict | Duplicate (email already exists) |
| **500** | Internal Error | Server crash |

👉 **401 vs 403** — ye bahut poochte hain:
- **401** = "tum ho kaun?" (authentication fail)
- **403** = "tum jaanta hoon, par allowed nahi" (authorization fail)

## Q4. Achhe API URL kaise banayein?
**Answer:**

```
✅ /users/42/orders           — user 42 ke orders
✅ /orders?status=pending     — filter query param me
✅ /products?page=2&limit=20  — pagination

❌ /getUsers                  — method URL me mat daalo
❌ /users/delete/42           — DELETE method use karo
❌ /user_list                 — snake_case nahi, plural nouns
```

**Rules:**
1. **Nouns use karo, verbs nahi** — HTTP method hi verb hai
2. **Plural:** `/users` not `/user`
3. **Hierarchy:** `/users/42/orders/7`
4. **Filter/sort/paginate = query params**

## Q5. Request ka anatomy
**Answer:**

```http
POST /users HTTP/1.1              ← method + path
Host: api.example.com
Authorization: Bearer eyJhbG...    ← auth token
Content-Type: application/json    ← body ka format

{                                 ← body
  "name": "Ashu",
  "email": "ashu@mail.com"
}
```

**Parts:**
- **Method + URL** — kya karna hai, kahan
- **Headers** — metadata (auth, content type)
- **Body** — asli data (POST/PUT/PATCH me)

## Q6. Authentication kaise hoti hai?
**Answer:**

**Basic Auth:** `Authorization: Basic base64(user:pass)` — sirf testing ke liye

**Bearer Token / JWT:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```
JWT me teen parts hote hain: `header.payload.signature`
- **Header:** algorithm
- **Payload:** data (userId, expiry)
- **Signature:** tamper-proof seal

👉 **JWT ka faayda:** server ko session store karne ki zaroorat nahi — token khud me hi info hai. **Nuksaan:** ban karne pe bhi token expire tak valid rehta hai.

## Q7. Idempotency aur safe retries
**Answer:**

```
User ne "Pay" dabaya → network slow → dobara dabaya
→ DOUBLE PAYMENT?! ❌
```

**Solution — Idempotency Key:**
```http
POST /payments
Idempotency-Key: abc-123-unique

{ "amount": 500 }
```
Server key yaad rakhta hai — same key wapas aaye to **naya payment nahi**, purana result return karta hai.

👉 Stripe, Razorpay sab ye pattern use karte hain.

## Q8. API Versioning
**Answer:** API badalni hai par purane clients tootne nahi chahiye:

```
✅ /v1/users            — URL me (sabse common)
✅ /v2/users

Headers me bhi ho sakta hai:
Accept: application/vnd.myapp.v2+json
```

👉 **Rule:** breaking change → naya version. Non-breaking (naya field add) → same version me theek hai.

## Q9. Error response ka format
**Answer:** Achhi API consistent errors deti hai:

```json
{
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "Email format galat hai",
    "field": "email",
    "timestamp": "2026-08-14T10:30:00Z"
  }
}
```

❌ **Mat karo:**
- Stack trace client ko bhejna (security risk)
- Sirf `"error"` likhna (kaunsa error?)
- Har endpoint ka alag format

## Q10. Stateless kyun important hai?
**Answer:** Server **kuch yaad nahi rakhta** requests ke beech me.

```
Request 1: POST /login → token mila
Request 2: GET /profile (token ke saath) → server ne token verify kiya
```

Server ne request 1 ka **koi record nahi rakha**. Har request independent hai.

👉 **Faayda:** kisi bhi server pe request bhej do — kaam chalega. Isliye **horizontal scaling** aasan hai (load balancer pe 10 servers laga do).

⚠️ Session-based auth (server pe session store) = **stateful** = scaling mushkil. Isliye JWT popular hua.

## Q11. Common interview questions
**Answer:**

**"GET me body bhej sakte hain?"** — Technically haan, par **mat bhejo** — kai servers ignore kar dete hain.

**"POST vs PUT — naya create karne me?"** — POST jab server ID banata hai (`POST /users` → 201, ID: 42). PUT jab client ID jaanta hai (`PUT /users/42`).

**"CORS kya hai?"** — Browser security rule: `site-a.com` ka JavaScript `api-b.com` ko call nahi kar sakta jab tak wo server allow na kare (`Access-Control-Allow-Origin` header).

**"HTTP vs HTTPS?"** — HTTPS = HTTP + encryption (TLS). Data beech me koi padh nahi sakta. Production me hamesha HTTPS.

---

> 💡 **Interview tip:** REST poochhein to ye 4-line answer do: *"REST resources pe based hai — har resource ka URL hota hai, HTTP methods actions hote hain (GET lao, POST banao, PUT/PATCH update, DELETE hatao), server stateless hai, aur responses JSON me hote hain with proper status codes jaise 201 create ke liye, 400 validation ke liye."* Phir 401 vs 403 ka farak khud bata do — interviewer ka favourite wahi hai.
