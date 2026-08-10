// Optional basics: avoid null checks
import java.util.Optional;

public class OptionalDemo {
    public static void main(String[] args) {
        Optional<String> name = Optional.ofNullable(null);
        String result = name.orElse("Guest");
        System.out.println(result); // Guest

        Optional<String> present = Optional.of("Ashu");
        present.ifPresent(n -> System.out.println("Hello, " + n)); // Hello, Ashu
    }
}
