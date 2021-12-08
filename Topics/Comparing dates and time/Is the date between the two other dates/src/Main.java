import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        List<LocalDate> date = Arrays
                .stream(s.nextLine().split("\\s+"))
                .map(LocalDate::parse)
                .collect(Collectors.toList());

        LocalDate x = date.get(0);
        LocalDate m = date.get(1);
        LocalDate n = date.get(2);
        System.out.println(x.isAfter(m) && x.isBefore(n)
                        || x.isAfter(n) && x.isBefore(m));
    }
}