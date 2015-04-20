package stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * @author huhai
 * @date 2015/4/20 19:03
 */
public class HelloStream {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 1, null, 2, 3, 4, null, 5, 6, 7, 8, 9, 10);
        System.out.println("sum is:" + nums.stream().filter(num -> num != null).map((num) -> {
            //System.out.println("before distinct:" + num);
            return num * 2;
        }).distinct().
                peek(item -> System.out.println("consume:" + item)).map((item) -> {
            System.out.println("before skip:" + item);
            return item;
        }).skip(2).limit(4).collect(toList()).parallelStream().sorted(comparing(Integer::intValue).reversed()).collect(toList()));

        Stream<List<Integer>> s = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        System.out.println(s.flatMap(item -> item.stream()).collect(toList()));

        Arrays.asList("A", "B", "C").forEach(item -> System.out.print(item));
        System.out.println();

        System.out.println(Arrays.asList('A', 'B', 'C').stream().anyMatch(item -> item > 'Z'));

        Supplier<Integer> supplier = new Supplier<Integer>() {
            Random random = new Random();
            @Override
            public Integer get() {
                return random.nextInt(1000);
            }
        };

        Collection<Integer> integers = Stream.generate(supplier).limit(10).filter(item -> item % 2 == 0).collect(toList());
        System.out.println(integers);

        Stream.iterate("a", item -> item + ".").limit(5).forEach(item -> System.out.println(item));
    }
}
