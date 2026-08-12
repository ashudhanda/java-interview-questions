# 47 — Stack & Queue Coding Questions 🦝

Stack ka use pehchan-na sabse zaroori hai. Jahan bhi **"pichhla element yaad rakhna hai"** ya **"nesting/matching"** ho — wahan stack lagta hai.

## Q1. Valid Parentheses
**Answer:** Sabse classic stack question.

```java
static boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();

    for (char c : s.toCharArray()) {
        if (c == '(' || c == '{' || c == '[') {
            stack.push(c);
        } else {
            if (stack.isEmpty()) return false;      // closing pehle aa gaya
            char open = stack.pop();

            if ((c == ')' && open != '(') ||
                (c == '}' && open != '{') ||
                (c == ']' && open != '[')) return false;
        }
    }
    return stack.isEmpty();     // kuch bacha to unmatched hai
}
```

⚠️ **Do edge cases** jo log bhool jaate hain:
1. `stack.isEmpty()` check karna — `")))"` pe crash ho jaayega
2. Aakhir me `stack.isEmpty()` return karna — `"((("` `true` de dega warna

👉 `Stack` class ki jagah `ArrayDeque` use kiya — wajah topic 38 me hai.

## Q2. Next Greater Element
**Answer:** **Monotonic stack** pattern — ye advanced hai par bahut poocha jaata hai.

```java
static int[] nextGreater(int[] arr) {
    int[] result = new int[arr.length];
    Arrays.fill(result, -1);
    Deque<Integer> stack = new ArrayDeque<>();    // indexes store karenge

    for (int i = 0; i < arr.length; i++) {
        // jitne bhi chhote elements wait kar rahe hain, unka answer mil gaya
        while (!stack.isEmpty() && arr[stack.peek()] < arr[i]) {
            result[stack.pop()] = arr[i];
        }
        stack.push(i);
    }
    return result;
}
```

`[4, 5, 2, 25]` → `[5, 25, 25, -1]`

**Time:** `O(n)` — har element ek baar push, ek baar pop hota hai.

👉 **Insight:** stack me hamesha **decreasing** order ke elements rehte hain — isliye "monotonic stack" kehte hain. Ye pattern "Daily Temperatures", "Stock Span", "Largest Rectangle in Histogram" sab me lagta hai.

## Q3. Min Stack — `O(1)` me minimum
**Answer:** Push/pop/top/getMin sab `O(1)` me. Trick: **do stacks**.

```java
class MinStack {
    private Deque<Integer> stack = new ArrayDeque<>();
    private Deque<Integer> minStack = new ArrayDeque<>();

    void push(int val) {
        stack.push(val);
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);          // naya minimum
        }
    }

    void pop() {
        int removed = stack.pop();
        if (removed == minStack.peek()) minStack.pop();
    }

    int top()    { return stack.peek(); }
    int getMin() { return minStack.peek(); }
}
```

⚠️ `val <= minStack.peek()` me `=` **zaroori** hai! Duplicate minimums ho to `<` likhne pe pop galat ho jaayega.

## Q4. Do stacks se queue banao
**Answer:** Classic "implement X using Y" question.

```java
class MyQueue {
    private Deque<Integer> input = new ArrayDeque<>();
    private Deque<Integer> output = new ArrayDeque<>();

    void push(int x) { input.push(x); }

    int pop() {
        shift();
        return output.pop();
    }

    int peek() {
        shift();
        return output.peek();
    }

    private void shift() {
        if (output.isEmpty()) {              // sirf khaali hone pe transfer
            while (!input.isEmpty()) output.push(input.pop());
        }
    }
}
```

👉 **Amortized `O(1)`** — har element zyada se zyada ek baar transfer hota hai. Agar har `pop()` pe transfer karte to `O(n)` ho jaata. Ye `if (output.isEmpty())` hi asli optimization hai.

## Q5. Do queues se stack banao
**Answer:** Ulta wala question.

```java
class MyStack {
    private Queue<Integer> q = new LinkedList<>();

    void push(int x) {
        q.offer(x);
        // naye element ko aage laane ke liye baaki sabko ghumao
        for (int i = 0; i < q.size() - 1; i++) {
            q.offer(q.poll());
        }
    }

    int pop()  { return q.poll(); }
    int top()  { return q.peek(); }
}
```

**Push `O(n)`, pop `O(1)`.** Ek hi queue se ho jaata hai — ye batana bonus point hai.

## Q6. Postfix expression evaluate karo
**Answer:**

```java
static int evalPostfix(String[] tokens) {
    Deque<Integer> stack = new ArrayDeque<>();

    for (String token : tokens) {
        switch (token) {
            case "+" -> stack.push(stack.pop() + stack.pop());
            case "*" -> stack.push(stack.pop() * stack.pop());
            case "-" -> { int b = stack.pop(); stack.push(stack.pop() - b); }
            case "/" -> { int b = stack.pop(); stack.push(stack.pop() / b); }
            default  -> stack.push(Integer.parseInt(token));
        }
    }
    return stack.pop();
}
```

⚠️ **Order ka trap:** `+` aur `*` me order matter nahi karta, par `-` aur `/` me karta hai! Pehla pop **dusra operand** hota hai. Ye galti bahut hoti hai.

## Q7. Stack recursion me kaise use hoti hai?
**Answer:** Recursion andar se **call stack** hi use karta hai. Isliye har recursive solution ko iterative banaya ja sakta hai — bas apna stack banana padta hai.

```java
// Recursive
void inorder(Node root) {
    if (root == null) return;
    inorder(root.left);
    print(root.data);
    inorder(root.right);
}

// Iterative — apna stack
void inorderIterative(Node root) {
    Deque<Node> stack = new ArrayDeque<>();
    Node curr = root;

    while (curr != null || !stack.isEmpty()) {
        while (curr != null) { stack.push(curr); curr = curr.left; }
        curr = stack.pop();
        print(curr.data);
        curr = curr.right;
    }
}
```

👉 **Kab iterative chahiye?** Jab depth bahut zyada ho aur `StackOverflowError` ka risk ho.

## Q8. Sliding Window Maximum (Deque ka best use)
**Answer:** Har window ka maximum — `O(n)` me.

```java
static int[] maxSlidingWindow(int[] arr, int k) {
    int[] result = new int[arr.length - k + 1];
    Deque<Integer> deque = new ArrayDeque<>();   // indexes, decreasing values

    for (int i = 0; i < arr.length; i++) {
        // window se bahar wale index hatao
        if (!deque.isEmpty() && deque.peekFirst() <= i - k) deque.pollFirst();

        // peeche se chhote elements hatao — wo kabhi max nahi banenge
        while (!deque.isEmpty() && arr[deque.peekLast()] <= arr[i]) deque.pollLast();

        deque.offerLast(i);

        if (i >= k - 1) result[i - k + 1] = arr[deque.peekFirst()];
    }
    return result;
}
```

👉 Yahan **dono taraf** se operations chahiye — isliye `Deque`. Brute force `O(n*k)` hota, ye `O(n)` hai.

## Q9. Stack overflow kab hota hai?
**Answer:** Recursion bahut gehri ho jaaye ya base case galat ho.

```java
void infinite(int n) {
    infinite(n - 1);      // base case hi nahi → StackOverflowError
}
```

Default JVM stack ~512KB-1MB hota hai, roughly **10,000-20,000** frames. Badhana ho to `-Xss2m` flag, par asli fix base case theek karna ya iterative likhna hai.

## Q10. Kaunsa question pe stack use karein — pehchan
**Answer:**

| Signal | Pattern |
|---|---|
| Brackets / nesting / matching | Stack |
| "Next greater/smaller element" | Monotonic stack |
| "Previous greater/smaller" | Monotonic stack (peeche se loop) |
| Undo / back button / history | Stack |
| Expression evaluation | Stack |
| Recursion ko iterative banana | Stack |
| Sliding window ka max/min | Deque |
| BFS / level order traversal | Queue |
| Task scheduling / rate limiting | Queue |

---

> 💡 **Interview tip:** "Next greater element" sunte hi **monotonic stack** bol dena. Zyadatar candidates nested loop se `O(n²)` likhte hain — `O(n)` stack solution seedha alag category me daal deta hai.
