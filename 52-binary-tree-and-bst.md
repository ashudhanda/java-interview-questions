# 52 — Binary Tree & BST 🌳

Tree questions **recursion** pe chalte hain. Ek baar ye samajh lo ki *"left subtree ka answer + right subtree ka answer = mera answer"*, to 80% questions khud ban jaate hain.

## Setup — TreeNode class

```java
class TreeNode {
    int data;
    TreeNode left, right;
    TreeNode(int data) { this.data = data; }
}
```

## Q1. Teen traversals likho
**Answer:** Farak sirf ye hai ki **root kab print hota hai**.

```java
// Inorder: Left → Root → Right
void inorder(TreeNode root) {
    if (root == null) return;
    inorder(root.left);
    System.out.print(root.data + " ");
    inorder(root.right);
}

// Preorder: Root → Left → Right
void preorder(TreeNode root) {
    if (root == null) return;
    System.out.print(root.data + " ");
    preorder(root.left);
    preorder(root.right);
}

// Postorder: Left → Right → Root
void postorder(TreeNode root) {
    if (root == null) return;
    postorder(root.left);
    postorder(root.right);
    System.out.print(root.data + " ");
}
```

**Kab kaunsa use hota hai:**

| Traversal | Use case |
|---|---|
| **Inorder** | BST me **sorted order** deta hai ⭐ |
| **Preorder** | Tree copy/serialize karne me |
| **Postorder** | Tree delete karne me, children pehle chahiye ho tab |

👉 **Naam ka logic:** "pre/in/post" batata hai ki **root** kahan aata hai — pehle, beech me, ya baad me. Left hamesha right se pehle hi hota hai.

## Q2. Level order traversal (BFS)
**Answer:** Yahan recursion nahi — **queue** chahiye.

```java
static List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
        int levelSize = queue.size();          // is level me kitne nodes
        List<Integer> level = new ArrayList<>();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            level.add(node.data);

            if (node.left != null)  queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        result.add(level);
    }
    return result;
}
```

⚠️ **`int levelSize = queue.size()` loop ke bahar** lena zaroori hai! Loop ke andar `queue.size()` badalta rehta hai — levels mix ho jaayenge. Ye sabse common bug hai.

## Q3. Tree ki height nikalo
**Answer:** Recursion ka sabse saaf example.

```java
static int height(TreeNode root) {
    if (root == null) return 0;

    return 1 + Math.max(height(root.left), height(root.right));
}
```

**Bas teen line.** Meri height = 1 + (mere bacchon me se jo sabse lamba hai).

⚠️ **Height vs Depth:**
- **Height** = node se **neeche** patte tak ki doori
- **Depth** = **root** se node tak ki doori

Root ki depth `0`, patton ki height `0`. Interviewer ye farak poochta hai.

## Q4. Tree balanced hai ya nahi?
**Answer:** Har node pe left aur right height ka farak `≤ 1` ho.

```java
// ❌ Naive — O(n²)
static boolean isBalancedSlow(TreeNode root) {
    if (root == null) return true;
    return Math.abs(height(root.left) - height(root.right)) <= 1
            && isBalancedSlow(root.left) && isBalancedSlow(root.right);
}

// ✅ Optimized — O(n), height aur balance ek saath
static boolean isBalanced(TreeNode root) {
    return check(root) != -1;
}

static int check(TreeNode root) {
    if (root == null) return 0;

    int lh = check(root.left);
    if (lh == -1) return -1;              // left imbalanced → turant nikal jao

    int rh = check(root.right);
    if (rh == -1) return -1;

    if (Math.abs(lh - rh) > 1) return -1; // -1 = "imbalanced" ka signal

    return 1 + Math.max(lh, rh);
}
```

👉 **Trick:** `-1` ko "imbalanced" ka flag bana diya — isse height aur balance dono **ek hi traversal** me nikal aaye. `O(n²)` → `O(n)`.

## Q5. BST kya hai?
**Answer:** Binary Search Tree — har node ke liye:
- **Left subtree** ke saare nodes **chhote**
- **Right subtree** ke saare nodes **bade**

```
        8
      /   \
     3     10
    / \      \
   1   6      14
```

**Isliye:** search, insert, delete sab **`O(log n)`** (balanced ho to).

⚠️ **Skewed BST me `O(n)` ho jaata hai:**
```
1 → 2 → 3 → 4 → 5      (sorted data insert kiya)
```
Ye linked list ban gaya! Isliye **AVL / Red-Black Tree** aate hain jo khud ko balance karte hain. Java ka `TreeMap` Red-Black Tree hi hai.

## Q6. Valid BST check karo
**Answer:** Ye **sabse zyada galat solve** kiya jaane wala question hai.

```java
// ❌ GALAT — sirf turant bacchon ko check karta hai
boolean isBSTWrong(TreeNode root) {
    if (root == null) return true;
    if (root.left != null && root.left.data > root.data) return false;
    if (root.right != null && root.right.data < root.data) return false;
    return isBSTWrong(root.left) && isBSTWrong(root.right);
}
```

**Ye kyun galat hai:**
```
        10
       /  \
      5    15
          /  \
         6    20      ← 6 galat jagah hai! (10 se chhota hai)
```
Upar wala code isko **valid** bata dega, kyunki `6 < 15` hai. Par `6` poore right subtree me hai — usse `10` se bada hona chahiye tha.

```java
// ✅ SAHI — range maintain karo
static boolean isValidBST(TreeNode root) {
    return validate(root, null, null);
}

static boolean validate(TreeNode node, Integer min, Integer max) {
    if (node == null) return true;

    if (min != null && node.data <= min) return false;
    if (max != null && node.data >= max) return false;

    return validate(node.left,  min, node.data)      // left ka max = mera data
        && validate(node.right, node.data, max);     // right ka min = mera data
}
```

👉 `Integer` (`int` nahi) use kiya taaki `null` = "koi limit nahi" bata sakein. `Integer.MIN_VALUE` use karoge to us value wale node pe bug aa jaayega.

**Alternative:** inorder traversal karo — agar **strictly increasing** hai to valid BST hai.

## Q7. BST me search / insert
**Answer:**

```java
static TreeNode search(TreeNode root, int key) {
    if (root == null || root.data == key) return root;

    return key < root.data ? search(root.left, key)
                           : search(root.right, key);
}

static TreeNode insert(TreeNode root, int key) {
    if (root == null) return new TreeNode(key);

    if (key < root.data)      root.left  = insert(root.left, key);
    else if (key > root.data) root.right = insert(root.right, key);
    // barabar → kuch mat karo (duplicates allowed nahi)

    return root;
}
```

👉 `root.left = insert(...)` — **return value wapas assign karna** zaroori hai. Ye pattern tree modification me hamesha lagta hai.

## Q8. Lowest Common Ancestor (LCA)
**Answer:** BST me bahut aasan hai:

```java
static TreeNode lcaBST(TreeNode root, int p, int q) {
    if (root == null) return null;

    if (p < root.data && q < root.data)  return lcaBST(root.left, p, q);
    if (p > root.data && q > root.data)  return lcaBST(root.right, p, q);

    return root;      // ek left me ek right me → yahi LCA hai
}
```

**Normal binary tree me** (BST nahi):
```java
static TreeNode lca(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) return root;

    TreeNode left  = lca(root.left, p, q);
    TreeNode right = lca(root.right, p, q);

    if (left != null && right != null) return root;    // dono taraf mile
    return left != null ? left : right;
}
```

## Q9. Tree mirror / invert karo
**Answer:** Famous question — sirf 4 line.

```java
static TreeNode invert(TreeNode root) {
    if (root == null) return null;

    TreeNode temp = root.left;
    root.left = invert(root.right);
    root.right = invert(temp);

    return root;
}
```

👉 Mazedaar fact: Homebrew ke creator ko Google ne isi question pe reject kar diya tha — uske baad ye internet pe famous ho gaya.

## Q10. Diameter of tree
**Answer:** Kisi bhi do nodes ke beech ka **sabse lamba path**.

```java
static int maxDiameter = 0;

static int diameter(TreeNode root) {
    maxDiameter = 0;
    depth(root);
    return maxDiameter;
}

static int depth(TreeNode node) {
    if (node == null) return 0;

    int left = depth(node.left);
    int right = depth(node.right);

    maxDiameter = Math.max(maxDiameter, left + right);   // is node se guzarne wala path

    return 1 + Math.max(left, right);                    // upar height return karo
}
```

⚠️ **Key insight:** diameter root se guzarna **zaroori nahi**. Isliye har node pe check karna padta hai. Function height return karta hai par diameter **side me** update karta hai — ye pattern bahut kaam aata hai.

## Q11. Tree traversal complexity
**Answer:**

| Operation | Balanced BST | Skewed BST | Binary Tree |
|---|---|---|---|
| Search | `O(log n)` | `O(n)` | `O(n)` |
| Insert | `O(log n)` | `O(n)` | `O(1)`* |
| Delete | `O(log n)` | `O(n)` | `O(n)` |
| Traversal | `O(n)` | `O(n)` | `O(n)` |

**Space:** recursion me `O(h)` — `h` = height. Balanced me `O(log n)`, skewed me `O(n)`.

---

> 💡 **Interview tip:** tree question me **hamesha `if (root == null) return ...` se shuru karo** — ye base case 90% questions me chahiye hota hai aur isse recursion khud clear ho jaata hai. Aur "valid BST" wale question me **min-max range wala** solution hi likhna — seedha parent-child compare karne wala solution galat hai, aur interviewer specifically wahi counter-example poochta hai.
