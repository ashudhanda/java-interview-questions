# 12 — File I/O & Serialization 📁

## Q1. Difference between byte streams and character streams?
**Answer:** Byte streams (`InputStream`/`OutputStream`) handle raw binary data (images, files). Character streams (`Reader`/`Writer`) handle text with proper character encoding.

## Q2. What is the advantage of BufferedReader?
**Answer:** It buffers input in memory, reducing disk reads — much faster for large files. Also gives handy `readLine()`.

```java
try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
    String line;
    while ((line = br.readLine()) != null) System.out.println(line);
}
```

## Q3. What is try-with-resources?
**Answer:** Streams that implement `AutoCloseable` are closed automatically when the try block ends — no manual `finally { close(); }` needed (Java 7+).

## Q4. What is serialization?
**Answer:** Converting an object into a byte stream (to save to file / send over network). Class must implement `Serializable` (marker interface). Deserialization = reverse process.

## Q5. What is the role of `transient`?
**Answer:** `transient` fields are **skipped** during serialization (e.g. passwords, cached data). After deserialization they get default values (`null`, `0`, `false`).

## Q6. What is serialVersionUID?
**Answer:** A version number for the class. If it doesn't match during deserialization, you get `InvalidClassException`. Best practice: declare it explicitly.

## Q7. Scanner vs BufferedReader?
**Answer:** Scanner parses tokens (`nextInt()`, `nextLine()`) — convenient but slower. BufferedReader just reads text fast; parsing is manual. For competitive coding, BufferedReader wins.

## Q8. `File` vs `Path` (NIO.2) — which one to use?
**Answer:** `File` purana API hai (Java 1.0 se). Modern code me `java.nio.file.Path` + `Files` prefer karo — better error messages, symbolic link support, aur handy one-liners:
```java
List<String> lines = Files.readAllLines(Path.of("data.txt"));
```

## Q9. Serializable vs Externalizable?
**Answer:** `Serializable` me JVM sab kuch automatic karta hai (reflection-based, thoda slow). `Externalizable` me tumhe `writeExternal()` / `readExternal()` khud implement karne padte hain — full control over *kya* aur *kaise* serialize ho. Performance-critical cases me useful, warna `Serializable` kaafi hai.
