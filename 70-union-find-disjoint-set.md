# 70 — Union-Find (Disjoint Set) 🤝

Graph ka ek advanced topic — par pattern simple hai. **"Kya ye do nodes connected hain?"** type ke questions ka sabse tez solution.

## Q1. Union-Find kya hai?
**Answer:** Ek data structure jo groups (sets) manage karta hai aur do kaam karta hai:

1. **Find(x)** — x kis group me hai? (group ka leader kaun?)
2. **Union(x, y)** — x aur y ke groups ko jodo

**Real life:** Shaadi me do families mili — ab dono ek family. Phir teesri family mili — sab ek. Union-Find batata hai *"kya Ravi aur Sonu same family ke hain?"* `O(1)` me (almost).

👉 Graph me DFS/BFS se bhi connectivity check ho sakti hai, par **edges ek-ek karke aate hon** to Union-Find much faster hai.

## Q2. Basic implementation
**Answer:** Har node ka **parent** track karo. Leader ka parent khud hota hai.

```java
class UnionFind {
    int[] parent;

    UnionFind(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;   // sab apne leader
    }

    int find(int x) {
        while (parent[x] != x) {      // jab tak leader na mile
            x = parent[x];
        }
        return x;
    }

    void union(int x, int y) {
        int leaderX = find(x);
        int leaderY = find(y);
        if (leaderX != leaderY) {
            parent[leaderX] = leaderY;   // X ka leader Y ke neeche
        }
    }

    boolean connected(int x, int y) {
        return find(x) == find(y);
    }
}
```

**Problem:** `find` `O(n)` tak ho sakta hai — chain lambi ho jaaye to. Isliye do optimizations aate hain.

## Q3. Optimization 1 — Path Compression
**Answer:** `find` karte waqt raaste ke sab nodes ko **seedha leader** se jod do. Agli baar `O(1)`.

```java
int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);   // ← recursion me hi shortcut bana do
    }
    return parent[x];
}
```

```
Pehle:   1 → 2 → 3 → 4 (leader)
Baad me: 1 → 4,  2 → 4,  3 → 4    (sab seedha leader pe!)
```

## Q4. Optimization 2 — Union by Rank
**Answer:** Chhote tree ko **bade tree ke neeche** lagao — height chhoti rehti hai.

```java
class UnionFind {
    int[] parent, rank;

    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return;

        if (rank[px] < rank[py])       parent[px] = py;
        else if (rank[px] > rank[py])  parent[py] = px;
        else { parent[py] = px; rank[px]++; }
    }
}
```

👉 **Dono optimizations saath me:** complexity **almost `O(1)`** ho jaati hai (inverse Ackermann — practically constant). Ye batana interview me impress karta hai.

## Q5. Number of Provinces — classic question
**Answer:** Kitne connected components hain?

```java
static int findCircleNum(int[][] isConnected) {
    int n = isConnected.length;
    UnionFind uf = new UnionFind(n);

    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++)
            if (isConnected[i][j] == 1) uf.union(i, j);

    int count = 0;
    for (int i = 0; i < n; i++)
        if (uf.find(i) == i) count++;      // jitne leaders, utne groups

    return count;
}
```

👉 **Leaders gino** — wahi components ka count hai.

## Q6. Redundant Connection — cycle dhoondo
**Answer:** Kaunsi edge hatane se tree ban jaayega?

```java
static int[] findRedundantConnection(int[][] edges) {
    UnionFind uf = new UnionFind(edges.length + 1);

    for (int[] edge : edges) {
        if (uf.connected(edge[0], edge[1])) {
            return edge;           // already connected — YE edge cycle bana rahi hai
        }
        uf.union(edge[0], edge[1]);
    }
    return new int[0];
}
```

👉 **Logic:** edge ke dono ends pehle se connected hain → wo edge **extra** hai. DFS se bhi hota hai par Union-Find zyada clean hai.

## Q7. Union-Find vs DFS/BFS — kab kya?
**Answer:**

| Situation | Best choice |
|---|---|
| Edges ek-ek karke aati hain (dynamic) | **Union-Find** ✅ |
| Poora graph pehle se pata hai | DFS/BFS bhi chalega |
| Sirf connectivity chahiye | Union-Find |
| Actual path chahiye | BFS/DFS (Union-Find path nahi deta) ❌ |
| Components ka count | Dono |
| Cycle detect (undirected) | Dono |

⚠️ Union-Find **path nahi** batata — sirf "connected hai ya nahi". Path chahiye to BFS (topic 60).

## Q8. Accounts Merge — advanced example
**Answer:** Same person ke emails jodo.

```java
static List<List<String>> accountsMerge(List<List<String>> accounts) {
    Map<String, String> emailToName = new HashMap<>();
    Map<String, String> parent = new HashMap<>();

    // Har email ka parent khud
    for (List<String> acc : accounts) {
        String name = acc.get(0);
        for (int i = 1; i < acc.size(); i++) {
            parent.putIfAbsent(acc.get(i), acc.get(i));
            emailToName.put(acc.get(i), name);
            union(parent, acc.get(1), acc.get(i));   // pehle email se jodo
        }
    }

    // Groups banao
    Map<String, TreeSet<String>> groups = new HashMap<>();
    for (String email : parent.keySet()) {
        String leader = find(parent, email);
        groups.computeIfAbsent(leader, k -> new TreeSet<>()).add(email);
    }

    // Result me naam jodo
    List<List<String>> result = new ArrayList<>();
    for (var entry : groups.entrySet()) {
        List<String> merged = new ArrayList<>();
        merged.add(emailToName.get(entry.getKey()));
        merged.addAll(entry.getValue());
        result.add(merged);
    }
    return result;
}
```

👉 Union-Find sirf `int[]` pe nahi — **`HashMap` se strings** pe bhi chalta hai. Ye flexibility dikhana accha hai.

## Q9. Kab Union-Find pehchano?
**Answer:**

| Signal | Matlab |
|---|---|
| "connected components kitne" | Union-Find / DFS |
| "edges ek-ek add hoti hain, kab cycle bane" | Union-Find |
| "accounts/records merge karo" | Union-Find |
| "dynamic connectivity queries" | Union-Find |
| MST (minimum spanning tree) | Kruskal = Union-Find |

## Q10. Complexity summary
**Answer:**

| Operation | Naive | + Path Compression + Rank |
|---|---|---|
| `find` | `O(n)` | **~O(1)** ✅ |
| `union` | `O(n)` | **~O(1)** ✅ |
| Space | `O(n)` | `O(n)` |

---

> 💡 **Interview tip:** connectivity question aaye to pehle bolo — *"main Union-Find use karunga with path compression aur union by rank, jisse har operation almost O(1) ho jaayega."* Template yaad rakho — `find` me path compression, `union` me rank. Aur ye zaroor bolo: *"Union-Find path nahi deta, sirf connectivity — agar actual path chahiye to BFS lagega."* Ye limitation jaanna hi senior-level soch hai.
