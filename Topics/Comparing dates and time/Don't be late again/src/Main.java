import java.time.LocalTime;
import java.util.Scanner;
import java.util.stream.Stream;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = Integer.parseInt(s.nextLine());
        final LocalTime time = LocalTime.parse("19:30").plusMinutes(30);

        Stream.generate(s::nextLine)
                .limit(n)
                .filter(str -> LocalTime.parse(str.split("\\s+")[1]).isAfter(time))
                .forEachOrdered(str -> System.out.println(str.split("\\s+")[0]));
    }
}