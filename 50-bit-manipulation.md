# 50 — Bit Manipulation 🔢

Bit manipulation dikhne me daraavni lagti hai, par sirf **5-6 tricks** yaad karne se 90% questions ho jaate hain. Aur XOR wale questions to interview me favourite hain.

## Q1. Bitwise operators ka basic
**Answer:**

| Operator | Naam | Kaam |
|---|---|---|
| `&` | AND | dono 1 → 1 |
| `\|` | OR | koi ek 1 → 1 |
| `^` | XOR | **alag** hon → 1 |
| `~` | NOT | ulta kar do |
| `<<` | Left shift | `× 2` per shift |
| `>>` | Right shift | `÷ 2` per shift (sign rakhta hai) |
| `>>>` | Unsigned right shift | left me hamesha 0 bharta hai |

```java
5  & 3    // 101 & 011 = 001 → 1
5  | 3    // 101 | 011 = 111 → 7
5  ^ 3    // 101 ^ 011 = 110 → 6
5 << 1    // 1010 → 10  (5 × 2)
5 >> 1    // 10   → 2   (5 ÷ 2)
```

## Q2. `>>` aur `>>>` me kya farak hai?
**Answer:** Negative numbers pe farak dikhta hai.

```java
-8 >> 1    // -4   — sign bit copy hota hai
-8 >>> 1   // 2147483644 — left me 0 aata hai, number positive ho gaya!
```

⚠️ Java me **`<<<` hota hi nahi** — left shift me sign ka issue hi nahi aata. Ye trick question hai.

👉 `>>>` ka asli use: `mid = (low + high) >>> 1` — overflow-safe binary search (topic 44). Java ki apni library yahi use karti hai.

## Q3. Number even hai ya odd — bit se?
**Answer:**

```java
if ((n & 1) == 0) even();
else odd();
```

**Logic:** last bit `1` ho to number odd hai. `n % 2` se tez hai (modulo division karta hai, `&` nahi).

⚠️ Brackets zaroori hain! `n & 1 == 0` galat hai — Java me `==` ki precedence `&` se **zyada** hai, to ye `n & (1 == 0)` ban jaayega → compile error.

## Q4. Kth bit check / set / clear / toggle karo
**Answer:** Ye chaar operations base hain — yaad kar lo:

```java
// Check — kth bit 1 hai?
boolean isSet = (n & (1 << k)) != 0;

// Set — kth bit 1 kar do
n = n | (1 << k);

// Clear — kth bit 0 kar do
n = n & ~(1 << k);

// Toggle — ulta kar do
n = n ^ (1 << k);
```

👉 `1 << k` ek **mask** banata hai jisme sirf kth position pe 1 hai. Baaki sab operations usi mask se hote hain.

## Q5. Single Number — XOR ka jaadu
**Answer:** Array me har element **do baar** hai, sirf ek **ek baar**. Usse dhoondho.

```java
static int singleNumber(int[] arr) {
    int result = 0;
    for (int num : arr) result ^= num;
    return result;
}
```

**Time:** `O(n)` | **Space:** `O(1)` — HashMap ki zaroorat hi nahi!

**XOR ke teen properties** (yahi poore jaadu ki jad hain):
```java
a ^ a = 0        // khud se XOR = 0
a ^ 0 = a        // 0 se XOR = wahi
a ^ b = b ^ a    // order matter nahi karta
```

Isliye saare duplicates aapas me cancel ho jaate hain, sirf akela bachta hai.

## Q6. Do numbers swap karo — bina temp variable
**Answer:**

```java
a = a ^ b;
b = a ^ b;      // (a^b)^b = a
a = a ^ b;      // (a^b)^a = b
```

⚠️ **Interview me ye bolna:** ye trick dikhne me smart hai par **real code me use mat karo** — agar `a` aur `b` **same variable** hon to dono `0` ho jaate hain! Aur modern compilers temp variable wala code waise bhi optimize kar dete hain. Ye caveat batana zyada impress karta hai trick se.

## Q7. Set bits count karo (Hamming weight)
**Answer:** Kitne `1` hain — do tareeke:

```java
// Tareeka 1: har bit check karo — O(32)
static int countBits(int n) {
    int count = 0;
    while (n != 0) {
        count += (n & 1);
        n >>>= 1;              // >>> use karo, negative ke liye zaroori
    }
    return count;
}

// Tareeka 2: Brian Kernighan — O(set bits) ✅ tez
static int countBitsFast(int n) {
    int count = 0;
    while (n != 0) {
        n = n & (n - 1);       // sabse right wala 1 hata do
        count++;
    }
    return count;
}
```

👉 **`n & (n - 1)` kya karta hai?** Sabse right wala set bit **hata** deta hai. `12 (1100)` → `8 (1000)` → `0`. Sirf 2 iterations!

**Built-in:** `Integer.bitCount(n)` — real code me yahi use karo.

## Q8. Power of 2 check karo
**Answer:** One-liner:

```java
static boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
}
```

**Logic:** power of 2 me **sirf ek** bit set hoti hai. `n & (n-1)` wo hata deta hai → `0` bachta hai.

```
8  = 1000
7  = 0111
8 & 7 = 0000  ✅ power of 2

12 = 1100
11 = 1011
12 & 11 = 1000  ❌ power of 2 nahi
```

⚠️ **`n > 0` check zaroori hai** — warna `0` aur negative numbers galat `true` de denge.

## Q9. Missing number dhoondho
**Answer:** `0` se `n` tak me ek number missing hai.

```java
static int missingNumber(int[] arr) {
    int result = arr.length;              // n se shuru

    for (int i = 0; i < arr.length; i++) {
        result ^= i ^ arr[i];             // index aur value dono XOR
    }
    return result;
}
```

**Logic:** saare indexes aur saari values XOR karo — jo present hain wo cancel ho jaate hain, missing bachta hai.

👉 **Sum formula se bhi hota hai:** `n*(n+1)/2 - actualSum`. Par bade arrays me wo **overflow** kar sakta hai — XOR nahi karta. Ye baat bolna accha point hai.

## Q10. Sabse right wala set bit nikalo
**Answer:**

```java
int rightmostSetBit = n & (-n);
```

`12 (1100)` → `4 (0100)`

**Logic:** `-n` two's complement hota hai (`~n + 1`). AND karne pe sirf rightmost set bit bachti hai.

👉 Ye Fenwick Tree / Binary Indexed Tree ka base hai.

## Q11. Java me integers kaise store hote hain?
**Answer:** **Two's complement** me. Ye samajhna zaroori hai:

```java
 int  5 = 00000000 00000000 00000000 00000101
 int -5 = 11111111 11111111 11111111 11111011
```

`-5` nikalne ka tareeka: `5` ke saare bits ulto (`~`), phir `1` jodo.

**Isliye:**
- `Integer.MIN_VALUE = -2147483648`, `MAX_VALUE = 2147483647` (ek negative extra)
- `Math.abs(Integer.MIN_VALUE)` **negative** return karta hai! Overflow ho jaata hai — ye classic trap question hai.
- Leftmost bit **sign bit** hai

## Q12. Kaam ke built-in methods
**Answer:** Ye pata hone chahiye — manually likhne ki zaroorat nahi:

```java
Integer.bitCount(n)              // kitne 1 hain
Integer.toBinaryString(n)        // binary string me
Integer.highestOneBit(n)         // sabse left wali set bit
Integer.lowestOneBit(n)          // sabse right wali set bit
Integer.reverse(n)               // bits ulte kar do
Integer.numberOfLeadingZeros(n)
Integer.numberOfTrailingZeros(n)
```

## Q13. Bit tricks ka cheat sheet
**Answer:**

| Kaam | Trick |
|---|---|
| Even check | `(n & 1) == 0` |
| 2 se multiply | `n << 1` |
| 2 se divide | `n >> 1` |
| Rightmost set bit hatao | `n & (n - 1)` |
| Rightmost set bit nikalo | `n & (-n)` |
| Power of 2 check | `n > 0 && (n & (n-1)) == 0` |
| Kth bit check | `(n >> k) & 1` |
| Duplicates cancel | XOR sab |
| Sign flip | `~n + 1` |

---

> 💡 **Interview reality check:** bit manipulation real production code me kam use hoti hai — readability marr jaati hai. Interview me ye **problem-solving depth** test karne ke liye poochi jaati hai. **XOR wale questions** (single number, missing number, swap) sabse zyada aate hain — kam se kam wo teen properties (`a^a=0`, `a^0=a`, commutative) zaroor yaad rakho.
