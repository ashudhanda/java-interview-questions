# 58 — Backtracking 🔄

Backtracking recursion ka hi advanced roop hai — **choice lo, aage badho, fail hua to wapas aao aur doosri choice lo.** Sudoku solver se lekar permutation tak, sab yahi hai.

## Q1. Backtracking hota kya hai?
**Answer:** Teen steps ka pattern:

1. **Choose** — ek option chuno
2. **Explore** — us option se aage recursion
3. **Un-choose** — wapas aake option hatao (backtrack)

```java
void solve(State state) {
    if (isDone(state)) {                    // answer mil gaya
        save(state);
        return;
    }

    for (Choice c : getChoices(state)) {
        if (isValid(c)) {
            applyChoice(state, c);          // 1. choose
            solve(state);                   // 2. explore
            undoChoice(state, c);           // 3. un-choose ← YEH bhoolte hain sab!
        }
    }
}
```

👉 **"Un-choose" hi backtracking ka dil hai.** Iske bina purani choices aage ke solutions ko bigaad deti hain.

## Q2. Saare subsets nikalo (Power Set)
**Answer:** Sabse classic backtracking question.

```java
static List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(nums, 0, new ArrayList<>(), result);
    return result;
}

static void backtrack(int[] nums, int index, List<Integer> current, List<List<Integer>> result) {
    if (index == nums.length) {
        result.add(new ArrayList<>(current));    // COPY karo — zaroori!
        return;
    }

    // Choice 1: mat lo
    backtrack(nums, index + 1, current, result);

    // Choice 2: lo
    current.add(nums[index]);
    backtrack(nums, index + 1, current, result);
    current.remove(current.size() - 1);          // ← un-choose
}
```

`[1,2,3]` → `[[], [3], [2], [2,3], [1], [1,3], [1,2], [1,2,3]]`

⚠️ **`new ArrayList<>(current)`** — reference store kar diya to saare answers same list point karenge. Copy zaroori hai.

**Time:** `O(2ⁿ)` — har element ke do choices.

## Q3. Permutations nikalo
**Answer:** Order matter karta hai — `[1,2]` aur `[2,1]` alag hain.

```java
static List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(nums, new ArrayList<>(), new boolean[nums.length], result);
    return result;
}

static void backtrack(int[] nums, List<Integer> current, boolean[] used, List<List<Integer>> result) {
    if (current.size() == nums.length) {
        result.add(new ArrayList<>(current));
        return;
    }

    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;              // already le liya

        used[i] = true;
        current.add(nums[i]);

        backtrack(nums, current, used, result);

        used[i] = false;                    // ← un-choose
        current.remove(current.size() - 1);
    }
}
```

👉 **`used[]` array** hi permutation aur subset ka farak hai. Subset me `index` aage badhta hai, permutation me har baar poora array check karte hain par used wale skip.

**Time:** `O(n! × n)` — `n!` permutations, har ek ko copy karne me `n`.

## Q4. Duplicates hone pe kya karein?
**Answer:** Pehle **sort** karo, phir **skip** karo:

```java
static List<List<Integer>> subsetsWithDup(int[] nums) {
    Arrays.sort(nums);                       // ← zaroori
    List<List<Integer>> result = new ArrayList<>();
    backtrack(nums, 0, new ArrayList<>(), result);
    return result;
}

static void backtrack(int[] nums, int index, List<Integer> current, List<List<Integer>> result) {
    result.add(new ArrayList<>(current));    // har node pe add (saare subsets)

    for (int i = index; i < nums.length; i++) {
        if (i > index && nums[i] == nums[i - 1]) continue;   // ← duplicate skip

        current.add(nums[i]);
        backtrack(nums, i + 1, current, result);
        current.remove(current.size() - 1);
    }
}
```

⚠️ **Condition ka farak:** `i > index` — same **level** pe duplicate skip karna hai, alag depth pe nahi. Ye line samajhna mushkil hai, dry run karo.

## Q5. Combination Sum
**Answer:** Target sum banane wale combinations — **same number baar-baar** use kar sakte ho.

```java
static List<List<Integer>> combinationSum(int[] candidates, int target) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(candidates, target, 0, new ArrayList<>(), result);
    return result;
}

static void backtrack(int[] candidates, int remaining, int start, 
                      List<Integer> current, List<List<Integer>> result) {
    if (remaining == 0) {
        result.add(new ArrayList<>(current));    // target poora!
        return;
    }

    for (int i = start; i < candidates.length; i++) {
        if (candidates[i] > remaining) break;    // pruning — aage sab bade hain

        current.add(candidates[i]);
        backtrack(candidates, remaining - candidates[i], i, current, result);  // i, i+1 nahi!
        current.remove(current.size() - 1);
    }
}
```

👉 **`i` pass kiya, `i + 1` nahi** — isliye same element dobara chun sakte hain. Combinations me `i + 1` karte hain (ek hi baar).

👉 **`if (candidates[i] > remaining) break;`** = **pruning** — sorted array me aage ke sab bekaar hain, turant band kar do. Ye optimization interview me batana zaroori hai.

## Q6. Palindrome Partitioning
**Answer:** String ko palindromes me todne ke saare tareeke.

```java
static List<List<String>> partition(String s) {
    List<List<String>> result = new ArrayList<>();
    backtrack(s, 0, new ArrayList<>(), result);
    return result;
}

static void backtrack(String s, int start, List<String> current, List<List<String>> result) {
    if (start == s.length()) {
        result.add(new ArrayList<>(current));
        return;
    }

    for (int end = start + 1; end <= s.length(); end++) {
        String piece = s.substring(start, end);
        if (isPalindrome(piece)) {
            current.add(piece);
            backtrack(s, end, current, result);
            current.remove(current.size() - 1);
        }
    }
}
```

👉 Har `end` pe ek **cut** lagate hain — string ke beech me. `aab` → `[a,a,b], [aa,b], [a,ab✗ nahi hai]`.

## Q7. N-Queens
**Answer:** `N × N` board pe `N` queens — koi ek dusre ko attack nahi kare.

```java
static List<List<String>> solveNQueens(int n) {
    List<List<String>> result = new ArrayList<>();
    char[][] board = new char[n][n];
    for (char[] row : board) Arrays.fill(row, '.');
    backtrack(board, 0, result);
    return result;
}

static void backtrack(char[][] board, int row, List<List<String>> result) {
    if (row == board.length) {
        result.add(toList(board));
        return;
    }

    for (int col = 0; col < board.length; col++) {
        if (isSafe(board, row, col)) {
            board[row][col] = 'Q';           // choose
            backtrack(board, row + 1, result); // explore
            board[row][col] = '.';            // un-choose
        }
    }
}

static boolean isSafe(char[][] board, int row, int col) {
    // upar check
    for (int i = 0; i < row; i++)
        if (board[i][col] == 'Q') return false;
    // upper-left diagonal
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
        if (board[i][j] == 'Q') return false;
    // upper-right diagonal
    for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++)
        if (board[i][j] == 'Q') return false;

    return true;
}
```

👉 **Optimization:** sirf **upar** check karo — neeche koi queen hai hi nahi (row by row bhar rahe hain). Ye baat bolna interview me impress karta hai.

## Q8. Backtracking vs Recursion vs DP
**Answer:**

| | Recursion | Backtracking | DP |
|---|---|---|---|
| Kya hai | Function khud call | Recursion + undo | Recursion + memo |
| Kab | Sub-problem chhota ho | **Saare answers** chahiye | **Ek optimal** answer chahiye |
| Overlap | Zaroori nahi | Nahi hota | Hota hai |
| Example | Fibonacci | N-Queens, Sudoku | Knapsack |

👉 **Yaad rakhne ka tareeka:** "Ek answer" → DP. "Saare answers / ek valid arrangement" → Backtracking.

## Q9. Kab backtracking use karein — pehchan
**Answer:**

| Signal | Matlab |
|---|---|
| "saare permutations/combinations nikalo" | Backtracking |
| "kitne tareeke se ban sakta hai" + saare list karo | Backtracking |
| "kya ye possible hai" (constraint satisfaction) | Backtracking |
| Sudoku, crossword, maze solver | Backtracking |
| Constraints itne ki DP state define nahi ho pa rahi | Backtracking |

⚠️ **Backtracking exponential hota hai** — `O(2ⁿ)`, `O(n!)`. Constraints chhote hone chahiye (`n ≤ 15-20`). Bada input ho to DP ya greedy socho.

## Q10. Pruning kya hai?
**Answer:** Bekaar branches **pehle hi kaat do** — poora tree mat explore karo.

```java
// Pruning ke bina: har branch me jaao
backtrack(candidates, remaining - candidates[i], i, current, result);

// Pruning ke saath: pata hai fail hoga, mat jaao
if (candidates[i] > remaining) break;      // break = poora loop rok
if (candidates[i] > remaining) continue;   // continue = sirf ye skip
```

| | `break` | `continue` |
|---|---|---|
| Array sorted hai | ✅ use karo (aage sab bekaar) | galat hoga |
| Array unsorted | galat hoga | ✅ use karo |

👉 Interview me pruning nahi kiya to solution **galat nahi** hoga, par **slow** hoga. Interviewer TLE ki taraf ishara karega — tab pruning add karo.

## Q11. Common template — har question me same
**Answer:**

```java
static void backtrack(// inputs,
                     int start,              // ya index / row / position
                     List<T> current,        // abhi tak ki choices
                     List<List<T>> result) { // saare answers

    if (/* base case — answer ban gaya */) {
        result.add(new ArrayList<>(current));   // ← COPY!
        return;
    }

    for (/* saari choices */) {
        if (/* pruning condition */) continue;  // ya break

        current.add(choice);                    // choose
        backtrack(...);                         // explore
        current.remove(current.size() - 1);     // un-choose
    }
}
```

Ye template 90% backtracking questions me fit ho jaata hai — sirf base case, choices, aur pruning badalte hain.

---

> 💡 **Interview tip:** backtracking question me **pehle plain recursion likho** bina `remove` ke — interviewer dekhega tumhe bug samajh aa raha hai. Phir bolo *"ab mujhe state revert karni padegi"* aur `current.remove(last)` add karo. Aur **hamesha copy store karo** — `new ArrayList<>(current)`. Ye do galtiyan 90% log karte hain.
