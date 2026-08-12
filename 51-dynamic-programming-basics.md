# 51 — Dynamic Programming Basics 🧩

DP se sabse zyada log darte hain. Par asli baat sirf ek hai — **same subproblem baar-baar solve mat karo, answer store kar lo.** Bas itna hi hai.

## Q1. DP hai kya?
**Answer:** Do conditions poori hon to DP lagta hai:

1. **Overlapping subproblems** — wahi chhote problems baar-baar solve ho rahe hain
2. **Optimal substructure** — chhote answers se bada answer ban sakta hai

**Fibonacci ka example** — dekho kitna waste ho raha hai:

```
              fib(5)
           /         \
       fib(4)        fib(3)
      /     \        /    \
  fib(3)  fib(2)  fib(2)  fib(1)
   /  \
fib(2) fib(1)
```

`fib(3)` **do baar**, `fib(2)` **teen baar** calculate hua. `n` bada hote hi ye exponential ho jaata hai.

## Q2. Memoization (Top-Down) kya hai?
**Answer:** Recursion + answers ko cache karna.

```java
static long[] memo;

static long fib(int n) {
    if (n <= 1) return n;

    if (memo[n] != 0) return memo[n];      // pehle se calculated?

    memo[n] = fib(n - 1) + fib(n - 2);     // store karo
    return memo[n];
}

// Call
memo = new long[n + 1];
fib(n);
```

**Time:** `O(2ⁿ)` → **`O(n)`** | **Space:** `O(n)` + recursion stack

👉 Sirf **do line** add ki (cache check + cache store) aur exponential se linear ho gaya. Yahi DP ka poora essence hai.

## Q3. Tabulation (Bottom-Up) kya hai?
**Answer:** Recursion hataao, chhote se bade ki taraf table bharo.

```java
static long fib(int n) {
    if (n <= 1) return n;

    long[] dp = new long[n + 1];
    dp[0] = 0;
    dp[1] = 1;

    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}
```

**Space optimized** — sirf pichhle do chahiye:
```java
static long fib(int n) {
    if (n <= 1) return n;
    long prev2 = 0, prev1 = 1;

    for (int i = 2; i <= n; i++) {
        long curr = prev1 + prev2;
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```
**Space:** `O(n)` → **`O(1)`** ✅

## Q4. Memoization vs Tabulation — kaunsa better?
**Answer:**

| | Memoization (Top-Down) | Tabulation (Bottom-Up) |
|---|---|---|
| Sochna | Aasan — recursion jaisa ✅ | Thoda mushkil |
| Speed | Thoda dheema (function calls) | **Tez** ✅ |
| Stack overflow | Ho sakta hai ❌ | Nahi hota ✅ |
| Sirf zaroori subproblems | Haan ✅ | Nahi, saare bharta hai |
| Space optimize karna | Mushkil | **Aasan** ✅ |

👉 **Interview strategy:** pehle **recursion** likho, phir **memo** lagao, phir bolo *"ise tabulation me convert kar sakta hoon aur space `O(1)` kar sakta hoon"*. Ye progression dikhana bahut accha impression deta hai.

## Q5. Climbing Stairs
**Answer:** `n` seedhi, ek baar me 1 ya 2 step. Kitne tareeke?

```java
static int climbStairs(int n) {
    if (n <= 2) return n;
    int prev2 = 1, prev1 = 2;

    for (int i = 3; i <= n; i++) {
        int curr = prev1 + prev2;
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

👉 **Ye Fibonacci hi hai!** `nth` step pe pahunchne ke tareeke = `(n-1)` se aane ke + `(n-2)` se aane ke. Interview me ye pehchan-na hi asli skill hai.

## Q6. House Robber
**Answer:** Bagal wale ghar nahi loot sakte. Max kitna milega?

```java
static int rob(int[] houses) {
    int prev2 = 0, prev1 = 0;

    for (int money : houses) {
        int curr = Math.max(prev1,           // ye ghar chhod do
                            prev2 + money);  // ye ghar loot lo
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

**DP ka core question:** har step pe **do choice** — lo ya chhodo. Dono ka max le lo.

## Q7. Coin Change — minimum coins
**Answer:** Classic DP question.

```java
static int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);        // "infinity" ki jagah
    dp[0] = 0;                          // 0 banane ke liye 0 coins

    for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
            if (coin <= i) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    return dp[amount] > amount ? -1 : dp[amount];
}
```

⚠️ **Greedy yahan kaam nahi karta!** `coins = [1, 3, 4]`, `amount = 6`:
- Greedy: `4 + 1 + 1` = **3 coins**
- Optimal: `3 + 3` = **2 coins** ✅

Ye example yaad rakho — interviewer greedy try karwata hai aur phir yahi counter-example poochta hai.

## Q8. 0/1 Knapsack
**Answer:** DP ka "hello world". 2D table chahiye.

```java
static int knapsack(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[][] dp = new int[n + 1][capacity + 1];

    for (int i = 1; i <= n; i++) {
        for (int w = 0; w <= capacity; w++) {

            if (weights[i - 1] <= w) {
                dp[i][w] = Math.max(
                    dp[i - 1][w],                                      // na lo
                    values[i - 1] + dp[i - 1][w - weights[i - 1]]      // lo
                );
            } else {
                dp[i][w] = dp[i - 1][w];       // fit hi nahi hota
            }
        }
    }
    return dp[n][capacity];
}
```

**Time:** `O(n × capacity)` | **Space:** `O(n × capacity)`

👉 `dp[i][w]` ka matlab: *"pehle `i` items me se, `w` capacity me, max value kitni?"* — **DP me state ka matlab clearly define karna** sabse zaroori step hai.

## Q9. Longest Common Subsequence (LCS)
**Answer:** String DP ka base question.

```java
static int lcs(String a, String b) {
    int[][] dp = new int[a.length() + 1][b.length() + 1];

    for (int i = 1; i <= a.length(); i++) {
        for (int j = 1; j <= b.length(); j++) {

            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1] + 1;          // match → diagonal + 1
            } else {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
    }
    return dp[a.length()][b.length()];
}
```

👉 **Subsequence ≠ substring.** Subsequence me characters continuous hone zaroori nahi (par order same rehna chahiye). `"ace"` `"abcde"` ka subsequence hai, substring nahi.

## Q10. DP problem kaise solve karein — 5 step method
**Answer:** Ye process follow karo, DP darawni nahi lagegi:

1. **State define karo** — `dp[i]` ka matlab kya hai? (sabse important step)
2. **Recurrence relation** — `dp[i]` chhote answers se kaise banega?
3. **Base case** — sabse chhota answer kya hai?
4. **Order** — table kis direction me bharna hai?
5. **Answer kahan hai** — `dp[n]`? `dp[n][m]`? ya max of all?

## Q11. DP problem pehchano kaise?
**Answer:** Ye signals dikhein to DP socho:

| Signal | Matlab |
|---|---|
| "maximum / minimum ... nikalo" | Optimization → DP |
| "kitne tareeke hain" | Counting → DP |
| "possible hai ya nahi" | Decision → DP |
| Har step pe **choice** ho | Take / not-take → DP |
| Recursion likha aur **TLE** aaya | Memo lagao |
| "longest / shortest ... subsequence" | DP |

⚠️ **DP nahi hai jab:** har step ka best choice locally decide ho sakta hai — wo **greedy** hai. Ya subproblems overlap hi nahi karte — wo simple **divide & conquer** hai (jaise merge sort).

## Q12. Recursion → DP conversion ka shortcut
**Answer:** Kisi bhi recursive solution ko DP banane ka mechanical tareeka:

```java
// Step 1: Plain recursion likho
int solve(int i) {
    if (base) return baseValue;
    return f(solve(i - 1), solve(i - 2));
}

// Step 2: memo array banao, 2 line add karo
int solve(int i) {
    if (base) return baseValue;
    if (memo[i] != -1) return memo[i];              // ← line 1
    return memo[i] = f(solve(i-1), solve(i-2));     // ← line 2
}
```

⚠️ `memo` ko `-1` se fill karo (`Arrays.fill(memo, -1)`), `0` se nahi — kyunki `0` valid answer ho sakta hai.

---

> 💡 **Interview tip:** DP question me **turant table mat banane lago.** Pehle bolo *"main brute force recursion se start karta hoon"*, wo likho, phir bolo *"yahan `fib(3)` do baar calculate ho raha hai — overlapping subproblems hain, memo laga deta hoon"*. Ye soch ka safar dikhana **seedha optimal code likhne se zyada** marks deta hai.
