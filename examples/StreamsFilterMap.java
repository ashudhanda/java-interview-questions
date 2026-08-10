// Streams API: filter + map + collect
import java.util.List;
import java.util.stream.Collectors;

public class StreamsFilterMap {
    public static void main(String[] args) {
        List<String> names = List.of("ashu", "ravi", "amit", "neha");
        List<String> result = names.stream()
                .filter(n -> n.startsWith("a"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println(result); // [ASHU, AMIT]
    }
}
