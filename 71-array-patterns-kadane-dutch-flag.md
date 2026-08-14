# 71 — Array Patterns: Kadane's, Dutch Flag & More 📊

Array questions ka ek alag set — wo famous algorithms jinke **naam** se poochte hain. Kadane's, Dutch National Flag, Moore's Voting — naam yaad rakho, interview me seedha bola jaata hai.

## Q1. Kadane's Algorithm — Maximum Subarray Sum
**Answer:** Sabse famous. Maximum sum wala **continuous** subarray dhoondo.

```java
static int maxSubArray(int[] nums) {
    int maxSoFar = nums[0];
    int currentMax = nums[0];

    for (int i = 1; i < nums.length; i++) {
        // Ya to purana subarray continue karo, ya yahan se naya shuru
        currentMax = Math.max(nums[i], currentMax + nums[i]);
        maxSoFar = Math.max(maxSoFar, currentMax);
    }
    return maxSoFar;
}
```

`[-2, 1, -3, 4, -1, 2, 1, -5, 4]` → `6` (subarray `[4, -1, 2, 1]`)

**Logic:** har position pe decision — *"pichhla sum saath laau ya naya shuru karu?"* Agar pichhla sum **negative** hai to usse saath laane ka koi fayda nahi.

**Time:** `O(n)` | **Space:** `O(1)`

⚠️ **Sab negative ho to?** Initialize `nums[0]` se karo, `0` se nahi — warna `[-3, -1]` ka answer `0` aa jaayega (galat, sahi `-1` hai).

## Q2. Dutch National Flag — Sort Colors
**Answer:** Sirf 0, 1, 2 sort karo — **ek hi pass me**, bina counting ke.

```java
static void sortColors(int[] nums) {
    int low = 0, mid = 0, high = nums.length - 1;

    while (mid <= high) {
        if (nums[mid] == 0) {
            swap(nums, low++, mid++);      // 0 → aage bhejo
        } else if (nums[mid] == 1) {
            mid++;                          // 1 → wahi rehne do
        } else {
            swap(nums, mid, high--);        // 2 → peeche bhejo
            // mid++ NAHI — swapped value check karni hai!
        }
    }
}
```

⚠️ **`2` wale case me `mid++` nahi karna** — kyunki `high` se jo aaya wo check nahi hua abhi. Ye sabse common bug hai.

**Time:** `O(n)` | **Space:** `O(1)`

👉 **3-pointer pattern:** `low` = 0s ka boundary, `high` = 2s ka boundary, `mid` = current.

## Q3. Moore's Voting — Majority Element
**Answer:** Element jo **n/2 se zyada** baar aata hai — `O(1)` space me dhoondo.

```java
static int majorityElement(int[] nums) {
    int candidate = nums[0], count = 1;

    for (int i = 1; i < nums.length; i++) {
        if (count == 0) {
            candidate = nums[i];          // naya candidate
            count = 1;
        } else if (nums[i] == candidate) {
            count++;
        } else {
            count--;                      // cancel out
        }
    }
    return candidate;
}
```

**Logic:** Majority element aur baaki elements **ek-ek cancel** karo. Majority hamesha bachega kyunki wo aadhe se zyada hai.

`[3, 2, 3, 4, 3, 3, 1]` → `3`

⚠️ **Guarantee chahiye** ki majority exist karta hai. Nahi hai to ek aur pass me verify karo.

**Time:** `O(n)` | **Space:** `O(1)` — HashMap se bhi hota par space `O(n)` lagta.

## Q4. Best Time to Buy & Sell Stock
**Answer:** Ek baar kharido, ek baar becho — max profit.

```java
static int maxProfit(int[] prices) {
    int minPrice = Integer.MAX_VALUE;
    int maxProfit = 0;

    for (int price : prices) {
        minPrice = Math.min(minPrice, price);           // ab tak ka sasta din
        maxProfit = Math.max(maxProfit, price - minPrice);  // aaj bechne pe profit
    }
    return maxProfit;
}
```

👉 **Ek pass:** har din pe socho — *"agar aaj bechun to profit kitna?"* Aur minimum price track rakho.

**Time:** `O(n)` | **Space:** `O(1)`

## Q5. Product of Array Except Self
**Answer:** Division **bina use kiye** — `O(n)` me.

```java
static int[] productExceptSelf(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];

    // Pass 1: left products
    int left = 1;
    for (int i = 0; i < n; i++) {
        result[i] = left;
        left *= nums[i];
    }

    // Pass 2: right products multiply karo
    int right = 1;
    for (int i = n - 1; i >= 0; i--) {
        result[i] *= right;
        right *= nums[i];
    }
    return result;
}
```

`[1, 2, 3, 4]` → `[24, 12, 8, 6]`

👉 **Trick:** har index pe answer = (left side ka product) × (right side ka product). Do passes, koi division nahi.

⚠️ **Division wala tareeka** zero aane pe toot jaata hai — isliye yahi expected solution hai.

## Q6. Move Zeroes
**Answer:** Saare 0s end me — order maintain karte hue, in-place.

```java
static void moveZeroes(int[] nums) {
    int write = 0;

    for (int read = 0; read < nums.length; read++) {
        if (nums[read] != 0) {
            nums[write++] = nums[read];    // non-zero ko aage likho
        }
    }
    while (write < nums.length) {
        nums[write++] = 0;                 // baaki zero bharo
    }
}
```

👉 **Two-pointer (read/write)** — ye pattern "remove element", "remove duplicates" sab me lagta hai.

## Q7. Container With Most Water
**Answer:** Do lines choose karo — max paani.

```java
static int maxArea(int[] height) {
    int left = 0, right = height.length - 1, maxArea = 0;

    while (left < right) {
        int area = Math.min(height[left], height[right]) * (right - left);
        maxArea = Math.max(maxArea, area);

        if (height[left] < height[right]) left++;   // chhoti wali hatao
        else right--;
    }
    return maxArea;
}
```

👉 **Kyun chhoti wali hataate hain?** Kyunki area **chhoti line** se limited hai. Chhoti ko badhaane ka chance sirf chhoti hatane se hai. Badi hatane se area sirf ghat-ega.

**Time:** `O(n)` — two pointers, topic 42 ka extension.

## Q8. Trapping Rain Water
**Answer:** Hard question — par pattern fixed hai.

```java
static int trap(int[] height) {
    int left = 0, right = height.length - 1;
    int leftMax = 0, rightMax = 0, water = 0;

    while (left < right) {
        if (height[left] < height[right]) {
            if (height[left] >= leftMax) leftMax = height[left];
            else water += leftMax - height[left];
            left++;
        } else {
            if (height[right] >= rightMax) rightMax = height[right];
            else water += rightMax - height[right];
            right--;
        }
    }
    return water;
}
```

👉 **Logic:** har bar pe paani = `min(leftMax, rightMax) - height`. Do pointers se ek pass me.

**Time:** `O(n)` | **Space:** `O(1)` — DP se bhi hota hai par space `O(n)`.

## Q9. Find Duplicate (1 to N, ek repeat)
**Answer:** Floyd's cycle detection — array ko linked list ki tarah use karo!

```java
static int findDuplicate(int[] nums) {
    int slow = nums[0], fast = nums[0];

    do {
        slow = nums[slow];
        fast = nums[nums[fast]];
    } while (slow != fast);

    slow = nums[0];
    while (slow != fast) {
        slow = nums[slow];
        fast = nums[fast];
    }
    return slow;
}
```

👉 **Genius trick:** values ko next index ki tarah treat karo. Duplicate value = do arrows same node pe = **cycle**. Cycle ka entry point = duplicate.

**Time:** `O(n)` | **Space:** `O(1)` — array modify nahi kiya!

## Q10. Next Permutation
**Answer:**

```java
static void nextPermutation(int[] nums) {
    int i = nums.length - 2;

    // 1. Pehla decreasing element peeche se dhoondo
    while (i >= 0 && nums[i] >= nums[i + 1]) i--;

    if (i >= 0) {
        // 2. Usse just bada element peeche se dhoondo, swap
        int j = nums.length - 1;
        while (nums[j] <= nums[i]) j--;
        swap(nums, i, j);
    }
    // 3. i ke baad wala hissa reverse karo
    reverse(nums, i + 1, nums.length - 1);
}
```

👉 3 steps yaad rakho: **find dip → swap with next bigger → reverse suffix**. Interview me derive karne se accha yaad rakho.

## Q11. Quick pattern reference
**Answer:**

| Question | Algorithm | Complexity |
|---|---|---|
| Max subarray sum | Kadane's | `O(n)` |
| Sort 0s,1s,2s | Dutch Flag | `O(n)` |
| Majority element | Moore's Voting | `O(n)`, `O(1)` space |
| Stock buy/sell | Min-track | `O(n)` |
| Product except self | Left×Right passes | `O(n)` |
| Find duplicate | Floyd cycle | `O(n)`, `O(1)` space |
| Most water | Two pointers | `O(n)` |
| Rain water | Two pointers + max-track | `O(n)` |

---

> 💡 **Interview tip:** ye algorithms **naam se** poochhe jaate hain — "Kadane's pata hai?" Bolo: *"Haan, maximum subarray sum O(n) me — har position pe decide karte hain ki purana sum continue karein ya naya start. Handle karta hai all-negative case bhi."* Naam + complexity + edge case — teeno bolne se poora command dikhta hai.
