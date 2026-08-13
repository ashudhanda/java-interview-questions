# 60 — Graph: BFS & DFS 🕸️

Graph questions dikhne me daraavne lagte hain, par andar se sirf **BFS** aur **DFS** hi hote hain. Do patterns yaad kar lo — aadhe graph questions solve.

## Q1. Graph Java me kaise banate hain?
**Answer:** Adjacency list — har node ke saamne uske neighbors ki list.

```java
// n nodes, directed edges
Map<Integer, List<Integer>> graph = new HashMap<>();

// Ya arrays se (fastest)
List<List<Integer>> graph = new ArrayList<>();
for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

// Edge add karo
graph.get(u).add(v);            // directed: u → v
graph.get(v).add(u);            // undirected: dono taraf
```

👉 **Adjacency list vs matrix:**

| | List | Matrix |
|---|---|---|
| Space | `O(V + E)` ✅ | `O(V²)` |
| Edge check | `O(degree)` | `O(1)` ✅ |
| Kab use | Sparse graph (zyada tar) | Dense graph |

## Q2. BFS likho
**Answer:** Level by level — **queue** use hoti hai.

```java
static void bfs(List<List<Integer>> graph, int start) {
    boolean[] visited = new boolean[graph.size()];
    Queue<Integer> queue = new LinkedList<>();

    queue.offer(start);
    visited[start] = true;

    while (!queue.isEmpty()) {
        int node = queue.poll();
        System.out.print(node + " ");

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;    // ← DAALTE waqt mark karo!
                queue.offer(neighbor);
            }
        }
    }
}
```

⚠️ **Sabse common bug:** `visited` ko `poll()` ke baad mark karte hain — tab tak wo node queue me do baar ja chuka hota hai. **Daalte waqt hi** mark karo.

**Time:** `O(V + E)` | **Space:** `O(V)`

## Q3. DFS likho
**Answer:** Gehrai me jao — **recursion** (ya stack).

```java
static void dfs(List<List<Integer>> graph, int node, boolean[] visited) {
    visited[node] = true;
    System.out.print(node + " ");

    for (int neighbor : graph.get(node)) {
        if (!visited[neighbor]) {
            dfs(graph, neighbor, visited);
        }
    }
}
```

**Iterative version:**
```java
static void dfsIterative(List<List<Integer>> graph, int start) {
    boolean[] visited = new boolean[graph.size()];
    Deque<Integer> stack = new ArrayDeque<>();

    stack.push(start);
    while (!stack.isEmpty()) {
        int node = stack.pop();
        if (visited[node]) continue;
        visited[node] = true;
        System.out.print(node + " ");

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) stack.push(neighbor);
        }
    }
}
```

## Q4. BFS vs DFS — kab kya?
**Answer:**

| | BFS | DFS |
|---|---|---|
| Data structure | Queue | Stack / Recursion |
| Jaata hai | Level by level | Pehle gehrai me |
| Shortest path | ✅ **Yahi do** (unweighted) | ❌ |
| Cycle detect | ✅ | ✅ |
| Topological sort | ✅ (Kahn's) | ✅ |
| Space | `O(V)` — wide tree me zyada | `O(depth)` |
| Memory efficient | ❌ | ✅ deep graph me |

👉 **Simple rule:** shortest path / level by level → **BFS**. Exhaustive search / path exists? → **DFS**.

## Q5. Number of Islands — DFS on grid
**Answer:** Topic 49 me tha — ab samjho **kyun** kaam karta hai.

```java
static int numIslands(char[][] grid) {
    int count = 0;
    for (int i = 0; i < grid.length; i++)
        for (int j = 0; j < grid[0].length; j++)
            if (grid[i][j] == '1') {
                count++;
                dfs(grid, i, j);       // poora island "doobo" do
            }
    return count;
}

static void dfs(char[][] grid, int i, int j) {
    if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length
            || grid[i][j] != '1') return;

    grid[i][j] = '0';                  // visited mark
    dfs(grid, i+1, j); dfs(grid, i-1, j);
    dfs(grid, i, j+1); dfs(grid, i, j-1);
}
```

👉 **Grid = graph.** Har cell ek node hai, char neighbors uske edges. DFS se connected component ka count nikal jaata hai.

## Q6. Cycle detect karo (undirected graph)
**Answer:** DFS + parent track karo.

```java
static boolean hasCycle(List<List<Integer>> graph) {
    boolean[] visited = new boolean[graph.size()];
    for (int i = 0; i < graph.size(); i++) {
        if (!visited[i] && dfsCycle(graph, i, -1, visited)) return true;
    }
    return false;
}

static boolean dfsCycle(List<List<Integer>> graph, int node, int parent, boolean[] visited) {
    visited[node] = true;

    for (int neighbor : graph.get(node)) {
        if (!visited[neighbor]) {
            if (dfsCycle(graph, neighbor, node, visited)) return true;
        } else if (neighbor != parent) {
            return true;    // visited mila jo parent nahi hai → cycle!
        }
    }
    return false;
}
```

⚠️ **`parent` zaroori hai** — undirected graph me `A-B` edge hai to BFS me B se wapas A dikhega. Wo cycle nahi hai, wo wahi edge hai ulta. Parent skip karo.

## Q7. Cycle detect (directed graph) — 3 colors
**Answer:** Directed me alag tareeka chahiye:

```java
// 0 = white (unvisited), 1 = gray (in current path), 2 = black (done)
static boolean hasCycleDirected(List<List<Integer>> graph) {
    int[] color = new int[graph.size()];
    for (int i = 0; i < graph.size(); i++) {
        if (color[i] == 0 && dfsDirected(graph, i, color)) return true;
    }
    return false;
}

static boolean dfsDirected(List<List<Integer>> graph, int node, int[] color) {
    color[node] = 1;                           // gray — abhi process me

    for (int neighbor : graph.get(node)) {
        if (color[neighbor] == 1) return true; // gray mila → CYCLE
        if (color[neighbor] == 0 && dfsDirected(graph, neighbor, color)) return true;
    }

    color[node] = 2;                           // black — ho gaya
    return false;
}
```

👉 **Gray = current recursion stack me hai.** Agar koi gray node dobara mile — matlab back edge hai, cycle hai. Black = safe, us path me koi cycle nahi tha.

## Q8. Topological Sort (Kahn's Algorithm)
**Answer:** Course schedule type problems — kaun pehle, kaun baad me.

```java
static List<Integer> topologicalSort(int n, int[][] edges) {
    List<List<Integer>> graph = new ArrayList<>();
    int[] inDegree = new int[n];
    for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

    for (int[] edge : edges) {
        graph.get(edge[0]).add(edge[1]);
        inDegree[edge[1]]++;                    // kitne prerequisites hain
    }

    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < n; i++) {
        if (inDegree[i] == 0) queue.offer(i);  // no prerequisites → start
    }

    List<Integer> result = new ArrayList<>();
    while (!queue.isEmpty()) {
        int node = queue.poll();
        result.add(node);

        for (int neighbor : graph.get(node)) {
            inDegree[neighbor]--;
            if (inDegree[neighbor] == 0) queue.offer(neighbor);
        }
    }

    return result.size() == n ? result : new ArrayList<>();   // cycle → khaali
}
```

👉 **Result ka size `n` nahi hai** to cycle hai — topological order possible nahi. Ye hi **Course Schedule** ka solution hai.

## Q9. Shortest path in unweighted graph
**Answer:** **BFS hi hai** — level by level jaa raha hai, pehli baar pahunche wahi shortest.

```java
static int shortestPath(List<List<Integer>> graph, int src, int dest) {
    boolean[] visited = new boolean[graph.size()];
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{src, 0});     // {node, distance}
    visited[src] = true;

    while (!queue.isEmpty()) {
        int[] curr = queue.poll();
        int node = curr[0], dist = curr[1];

        if (node == dest) return dist;

        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.offer(new int[]{neighbor, dist + 1});
            }
        }
    }
    return -1;
}
```

⚠️ **BFS shortest kyun deta hai?** Kyunki level 0 pe source, level 1 pe 1-doori wale, level 2 pe 2-doori wale... **pehli baar** milne pe wahi shortest hai. DFS se pehli baar milna shortest **nahi** hota.

## Q10. Word Ladder type problems
**Answer:** BFS ka classic use.

```java
static int ladderLength(String begin, String end, List<String> wordList) {
    Set<String> dict = new HashSet<>(wordList);
    if (!dict.contains(end)) return 0;

    Queue<String> queue = new LinkedList<>();
    queue.offer(begin);
    int level = 1;

    while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            String word = queue.poll();
            if (word.equals(end)) return level;

            // har character position pe a-z try karo
            char[] chars = word.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char original = chars[j];
                for (char c = 'a'; c <= 'z'; c++) {
                    chars[j] = c;
                    String newWord = new String(chars);
                    if (dict.contains(newWord)) {
                        queue.offer(newWord);
                        dict.remove(newWord);    // visited mark
                    }
                }
                chars[j] = original;
            }
        }
        level++;
    }
    return 0;
}
```

👉 Har word = node, ek letter change = edge. BFS = shortest transformation sequence.

## Q11. Common graph patterns
**Answer:**

| Question | Approach |
|---|---|
| Shortest path (unweighted) | BFS |
| Connected components count | DFS/BFS loop |
| Cycle detection | DFS (3 colors) / Union-Find |
| Topological order | Kahn's BFS / DFS |
| All paths | DFS + backtracking |
| Level by level | BFS |
| Grid problems | DFS/BFS on cells |

---

> 💡 **Interview tip:** graph question me **pehle bolna** — *"ye unweighted graph hai, shortest path chahiye, to main BFS use karunga kyunki BFS level by level jaata hai aur pehli baar pahunchne pe shortest milta hai."* Graph banane se pehle yeh reasoning dikhana interviewer ko impress karta hai. Aur **visited mark karte waqt `queue.offer()` ke saath karo** — `poll()` ke baad nahi.
