# 69 — SQL Interview Queries 🗄️

Java interviews me SQL almost hamesha poocha jaata hai — specially JOINs, GROUP BY, aur "second highest salary" jaisi classic queries.

## Setup — sample tables

```sql
-- employees
| id | name    | dept_id | salary | manager_id |
|----|---------|---------|--------|------------|
| 1  | Ashu    | 10      | 70000  | 3          |
| 2  | Rahul   | 10      | 60000  | 3          |
| 3  | Priya   | 20      | 90000  | NULL       |
| 4  | Sneha   | 20      | 85000  | 3          |

-- departments
| id | dept_name |
|----|-----------|
| 10 | IT        |
| 20 | HR        |
| 30 | Sales     |      ← koi employee nahi
```

## Q1. JOINs ke types
**Answer:** Ye Venn diagram wala question har baar aata hai:

| JOIN | Kya milta hai |
|---|---|
| `INNER JOIN` | Sirf **dono table me match** karne wale |
| `LEFT JOIN` | **Left ke saare** + right ke matches (na mile to NULL) |
| `RIGHT JOIN` | **Right ke saare** + left ke matches |
| `FULL OUTER JOIN` | **Dono ke saare** |
| `CROSS JOIN` | Har row × har row (cartesian) |
| `SELF JOIN` | Table khud se join |

```sql
-- Har employee ka department naam
SELECT e.name, d.dept_name
FROM employees e
INNER JOIN departments d ON e.dept_id = d.id;

-- Saare departments (jisme koi employee nahi bhi)
SELECT d.dept_name, e.name
FROM departments d
LEFT JOIN employees e ON e.dept_id = d.id;
-- Sales | NULL  ← dikh raha hai ki koi nahi hai
```

👉 **Interview tip:** LEFT JOIN ko "saare left wale, match ho ya na ho" bolo — mat seedha definition ratt ke.

## Q2. Second highest salary — CLASSIC
**Answer:** 4 tareeke, sab poochhe jaate hain:

```sql
-- 1. Subquery (sabse simple)
SELECT MAX(salary) FROM employees
WHERE salary < (SELECT MAX(salary) FROM employees);

-- 2. LIMIT + OFFSET (MySQL/Postgres)
SELECT DISTINCT salary FROM employees
ORDER BY salary DESC
LIMIT 1 OFFSET 1;

-- 3. DENSE_RANK (best — ties handle karta hai)
SELECT salary FROM (
    SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS rnk
    FROM employees
) ranked
WHERE rnk = 2;

-- 4. NOT EXISTS
SELECT salary FROM employees e1
WHERE 1 = (SELECT COUNT(DISTINCT salary) FROM employees e2
           WHERE e2.salary > e1.salary);
```

⚠️ **DENSE_RANK vs RANK vs ROW_NUMBER:**
- `ROW_NUMBER`: 1,2,3,4 — kabhi tie nahi
- `RANK`: 1,2,2,**4** — tie ke baad gap
- `DENSE_RANK`: 1,2,2,**3** — tie ke baad no gap ✅ salary questions me yahi

## Q3. Duplicate rows dhoondo
**Answer:**

```sql
-- Kaunse emails duplicate hain
SELECT email, COUNT(*) AS cnt
FROM users
GROUP BY email
HAVING COUNT(*) > 1;

-- Duplicates DELETE karo (ek copy rakho)
DELETE FROM users
WHERE id NOT IN (
    SELECT MIN(id) FROM users GROUP BY email
);
```

⚠️ **`WHERE` vs `HAVING`** — favourite question:
- `WHERE` = group banne se **pehle** filter
- `HAVING` = group banne ke **baad** filter (aggregates pe)

## Q4. Har department ka highest paid employee
**Answer:**

```sql
-- Window function se (modern)
SELECT name, dept_id, salary FROM (
    SELECT *, RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rnk
    FROM employees
) t
WHERE rnk = 1;

-- Correlated subquery se (old school)
SELECT * FROM employees e
WHERE salary = (
    SELECT MAX(salary) FROM employees
    WHERE dept_id = e.dept_id
);
```

👉 **`PARTITION BY`** = "GROUP BY jaisa, par rows khatam nahi hote" — har row ke saamne uska rank aa jaata hai.

## Q5. Employee + Manager naam (self join)
**Answer:**

```sql
SELECT e.name AS employee, m.name AS manager
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.id;
-- Same table do baar! Aliases e aur m zaroori hain
```

👉 **LEFT kyun?** Kyunki CEO ka `manager_id` NULL hai — INNER JOIN me CEO gayab ho jaayega. Ye catch interviewer dekhta hai.

## Q6. Nth highest salary (generic)
**Answer:**

```sql
SELECT DISTINCT salary FROM employees e1
WHERE N - 1 = (
    SELECT COUNT(DISTINCT salary) FROM employees e2
    WHERE e2.salary > e1.salary
);
-- N=3 → teesri highest
```

Ya window function: `WHERE rnk = N` (DENSE_RANK wala pattern).

## Q7. Departments jisme 2+ employees hain
**Answer:**

```sql
SELECT d.dept_name, COUNT(e.id) AS emp_count
FROM departments d
JOIN employees e ON e.dept_id = d.id
GROUP BY d.dept_name
HAVING COUNT(e.id) >= 2
ORDER BY emp_count DESC;
```

👉 **SQL execution order yaad rakho** — bahut poocha jaata hai:
`FROM → JOIN → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT`

## Q8. Aggregate functions quick list
**Answer:**

| Function | Kaam |
|---|---|
| `COUNT(*)` | saari rows (NULL bhi) |
| `COUNT(col)` | non-NULL values only |
| `COUNT(DISTINCT col)` | unique values |
| `SUM / AVG` | jodo / average |
| `MIN / MAX` | sabse chhota / bada |

⚠️ `COUNT(*)` vs `COUNT(col)` ka farak poochte hain — `COUNT(col)` NULLs **skip** karta hai.

## Q9. NULL handling ke traps
**Answer:**

```sql
-- ❌ Ye KUCH nahi dega — NULL = NULL kabhi true nahi hota
SELECT * FROM employees WHERE manager_id = NULL;

-- ✅ Sahi tareeka
SELECT * FROM employees WHERE manager_id IS NULL;

-- NULL ko default se replace
SELECT name, COALESCE(manager_id, 0) FROM employees;
```

⚠️ **Sabse bada trap:** `NULL = NULL` → NULL (true nahi!). Isliye hamesha `IS NULL` / `IS NOT NULL`.

## Q10. DELETE vs TRUNCATE vs DROP
**Answer:**

| | `DELETE` | `TRUNCATE` | `DROP` |
|---|---|---|---|
| Kya karta hai | Rows delete (WHERE lag sakta) | **Saari** rows | Poori **table** uda do |
| Rollback | ✅ | ❌ (zyada tar) | ❌ |
| Speed | Dheema | **Tez** ✅ | Tez |
| Auto-increment reset | Nahi | ✅ Haan | Table hi nahi bachi |

## Q11. ACID aur basic concepts
**Answer:** Interview me theory bhi aati hai:

- **A**tomicity — transaction poora ho ya bilkul na ho
- **C**onsistency — rules kabhi nahi tootne chahiye
- **I**solation — parallel transactions ek dusre ko na bigaadein
- **D**urability — commit ke baad crash me bhi data safe

**Index:** B-Tree jaisa structure — `WHERE`/`JOIN` columns pe lagao → search `O(log n)`. Har column pe mat lagao — writes slow ho jaati hain.

**Primary key:** unique + NOT NULL + ek hi table me ek. **Foreign key:** doosri table ki PK point karta hai — relationship banata hai.

---

> 💡 **Interview tip:** SQL question me pehle **output soch ke bolo** — *"is query se mujhe department-wise max salary chahiye, to GROUP BY dept_id lagega aur tie handle karne ke liye RANK use karunga."* Query likhne se pehle approach bolna dikhata hai tum query ratt ke nahi, soch ke likhte ho. Aur `= NULL` wali galti **kabhi mat karna** — `IS NULL` hi sahi hai.
