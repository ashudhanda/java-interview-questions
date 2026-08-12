# 49 — Matrix & 2D Array Problems 🧮

2D array questions me **index ki galti** sabse zyada hoti hai. `matrix[row][col]` — pehle row, phir column. Ye ek line dimaag me fix kar lo.

## Setup — basics

```java
int[][] matrix = new int[3][4];        // 3 rows, 4 columns

int rows = matrix.length;              // 3
int cols = matrix[0].length;           // 4

// Traverse
for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
        System.out.print(matrix[i][j] + " ");
    }
    System.out.println();
}
```

⚠️ `matrix[0].length` tab crash karega jab matrix **khaali** ho. Safe check:
```java
if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return;
```

## Q1. Matrix transpose karo
**Answer:** Rows ko columns bana do.

```java
static void transpose(int[][] m) {
    int n = m.length;

    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {      // j = i+1 — sirf upper half!
            int temp = m[i][j];
            m[i][j] = m[j][i];
            m[j][i] = temp;
        }
    }
}
```

⚠️ **`j = i + 1` se shuru karo, `j = 0` se nahi!** Warna har element **do baar** swap hoga aur matrix wapas wahi ban jaayegi. Ye sabse common bug hai is question me.

👉 In-place transpose sirf **square matrix** me hota hai. Rectangular ke liye nayi matrix banani padegi.

## Q2. Matrix 90° clockwise rotate karo
**Answer:** Do steps — ye trick yaad rakho.

```java
static void rotate90(int[][] m) {
    transpose(m);                      // Step 1: transpose

    // Step 2: har row ko reverse karo
    for (int[] row : m) {
        int left = 0, right = row.length - 1;
        while (left < right) {
            int temp = row[left];
            row[left++] = row[right];
            row[right--] = temp;
        }
    }
}
```

**Formula yaad rakho:**
- **90° clockwise** = transpose + har row reverse
- **90° anti-clockwise** = transpose + har **column** reverse
- **180°** = har row reverse + har column reverse

**Space:** `O(1)` — in-place. Nayi matrix banana `O(n²)` space lega, interviewer in-place maangega.

## Q3. Spiral order me print karo
**Answer:** Char boundaries maintain karo — aur unhe shrink karte jao.

```java
static List<Integer> spiralOrder(int[][] m) {
    List<Integer> result = new ArrayList<>();
    if (m.length == 0) return result;

    int top = 0, bottom = m.length - 1;
    int left = 0, right = m[0].length - 1;

    while (top <= bottom && left <= right) {
        for (int j = left; j <= right; j++) result.add(m[top][j]);    // →
        top++;

        for (int i = top; i <= bottom; i++) result.add(m[i][right]);  // ↓
        right--;

        if (top <= bottom) {                       // check zaroori!
            for (int j = right; j >= left; j--) result.add(m[bottom][j]);  // ←
            bottom--;
        }
        if (left <= right) {                       // check zaroori!
            for (int i = bottom; i >= top; i--) result.add(m[i][left]);    // ↑
            left++;
        }
    }
    return result;
}
```

⚠️ **Beech ke do `if` checks** — single row ya single column bachne pe elements **do baar** add ho jaate hain. Ye edge case interviewer specially test karta hai.

## Q4. Sorted matrix me element search karo
**Answer:** Har row sorted, har column sorted — **staircase search** `O(m + n)`.

```java
static boolean searchMatrix(int[][] m, int target) {
    int row = 0, col = m[0].length - 1;        // top-right corner se shuru!

    while (row < m.length && col >= 0) {
        if (m[row][col] == target) return true;
        else if (m[row][col] > target) col--;   // bada hai → left jao
        else row++;                             // chhota hai → neeche jao
    }
    return false;
}
```

👉 **Top-right corner hi kyun?** Kyunki wahan se **left** jaane pe value **ghatti** hai aur **neeche** jaane pe **badhti** hai — har step pe ek poori row ya column eliminate ho jaati hai. Bottom-left se bhi ho sakta hai. Top-left se **nahi** hoga — wahan dono direction me value badhti hai, decision nahi le paoge.

## Q5. Matrix me zeroes set karo
**Answer:** Jis cell me 0 hai, uski poori row aur column 0 kar do.

```java
static void setZeroes(int[][] m) {
    Set<Integer> zeroRows = new HashSet<>();
    Set<Integer> zeroCols = new HashSet<>();

    for (int i = 0; i < m.length; i++)
        for (int j = 0; j < m[0].length; j++)
            if (m[i][j] == 0) { zeroRows.add(i); zeroCols.add(j); }

    for (int i = 0; i < m.length; i++)
        for (int j = 0; j < m[0].length; j++)
            if (zeroRows.contains(i) || zeroCols.contains(j)) m[i][j] = 0;
}
```

⚠️ **Ek hi pass me mat karo!** Zero set karte hi wo naya zero aage ke iterations ko trigger kar dega → poori matrix zero. Do pass zaroori hai.

👉 **Follow-up:** `O(1)` space me? Pehli row aur pehle column ko hi marker ki tarah use karo.

## Q6. Number of Islands (Grid DFS)
**Answer:** Grid pe DFS — ye pattern bahut common hai.

```java
static int numIslands(char[][] grid) {
    int count = 0;

    for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[0].length; j++) {
            if (grid[i][j] == '1') {
                count++;
                sink(grid, i, j);       // poora island doobo do
            }
        }
    }
    return count;
}

static void sink(char[][] grid, int i, int j) {
    if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length
            || grid[i][j] != '1') return;         // boundary + visited check

    grid[i][j] = '0';                              // visited mark

    sink(grid, i + 1, j);
    sink(grid, i - 1, j);
    sink(grid, i, j + 1);
    sink(grid, i, j - 1);
}
```

👉 **Grid ko hi visited array ki tarah use kiya** — extra space bacha. Agar grid modify nahi kar sakte to `boolean[][] visited` banana padega.

## Q7. Direction array trick
**Answer:** Char directions baar-baar likhne se accha:

```java
int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};    // up, down, left, right

for (int[] d : dirs) {
    int newRow = row + d[0];
    int newCol = col + d[1];

    if (isValid(newRow, newCol)) {
        // process
    }
}
```

8 directions chahiye (diagonals bhi):
```java
int[][] dirs8 = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
```

👉 Ye trick code ko chhota aur bug-free banati hai. Interviewer ko clean lagta hai.

## Q8. Jagged array kya hota hai?
**Answer:** Har row ki alag length — Java me allowed hai.

```java
int[][] jagged = new int[3][];      // columns specify nahi kiye
jagged[0] = new int[2];
jagged[1] = new int[5];
jagged[2] = new int[3];
```

⚠️ Isliye `matrix[0].length` sabhi rows ke liye **assume mat karo**. Loop me `matrix[i].length` use karo:
```java
for (int i = 0; i < matrix.length; i++)
    for (int j = 0; j < matrix[i].length; j++)      // matrix[i], matrix[0] nahi
```

## Q9. 2D array memory me kaise store hota hai?
**Answer:** Java me **true 2D array hota hi nahi** — ye **"array of arrays"** hai.

```java
int[][] m = new int[3][4];
```

Heap me: ek array of 3 **references**, aur har reference ek alag `int[4]` array ko point karta hai.

👉 **Isliye:** rows memory me continuous **nahi** hoti. C/C++ me hoti hai. Isi wajah se Java me row-wise traverse column-wise se tez hota hai (cache locality).

## Q10. 2D array print kaise karein?
**Answer:**

```java
System.out.println(matrix);                   // [[I@1b6d3586 ❌
System.out.println(Arrays.toString(matrix));  // [[I@1b6d..., [I@4554... ❌
System.out.println(Arrays.deepToString(matrix)); // [[1, 2], [3, 4]] ✅
```

👉 **`Arrays.deepToString()`** — nested arrays ke liye yahi chahiye. Debugging me bahut kaam aata hai. Isi tarah `Arrays.deepEquals()` comparison ke liye.

---

> 💡 **Interview tip:** matrix question me sabse pehle **boundary conditions** bol do — "main pehle null aur empty check karunga, phir `rows` aur `cols` variables me nikaal lunga". Aur rotate wale question me **transpose + reverse** wala do-step formula seedha bol dena — log yahan 4-pointer wala complicated code likh ke phas jaate hain.
