# HashMap Quick Facts

- Default capacity: 16, load factor: 0.75
- Bucket converts to tree at 8 entries (Java 8+)
- `null` keys allowed (one null key, many null values)
- Not thread-safe — use `ConcurrentHashMap`
