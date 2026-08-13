# 64 — Trie (Prefix Tree) 🌲

Trie dikhta hai mushkil, hai bilkul aasan — **har node ek character, har path ek word.** Autocomplete, spell-check, dictionary — sab Trie pe chalte hain.

## Q1. Trie kya hai?
**Answer:** Tree jisme:
- Root khaali hota hai
- Har **node = ek character**
- Root se node tak ka **path = prefix**
- Node pe flag hota hai: "yahan word **khatam** hota hai"

```
Words: cat, car, card, care, dog

        root
       /    \
      c      d
      |      |
      a      o
     / \     |
    t   r    g*
       / \
      d*  e*
```

`*` = word end. `t` pe `*` nahi — kyunki "cat" word hai par "ca" nahi.

👉 **Space heavy** hai (har character ka node) par **search bahut tez** — `O(word length)`, dictionary size pe depend nahi karta.

## Q2. Trie implement karo
**Answer:**

```java
class TrieNode {
    TrieNode[] children = new TrieNode[26];    // a-z
    boolean isEndOfWord = false;
}

class Trie {
    private TrieNode root = new TrieNode();

    void insert(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) {
                curr.children[idx] = new TrieNode();
            }
            curr = curr.children[idx];
        }
        curr.isEndOfWord = true;
    }

    boolean search(String word) {
        TrieNode node = find(word);
        return node != null && node.isEndOfWord;
    }

    boolean startsWith(String prefix) {
        return find(prefix) != null;
    }

    private TrieNode find(String s) {
        TrieNode curr = root;
        for (char c : s.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) return null;
            curr = curr.children[idx];
        }
        return curr;
    }
}
```

**Complexity:**

| Operation | Time |
|---|---|
| Insert | `O(L)` — L = word length |
| Search | `O(L)` |
| Prefix search | `O(L)` |

👉 **HashMap se `O(L)` hi hota hai search** — par Trie ka asli fayda **prefix search** aur **autocomplete** me hai.

## Q3. `search()` vs `startsWith()` — farak
**Answer:**

```java
trie.insert("apple");

trie.search("apple");       // true  — poora word hai
trie.search("app");          // false — "app" word nahi hai (prefix hai)

trie.startsWith("app");     // true  — prefix to hai!
trie.startsWith("apl");     // false — koi word "apl" se start nahi hota
```

⚠️ **`isEndOfWord` flag hi farak hai.** `startsWith` me flag matter nahi karta — sirf path exist karna chahiye.

## Q4. Autocomplete — prefix se saare words
**Answer:** Prefix tak jao, phir DFS se saare words collect karo.

```java
List<String> autocomplete(String prefix) {
    List<String> result = new ArrayList<>();
    TrieNode node = find(prefix);
    if (node == null) return result;

    collect(node, new StringBuilder(prefix), result);
    return result;
}

void collect(TrieNode node, StringBuilder sb, List<String> result) {
    if (node.isEndOfWord) result.add(sb.toString());

    for (int i = 0; i < 26; i++) {
        if (node.children[i] != null) {
            sb.append((char) ('a' + i));
            collect(node.children[i], sb, result);
            sb.deleteCharAt(sb.length() - 1);    // backtrack
        }
    }
}
```

👉 **Prefix tak `O(L)`, phir subtree DFS** — efficient hai kyunki sirf relevant subtree explore hota hai.

## Q5. Word Search II (grid me words dhoondo)
**Answer:** Trie + backtracking — classic hard question.

```java
static List<String> findWords(char[][] board, String[] words) {
    // Step 1: Trie banao saare words ka
    TrieNode root = new TrieNode();
    for (String word : words) insert(root, word);

    List<String> result = new ArrayList<>();
    int rows = board.length, cols = board[0].length;

    // Step 2: har cell se DFS
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            dfs(board, i, j, root, result);
        }
    }
    return result;
}

static void dfs(char[][] board, int i, int j, TrieNode node, List<String> result) {
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) return;
    char c = board[i][j];
    if (c == '#' || node.children[c - 'a'] == null) return;

    node = node.children[c - 'a'];
    if (node.word != null) {
        result.add(node.word);
        node.word = null;    // duplicate avoid
    }

    board[i][j] = '#';       // visited mark
    dfs(board, i+1, j, node, result);
    dfs(board, i-1, j, node, result);
    dfs(board, i, j+1, node, result);
    dfs(board, i, j-1, node, result);
    board[i][j] = c;         // backtrack
}
```

👉 **Trie se pruning** — agar `"ca"` se koi word nahi hai to us direction me jaana hi nahi. Brute force se **100x faster**.

## Q6. Trie vs HashMap — kab kya?
**Answer:**

| | Trie | HashMap |
|---|---|---|
| Exact search | `O(L)` | `O(L)` |
| Prefix search | ✅ `O(L)` | ❌ `O(N)` — sab keys check karni padengi |
| Autocomplete | ✅ Natural fit | ❌ |
| Space | Zyada (har char ka node) | Kam ✅ |
| Sorted order | ✅ Inorder DFS | ❌ |
| Implementation | Mushkil | Aasan ✅ |

👉 **Rule:** sirf exact match chahiye → HashMap. Prefix/autocomplete/sorted order chahiye → Trie.

## Q7. Longest Common Prefix (Trie se)
**Answer:**

```java
static String longestCommonPrefix(String[] words) {
    Trie trie = new Trie();
    for (String w : words) trie.insert(w);

    StringBuilder prefix = new StringBuilder();
    TrieNode curr = trie.root;

    while (true) {
        int childCount = 0;
        int childIdx = -1;
        for (int i = 0; i < 26; i++) {
            if (curr.children[i] != null) { childCount++; childIdx = i; }
        }
        if (childCount != 1 || curr.isEndOfWord) break;
        prefix.append((char) ('a' + childIdx));
        curr = curr.children[childIdx];
    }
    return prefix.toString();
}
```

👉 **Jab tak sirf 1 child hai** — common prefix chal raha hai. Branch aate hi break. Word end aate hi bhi break.

## Q8. Replace Words (dictionary se)
**Answer:** Sentence ke words ko shortest root se replace karo.

```java
static String replaceWords(List<String> dictionary, String sentence) {
    Trie trie = new Trie();
    for (String word : dictionary) trie.insert(word);

    StringBuilder result = new StringBuilder();
    for (String word : sentence.split(" ")) {
        result.append(findRoot(trie, word)).append(" ");
    }
    return result.toString().trim();
}

static String findRoot(Trie trie, String word) {
    TrieNode curr = trie.root;
    for (int i = 0; i < word.length(); i++) {
        int idx = word.charAt(i) - 'a';
        if (curr.children[idx] == null) return word;   // no root found
        curr = curr.children[idx];
        if (curr.isEndOfWord) return word.substring(0, i + 1);  // shortest root
    }
    return word;
}
```

👉 `"the cattle was rattled by the battery"` dictionary `[cat, bat, rat]` → `"the cat was rat by the bat"`.

## Q9. Trie me delete kaise karein?
**Answer:** Thoda tricky hai — nodes delete karte waqt shared prefixes mat todo.

```java
boolean delete(String word) {
    return deleteHelper(root, word, 0);
}

boolean deleteHelper(TrieNode node, String word, int depth) {
    if (depth == word.length()) {
        if (!node.isEndOfWord) return false;    // word hai hi nahi
        node.isEndOfWord = false;
        return isEmpty(node);                   // true = is node ko delete karo
    }

    int idx = word.charAt(depth) - 'a';
    if (node.children[idx] == null) return false;

    boolean shouldDelete = deleteHelper(node.children[idx], word, depth + 1);
    if (shouldDelete) {
        node.children[idx] = null;
        return isEmpty(node) && !node.isEndOfWord;
    }
    return false;
}

boolean isEmpty(TrieNode node) {
    for (TrieNode child : node.children) if (child != null) return false;
    return true;
}
```

⚠️ **"car" delete karo to "card" tootna nahi chahiye** — sirf unshared nodes delete hote hain.

## Q10. Trie kab use karein — pehchan
**Answer:**

| Signal | Approach |
|---|---|
| "prefix se search" | Trie |
| "autocomplete / suggestions" | Trie |
| "saare words jo is grid me hain" | Trie + DFS |
| "shortest/longest common prefix" | Trie |
| "spell checker" | Trie |
| "XOR pair maximum" | Binary Trie (advanced) |
| Sirf exact match | HashMap (Trie overkill) |

---

> 💡 **Interview tip:** Trie question me **pehle HashMap wala approach bolo** — *"exact match ke liye HashMap kaafi hai, par prefix search ke liye Trie better hai kyunki..."* — ye dikhata hai tum **trade-offs** samajhte ho. Aur TrieNode me `HashMap<Character, TrieNode>` use karna (`TrieNode[26]` ki jagah) — Unicode support ke liye. Ye bolna extra marks dilata hai.
