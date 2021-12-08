import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        LocalDateTime dateTime1 = LocalDateTime.parse(s.nextLine());
        LocalDateTime dateTime2 = LocalDateTime.parse(s.nextLine());

        int limit = Integer.parseInt(s.nextLine());
        int match = 0;

        for (int i = 0; i < limit; i++) {
            LocalDateTime dateTime = LocalDateTime.parse(s.nextLine());

            if ((dateTime.isEqual(dateTime1) || dateTime.isAfter(dateTime1))
                && dateTime.isBefore(dateTime2) || dateTime.isBefore(dateTime1)
                && (dateTime.isAfter(dateTime2) || dateTime.isEqual(dateTime2))) {
                match++;
            }
        }

        System.out.println(match);
    }
}