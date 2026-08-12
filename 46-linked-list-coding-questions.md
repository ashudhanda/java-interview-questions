# 46 — Linked List Coding Questions 🔗

Linked list questions me pointer manipulation hoti hai — ek line galat likhi to list toot jaati hai. In patterns ko haath se dry run karke samjho.

## Setup — Node class

```java
class Node {
    int data;
    Node next;
    Node(int data) { this.data = data; }
}
```

## Q1. Linked list reverse karo
**Answer:** Sabse zyada poocha jaane wala question. Teen pointers chahiye.

```java
static Node reverse(Node head) {
    Node prev = null, curr = head;

    while (curr != null) {
        Node nextTemp = curr.next;   // aage ka reference bachao
        curr.next = prev;            // arrow ulta karo
        prev = curr;                 // dono aage badhao
        curr = nextTemp;
    }
    return prev;                     // prev hi naya head hai
}
```

**Time:** `O(n)` | **Space:** `O(1)`

⚠️ `nextTemp` bachana **zaroori** hai. `curr.next = prev` karte hi aage ka link kho jaata hai — phir list ka baaki hissa gum.

**Recursive version** (bhi poocha jaata hai):
```java
static Node reverseRec(Node head) {
    if (head == null || head.next == null) return head;

    Node newHead = reverseRec(head.next);
    head.next.next = head;      // peeche wale ka arrow ulta
    head.next = null;
    return newHead;
}
```

## Q2. Middle element dhoondho
**Answer:** Fast & slow pointer — **ek hi pass** me.

```java
static Node findMiddle(Node head) {
    Node slow = head, fast = head;

    while (fast != null && fast.next != null) {
        slow = slow.next;           // 1 kadam
        fast = fast.next.next;      // 2 kadam
    }
    return slow;
}
```

**Logic:** fast double speed se chal raha hai — jab fast end pe pahuncha, slow beech me hoga.

⚠️ **Even length me do middle hote hain.** Ye code **dusra** middle deta hai. Pehla chahiye to condition `fast.next != null && fast.next.next != null` karo.

## Q3. Cycle detect karo aur cycle ka start dhoondho
**Answer:** Floyd's algorithm. Part 1 topic 42 me tha, ab **poora**:

```java
static Node detectCycleStart(Node head) {
    Node slow = head, fast = head;

    // Step 1: cycle hai ya nahi
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) break;
    }
    if (fast == null || fast.next == null) return null;   // cycle nahi hai

    // Step 2: head se aur meeting point se ek-ek kadam
    slow = head;
    while (slow != fast) {
        slow = slow.next;
        fast = fast.next;
    }
    return slow;   // yahi cycle ka start hai
}
```

👉 **Step 2 kyun kaam karta hai?** Maths hai: head se cycle start tak ki doori = meeting point se cycle start tak ki doori (cycle ke around jaate hue). Isliye dono ek saath chal ke exactly cycle start pe milte hain.

## Q4. Do sorted lists merge karo
**Answer:** **Dummy node** trick — ye pattern bahut kaam aata hai.

```java
static Node mergeSorted(Node a, Node b) {
    Node dummy = new Node(0);      // fake head
    Node tail = dummy;

    while (a != null && b != null) {
        if (a.data <= b.data) { tail.next = a; a = a.next; }
        else                  { tail.next = b; b = b.next; }
        tail = tail.next;
    }
    tail.next = (a != null) ? a : b;   // jo bacha hai wo attach kar do

    return dummy.next;             // asli head
}
```

👉 **Dummy node** se "pehla element special case" wala `if` likhne ki zaroorat hi nahi padti. Ye trick yaad rakho — aadhe linked list questions me use hoti hai.

## Q5. End se Nth node hatao
**Answer:** Do pointers, `n` ka gap.

```java
static Node removeNthFromEnd(Node head, int n) {
    Node dummy = new Node(0);
    dummy.next = head;
    Node fast = dummy, slow = dummy;

    for (int i = 0; i <= n; i++) fast = fast.next;   // n+1 aage bhejo

    while (fast != null) {          // dono saath chalao
        fast = fast.next;
        slow = slow.next;
    }
    slow.next = slow.next.next;     // skip kar do
    return dummy.next;
}
```

⚠️ Yahan **dummy node zaroori hai** — warna head hi delete karna pade to code toot jaayega (`slow` `null` ho jaata).

## Q6. Duplicates hatao (sorted list se)
**Answer:**

```java
static Node removeDuplicates(Node head) {
    Node curr = head;

    while (curr != null && curr.next != null) {
        if (curr.data == curr.next.data) {
            curr.next = curr.next.next;    // skip — curr aage mat badhao
        } else {
            curr = curr.next;
        }
    }
    return head;
}
```

⚠️ Delete karne ke baad `curr` ko **aage mat badhao** — teen same values ho sakti hain. Ye common bug hai.

## Q7. Palindrome linked list check karo
**Answer:** Teen steps — `O(1)` space me.

```java
static boolean isPalindrome(Node head) {
    if (head == null || head.next == null) return true;

    // 1. Middle dhoondho
    Node slow = head, fast = head;
    while (fast.next != null && fast.next.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }

    // 2. Second half reverse karo
    Node secondHalf = reverse(slow.next);

    // 3. Compare karo
    Node p1 = head, p2 = secondHalf;
    boolean result = true;
    while (p2 != null) {
        if (p1.data != p2.data) { result = false; break; }
        p1 = p1.next;
        p2 = p2.next;
    }

    slow.next = reverse(secondHalf);   // list wapas theek karo (achhi practice)
    return result;
}
```

👉 Stack use karke bhi ho sakta hai par wo `O(n)` space leta hai. Interviewer `O(1)` space maangega.

## Q8. Do lists ka intersection point?
**Answer:** Smart trick — dono pointers **dono lists** traverse karte hain.

```java
static Node getIntersection(Node a, Node b) {
    Node p1 = a, p2 = b;

    while (p1 != p2) {
        p1 = (p1 == null) ? b : p1.next;   // khatam hone pe dusri list pe
        p2 = (p2 == null) ? a : p2.next;
    }
    return p1;   // intersection ya null
}
```

**Logic:** dono pointers total `lenA + lenB` distance cover karte hain — isliye same time pe intersection pe pahunchte hain. Length calculate karne ki zaroorat hi nahi.

## Q9. `LinkedList` (Java) andar se kya hai?
**Answer:** **Doubly linked list** — har node me `prev` aur `next` dono hote hain. `List`, `Deque` aur `Queue` teeno implement karta hai.

⚠️ **Common galatfehmi:** log sochte hain `LinkedList` me insert `O(1)` hai. Actually `list.add(index, val)` **`O(n)`** hai — pehle us index tak **traverse** karna padta hai! `O(1)` sirf tab hai jab tumhare paas node ka reference already ho (iterator se).

Isi wajah se practically `ArrayList` almost hamesha better hai (topic 38 dekho).

## Q10. Linked list vs Array — kab kya?
**Answer:**

| | Array / ArrayList | Linked List |
|---|---|---|
| Access by index | `O(1)` ✅ | `O(n)` ❌ |
| Start me insert | `O(n)` | `O(1)` ✅ |
| End me insert | `O(1)` amortized | `O(1)` |
| Memory | Compact ✅ | Har node pe extra pointers |
| Cache locality | Achhi ✅ | Kharab ❌ |

👉 Modern CPUs pe **cache locality** itni matter karti hai ki `ArrayList` aksar theory ke against bhi jeet jaata hai.

---

> 💡 **Interview strategy:** linked list question me **hamesha pehle dry run karo** — 3-4 node ka diagram banao aur pointers move karke dikhao. Seedha code likhne se pointer galat ho jaata hai. Aur **dummy node** aur **fast-slow pointer** — ye do patterns 80% questions cover kar lete hain.
