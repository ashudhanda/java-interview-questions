# 59 — Greedy Algorithms 💰

Greedy matlab — **har step pe jo sabse achha dikhta hai, wahi lo.** Koi aage ka plan nahi. Kabhi perfect answer deta hai, kabhi galat — ye farak samajhna hi asli skill hai.

## Q1. Greedy kya hota hai?
**Answer:** Do rules:

1. **Local best choice** lo — abhi jo sabse achha lagta hai
2. **Kabhi wapas mat aao** — decision final hai, badloge nahi

👉 **Real life example:** paise wapas dene hain ₹37 — tum pehle sabse bada note lete ho (₹20), phir ₹10, phir ₹5, phir ₹2. Har baar **bade se bada** liya. Yahi greedy hai.

⚠️ **Greedy hamesha optimal NAHI hota.** Kabhi-kabhi local best se global best nahi milta.

## Q2. Activity Selection — classic greedy
**Answer:** Sabse zyada activities karo — overlap nahi honi chahiye.

```java
static int maxActivities(int[][] activities) {
    // End time se sort karo — yahi greedy choice hai
    Arrays.sort(activities, (a, b) -> a[1] - b[1]);

    int count = 1;
    int lastEnd = activities[0][1];

    for (int i = 1; i < activities.length; i++) {
        if (activities[i][0] >= lastEnd) {    // overlap nahi hai
            count++;
            lastEnd = activities[i][1];
        }
    }
    return count;
}
```

**Greedy choice:** jo activity **sabse jaldi khatam** ho, usse pehle lo — taaki baaki ke liye zyada waqt bache.

⚠️ **Start time se sort karoge to galat** ho jaayega. Koi activity subah 6 baje shuru ho aur raat 10 tak chale — pehle le liya to baaki sab chutenge. **End time hi** sahi sort hai.

## Q3. Fractional Knapsack
**Answer:** Items tod sakte ho (0/1 nahi hai) — greedy perfect kaam karta hai.

```java
static double fractionalKnapsack(int[] values, int[] weights, int capacity) {
    int n = values.length;
    Integer[] idx = new Integer[n];
    for (int i = 0; i < n; i++) idx[i] = i;

    // Value/weight ratio se sort — sabse valuable pehle
    Arrays.sort(idx, (a, b) -> Double.compare(
        (double) values[b] / weights[b], 
        (double) values[a] / weights[a]
    ));

    double totalValue = 0;
    for (int i : idx) {
        if (capacity >= weights[i]) {
            capacity -= weights[i];
            totalValue += values[i];           // poora le lo
        } else {
            totalValue += values[i] * ((double) capacity / weights[i]);  // fraction
            break;                              // bag full
        }
    }
    return totalValue;
}
```

👉 **Ye greedy kyun optimal hai?** Kyunki tod sakte ho. Agar **tod nahi sakte** (0/1 knapsack) to greedy **galat** dega — wahan DP chahiye (topic 51).

| | Fractional | 0/1 |
|---|---|---|
| Tod sakte ho? | ✅ Haan | ❌ Nahi |
| Algorithm | **Greedy** ✅ | **DP** ✅ |
| Greedy optimal? | ✅ | ❌ |

## Q4. Jump Game
**Answer:** Last index tak pahunch sakte ho ya nahi?

```java
static boolean canJump(int[] nums) {
    int maxReach = 0;

    for (int i = 0; i < nums.length; i++) {
        if (i > maxReach) return false;        // yahan tak hi nahi pahunche
        maxReach = Math.max(maxReach, i + nums[i]);

        if (maxReach >= nums.length - 1) return true;   // pahunch gaye!
    }
    return true;
}
```

**Greedy choice:** har step pe **farthest reachable** track karo. Agar current index `maxReach` se aage chala gaya — matlab wahan tak jaana possible hi nahi.

**Time:** `O(n)` — ek hi pass.

## Q5. Jump Game II — minimum jumps
**Answer:**

```java
static int minJumps(int[] nums) {
    int jumps = 0, currentEnd = 0, farthest = 0;

    for (int i = 0; i < nums.length - 1; i++) {
        farthest = Math.max(farthest, i + nums[i]);

        if (i == currentEnd) {          // current jump ka coverage khatam
            jumps++;
            currentEnd = farthest;
        }
    }
    return jumps;
}
```

👉 **Logic:** `currentEnd` batata hai "current jump me main kahantak jaa sakta hoon". Us boundary pe pahunchte hi **naya jump** chahiye. Ye BFS jaisa hai par space `O(1)`.

## Q6. Coin Change me greedy KAB galat hota hai?
**Answer:** Ye **sabse important** question hai — greedy ki limitation dikhata hai.

```
Coins: [1, 3, 4],  Amount: 6

Greedy:  4 + 1 + 1 = 3 coins
Optimal: 3 + 3     = 2 coins  ✅

Greedy GALAT!
```

**Kab kaam karta hai:** Indian coins `[1, 2, 5, 10, 20, 50, 100, 500, 2000]` — inpe greedy **hamesha optimal** hai (canonical coin system).

**Kab nahi:** `[1, 3, 4]` jaise arbitrary sets. Wahan **DP** chahiye (topic 51, Q7).

👉 **Interview me ye bolo:** *"Greedy canonical coins pe kaam karta hai, par arbitrary denominations pe fail hota hai — uske liye DP use karta hoon."* Ye ek line tumhe alag level pe le jaati hai.

## Q7. Minimum Platforms (Railway Station)
**Answer:** Kitne platforms chahiye taaki koi train wait na kare?

```java
static int minPlatforms(int[] arrivals, int[] departures) {
    Arrays.sort(arrivals);
    Arrays.sort(departures);

    int platforms = 1, maxPlatforms = 1;
    int i = 1, j = 0;

    while (i < arrivals.length && j < departures.length) {
        if (arrivals[i] <= departures[j]) {
            platforms++;        // nayi train aa gayi, pehli abhi gayi nahi
            i++;
        } else {
            platforms--;        // ek train chali gayi
            j++;
        }
        maxPlatforms = Math.max(maxPlatforms, platforms);
    }
    return maxPlatforms;
}
```

👉 **Two-pointer + sort** — ye pattern kai greedy questions me aata hai.

## Q8. Assign Cookies to Children
**Answer:** Greedy ka simplest example.

```java
static int findContentChildren(int[] greed, int[] cookies) {
    Arrays.sort(greed);
    Arrays.sort(cookies);

    int child = 0, cookie = 0;
    while (child < greed.length && cookie < cookies.length) {
        if (cookies[cookie] >= greed[child]) {
            child++;            // cookie se child khush
        }
        cookie++;               // cookie use ho gayi (ya chhoti thi)
    }
    return child;
}
```

👉 **Greedy choice:** sabse **kam greedy** baccho ko pehle satisfy karo — aasan targets pehle.

## Q9. Greedy kab use karein — pehchan
**Answer:**

| Signal | Algorithm |
|---|---|
| Sort karke sequential decision le sakte ho | Greedy |
| Har step ka local best = global best ho | Greedy |
| "maximum activities / meetings fit karo" | Greedy (sort by end) |
| Coin change, Indian coins | Greedy |
| Coin change, arbitrary coins | **DP, greedy nahi** |
| "kya ye possible hai" (jump game type) | Greedy |
| Optimal guarantee nahi chahiye, sirf achha chahiye | Greedy |

⚠️ **Greedy prove karna mushkil hai.** Interview me bolo: *"Main greedy try karunga kyunki... aur ye optimal hai kyunki [reason]. Agar doubt ho to DP se compare karunga."*

## Q10. Greedy vs DP — clear difference
**Answer:**

| | Greedy | DP |
|---|---|---|
| Decision | Ek baar, final | Sab options explore |
| Speed | **Fast** ✅ (usually `O(n log n)`) | Slow (`O(n²)`+) |
| Always optimal? | ❌ Nahi | ✅ Haan |
| Implementation | **Aasan** ✅ | Mushkil |
| Kab use | Jab proof ho ki local best = global | Jab greedy fail ho |

👉 **Strategy:** pehle greedy socho. Counter-example mile to DP. Dono me confusion ho to **brute force** likho phir optimize karo.

## Q11. Common greedy questions — list
**Answer:** Ye practice karo:

- Activity Selection / Meeting Rooms
- Fractional Knapsack
- Jump Game I & II
- Minimum Platforms
- Assign Cookies
- Gas Station
- Merge Intervals
- Non-overlapping Intervals
- Boats to Save People
- Lemonade Change

---

> 💡 **Interview tip:** greedy question me **kabhi seedha code mat likho.** Pehle bolo — *"main ye approach try karunga: [sort karo + yeh choice lo]. Ye optimal hai kyunki [reason]. Let me verify with a counter-example..."* — counter-example dhoondhna dikhata hai tum blindly code nahi likhte. Agar greedy galat nikla to khud bolo *"yahan DP lagega"* — self-correction sabse badi skill hai.
