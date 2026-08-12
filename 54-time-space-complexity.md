# 54 — Time & Space Complexity (Big-O) ⏱️

Har coding round me **"iski complexity kya hai?"** poocha jaata hai. Code sahi ho par complexity galat bata do — interviewer ka bharosa uth jaata hai.

## Q1. Big-O hota kya hai?
**Answer:** Big-O batata hai ki **input badhne pe kaam kitna badhta hai** — exact time nahi.

👉 **Seconds nahi naapte, growth naapte hain.** `O(n)` ka matlab nahi ki `n` second lagenge. Matlab: input **double** karoge to kaam bhi lagbhag **double** hoga.

**Constants ignore hote hain:**
```java
O(2n)      → O(n)
O(n + 100) → O(n)
O(3n² + 5n + 7) → O(n²)      // sirf sabse bada term bachta hai
```

**Kyun?** Kyunki `n` bahut bada hone pe `n²` ke saamne `5n` kuch bhi nahi hai.

## Q2. Common complexities — tez se slow
**Answer:**

| Big-O | Naam | Example |
|---|---|---|
| `O(1)` | Constant | `arr[5]`, `map.get()` |
| `O(log n)` | Logarithmic | Binary search |
| `O(n)` | Linear | Ek loop |
| `O(n log n)` | Linearithmic | Merge sort, `Arrays.sort()` |
| `O(n²)` | Quadratic | Nested loop |
| `O(2ⁿ)` | Exponential | Bina memo wala recursion |
| `O(n!)` | Factorial | Saare permutations |

**Feel ke liye** — `n = 1,000,000` pe roughly:

| Complexity | Steps |
|---|---|
| `O(log n)` | ~20 |
| `O(n)` | 10 lakh |
| `O(n log n)` | 2 crore |
| `O(n²)` | 10 kharab ❌ (ghanton lagega) |

## Q3. Complexity nikalte kaise hain?
**Answer:** Loops gino — seedha rule hai.

```java
// O(1) — loop hi nahi
int x = arr[0] + arr[1];

// O(n) — ek loop
for (int i = 0; i < n; i++) { ... }

// O(n²) — nested loop
for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++) { ... }

// O(n) — alag-alag loops JUDTE hain, gunaa nahi hote
for (int i = 0; i < n; i++) { ... }
for (int j = 0; j < n; j++) { ... }
// O(n) + O(n) = O(2n) = O(n)

// O(log n) — har baar aadha
while (n > 1) { n = n / 2; }
```

⚠️ **Nested = multiply, sequential = add.** Ye do rule 90% cases cover kar lete hain.

## Q4. Ye nested loop `O(n²)` hai ya nahi?
**Answer:** Trick question — dhyan se dekho:

```java
for (int i = 0; i < n; i++)
    for (int j = i + 1; j < n; j++)      // j = i+1 se shuru!
        ...
```

Total iterations: `(n-1) + (n-2) + ... + 1 = n(n-1)/2`

→ `O(n²/2)` → **`O(n²)`** ✅

👉 Aadha kaam hai par **phir bhi `O(n²)`** — constant `1/2` ignore hota hai.

**Par ye `O(n²)` nahi hai:**
```java
for (int i = 0; i < n; i++)
    for (int j = 0; j < 100; j++)        // fixed 100
        ...
```
→ `O(100n)` → **`O(n)`** ✅ — andar wala loop `n` pe depend hi nahi karta.

## Q5. `O(log n)` kahan se aata hai?
**Answer:** Jab har step pe **kaam aadha** ho jaaye.

```java
while (left <= right) {
    int mid = ...;
    if (...) left = mid + 1;    // aadha discard
    else right = mid - 1;
}
```

`n` → `n/2` → `n/4` → ... → `1`

Kitne steps? `log₂(n)`. Isliye 10 lakh elements me sirf **~20** steps.

👉 Signal: **"aadha karna"**, **balanced tree**, **divide & conquer** → wahan `log n` hoga.

## Q6. Space complexity kya hai?
**Answer:** **Extra** memory jo tumne use ki — input ki memory nahi ginte.

```java
// O(1) space — sirf kuch variables
static int sum(int[] arr) {
    int total = 0;
    for (int num : arr) total += num;
    return total;
}

// O(n) space — nayi array banayi
static int[] doubled(int[] arr) {
    int[] result = new int[arr.length];
    ...
}
```

⚠️ **Recursion me call stack bhi space hai!**
```java
static int fact(int n) {
    if (n <= 1) return 1;
    return n * fact(n - 1);      // O(n) space — n stack frames
}
```
Log isko `O(1)` bata dete hain — galat hai. Interviewer specially yahi pakadta hai.

## Q7. Amortized complexity kya hai?
**Answer:** "Average over many operations" — `ArrayList.add()` ka classic case.

- Normally `add()` = `O(1)`
- Par array bhar jaaye to **double size ki nayi array** banti hai aur sab copy hota hai = `O(n)`

**To `add()` `O(1)` hai ya `O(n)`?**

👉 **Amortized `O(1)`.** Kyunki resize **kabhi-kabhi** hota hai. `n` additions me total kaam `O(n)` hi hota hai (`1 + 2 + 4 + 8 + ... + n ≈ 2n`), to per operation **average `O(1)`**.

**Isi tarah:** `HashMap.put()` bhi amortized `O(1)` hai.

## Q8. Best / Average / Worst case
**Answer:** Teeno alag ho sakte hain:

```java
static boolean search(int[] arr, int target) {
    for (int num : arr) if (num == target) return true;
    return false;
}
```

- **Best:** `O(1)` — pehla hi element mil gaya
- **Average:** `O(n/2)` = `O(n)`
- **Worst:** `O(n)` — aakhir me ya hai hi nahi

⚠️ **Big-O by default worst case hi hota hai** — jab tak alag na bola jaaye. Interview me worst case hi batao.

**Quick sort** iska best example hai: average `O(n log n)`, worst `O(n²)` (topic 45).

## Q9. Java collections ki complexity — cheat sheet
**Answer:** Ye table interview me seedha poocha jaata hai:

| Operation | `ArrayList` | `LinkedList` | `HashMap` | `TreeMap` |
|---|---|---|---|---|
| Get by index | `O(1)` ✅ | `O(n)` | — | — |
| Get by key | — | — | `O(1)` ✅ | `O(log n)` |
| Add at end | `O(1)`* | `O(1)` | `O(1)`* | `O(log n)` |
| Add at start | `O(n)` | `O(1)` ✅ | — | — |
| Remove by index | `O(n)` | `O(n)`† | — | — |
| Search (`contains`) | `O(n)` | `O(n)` | `O(1)` ✅ | `O(log n)` |
| Sorted order | ❌ | ❌ | ❌ | ✅ |

`*` amortized · `†` traverse karna padta hai

⚠️ **`HashMap` worst case `O(n)` hai** — saari keys ek hi bucket me chali jaayein to. Java 8 se tree-fy hone ki wajah se ab worst case **`O(log n)`** hai (topic 19).

## Q10. Complexity kam kaise karein — common upgrades
**Answer:**

| Se | Ko | Kaise |
|---|---|---|
| `O(n²)` | `O(n)` | HashMap se lookup (topic 48) |
| `O(n²)` | `O(n)` | Two pointers / sliding window (topic 42) |
| `O(n)` | `O(log n)` | Sorted hai to binary search (topic 44) |
| `O(2ⁿ)` | `O(n)` | Memoization (topic 51) |
| `O(n log n)` | `O(n)` | Counting sort (range chhoti ho) |

👉 **Nested loop dikhe to hamesha socho** — "kya HashMap ya two pointers se ek pass me ho sakta hai?"

## Q11. Common galatiyan
**Answer:** Ye galtiyan interview me bahut hoti hain:

```java
// ❌ "String concat loop me O(n) hai"
String s = "";
for (int i = 0; i < n; i++) s += i;      // ACTUALLY O(n²)!
```
String immutable hai — har `+=` **nayi string** banata hai. `StringBuilder` use karo → `O(n)`.

```java
// ❌ "list.contains() O(1) hai"
for (int num : arr) {
    if (list.contains(num)) ...          // O(n) andar O(n) = O(n²)!
}
```
`HashSet` use karo → `contains()` `O(1)` → total `O(n)`.

```java
// ❌ "recursion me space O(1) hai"
// Call stack bhi ginta hai → O(depth)
```

## Q12. Interview me complexity kaise batayein?
**Answer:** Sirf `O(n)` bolna kaafi nahi — **wajah** bhi batao:

> ❌ *"Ye `O(n)` hai."*
>
> ✅ *"Time `O(n)` hai kyunki main array ek hi baar traverse kar raha hoon, aur HashMap ka lookup `O(1)` hai. Space bhi `O(n)` hai worst case me, jab saare elements distinct hon aur map me chale jaayein."*

**Dono batao — time aur space.** Log space bhool jaate hain, aur interviewer aksar wahi follow-up karta hai.

---

> 💡 **Interview tip:** code likhne ke **turant baad, bina pooche** complexity bol do — *"iska time `O(n log n)` hai aur space `O(n)`"*. Ye chhoti si aadat bahut mature dikhati hai. Aur agar solution optimal nahi hai to **khud bol do** — *"abhi `O(n²)` hai, HashMap se `O(n)` kar sakta hoon"*. Ye honesty interviewer ko impress karti hai.
