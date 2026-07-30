# 14 — JDBC Basics 🗄️

## Q1. What is JDBC?
**Answer:** Java Database Connectivity — a standard API to connect Java programs to relational databases and run SQL.

## Q2. What are the main steps to query a database?
**Answer:**
```java
try (Connection con = DriverManager.getConnection(url, user, pass);
     PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id = ?")) {
    ps.setInt(1, 5);
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) System.out.println(rs.getString("name"));
    }
}
```
1. Connection banao → 2. Statement prepare karo → 3. Execute → 4. ResultSet read → 5. Close (try-with-resources).

## Q3. Statement vs PreparedStatement?
**Answer:** PreparedStatement is precompiled, faster for repeated queries, and **prevents SQL injection** because parameters are bound, not concatenated. Always prefer it.

## Q4. What is SQL injection and how does JDBC prevent it?
**Answer:** Attack where user input alters your SQL (`"' OR '1'='1"`). PreparedStatement placeholders (`?`) treat input as pure data, never as SQL code.

## Q5. executeQuery() vs executeUpdate()?
**Answer:**
- `executeQuery()` → SELECT → returns `ResultSet`
- `executeUpdate()` → INSERT/UPDATE/DELETE → returns affected row count

## Q6. What is a transaction in JDBC?
**Answer:** A group of statements that succeed or fail together:
```java
con.setAutoCommit(false);
// ... multiple updates ...
con.commit();      // sab save
// error aaye to: con.rollback();
```

## Q7. What is a connection pool?
**Answer:** Creating connections is expensive. A pool (HikariCP etc.) keeps ready connections that are reused — apps borrow and return them instead of opening new ones.
